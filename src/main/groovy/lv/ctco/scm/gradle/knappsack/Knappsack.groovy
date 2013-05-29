/*
 * Copyright 2013 C.T.Co SIA, Brivibas street 48/50, Riga, Latvia. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lv.ctco.scm.gradle.knappsack

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.InputStreamBody
import org.apache.http.entity.mime.content.StringBody

import javax.net.ssl.SSLPeerUnverifiedException
import java.security.KeyStore

/**
 *
 * @(#)Knappsack.groovy
 *
 * @author Konstantin Zmanovsky <konstantin.zmanovsky@consultingeeks.com>
 *
 * This class provide means to connect to a Knappsack server and perform deployment of an
 * artifact. It handles authentication and SSL connections as well. All operations are
 * performed using plain HTTP requests.
 *
 */
class Knappsack {
    protected HTTPBuilder http

    public Knappsack(String serverUrl) {
        this.http = new HTTPBuilder(normalizeServerUrl(serverUrl))
    }

    /**
     * Configures given HTTPBuilder to use a user defined key store.
     *
     * @param keyStoreFile
     * @param http HTTPBuilder that should be reconfigured. @param password
     */
    public void switchToExternalKeyStore(File keyStoreFile, String password) {
        def keyStore = KeyStore.getInstance(KeyStore.defaultType)

        keyStoreFile.withInputStream {
            keyStore.load(it, password.toCharArray())
        }

        http.client.connectionManager.schemeRegistry.register(
                new Scheme("https", 443, new SSLSocketFactory(keyStore)))
    }

    /**
     * Performs web-based authentication on the server.
     *
     * @param userName User name.
     * @param password User password.
     *
     * @throws RuntimeException If authentication or connection failed.
     */
    public void authenticate(String userName, String password) {
        def postBody = ['action': 'verify', 'j_username': userName, 'j_password': password]

        try {
            http.post(path: '/j_spring_security_check',
                    body: postBody) { resp ->
                if (resp.status != 302) {
                    throw new RuntimeException("Incorrect login response code. Expected 302, returned ${resp.status}")
                }
                if (!isExpectedLocation(resp, '')) {
                    throw new RuntimeException("Authentication failed! Please check user name and password.");
                }

            }
        } catch (SSLPeerUnverifiedException e) {
            throw new RuntimeException("Unable to verify the SSL ceritificate of ${http.getUri()}. Possible reason:" +
                    " the certificate has not been added to a JDK trust store, or the added certificate has" +
                    " expired. To fix the problem you may download new certificate from the site, and add it to the" +
                    " trust store with the following command: keytool -importcert -alias 'cert_alias'" +
                    " -file <downloaded file> -keystore ${System.properties['java.home']}/lib/security/cacerts" +
                    " -storepass 'changeit'.", e)
        }
    }

    /**
     * Uploads a single artifact (e.g. IPA file) to Knappsack server. Convenience method that accepts full
     * artifact file path, parses its name and opens an InputStream.
     *
     * @param parentId Application id.
     * @param groupId Group id.
     * @param storageConfigurationId Storage configuration id.
     * @param versionName Application version.
     * @param recentChanges Changes in uploaded version in HTML format that will be shown to user.
     * @param filePath File path of the artifact.
     *
     * @throws RuntimeException If upload returned response code other than 302.
     */
    public void uploadArtifact(String parentId, String groupId, String storageConfigurationId, String versionName,
                               String recentChanges, String filePath) {
        File file = new File(filePath)
        InputStream stream = new FileInputStream(file)
        uploadArtifact parentId, groupId, storageConfigurationId, versionName, recentChanges,
                stream, file.name
    }

    /**
     * Uploads a single artifact (e.t. IPA file) to Knappsack server.
     *
     * @param parentId Application id.
     * @param groupId Group id.
     * @param storageConfigurationId Storage configuration id.
     * @param versionName Application version.
     * @param recentChanges Changes in uploaded version in HTML format that will be shown to user.
     * @param inputStream Input stream to read artifact binary date from.
     * @param fileName File name of the artifact.
     *
     * @throws RuntimeException If upload returned response code other than 302.
     */
    public void uploadArtifact(String parentId, String groupId, String storageConfigurationId, String versionName,
                               String recentChanges, InputStream inputStream, String fileName) {
        MultipartEntity multiPartContent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE)
        multiPartContent.addPart 'id', new StringBody('')
        multiPartContent.addPart 'parentId', new StringBody(parentId)
        multiPartContent.addPart 'groupId', new StringBody(groupId)
        multiPartContent.addPart 'editing', new StringBody('false')
        multiPartContent.addPart 'storageConfigurationId', new StringBody(storageConfigurationId)
        multiPartContent.addPart 'versionName', new StringBody(versionName)
        multiPartContent.addPart 'recentChanges', new StringBody(recentChanges)
        multiPartContent.addPart '_wysihtml5_mode', new StringBody('1')
        multiPartContent.addPart 'appFile', new InputStreamBody(inputStream, 'application/octet-stream', fileName)
        multiPartContent.addPart 'appState', new StringBody('GROUP_PUBLISH')

        http.request(Method.POST) { request ->
            uri.path = '/manager/uploadVersion'
            requestContentType = "multipart/form-data"
            request.entity = multiPartContent

            response.success = { resp ->
                if (resp.status != 302) {
                    throw new RuntimeException("Incorrect upload response code. " +
                            "Expected 302, returned ${resp.status}. " +
                            "Please check the server error logs for more details.")
                }
                if (!isExpectedLocation(resp, "manager/editApplication/${parentId}")) {
                    throw new RuntimeException("Error uploading artifact to the server. " +
                            "Response redirect URL is incorrect. Please check the server error logs for more details.")
                }
            }
        }
    }

    /**
     * Checks if Location element of the response is pointing to expected URL.
     *
     * @param resp HTTP response object.
     * @param expectedPath Expected path in Location element.
     * @return True if there is an expected location.
     */
    private boolean isExpectedLocation(resp, String expectedPath) {
        def uri = http.getUri().clone()
        uri.setPath(expectedPath)
        def locationHeader = resp.headers.find { it.name == 'Location' }
        locationHeader.value == uri.toString()
    }

    /**
     * Adds a trailing slash to the server URL. The trailing slash is required for
     * login validation to work properly
     *
     * @param serverUrl URL to process
     */
    private static String normalizeServerUrl(String serverUrl) {
        if (!serverUrl.endsWith('/')) {
            serverUrl += '/'
        }
        return serverUrl
    }

}
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

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction

/**
 *
 * @(#)KnappsackUploadTask.groovy
 *
 * @author Konstantin Zmanovsky <konstantin.zmanovsky@consultingeeks.com>
 *
 * The task manages the actual upload of an artifact to a Knappsack server.
 *
 */
class KnappsackUploadTask extends DefaultTask {
    KnappsackUploadTask() {
        this.description = 'Upload an artifact to a Knappsack server';
    }

    @SuppressWarnings(["GroovyUnusedDeclaration"])
    @TaskAction
    def upload() {
        validateConfiguration()
        ensureArtifactExists()
        loadWhatsNewFile()
        uploadFile()
    }

    protected void uploadFile() {
        String knappsackUrl = project.knappsack.url
        String applicationId = project.knappsack.applicationId
        String groupId = project.knappsack.groupId
        String storageId = project.knappsack.storageId
        String version = project.knappsack.version
        String whatsNew = project.knappsack.whatsNew
        File artifact = project.knappsack.artifactFile

        if (logger.isDebugEnabled()) {
            logger.debug("Uploading ${artifact.path} to ${knappsackUrl}" +
                    " [group id: ${groupId}, application id: ${applicationId}, storage id: ${storageId}" +
                    ", whats new: ${whatsNew}]")
        } else {
            logger.lifecycle("Uploading ${artifact.path} ${version} to ${knappsackUrl}")
        }

        Knappsack knappsack = new Knappsack(knappsackUrl)
        configureCustomKeyStore knappsack
        authenticate knappsack
        knappsack.uploadArtifact applicationId, groupId, storageId, version, whatsNew, artifact.absolutePath
    }


    protected void authenticate(Knappsack knappsack) {
        try {
            String userName = project.knappsack.userName
            String password = project.knappsack.password
            knappsack.authenticate userName, password
        } catch (IOException e) {
            throw new GradleException("Server communication problem occurred", e);
        }
    }

    protected void configureCustomKeyStore(Knappsack knappsack) {
        File keyStore = project.knappsack.keyStoreFile
        if (keyStore) {
            String password = project.knappsack.keyStorePassword
            if (!keyStore.exists()) {
                throw new GradleException("Custom key store file ${keyStore.absolutePath} does not exist")
            }
            knappsack.switchToExternalKeyStore(keyStore, password)
        }
    }

    protected void loadWhatsNewFile() {
        if (project.knappsack.whatsNewFile) {
            File whatsNewFile = project.knappsack.whatsNewFile
            if (!whatsNewFile.exists()) {
                throw new GradleException("What's new file ${whatsNewFile.absolutePath} does not exist")
            }
            if (!whatsNewFile.isFile()) {
                throw new GradleException("What's new file ${whatsNewFile.absolutePath} is not a regular file")
            }
            try {
                project.knappsack.whatsNew = whatsNewFile.text
            } catch (IOException e) {
                throw new GradleScriptException("Unable to read what's new file ${whatsNewFile.absolutePath}", e)
            }
        }
    }

    protected void validateConfiguration() {
        if (!project.knappsack.url) {
            failMissingProperty 'Knappsack server URL', 'knappsack.url', KnappsackPlugin.PROP_URL
        }
        if (!project.knappsack.userName) {
            failMissingProperty 'Knappsack user name', 'knappsack.userName', KnappsackPlugin.PROP_USER_NAME
        }
        if (!project.knappsack.password) {
            failMissingProperty 'Knappsack user password', 'knappsack.password', KnappsackPlugin.PROP_PASSWORD
        }
        if (!project.knappsack.applicationId) {
            failMissingProperty 'Knappsack application ID', 'knappsack.applicationId', KnappsackPlugin.PROP_APP_ID
        }
        if (!project.knappsack.groupId) {
            failMissingProperty 'Knappsack group ID', 'knappsack.groupId', KnappsackPlugin.PROP_GROUP_ID
        }
        if (!project.knappsack.storageId) {
            failMissingProperty 'Knappsack storage ID', 'knappsack.storageId', KnappsackPlugin.PROP_STORAGE_ID
        }
        //TODO: Read the project version if properties are not available
        if (!project.knappsack.version) {
            failMissingProperty 'Knappsack application version', 'knappsack.version', KnappsackPlugin.PROP_VERSION
        }
        //TODO: Perform automatic fallback to a default message containing some Gradle project data
        if (!project.knappsack.whatsNew && !project.knappsack.whatsNewFileName) {
            failMissingProperty 'Recent changes for the version', 'knappsack.whatsNew',
                    "${KnappsackPlugin.PROP_WHATS_NEW} or ${KnappsackPlugin.PROP_WHATS_NEW_FILE}"
        }
        if (project.knappsack.whatsNew && project.knappsack.whatsNewFileName) {
            throw new GradleException('Properties knappsack.whatsNew and knappsack.whatsNewFileName" +\n' +
                    '                    " can not be used together')
        }
        if (!project.knappsack.artifactFileName) {
            failMissingProperty 'Knappsack artifact file name', 'knappsack.artifact', KnappsackPlugin.PROP_ARTIFACT
        }
        if (project.knappsack.keyStoreFileName && !project.knappsack.keyStorePassword) {
            throw new GradleException('When custom key store for Knappsack https connection is defined, it\'s' +
                    ' password should also be specified (convention: knappsack.keystorePassword, property:' +
                    " ${KnappsackPlugin.PROP_KEY_STORE_PASSWORD}")
        }
    }

    protected void ensureArtifactExists() {
        File artifact = project.knappsack.artifactFile
        if (!artifact.exists()) {
            throw new GradleException("Artifact file ${artifact.absolutePath} does not exist")
        }
        if (!artifact.isFile()) {
            throw new GradleException("Artifact file ${artifact.absolutePath} is not a regular file")
        }

    }

    protected static void failMissingProperty(String name, String convention, String property) {
        throw new GradleException("${name} configuration (convention: ${convention}, " +
                "property: ${property}) is not specified")
    }

}

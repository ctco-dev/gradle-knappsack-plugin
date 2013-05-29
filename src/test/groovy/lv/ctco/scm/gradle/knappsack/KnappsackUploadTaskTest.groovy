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

import org.gmock.GMockController
import org.gradle.api.GradleException
import org.gradle.testfixtures.ProjectBuilder
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static groovy.test.GroovyAssert.shouldFail
import static org.gmock.GMock.constructor
import static org.testng.Assert.assertEquals

/**
 *
 * @(#)KnappsackUploadTaskTest.groovy
 *
 * @author Konstantin Zmanovsky <konstantin.zmanovsky@consultingeeks.com>
 *
 */
class KnappsackUploadTaskTest {
    def project
    GMockController gmc

    @BeforeMethod
    void setUp() {
        project = ProjectBuilder.builder().build()
        gmc = new GMockController()
    }

    @Test
    void testValidateConfigurationUrlUndefined() {
        def mockExtension = gmc.mock(KnappsackExtension, constructor(project))
        mockExtension.url.returns('').stub()

        gmc.play {
            project.extensions.add('knappsack', new KnappsackExtension(project))
            def ex = shouldFail(GradleException) {
                project.task('knappsackUpload', type: KnappsackUploadTask).validateConfiguration()
            }
            assertEquals ex.message, 'Knappsack server URL configuration (convention: knappsack.url,' +
                    ' property: knappsack.url) is not specified'
        }

    }

    @Test
    void testValidateConfigurationUserNameUndefined() {
        gmc.mock(KnappsackExtension, constructor(project)) {
            url.returns('url').stub()
            userName.returns('').stub()
        }

        gmc.play {
            project.extensions.add('knappsack', new KnappsackExtension(project))
            def ex = shouldFail(GradleException) {
                project.task('knappsackUpload', type: KnappsackUploadTask).validateConfiguration()
            }
            assertEquals ex.message, 'Knappsack user name configuration (convention: knappsack.userName,' +
                    ' property: knappsack.user.name) is not specified'
        }

    }

    @Test
    void testValidateConfigurationPasswordUndefined() {
        gmc.mock(KnappsackExtension, constructor(project)) {
            url.returns('url').stub()
            userName.returns('userName').stub()
            password.returns('').stub()
        }

        gmc.play {
            project.extensions.add('knappsack', new KnappsackExtension(project))
            def ex = shouldFail(GradleException) {
                project.task('knappsackUpload', type: KnappsackUploadTask).validateConfiguration()
            }
            assertEquals ex.message, 'Knappsack user password configuration (convention: knappsack.password,' +
                    ' property: knappsack.user.password) is not specified'
        }

    }

    @Test
    void testValidateConfigurationApplicationIdUndefined() {
        gmc.mock(KnappsackExtension, constructor(project)) {
            url.returns('url').stub()
            userName.returns('userName').stub()
            password.returns('password').stub()
            applicationId.returns('').stub()
        }

        gmc.play {
            project.extensions.add('knappsack', new KnappsackExtension(project))
            def ex = shouldFail(GradleException) {
                project.task('knappsackUpload', type: KnappsackUploadTask).validateConfiguration()
            }
            assertEquals ex.message, 'Knappsack application ID configuration (convention: knappsack.applicationId,' +
                    ' property: knappsack.app.id) is not specified'
        }

    }

    @Test
    void testValidateConfigurationGroupIdUndefined() {
        gmc.mock(KnappsackExtension, constructor(project)) {
            url.returns('url').stub()
            userName.returns('userName').stub()
            password.returns('password').stub()
            applicationId.returns('applicationId').stub()
            groupId.returns('').stub()
        }

        gmc.play {
            project.extensions.add('knappsack', new KnappsackExtension(project))
            def ex = shouldFail(GradleException) {
                project.task('knappsackUpload', type: KnappsackUploadTask).validateConfiguration()
            }
            assertEquals ex.message, 'Knappsack group ID configuration (convention: knappsack.groupId,' +
                    ' property: knappsack.group.id) is not specified'
        }

    }

    @Test
    void testValidateConfigurationStorageIdUndefined() {
        gmc.mock(KnappsackExtension, constructor(project)) {
            url.returns('url').stub()
            userName.returns('userName').stub()
            password.returns('password').stub()
            applicationId.returns('applicationId').stub()
            groupId.returns('groupId').stub()
            storageId.returns('').stub()
        }

        gmc.play {
            project.extensions.add('knappsack', new KnappsackExtension(project))
            def ex = shouldFail(GradleException) {
                project.task('knappsackUpload', type: KnappsackUploadTask).validateConfiguration()
            }
            assertEquals ex.message, 'Knappsack storage ID configuration (convention: knappsack.storageId,' +
                    ' property: knappsack.storage.id) is not specified'
        }

    }

    @Test
    void testValidateConfigurationVersionUndefined() {
        gmc.mock(KnappsackExtension, constructor(project)) {
            url.returns('url').stub()
            userName.returns('userName').stub()
            password.returns('password').stub()
            applicationId.returns('applicationId').stub()
            groupId.returns('groupId').stub()
            storageId.returns('storageId').stub()
            version.returns('').stub()
        }

        gmc.play {
            project.extensions.add('knappsack', new KnappsackExtension(project))
            def ex = shouldFail(GradleException) {
                project.task('knappsackUpload', type: KnappsackUploadTask).validateConfiguration()
            }
            assertEquals ex.message, 'Knappsack application version configuration (convention: knappsack.version,' +
                    ' property: knappsack.version) is not specified'
        }

    }

    @Test
    void testValidateConfigurationWhatsNewUndefined() {
        gmc.mock(KnappsackExtension, constructor(project)) {
            url.returns('url').stub()
            userName.returns('userName').stub()
            password.returns('password').stub()
            applicationId.returns('applicationId').stub()
            groupId.returns('groupId').stub()
            storageId.returns('storageId').stub()
            version.returns('version').stub()
            whatsNew.returns('').stub()
            whatsNewFileName.returns('').stub()
        }

        gmc.play {
            project.extensions.add('knappsack', new KnappsackExtension(project))
            def ex = shouldFail(GradleException) {
                project.task('knappsackUpload', type: KnappsackUploadTask).validateConfiguration()
            }
            assertEquals ex.message, 'Recent changes for the version configuration (convention: knappsack.whatsNew,' +
                    ' property: knappsack.whatsnew or knappsack.whatsnew.file) is not specified'
        }

    }

    @Test
    void testValidateConfigurationWhatsNewBothDefined() {
        gmc.mock(KnappsackExtension, constructor(project)) {
            url.returns('url').stub()
            userName.returns('userName').stub()
            password.returns('password').stub()
            applicationId.returns('applicationId').stub()
            groupId.returns('groupId').stub()
            storageId.returns('storageId').stub()
            version.returns('version').stub()
            whatsNew.returns('New version 1.2.3').stub()
            whatsNewFileName.returns('whatsnew.html').stub()
        }

        gmc.play {
            project.extensions.add('knappsack', new KnappsackExtension(project))
            def ex = shouldFail(GradleException) {
                project.task('knappsackUpload', type: KnappsackUploadTask).validateConfiguration()
            }
            assertEquals ex.message, 'Properties knappsack.whatsNew and knappsack.whatsNewFileName" +\n' +
                    '                    " can not be used together'
        }

    }

    @Test
    void testValidateConfigurationArtifactUndefined() {
        gmc.mock(KnappsackExtension, constructor(project)) {
            url.returns('url').stub()
            userName.returns('userName').stub()
            password.returns('password').stub()
            applicationId.returns('applicationId').stub()
            groupId.returns('groupId').stub()
            storageId.returns('storageId').stub()
            version.returns('version').stub()
            whatsNew.returns('New version 1.2.3').stub()
            whatsNewFileName.returns(null).stub()
            artifactFileName.returns('').stub()
        }

        gmc.play {
            project.extensions.add('knappsack', new KnappsackExtension(project))
            def ex = shouldFail(GradleException) {
                project.task('knappsackUpload', type: KnappsackUploadTask).validateConfiguration()
            }
            assertEquals ex.message, 'Knappsack artifact file name configuration (convention: knappsack.artifact,' +
                    ' property: knappsack.artifact) is not specified'
        }

    }

    @Test
    void testValidateConfigurationKeyStoreWithoutPassword() {
        gmc.mock(KnappsackExtension, constructor(project)) {
            url.returns('url').stub()
            userName.returns('userName').stub()
            password.returns('password').stub()
            applicationId.returns('applicationId').stub()
            groupId.returns('groupId').stub()
            storageId.returns('storageId').stub()
            version.returns('version').stub()
            whatsNew.returns('New version 1.2.3').stub()
            whatsNewFileName.returns(null).stub()
            artifactFileName.returns('application-1.2.3.ipa').stub()
            keyStoreFileName.returns('cacerts').stub()
            keyStorePassword.returns(null).stub()
        }

        gmc.play {
            project.extensions.add('knappsack', new KnappsackExtension(project))
            def ex = shouldFail(GradleException) {
                project.task('knappsackUpload', type: KnappsackUploadTask).validateConfiguration()
            }
            assertEquals ex.message, 'When custom key store for Knappsack https connection is defined, it\'s password' +
                    ' should also be specified (convention: knappsack.keystorePassword,' +
                    ' property: knappsack.keystore.password'
        }

    }

    @Test
    void testUploadFile() {
        def mockKeyStoreFile = gmc.mock(new File('cacerts'))
        mockKeyStoreFile.exists().returns(true)

        def mockArtifactFile = gmc.mock(new File(''))
        mockArtifactFile.absolutePath.returns('/home/user/app/application-1.2.3.ipa')

        gmc.mock(KnappsackExtension, constructor(project)) {
            url.returns('url')
            userName.returns('userName')
            password.returns('password')
            applicationId.returns('applicationId')
            groupId.returns('groupId')
            storageId.returns('storageId')
            version.returns('version')
            whatsNew.returns('New version 1.2.3')
            artifactFile.returns(mockArtifactFile)
            keyStoreFile.returns(mockKeyStoreFile)
            keyStorePassword.returns('qwerty')
        }
        gmc.mock(Knappsack, constructor('url')) {
            switchToExternalKeyStore(mockKeyStoreFile, 'qwerty')
            authenticate('userName', 'password')
            uploadArtifact('applicationId', 'groupId', 'storageId', 'version', 'New version 1.2.3',
                    '/home/user/app/application-1.2.3.ipa')
        }
        gmc.play {
            project.extensions.add('knappsack', new KnappsackExtension(project))
            project.task('knappsackUpload', type: KnappsackUploadTask).uploadFile()
        }
    }

    @Test
    void testLoadWhatsNewFile() {
        def mockWhatsNewFile = gmc.mock(new File('.')) {
            exists().returns(true)
            isFile().returns(true)
            text.returns('New version 1.2.3')
        }

        gmc.mock(KnappsackExtension, constructor(project)) {
            whatsNewFile.returns(mockWhatsNewFile).times(2)
            whatsNew.set('New version 1.2.3')
        }

        gmc.play {
            project.extensions.add('knappsack', new KnappsackExtension(project))
            project.task('knappsackUpload', type: KnappsackUploadTask).loadWhatsNewFile()
        }
    }

    @Test(expectedExceptions = [GradleException], expectedExceptionsMessageRegExp =
    'What\'s new file /tmp/whatsnew.html does not exist')
    void testLoadWhatsNewFileDoesNotExist() {
        def mockWhatsNewFile = gmc.mock(new File('.')) {
            exists().returns(false)
            absolutePath.returns('/tmp/whatsnew.html')
        }

        def mockExtension = gmc.mock(KnappsackExtension, constructor(project))
        mockExtension.whatsNewFile.returns(mockWhatsNewFile).times(2)

        gmc.play {
            project.extensions.add('knappsack', new KnappsackExtension(project))
            def task = project.task('knappsackUpload', type: KnappsackUploadTask)
            task.loadWhatsNewFile()
        }
    }

    @Test(expectedExceptions = [GradleException], expectedExceptionsMessageRegExp =
    'What\'s new file /tmp/whatsnew.html is not a regular file')
    void testLoadWhatsNewFileNotRegular() {
        def mockWhatsNewFile = gmc.mock(new File('.')) {
            exists().returns(true)
            isFile().returns(false)
            absolutePath.returns('/tmp/whatsnew.html')
        }

        def mockExtension = gmc.mock(KnappsackExtension, constructor(project))
        mockExtension.whatsNewFile.returns(mockWhatsNewFile).times(2)

        gmc.play {
            project.extensions.add('knappsack', new KnappsackExtension(project))
            def task = project.task('knappsackUpload', type: KnappsackUploadTask)
            task.loadWhatsNewFile()
        }
    }

}

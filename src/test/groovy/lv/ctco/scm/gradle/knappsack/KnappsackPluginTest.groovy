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

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals
import static org.testng.Assert.assertTrue

/**
 *
 * @(#)KnappsackPluginTest.groovy
 *
 * @author Konstantin Zmanovsky <konstantin.zmanovsky@consultingeeks.com>
 *
 */
class KnappsackPluginTest {

    Project project

    @BeforeClass
    def setUp() {
        project = ProjectBuilder.builder().build()
    }

    @Test(expectedExceptions = [GradleException], expectedExceptionsMessageRegExp =
    'Properties knappsack.whatsnew and knappsack.whatsnew.file can not be used together')
    void testBothWhatsNewParametersDefined() {
        project.ext['knappsack.whatsnew'] = 'What\'s new'
        project.ext['knappsack.whatsnew.file'] = '/tmp/whatsnew.html'

        KnappsackPlugin plugin = applyPlugin()

        plugin.validateProperties()
    }

    @Test
    void testReadConventionsFromPropertiesMostlyDefined() {
        project.ext['knappsack.url'] = 'http://server.com/path'
        project.ext['knappsack.user.name'] = 'userName'
        project.ext['knappsack.user.password'] = 'querty'
        project.ext['knappsack.app.id'] = '1'
        project.ext['knappsack.group.id'] = '2'
        project.ext['knappsack.storage.id'] = '3'
        project.ext['knappsack.version'] = '1.2.3'
        project.ext['knappsack.artifact'] = 'application.ipa'
        project.ext['knappsack.keystore.file'] = 'cacerts'
        project.ext['knappsack.keystore.password'] = 'azerty'

        KnappsackPlugin plugin = applyPlugin()
        plugin.readConventionsFromProperties()

        assertEquals project.knappsack.url, 'http://server.com/path'
        assertEquals project.knappsack.userName, 'userName'
        assertEquals project.knappsack.password, 'querty'
        assertEquals project.knappsack.applicationId, '1'
        assertEquals project.knappsack.groupId, '2'
        assertEquals project.knappsack.storageId, '3'
        assertEquals project.knappsack.version, '1.2.3'
        assertTrue project.knappsack.artifactFile.name.endsWith('application.ipa')
        assertTrue project.knappsack.keyStoreFile.name.endsWith('cacerts')
        assertEquals project.knappsack.keyStorePassword, 'azerty'
    }

    @Test
    void testReadConventionsFromPropertiesWhatsNewTest() {
        project.ext['knappsack.whatsnew'] = 'New version 1.2.3'

        KnappsackPlugin plugin = applyPlugin()
        plugin.readConventionsFromProperties()

        assertEquals project.knappsack.whatsNew, 'New version 1.2.3'

    }

    @Test
    void testReadConventionsFromPropertiesWhatsNewFileTest() {
        project.ext['knappsack.whatsnew.file'] = 'whatsNew.html'

        KnappsackPlugin plugin = applyPlugin()
        plugin.readConventionsFromProperties()

        assertTrue project.knappsack.whatsNewFile.name.endsWith('whatsNew.html')
    }

    private KnappsackPlugin applyPlugin() {
        project.apply plugin: KnappsackPlugin
        project.getPlugins().findPlugin(KnappsackPlugin)
    }
}

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
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 *
 * @(#)KnappsackPlugin.groovy
 *
 * @author Konstantin Zmanovsky <konstantin.zmanovsky@consultingeeks.com>
 *
 * This plug-in allows uploading of artifacts to Knappsack server using plain HTTP requests.
 *
 */
class KnappsackPlugin implements Plugin<Project> {
    static final String PROP_URL = 'knappsack.url'
    static final String PROP_USER_NAME = 'knappsack.user.name'
    static final String PROP_PASSWORD = 'knappsack.user.password'
    static final String PROP_APP_ID = 'knappsack.app.id'
    static final String PROP_GROUP_ID = 'knappsack.group.id'
    static final String PROP_STORAGE_ID = 'knappsack.storage.id'
    static final String PROP_VERSION = 'knappsack.version'
    static final String PROP_WHATS_NEW = 'knappsack.whatsnew'
    static final String PROP_WHATS_NEW_FILE = 'knappsack.whatsnew.file'
    static final String PROP_ARTIFACT = 'knappsack.artifact'
    static final String PROP_KEY_STORE_FILE_NAME = 'knappsack.keystore.file'
    static final String PROP_KEY_STORE_PASSWORD = 'knappsack.keystore.password'

    protected Project project

    @Override
    void apply(Project project) {
        this.project = project

        project.extensions.create('knappsack', KnappsackExtension, project)
        project.task('knappsackUpload', type: KnappsackUploadTask, group: 'Knappsack')

        // Initialize the plug-in immediately if the project is already executed.
        // Required for running from init scripts w/o build scripts.
        if (project.state.executed) {
            initialize()
        } else {
            project.afterEvaluate {
                initialize()
            }
        }
    }

    protected void initialize() {
        validateProperties()
        readConventionsFromProperties()
    }

    protected void validateProperties() {
        if (project.hasProperty(PROP_WHATS_NEW) && project.hasProperty(PROP_WHATS_NEW_FILE)) {
            throw new GradleException("Properties ${PROP_WHATS_NEW} and ${PROP_WHATS_NEW_FILE}" +
                    " can not be used together")
        }
    }

    /**
     * Reads properties and overrides (or sets) values of Gradle convention object.
     * Properties have precedence over the convention object to make a command line
     * configuration possible.
     */
    protected void readConventionsFromProperties() {
        if (project.hasProperty(PROP_URL)) {
            project.knappsack.url = project[PROP_URL]
        }
        if (project.hasProperty(PROP_USER_NAME)) {
            project.knappsack.userName = project[PROP_USER_NAME]
        }
        if (project.hasProperty(PROP_PASSWORD)) {
            project.knappsack.password = project[PROP_PASSWORD]
        }
        if (project.hasProperty(PROP_APP_ID)) {
            project.knappsack.applicationId = project[PROP_APP_ID]
        }
        if (project.hasProperty(PROP_GROUP_ID)) {
            project.knappsack.groupId = project[PROP_GROUP_ID]
        }
        if (project.hasProperty(PROP_STORAGE_ID)) {
            project.knappsack.storageId = project[PROP_STORAGE_ID]
        }
        if (project.hasProperty(PROP_VERSION)) {
            project.knappsack.version = project[PROP_VERSION]
        }
        if (project.hasProperty(PROP_WHATS_NEW)) {
            project.knappsack.whatsNew = project[PROP_WHATS_NEW]
        }
        if (project.hasProperty(PROP_WHATS_NEW_FILE)) {
            project.knappsack.whatsNewFileName = project.file(project.file(project[PROP_WHATS_NEW_FILE]))
        }
        if (project.hasProperty(PROP_ARTIFACT)) {
            project.knappsack.artifactFileName = project[PROP_ARTIFACT]
        }
        if (project.hasProperty(PROP_KEY_STORE_FILE_NAME)) {
            project.knappsack.keyStoreFileName = project[PROP_KEY_STORE_FILE_NAME]
        }
        if (project.hasProperty(PROP_KEY_STORE_PASSWORD)) {
            project.knappsack.keyStorePassword = project[PROP_KEY_STORE_PASSWORD]
        }
    }

}

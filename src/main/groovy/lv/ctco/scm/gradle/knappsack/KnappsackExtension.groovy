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

import org.gradle.api.Project

/**
 *
 * @(#)KnappsackExtension.groovy
 *
 * @author Konstantin Zmanovsky <konstantin.zmanovsky@consultingeeks.com>
 *
 */
class KnappsackExtension {
    Project project

    KnappsackExtension(Project project) {
        this.project = project
    }

    /**
     * Knappsack server URL
     */
    def String url

    /**
     * Knappsack user name
     */
    def String userName

    /**
     * Knappsack user password
     */
    def String password

    /**
     * Knappsack application id
     */
    def String applicationId

    /**
     * Knappsack group id
     */
    def String groupId

    /**
     * Knappsack storage id
     */
    def String storageId

    /**
     * Application version
     */
    def String version

    /**
     * Recent changes description
     */
    String whatsNew

    /**
     * Name of file containing recent changes description
     */
    String whatsNewFileName

    /**
     * File name of the artifact to upload
     */
    String artifactFileName

    /**
     * Key store to use instead of JVM default when connecting with SSL
     */
    def String keyStoreFileName

    /**
     * Key store password for the custom key store
     */
    def String keyStorePassword

    File getArtifactFile() {
        return artifactFileName ? project.file(artifactFileName) : null
    }

    File getKeyStoreFile() {
        return keyStoreFileName ? project.file(keyStoreFileName) : null
    }

    File getWhatsNewFile() {
        return whatsNewFileName ? project.file(whatsNewFileName) : null
    }

}
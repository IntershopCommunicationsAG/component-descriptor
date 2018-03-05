/*
 * Copyright 2018 Intershop Communications AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intershop.gradle.component.descriptor

/**
 * Dependency object is representation of Gradle dependency.
 *
 * @property group or organization of the dependency
 * @property module or name of the dedpency
 * @property version of the dependency
 * @constructor provides a dependency object in the component description.
 */
data class Dependency(
        val group: String,
        val module: String,
        val version: String
) {
    /**
     * This returns the string representation
     * of the dependency in format group:module:version.
     */
    override fun toString() : String {
        return "$group:$module:$version"
    }
}

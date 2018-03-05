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
 * This object provides a library of this component.
 *
 * @property dependency     dependency object of the library
 * @property targetName     name of the installed library
 * @property types          deployment or environment types (default is an empty set)
 */
data class Library @JvmOverloads constructor(
        val dependency: Dependency,
        val targetName: String,
        val types: MutableSet<String> = mutableSetOf()
        )

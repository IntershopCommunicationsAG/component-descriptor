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

import com.intershop.gradle.component.descriptor.items.OSSpecificItem

/**
 * Provides a property object of a component.
 *
 * @property file           relative file or directory path
 * @property permissions    string for file permissions
 *
 * @property classifier     OS specific usage of this file (default is an empty set)
 **/
@Suppress("unused")
class PermissionConfig @JvmOverloads constructor (
    val file: String,
    val permissions: String,

    override val classifier: String = ""
) : OSSpecificItem

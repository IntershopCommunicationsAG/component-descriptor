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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.intershop.gradle.component.descriptor.items.ComponentItem
import com.intershop.gradle.component.descriptor.items.DeploymentItem
import com.intershop.gradle.component.descriptor.items.OSSpecificItem
import com.intershop.gradle.component.descriptor.json.ContentTypeDeserializer

/**
 * This is the description of a directory configuration of
 * a component. The content type is per default DATA.
 *
 * @property dirPath           path of the directory in the component.
 *
 * @property classifier     OS specific usage of this file container (default is an empty string)
 * @property contentType    content type of this container (default value is 'STATIC')
 * @property updatable If this value is true, the item will be not part of an update installation.
 * @property types          deployment or environment types (default is an empty set)
 * @constructor provides a directory configuration object of the component
 **/
data class Directory @JvmOverloads constructor(

        val dirPath: String,

        override val classifier: String = "",

        @JsonDeserialize(using = ContentTypeDeserializer::class)
        override val contentType: ContentType = ContentType.DATA,

        override val updatable: Boolean = false,
        override val types: MutableSet<String> = mutableSetOf()

) : DeploymentItem, ComponentItem, OSSpecificItem

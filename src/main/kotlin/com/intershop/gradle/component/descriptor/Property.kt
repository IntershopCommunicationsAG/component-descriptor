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
import com.intershop.gradle.component.descriptor.json.ContentTypeDeserializer

/**
 * Provides a property object of a component.
 *
 * @property key property key of special deployment property
 * @property value property value of special deployment property
 * @property contentType    content type of this file (default value is 'STATIC')
 * @property types          deployment or environment types (default is an empty set)
 * @property classifiers    OS specific usage of this file (default is an empty set)
 * @constructor provides a configured property object
 */
data class Property @JvmOverloads constructor(
        val key: String,
        val value: String,

        @JsonDeserialize(using = ContentTypeDeserializer::class)
        val contentType: ContentType = ContentType.STATIC,

        val types: MutableSet<String> = mutableSetOf(),
        val classifiers: MutableSet<String> = mutableSetOf()
)

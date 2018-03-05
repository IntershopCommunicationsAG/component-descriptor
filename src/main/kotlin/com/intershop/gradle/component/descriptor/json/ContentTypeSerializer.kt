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
package com.intershop.gradle.component.descriptor.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.intershop.gradle.component.descriptor.ContentType

/**
 * Provides a custom serializer for ContentType.
 *
 * @constructor provides a configured serializer for ContentType
 */
class ContentTypeSerializer : StdSerializer<ContentType>(ContentType::class.java) {

    /**
     * Method that can be called to serialize
     * values of ContentType.
     *
     * @param contentType Value to serialize; can <b>not</b> be null.
     * @param generator Generator used to output resulting Json content
     * @param provider Provider that can be used to get serializers for
     *   serializing Objects value contains, if any.
     */
    override fun serialize(contentType: ContentType, generator: JsonGenerator, provider: SerializerProvider) {
        generator.writeString(contentType.name)
    }
}

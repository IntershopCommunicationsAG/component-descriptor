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

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.intershop.gradle.component.descriptor.ContentType
import org.slf4j.LoggerFactory

/**
 * Provides a custom deserializer for ContentType
 * If the content type can not be found in the enumeration,
 * UNSPECIFIED will be returned by deserialize method.
 */
class ContentTypeDeserializer : JsonDeserializer<ContentType>() {

    companion object {
        private val logger = LoggerFactory.getLogger(ContentTypeDeserializer::class.java.simpleName)
    }

    /**
     * This method is used to deserialize a JsonObject to ContenType enumeration.
     *
     * @param parser Parser used for reading JSON content of content type
     * @param context Context that can be used to access information about
     *   this deserialization activity.
     *
     * @return Deserialized value of ContentType
     */
    override fun deserialize(parser: JsonParser, context: DeserializationContext): ContentType {

        val jsonValue = parser.valueAsString
        var returnContentType: ContentType = ContentType.UNSPECIFIED

        try {
            returnContentType = ContentType.valueOf(jsonValue)
        } catch (ex: IllegalArgumentException) {
            logger.error("ContentType {} was not available for deserialization.", jsonValue)
        }

        return returnContentType
    }
}

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
package com.intershop.gradle.component.descriptor.util

import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.intershop.gradle.component.descriptor.Component
import com.intershop.gradle.component.descriptor.Dependency
import com.intershop.gradle.component.descriptor.MetaData
import com.intershop.gradle.component.descriptor.exceptions.ComponentReadException
import com.intershop.gradle.component.descriptor.exceptions.ComponentWriteException
import com.intershop.gradle.component.descriptor.exceptions.InvalidDescriptorVersionException
import java.io.File
import java.io.IOException

/**
 * Static methods for component descriptor handling.
 */
object ComponentUtil {

    /**
     * Write component descriptor to file.
     * An existing file will be replaced
     *
     * @param component configured descriptor of a component
     * @param file file for output
     */
    @Throws(ComponentWriteException::class)
    fun writeToFile(component: Component, file: File) {
        if(file.exists()) {
            file.delete()
        }
        val mapper: ObjectMapper = jacksonObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true)
        try {
            mapper.writeValue(file, component)
        } catch (ioex: IOException) {
            throw ComponentWriteException("It was not possible to write" +
                    " component to ${file.absolutePath}. (${ioex.message})")
        } catch (jsonEx: JsonGenerationException) {
            throw ComponentWriteException("The component can not be generated" +
                    " to ${file.absolutePath}. (${jsonEx.message})")
        } catch (mappingEx: JsonMappingException) {
            throw ComponentWriteException("Json mapping failed for component desriptor " +
                    " to ${file.absolutePath}. (${mappingEx.message})")
        }
    }

    /**
     * Reads a component from a file. The version
     * will be compared with the library specific version.
     *
     * @param file component descriptor file
     * @return component descriptor object
     */
    @Throws(InvalidDescriptorVersionException::class,
            ComponentReadException::class)
    fun componentFromFile(file: File): Component {
        val metadata = metadataFromFile(file)

        if(metadata.version != version) {
            throw InvalidDescriptorVersionException("The version of the provided File (${metadata.version}) is" +
                    " not compatible with the librars version ($version).")
        }

        val mapper: ObjectMapper = jacksonObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true)
        try {
            return mapper.readValue(file)
        } catch (ioex: IOException) {
            throw ComponentReadException("It was not possible to read" +
                    " a component from ${file.absolutePath}. (${ioex.message})")
        } catch (jsonex: JsonParseException) {
            throw ComponentReadException("It was not possible to parse" +
                    " a component from ${file.absolutePath}. (${jsonex.message})")
        } catch (mappingex: JsonMappingException) {
            throw ComponentReadException("Json mapping failed for" +
                    " component file ${file.absolutePath}. (${mappingex.message})")
        }
    }

    /**
     * The value will be read from a resources file.
     *
     * @property version describes version status of the descriptor
     */
    val version: String
        get() = this::class.java.getResource("/com/intershop/gradle/component/descriptor/util/version.info").readText()


    /**
     * Provides a new metadata object for serilization.
     *
     * @property metadata provides a meta data object the current time in
     * milli seconds and the version of the descriptor
     */
    fun metadata(group:String, module: String, version: String) : MetaData {
        return MetaData(System.currentTimeMillis(), this.version, Dependency(group, module, version))
    }

    /**
     * Reads data from meta data of the descriptor file.
     *
     * @param file  descriptor file of a component
     * @return meta data object from file
     */
    fun metadataFromFile(file: File): MetaData {
        val mapper: ObjectMapper = jacksonObjectMapper()
        val rootNode = mapper.readValue(file, JsonNode::class.java)

        val metadata = rootNode.get("metadata")

        return MetaData(
                metadata.get("creation").longValue(),
                metadata.get("version").textValue(),
                Dependency(
                    metadata.get("componentID")?.get("group")?.textValue() ?: "",
                    metadata.get("componentID")?.get("module")?.textValue() ?: "",
                    metadata.get("componentID")?.get("version")?.textValue() ?: ""))
    }
}

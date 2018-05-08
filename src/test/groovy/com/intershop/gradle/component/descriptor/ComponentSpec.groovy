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

import com.fasterxml.jackson.databind.ObjectMapper
import com.intershop.gradle.component.descriptor.util.ComponentUtil
import com.intershop.gradle.test.util.TestDir
import spock.lang.Specification

class ComponentSpec extends Specification {

    @TestDir
    File testProjectDir

    def "Test ContentType Serialization"() {
        when:
        String ctype = new ObjectMapper().writeValueAsString(ContentType.CONFIGURATION)

        then:
        ctype == '"CONFIGURATION"'
    }

    def "Test validate file"() {
        given:
        File fileV1 = new File(testProjectDir, "component.descr.v1")
        fileV1 << this.getClass().getResource( '/component.descr.v1' ).text

        when:
        boolean valid = ComponentUtil.INSTANCE.metadataFromFile(fileV1).version == ComponentUtil.INSTANCE.version

        then:
        valid == true
    }

    def "Test invalidate file"() {
        given:
        File fileV2 = new File(testProjectDir, "component.descr.v2")
        fileV2 << this.getClass().getResource( '/component.descr.v2' ).text

        when:
        boolean valid = ComponentUtil.INSTANCE.metadataFromFile(fileV2).version == ComponentUtil.INSTANCE.version

        then:
        valid == false
    }

    def "Test simple without any components descriptor"() {
        given:
        def file = new File(testProjectDir, "component.descr")
        def componentIn = new Component('TestComponent', "Component for tests")

        when:
        ComponentUtil.INSTANCE.writeToFile(componentIn, file)

        then:
        file.exists()

        when:
        def componentOut = ComponentUtil.INSTANCE.componentFromFile(file)

        then:
        componentIn == componentOut
    }

    def "Test simple with modules descriptor"() {
        given:
        def file = new File(testProjectDir, "component.descr")
        def componentIn = new Component('TestComponent', "Component for tests")
        componentIn.addJarModule('com.intershop.platform:core:14.0.0')
        componentIn.addJarModule('com.intershop.platform:isml:14.0.0')

        when:
        ComponentUtil.INSTANCE.writeToFile(componentIn, file)

        then:
        file.exists()

        when:
        def componentOut = ComponentUtil.INSTANCE.componentFromFile(file)

        then:
        componentIn == componentOut
    }

    def "Test simple with libs descriptor"() {
        given:
        def file = new File(testProjectDir, "component.descr")
        def componentIn = new Component('TestComponent', "Component for tests")
        componentIn.addLib('com.intershop.platform:core:14.0.0')
        componentIn.addLib('com.intershop.platform:isml:14.0.0')

        when:
        ComponentUtil.INSTANCE.writeToFile(componentIn, file)

        then:
        file.exists()

        when:
        def componentOut = ComponentUtil.INSTANCE.componentFromFile(file)

        then:
        componentIn == componentOut
        componentOut.libs.size() == 2
    }

    def "Test simple with file container descriptor"() {
        given:
        def file = new File(testProjectDir, "component.descr")
        def componentIn = new Component('TestComponent', "Component for tests")
        componentIn.addFileContainer(new FileContainer("cartridge", "component", "cartridge"))
        componentIn.addFileContainer(new FileContainer("share", "share", "share"))

        when:
        ComponentUtil.INSTANCE.writeToFile(componentIn, file)

        then:
        file.exists()

        when:
        def componentOut = ComponentUtil.INSTANCE.componentFromFile(file)

        then:
        componentIn == componentOut
        componentOut.fileContainers.size() == 2
    }

    def "Test simple with file item descriptor"() {
        given:
        def file = new File(testProjectDir, "component.descr")
        def componentIn = new Component('TestComponent', "Component for tests")
        componentIn.addFileItem(new FileItem("configuration", "properties", "share"))
        componentIn.addFileItem(new FileItem("configuration", "xml", "share"))

        when:
        ComponentUtil.INSTANCE.writeToFile(componentIn, file)

        then:
        file.exists()

        when:
        def componentOut = ComponentUtil.INSTANCE.componentFromFile(file)

        then:
        componentIn == componentOut
        componentOut.fileItems.size() == 2
    }

    def "Test simple with property item descriptor"() {
        given:
        def file = new File(testProjectDir, "component.descr")
        def componentIn = new Component('TestComponent', "Component for tests")
        componentIn.addProperty(new Property('com.intershop.test1', 'value1', "**/**/file.properties"))
        componentIn.addProperty(new Property('com.intershop.test1', 'value1', "**/**/file.properties"))
        componentIn.addProperty(new Property('com.intershop.test2', 'value2', "**/**/file.properties"))

        when:
        ComponentUtil.INSTANCE.writeToFile(componentIn, file)

        then:
        file.exists()

        when:
        def componentOut = ComponentUtil.INSTANCE.componentFromFile(file)

        then:
        componentIn == componentOut
        componentOut.properties.size() == 2
    }
}

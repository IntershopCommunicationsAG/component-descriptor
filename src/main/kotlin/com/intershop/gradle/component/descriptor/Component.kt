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

import com.intershop.gradle.component.descriptor.exceptions.InvalidTargetPathException
import com.intershop.gradle.component.descriptor.items.DeploymentItem
import com.intershop.gradle.component.descriptor.util.ComponentUtil

/**
 * This class provides a component descriptor
 * for component deployments.
 *
 * @property displayName display name of the component
 * @property componentDescription description of the component
 * @property types all supported types of this component
 * @property classifiers all supported classifiers/OS
 * @property modulesPath installation target of all specified modules
 * @property libsPath installation target of all component libraries
 * @property containerPath installation target of all containers
 * @property target preconfigured install target of the component
 * @property descriptorPath path for the descriptor storage
 * @property modules map of all modules of the component. The key describes the target path in the component.
 * @property libs map of all libraries. The key is the target name on the file system.
 * @property fileContainers a set of all available file containers (zip files)
 * @property fileItems a set of all available fileitems (will be used as there are)
 * @property linkItems a set of link configurations
 * @property directoryItems a set of directory paths
 * @property properties a set of properties, used for the configuration of the component
 * @property excludes a set of file pattern to exclude from installation
 * @property preserveExcludes      a set of file pattern to exclude files from preserve during update.
 * @property preserveIncludes      a set of file pattern to include files to preserve during update.
 * @property metadata metadata of the component (version and creation time)
* @constructor provides the configured component object
*/
@Suppress("unused")
data class Component @JvmOverloads constructor(
        val displayName: String,
        val componentDescription: String,

        override val types: MutableSet<String> = mutableSetOf(),
        val classifiers: MutableSet<String> = mutableSetOf(),

        val modulesPath: String = "modules",
        val libsPath: String = "libs",
        val containerPath: String = "",
        val target: String = "",

        val descriptorPath: String = "",

        val modules: MutableMap<String, Module> = mutableMapOf(),
        val libs: MutableMap<String, Library> = mutableMapOf(),
        val fileContainers: MutableSet<FileContainer> = mutableSetOf(),
        val fileItems: MutableSet<FileItem> = mutableSetOf(),
        val linkItems: MutableSet<Link> = mutableSetOf(),
        val directoryItems: MutableSet<Directory> = mutableSetOf(),
        val properties: MutableSet<Property> = mutableSetOf(),
        val permissionConfig: MutableSet<PermissionConfig> = mutableSetOf(),

        val excludes: MutableSet<String> = mutableSetOf(),
        val preserveExcludes: MutableSet<String> = mutableSetOf(),
        val preserveIncludes: MutableSet<String> = mutableSetOf(),

        val metadata: MetaData = ComponentUtil.metadata("","","")
) : DeploymentItem {

    companion object {
        /**
         * Provides a dependency object from string in the format group:module:version.
         *
         * @param value module dependeny as a single string
         */
        @Suppress("MagicNumber")
        @Throws(IllegalArgumentException::class)
        fun getDependencyFrom(value: String): Dependency {
            val depParts = value.split(":")
            if(depParts.size < 3) {
                throw IllegalArgumentException("Dependency string must have a group," +
                        " a module and aversion part. See 'group:module:version")
            } else {
                return Dependency( depParts[0],  depParts[1],  depParts[2])
            }
        }
    }

    /**
     * Add module to the module map.
     */
    @Suppress("unused")
    fun addModule(module: Module) {
        addModule(module.targetPath, module)
    }

    /**
     * Add module to the module map with a special target.
     */
    @Suppress("unused", "private")
    @Throws(InvalidTargetPathException::class)
    fun addModule(targetPath: String, module: Module) {
        if(modules.containsKey(targetPath)) {
            throw InvalidTargetPathException("TargetPath $targetPath is configured for an other module")
        }
        if(modules.keys.any { it.startsWith(targetPath) &&
                            targetPath.substring(it.length - 1).matches(".*/.+".toRegex()) }) {
            throw InvalidTargetPathException("Other modules are configured with the same targetPath $targetPath.")
        }

        modules[targetPath] = module
    }

    /**
     * Add module with a single jar.
     *
     * @param dependency Module dependency with format "group:module:version".
     */
    @Suppress("unused")
    @Throws(IllegalArgumentException::class)
    fun addJarModule(dependency: String) {
        val dep = getDependencyFrom(dependency)

        modules[dep.module] = Module(
                dep.module, dep.module, getDependencyFrom(dependency))
    }

    /**
     * Add library object to libraries set.
     */
    @Suppress("unused")
    fun addLib(library: Library) {
        libs[library.dependency.toString()] = library
    }

    /**
     * Add library to component.
     *
     * @param dependency dependency with format "group:module:version".
     */
    @Suppress("unused")
    @Throws(IllegalArgumentException::class)
    fun addLib(dependency: String) {
        val dep = getDependencyFrom(dependency)

        libs[dep.toString()] = Library(dep, "libs")
    }

    /**
     * Add package to set of packages.
     *
     * @param container data of a package
     * @return false if the package description is available in the list
     */
    @Suppress("unused")
    fun addFileContainer(container: FileContainer) : Boolean {
        return fileContainers.add(container)
    }

    /**
     * Add file item to set of files.
     *
     * @param fileItem data of a file item
     * @return false if the file item description is available in the list
     */
    @Suppress("unused")
    fun addFileItem(fileItem: FileItem) : Boolean {
        return fileItems.add(fileItem)
    }

    /**
     * Add a link configuration to the set of links.
     *
     * @param link link item configuration.
     * @return false if the link name is available in the list.
     */
    @Suppress("unused")
    fun addLinkItem(link: Link) : Boolean {
        return linkItems.add(link)
    }

    /**
     * Add a directory to the list of directories.
     *
     * @param directory data of a directory configuration.
     * @return false if directory item is available in the list.
     */
    @Suppress("unused")
    fun addDirectory(directory: Directory) : Boolean {
        return directoryItems.add(directory)
    }

    /**
     * Add property item to set of properties.
     *
     * @param property data of a property item
     * @return false if the property item description is available in the list
     */
    @Suppress("unused")
    fun addProperty(property: Property): Boolean {
        return properties.add(property)
    }

    /**
     * Add permission to the set of permissions.
     *
     * @param permissions Permission configuration for a file or directory
     * @return false if the configuration is part of the list
     */
    @Suppress("unused")
    fun addPermissionConf(permissionconf: PermissionConfig) : Boolean {
        return permissionConfig.add(permissionconf)
    }
}

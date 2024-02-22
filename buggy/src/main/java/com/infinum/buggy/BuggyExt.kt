package com.infinum.buggy

/**
 * Returns all resources of the specified type.
 *
 * @param T type of the resource
 * @receiver buggy instance
 * @return list of resources of the specified type
 */
inline fun <reified T : BuggyResource> Buggy.resources(): List<T> = resources.filterIsInstance<T>()

/**
 * Returns the resource of the specified type with the given [name].
 *
 * @param T type of the resource
 * @receiver buggy instance
 * @param name name of the resource
 * @return resource with the given name or null if not found
 */
inline fun <reified T : BuggyResource> Buggy.get(name: String): T? = resources<T>().firstOrNull { it.name == name }

/**
 * Returns a resource of the specified type with the given name.
 *
 * @param T type of the resource
 * @receiver buggy instance
 * @param type type of the resource
 * @param name name of the resource
 * @return resource with the given name or null if not found
 */
operator fun <T : BuggyResource> Buggy.get(type: Class<T>, name: String): T? =
    resources.filterIsInstance(type).firstOrNull { it.name == name }

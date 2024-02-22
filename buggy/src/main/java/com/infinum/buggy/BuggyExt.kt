package com.infinum.buggy

inline fun <reified T : BuggyResource> Buggy.resources(): List<T> = resources.filterIsInstance<T>()
inline fun <reified T : BuggyResource> Buggy.get(name: String): T? = resources<T>().firstOrNull { it.name == name }
operator fun <T : BuggyResource> Buggy.get(type: Class<T>, name: String): T? =
    resources.filterIsInstance(type).firstOrNull { it.name == name }

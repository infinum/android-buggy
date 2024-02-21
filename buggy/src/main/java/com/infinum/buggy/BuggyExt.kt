package com.infinum.buggy

inline fun <reified T> Buggy.get(): T? = resourcesOfType<T>().lastOrNull()
operator fun <T> Buggy.get(type: Class<T>): T? = resources.filterIsInstance(type).lastOrNull()

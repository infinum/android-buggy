package com.infinum.buggy.android

import com.infinum.buggy.Buggy
import com.infinum.buggy.get

/**
 * Returns a [JsonBuggyResource] with the given [name].
 * If the resource with the given [name] already exists, it is returned.
 * Otherwise, a new resource is created and added to the [Buggy].
 *
 * @receiver [Buggy] instance
 * @param name name of the resource
 * @return [JsonBuggyResource] with the given [name]
 */
fun Buggy.json(name: String) = get(JsonBuggyResource::class.java, name) ?: JsonBuggyResource(name).apply { add(this) }

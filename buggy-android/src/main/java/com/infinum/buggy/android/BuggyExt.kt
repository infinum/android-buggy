package com.infinum.buggy.android

import com.infinum.buggy.Buggy
import com.infinum.buggy.get

fun Buggy.json(name: String) = get(JsonBuggyResource::class.java) ?: JsonBuggyResource(name).apply { add(this) }

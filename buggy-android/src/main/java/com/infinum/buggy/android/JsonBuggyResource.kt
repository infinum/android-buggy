package com.infinum.buggy.android

import com.infinum.buggy.BuggyResource
import java.io.InputStream
import java.nio.charset.StandardCharsets
import org.json.JSONObject

/**
 * Represents a JSON resource that can be updated.
 *
 * @property name the name of the resource
 */
class JsonBuggyResource(
    override val name: String
) : BuggyResource {

    private val data = JSONObject()

    fun update(block: JSONObject.() -> Unit) {
        data.block()
    }

    override fun openStream(): InputStream = data.toString(2).byteInputStream(StandardCharsets.UTF_8)
}

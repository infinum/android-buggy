package com.infinum.buggy.resources

import com.infinum.buggy.BuggyResource
import java.io.OutputStream
import java.io.Writer
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

fun OutputStream.write(resource: BuggyResource) = resource.openStream().copyTo(this)
fun Writer.write(resource: BuggyResource, charset: Charset = StandardCharsets.UTF_8) =
    resource.openStream().bufferedReader(charset).copyTo(this)

package com.infinum.buggy.android

import android.os.Build
import com.infinum.buggy.BuggyResource
import java.io.InputStream

class DeviceInfoBuggyResource(
    override val name: String = "device-info.json",
) : BuggyResource {

    private val json = JsonBuggyResource(name)

    override fun openStream(): InputStream {
        json.update {
            put("Manufacturer", Build.MANUFACTURER)
            put("Brand", Build.BRAND)
            put("Model", Build.MODEL)
            put("Device", Build.DEVICE)
            put("OS", Build.VERSION.SDK_INT.toString())
            put("OS version", Build.VERSION.RELEASE)
            put("Product", Build.PRODUCT)
            put("Type", Build.TYPE)
            put("User", Build.USER)
            put("Host", Build.HOST)
            put("Fingerprint", Build.FINGERPRINT)
            put("ID", Build.ID)
            put("Time", Build.TIME)
            put("Tags", Build.TAGS)
            put("Bootloader", Build.BOOTLOADER)
            put("Board", Build.BOARD)
            put("Hardware", Build.HARDWARE)
            put("Display", Build.DISPLAY)
        }
        return json.openStream()
    }
}

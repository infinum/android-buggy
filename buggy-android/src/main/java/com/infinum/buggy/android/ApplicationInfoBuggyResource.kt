package com.infinum.buggy.android

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.infinum.buggy.BuggyResource
import java.io.InputStream

/**
 * Buggy resource that provides information about the application.
 * It includes package name, version name, version code, first install time, last update time, among other things.
 *
 * @param context Context of the application.
 * @property name Name of the resource. Default value is "application-info.json".
 */
class ApplicationInfoBuggyResource(
    context: Context,
    override val name: String = "application-info.json",
) : BuggyResource {

    private val context = context.applicationContext

    /**
     * Opens the resource's stream.
     */
    override fun openStream(): InputStream {
        val packageInfo = getPackageInfo()
        return JsonBuggyResource(name).update {
            put("Package name", context.packageName)
            put("Version name", packageInfo?.versionName.orEmpty())
            put("Version code", packageInfo?.longVersionCode.toString())
            put("First install time", packageInfo?.firstInstallTime)
            put("Last update time", packageInfo?.lastUpdateTime)
            put("Compatible with limit Dp", context.applicationInfo.compatibleWidthLimitDp)
            put("Largest width limit Dp", context.applicationInfo.largestWidthLimitDp)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                put("Compile SDK version", context.applicationInfo.compileSdkVersion)
            }
            put("Min SDK version", context.applicationInfo.minSdkVersion)
            put("Target SDK version", context.applicationInfo.targetSdkVersion)
            put("Split names", context.applicationInfo.splitNames?.joinToString(", "))
        }.openStream()
    }

    private fun getPackageInfo(): PackageInfo? = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.PackageInfoFlags.of(0),
            )
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(context.packageName, 0)
        }
    } catch (@Suppress("SwallowedException") e: PackageManager.NameNotFoundException) {
        null
    }
}

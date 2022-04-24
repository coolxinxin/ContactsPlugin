package com.leos.contactsplugin.contacts_plugin

import android.content.Intent
import io.flutter.plugin.common.PluginRegistry

/**
 * @author: Leo
 * @time: 2022/4/24
 * @desc:
 */
class ContactResult : ContactResultListener {

    private val listeners by lazy { mutableListOf<PluginRegistry.ActivityResultListener>() }

    override fun addActivityResultListener(listener: PluginRegistry.ActivityResultListener) =
        listeners.add(listener)

    override fun removeActivityResultListener(listener: PluginRegistry.ActivityResultListener) =
        listeners.remove(listener)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return listeners.any { it.onActivityResult(requestCode, resultCode, data) }
    }

}
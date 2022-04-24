package com.leos.contactsplugin.contacts_plugin

import io.flutter.plugin.common.PluginRegistry

/**
 * @author: Leo
 * @time: 2022/4/24
 * @desc:
 */
interface ContactResultListener : PluginRegistry.ActivityResultListener {

    fun removeActivityResultListener(listener: PluginRegistry.ActivityResultListener): Boolean

    fun addActivityResultListener(listener: PluginRegistry.ActivityResultListener): Boolean
}
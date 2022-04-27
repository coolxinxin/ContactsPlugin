package com.leos.contactsplugin.contacts_plugin

import android.annotation.SuppressLint
import android.app.Activity
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.text.SimpleDateFormat

/** ContactsPlugin */
class ContactsPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private var activity: Activity? = null
    private var binding: ActivityPluginBinding? = null
    private var listener = ContactResult()
//    private var threadPoolExecutor: ThreadPoolExecutor? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "contacts_plugin")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
            "selectContact" -> ContactSelect.selectContact(activity, result, listener)
            "getAllContacts" -> getAllContacts(result)
            else -> result.notImplemented()
        }
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
        binding.addActivityResultListener(listener)
        this.binding = binding
//        threadPoolExecutor =
//            ThreadPoolExecutor(5, 20, 60, TimeUnit.SECONDS, LinkedBlockingQueue(200))
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivity() {
        binding?.removeActivityResultListener(listener)
        binding = null
        activity = null
//        threadPoolExecutor = null
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    private fun getAllContacts(result: Result) {
//        threadPoolExecutor?.execute {
        val list = ArrayList<HashMap<String, String>>()
        activity?.apply {
            val contacts = ContactUtils.getAllContacts(this)
            contacts.forEach {
                val hashMap = HashMap<String, String>()
                hashMap["other_name"] = it.name
//                if (it.number != null && it.number.size > 0) {
//                    hashMap["other_mobile"] = it.number[0].number
//                }
                hashMap["phones"] = it.number
                hashMap["last_time"] = formatTime(it.lastUpdate)
                list.add(hashMap)
            }
        }
        result.success(list)
//        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatTime(date: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.format(date)
    }

}

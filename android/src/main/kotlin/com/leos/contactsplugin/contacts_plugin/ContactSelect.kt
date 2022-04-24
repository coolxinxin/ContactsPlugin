package com.leos.contactsplugin.contacts_plugin

import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

/**
 * @author: Leo
 * @time: 2022/4/23
 * @desc:
 */
class ContactSelect private constructor(
    private val activity: Activity?,
    private val result: MethodChannel.Result,
    private val listener: ContactResultListener,
) : PluginRegistry.ActivityResultListener {

    companion object {

        const val CONTACT_CODE = 0

        fun selectContact(
            activity: Activity?,
            result: MethodChannel.Result,
            listener: ContactResultListener,
        ) = ContactSelect(activity, result, listener).selectContact()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        when (requestCode) {
            CONTACT_CODE -> queryContact(data)
            else -> return false
        }
        return true
    }

    private fun selectContact() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        listener.addActivityResultListener(this)
        activity?.startActivityForResult(intent, CONTACT_CODE)
    }

    private fun queryContact(data: Intent?) {
        val uri = data?.data ?: return
        var number = ""
        var name = ""
        val cursor = activity?.contentResolver?.query(uri,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME),
            null, null, null)
        cursor?.apply {
            while (moveToNext()) {
                number = getString(0)
                name = getString(1)
            }
            close()
        }
        val hashMap = HashMap<String, String>()
        hashMap["name"] = name
        hashMap["number"] = number
        result.success(hashMap)
        listener.removeActivityResultListener(this)
    }
}
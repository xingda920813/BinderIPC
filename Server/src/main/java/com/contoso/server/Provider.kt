package com.contoso.server

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle

/**
 * https://www.zhihu.com/question/37929529
 */
class Provider : ContentProvider() {

    override fun onCreate() = true

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? =
        null

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?) = 0

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?) = 0

    @Suppress("UnnecessaryVariable")
    override fun call(authority: String, method: String, arg: String?, extras: Bundle?): Bundle? {
        val reply = Bundle()
        when (method) {
            "sayHello" -> {
                val firstPerson = arg
                val secondPerson = extras?.getString("secondPerson")
                reply.putString("reply", "Hello, $firstPerson and $secondPerson")
            }
            "getProtocol" -> {
                val protocol = ProtocolImpl()
                reply.putBinder("protocol", protocol)
            }
        }
        return reply
    }
}

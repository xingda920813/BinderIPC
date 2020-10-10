package com.contoso.server

import android.os.Bundle
import android.util.Log
import com.contoso.lib.IGetPackageSizeCallback
import com.contoso.lib.IProtocol
import com.contoso.lib.Person
import java.io.IOException
import java.util.*

class ProtocolImpl : IProtocol.Stub() {

    override fun basicTypes(i: Int, s: String?, cs: CharSequence?, a: IntArray?, l: MutableList<String>, m: MutableMap<Any?, Any?>?): String {
        Log.d(TAG, "basicTypes() called with: i = $i, s = $s, cs = $cs, a = ${Arrays.toString(a)}, l = $l, m = $m")
        l.add("New Element 1 From Server")
        l.add("New Element 2 From Server")
        return s + cs
    }

    override fun parcelables(b: Bundle, p: Person): Person {
        Log.d(TAG, "parcelables() called with: b = ${b.keySet().first() to b.get(b.keySet().first())}, p = $p")
        return Person(p.name.toUpperCase(Locale.US), p.age + 2)
    }

    override fun getPackageSize(pkgName: String, callback: IGetPackageSizeCallback) {
        Log.d(TAG, "getPackageSize() called with: pkgName = $pkgName, callback = $callback")
        callback.onPackageSizeAcquired(pkgName, 1_048_576)
        Log.d(TAG, "onPackageSizeAcquired() callback finished on the server side")
    }

    override fun throwIllegalArgumentException() {
        throw IllegalArgumentException("Main reason", IOException("Root cause"))
    }

    override fun throwRuntimeException() {
        throw RuntimeException("Main reason", IOException("Root cause"))
    }

    override fun throwIOException() {
        throw IOException("Main reason", RuntimeException("Root cause"))
    }

    // Remote Exceptions? (and subclasses)

/*    override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {

        // com.microsoft.office.outlook.bluetooth.BluetoothContentProvider:
        // if (Binder.getCallingUid() != Process.BLUETOOTH_UID) {
        //     throw SecurityException()
        // }

        val pkgName = App.app.packageManager.getPackagesForUid(Binder.getCallingUid())?.get(0)
        if (pkgName != "com.contoso.client") {
            throw SecurityException()
        }

        // App.app.packageManager.checkSignatures(pkgName, App.app.packageName)
        // App.app.packageManager.checkSignatures(Binder.getCallingUid(), Process.myUid())

        // App.app.checkCallingPermission() / enforceCallingPermission()

        Log.d(TAG, "Before clearCallingIdentity(), isGranted = "
                + (App.app.checkCallingOrSelfPermission("com.contoso.lib.IpcPermission") == PackageManager.PERMISSION_GRANTED))
        val ident = Binder.clearCallingIdentity()
        try {
            Log.d(TAG, "After clearCallingIdentity(), isGranted = "
                    + (App.app.checkCallingOrSelfPermission("com.contoso.lib.IpcPermission") == PackageManager.PERMISSION_GRANTED))
        } finally {
            Binder.restoreCallingIdentity(ident)
        }

        // Service.onBind() and ContentProvider.call() / insert() / ... are also good places to do such permission checks.

        // We can check Parcel.dataSize() against IBinder.getSuggestedMaxIpcSizeBytes() to prevent TransactionTooLargeException
        Log.d(TAG, reply?.dataSize().toString())
        // ParceledListSlice

        return super.onTransact(code, data, reply, flags)
    }*/

    private companion object {

        private const val TAG = "Server"
    }
}

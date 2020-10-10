package com.contoso.client

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Space
import com.contoso.lib.IGetPackageSizeCallback
import com.contoso.lib.IProtocol
import com.contoso.lib.Person

class Client : Activity() {

    private var protocol: IProtocol? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    private fun bindService() {
        val intent = Intent()
        intent.component = ComponentName("com.contoso.server", "com.contoso.server.Server")
        bindService(intent, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                Log.d(TAG, "onServiceConnected() called with: name = $name, service = $service")
                protocol = IProtocol.Stub.asInterface(service)
                Log.d(TAG, "protocol = $protocol")
                service?.linkToDeath({
                    Log.d(TAG, "binderDied() called")
                    protocol = null
                    // reconnect, if we did't disconnect the service deliberately via unbindService()
                    // bindService(intent, this, BIND_AUTO_CREATE)
                }, 0)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.d(TAG, "onServiceDisconnected() called")
                protocol = null
                // reconnect, if we did't disconnect the service deliberately via unbindService()
                // bindService(intent, this, BIND_AUTO_CREATE)
            }

            override fun onBindingDied(name: ComponentName?) {
                Log.d(TAG, "onBindingDied() called")
                protocol = null
                // reconnect, if we did't disconnect the service deliberately via unbindService()
                // bindService(intent, this, BIND_AUTO_CREATE)
            }
        }, BIND_AUTO_CREATE)
    }

    private fun basicTypes() {
        val list = arrayListOf("Original Element 1", "Original Element 2")
        val ret = protocol!!.basicTypes(1024, "Hello", ", World!", intArrayOf(1, 2), list, mapOf("Key" to "Value"))
        Log.d(TAG, ret)
        Log.d(TAG, list.toString())
    }

    private fun parcelables() {
        val ret = protocol!!.parcelables(Bundle().apply {
            putInt("Hello", 1024)
        }, Person("Chad", 10))
        Log.d(TAG, ret.toString())
    }

    private fun getPackageSize() {
        protocol!!.getPackageSize("pkg", object : IGetPackageSizeCallback.Stub() {
            override fun onPackageSizeAcquired(pkgName: String, size: Long) {
                Thread.sleep(1000)
                Log.d(TAG, "onPackageSizeAcquired() called with: pkgName = $pkgName, size = $size")
            }
        })
    }

    private fun throwIllegalArgumentException() {
        protocol!!.throwIllegalArgumentException()
    }

    private fun throwRuntimeException() {
        protocol!!.throwRuntimeException()
    }

    private fun throwIOException() {
        protocol!!.throwIOException()
    }

    private fun contentProviderCallExchangeData() {
        val replyBundle = contentResolver.call(PROVIDER_URI, "sayHello", "Chad", Bundle().apply {
            putString("secondPerson", "Nathan")
        })
        val replyString = replyBundle?.getString("reply")
        Log.d(TAG, replyString.toString())
    }

    private fun contentProviderCallExchangeBinder() {
        val protocolBundle = contentResolver.call(PROVIDER_URI, "getProtocol", null, null)
        val protocol = IProtocol.Stub.asInterface(protocolBundle?.getBinder("protocol"))
        val ret = protocol.basicTypes(10, "ContentProvider.call()", ": exchanging Binders", intArrayOf(), arrayListOf(),
            hashMapOf<String, String>())
        Log.d(TAG, ret)
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        setContentView(container)

        val bindServiceButton = Button(this)
        bindServiceButton.text = "Bind Service"
        bindServiceButton.setOnClickListener { bindService() }
        container.addView(bindServiceButton)

        val basicTypesButton = Button(this)
        basicTypesButton.text = "Basic Types"
        basicTypesButton.setOnClickListener { basicTypes() }
        container.addView(basicTypesButton)

        val parcelablesButton = Button(this)
        parcelablesButton.text = "Parcelables"
        parcelablesButton.setOnClickListener { parcelables() }
        container.addView(parcelablesButton)

        val getPackageSizeButton = Button(this)
        getPackageSizeButton.text = "Get Package Size"
        getPackageSizeButton.setOnClickListener { getPackageSize() }
        container.addView(getPackageSizeButton)

        val throwIllegalArgumentExceptionButton = Button(this)
        throwIllegalArgumentExceptionButton.text = "Throw IllegalArgumentException"
        throwIllegalArgumentExceptionButton.setOnClickListener { throwIllegalArgumentException() }
        container.addView(throwIllegalArgumentExceptionButton)

        val throwRuntimeExceptionButton = Button(this)
        throwRuntimeExceptionButton.text = "Throw RuntimeException"
        throwRuntimeExceptionButton.setOnClickListener { throwRuntimeException() }
        container.addView(throwRuntimeExceptionButton)

        val throwIOExceptionButton = Button(this)
        throwIOExceptionButton.text = "Throw IOException"
        throwIOExceptionButton.setOnClickListener { throwIOException() }
        container.addView(throwIOExceptionButton)

        val space = Space(this)
        space.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 48)
        container.addView(space)

        val contentProviderCallExchangeDataButton = Button(this)
        contentProviderCallExchangeDataButton.text = "ContentProvider Exchange Data"
        contentProviderCallExchangeDataButton.setOnClickListener { contentProviderCallExchangeData() }
        container.addView(contentProviderCallExchangeDataButton)

        val contentProviderCallExchangeBinderButton = Button(this)
        contentProviderCallExchangeBinderButton.text = "ContentProvider Exchange Binder"
        contentProviderCallExchangeBinderButton.setOnClickListener { contentProviderCallExchangeBinder() }
        container.addView(contentProviderCallExchangeBinderButton)
    }

    private companion object {

        private const val TAG = "Client"
        private val PROVIDER_URI = Uri.parse("content://com.contoso.server.Provider")
    }
}

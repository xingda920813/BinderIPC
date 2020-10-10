package com.contoso.server

import android.app.Service
import android.content.Intent

class Server : Service() {

    private val binder = ProtocolImpl()

    override fun onBind(intent: Intent?) = binder
}

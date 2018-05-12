package com.whynuttalk.foreignteacher.Service

import android.content.Context
import cn.sinata.xldutils.utils.Toast
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import java.util.*

/**
 * Created by cz on 5/4/18.
 */
class MessageHandler(val context: Context) : EMConnectionListener, EMMessageListener {
    override fun onMessageRecalled(messages: MutableList<EMMessage>?) {

    }

    override fun onMessageChanged(message: EMMessage?, change: Any?) {
    }

    override fun onCmdMessageReceived(messages: MutableList<EMMessage>?) {
    }

    override fun onMessageReceived(messages: MutableList<EMMessage>?) {
    }

    override fun onMessageDelivered(messages: MutableList<EMMessage>?) {
    }

    override fun onMessageRead(messages: MutableList<EMMessage>?) {
    }

    override fun onConnected() {
        onConnectionConnected()
    }

    override fun onDisconnected(errorCode: Int) {
        onConnectionDisconnected()
    }

    private var conversationList: MutableList<EMConversation> = ArrayList()

    init {
        EMClient.getInstance().addConnectionListener(this)
        EMClient.getInstance().chatManager().addMessageListener(this)
    }

    /**
     * connected to server
     */
    fun onConnectionConnected() {
        Toast.create(context).show("connect success")
    }

    /**
     * disconnected with server
     */
    fun onConnectionDisconnected() {
        Toast.create(context).show("connect disconnected")
    }
}

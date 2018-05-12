package com.whynuttalk.foreignteacher.ui.msg

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import cn.sinata.xldutils.fragment.BaseFragment
import cn.sinata.xldutils.utils.ActivityUtil
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import com.hyphenate.easeui.EaseConstant
import com.hyphenate.easeui.widget.EaseConversationList
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.User
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import java.util.*

/**
 * Created by cz on 3/29/18.
 */
open class MessageFragment : BaseFragment() {
    @BindView(R.id.lv_msg)
    lateinit var lvMsg: EaseConversationList
    @BindView(R.id.tv_content_order)
    lateinit var tvContentOrder: TextView
    @BindView(R.id.tv_new_order_count)
    lateinit var tvNewOrderCount: TextView
    @BindView(R.id.tv_content_system)
    lateinit var tvContentSystem: TextView
    @BindView(R.id.tv_new_system_count)
    lateinit var tvNewSystemCount: TextView
    @BindView(R.id.rl_system_msg)
    lateinit var rlSystemMsg: RelativeLayout
    lateinit var unbinder: Unbinder
    val user: User by lazy { appComponent.userHandler.getUser() }

    private var userId: Int = 0

    protected var isConflict: Boolean = false
    protected var hidden: Boolean = false
    private val MSG_REFRESH = 2
    private lateinit var activityUtil: ActivityUtil
    protected var conversationList: MutableList<EMConversation> = ArrayList()

    protected var handler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            when (msg.what) {
                0 -> onConnectionDisconnected()
                1 -> onConnectionConnected()

                MSG_REFRESH -> {
                    conversationList.clear()
                    conversationList.addAll(loadConversationList())
                    lvMsg!!.refresh()
                }
                else -> {
                }
            }
        }
    }

    /**
     * connected to server
     */
    protected fun onConnectionConnected() {
        //showToast("connect success")
        //Toast.create(this).show("connect success")
    }

    /**
     * disconnected with server
     */
    protected fun onConnectionDisconnected() {
        //        showToast("聊天服务器断开");
    }

    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_message
    }

    override fun onFirstVisibleToUser() {
        activityUtil = ActivityUtil.create(activity)

        if (user.id != -1) {
            userId = user.id
            loadData()
        }
        conversationList.addAll(loadConversationList())
        lvMsg.init(conversationList)
        lvMsg.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val conversation = lvMsg!!.getItem(position)
            startActivity(Intent(activity, ChatActivity::class.java).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()))
        }
        EMClient.getInstance().addConnectionListener(connectionListener)
        EMClient.getInstance().chatManager().addMessageListener(messageListener)
    }

    private var connectionListener: EMConnectionListener = object : EMConnectionListener {

        override fun onDisconnected(error: Int) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED
                    || error == EMError.USER_KICKED_BY_CHANGE_PASSWORD || error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
                isConflict = true
            } else {
                handler.sendEmptyMessage(0)
            }
        }

        override fun onConnected() {
            handler.sendEmptyMessage(1)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            EMClient.getInstance().chatManager().addMessageListener(messageListener)
            if (user.id != id) { //账号切换
                //showDialog()
                tvContentOrder.text = ""
                tvContentSystem.text = ""
                tvNewOrderCount.visibility = View.GONE
                tvNewSystemCount.visibility = View.GONE
            }
            if (user.id != -1)
                loadData()
        } else
            EMClient.getInstance().chatManager().removeMessageListener(messageListener)
        this.hidden = hidden
        if (!hidden && !isConflict) {
            refresh()
        }
    }

    /**
     * refresh ui
     */
    fun refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH)
        }
    }

    override fun onVisibleToUser() {}


    override fun onInvisibleToUser() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TODO: inflate a fragment view
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        unbinder = ButterKnife.bind(this, rootView!!)
        userVisibleHint = true
        return rootView
    }

    private fun loadData() {
        appComponent.netWork.getUnReadMessageCount(appComponent.userHandler.getUser().id, 2)
                .doOnLoading {isShowDialog(it)}
                .doOnSubscribe { addDisposable(it) }
                .subscribe {
                    tvContentSystem.text = it.messTitle
                    tvContentOrder.text = it.noticeTitle
                    if (it.messNum > 0) {
                        tvNewSystemCount.visibility = View.VISIBLE
                        tvNewSystemCount.text = it.messNum.toString()
                    }
                    if (it.noticeNum > 0) {
                        tvNewOrderCount.visibility = View.VISIBLE
                        tvNewSystemCount.text = it.noticeNum.toString()
                    }
                }
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
        EMClient.getInstance().chatManager().removeMessageListener(messageListener)
    }

    @OnClick(R.id.rl_order_msg_all, R.id.rl_system_msg)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.rl_order_msg_all -> activityUtil.go(OrderMsgActivity::class.java).start()
            R.id.rl_system_msg -> activityUtil.go(SystemMsgActivity::class.java).start()
        }
    }

    /**
     * load conversation list
     *
     * @return
     * +
     */
    protected fun loadConversationList(): List<EMConversation> {
        // get all conversations
        val conversations = EMClient.getInstance().chatManager().allConversations
        val sortList = ArrayList<Pair<Long, EMConversation>>()
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized(conversations) {
            for (conversation in conversations.values) {
                if (conversation.allMessages.size != 0) {
                    sortList.add(Pair(conversation.lastMessage.msgTime, conversation))
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val list = ArrayList<EMConversation>()
        for (sortItem in sortList) {
            list.add(sortItem.second)
        }
        return list
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private fun sortConversationByLastChatTime(conversationList: List<Pair<Long, EMConversation>>) {
        Collections.sort(conversationList) { con1, con2 ->
            when {
                con1.first == con2.first -> 0
                con2.first.toLong() > con1.first.toLong() -> 1
                else -> -1
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (!hidden) {
            refresh()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().removeConnectionListener(connectionListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (isConflict) {
            outState.putBoolean("isConflict", true)
        }
    }

    var messageListener: EMMessageListener = object : EMMessageListener {

        override fun onMessageReceived(messages: List<EMMessage>) {
            refresh()
        }

        override fun onCmdMessageReceived(messages: List<EMMessage>) {
            refresh()
        }

        override fun onMessageRead(messages: List<EMMessage>) {}

        override fun onMessageDelivered(message: List<EMMessage>) {}

        override fun onMessageRecalled(messages: List<EMMessage>) {
            refresh()
        }

        override fun onMessageChanged(message: EMMessage, change: Any) {}
    }
}

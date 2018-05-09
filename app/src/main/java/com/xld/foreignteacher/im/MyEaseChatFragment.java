package com.xld.foreignteacher.im;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xld.foreignteacher.BaseApp;
import com.xld.foreignteacher.api.dto.User;
import com.xld.foreignteacher.data.UserHomeData;
import com.xld.foreignteacher.ui.msg.ChatActivity;
import com.xld.foreignteacher.util.RxBus;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 聊天页面 ，继承自easeui
 * Created by Administrator on 2018/3/15.
 */

public class MyEaseChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {

    private boolean isRobot;
    private CompositeDisposable compositeDisposable;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        hideTitleBar();
    }

    @Override
    protected void setUpView() {
        compositeDisposable = new CompositeDisposable();
        inputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                sendTextMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                RxPermissions permissions = new RxPermissions(getActivity());
                permissions.request(Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {
                                if (!aBoolean) {
                                    Toast.makeText(getActivity(), "请授予录音和访问本地存储的权限！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                    }
                });
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
                sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
            }
        });
        setChatFragmentHelper(this);
        if (chatType == Constant.CHATTYPE_SINGLE) {
            Map<String, RobotUser> robotMap = HxEaseuiHelper.getInstance().getRobotList();
            if (robotMap != null && robotMap.containsKey(toChatUsername)) {
                isRobot = true;
            }
        }
        RxBus.getInstance().toObservable(UserHomeData.class)
                .subscribe(new Observer<UserHomeData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(UserHomeData userHomeData) {
                        EMMessage message = EMMessage.createTxtSendMessage("scoreCard", toChatUsername);
                        message.setAttribute("listening",userHomeData.getListening());
                        message.setAttribute("grammar",userHomeData.getGrammar());
                        message.setAttribute("vocabulary",userHomeData.getVocabulary());
                        message.setAttribute("pronunciation",userHomeData.getPronunciation());
                        message.setAttribute("fluency",userHomeData.getFluency());
                        sendMessage(message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(),"获取学卡数据失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        super.setUpView();
    }


    @Override
    public void onSetMessageAttributes(EMMessage message) {
        if (isRobot) {
            //set message extension
            message.setAttribute("em_robot_message", isRobot);
        }
        Log.e(TAG, "onSetMessageAttributes: 发送头像");
        //设置要发送扩展消息用户昵称
        User user= BaseApp.getInstance().userHandler.getUser();
        String nickname =user.getNickName();
        String url =user.getImgUrl();
        String id = String.valueOf(user.getId());
        SharedPreferencesUtils.setParam(getContext(), APPConfig.USER_HEAD_IMG, url);
        SharedPreferencesUtils.setParam(getContext(), APPConfig.USER_NAME, nickname);
        SharedPreferencesUtils.setParam(getContext(), APPConfig.USER_ID, id);
        Log.e(TAG, "onSetMessageAttributes: 姓名" + nickname + ",头像" + url + ",id" + id);
        message.setAttribute(Constant.NICK_NAME, nickname);
//        message.setAttribute(Constant.USER_ID,);
        //设置要发送扩展消息用户头像
        message.setAttribute(Constant.HEAD_IMAGE_URL, url);
        message.setAttribute(Constant.USER_ID, id);

//        message.setAttribute(Constant.SEX, "1");
//        message.setAttribute(Constant.RECEIVOR_HEAD_IMAGE_URL, "http://www.qqzhi.com/uploadpic/2014-09-14/070503273.jpg");
//        message.setAttribute(Constant.RECEIVOR_USER_NAME, "honey");
//        message.setAttribute(Constant.RECEIVOR_USER_SEX, "2");
//        message.setAttribute(Constant.RECEIVOR_USERID, "150");

    }

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {

    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }

    @Override
    protected void sendScoreCardMessage() {
        ChatActivity activity = (ChatActivity) getActivity();
        activity.getScoreCard();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear();
    }
}

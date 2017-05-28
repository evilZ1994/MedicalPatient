package com.example.r2d2.medicalpatient.ui.fragment.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.data.bean.DefaultUser;
import com.example.r2d2.medicalpatient.data.bean.MyMessage;
import com.example.r2d2.medicalpatient.data.bean.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;


public class MessageFragment extends Fragment {
    private MessageList messageList;
    private ChatInputView chatInputView;
    private MsgListAdapter adapter;
    private Conversation conversation;

    private User currentUser = App.getCurrentUser();
    private DefaultUser me = new DefaultUser("0", currentUser.getUsername(), null);
    private DefaultUser doctor = new DefaultUser(currentUser.getDoctor_id()+"", currentUser.getDoctor_username(), null);

    public MessageFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_message, container, false);
        //初始化MessageList和ChatInputView
        messageList = (MessageList) view.findViewById(R.id.msg_list);
        chatInputView = (ChatInputView) view.findViewById(R.id.chat_input);
        chatInputView.setMenuContainerHeight(1);
        //MsgListAdapter，默认发送者id为0，ImageLoader为null（不显示头像）
        adapter = new MsgListAdapter<>("0", new MsgListAdapter.HoldersConfig(), null);
        messageList.setAdapter(adapter);
        //获取当前conversation，如果没有则新建
        conversation = JMessageClient.getSingleConversation(doctor.getDisplayName(), App.CHAT_APP_KEY);
        if (conversation == null){
            conversation = Conversation.createSingleConversation(doctor.getDisplayName(), App.CHAT_APP_KEY);
        }
        //获取本地聊天记录,并展示
        showLocalMessages();
        //注册离线消息接收者
        JMessageClient.registerEventReceiver(new OfflineMessageEventReceiver());
        //注册消息事件接收者
        JMessageClient.registerEventReceiver(new MessageEventReceiver());

        //绑定输入控件的监听器,用于发送消息
        chatInputView.setMenuClickListener(new OnMenuClickListener() {
            @Override
            public boolean onSendTextMessage(final CharSequence input) {
                if (input != null && !input.equals("")){
                    Message message = JMessageClient.createSingleTextMessage(doctor.getDisplayName(), App.CHAT_APP_KEY, input.toString());
                    message.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            //发送成功的回调,成功后显示在列表上
                            IMessage iMessage = new MyMessage(input.toString(), IMessage.MessageType.SEND_TEXT);
                            adapter.addToStart(iMessage, true);
                        }
                    });
                    JMessageClient.sendMessage(message);
                }
                return true;
            }

            @Override
            public void onSendFiles(List<FileItem> list) {

            }

            @Override
            public void switchToMicrophoneMode() {

            }

            @Override
            public void switchToGalleryMode() {

            }

            @Override
            public void switchToCameraMode() {

            }
        });

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void showLocalMessages(){
        List<Message> messages = conversation.getAllMessage();
        if (messages != null && messages.size()>0){
            for(Message message : messages){
                MessageContent content = message.getContent();
                try {
                    JSONObject jsonObject = new JSONObject(content.toJson());
                    IMessage iMessage;
                    if (message.getDirect() == MessageDirect.send) {
                        iMessage = new MyMessage(jsonObject.getString("text"), IMessage.MessageType.SEND_TEXT);
                    } else {
                        iMessage = new MyMessage(jsonObject.getString("text"), IMessage.MessageType.RECEIVE_TEXT);
                    }
                    adapter.addToStart(iMessage, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class OfflineMessageEventReceiver{
        public void onEvent(OfflineMessageEvent event){
            List<Message> messages = event.getOfflineMessageList();
            for (Message message : messages){
                MessageContent content = message.getContent();
                IMessage iMessage;
                if (message.getDirect() == MessageDirect.receive){
                    iMessage = new MyMessage(content.getStringExtra("text"), IMessage.MessageType.RECEIVE_TEXT);
                }else {
                    iMessage = new MyMessage(content.getStringExtra("text"), IMessage.MessageType.SEND_TEXT);
                }
                adapter.addToStart(iMessage, true);
            }
        }
    }

    public class MessageEventReceiver{
        public void onEvent(MessageEvent event){
            Message message = event.getMessage();
            MessageContent content = message.getContent();
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(content.toJson());
                IMessage iMessage = new MyMessage(jsonObject.getString("text"), IMessage.MessageType.RECEIVE_TEXT);
                adapter.addToStart(iMessage, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回输入框组件，用于隐藏软键盘
     */
    public ChatInputView getChatInputView(){
        return chatInputView;
    }
}

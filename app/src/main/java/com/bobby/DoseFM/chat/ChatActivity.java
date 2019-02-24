package com.bobby.DoseFM.chat;

import android.app.Activity;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bobby.DoseFM.R;
import com.bobby.DoseFM.model.Message;
import com.bobby.DoseFM.model.MessageList;
import com.bobby.DoseFM.utils.ConnectivityReceiver;
import com.bobby.DoseFM.model.MessageItem;
import com.bobby.DoseFM.utils.Constants;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class ChatActivity extends AppCompatActivity implements ChatContract.ChatView, ConnectivityReceiver.ConnectivityReceiverListener{
    private ArrayList<Message> arrayListMessages;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView tv_empty;
    private EditText et_message;
    private Activity mActivity;
    private ImageView img_empty;
    private ImageView img_btn_send;
    private ChatMessagesAdapter messagesAdapter;
    private boolean isNetworkAvailable = true;
    private IntentFilter intentFilter;
    private ConnectivityReceiver receiver;
    private ChatPresenter presenter;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);

        pref = getSharedPreferences(Constants.CHAT_HISTORY, 0);
        editor = pref.edit();
        presenter=new ChatPresenter(pref,editor,this);
        mActivity = this;

        renderView();
        init();
    }

    private void renderView(){
        toolbar = findViewById(R.id.toolbar_message);
        tv_empty =  findViewById(R.id.tv_empty);
        img_empty = findViewById(R.id.empty_img);
        et_message = findViewById(R.id.et_message);
        recyclerView = findViewById(R.id.listview_message);
        img_btn_send = findViewById(R.id.img_btn_send);
    }

    private void init(){
        toolbar.setTitle(R.string.chat_bot);
        setSupportActionBar(toolbar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        if(presenter.getChatHistory()!=null){
            arrayListMessages = presenter.getChatHistory().getMessageList();                             //Fetch Chat History
        }


        if(arrayListMessages==null){
            arrayListMessages = new ArrayList<>();
        }
        else {
            tv_empty.setVisibility(View.GONE);
            img_empty.setVisibility(View.GONE);
            messagesAdapter = new ChatMessagesAdapter(mActivity, arrayListMessages,recyclerView);    //Display previous chat history
            recyclerView.setAdapter(messagesAdapter);
            recyclerView.scrollToPosition(arrayListMessages.size() - 1);
        }

        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        receiver = new ConnectivityReceiver();

        //send message
        img_btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(TextUtils.isEmpty(et_message.getText().toString())){     //Editext empty- Nothing to send
                        return;
                    }

                    if(isNetworkAvailable) {                                     //Checking internet connection before trying to send message
                        String str_message = et_message.getText().toString();
                        et_message.setText("");

                        setMessage("Me",str_message);                     //Setting  user message on screen
                        presenter.sendMessage(str_message);                      //Sending message to server chatbot
                    }
                    else{
                        StyleableToast.makeText(mActivity, getString(R.string.check_internet), Toast.LENGTH_LONG, R.style.successToast).show();
                    }

                }catch (Exception e){
                    e.getMessage();
                }
            }
        });
    }

    @Override
    public void onFailure(String message) {
        StyleableToast.makeText(this, message, Toast.LENGTH_LONG, R.style.warningToast).show();
    }

    @Override
    public void onSuccess(MessageItem response) {
       setMessage(response.getData().getSenderName(),response.getData().getMessage());             //Displaying Chatbot response
    }


    private void setMessage(String sender, String message){
        //Message object for storing in database;
        Message messageItem = new Message();
        messageItem.setDate(System.currentTimeMillis());
        messageItem.setSenderName(sender);
        messageItem.setMessage(message);
        arrayListMessages.add(messageItem);

        tv_empty.setVisibility(View.GONE);
        img_empty.setVisibility(View.GONE);
        if (messagesAdapter == null) {
            messagesAdapter = new ChatMessagesAdapter(mActivity, arrayListMessages,recyclerView);
            recyclerView.setAdapter(messagesAdapter);

        } else {
            messagesAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(arrayListMessages.size() - 1);
        }

        MessageList messageList=new MessageList();
        messageList.setMessageList(arrayListMessages);
        presenter.storeChatHistory(messageList);              //Update chat history
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
        ConnectivityReceiver.connectivityReceiverListener = this;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isNetworkAvailable != isConnected) {
            if (isConnected) {
                StyleableToast.makeText(this, getString(R.string.connected), Toast.LENGTH_LONG, R.style.successToast).show();
                img_btn_send.setEnabled(true);
            } else {
                StyleableToast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_LONG, R.style.warningToast).show();
                img_btn_send.setEnabled(false);
            }
        }
        isNetworkAvailable = (isConnected);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (receiver != null) {
            unregisterReceiver(receiver);            //Stopping internet connection check
        }
        presenter.cleanMemory();                     //Disposing Rxjava call
    }
}


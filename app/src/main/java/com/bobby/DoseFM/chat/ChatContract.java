package com.bobby.DoseFM.chat;


import com.bobby.DoseFM.model.MessageItem;

public class ChatContract {
    interface ChatView {
        void onFailure(String message);
        void onSuccess(MessageItem response);
    }

    interface Presenter{
        void cleanMemory();
        void sendMessage(String message);
    }
}

package com.bobby.DoseFM.chat;

import android.content.SharedPreferences;

import com.bobby.DoseFM.model.MessageItem;
import com.bobby.DoseFM.model.MessageList;
import com.bobby.DoseFM.network.NetworkError;
import com.bobby.DoseFM.network.Service;
import com.google.gson.Gson;


public class ChatPresenter implements ChatContract.Presenter{
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ChatContract.ChatView view;

    ChatPresenter(SharedPreferences pref, SharedPreferences.Editor editor, ChatContract.ChatView view) {
        this.editor = editor;
        this.pref = pref;
        this.view = view;
    }

    @Override
    public void sendMessage(String message) {
          new Service().getChatbotResponse(new Service.GetChatbotResponseCallback() {
              @Override
              public void onSuccess(MessageItem response) {
                  view.onSuccess(response);
              }

              @Override
              public void onError(NetworkError networkError) {
                  view.onFailure(networkError.getAppErrorMessage());
              }

              @Override
              public void onFailure(String error) {
                  view.onFailure(error);
              }
          },message);
    }

    @Override
    public void cleanMemory(){
        if(Service.disposable!=null){
            Service.disposable.dispose();
        }
    }

    public void storeChatHistory(MessageList response, String category) {
        Gson gson = new Gson();
        String json = gson.toJson(response);
        editor.putString(category, json);
        editor.commit();
    }

    public MessageList getChatHistory(String category) {
        Gson gson = new Gson();
        String json = pref.getString(category, "");
        return gson.fromJson(json,MessageList.class);
    }


}

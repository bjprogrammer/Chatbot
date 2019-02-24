package com.bobby.DoseFM.chat;

import com.bobby.DoseFM.model.MessageItem;
import com.bobby.DoseFM.network.NetworkError;
import com.bobby.DoseFM.network.Service;


public class ChatPresenter implements ChatContract.Presenter{

    private ChatContract.ChatView view;

    ChatPresenter( ChatContract.ChatView view) {
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
}

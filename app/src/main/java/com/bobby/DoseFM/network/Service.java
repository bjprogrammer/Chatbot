package com.bobby.DoseFM.network;

import com.bobby.DoseFM.utils.Constants;
import com.bobby.DoseFM.model.MessageItem;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


//Networking using RxJava 2
public class Service {
    public  static CompositeDisposable disposable;
    private NetworkService networkService;
    public Service(){
        networkService=NetworkAPI.getClient().create(NetworkService.class);
        if(disposable==null){
            disposable=new CompositeDisposable();
        }
    }


    public Observer getChatbotResponse(final GetChatbotResponseCallback callback, String message) {
        return networkService.getMessage(Constants.APP_KEY,Constants.CHATBOT_ID,Constants.EXTERNAL_ID,message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext((Function) throwable -> {
                    return Observable.error((Throwable) throwable);
                })
                .subscribeWith(new Observer<MessageItem>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if(d.isDisposed()) {
                            disposable.add(d);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(MessageItem response) {
                        if(response.getSuccess()==1) {
                            callback.onSuccess(response);
                        }
                        else {
                            callback.onFailure(response.getError());
                        }
                    }
                });
    }



    public interface GetChatbotResponseCallback{
        void onSuccess(MessageItem response);
        void onError(NetworkError networkError);
        void onFailure(String error);
    }

}


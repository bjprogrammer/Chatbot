package com.bobby.DoseFM.network;


import com.bobby.DoseFM.model.MessageItem;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

//ALL API calls
public interface NetworkService {

    @GET("api/chat/")
    Observable<MessageItem> getMessage(@Query("apiKey") String apiKey, @Query("chatBotID") String chatBotId, @Query("externalID") String externalId, @Query("message") String message) ;

}


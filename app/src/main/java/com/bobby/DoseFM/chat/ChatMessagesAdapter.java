package com.bobby.DoseFM.chat;

import android.content.Context;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobby.DoseFM.R;
import com.bobby.DoseFM.model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


public class ChatMessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Message> arrayList;
    Context mContext;
    LayoutInflater layoutInflater;
    RecyclerView recyclerView;

    public ChatMessagesAdapter(Context context,  List<Message> arraylist, RecyclerView recyclerview) {
        mContext = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        recyclerView = recyclerview;
        arrayList=arraylist;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.my_message_layout,parent,false);
        return new ViewHolderTextMessage(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

               ViewHolderTextMessage viewHolderTextMessage = (ViewHolderTextMessage)holder;
               if(arrayList.get(position).getSenderName().equals("Me")){                       //Displaying messages sent from device
                   ((ViewHolderTextMessage) holder).l_layout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
                   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                   layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                   layoutParams.setMargins(200,20,50,20);
                   ((ViewHolderTextMessage) holder).card_message.setLayoutParams(layoutParams);
                   viewHolderTextMessage.from.setTextColor(ContextCompat.getColor(mContext,android.R.color.white));
                   viewHolderTextMessage.message.setTextColor(ContextCompat.getColor(mContext,android.R.color.white));
                   viewHolderTextMessage.date.setTextColor(ContextCompat.getColor(mContext,android.R.color.white));
               }
               else{                                                                         //Displaying response from ChatBot
                   ((ViewHolderTextMessage) holder).l_layout.setBackgroundColor(Color.parseColor("#f0f0f0"));
                   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                   layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                   layoutParams.setMargins(50,20,220,20);
                   ((ViewHolderTextMessage) holder).card_message.setLayoutParams(layoutParams);
                   viewHolderTextMessage.from.setTextColor(ContextCompat.getColor(mContext,android.R.color.black));
                   viewHolderTextMessage.message.setTextColor(ContextCompat.getColor(mContext,android.R.color.black));
                   viewHolderTextMessage.date.setTextColor(ContextCompat.getColor(mContext,android.R.color.black));
               }



               viewHolderTextMessage.message.setText(arrayList.get(position).getMessage());
               Long date_time = arrayList.get(position).getDate();
               viewHolderTextMessage.from.setText(arrayList.get(position).getSenderName());
               try {
                   String date = convertLongToDate(date_time,1);
                   String time = convertLongToDate(date_time,2);
                   viewHolderTextMessage.date.setText(date+"  "+time);
               }catch (Exception e){
                   viewHolderTextMessage.date.setText("");
               }
    }

    public  String convertLongToDate(long time, int dateFormat) {
        String dateString = "";
        if (dateFormat == 1) {
            dateString = new SimpleDateFormat("MM/dd/yyyy")           //Formatting date from milliseconds
                    .format(new Date(time));
        }
        if (dateFormat == 2) {
            dateString = new SimpleDateFormat("HH:mm")                //Formatting time from milliseconds
                    .format(new Date(time));
        }
        return dateString;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolderTextMessage extends RecyclerView.ViewHolder{

        TextView message;
        TextView date;
        TextView from;
        CardView card_message;
        LinearLayout l_layout;

        public ViewHolderTextMessage(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.tv_message);
            date = itemView.findViewById(R.id.tv_time);
            from = itemView.findViewById(R.id.tv_name);
            card_message = itemView.findViewById(R.id.card_message);
            l_layout = itemView.findViewById(R.id.l_message);
        }
    }
}

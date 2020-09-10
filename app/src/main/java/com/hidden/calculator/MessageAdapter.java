package com.hidden.calculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chat> mChat;
    public String currUser;

    public MessageAdapter(Context mContext, List<Chat> mChat,String currUser) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.currUser = currUser;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.sentmessage, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.receivedmessage, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());
        try{
            DateFormat dateFormat = DateFormat.getTimeInstance();
            Date netDate = (new Date(chat.getTime()));
            String time = dateFormat.format(netDate);
            holder.time.setText(time);
        }catch (Exception e){
            holder.time.setText("date");
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public TextView time;
        public ViewHolder(View itemView){
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            time = itemView.findViewById(R.id.time);
        }
    }

    @Override
    public int getItemViewType(int position) {
        try{
            String sender = mChat.get(position).getSender();
            if(currUser!=null && sender!=null && !currUser.equals(sender)){
                return MSG_TYPE_LEFT;
            }
            else {
                return MSG_TYPE_RIGHT;
            }
        }catch (Exception e){
            return MSG_TYPE_RIGHT;
        }
    }
}

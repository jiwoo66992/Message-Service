package com.example.realtimechat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtimechat.R;
import com.example.realtimechat.model.Account;
import com.example.realtimechat.model.RoomChat;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    Context context;
    ArrayList<RoomChat> data;
    ClickChat clickChat;


    public ChatAdapter(Context context, ArrayList<RoomChat> data, ClickChat clickChat) {
        this.context = context;
        this.data = data;
        this.clickChat = clickChat;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<RoomChat> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_room_chat, parent, false);
        ChatAdapter.ViewHolder viewHolder = new ChatAdapter.ViewHolder(view);
        return viewHolder;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RoomChat roomChat = data.get(position);

        holder.lastMess.setText(roomChat.getLastMessage());
        holder.gmail.setText(roomChat.getEmail());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickChat.clickChat(roomChat);
            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView gmail, lastMess;
        LinearLayout view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lastMess = itemView.findViewById(R.id.txtLastMess);
            gmail = itemView.findViewById(R.id.txtGmail);
            view = itemView.findViewById(R.id.view);
        }

    }
}

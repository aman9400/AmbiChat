package com.ambiguous.ambichat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambiguous.ambichat.model.ModelChat;
import com.example.ambichat.R;

import java.util.List;

public class AdapterChat extends RecyclerView.Adapter  {

    final Context context;
    final List<ModelChat> modelChats;
    final int SENDER_VIEW_TYPE = 1;
    final int RECEIVER_VIEW_TYPE = 2;

    public AdapterChat(Context context, List<ModelChat> modelChats) {
        this.context = context;
        this.modelChats = modelChats;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sendermsg,parent,false);
            return new SenderViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.receivermsg,parent,false);
            return new ReceiverViewholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder) holder).sendermsg.setText(modelChats.get(position).getMsg());
            ((SenderViewHolder) holder).senderTime.setText(modelChats.get(position).getTime());
        }else {
            ((ReceiverViewholder) holder).receivermsg.setText(modelChats.get(position).getMsg());
            ((ReceiverViewholder) holder).receivertime.setText(modelChats.get(position).getTime());
        }
    }

    @Override
    public int getItemCount() {
        return modelChats.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(modelChats.get(position).getUserWho().equalsIgnoreCase("0")){
            return SENDER_VIEW_TYPE;
        }else {
            return RECEIVER_VIEW_TYPE;
        }

    }

    public static class ReceiverViewholder extends RecyclerView.ViewHolder{

      final TextView receivermsg;
        final TextView receivertime;

      public ReceiverViewholder(@NonNull View itemView) {
          super(itemView);

          receivermsg = itemView.findViewById(R.id.receivermsg);
          receivertime = itemView.findViewById(R.id.receivertime);
      }
  }

    public static class SenderViewHolder extends RecyclerView.ViewHolder{

        final TextView sendermsg;
        final TextView senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            sendermsg = itemView.findViewById(R.id.senderMsg);
            senderTime = itemView.findViewById(R.id.senderdate);
        }
    }

}

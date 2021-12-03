package com.ambiguous.ambichat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ambiguous.ambichat.interfaces.OnClickINterface;
import com.ambiguous.ambichat.model.ModelChatList;
import com.example.ambichat.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterChatList  extends RecyclerView.Adapter<AdapterChatList.Myholder> {

    final Context context;
    final List<ModelChatList> modelChatList;
    final OnClickINterface onClickINterface;

    public AdapterChatList(Context context, List<ModelChatList> modelChatList, OnClickINterface onClickINterface) {
        this.context = context;
        this.modelChatList = modelChatList;
        this.onClickINterface = onClickINterface;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.recyclerchatlist, parent, false);
            return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.msgChaList.setText(modelChatList.get(position).getMsg());
        holder.userNameCHatList.setText(modelChatList.get(position).getName());
        Picasso.get().load(modelChatList.get(position).getImageUrl()).error(R.drawable.iconprofile)
                .placeholder(R.drawable.iconprofile)
                .into(holder.iv_chatList);

        holder.cl_chatList.setOnClickListener(v -> onClickINterface.getPositin(position));
    }

    @Override
    public int getItemCount() {
        return modelChatList.size();
    }

    public static class Myholder extends RecyclerView.ViewHolder {
        final ImageView iv_chatList;
        final TextView userNameCHatList;
        final TextView msgChaList;
        final ConstraintLayout cl_chatList;
        public Myholder(@NonNull View itemView) {
            super(itemView);

            iv_chatList = itemView.findViewById(R.id.iv_chatList);
            userNameCHatList = itemView.findViewById(R.id.userNameCHatList);
            msgChaList = itemView.findViewById(R.id.msgChaList);
            cl_chatList = itemView.findViewById(R.id.cl_chatList);
        }
    }
}

package com.example.bus_reservation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bus_reservation.Activity.MessageActivity;
import com.example.bus_reservation.Constant;
import com.example.bus_reservation.Model.Chat_model;
import com.example.bus_reservation.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_Adapter extends RecyclerView.Adapter<Chat_Adapter.ChatViewholder> {

    List<Chat_model> chat_models;
    Context context;


    public Chat_Adapter(List<Chat_model> chat_models) {
        this.chat_models = chat_models;
    }

    @NonNull
    @Override
    public ChatViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat,parent,false);
        context=parent.getContext();
        return new Chat_Adapter.ChatViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewholder holder, int position) {

        final Chat_model cmodel=chat_models.get(position);


        Glide.with(context)
                .load(Constant.Base_url_provider_image + cmodel.getImagelocation())
                .placeholder(R.drawable.app_icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.userimage);

        holder.name.setText(cmodel.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context.getApplicationContext(), MessageActivity.class);
                intent.putExtra("pid",cmodel.getProvider_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chat_models.size();
    }

    public class ChatViewholder extends RecyclerView.ViewHolder{

        TextView name;
        CircleImageView userimage;
        public ChatViewholder(@NonNull View itemView) {
            super(itemView);

        name=(TextView) itemView.findViewById(R.id.chatname);
        userimage=(CircleImageView) itemView.findViewById(R.id.ciprofilepicture);
        }
    }
}

package com.example.bus_reservation.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bus_reservation.R;

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.GithubViewHolder> {





    public class GithubViewHolder extends RecyclerView.ViewHolder {
        TextView textView,date,mydate,mytext,Tick;
        ImageView imageView,myimage;
        RelativeLayout relativeLayout;
        CardView cardView;
        public GithubViewHolder(View view) {
            super(view);

            myimage=view.findViewById(R.id.myimage);
            mydate=(TextView)view.findViewById(R.id.mydate);
            mytext=(TextView)view.findViewById(R.id.my_text);
            cardView=(CardView) view.findViewById(R.id.sender_card);

            relativeLayout=(RelativeLayout) view.findViewById(R.id.relative_id);
            date=(TextView)view.findViewById(R.id.date);
            textView=(TextView)view.findViewById(R.id.text);
            imageView=(ImageView) view.findViewById(R.id.image);
        }
    }
}

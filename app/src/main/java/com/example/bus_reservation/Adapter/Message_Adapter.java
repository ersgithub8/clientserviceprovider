package com.example.bus_reservation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bus_reservation.Activity.Show_image;
import com.example.bus_reservation.Constant;
import com.example.bus_reservation.Model.Message_Model;
import com.example.bus_reservation.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.bus_reservation.Activity.Login.MY_PREFS_NAME;

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.GithubViewHolder> {
    private Context context;
    private ArrayList<Message_Model> data;
    private Message_Adapter adapter;

    public Message_Adapter(Context context, ArrayList<Message_Model> data) {

        this.context=context;
        this.data=data;
        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public Message_Adapter.GithubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chat_cardview,parent,false);
        return new Message_Adapter.GithubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GithubViewHolder holder, final int position) {

        SharedPreferences editors = context.getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
        String Id = editors.getString("id","Null");

        String status = data.get(position).getImage_flag();

        holder.setIsRecyclable(false);

        if (data.get(position).getSender_id().equals(Id)){
            holder.cardView.setVisibility(View.GONE);
            holder.mydate.setText(data.get(position).getDate());
            if (status.equals("0")) {
                holder.myimage.setVisibility(View.GONE);
                holder.mytext.setText(data.get(position).getMsg());
            }
            else if (status.equals("1")){
                holder.mytext.setVisibility(View.GONE);
                Picasso.with(context).load(Constant.Base_url_chatimage.concat(data.get(position).getMsg()))
                        .resize(400,400)
                        .into(holder.myimage);
            }
            else {

            }
        }

        else {
            holder.date.setText(data.get(position).getDate());
            holder.relativeLayout.setVisibility(View.GONE);
            if (status.equals("0")) {
                holder.imageView.setVisibility(View.GONE);
                holder.textView.setText(data.get(position).getMsg());
            }
            else if (status.equals("1")){
                holder.textView.setVisibility(View.GONE);
                Picasso.with(context).load(Constant.Base_url_chatimage.concat(data.get(position).getMsg()))
                        .resize(200,200)
                        .into(holder.imageView);
            }
            else {

            }

        }

        final Intent intent =new Intent(context, Show_image.class);
        holder.myimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("image", Constant.Base_url_chatimage.concat(data.get(position).getMsg()));
                context.startActivity(intent);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("image",Constant.Base_url_chatimage.concat(data.get(position).getMsg()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

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

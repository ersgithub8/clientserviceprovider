package com.example.bus_reservation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.bus_reservation.Model.Membership_model;
import com.example.bus_reservation.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.bus_reservation.Activity.Login.MY_PREFS_NAME;

public class Membership_Adapter  extends RecyclerView.Adapter<Membership_Adapter.MyViewHolder> {

        private List<Membership_model> modelList;
        private Context context;
        String language;
        SharedPreferences preferences;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name,price,see,buy;
            public Button btnselect,btnselected;


            public MyViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.membershipname);
                price = (TextView) view.findViewById(R.id.membershipprice);
                see = (TextView) view.findViewById(R.id.membershipseebid);
                buy = (TextView) view.findViewById(R.id.membershipbuybid);
                btnselect=(Button)view.findViewById(R.id.select);
                btnselected=(Button)view.findViewById(R.id.selected);



            }
        }

        public Membership_Adapter(List<Membership_model> modelList) {
            this.modelList = modelList;
        }

        @Override
        public com.example.bus_reservation.Adapter.Membership_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_membership, parent, false);

            context = parent.getContext();

            return new com.example.bus_reservation.Adapter.Membership_Adapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(com.example.bus_reservation.Adapter.Membership_Adapter.MyViewHolder holder, int position) {
            final Membership_model mList = modelList.get(position);


                holder.name.setText(mList.getName());
                holder.price.setText(mList.getPrice()+"$");
                if (mList.getSee().equals("1")){
                    holder.see.setText("Can Be able to see Other Bids :- Yes");
                }else {
                    holder.see.setText("Can Be able to see Other Bids :- No");
                }
                 if (mList.getBuy().equals("1")){
                      holder.buy.setText("Can Be able to buy Other Bids :- Yes");
                }else {
                     holder.buy.setText("Can Be able to buy Other Bids :- No");
                 }
            SharedPreferences editors = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            final String membership_id = editors.getString("membershipid", "Null");

            if (mList.getMambershipid().equals(membership_id)){
                holder.btnselected.setVisibility(View.VISIBLE);

            }else{
                holder.btnselect.setVisibility(View.VISIBLE);
            }



        }

        @Override
        public int getItemCount() {
            return modelList.size();
        }

    }


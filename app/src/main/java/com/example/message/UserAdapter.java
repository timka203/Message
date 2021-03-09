package com.example.message;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<com.example.message.UserAdapter.ViewHolder>{





    private final LayoutInflater inflater;
    private final List<UserModel> users;

    UserAdapter(Context context, List<UserModel> states) {

        this.users = states;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public com.example.message.UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(com.example.message.UserAdapter.ViewHolder holder, int position) {
        UserModel user = users.get(position);

        holder.cityView.setText(user.city);
        holder.timeView.setText(user.time.getHours()+":" + user.time.getMinutes());




    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView cityView,timeView;
        ViewHolder(View view){
            super(view);

            timeView = (TextView) view.findViewById(R.id.time);
            cityView = (TextView) view.findViewById(R.id.city);


        }
    }
}
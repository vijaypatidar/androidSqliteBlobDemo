package com.vkpapps.sqlitedemo.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vkpapps.sqlitedemo.R;
import com.vkpapps.sqlitedemo.model.Profile;

import java.io.File;
import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {

    final ArrayList<Profile> profiles;
    final String profileRoot;

    public ProfileAdapter(ArrayList<Profile> profiles, String profileRoot) {
        this.profiles = profiles;
        this.profileRoot = profileRoot;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_item,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Profile profile = profiles.get(position);
        Picasso.get().load(new File(profileRoot,profile.getId()+".png")).into(holder.profilePic);
        holder.userName.setText(profile.getName());
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        AppCompatImageView profilePic;
        AppCompatTextView userName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            profilePic = itemView.findViewById(R.id.profilePic);
        }
    }
}

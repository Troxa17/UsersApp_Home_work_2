package com.example.usersapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.firstName.setText(user.firstName);
        holder.lastName.setText(user.lastName);
        holder.city.setText(user.city);
        holder.country.setText(user.country);
        Picasso.get().load(user.pictureUrl).into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        public ImageView picture;
        public TextView firstName, lastName, city, country;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.userImage);
            firstName = itemView.findViewById(R.id.firstName);
            lastName = itemView.findViewById(R.id.lastName);
            city = itemView.findViewById(R.id.city);
            country = itemView.findViewById(R.id.country);
        }
    }
}

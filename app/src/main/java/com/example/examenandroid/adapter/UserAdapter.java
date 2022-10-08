package com.example.examenandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.examenandroid.R;
import com.example.examenandroid.model.UserList;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements Filterable{

    private List<UserList.User> users;
    private List<UserList.User> usersFull;
    private Context context;

    public UserAdapter(List<UserList.User> users, Context context) {
        this.users = users;
        this.context = context;
        this.usersFull = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        holder.tvId.setText(usersFull.get(position).id.toString());
        holder.tvEmail.setText(usersFull.get(position).email);
        holder.tvFirstName.setText(usersFull.get(position).first_name);
        holder.tvLastName.setText(usersFull.get(position).last_name);
        Glide.with(context).load(usersFull.get(position).avatar).into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return usersFull.size();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<UserList.User> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(usersFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(UserList.User u : usersFull){
                    if(u.id.toString().contains(filterPattern) ||
                            u.email.contains(filterPattern)||
                            u.first_name.contains(filterPattern)||
                            u.last_name.contains(filterPattern)||
                            u.avatar.contains(filterPattern)){
                        filteredList.add(u);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            users.clear();
            users.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar;
        private TextView tvId,tvEmail,tvFirstName,tvLastName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.img_item_avatar);
            tvId = itemView.findViewById(R.id.tv_item_id);
            tvEmail = itemView.findViewById(R.id.tv_item_email);
            tvFirstName = itemView.findViewById(R.id.tv_item_first_name);
            tvLastName = itemView.findViewById(R.id.tv_item_last_name);
        }
    }
}

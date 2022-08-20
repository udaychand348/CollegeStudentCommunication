package com.example.collegestudentcommunication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.collegestudentcommunication.GroupMessageActivity;
import com.example.collegestudentcommunication.Model.Group;
import com.example.collegestudentcommunication.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter2 extends RecyclerView.Adapter<GroupAdapter2.viewHolder>{
    private Context mContext;
    private List<Group> mGroupList;

    public GroupAdapter2(Context mContext, List<Group> mGroupList) {
        this.mContext = mContext;
        this.mGroupList = mGroupList;
    }

    @NonNull
    @Override
    public GroupAdapter2.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_group_joined, parent, false);
        return new GroupAdapter2.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter2.viewHolder holder, int position) {
        Group group = mGroupList.get(position);
        holder.tv_usernameCSE.setText(group.getName());
        if (group.getImageLogo().equals("default")) {
            holder.cv_profileImageCSE.setImageResource(R.drawable.ic_launcher_background);
        } else {
            Glide.with(mContext).load(group.getImageLogo()).into(holder.cv_profileImageCSE);
        }
        holder.btn_joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupMessageActivity.class);
                intent.putExtra("name", group.getName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGroupList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        CircleImageView cv_profileImageCSE;
        TextView tv_usernameCSE;
        Button btn_joinGroup;
        LinearLayout ll_groupCSE;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            cv_profileImageCSE = itemView.findViewById(R.id.cv_profileImageCSE);
            tv_usernameCSE = itemView.findViewById(R.id.tv_usernameCSE);
            btn_joinGroup = itemView.findViewById(R.id.btn_joinGroup);
            ll_groupCSE = itemView.findViewById(R.id.ll_groupCSE);
        }
    }
}

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.viewHolder> {
    private Context mContext;
    private List<Group> mGroupList;

    public GroupAdapter(Context mContext, List<Group> mGroupList) {
        this.mContext = mContext;
        this.mGroupList = mGroupList;
    }

    @NonNull
    @Override
    public GroupAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_group, parent, false);
        return new GroupAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.viewHolder holder, int position) {
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
                createDialog(group.getCode(), group.getName());
            }
        });
    }

    private void createDialog(String groupCode,String groupName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.group_enter_code, null, false);
        builder.setView(view);
        EditText et_enterCode = view.findViewById(R.id.et_enterCode);
        Button btn_join = view.findViewById(R.id.btn_join);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = et_enterCode.getText().toString();
                if (code.isEmpty()) {
                    et_enterCode.setError("Empty Field");
                } else {
                    if (code.equals(groupCode)) {
                        alertDialog.dismiss();
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String userId = firebaseUser.getUid();
                        HashMap<String, String> hashMap1 = new HashMap<>();
                        hashMap1.put("uid", userId);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Group").child(groupName);
                        reference.child("Participants").child(userId).setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(mContext, "Joined Group Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(mContext, "Wrong Group Code", Toast.LENGTH_SHORT).show();
                    }
                }
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

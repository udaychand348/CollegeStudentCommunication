package com.example.collegestudentcommunication;

import static com.example.collegestudentcommunication.ui.Groups.GroupFragment.GROUP_ICON;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.collegestudentcommunication.Adapter.GroupMessageAdapter;
import com.example.collegestudentcommunication.Model.GroupChat;
import com.example.collegestudentcommunication.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMessageActivity extends AppCompatActivity {
    CircleImageView cv_profileImage;
    TextView tv_username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    Intent intent;

    ImageView iv_btnSend;
    ImageView iv_backBtn;
    EditText et_message;

    LinearLayout ll_usernameDetails;

    GroupMessageAdapter messageAdapter;
    List<GroupChat> mChat;

    RecyclerView recyclerView;

    String groupName;

    boolean notify = false;
    String imageUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);

        cv_profileImage = findViewById(R.id.cv_profile_image);
        tv_username = findViewById(R.id.tv_username);
        iv_btnSend = findViewById(R.id.iv_btnSend);
        et_message = findViewById(R.id.et_message);
        iv_backBtn = findViewById(R.id.iv_back);

        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ll_usernameDetails = findViewById(R.id.ll_usernameDetails);

        recyclerView = findViewById(R.id.rv_chatMessages);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        intent = getIntent();
        groupName = intent.getStringExtra("name");

        tv_username.setText(groupName);
        Glide.with(GroupMessageActivity.this).load(GROUP_ICON).into(cv_profileImage);

        iv_btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = et_message.getText().toString();
                if (!msg.isEmpty()) {
                    sendMessage(firebaseUser.getUid(), msg, groupName,imageUrl);
                } else {
                    et_message.setError("Empty Message");
                }
                et_message.setText("");
            }
        });

        readMessages(groupName);

    }

    private void sendMessage(String sender, String message, String groupName,String imageUrl) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("message", message);

        reference.child("GroupChat").child(groupName).push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                }
            }
        });

    }

    private void readMessages( String groupName) {
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("GroupChat").child(groupName);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GroupChat chat = dataSnapshot.getValue(GroupChat.class);

                    mChat.add(chat);
                    Log.d("TAG2", "message: "+ chat.getMessage());
                    messageAdapter = new GroupMessageAdapter(GroupMessageActivity.this, mChat);
                    recyclerView.setAdapter(messageAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
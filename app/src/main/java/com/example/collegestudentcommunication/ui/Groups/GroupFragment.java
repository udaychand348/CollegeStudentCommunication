package com.example.collegestudentcommunication.ui.Groups;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegestudentcommunication.Adapter.GroupAdapter;
import com.example.collegestudentcommunication.Adapter.GroupAdapter2;
import com.example.collegestudentcommunication.Model.Group;
import com.example.collegestudentcommunication.Model.Participants;
import com.example.collegestudentcommunication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private List<Group> mGroupList = new ArrayList<>();
    private RecyclerView rv_allGroups;
    private GroupAdapter groupAdapter;
    private FloatingActionButton fab_createGroup;

    ProgressDialog progressDialog;

    public static final String GROUP_ICON = "data:image/png;base64,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";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        rv_allGroups = view.findViewById(R.id.rv_allGroups);
        rv_allGroups.setHasFixedSize(true);
        rv_allGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        fab_createGroup = view.findViewById(R.id.fab_createGroup);

        progressDialog = new ProgressDialog(getContext());

        fab_createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });

        readGroupList();

        return view;
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.group_create, null, false);
        builder.setView(view);
        EditText et_nameGroup = view.findViewById(R.id.et_nameGroup);
        EditText et_entryCode = view.findViewById(R.id.et_entryCode);
        Button btn_join = view.findViewById(R.id.btn_join);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = et_entryCode.getText().toString();
                String name = et_nameGroup.getText().toString();
                if (name.isEmpty()) {
                    et_nameGroup.setError("Empty Name");
                } else if (code.isEmpty()) {
                    et_entryCode.setError("Empty Code");
                } else {
                    progressDialog.setMessage("Creating Your Group");
                    progressDialog.setTitle("Creating...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    createGroup(code, name);
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void createGroup(String code, String name) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Group").child(name);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("code", code);
        hashMap.put("imageLogo", GROUP_ICON);
        hashMap.put("name", name);
        hashMap.put("id", userId);
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    HashMap<String, String> hashMap1 = new HashMap<>();
                    hashMap1.put("uid", userId);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Group").child(name);
                    reference.child("Participants").child(userId).setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "Group Created Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Group Created Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void readGroupList() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.getUid() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Group");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mGroupList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.d("TAG", "datasnapshot: " + dataSnapshot.getValue());
                        Group group = dataSnapshot.getValue(Group.class);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Group").child(group.getName()).child("Participants");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean isAlreadyJoined = false;
                                List<Participants> participants = new ArrayList<>();
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    Participants participants1 = dataSnapshot1.getValue(Participants.class);
                                    participants.add(participants1);
                                }
                                if (group != null) {
                                    for (int i = 0; i < participants.size(); i++) {
                                        if (participants.get(i).getUid().equals(firebaseUser.getUid())) {
                                            isAlreadyJoined = true;
                                            Log.d("TAG", "participant found: " + isAlreadyJoined);
                                            break;
                                        }
                                    }
                                    if (!isAlreadyJoined) {
                                        Log.d("TAG1", "participant not found: " + isAlreadyJoined);
                                        mGroupList.add(group);
                                    }
                                } else {
                                }
                                groupAdapter = new GroupAdapter(getContext(), mGroupList);
                                rv_allGroups.setAdapter(groupAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }
}
package com.example.collegestudentcommunication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegestudentcommunication.Adapter.GroupAdapter2;
import com.example.collegestudentcommunication.Model.Group;
import com.example.collegestudentcommunication.Model.Participants;
import com.example.collegestudentcommunication.R;
import com.example.collegestudentcommunication.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    RecyclerView rv_allMyJoinedGroups;
    private List<Group> mGroupList = new ArrayList<>();
    private GroupAdapter2 groupAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_allMyJoinedGroups = root.findViewById(R.id.rv_allMyJoinedGroups);
        rv_allMyJoinedGroups.setHasFixedSize(true);
        rv_allMyJoinedGroups.setLayoutManager(new LinearLayoutManager(getContext()));

        readGroupList();

        return root;
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
                                    if (isAlreadyJoined) {
                                        Log.d("TAG1", "participant not found: " + isAlreadyJoined);
                                        mGroupList.add(group);
                                    }
                                } else {
                                }
                                groupAdapter = new GroupAdapter2(getContext(), mGroupList);
                                rv_allMyJoinedGroups.setAdapter(groupAdapter);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.vkpapps.sqlitedemo.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vkpapps.sqlitedemo.R;
import com.vkpapps.sqlitedemo.ui.adapter.ProfileAdapter;
import com.vkpapps.sqlitedemo.model.Profile;
import com.vkpapps.sqlitedemo.sqlite.Database;

import java.util.ArrayList;
import java.util.List;

public class ProfileListFragment extends Fragment {

    private final ArrayList<Profile> profiles = new ArrayList<>();
    private ProfileAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView profileList = view.findViewById(R.id.profileList);
        profileList.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ProfileAdapter(profiles, requireContext().getDir("profilesFetched", Context.MODE_PRIVATE).getPath());
        profileList.setAdapter(adapter);


        view.findViewById(R.id.btnAddProfile).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.addProfile)
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(() -> {
            List<Profile> profiles = Database.getDatabase(ProfileListFragment.this.requireContext()).getProfiles(requireContext());
            ProfileListFragment.this.profiles.clear();
            ProfileListFragment.this.profiles.addAll(profiles);
            ProfileListFragment.this.requireActivity().runOnUiThread(
                    () -> adapter.notifyDataSetChanged()
            );
        }).start();
    }
}
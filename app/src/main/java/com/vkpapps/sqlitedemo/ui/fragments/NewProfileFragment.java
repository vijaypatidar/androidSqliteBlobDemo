package com.vkpapps.sqlitedemo.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.squareup.picasso.Picasso;
import com.vkpapps.sqlitedemo.R;
import com.vkpapps.sqlitedemo.model.Profile;
import com.vkpapps.sqlitedemo.sqlite.Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class NewProfileFragment extends Fragment {

    AppCompatImageView profilePic;
    private int id = new Random().nextInt();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatEditText editName = view.findViewById(R.id.userName);
        profilePic = view.findViewById(R.id.profilePic);
        profilePic.setOnClickListener(v -> {
            Intent intent =  new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent,"select profile"),100);
        });

        view.findViewById(R.id.btnSave).setOnClickListener(v ->{
            String name = editName.getText().toString();
            if (name.length()>0){
                Profile profile = new Profile();
                profile.setId(id);
                profile.setName(name);
                Database.getDatabase(requireContext()).
                        addProfile(profile,requireContext());
                Navigation.findNavController(view).popBackStack();
                Toast.makeText(requireContext(), "new profile added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK){
            Uri data1 = data.getData();
            Picasso.get().load(data1).into(profilePic);
            try {
                InputStream inputStream = requireActivity().getContentResolver().openInputStream(data1);
                File file = new File(requireContext().getDir("profiles", Context.MODE_PRIVATE),id+"");
                FileOutputStream fos = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int read;
                while ((read=inputStream.read(bytes))>0){
                    fos.write(bytes,0,read);
                }
                fos.flush();
                fos.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
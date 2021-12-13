package com.example.control_inventario.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.control_inventario.R;
import com.example.control_inventario.databinding.FragmentAltasProdBinding;
import com.example.control_inventario.databinding.FragmentHomeBinding;
import com.example.control_inventario.ui.Productos.Altas.AltasProdViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        VideoView mivdeo = root.findViewById(R.id.videoView1);
        String videop = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.control;
        Uri uri = Uri.parse(videop);
        mivdeo.setVideoURI(uri);

        MediaController mediaController = new MediaController(getContext());
        mivdeo.setMediaController(mediaController);
        mediaController.setAnchorView(mivdeo);

        return root;
    }





}
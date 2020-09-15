package com.sid.musikale.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sid.musikale.R;
import com.sid.musikale.adapter.MusicAdapter;
import com.sid.musikale.adapter.albumAdapter;

import static com.sid.musikale.MainActivity.albums;
import static com.sid.musikale.MainActivity.musicFiles;


public class ListFragment extends Fragment {
    RecyclerView recyclerView;
    albumAdapter albumAdapter;



    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView= view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        if (!(albums.size()<1)){

            albumAdapter=new albumAdapter(getContext(),albums);
            recyclerView.setAdapter(albumAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));


        }
        return view;

    }
}
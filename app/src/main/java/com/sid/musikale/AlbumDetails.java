package com.sid.musikale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sid.musikale.adapter.AlbumDetailAdapter;
import com.sid.musikale.models.MusicFiles;

import java.util.ArrayList;

import static com.sid.musikale.MainActivity.musicFiles;

public class AlbumDetails extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView albumDetailImage;
    String albumName;
    ArrayList<MusicFiles> albumSongs= new ArrayList<>();
    AlbumDetailAdapter albumDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        recyclerView = findViewById(R.id.recyclerView_detail);
        albumDetailImage= findViewById(R.id.album_detail_image);
        albumName = getIntent().getStringExtra("albumName");
        int j = 0;
        for (int i = 0 ; i< musicFiles.size() ; i++){
            if (albumName.equals(musicFiles.get(i).getAlbum())){
                albumSongs.add(j, musicFiles.get(i));
                j ++;
            }
        }
        byte[] image= getAlbumArt(albumSongs.get(0).getPath());
        if (image != null){
            Glide.with(this).load(image).into(albumDetailImage);
        }
        else {
            Glide.with(this).load(R.drawable.song).into(albumDetailImage);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(albumSongs.size() < 1)){
            albumDetailAdapter = new AlbumDetailAdapter(this,albumSongs);
            recyclerView.setAdapter(albumDetailAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

        }
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
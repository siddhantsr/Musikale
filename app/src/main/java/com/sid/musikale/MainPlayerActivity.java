package com.sid.musikale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sid.musikale.models.MusicFiles;

import java.util.ArrayList;
import java.util.Random;

import static com.sid.musikale.MainActivity.musicFiles;
import static com.sid.musikale.MainActivity.repeat;
import static com.sid.musikale.MainActivity.shuffle;
import static com.sid.musikale.adapter.AlbumDetailAdapter.albumFiles;
import static com.sid.musikale.adapter.MusicAdapter.mFiles;

public class MainPlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    TextView songName, artistName,duration, durationTotal;
    ImageView coverArt, nextBtn,prevBtn,shuffleBtn,repeatBtn, backButton;
    SeekBar seekBar;
    FloatingActionButton play_pause;
    int position= -1;
    static ArrayList<MusicFiles> songsList= new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler= new Handler();
    private Thread playThread, prevThread, nextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_player);
        initViews();
        getIntentMethod();
        songName.setText(songsList.get(position).getTitle());
        artistName.setText(songsList.get(position).getSinger());
        mediaPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer !=null && fromUser){
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        MainPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer !=null){
                    int mCurrentPosition= mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    duration.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffle){

                    shuffle = false;
                    shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_24);
                }
                else{
                    shuffle = true;
                    shuffleBtn.setImageResource(R.drawable.shuffle_off);
                }

            }
        });
        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeat){

                    repeat=false;
                    repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_off);
                }
                else {
                    repeat=true;
                    repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_on);
                }
            }
        });

        backButton =findViewById(R.id.bck_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPlayerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void prevThreadBtn() {
        prevThread=new Thread(){

            @Override
            public void run() {
                super.run();
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Click_prev_Btn();
                    }
                });
            }
        };
        prevThread.start();

    }

    private void Click_prev_Btn() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shuffle && !repeat){
                position = getRandom(songsList.size() -1);
            }
            else if (!shuffle && !repeat) {
                position = ((position - 1) < 0 ? (songsList.size() -1) : (position - 1));
            }

            uri= Uri.parse(songsList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(songsList.get(position).getTitle());
            artistName.setText(songsList.get(position).getSinger());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            MainPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer !=null){
                        int mCurrentPosition= mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            play_pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            mediaPlayer.start();

        }
        else {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shuffle && !repeat){
                position = getRandom(songsList.size() -1);
            }
            else if (!shuffle && !repeat) {
                position = ((position - 1) < 0 ? (songsList.size() -1) : (position - 1));
            }
            uri= Uri.parse(songsList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(songsList.get(position).getTitle());
            artistName.setText(songsList.get(position).getSinger());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            MainPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer !=null){
                        int mCurrentPosition= mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            play_pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
        }
    }

    private void nextThreadBtn() {
        nextThread=new Thread(){

            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Click_next_Btn();
                    }
                });
            }
        };
        nextThread.start();

    }

    private void Click_next_Btn() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shuffle && !repeat){
                position = getRandom(songsList.size() -1);
            }
            else if (!shuffle && !repeat) {
                position = ((position + 1) % songsList.size());
            }
            uri= Uri.parse(songsList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(songsList.get(position).getTitle());
            artistName.setText(songsList.get(position).getSinger());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            MainPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer !=null){
                        int mCurrentPosition= mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            play_pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            mediaPlayer.start();

        }
        else {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shuffle && !repeat){
                position = getRandom(songsList.size() -1);
            }
            else if (!shuffle && !repeat) {
                position = ((position + 1) % songsList.size());
            }
            uri= Uri.parse(songsList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(songsList.get(position).getTitle());
            artistName.setText(songsList.get(position).getSinger());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            MainPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer !=null){
                        int mCurrentPosition= mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            play_pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
        }
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);

    }

    private void playThreadBtn() {
        playThread=new Thread(){

            @Override
            public void run() {
                super.run();
                play_pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Click_play_pauseBtn();
                    }
                });
            }
        };
        playThread.start();
    }

    private void Click_play_pauseBtn() {
        if (mediaPlayer.isPlaying()){
            play_pause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            MainPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer !=null){
                        int mCurrentPosition= mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
        else {

            play_pause.setImageResource(R.drawable.ic_baseline_pause_24);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration()/ 1000);
            MainPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer !=null){
                        int mCurrentPosition= mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });

        }
    }

    private String formattedTime(int mCurrentPosition) {
        String total = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        total = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return total;

        }
    }

    private void getIntentMethod() {
        position= getIntent().getIntExtra("position", -1);
        String sender = getIntent().getStringExtra("sender");
        if (sender !=null && sender.equals("albumDetails")){
            songsList = albumFiles;
        }
        else {
            songsList = mFiles;
        }
        if (songsList !=null){
            play_pause.setImageResource(R.drawable.ic_baseline_pause_24);
            uri=Uri.parse(songsList.get(position).getPath());
        }
         if (mediaPlayer !=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();


        }
        else {
            mediaPlayer= MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        metaData(uri);
    }

    private void initViews() {
        songName= findViewById(R.id.song_name);
        artistName= findViewById(R.id.artist_name);
        duration= findViewById(R.id.duration);
        durationTotal= findViewById(R.id.durationTotal);
        coverArt= findViewById(R.id.middle_img);
        nextBtn= findViewById(R.id.next);
        prevBtn= findViewById(R.id.prev);
        shuffleBtn= findViewById(R.id.shuffle);
        repeatBtn= findViewById(R.id.repeat);
        seekBar= findViewById(R.id.seekBar);
        play_pause= findViewById(R.id.play_pause);
    }

    private void metaData (Uri uri){

        MediaMetadataRetriever retriever =new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int duration_Total = Integer.parseInt(songsList.get(position).getDuration()) /1000;
        durationTotal.setText(formattedTime(duration_Total));
        byte[] art = retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if (art !=null){

            bitmap = BitmapFactory.decodeByteArray(art,0, art.length);
            ImageAnimation(this, coverArt,bitmap);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch !=null){
                        ImageView gradient = findViewById(R.id.gradient);
                        RelativeLayout mContainer = findViewById(R.id.song_container);
                        gradient.setBackgroundResource(R.drawable.btn_bg);
                        mContainer.setBackgroundResource(R.drawable.main_player);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{swatch.getRgb(), 0x00000000});
                        gradient.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{swatch.getRgb(), swatch.getRgb()});
                        mContainer.setBackground(gradientDrawableBg);
                        songName.setTextColor(swatch.getTitleTextColor());
                        artistName.setTextColor(swatch.getBodyTextColor());
                    }
                    else {
                        ImageView gradient = findViewById(R.id.gradient);
                        RelativeLayout mContainer = findViewById(R.id.song_container);
                        gradient.setBackgroundResource(R.drawable.btn_bg);
                        mContainer.setBackgroundResource(R.drawable.main_player);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xff000000, 0x00000000});
                        gradient.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xff000000, 0xff000000});
                        mContainer.setBackground(gradientDrawableBg);
                        songName.setTextColor(Color.WHITE);
                        artistName.setTextColor(Color.DKGRAY);
                    }
                }

            });
        }
        else {
            Glide.with(this).asBitmap().load(R.drawable.song).into(coverArt);
            ImageView gradient = findViewById(R.id.gradient);
            RelativeLayout mContainer = findViewById(R.id.song_container);
            gradient.setBackgroundResource(R.drawable.btn_bg);
            mContainer.setBackgroundResource(R.drawable.main_player);
            songName.setTextColor(Color.WHITE);
            artistName.setTextColor(Color.DKGRAY);
        }
    }
    public void ImageAnimation(final Context context, final ImageView imageView, final Bitmap bitmap ){
        Animation fadeOut= AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        final Animation fadeIn= AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(fadeIn);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(fadeOut);


    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Click_next_Btn();
        if (mediaPlayer !=null){
           mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
           mediaPlayer.start();
           mediaPlayer.setOnCompletionListener(this);
        }
    }
}
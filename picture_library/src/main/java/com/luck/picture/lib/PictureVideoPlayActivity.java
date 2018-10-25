package com.luck.picture.lib;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.tools.MyUtilHelper;

public class PictureVideoPlayActivity extends PictureBaseActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, View.OnClickListener {
    private String video_path = "",cover_path="";
    private ImageView picture_left_back, videoCover;
    private MediaController mMediaController;
    private ProgressBar mProgressBar;
    private VideoView mVideoView;
    private ImageView iv_play;
    private int mPositionWhenPaused = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity_video_play);
        video_path = getIntent().getStringExtra("video_path");

        picture_left_back = (ImageView) findViewById(R.id.picture_left_back);
        mVideoView = (VideoView) findViewById(R.id.video_view);
        videoCover = findViewById(R.id.video_cover);
        mVideoView.setBackgroundColor(Color.BLACK);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        mProgressBar = (ProgressBar) findViewById(R.id.loading);
        mMediaController = new MediaController(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnPreparedListener(this);
        //mVideoView.setMediaController(mMediaController);
        picture_left_back.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        MyUtilHelper.hideBottomUIMenu(this);
        mVideoView.setOnClickListener(this);
        findViewById(R.id.main_layout).setOnClickListener(this);
        if(getIntent().hasExtra("cover_path")) {
            cover_path = getIntent().getStringExtra("cover_path");
            //Log.d("Acheng","视频地址:"+cover_path);
            Glide.with(this).load(cover_path).into(videoCover);
        }
    }


    @Override
    public void onStart() {
        // Play Video
        mVideoView.setVideoPath(video_path);
        mVideoView.start();
        mProgressBar.setVisibility(View.VISIBLE);
        super.onStart();
    }

    @Override
    public void onPause() {
        // Stop video when the activity is pause.
        mPositionWhenPaused = mVideoView.getCurrentPosition();
        mVideoView.stopPlayback();

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMediaController = null;
        mVideoView = null;
        iv_play = null;
        super.onDestroy();
    }

    @Override
    public void onResume() {
        // Resume video player
        if (mPositionWhenPaused >= 0) {
            mVideoView.seekTo(mPositionWhenPaused);
            mPositionWhenPaused = -1;
        }

        super.onResume();
    }

    @Override
    public boolean onError(MediaPlayer player, int arg1, int arg2) {
        mVideoView.stopPlayback(); //播放异常，则停止播放，防止弹窗使界面阻塞
        mProgressBar.setVisibility(View.GONE);
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (null != iv_play) {
            iv_play.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.picture_left_back) {
            finish();
            overridePendingTransition(0, R.anim.a3);
        } else if (id == R.id.iv_play) {
            mVideoView.start();
            iv_play.setVisibility(View.INVISIBLE);
        } else if (id == R.id.video_view) {
            finish();
            overridePendingTransition(0, R.anim.a3);
        } else if (id == R.id.main_layout) {
            finish();
            overridePendingTransition(0, R.anim.a3);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new ContextWrapper(newBase) {
            @Override
            public Object getSystemService(String name) {
                if (Context.AUDIO_SERVICE.equals(name)) {
                    return getApplicationContext().getSystemService(name);
                }
                return super.getSystemService(name);
            }
        });
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // video started
                    mVideoView.setBackgroundColor(Color.TRANSPARENT);
                    mProgressBar.setVisibility(View.GONE);
                    videoCover.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });
    }
}

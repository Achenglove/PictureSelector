package com.ccr.pictureselector.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.ccr.achenglibrary.photopicker.activity.CCRPhotoPreviewActivity;
import com.ccr.pictureselector.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * @Author: Acheng
 * @Email: 345887272@qq.com
 * @Date: 2018-01-06 14:54
 * @Version: V1.0 <描述当前版本功能>
 * 在此写用途
 */

public class MainActivity extends AppCompatActivity{
    ImageView mImageView;
    File downloadDir;
    ArrayList<String> imageList=new ArrayList<>();
    String mString;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mImageView=(ImageView) findViewById(R.id.m_image);
        mString="http://img01.taopic.com/160117/318752-16011F9334648.jpg";
        imageList.add("http://img01.taopic.com/160117/318752-16011F9334648.jpg");
        Picasso.with(this).load(mString).into(mImageView);
        downloadDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/ichuangqi/创骐");
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CCRPhotoPreviewActivity.newIntent(MainActivity.this, true ? downloadDir : null, mString, true,false,false));
                overridePendingTransition(0,0);
            }
        });

    }
}

/**
 * Copyright 2016 bingoogolapple
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ccr.achenglibrary.photopicker.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccr.achenglibrary.R;
import com.ccr.achenglibrary.photopicker.adapter.CCRPhotoPageAdapter;
import com.ccr.achenglibrary.photopicker.imageloader.CCRImage;
import com.ccr.achenglibrary.photopicker.imageloader.CCRImageLoader;
import com.ccr.achenglibrary.photopicker.listener.CCROnNoDoubleClickListener;
import com.ccr.achenglibrary.photopicker.util.CCRAsyncTask;
import com.ccr.achenglibrary.photopicker.util.CCRPhotoPickerUtil;
import com.ccr.achenglibrary.photopicker.util.CCRSavePhotoTask;
import com.ccr.achenglibrary.photopicker.util.MyUtilHelper;
import com.ccr.achenglibrary.photopicker.widget.CCRHackyViewPager;
import com.ccr.achenglibrary.photoview.PhotoViewAttacher;

import java.io.File;
import java.util.ArrayList;


/**
 * 在此写用途
 * 图片预览界面
 *
 * @Author: Acheng
 * @Email: 345887272@qq.com
 * @Date: 2017-09-22 16:47
 * @Version: V1.0 <描述当前版本功能>
 */

public class CCRPhotoPreviewActivity extends CCRPPToolbarActivity implements PhotoViewAttacher.OnViewTapListener, CCRAsyncTask.Callback<Void>,CCRPhotoPageAdapter.LongClickListener {
    private static final String EXTRA_SAVE_IMG_DIR = "EXTRA_SAVE_IMG_DIR";
    private static final String EXTRA_PREVIEW_IMAGES = "EXTRA_PREVIEW_IMAGES";
    private static final String EXTRA_CURRENT_POSITION = "EXTRA_CURRENT_POSITION";
    private static final String EXTRA_IS_SINGLE_PREVIEW = "EXTRA_IS_SINGLE_PREVIEW";
    private static final String EXTRA_PHOTO_PATH = "EXTRA_PHOTO_PATH";
    private static final String CLICK_CLOSE = "CLICK_CLOSE";
    private static final String IS_SHARE = "IS_SHARE";
    private static final String IS_SHOW_SAVE = "IS_SHOW_SAVE";

    private TextView mTitleTv;
    private TextView numberText;
    private TextView saveButton;
    private TextView shareButton;
    private ImageView mDownloadIv;
    private CCRHackyViewPager mContentHvp;
    private CCRPhotoPageAdapter mPhotoPageAdapter;

    private boolean mIsSinglePreview;//判断是单张图片还是多张图片

    private File mSaveImgDir;

    private boolean mIsHidden = false;
    private boolean isClickClose = false;//是否单击图片关闭预览
    private boolean isShare = false;//是否分享
    private boolean isSave = false;//是否显示保存按钮
    private CCRSavePhotoTask mSavePhotoTask;
    private ArrayList<String> previewImages;//图片集合

    /**
     * 上一次标题栏显示或隐藏的时间戳
     */
    private long mLastShowHiddenTime;

    /**
     * 获取查看多张图片的intent
     *
     * @param context
     * @param saveImgDir      保存图片的目录，如果传null，则没有保存图片功能
     * @param previewImages   当前预览的图片目录里的图片路径集合
     * @param currentPosition 当前预览图片的位置
     * @param clickClose      单击图片是否关闭图片预览功能
     * @param isShare      是否显示分享按钮
     * @param isSave      是否显示保存按钮
     * @return
     */
    public static Intent newIntent(Context context, File saveImgDir, ArrayList<String> previewImages, int currentPosition, boolean clickClose,boolean isShare,boolean isSave) {
        Intent intent = new Intent(context, CCRPhotoPreviewActivity.class);
        intent.putExtra(EXTRA_SAVE_IMG_DIR, saveImgDir);
        intent.putStringArrayListExtra(EXTRA_PREVIEW_IMAGES, previewImages);
        intent.putExtra(EXTRA_CURRENT_POSITION, currentPosition);
        intent.putExtra(EXTRA_IS_SINGLE_PREVIEW, false);
        intent.putExtra(CLICK_CLOSE, clickClose);
        intent.putExtra(IS_SHARE, isShare);
        intent.putExtra(IS_SHOW_SAVE, isSave);
        return intent;
    }

    /**
     * 获取查看单张图片的intent
     *
     * @param context
     * @param saveImgDir 保存图片的目录，如果传null，则没有保存图片功能
     * @param photoPath  图片路径
     * @param clickClose 单击图片是否关闭图片预览功能
     * @param isShare 是否显示分享按钮
     * @return
     */
    public static Intent newIntent(Context context, File saveImgDir, String photoPath, boolean clickClose,boolean isShare,boolean isSave) {
        Intent intent = new Intent(context, CCRPhotoPreviewActivity.class);
        intent.putExtra(EXTRA_SAVE_IMG_DIR, saveImgDir);
        intent.putExtra(EXTRA_PHOTO_PATH, photoPath);
        intent.putExtra(EXTRA_CURRENT_POSITION, 0);
        intent.putExtra(EXTRA_IS_SINGLE_PREVIEW, true);
        intent.putExtra(CLICK_CLOSE, clickClose);
        intent.putExtra(IS_SHARE, isShare);
        intent.putExtra(IS_SHOW_SAVE, isSave);
        return intent;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setNoLinearContentView(R.layout.bga_pp_activity_photo_preview);
        mContentHvp = getViewById(R.id.hvp_photo_preview_content);
        numberText = (TextView) findViewById(R.id.number_text);
        saveButton = (TextView) findViewById(R.id.save_button);
        shareButton = (TextView) findViewById(R.id.share_button);
        mToolbar.setVisibility(View.GONE);//图片预览时隐藏导航栏
        MyUtilHelper.hideBottomUIMenu(this);//进入图片预览界面隐藏虚拟按键

    }

    @Override
    protected void setListener() {
        mContentHvp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                renderTitleTv();
//                Log.d("Acheng","当前位置:"+previewImages.get(position));
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSavePhotoTask == null) {
                    savePic();
                }
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CCRPhotoPreviewActivity.this, "分享", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mSaveImgDir = (File) getIntent().getSerializableExtra(EXTRA_SAVE_IMG_DIR);
        isClickClose = getIntent().getBooleanExtra(CLICK_CLOSE, false);
        isShare = getIntent().getBooleanExtra(IS_SHARE, false);
        isSave = getIntent().getBooleanExtra(IS_SHOW_SAVE, false);
        if(!isShare){
            shareButton.setVisibility(View.INVISIBLE);
        }
        if (mSaveImgDir != null && !mSaveImgDir.exists()) {
            mSaveImgDir.mkdirs();
        }

        previewImages = getIntent().getStringArrayListExtra(EXTRA_PREVIEW_IMAGES);

        mIsSinglePreview = getIntent().getBooleanExtra(EXTRA_IS_SINGLE_PREVIEW, false);
        if (mIsSinglePreview) {
            previewImages = new ArrayList<>();
            previewImages.add(getIntent().getStringExtra(EXTRA_PHOTO_PATH));
        }

        int currentPosition = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);
        mPhotoPageAdapter = new CCRPhotoPageAdapter(this, this, previewImages,this);
        mContentHvp.setAdapter(mPhotoPageAdapter);
        mContentHvp.setCurrentItem(currentPosition);
//        Log.d("Acheng","当前位置:"+previewImages.get(currentPosition));

        // 过2秒隐藏标题栏
        if (!isClickClose) {
            mToolbar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hiddenTitleBar();
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bga_pp_menu_photo_preview, menu);
        MenuItem menuItem = menu.findItem(R.id.item_photo_preview_title);
        View actionView = menuItem.getActionView();

        mTitleTv = (TextView) actionView.findViewById(R.id.tv_photo_preview_title);
        mDownloadIv = (ImageView) actionView.findViewById(R.id.iv_photo_preview_download);



        mDownloadIv.setOnClickListener(new CCROnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mSavePhotoTask == null) {
                    savePic();
                }
            }
        });
        //根据保存图片路径地址是否为空来判断是否显示保存功能
//        if (mSaveImgDir == null) {
//            mDownloadIv.setVisibility(View.INVISIBLE);
//            saveButton.setVisibility(View.INVISIBLE);
//        }

        if (!isSave) {
            mDownloadIv.setVisibility(View.INVISIBLE);
            saveButton.setVisibility(View.INVISIBLE);
        }
        renderTitleTv();

        return true;
    }

    private void renderTitleTv() {
        if (mTitleTv == null || mPhotoPageAdapter == null) {
            return;
        }

        if (mIsSinglePreview) {
            mTitleTv.setText(R.string.bga_pp_view_photo);
        } else {
//            mTitleTv.setText((mContentHvp.getCurrentItem() + 1) + "/" + mPhotoPageAdapter.getCount());
            numberText.setText((mContentHvp.getCurrentItem() + 1) + "/" + mPhotoPageAdapter.getCount());
        }
    }

    //单击图片时的操作(isClickClose  true单击关闭 false单击隐藏与显示标题栏)
    @Override
    public void onViewTap(View view, float x, float y) {
        if (isClickClose) {
            finish();
        } else {
            if (System.currentTimeMillis() - mLastShowHiddenTime > 500) {
                mLastShowHiddenTime = System.currentTimeMillis();
                if (mIsHidden) {
                    showTitleBar();
                } else {
                    hiddenTitleBar();
                }
            }
        }
    }



    private void showTitleBar() {
        if (mToolbar != null) {
            ViewCompat.animate(mToolbar).translationY(0).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = false;
                }
            }).start();
        }
    }

    private void hiddenTitleBar() {
        if (mToolbar != null) {
            ViewCompat.animate(mToolbar).translationY(-mToolbar.getHeight()).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = true;
                }
            }).start();
        }
    }


    private synchronized void savePic() {
        if (mSavePhotoTask != null) {
            return;
        }

        final String url = mPhotoPageAdapter.getItem(mContentHvp.getCurrentItem());
        File file;
        if (url.startsWith("file")) {
            file = new File(url.replace("file://", ""));
            if (file.exists()) {
                CCRPhotoPickerUtil.showSafe(getString(R.string.bga_pp_save_img_success_folder, file.getParentFile().getAbsolutePath()));
                updateImg(file);
                return;
            }
        }

        // 通过MD5加密url生成文件名，避免多次保存同一张图片
        file = new File(mSaveImgDir, CCRPhotoPickerUtil.md5(url) + ".jpg");
        if (file.exists()) {
            CCRPhotoPickerUtil.showSafe(getString(R.string.bga_pp_save_img_success_folder, mSaveImgDir.getAbsolutePath()));
            updateImg(file);
            return;
        }

        mSavePhotoTask = new CCRSavePhotoTask(this, this, file);
        CCRImage.download(url, new CCRImageLoader.DownloadDelegate() {
            @Override
            public void onSuccess(String url, Bitmap bitmap) {
                mSavePhotoTask.setBitmapAndPerform(bitmap);
            }

            @Override
            public void onFailed(String url) {
                mSavePhotoTask = null;
                CCRPhotoPickerUtil.show(R.string.bga_pp_save_img_failure);
            }
        });

    }

    /**
     * 更新图片
     * @param file
     */
    private void updateImg(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file
    }

    @Override
    public void onPostExecute(Void aVoid) {
        mSavePhotoTask = null;
    }

    @Override
    public void onTaskCancelled() {
        mSavePhotoTask = null;
    }

    @Override
    protected void onDestroy() {
        if (mSavePhotoTask != null) {
            mSavePhotoTask.cancelTask();
            mSavePhotoTask = null;
        }
        MyUtilHelper.showBottomUIMenu(this);//退出图片预览显示虚拟按键
        super.onDestroy();
    }

    @Override
    public boolean onLongClick(View v) {
        savePic();
        return true;
    }
}
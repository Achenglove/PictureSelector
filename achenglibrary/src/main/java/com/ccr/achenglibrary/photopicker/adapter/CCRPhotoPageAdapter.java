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
package com.ccr.achenglibrary.photopicker.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import com.ccr.achenglibrary.R;
import com.ccr.achenglibrary.photopicker.imageloader.CCRImage;
import com.ccr.achenglibrary.photopicker.util.CCRBrowserPhotoViewAttacher;
import com.ccr.achenglibrary.photopicker.util.CCRPhotoPickerUtil;
import com.ccr.achenglibrary.photopicker.widget.CCRImageView;
import com.ccr.achenglibrary.photoview.PhotoViewAttacher;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;


/**
 * 在此写用途
 *大图预览适配器
 * @Author: Acheng
 * @Email: 345887272@qq.com
 * @Date: 2017-09-22 16:47
 * @Version: V1.0 <描述当前版本功能>
 */

public class CCRPhotoPageAdapter extends PagerAdapter {
    private ArrayList<String> mPreviewImages;
    private PhotoViewAttacher.OnViewTapListener mOnViewTapListener;
    private Activity mActivity;
    LongClickListener mLongClickListener;

    public CCRPhotoPageAdapter(Activity activity, PhotoViewAttacher.OnViewTapListener onViewTapListener, ArrayList<String> previewImages,LongClickListener pLongClickListener) {
        mOnViewTapListener = onViewTapListener;
        mPreviewImages = previewImages;
        mActivity = activity;
        this.mLongClickListener=pLongClickListener;
    }

    @Override
    public int getCount() {
        return mPreviewImages == null ? 0 : mPreviewImages.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        final CCRImageView imageView = new CCRImageView(container.getContext());
        container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final CCRBrowserPhotoViewAttacher photoViewAttacher = new CCRBrowserPhotoViewAttacher(imageView);
        photoViewAttacher.setOnViewTapListener(mOnViewTapListener);
        imageView.setDelegate(new CCRImageView.Delegate() {
            @Override
            public void onDrawableChanged(Drawable drawable) {
                if (drawable != null && drawable.getIntrinsicHeight() > drawable.getIntrinsicWidth() && drawable.getIntrinsicHeight() > CCRPhotoPickerUtil.getScreenHeight()) {
                    photoViewAttacher.setIsSetTopCrop(true);
                    photoViewAttacher.setUpdateBaseMatrix();
                } else {
                    photoViewAttacher.update();
                }
            }
        });
        photoViewAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Toast.makeText(mActivity, "长按", Toast.LENGTH_SHORT).show();
                if(mLongClickListener!=null){
                    mLongClickListener.onLongClick(imageView);
                }
                return true;
            }
        });
        CCRImage.display(imageView, R.mipmap.bga_pp_ic_holder_dark, mPreviewImages.get(position), CCRPhotoPickerUtil.getScreenWidth(), CCRPhotoPickerUtil.getScreenHeight());
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public String getItem(int position) {
        return mPreviewImages == null ? "" : mPreviewImages.get(position);
    }

    public interface LongClickListener{
        boolean onLongClick(View v);
    }
}
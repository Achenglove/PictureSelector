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
package cn.bingoogolapple.photopicker.adapter;

import android.graphics.drawable.Drawable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.bingoogolapple.photopicker.R;
import cn.bingoogolapple.photopicker.imageloader.CCRImage;
import cn.bingoogolapple.photopicker.util.CCRBrowserPhotoViewAttacher;
import cn.bingoogolapple.photopicker.util.CCRPhotoPickerUtil;
import cn.bingoogolapple.photopicker.widget.CCRImageView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * @author Acheng
 * @Created on 2020/5/16.
 * @Email 345887272@qq.com
 * @Description 说明:图预览适配器
 */
public class CCRPhotoPageAdapter extends PagerAdapter {
    private ArrayList<String> mPreviewPhotos;
    private PhotoViewAttacher.OnViewTapListener mOnViewTapListener;
    LongClickListener mLongClickListener;

    public CCRPhotoPageAdapter(PhotoViewAttacher.OnViewTapListener onViewTapListener, ArrayList<String> previewPhotos,LongClickListener pLongClickListener) {
        mOnViewTapListener = onViewTapListener;
        mPreviewPhotos = previewPhotos;
        this.mLongClickListener=pLongClickListener;
    }

    @Override
    public int getCount() {
        return mPreviewPhotos == null ? 0 : mPreviewPhotos.size();
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
                if(mLongClickListener!=null){
                    mLongClickListener.onLongClick(imageView);
                }
                return true;
            }
        });
        CCRImage.display(imageView, R.mipmap.bga_pp_ic_holder_dark, mPreviewPhotos.get(position), CCRPhotoPickerUtil.getScreenWidth(), CCRPhotoPickerUtil.getScreenHeight());

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
        return mPreviewPhotos == null ? "" : mPreviewPhotos.get(position);
    }
    public interface LongClickListener{
        boolean onLongClick(View v);
    }
}
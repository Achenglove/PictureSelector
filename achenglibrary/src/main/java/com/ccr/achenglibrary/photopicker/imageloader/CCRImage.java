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
package com.ccr.achenglibrary.photopicker.imageloader;

import android.app.Activity;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

/**
 * 在此写用途
 *
 * @Author: Acheng
 * @Email: 345887272@qq.com
 * @Date: 2017-09-22 16:47
 * @Version: V1.0 <描述当前版本功能>
 */
public class CCRImage {
    private static CCRImageLoader sImageLoader;

    private CCRImage() {
    }

    private static final CCRImageLoader getImageLoader() {
        if (sImageLoader == null) {
            synchronized (CCRImage.class) {
                if (sImageLoader == null) {
                    if (isClassExists("com.bumptech.glide.Glide")) {
                        sImageLoader = new CCRGlideImageLoader();
                    } else if (isClassExists("com.squareup.picasso.Picasso")) {
                        sImageLoader = new CCRPicassoImageLoader();
                    } else if (isClassExists("com.nostra13.universalimageloader.core.ImageLoader")) {
                        sImageLoader = new CCRUILImageLoader();
                    } else if (isClassExists("org.xutils.x")) {
                        sImageLoader = new CCRXUtilsImageLoader();
                    } else {
                        throw new RuntimeException("必须在你的build.gradle文件中配置「Glide、Picasso、universal-image-loader、XUtils3」中的某一个图片加载库的依赖");
                    }
                }
            }
        }
        return sImageLoader;
    }

    private static final boolean isClassExists(String classFullName) {
        try {
            Class.forName(classFullName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static void display(ImageView imageView, @DrawableRes int loadingResId, @DrawableRes int failResId, String path, int width, int height, final CCRImageLoader.DisplayDelegate delegate) {
        getImageLoader().display(imageView, path, loadingResId, failResId, width, height, delegate);
    }

    public static void display(ImageView imageView, @DrawableRes int placeholderResId, String path, int width, int height, final CCRImageLoader.DisplayDelegate delegate) {
        display(imageView, placeholderResId, placeholderResId, path, width, height, delegate);
    }

    public static void display(ImageView imageView, @DrawableRes int placeholderResId, String path, int width, int height) {
        display(imageView, placeholderResId, path, width, height, null);
    }

    public static void display(ImageView imageView, @DrawableRes int placeholderResId, String path, int size) {
        display(imageView, placeholderResId, path, size, size);
    }

    public static void download(String path, final CCRImageLoader.DownloadDelegate delegate) {
        getImageLoader().download(path, delegate);
    }

    /**
     * 暂停加载
     *
     * @param activity
     */
    public static void pause(Activity activity) {
        getImageLoader().pause(activity);
    }

    /**
     * @param activity
     */
    public static void resume(Activity activity) {
        getImageLoader().resume(activity);
    }
}

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
package cn.bingoogolapple.photopicker.pw;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.pasture.baseadapterlib.CCROnRVItemClickListener;
import com.pasture.baseadapterlib.CCRRecyclerViewAdapter;
import com.pasture.baseadapterlib.CCRViewHolderHelper;

import java.util.ArrayList;


import cn.bingoogolapple.photopicker.R;
import cn.bingoogolapple.photopicker.imageloader.CCRImage;
import cn.bingoogolapple.photopicker.model.CCRPhotoFolderModel;
import cn.bingoogolapple.photopicker.util.CCRPhotoPickerUtil;


/**
 * @author Acheng
 * @Created on 2020/5/16.
 * @Email 345887272@qq.com
 * @Description 说明:
 */
public class CCRPhotoFolderPw extends CCRBasePopupWindow implements CCROnRVItemClickListener {
    public static final int ANIM_DURATION = 300;
    private LinearLayout mRootLl;
    private RecyclerView mContentRv;
    private FolderAdapter mFolderAdapter;
    private Delegate mDelegate;
    private int mCurrentPosition;

    public CCRPhotoFolderPw(Activity activity, View anchorView, Delegate delegate) {
        super(activity, R.layout.bga_pp_pw_photo_folder, anchorView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mDelegate = delegate;
    }

    @Override
    protected void initView() {
        mRootLl = findViewById(R.id.ll_photo_folder_root);
        mContentRv = findViewById(R.id.rv_photo_folder_content);
    }

    @Override
    protected void setListener() {
        mRootLl.setOnClickListener(this);
        mFolderAdapter = new FolderAdapter(mContentRv);
        mFolderAdapter.setOnRVItemClickListener(this);
    }

    @Override
    protected void processLogic() {
        setAnimationStyle(android.R.style.Animation);
        setBackgroundDrawable(new ColorDrawable(0x90000000));

        mContentRv.setLayoutManager(new LinearLayoutManager(mActivity));
        mContentRv.setAdapter(mFolderAdapter);
    }

    /**
     * 设置目录数据集合
     *
     * @param data
     */
    public void setData(ArrayList<CCRPhotoFolderModel> data) {
        mFolderAdapter.setData(data);
    }

    @Override
    public void show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            int[] location = new int[2];
            mAnchorView.getLocationInWindow(location);
            int offsetY = location[1] + mAnchorView.getHeight();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                setHeight(CCRPhotoPickerUtil.getScreenHeight() - offsetY);
            }
            showAtLocation(mAnchorView, Gravity.NO_GRAVITY, 0, offsetY);
        } else {
            showAsDropDown(mAnchorView);
        }

        ViewCompat.animate(mContentRv).translationY(-mWindowRootView.getHeight()).setDuration(0).start();
        ViewCompat.animate(mContentRv).translationY(0).setDuration(ANIM_DURATION).start();
        ViewCompat.animate(mRootLl).alpha(0).setDuration(0).start();
        ViewCompat.animate(mRootLl).alpha(1).setDuration(ANIM_DURATION).start();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_photo_folder_root) {
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        ViewCompat.animate(mContentRv).translationY(-mWindowRootView.getHeight()).setDuration(ANIM_DURATION).start();
        ViewCompat.animate(mRootLl).alpha(1).setDuration(0).start();
        ViewCompat.animate(mRootLl).alpha(0).setDuration(ANIM_DURATION).start();

        if (mDelegate != null) {
            mDelegate.executeDismissAnim();
        }

        mContentRv.postDelayed(new Runnable() {
            @Override
            public void run() {
                CCRPhotoFolderPw.super.dismiss();
            }
        }, ANIM_DURATION);
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    @Override
    public void onRVItemClick(ViewGroup viewGroup, View view, int position) {
        if (mDelegate != null && mCurrentPosition != position) {
            mDelegate.onSelectedFolder(position);
        }
        mCurrentPosition = position;
        dismiss();
    }

    private class FolderAdapter extends CCRRecyclerViewAdapter<CCRPhotoFolderModel> {
        private int mImageSize;

        public FolderAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.bga_pp_item_photo_folder);

            mData = new ArrayList<>();
            mImageSize = CCRPhotoPickerUtil.getScreenWidth() / 10;
        }

        @Override
        protected void fillData(CCRViewHolderHelper helper, int position, CCRPhotoFolderModel model) {
            helper.setText(R.id.tv_item_photo_folder_name, model.name);
            helper.setText(R.id.tv_item_photo_folder_count, String.valueOf(model.getCount()));
            CCRImage.display(helper.getImageView(R.id.iv_item_photo_folder_photo), R.mipmap.bga_pp_ic_holder_light, model.coverPath, mImageSize);
        }
    }

    public interface Delegate {
        void onSelectedFolder(int position);

        void executeDismissAnim();
    }
}
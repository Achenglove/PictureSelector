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

import com.pasture.baseadapterlib.CCRRecyclerViewAdapter;
import com.pasture.baseadapterlib.CCRViewHolderHelper;

import androidx.recyclerview.widget.RecyclerView;

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
public class CCRPhotoPickerAdapter extends CCRRecyclerViewAdapter<String> {
    private ArrayList<String> mSelectedPhotos = new ArrayList<>();
    private int mPhotoSize;
    private boolean mTakePhotoEnabled;

    public CCRPhotoPickerAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.bga_pp_item_photo_picker);
        mPhotoSize = CCRPhotoPickerUtil.getScreenWidth() / 6;
    }

    @Override
    public int getItemViewType(int position) {
        if (mTakePhotoEnabled && position == 0) {
            return R.layout.bga_pp_item_photo_camera;
        } else {
            return R.layout.bga_pp_item_photo_picker;
        }
    }

    @Override
    public void setItemChildListener(CCRViewHolderHelper helper, int viewType) {
        if (viewType == R.layout.bga_pp_item_photo_camera) {
            helper.setItemChildClickListener(R.id.iv_item_photo_camera_camera);
        } else {
            helper.setItemChildClickListener(R.id.iv_item_photo_picker_flag);
            helper.setItemChildClickListener(R.id.iv_item_photo_picker_photo);
        }
    }

    @Override
    protected void fillData(CCRViewHolderHelper helper, int position, String model) {
        if (getItemViewType(position) == R.layout.bga_pp_item_photo_picker) {
            CCRImage.display(helper.getImageView(R.id.iv_item_photo_picker_photo), R.mipmap.bga_pp_ic_holder_dark, model, mPhotoSize);

            if (mSelectedPhotos.contains(model)) {
                helper.setImageResource(R.id.iv_item_photo_picker_flag, R.mipmap.bga_pp_ic_cb_checked);
                helper.getImageView(R.id.iv_item_photo_picker_photo).setColorFilter(helper.getConvertView().getResources().getColor(R.color.bga_pp_photo_selected_mask));
            } else {
                helper.setImageResource(R.id.iv_item_photo_picker_flag, R.mipmap.bga_pp_ic_cb_normal);
                helper.getImageView(R.id.iv_item_photo_picker_photo).setColorFilter(null);
            }
        }
    }

    public void setSelectedPhotos(ArrayList<String> selectedPhotos) {
        if (selectedPhotos != null) {
            mSelectedPhotos = selectedPhotos;
        }
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedPhotos() {
        return mSelectedPhotos;
    }

    public int getSelectedCount() {
        return mSelectedPhotos.size();
    }

    public void setPhotoFolderModel(CCRPhotoFolderModel photoFolderModel) {
        mTakePhotoEnabled = photoFolderModel.isTakePhotoEnabled();
        setData(photoFolderModel.getPhotos());
    }
}

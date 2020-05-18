package cn.bingoogolapple.photopicker.adapters;

import androidx.databinding.BindingAdapter;

import java.util.ArrayList;

import cn.bingoogolapple.photopicker.widget.CCRSortableNinePhotoLayout;

/**
 * @author Acheng
 * @Created on 2020/5/16.
 * @Email 345887272@qq.com
 * @Description 说明:
 */
public class CCRSortableNinePhotoLayoutAdapter {

    @BindingAdapter({"bga_snpl_delegate"})
    public static void setDelegate(CCRSortableNinePhotoLayout sortableNinePhotoLayout, CCRSortableNinePhotoLayout.Delegate delegate) {
        sortableNinePhotoLayout.setDelegate(delegate);
    }

    @BindingAdapter({"bga_snpl_data"})
    public static void setData(CCRSortableNinePhotoLayout sortableNinePhotoLayout, ArrayList<String> data) {
        sortableNinePhotoLayout.setData(data);
    }

    @BindingAdapter({"bga_snpl_editable"})
    public static void setData(CCRSortableNinePhotoLayout sortableNinePhotoLayout, boolean editable) {
        sortableNinePhotoLayout.setEditable(editable);
    }
}

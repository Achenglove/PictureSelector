package cn.bingoogolapple.photopicker.adapters;

import androidx.databinding.BindingAdapter;

import java.util.ArrayList;

import cn.bingoogolapple.photopicker.widget.CCRNinePhotoLayout;


/**
 * @author Acheng
 * @Created on 2020/5/16.
 * @Email 345887272@qq.com
 * @Description 说明:
 */
public class CCRNinePhotoLayoutAdapter {

    @BindingAdapter({"bga_npl_delegate"})
    public static void setDelegate(CCRNinePhotoLayout ninePhotoLayout, CCRNinePhotoLayout.Delegate delegate) {
        ninePhotoLayout.setDelegate(delegate);
    }

    @BindingAdapter({"bga_npl_data"})
    public static void setData(CCRNinePhotoLayout ninePhotoLayout, ArrayList<String> data) {
        ninePhotoLayout.setData(data);
    }
}

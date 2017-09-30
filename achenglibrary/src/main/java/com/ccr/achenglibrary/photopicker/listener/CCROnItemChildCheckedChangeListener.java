package com.ccr.achenglibrary.photopicker.listener;

import android.view.ViewGroup;
import android.widget.CompoundButton;

/**
 * 在此写用途
 *
 * @Author: Acheng
 * @Email: 345887272@qq.com
 * @Date: 2017-09-22 13:59
 * @Version: V1.0 <描述当前版本功能>
 */

public interface CCROnItemChildCheckedChangeListener {
    void onItemChildCheckedChanged(ViewGroup parent, CompoundButton childView, int position, boolean isChecked);
}

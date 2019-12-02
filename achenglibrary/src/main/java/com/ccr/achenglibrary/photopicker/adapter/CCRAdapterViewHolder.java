package com.ccr.achenglibrary.photopicker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 在此写用途
 *
 * @Author: Acheng
 * @Email: 345887272@qq.com
 * @Date: 2017-09-22 14:06
 * @Version: V1.0 <描述当前版本功能>
 */

public class CCRAdapterViewHolder {
    protected View mConvertView;
    protected CCRViewHolderHelper mViewHolderHelper;

    private CCRAdapterViewHolder(ViewGroup parent, int layoutId) {
        mConvertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
        mViewHolderHelper = new CCRViewHolderHelper(parent, mConvertView);
    }

    /**
     * 拿到一个可重用的ViewHolder对象
     *
     * @param convertView
     * @param parent
     * @param layoutId
     * @return
     */
    public static CCRAdapterViewHolder dequeueReusableAdapterViewHolder(View convertView, ViewGroup parent, int layoutId) {
        if (convertView == null) {
            return new CCRAdapterViewHolder(parent, layoutId);
        }
        return (CCRAdapterViewHolder) convertView.getTag();
    }

    public CCRViewHolderHelper getViewHolderHelper() {
        return mViewHolderHelper;
    }

    public View getConvertView() {
        return mConvertView;
    }

}

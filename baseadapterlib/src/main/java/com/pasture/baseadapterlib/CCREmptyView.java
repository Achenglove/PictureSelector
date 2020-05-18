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

package com.pasture.baseadapterlib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:17/1/9 下午9:45
 * 描述:
 */
public class CCREmptyView extends FrameLayout {
    private View mContentView;
    private View mEmptyView;
    private TextView mMsgTv;
    private ImageView mIconIv;
    private Delegate mDelegate;

    public CCREmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() < 0 || getChildCount() > 2) {
            throw new IllegalStateException(CCREmptyView.class.getSimpleName() + "只能有一个或两个子控件");
        }
        initView();
        setListener();

        showContentView();
    }

    private void initView() {
        if (getChildCount() == 1) {
            mContentView = getChildAt(0);
            View.inflate(getContext(), R.layout.bga_baseadapter_empty_view, this);
            mEmptyView = getViewById(R.id.ll_bga_adapter_empty_view_root);
        } else {
            mEmptyView = getChildAt(0);
            mContentView = getChildAt(1);
        }

        mMsgTv = getViewById(R.id.tv_bga_adapter_empty_view_msg);
        mIconIv = getViewById(R.id.iv_bga_adapter_empty_view_icon);
    }

    private void setListener() {
        mEmptyView.setOnClickListener(new CCROnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mDelegate != null) {
                    mDelegate.onClickEmptyView(CCREmptyView.this);
                }
            }
        });
        mIconIv.setOnClickListener(new CCROnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mDelegate != null) {
                    mDelegate.onClickIconEmptyView(CCREmptyView.this);
                }
            }
        });
        mMsgTv.setOnClickListener(new CCROnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mDelegate != null) {
                    mDelegate.onClickTextEmptyView(CCREmptyView.this);
                }
            }
        });
    }

    private void showEmptyView() {
        mContentView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }

    /**
     * 显示文字空视图
     *
     * @param msgResId 提示文字资源 id
     */
    public void showTextEmptyView(@StringRes int msgResId) {
        showTextEmptyView(getResources().getString(msgResId));
    }

    /**
     * 显示文字空视图
     *
     * @param msg 提示文字
     */
    public void showTextEmptyView(String msg) {
        mIconIv.setVisibility(View.GONE);
        mMsgTv.setVisibility(View.VISIBLE);

        mMsgTv.setText(msg);

        showEmptyView();
    }

    /**
     * 显示图片空视图
     *
     * @param iconResId 图片资源 id
     */
    public void showIconEmptyView(@DrawableRes int iconResId) {
        mIconIv.setVisibility(View.VISIBLE);
        mMsgTv.setVisibility(View.GONE);

        mIconIv.setImageResource(iconResId);

        showEmptyView();
    }

    /**
     * 显示文字和图片空视图
     *
     * @param msgResId  提示文字资源 id
     * @param iconResId 图片资源 id
     */
    public void showEmptyView(@StringRes int msgResId, @DrawableRes int iconResId) {
        showEmptyView(getResources().getString(msgResId), iconResId);
    }

    /**
     * 显示文字和图片空视图
     *
     * @param msg       提示文字
     * @param iconResId 图片资源 id
     */
    public void showEmptyView(String msg, @DrawableRes int iconResId) {
        mIconIv.setVisibility(View.VISIBLE);
        mMsgTv.setVisibility(View.VISIBLE);

        mIconIv.setImageResource(iconResId);
        mMsgTv.setText(msg);

        showEmptyView();
    }

    /**
     * 显示内容视图
     */
    public void showContentView() {
        mEmptyView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
    }

    public interface Delegate {
        /**
         * 点击了文字空视图
         */
        void onClickTextEmptyView(CCREmptyView emptyView);

        /**
         * 点击了图片空视图
         */
        void onClickIconEmptyView(CCREmptyView emptyView);

        /**
         * 点击了空视图
         */
        void onClickEmptyView(CCREmptyView emptyView);
    }

    public static class SimpleDelegate implements Delegate {
        @Override
        public void onClickTextEmptyView(CCREmptyView emptyView) {
        }

        @Override
        public void onClickIconEmptyView(CCREmptyView emptyView) {
        }

        @Override
        public void onClickEmptyView(CCREmptyView emptyView) {
        }
    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }
}
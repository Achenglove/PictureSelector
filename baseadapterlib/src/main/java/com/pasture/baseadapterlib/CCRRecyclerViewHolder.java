/**
 * Copyright 2015 bingoogolapple
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pasture.baseadapterlib;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/28 上午7:28
 * 描述:适用于RecyclerView的item的ViewHolder
 */
public class CCRRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
    protected Context mContext;
    protected CCROnRVItemClickListener mOnRVItemClickListener;
    protected CCROnRVItemLongClickListener mOnRVItemLongClickListener;
    protected CCRViewHolderHelper mViewHolderHelper;
    protected RecyclerView mRecyclerView;
    protected CCRRecyclerViewAdapter mRecyclerViewAdapter;

    public CCRRecyclerViewHolder(CCRRecyclerViewAdapter recyclerViewAdapter, RecyclerView recyclerView, View itemView, CCROnRVItemClickListener onRVItemClickListener, CCROnRVItemLongClickListener onRVItemLongClickListener) {
        super(itemView);
        mRecyclerViewAdapter = recyclerViewAdapter;
        mRecyclerView = recyclerView;
        mContext = mRecyclerView.getContext();
        mOnRVItemClickListener = onRVItemClickListener;
        mOnRVItemLongClickListener = onRVItemLongClickListener;
        itemView.setOnClickListener(new CCROnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (v.getId() == CCRRecyclerViewHolder.this.itemView.getId() && null != mOnRVItemClickListener) {
                    mOnRVItemClickListener.onRVItemClick(mRecyclerView, v, getAdapterPositionWrapper());
                }
            }
        });
        itemView.setOnLongClickListener(this);

        mViewHolderHelper = new CCRViewHolderHelper(mRecyclerView, this);
    }

    public CCRViewHolderHelper getViewHolderHelper() {
        return mViewHolderHelper;
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == this.itemView.getId() && null != mOnRVItemLongClickListener) {
            return mOnRVItemLongClickListener.onRVItemLongClick(mRecyclerView, v, getAdapterPositionWrapper());
        }
        return false;
    }

    public int getAdapterPositionWrapper() {
        if (mRecyclerViewAdapter.getHeadersCount() > 0) {
            return getAdapterPosition() - mRecyclerViewAdapter.getHeadersCount();
        } else {
            return getAdapterPosition();
        }
    }
}
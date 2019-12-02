package com.ccr.achenglibrary.photopicker.adapter;


import android.view.View;
import android.view.ViewGroup;

import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 在此写用途
 *
 * @Author: Acheng
 * @Email: 345887272@qq.com
 * @Date: 2017-09-22 14:02
 * @Version: V1.0 <描述当前版本功能>
 */

public class CCRHeaderAndFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int BASE_ITEM_TYPE_HEADER = 2048;
    private static final int BASE_ITEM_TYPE_FOOTER = 4096;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdapter;

    CCRHeaderAndFooterAdapter(RecyclerView.Adapter innerAdapter) {
        mInnerAdapter = innerAdapter;
    }

    public RecyclerView.Adapter getInnerAdapter() {
        return mInnerAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            // header 类型
            return new RecyclerView.ViewHolder(mHeaderViews.get(viewType)) {
            };
        } else if (mFootViews.get(viewType) != null) {
            // footer 类型
            return new RecyclerView.ViewHolder(mFootViews.get(viewType)) {
            };
        } else {
            // 真实 item 类型
            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            // 返回 header 的 itemType
            return mHeaderViews.keyAt(position);
        } else if (isFooterView(position)) {
            // 返回 footer 的 itemType
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        } else {
            // 返回真实 item 的 itemType
            return mInnerAdapter.getItemViewType(getRealItemPosition(position));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 如果是 header 或 footer 就不绑定数据
        if (isHeaderViewOrFooterView(position)) {
            return;
        }

        mInnerAdapter.onBindViewHolder(holder, getRealItemPosition(position));
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isHeaderViewOrFooterView(position)) {
                        // header 或 footer 时宽度占满父控件
                        return gridLayoutManager.getSpanCount();
                    } else {
                        // 真实的 item
                        if (spanSizeLookup != null) {
                            return spanSizeLookup.getSpanSize(position - getHeadersCount());
                        }
                        return 1;
                    }

//                    int viewType = getItemViewType(position);
//                    if (mHeaderViews.get(viewType) != null) {
//                        return gridLayoutManager.getSpanCount();
//                    } else if (mFootViews.get(viewType) != null) {
//                        return gridLayoutManager.getSpanCount();
//                    }
//                    if (spanSizeLookup != null) {
//                        return spanSizeLookup.getSpanSize(position - getHeadersCount());
//                    }
//                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewOrFooterView(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    /**
     * 获取除去 header 和 footer 后真实的 item 总数
     *
     * @return
     */
    public int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    /**
     * 获取真实 item 的索引
     *
     * @param position
     * @return
     */
    public int getRealItemPosition(int position) {
        return position - getHeadersCount();
    }

    /**
     * 索引为 position 的 item 是否是 header
     *
     * @param position
     * @return
     */
    public boolean isHeaderView(int position) {
        return position < getHeadersCount();
    }

    /**
     * 索引为 position 的 item 是否是 footer
     *
     * @param position
     * @return
     */
    public boolean isFooterView(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    /**
     * 索引为 position 的 item 是否是 header 或 footer
     *
     * @param position
     * @return
     */
    public boolean isHeaderViewOrFooterView(int position) {
        return isHeaderView(position) || isFooterView(position);
    }

    /**
     * 添加 header
     *
     * @param view
     */
    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    /**
     * 添加 footer
     *
     * @param view
     */
    public void addFooterView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    /**
     * 获取 header 的个数
     *
     * @return
     */
    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    /**
     * 获取 footer 的个数
     *
     * @return
     */
    public int getFootersCount() {
        return mFootViews.size();
    }
}
package cn.sinata.xldutils.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import cn.sinata.xldutils.adapter.util.ViewHolder;

/**
 * Created by lmw on 2018/3/21.
 */

public class LoadMoreAdapter extends RecyclerView.Adapter {
    protected static final int TYPE_NORMAL = -1;//普通
    protected static final int TYPE_FOOTER = -2;//尾部
    private ViewHolder footerViewHolder;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return footerViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount()-1 == position) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    /**
     * 设置footerView
     *
     * @param view footerView
     */
    public void setFooterView(View view) {

        if (footerViewHolder == null || view != footerViewHolder.itemView) {
            footerViewHolder = new ViewHolder(view);
            notifyDataSetChanged();
        }
    }

    /**
     * 当前位置是否是footerView
     *
     * @param position 当前位置
     * @return 是否footerView
     */
    private boolean isFooter(int position) {
        return hasFooter() && position == getItemCount();
    }

    /**
     * 是否有footerView
     *
     * @return 是否有footerView
     */
    private boolean hasFooter() {
        return footerViewHolder != null;
    }
}

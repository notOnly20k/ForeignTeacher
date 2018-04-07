package cn.sinata.xldutils.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import cn.sinata.xldutils.adapter.util.ViewHolder;

/**
 * 可设置头，尾的RecyclerAdapter
 * Created by LiaoXiang on 2015/11/4.
 */
public abstract class HFRecyclerAdapter<T> extends BaseRecyclerAdapter<T> {
    protected static final int TYPE_HEADER = -3;//头部
    protected static final int TYPE_NORMAL = 0;//普通
    protected static final int TYPE_FOOTER = -1;//尾部
    private ViewHolder headerViewHolder;
    private ViewHolder footerViewHolder;

    public HFRecyclerAdapter(List<T> mData, int layoutId) {
        super(mData,layoutId);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)){
            return TYPE_HEADER;
        }
        if (isFooter(position)){
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER){
            return headerViewHolder;
        }else if (viewType == TYPE_FOOTER){
            return footerViewHolder;
        }
        return super.onCreateViewHolder(parent, viewType);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!isHeader(position) && !isFooter(position)){
            super.onBindViewHolder(holder,position-(hasHeader()?1:0));
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            GridSpanSizeLookup gridSpanSizeLookup = new GridSpanSizeLookup((GridLayoutManager) layoutManager);
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(gridSpanSizeLookup);
        }
    }

    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        if (hasHeader()){
            count += 1;
        }
        if (hasFooter()){
            count += 1;
        }
        return  count;
    }
    public int getDataItemCount() {
        return super.getItemCount();
    }
    /**
     * 设置headerView
     * @param view    headerView
     */
    public void setHeaderView(View view){
        if (headerViewHolder == null || view != headerViewHolder.itemView){
            headerViewHolder = new ViewHolder(view);
            notifyDataSetChanged();
        }
    }

    /**
     * 设置footerView
     * @param view footerView
     */
    public void setFooterView(View view){

        if (footerViewHolder == null || view != footerViewHolder.itemView){
            footerViewHolder = new ViewHolder(view);
            notifyDataSetChanged();
        }
    }

    /**
     * 是否有headerView
     * @return 是否有headerView
     */
    protected boolean hasHeader(){
        return  headerViewHolder != null;
    }

    /**
     * 是否有footerView
     * @return 是否有footerView
     */
    protected boolean hasFooter(){
        return  footerViewHolder != null;
    }

    /**
     * 是否是头view
     * @param position 当前位置
     * @return 是否头view
     */
    private boolean isHeader(int position){
        return  hasHeader() && position == 0;
    }

    /**
     * 当前位置是否是footerView
     * @param position 当前位置
     * @return 是否footerView
     */
    private boolean isFooter(int position){
        return hasFooter() && position == getDataItemCount() + (hasHeader() ? 1 : 0);
    }

    private final class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup{
        private GridLayoutManager layoutManager;
        GridSpanSizeLookup(GridLayoutManager layoutManager){
            this.layoutManager = layoutManager;
        }

        @Override
        public int getSpanSize(int position) {
            if (isFooter(position) || isHeader(position)){
                return layoutManager.getSpanCount();
            }
            return 1;
        }
    }
}
package com.hyphenate.easeui.widget.emojicon;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.model.ShortMessage;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconPagerView.EaseEmojiconPagerViewListener;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconScrollTabBar.EaseScrollTabBarItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Emojicon menu
 */
public class EaseEmojiconMenu extends EaseEmojiconMenuBase {

    private Context context;
    private int emojiconColumns;
    private int bigEmojiconColumns;
    private EaseEmojiconScrollTabBar tabBar;
    private EaseEmojiconIndicatorView indicatorView;
    private EaseEmojiconPagerView pagerView;

    private List<EaseEmojiconGroupEntity> emojiconGroupList = new ArrayList<EaseEmojiconGroupEntity>();
    private List<ShortMessage> shortMessageList = new ArrayList<>(); //短语数据


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public EaseEmojiconMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(context, attrs);
    }

    public EaseEmojiconMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    public EaseEmojiconMenu(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    private void init(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.ease_widget_emojicon, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseEmojiconMenu);
        int defaultColumns = 7;
        emojiconColumns = ta.getInt(R.styleable.EaseEmojiconMenu_emojiconColumns, defaultColumns);
        int defaultBigColumns = 4;
        bigEmojiconColumns = ta.getInt(R.styleable.EaseEmojiconMenu_bigEmojiconRows, defaultBigColumns);
        ta.recycle();

        pagerView = (EaseEmojiconPagerView) findViewById(R.id.pager_view);
        indicatorView = (EaseEmojiconIndicatorView) findViewById(R.id.indicator_view);
        tabBar = (EaseEmojiconScrollTabBar) findViewById(R.id.tab_bar);
        final TextView tabEmoji = findViewById(R.id.tab_express);
        final TextView tabShort = findViewById(R.id.tab_short);
        final ListView lvShort = findViewById(R.id.lv_short);
        initShortMessage();
        lvShort.setAdapter(new ShortMessageAdapter());
        lvShort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener!=null){
                    listener.onShortClicked(shortMessageList.get(position));
                }
            }
        });
        tabEmoji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pagerView.getVisibility() == VISIBLE)
                    return;
                pagerView.setVisibility(VISIBLE);
                lvShort.setVisibility(GONE);
                tabEmoji.setTextColor(Color.parseColor("#333333"));
                tabEmoji.setBackgroundResource(R.drawable.bg_top_line);
                tabShort.setTextColor(Color.parseColor("#666666"));
                tabShort.setBackgroundColor(Color.WHITE);
            }
        });
        tabShort.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lvShort.getVisibility() == VISIBLE)
                    return;
                lvShort.setVisibility(VISIBLE);
                pagerView.setVisibility(GONE);
                tabShort.setTextColor(Color.parseColor("#333333"));
                tabShort.setBackgroundResource(R.drawable.bg_top_line);
                tabEmoji.setTextColor(Color.parseColor("#666666"));
                tabEmoji.setBackgroundColor(Color.WHITE);
            }
        });
    }

    private void initShortMessage() {
        shortMessageList.add(new ShortMessage("幸会！","Hello!"));
        shortMessageList.add(new ShortMessage("这个见面地点和时间可以吗？","Is the location and time good for you?"));
        shortMessageList.add(new ShortMessage("我想约定一个长期时间。","I would like to book for long term class."));
        shortMessageList.add(new ShortMessage("期待和你见面！","Looking forward to meet you!"));
        shortMessageList.add(new ShortMessage("我到了，你在哪？","I’m here, where are you?"));
        shortMessageList.add(new ShortMessage("请稍等我一下。","Please wait for a little bit."));
    }

    public void init(List<EaseEmojiconGroupEntity> groupEntities) {
        if (groupEntities == null || groupEntities.size() == 0) {
            return;
        }
        for (EaseEmojiconGroupEntity groupEntity : groupEntities) {
            emojiconGroupList.add(groupEntity);
            tabBar.addTab(groupEntity.getIcon());
        }

        pagerView.setPagerViewListener(new EmojiconPagerViewListener());
        pagerView.init(emojiconGroupList, emojiconColumns, bigEmojiconColumns);

        tabBar.setTabBarItemClickListener(new EaseScrollTabBarItemClickListener() {

            @Override
            public void onItemClick(int position) {
                pagerView.setGroupPostion(position);
            }
        });

    }

    private class ShortMessageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return shortMessageList.size();
        }

        @Override
        public Object getItem(int position) {
            return shortMessageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_short_msg,null);
            }
            TextView view = (TextView) convertView;
            view.setText(shortMessageList.get(position).getEnglish());
            return convertView;
        }
    }

    /**
     * add emojicon group
     *
     * @param groupEntity
     */
    public void addEmojiconGroup(EaseEmojiconGroupEntity groupEntity) {
        emojiconGroupList.add(groupEntity);
        pagerView.addEmojiconGroup(groupEntity, true);
        tabBar.addTab(groupEntity.getIcon());
    }

    /**
     * add emojicon group list
     *
     * @param groupEntitieList
     */
    public void addEmojiconGroup(List<EaseEmojiconGroupEntity> groupEntitieList) {
        for (int i = 0; i < groupEntitieList.size(); i++) {
            EaseEmojiconGroupEntity groupEntity = groupEntitieList.get(i);
            emojiconGroupList.add(groupEntity);
            pagerView.addEmojiconGroup(groupEntity, i == groupEntitieList.size() - 1 ? true : false);
            tabBar.addTab(groupEntity.getIcon());
        }

    }

    /**
     * remove emojicon group
     *
     * @param position
     */
    public void removeEmojiconGroup(int position) {
        emojiconGroupList.remove(position);
        pagerView.removeEmojiconGroup(position);
        tabBar.removeTab(position);
    }

    public void setTabBarVisibility(boolean isVisible) {
        if (!isVisible) {
            tabBar.setVisibility(View.GONE);
        } else {
            tabBar.setVisibility(View.VISIBLE);
        }
    }


    private class EmojiconPagerViewListener implements EaseEmojiconPagerViewListener {

        @Override
        public void onPagerViewInited(int groupMaxPageSize, int firstGroupPageSize) {
            indicatorView.init(groupMaxPageSize);
            indicatorView.updateIndicator(firstGroupPageSize);
            tabBar.selectedTo(0);
        }

        @Override
        public void onGroupPositionChanged(int groupPosition, int pagerSizeOfGroup) {
            indicatorView.updateIndicator(pagerSizeOfGroup);
            tabBar.selectedTo(groupPosition);
        }

        @Override
        public void onGroupInnerPagePostionChanged(int oldPosition, int newPosition) {
            indicatorView.selectTo(oldPosition, newPosition);
        }

        @Override
        public void onGroupPagePostionChangedTo(int position) {
            indicatorView.selectTo(position);
        }

        @Override
        public void onGroupMaxPageSizeChanged(int maxCount) {
            indicatorView.updateIndicator(maxCount);
        }

        @Override
        public void onDeleteImageClicked() {
            if (listener != null) {
                listener.onDeleteImageClicked();
            }
        }

        @Override
        public void onExpressionClicked(EaseEmojicon emojicon) {
            if (listener != null) {
                listener.onExpressionClicked(emojicon);
            }
        }

    }

}

package com.xld.foreignteacher.ui.square

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.adapter.LoadMoreAdapter
import cn.sinata.xldutils.utils.ActivityUtil
import com.facebook.drawee.view.SimpleDraweeView
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.userinfo.TeacherDetailActivity
import com.xld.foreignteacher.views.NestedGridView
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Created by cz on 4/1/18.
 */
class SquareAdapter(private val context: Context, private val fragmentManager: FragmentManager) : LoadMoreAdapter() {

    private val data: MutableList<String>
    private val activityUtil: ActivityUtil
    private val logger=LoggerFactory.getLogger("SquareAdapter")

    init {
        data = ArrayList()
        activityUtil = ActivityUtil.create(context)
        data.add("")
        data.add("")
        data.add("")
        data.add("")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == TYPE_NORMAL) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_square, parent, false)
            return ViewHolder(view)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1)
            TYPE_FOOTER
        else
            TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            val viewHolder = holder as ViewHolder
            viewHolder.elSquareItem.setOnClickListener{
                activityUtil.go(SquareDetailActivity::class.java).put("id","").start()
            }
            viewHolder.ivHead.setOnClickListener {
                activityUtil.go(TeacherDetailActivity::class.java).put("id","").start()
            }
            viewHolder.tvName.setOnClickListener {
                //todo 跳用户资料
            }
            viewHolder.tvContent.setOnLongClickListener {
                //todo 翻译成功后显示
                viewHolder.tvTranslation.visibility = View.VISIBLE
                true
            }
            viewHolder.tvLocation.setOnClickListener {
                //todo 跳地图
            }
            viewHolder.btnLike.setOnClickListener {
                //todo 点赞
            }
            viewHolder.btnLike.setOnClickListener {
                //todo 评论
            }
            //todo 如果有图片就加载
            val urls = ArrayList<String>()
            urls.add("")
            urls.add("")
            urls.add("")
            urls.add("")
            urls.add("")
            viewHolder.gvImg.adapter = SquareImgAdapter(urls, context)
            viewHolder.gvImg.visibility = View.VISIBLE
            //todo 如果有评论就加载
            //            for ()
            //            viewHolder.llReplay.addView();
        }
    }

    inner class ViewHolder  constructor(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.iv_head)
        lateinit var ivHead: SimpleDraweeView
        @BindView(R.id.tv_name)
        lateinit var tvName: TextView
        @BindView(R.id.tv_time)
        lateinit var tvTime: TextView
        @BindView(R.id.tv_content)
        lateinit var tvContent: TextView
        @BindView(R.id.tv_translation)
        lateinit var tvTranslation: TextView
        @BindView(R.id.gv_img)
        lateinit var gvImg: NestedGridView
        @BindView(R.id.tv_location)
        lateinit var tvLocation: TextView
        @BindView(R.id.btn_like)
        lateinit var btnLike: TextView
        @BindView(R.id.btn_comment)
        lateinit var btnComment: ImageView
        @BindView(R.id.ll_bottom)
        lateinit var llBottom: LinearLayout
        @BindView(R.id.ll_replay)
        lateinit var llReplay: LinearLayout
        @BindView(R.id.el_square_item)
        lateinit var elSquareItem: RelativeLayout

        init {
            ButterKnife.bind(this, view)
        }
    }
    //评价 弹出输入框
    //    public void evaluateDialog() {
    //        new TDialog.Builder(fragmentManager)
    //                .setLayoutRes(R.layout.dialog_evaluate)
    //                .setScreenWidthAspect(this, 1.0f)
    //                .setGravity(Gravity.BOTTOM)
    //                .addOnClickListener(R.id.btn_evluate)
    //                .setOnBindViewListener(new OnBindViewListener() {
    //                    @Override
    //                    public void bindView(BindViewHolder viewHolder) {
    //                        final EditText editText = viewHolder.getView(R.id.editText);
    //                        editText.post(new Runnable() {
    //                            @Override
    //                            public void run() {
    //                                InputMethodManager imm = (InputMethodManager) DiffentDialogActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
    //                                imm.showSoftInput(editText, 0);
    //                            }
    //                        });
    //                    }
    //                })
    //                .setOnViewClickListener(new OnViewClickListener() {
    //                    @Override
    //                    public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
    //                        EditText editText = viewHolder.getView(R.id.editText);
    //                        String content = editText.getText().toString().trim();
    //                        Toast.makeText(DiffentDialogActivity.this, "评价内容:" + content, Toast.LENGTH_SHORT).show();
    //                        tDialog.dismiss();
    //                    }
    //                })
    //                .create()
    //                .show();
    //    }
}

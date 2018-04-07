package com.xld.foreignteacher;

import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity;

/**
 * Created by cz on 4/4/18.
 */

public class TestActivity extends BaseTranslateStatusActivity {
    @Override
    protected int getContentViewResId() {
        return R.layout.item_select_a_to_z;
    }

    @Override
    protected boolean getChangeTitleBar() {
        return false;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class Teacher{

    }
}

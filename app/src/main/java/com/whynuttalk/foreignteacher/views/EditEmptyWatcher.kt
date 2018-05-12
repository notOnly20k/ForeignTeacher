package com.whynuttalk.foreignteacher.views

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by cz on 3/29/18.
 */
class EditEmptyWatcher(private val checkable: Checkable) : TextWatcher {

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun afterTextChanged(editable: Editable) {
        checkable.checkAllEditContent()
    }

    interface Checkable {
        fun checkAllEditContent()
    }
}

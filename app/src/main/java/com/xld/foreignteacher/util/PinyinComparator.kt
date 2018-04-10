package com.xld.foreignteacher.util

import com.xld.foreignteacher.api.dto.SelectData


/**
 * Created by cz on 4/6/18.
 */
class PinyinComparator : Comparator<SelectData> {

    override fun compare(o1: SelectData, o2: SelectData): Int {
        return if (o1.firstLetter().equals("@") || o2.firstLetter().equals("#")) {
            -1
        } else if (o1.firstLetter().equals("#") || o2.firstLetter().equals("@")) {
            1
        } else {
            o1.firstLetter().compareTo(o2.firstLetter())
        }
    }

}
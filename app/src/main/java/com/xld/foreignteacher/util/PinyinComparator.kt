package com.xld.foreignteacher.util

import com.xld.foreignteacher.api.dto.Language


/**
 * Created by cz on 4/6/18.
 */
class PinyinComparator : Comparator<Language> {

    override fun compare(o1: Language, o2: Language): Int {
        return if (o1.firstLetter().equals("@") || o2.firstLetter().equals("#")) {
            -1
        } else if (o1.firstLetter().equals("#") || o2.firstLetter().equals("@")) {
            1
        } else {
            o1.firstLetter().compareTo(o2.firstLetter())
        }
    }

}
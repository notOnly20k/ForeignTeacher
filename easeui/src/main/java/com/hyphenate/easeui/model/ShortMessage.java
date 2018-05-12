package com.hyphenate.easeui.model;

/**
 * Created by lmw on 2018/4/28.
 */

public class ShortMessage {
    private String chinese;
    private String english;

    public ShortMessage(String chinese, String english) {
        this.chinese = chinese;
        this.english = english;
    }

    public String getChinese() {
        return chinese == null ? "" : chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getEnglish() {
        return english == null ? "" : english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}

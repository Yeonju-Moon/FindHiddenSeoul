package com.seoulapp.findhiddenseoul.ListViewItem;

/**
 * Created by JIYOUNGKIM on 2017-10-27.
 */

public class ListViewItem2 {
    private int RANK;
    private String level_imgUrl;
    private String nick_name;
    private int level;
    private int counts;

    public int getRANK() {
        return RANK;
    }

    public void setRANK(int RANK) {
        this.RANK = RANK;
    }

    public String getLevel_imgUrl() {
        return level_imgUrl;
    }

    public void setLevel_imgUrl(String level_imgUrl) {
        this.level_imgUrl = level_imgUrl;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public ListViewItem2(int RANK, String level_imgUrl, String nick_name, int level, int counts) {
        this.RANK = RANK;
        this.level_imgUrl = level_imgUrl;
        this.nick_name = nick_name;
        this.level = level;
        this.counts = counts;
    }

}

package com.zteng.common.util;

/**
 * 纸张定义
 * <i>
 * <br>
 * 作者：Created by ly1054 on 2020/4/2.
 * <br>
 * 邮箱：2791014943@qq.com
 * </i>
 */
public enum Paper {
    A3(420),
//    A3横向( 297),
    A4( 297),
    A4横向(210),
    A5(210),
    A5横向( 148),
    B5(250),
    B5横向(176);

    private int height;

    Paper(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return name();
    }

}

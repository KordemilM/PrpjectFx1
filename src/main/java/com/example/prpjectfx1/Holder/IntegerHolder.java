package com.example.prpjectfx1.Holder;

public class IntegerHolder {
    private int num;
    private int index;
    private static final IntegerHolder INSTANCE = new IntegerHolder();
    public static IntegerHolder getInstance() {
        return INSTANCE;
    }
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}

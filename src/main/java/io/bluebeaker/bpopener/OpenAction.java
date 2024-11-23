package io.bluebeaker.bpopener;

public enum OpenAction {
    USE,SNEAK_USE;
    public boolean isSneaking(){
        return this==SNEAK_USE;
    }
}

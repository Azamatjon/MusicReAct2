package com.ReAct.MusicReAct.Helper;

public class PaginationPage {
    private boolean isPrev;
    private boolean isNext;
    private Integer pageNumber;
    private boolean isActive;
    private boolean isDisabled;
    private boolean isBetween;

    public PaginationPage(int pageNumber, boolean isActive, boolean isDisabled, boolean isPrev, boolean isBetween, boolean isNext){
        this.pageNumber = pageNumber;
        this.isActive = isActive;
        this.isDisabled = isDisabled;
        this.isPrev = isPrev;
        this.isBetween = isBetween;
        this.isNext = isNext;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public boolean isPrev() {
        return isPrev;
    }

    public boolean isBetween() {
        return isBetween;
    }

    public boolean isNext() {
        return isNext;
    }


}

package com.ReAct.MusicReAct.Helper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Set;

public class Pagination {
    private ArrayList<PaginationPage> pages = new ArrayList<>();
    private Integer pageNumber;
    private Integer pageSize;

    public Pagination(Page items, Pageable pageable, int maxPages){
        if (items.getTotalPages() > 1){
            boolean isFirstDotAdded = false;
            boolean isSecondDotAdded = false;
            this.pageNumber = pageable.getPageNumber();
            this.pageSize = pageable.getPageSize();

            for (int i = 0; i < items.getTotalPages(); i++) {
                if (i == 0) {
                    if (pageable.getPageNumber() == i) {
                        pages.add(new PaginationPage(i, false, true, true, false, false));
                        pages.add(new PaginationPage(i, true, true, false, false, false));
                    } else {
                        pages.add(new PaginationPage(i, false, false, true, false, false));
                        pages.add(new PaginationPage(i, false, false, false, false, false));
                    }
                } else if (i == items.getTotalPages() - 1) {
                    if (pageable.getPageNumber() == i) {
                        pages.add(new PaginationPage(i, true, true, false, false, false));
                        pages.add(new PaginationPage(i, false, true, false, false, true));
                    } else {
                        pages.add(new PaginationPage(i, false, false, false, false, false));
                        pages.add(new PaginationPage(i, false, false, false, false, true));
                    }
                } else {
                    if (!(i > (int) (maxPages / 2) && i < (pageable.getPageNumber() - 2)) && !((i > (pageable.getPageNumber() - 1 + 3)) && (i < items.getTotalPages() - 1 - (int) (maxPages / 2)))) {
                        if (pageable.getPageNumber() == i) {
                            pages.add(new PaginationPage(i, true, true, false, false, false));
                        } else {
                            pages.add(new PaginationPage(i, false, false, false, false, false));
                        }
                    } else if ((i > (int) (maxPages / 2) && i < (pageable.getPageNumber() - 2)) && !isFirstDotAdded) {
                        isFirstDotAdded = true;
                        pages.add(new PaginationPage(i, false, true, false, true, false));
                    } else if (((i > (pageable.getPageNumber() - 1 + 3)) && (i < items.getTotalPages() - 1 - (int) (maxPages / 2))) && !isSecondDotAdded) {
                        isSecondDotAdded = true;
                        pages.add(new PaginationPage(i, false, true, false, true, false));
                    }
                }
            }
        }
    }

    public ArrayList<PaginationPage> getPages() {
        return pages;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

}

package com.art.sell.pojo;



public class Page {
    //页码
    private Integer pageNum = 1;
    //页码条数
    private Integer pageSize = 10;
    //总条数
    private Integer pages;

    public Integer getPageNum() {
        return pageNum == null ? 1 : pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize == null ? 10 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public static int getPages(Long totalPages, int pageSize){
        int total = Integer.valueOf(totalPages + "");
        int pages = 0;
        if(total % pageSize > 0){
            pages = total/pageSize + 1;
        }else{
            pages = total/pageSize;
        }
        return pages;
    }

}

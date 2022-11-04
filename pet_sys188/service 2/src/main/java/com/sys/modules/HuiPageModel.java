package com.sys.modules;

import java.io.Serializable;
import java.util.List;

public class HuiPageModel<T> implements Serializable {
    /// <summary>
    /// 请求次数（前端==》后端）
    /// </summary>
    private int draw;

    /// <summary>
    /// 总记录数（前端《==后端）
    /// </summary>
    private int recordsTotal;

    /// <summary>
    /// 过滤后的总记录数（前端《==后端）
    /// </summary>
    private int recordsFiltered;

    /// <summary>
    /// 记录开始索引（前端==》后端）
    /// </summary>
    private int start;

    /// <summary>
    /// PageIndex（前端==》后端）
    /// </summary>
    private int pageIndex;
    /// <summary>
    /// PageSize（前端==》后端）
    /// </summary>
    private int length;

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    /// <summary>
    /// 集合分页数据（前端《==后端）
    /// </summary>
    private List<T> data;
}

package com.shizy.common.json.page;

import com.baomidou.mybatisplus.plugins.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {

    /************************************************/

    public static final int DEFALT_PAGE = 1;

    public static final int DEFALT_PAGE_SIZE = 10;

    /************************************************/

    /**
     * 当前页
     */
    private Integer page;

    /**
     * 每页的data条数
     */
    private Integer pageSize;

    /************************************************/

    public Page getPageOrDefalt() {
        return getPageOrDefalt(null, null);
    }

    public Page getPageOrDefalt(Integer pageDefalt, Integer pageSizeDefalt) {
        Integer page = this.getPage();
        if (page == null || page == 0) {
            if (pageDefalt != null) {
                page = pageDefalt;
            } else {
                page = DEFALT_PAGE;
            }
        }
        Integer pageSize = this.getPageSize();
        if (pageSize == null || pageSize == 0) {
            if (pageSizeDefalt != null) {
                pageSize = pageSizeDefalt;
            } else {
                pageSize = DEFALT_PAGE_SIZE;
            }
        }
        return new Page(page, pageSize);
    }


    /************************************************/


}



























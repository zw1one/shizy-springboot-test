package com.shizy.common.json.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {

    /**
     * 当前页
     */
    private Integer page;

    /**
     * 每页的data条数
     */
    private Integer pageSize;

    /**
     * data总条数
     */
    private Long total;

    /**
     * 以pageSize为准的page总页数
     */
    private Long pages;

}


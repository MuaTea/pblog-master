package com.niit.blog.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AjaxResult {
    private Integer code;
    private String msg;
    private Object data;

    public static AjaxResult success(Integer code, String msg, Object data) {
        return new AjaxResult(code, msg, data);
    }

    public static AjaxResult error(Integer code, String msg) {
        return new AjaxResult(code, msg, null);
    }
}

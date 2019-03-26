package com.xjxxxc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author Jason Xu
 * @DESCRIPTION 临时存储信息，应替换其他DB
 * @create 2019/3/21
 */
@Data
@NoArgsConstructor
public class ConvertInfo {
    private String id;

    private String filepath;

    private String content;

}

package com.xjxxxc.service;

import java.util.Optional;

/*
 * @author Jason Xu
 * @DESCRIPTION Aliyun获取Token
 * @create 2019/3/20
 */
public interface AliyunTokenService {

    /**
     * get aliyun asr accessToken
     * 文档 https://help.aliyun.com/document_detail/72153.html?spm=a2c4g.11186623.6.549.372b5397jw5elp
     *
     * @return accessToken
     */
    Optional<String> token() throws Exception;
}

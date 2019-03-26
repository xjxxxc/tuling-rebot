package com.xjxxxc.service;


import java.io.InputStream;
import java.util.Optional;

/*
 * @author Jason Xu
 * @DESCRIPTION 阿里云语音处理服务
 * @create 2019/3/20
 */
public interface NlsService {

    /**
     * 语音转换文字
     * @param ins 语音流 .
     * @return Optional<String> 返回文字结果 .
     */
    public Optional<String> speechRecognizer(String id, InputStream ins);

    /**
     * 文字转换语音
     * @param content 转换的文字
     * @return Optional<String> 文件路径
     */
    public Optional<String> speechSynthesizer(String id, String content);
}

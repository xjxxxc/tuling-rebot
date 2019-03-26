package com.xjxxxc.service.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/*
 * @author Jason Xu
 * @DESCRIPTION Aliyun配置信息
 * @create 2019/3/20
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun")
public class AliyunProperties {
    /**
     * API URL
     *     https://help.aliyun.com/document_detail/72153.html?spm=a2c4g.11174283.3.4.7c747275FcxcCg
     */

    /**
     * accessKeyId,控制台查看
     */
    private String accessKeyId;

    /**
     * asrkey
     */
    private String asrAppKey;

    /**
     * secret,控制台查看
     */
    private String secret;

    /**
     * 产品的通用访问地址
     */
    private String region = "cn-shanghai";

    /**
     * 产品的通用访问域名
     */
    private String domain = "nls-meta.cn-shanghai.aliyuncs.com";

    /**
     * 该API的版本号，格式’YYYY-MM-DD’
     */
    private String version = "2018-05-18";

    /**
     * 	该API的路径
     */
    private String uriPattern = "/pop/2018-05-18/tokens";
}

package com.xjxxxc.configurer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** 
 * @author: Jason Xu
 * @Date: 2019年3月16日
 * @description: Api配置文件
 */
@Configuration
@ConfigurationProperties(prefix = "api")
public class ApiConfigurer {
	public static String accessToken;
	public static String serverUrl;
	
	
}
 
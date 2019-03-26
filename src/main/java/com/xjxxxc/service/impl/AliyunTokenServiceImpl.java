package com.xjxxxc.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.xjxxxc.service.AliyunTokenService;
import com.xjxxxc.service.conf.AliyunProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;


/*
 * @author Jason Xu
 * @DESCRIPTION Aliyun获取Token服务实现
 * @create 2019/3/20
 */
@Service
@Slf4j
public class AliyunTokenServiceImpl implements AliyunTokenService {

    @Autowired
    private AliyunProperties aliyunProperties;
    private IAcsClient client;

    @PostConstruct
    public void init() {

        log.info("init aliyun Profile");
        DefaultProfile profile = DefaultProfile.
                getProfile(aliyunProperties.getRegion(),
                        aliyunProperties.getAccessKeyId(),
                        aliyunProperties.getSecret());
        client = new DefaultAcsClient(profile);
    }

    @Override
    public Optional<String> token() throws Exception {

        //TODO:cache token
        return getToken();
    }

    private Optional<String> getToken() throws Exception {

        // ~ create CommonRequest
        CommonRequest request = new CommonRequest();
        request.setDomain(aliyunProperties.getDomain());
        request.setRegionId(aliyunProperties.getRegion());
        request.setVersion(aliyunProperties.getVersion());
        request.setUriPattern(aliyunProperties.getUriPattern());
        request.setMethod(MethodType.POST);

        // ~ request common for ali yun server
        CommonResponse response = client.getCommonResponse(request);

        log.debug("get request from ali response:{}", response);

        if (response.getHttpStatus() != 200) {
            return Optional.empty();
        }

        JSONObject result = JSON.parseObject(response.getData());
        JSONObject jsonToken = result.getJSONObject("Token");
        return Optional.of(jsonToken.getString("Id"));
    }
}

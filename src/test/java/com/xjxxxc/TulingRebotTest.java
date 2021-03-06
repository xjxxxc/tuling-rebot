package com.xjxxxc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xjxxxc.model.ConvertInfo;
import com.xjxxxc.service.NlsService;
import com.xjxxxc.utils.DatabaseUtils;
import com.xjxxxc.utils.IdGen;
import com.xjxxxc.utils.TuLingApiUtil;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/*
 * @author Jason Xu
 * @DESCRIPTION 模拟：语音转换 -> 文字 -> 图灵机器人 -> 文字 -> 语音 的转换流程
 * @create 2019/3/26
 */
public class TulingRebotTest extends Tester {
    @Resource
    NlsService nlsService;
    /**
     * 模拟：语音转换 -> 文字 -> 图灵机器人 -> 文字 -> 语音 的转换流程
     */
    @Test
    public void unite() {

        try {
            String uuid = IdGen.genId();
            ConvertInfo convertInfo = null;
            //0 .模拟发起语音
            nlsService.speechSynthesizer(uuid, "北京的天气怎样呢?");
            convertInfo = DatabaseUtils.map.get(uuid);

            File file = new File(convertInfo.getFilepath());
            InputStream inputStream = new FileInputStream(file);

            //1.语音转成文字
            nlsService.speechRecognizer(uuid, inputStream);
            convertInfo = DatabaseUtils.map.get(uuid);
            System.out.println(convertInfo.getContent());

            //2.调图灵机器人，获取结果
            String tulingResult = TuLingApiUtil.getResult(convertInfo.getContent());
            JSONObject tulingJsonResult = JSON.parseObject(tulingResult);
            System.out.println(tulingJsonResult);
            System.out.println(tulingJsonResult.getString("text"));

            //3.将图灵结果解析成语音
            nlsService.speechSynthesizer(uuid, tulingJsonResult.getString("text"));
            convertInfo = DatabaseUtils.map.get(uuid);
            System.out.println(convertInfo.toString());

        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
    }
}

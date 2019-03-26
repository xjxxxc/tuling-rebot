package com.xjxxxc.service.impl;

import com.xjxxxc.Tester;
import com.xjxxxc.model.ConvertInfo;
import com.xjxxxc.service.NlsService;
import com.xjxxxc.utils.DatabaseUtils;
import com.xjxxxc.utils.IdGen;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/*
 * @author Jason Xu
 * @DESCRIPTION ${DESCRIPTION}
 * @create 2019/3/20
 */
public class NlsServiceImplTest extends Tester {
    @Resource
    NlsService nlsService;

    @Test
    public void speechRecognizer() {
        try {
            String uuid = IdGen.genId();
            File file = new File("428b9cfb9a2044e791e499a26cd572fc.wav");
            InputStream inputStream = new FileInputStream(file);
            nlsService.speechRecognizer(uuid, inputStream);
            ConvertInfo convertInfo = DatabaseUtils.map.get(uuid);
            System.out.println(convertInfo.toString());
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }

    }

    @Test
    public void speechSynthesizer() {
        String uuid = IdGen.genId();
        nlsService.speechSynthesizer(uuid, "深圳的天气怎样呢?");
        ConvertInfo convertInfo = DatabaseUtils.map.get(uuid);
        System.out.println(convertInfo.toString());
    }
}
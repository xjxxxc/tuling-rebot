package com.xjxxxc.service.impl;

import com.alibaba.nls.client.protocol.InputFormatEnum;
import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.OutputFormatEnum;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import com.alibaba.nls.client.protocol.asr.SpeechRecognizer;
import com.alibaba.nls.client.protocol.asr.SpeechRecognizerListener;
import com.alibaba.nls.client.protocol.asr.SpeechRecognizerResponse;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizer;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizerListener;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizerResponse;
import com.alibaba.nls.client.util.IdGen;
import com.xjxxxc.model.ConvertInfo;
import com.xjxxxc.service.AliyunTokenService;
import com.xjxxxc.service.NlsService;
import com.xjxxxc.service.conf.AliyunProperties;
import com.xjxxxc.utils.DatabaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/*
 * @author Jason Xu
 * @DESCRIPTION 阿里云语音处理服务
 * @create 2019/3/20
 */
@Service
@Slf4j
public class NlsServiceImpl implements NlsService {
    @Autowired
    private AliyunProperties aliyunProperties;

    @Autowired
    private AliyunTokenService aliyunTokenService;

    private  NlsClient client;


    @Value("${store.dir}")
    private static String store;

    /**
     * 语音转换文字
     *
     * @param ins 语音流 .
     * @return Optional<String> 返回文字结果 .
     */
    @Override
    public Optional<String> speechRecognizer(String id, InputStream ins) {

        SpeechRecognizer recognizer = null;
        try {
            Optional<String> token = aliyunTokenService.token();
            // Step0 创建NlsClient实例,应用全局创建一个即可,默认服务地址为阿里云线上服务地址
            if(!token.isPresent()){
                log.info("获取token失败");
            }

            client = new NlsClient(token.get());



            // Step1 创建实例,建立连接
            recognizer = new SpeechRecognizer(client, getRecognizerListener(id));
            recognizer.setAppKey(aliyunProperties.getAsrAppKey());
            // 设置音频编码格式
            recognizer.setFormat(InputFormatEnum.PCM);
            // 设置音频采样率
            recognizer.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
            // 设置是否返回中间识别结果
            recognizer.setEnableIntermediateResult(true);

            // Step2 此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
            recognizer.start();
            // Step3 语音数据来自声音文件用此方法,控制发送速率;若语音来自实时录音,不需控制发送速率直接调用 recognizer.sent(ins)即可
            recognizer.send(ins, 6400, 200);
            // Step4 通知服务端语音数据发送完毕,等待服务端处理完成
            recognizer.stop();
        } catch (Exception e) {
            log.info("语音转换文字失败:{}",e.getMessage());
        } finally {
            // Step5 关闭连接
            if (null != recognizer) {
                recognizer.close();
            }
            //关闭连接
            shutdown();
        }

        return Optional.empty();
    }

    /**
     * 文字转换语音
     *
     * @param content 转换的文字
     * @return Optional<String> 文件路径
     */
    @Override
    public Optional<String> speechSynthesizer(String id, String content) {

        SpeechSynthesizer synthesizer = null;
        try {
            Optional<String> token = aliyunTokenService.token();
            // Step0 创建NlsClient实例,应用全局创建一个即可,默认服务地址为阿里云线上服务地址
            if(!token.isPresent()){
                log.info("获取token失败");
            }
            client = new NlsClient(token.get());

            // Step1 创建实例,建立连接
            synthesizer = new SpeechSynthesizer(client, getSynthesizerListener(id));
            synthesizer.setAppKey(aliyunProperties.getAsrAppKey());
            // 设置返回音频的编码格式
            synthesizer.setFormat(OutputFormatEnum.WAV);
            // 设置返回音频的采样率
            synthesizer.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
            // 设置用于语音合成的文本
            synthesizer.setText(content);

            // Step2 此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
            synthesizer.start();
            // Step3 等待语音合成结束
            synthesizer.waitForComplete();
        } catch (Exception e) {
            log.info("文字转换语音失败:{}",e.getMessage());
        } finally {
            // Step4 关闭连接
            if (null != synthesizer) {
                synthesizer.close();
            }

            //关闭连接
            shutdown();
        }

        return Optional.empty();
    }


    private static SpeechRecognizerListener getRecognizerListener(String id) {
        SpeechRecognizerListener listener = new SpeechRecognizerListener() {
            // 识别出中间结果.服务端识别出一个字或词时会返回此消息.仅当setEnableIntermediateResult(true)时,才会有此类消息返回
            @Override
            public void onRecognitionResultChanged(SpeechRecognizerResponse response) {
                // 事件名称 RecognitionResultChanged
                System.out.println("name: " + response.getName() +
                        // 状态码 20000000 表示识别成功
                        ", status: " + response.getStatus() +
                        // 一句话识别的中间结果
                        ", result: " + response.getRecognizedText());
            }

            // 识别完毕
            @Override
            public void onRecognitionCompleted(SpeechRecognizerResponse response) {
                // 事件名称 RecognitionCompleted
                System.out.println("name: " + response.getName() +
                        // 状态码 20000000 表示识别成功
                        ", status: " + response.getStatus() +
                        // 一句话识别的完整结果
                        ", result: " + response.getRecognizedText());
                ConvertInfo convertInfo = new ConvertInfo();
                convertInfo.setId(id);
                convertInfo.setContent(response.getRecognizedText());

                DatabaseUtils.setKey(id,convertInfo);
            }
        };
        return listener;
    }


    private static SpeechSynthesizerListener getSynthesizerListener(String id) {
        SpeechSynthesizerListener listener = null;

        try {
            listener = new SpeechSynthesizerListener() {
                final String filepath = IdGen.genId() + ".wav";
                File f = new File(filepath);
                FileOutputStream fout = new FileOutputStream(f);



                // 语音合成结束
                @Override
                public void onComplete(SpeechSynthesizerResponse response) {
                    // 事件名称 SynthesisCompleted
                    System.out.println("name: " + response.getName() +
                            // 状态码 20000000 表示识别成功
                            ", status: " + response.getStatus() +
                            // 语音合成文件路径
                            ", output file :" + f.getAbsolutePath()
                    );
                    ConvertInfo convertInfotmp = new ConvertInfo();
                    convertInfotmp.setFilepath(f.getAbsolutePath());
                    DatabaseUtils.setKey(id,convertInfotmp);
                }

                // 语音合成的语音二进制数据
                @Override
                public void onMessage(ByteBuffer message) {
                    try {
                        byte[] bytesArray = new byte[message.remaining()];
                        message.get(bytesArray, 0, bytesArray.length);
                        System.out.println("write array:" + bytesArray.length);
                        fout.write(bytesArray);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listener;
    }


    public void shutdown() {
        client.shutdown();
    }

    /**
     * auto generate filename
     *
     * @return the new note default filename
     */
    public static String generateFilePath() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(format);
    }
}

package com.cslg.push;


import com.google.gson.Gson;
import com.cslg.global.GlobalConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：
 * 发送小程序订阅消息
 */
@Service
@Slf4j
public class SendWxMessage {

    //发送订阅消息
    public String pushOneUser(Integer id) {
        RestTemplate restTemplate = new RestTemplate();
        //这里简单起见我们每次都获取最新的access_token（时间开发中，应该在access_token快过期时再重新获取）
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + getAccessToken();
        //拼接推送的模版
        WxMssVo wxMssVo = new WxMssVo();

        wxMssVo.setPage("pages/index/index");
        //封装模板消息内容。必须和你申请的小程序模板格式一模一样。
        Map<String, TemplateData> m = new HashMap<>(2);
        //A小桌，B大桌

        m.put("phrase1", new TemplateData("您可入座啦"));

        wxMssVo.setData(m);
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(url, wxMssVo, String.class);
        log.info("推送返回的信息 ={}", responseEntity.getBody());
        return responseEntity.getBody();
    }
    //获取AccessToken
    public String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();
        params.put("APPID", GlobalConst.APPID);  //这里替换成你的appid
        params.put("APPSECRET", GlobalConst.APPSECRET);  //这里替换成你的appsecret
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}", String.class, params);
        String body = responseEntity.getBody();
        AccessToken object = new Gson().fromJson(body, AccessToken.class);
        log.info("返回的AccessToken={}", object);
        String Access_Token = object.getAccess_token();
        return Access_Token;
    }
}

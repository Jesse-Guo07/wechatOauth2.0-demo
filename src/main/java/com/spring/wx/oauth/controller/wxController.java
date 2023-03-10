package com.spring.wx.oauth.controller;


import com.alibaba.fastjson.JSONObject;
import com.spring.wx.oauth.entity.UserInfo;
import com.spring.wx.oauth.utils.httpClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class wxController {

    @Value("${oauth.wx.appid}")
    private String appid;

    @Value("${oauth.wx.appsecret}")
    private String appsecret;

    @Value("${oauth.callback.http}")
    private String http;

    @GetMapping("/")
    public String index() {
        return "/index";
    }

    @GetMapping("/wxlogin")
    public String wxlogin(){
        //第一步：用户同意授权，获取code
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid=" + appid +
                "&redirect_uri=" + http +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=STATE#wechat_redirect";

        return "redirect:" + url;
    }


    @GetMapping("/wxcallback")
    public String wxCallback(@RequestParam String code,
                             Model map) throws IOException {

        //第二步：通过 code 换取网页授权access_token以及openid
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + appid +
                "&secret=" + appsecret +
                "&code=" + code +
                "&grant_type=authorization_code";

        JSONObject jsonObject = httpClientUtils.doGet(url);
        String openid = jsonObject.getString("openid");
        String accessToken = jsonObject.getString("access_token");

        //拉取用户信息(需 scope 为 snsapi_userinfo)，scope为 snsapi_base 不弹出授权页面，直接跳转，只能获取用户openid
        String userinfoUrl = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + accessToken +
                "&openid=" + openid +
                "&lang=zh_CN";

        JSONObject userInfoJson = httpClientUtils.doGet(userinfoUrl);
        System.out.println(userInfoJson);
        UserInfo user = new UserInfo();
        user.setHeadimgurl(userInfoJson.getString("headimgurl"));
        user.setNickname(userInfoJson.getString("nickname"));
        map.addAttribute("user", user);
        return "home";
    }
}

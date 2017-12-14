package com.example.ljh.faceln;

import com.baidu.aip.face.AipFace;

/**
 * Created by ljh on 2017/11/29.
 */

public class BaiDuConfig {

    static final String APPID = "10457231";
    static final String API_KEY = "o7Gecp0z8PzpSAgIVGGcyD2L";
    static final String SECRET_KEY = "mG0IH9n1XUluvQciiZ2TtLTjrly4Ts3O";
    static final String url = "https://aip.baidubce.com/rest/2.0/face/v2/";

    static AipFace aipFace;

    public static AipFace getAipFace() {
        aipFace = new AipFace(APPID, API_KEY, SECRET_KEY);
        aipFace.setConnectionTimeoutInMillis(3000);
        aipFace.setSocketTimeoutInMillis(600000);

        return aipFace;
    }
}

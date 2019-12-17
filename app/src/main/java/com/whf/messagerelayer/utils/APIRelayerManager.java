package com.whf.messagerelayer.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class APIRelayerManager {

    public static final int CODE_SUCCESS = 0x1;
    public static final int CODE_FAILE = 0x0;

    private static final String URL = "http://dy.ghostry.cn/Home/api/sms/";

    //发送短信至API
    public static int relayAPI(NativeDataManager dataManager, String PHONE, String content) {
        String full_url = creatURL(dataManager);
        String params = creatParams(dataManager, PHONE, content);
        System.out.print(full_url);
        System.out.print(params);
        int status = 0;
        try {
            HttpUtils http = new HttpUtils();
            String ret = http.sendPost(full_url, params);
            System.out.print(ret);
            try {
                JSONObject obj = new JSONObject(ret);
                status = obj.getInt("status");
            } catch (JSONException e) {
                e.printStackTrace();
                return CODE_FAILE;
            }
            if (status == 1) {
                return CODE_SUCCESS;
            } else {
                return CODE_FAILE;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return CODE_FAILE;
        }
    }

    /**
     * 构建URL
     *
     * @return String
     */
    private static String creatURL(NativeDataManager dataManager) {
        String user_id = dataManager.getAPIID();
        return URL + "user/" + user_id + "/";
    }

    /**
     * 构建参数
     *
     * @return String
     */
    private static String creatParams(NativeDataManager dataManager, String phone, String content) {
        String token = dataManager.getAPIToken();
        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("token", token);
            jsonParam.put("phone", phone);
            jsonParam.put("text", content);
            String param = jsonParam.toString();
            return param;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }


}

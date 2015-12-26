package org.zywx.wbpalmstar.plugin.uextestincrash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.testin.agent.TestinAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;

public class EUExTestinCrash extends EUExBase {

    private static final String BUNDLE_DATA = "data";
    private static final int MSG_INIT = 1;
    private static final int MSG_SET_USER_INFO = 2;
    private static final int MSG_LEAVE_BREADCRUMB = 3;
    private static final int MSG_TEST = 4;

    public EUExTestinCrash(Context context, EBrowserView eBrowserView) {
        super(context, eBrowserView);
    }

    @Override
    protected boolean clean() {
        return false;
    }


    public void init(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_INIT;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void initMsg(String[] params) {
        String json = params[0];
        String appKey=null;
        String channel=null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            appKey=jsonObject.optString("appKey");
            channel=jsonObject.optString("channel");

        } catch (JSONException e) {
        }
        TestinAgent.init(mContext.getApplicationContext(),appKey,channel);
    }

    public void setUserInfo(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SET_USER_INFO;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void setUserInfoMsg(String[] params) {
        String json = params[0];
        String username=null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            username=jsonObject.optString("username");
        } catch (JSONException e) {
        }
        TestinAgent.setUserInfo(username);
    }

    public void leaveBreadcrumb(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_LEAVE_BREADCRUMB;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void leaveBreadcrumbMsg(String[] params) {
        String json = params[0];
        String breadcrumb=null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            breadcrumb=jsonObject.optString("breadcrumb");
        } catch (JSONException e) {
        }
        TestinAgent.leaveBreadcrumb(breadcrumb);
    }

    public void test(String[] params) {
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_TEST;
        mHandler.sendMessage(msg);
    }

    private void testMsg(String[] params) {
        String aa=null;
        Log.i("ylt",aa.toString()+"");
        String json = params[0];
        try {
            JSONObject jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHandleMessage(Message message) {
        if(message == null){
            return;
        }
        Bundle bundle=message.getData();
        switch (message.what) {

            case MSG_INIT:
                initMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_SET_USER_INFO:
                setUserInfoMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_LEAVE_BREADCRUMB:
                leaveBreadcrumbMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_TEST:
                testMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            default:
                super.onHandleMessage(message);
        }
    }

    private void callBackPluginJs(String methodName, String jsonData){
        String js = SCRIPT_HEADER + "if(" + methodName + "){"
                + methodName + "('" + jsonData + "');}";
        onCallback(js);
    }

}

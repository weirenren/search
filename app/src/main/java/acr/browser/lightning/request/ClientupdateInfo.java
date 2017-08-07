package acr.browser.lightning.request;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import acr.browser.lightning.annotation.NoProguard;
import acr.browser.lightning.constant.Constants;

/**
 * Created by weichao13 on 2017/8/7.
 */
@NoProguard
public class ClientupdateInfo {

    /** Log TAG. */
    private static final String TAG = "ClientupdateInfo";
    /** log 开关。 */
    private static final boolean DEBUG = Constants.DEBUG;

    /** 强制升级的标识 1 */
    private static final int IS_FORCE_UPDATE = 1;

    /** version name*/
    public String mVersionName;
    /** version code*/
    public int mVcode;
    /** 升级包下载地址*/
    public String mDownloadUrl;
    /** 升级包md5加密字符串*/
    public String mSign;
    /** 升级说明*/
    public String mChangeLog;
    /** 是否是强制升级*/
    public boolean mIsForce;
    /** 解密后的升级包的md5*/
    public String mApkMd5;
    /** banner url*/
    public String mBannerUrl;
    /** notify title*/
    public String mNotifyTitle;
    /** notify subtitle*/
    public String mNotifySubTitle;

    /**
     * private constructor
     */
    private ClientupdateInfo() {

    }

    /**
     * 解析升级信息
     * @param json 升级结果json
     * @return 升级信息
     */
    public static ClientupdateInfo parseFromJson(JSONObject json) {
        ClientupdateInfo updateInfo = parse(json);
        if (null == updateInfo) {
            printLog(json);
        }
        return updateInfo;
    }

    /**
     * 解析升级信息
     * @param json 升级结果json
     * @return 升级信息
     */
    private static ClientupdateInfo parse(JSONObject json) {
        if (null == json) {
            printLog(json);
            return null;
        }

        ClientupdateInfo updateInfo = new ClientupdateInfo();

        try {
            String forceValue = json.optString("isforce");
            if (TextUtils.isEmpty(forceValue)) {
                printLog(json);
                return null;
            }
            updateInfo.mIsForce = (Integer.valueOf(forceValue) == IS_FORCE_UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
            printLog(json);
            return null;
        }

        if (DEBUG) {
            Log.d(TAG, "isforce = " + updateInfo.mIsForce);
        }

        updateInfo.mVersionName = json.optString("vname");
        if (TextUtils.isEmpty(updateInfo.mVersionName)) {
            return null;
        }

        if (!json.has("vcode")) {
            return null;
        } else {
            try {
                updateInfo.mVcode = Integer.parseInt(json.optString("vcode", "0"));
                if (updateInfo.mVcode == 0) {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }

        updateInfo.mDownloadUrl = json.optString("downurl");
        if (TextUtils.isEmpty(updateInfo.mDownloadUrl)) {
            return null;
        }
        updateInfo.mChangeLog = json.optString("changelog");
        if (TextUtils.isEmpty(updateInfo.mChangeLog)) {
            printLog(json);
            return null;
        }

        updateInfo.mSign = json.optString("sign");
        if (TextUtils.isEmpty(updateInfo.mSign)) {
            return null;
        }
        // 迭代126新加入LC平台字段
        JSONObject custom = json.optJSONObject("custom");
        if (custom != null) {
            updateInfo.mBannerUrl = custom.optString("dialog_banner");
            updateInfo.mNotifyTitle = custom.optString("notifi_title");
            updateInfo.mNotifySubTitle = custom.optString("notifi_subtitle");
        } else {
            printLog(json);
            return null;
        }


        if (DEBUG) {
            Log.d(TAG, "--有更新--- 更新信息：  " + updateInfo);
        }
        return updateInfo;
    }

    /**
     * 打印升级信息错误log
     * @param json 升级结果信息
     */
    private static  void printLog(JSONObject json) {
        Log.w(TAG,
                "state: 服务端下发的升级信息非法，json内容为：" + json);
    }

    @Override
    public String toString() {
        return "ClientupdateInfo{"
                + ", mVersionName='" + mVersionName + '\''
                + ", mVcode='" + mVcode + '\''
                + ", mDownloadUrl='" + mDownloadUrl + '\''
                + ", mSign='" + mSign + '\''
                + ", mChangeLog='" + mChangeLog + '\''
                + ", mIsForce=" + mIsForce
                + ", mApkMd5='" + mApkMd5 + '\''
                + '}';
    }
}

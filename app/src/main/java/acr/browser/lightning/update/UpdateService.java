package acr.browser.lightning.update;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import acr.browser.lightning.constant.Constants;

/**
 * Created by codeest on 16/10/10.
 */

public class UpdateService extends Service {
    private BroadcastReceiver receiver;

    public static final String APK_DOWNLOAD_URL = "apk_download_url";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();

        String downloadurl = null;
        if (bundle != null) {
            downloadurl = bundle.getString(APK_DOWNLOAD_URL);
        }

        if (downloadurl == null) {
            stopSelf();
        }
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                unregisterReceiver(receiver);
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(Constants.getUpdateApkFile(getApplication())),
                        "application/vnd.android.package-archive");
//                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/geeknews.apk")),
//                        "application/vnd.android.package-archive");
                startActivity(intent);
                stopSelf();
            }
        };
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        startDownload(downloadurl);
        return Service.START_STICKY;
    }

    private void startDownload(String downloadurl) {
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(downloadurl));
        request.setTitle("火箭浏览器");
        request.setDescription("新版本下载中");
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "geeknews.apk");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        dm.enqueue(request);
        Toast.makeText(getApplicationContext(), "后台下载中，请稍候...", Toast.LENGTH_LONG).show();
    }
}

package acr.browser.lightning.download;

/**
 * Created by weichao13 on 2017/8/7.
 */

public interface IDownLoadListener {
    void onStart(long totalSize);

    void onProgress(float progress);

    void onDownloadFinish();

    void onDownloadError();
}

package acr.browser.lightning.browser;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;

import acr.browser.lightning.download.IDownLoadListener;
import acr.browser.lightning.request.ClientupdateInfo;

public interface BrowserView extends IDownLoadListener {

    void setTabView(@NonNull View view);

    void removeTabView();

    void updateUrl(@Nullable String url, boolean isLoading);

    void updateProgress(int progress);

    void updateTabNumber(int number);

    void closeBrowser();

    void closeActivity();

    void showBlockedLocalFileDialog(@NonNull DialogInterface.OnClickListener listener);

    void showSnackbar(@StringRes int resource);

    void setForwardButtonEnabled(boolean enabled);

    void setBackButtonEnabled(boolean enabled);

    void notifyTabViewRemoved(int position);

    void notifyTabViewAdded();

    void notifyTabViewChanged(int position);

    void notifyTabViewInitialized();

    void updateApp(boolean update, ClientupdateInfo clientupdateInfo);
}

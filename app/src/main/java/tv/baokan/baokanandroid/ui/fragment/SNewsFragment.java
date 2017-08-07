package tv.baokan.baokanandroid.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;

import acr.browser.lightning.R;
import acr.browser.lightning.browser.TabsManager;
import acr.browser.lightning.view.LightningView;


/**
 * Created by weichao13 on 2017/8/5.
 */

public class SNewsFragment extends BaseFragment {


    private TabsManager tabsManager = new TabsManager();

    LightningView lightningView;
    @Override
    protected View prepareUI() {
        LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.fragment_snews, null);

        lightningView = tabsManager.newTab(getActivity(), "https://m.toutiao.com/?W2atIF=1", true);

        view.addView(lightningView.getWebView());
        return view;
    }
}

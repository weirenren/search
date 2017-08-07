package tv.baokan.baokanandroid.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.share.news.R;

/**
 * Created by weichao13 on 2017/8/5.
 */

public class SNewsFragment extends BaseFragment {


    @Override
    protected View prepareUI() {


        LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.fragment_news, null);
        return view;
    }
}

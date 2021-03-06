package tv.baokan.baokanandroid.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import acr.browser.lightning.R;
import tv.baokan.baokanandroid.adapter.PhotoListRecyclerViewAdapter;
import tv.baokan.baokanandroid.cache.NewsDALManager;
import tv.baokan.baokanandroid.model.ArticleListBean;
import tv.baokan.baokanandroid.ui.activity.PhotoDetailActivity;
import tv.baokan.baokanandroid.utils.NetworkUtils;
import tv.baokan.baokanandroid.utils.ProgressHUD;

public class PhotoListFragment extends BaseFragment {

    private String classid;     // 栏目id
    private int pageIndex = 1;  // 当前页码

    private TwinklingRefreshLayout refreshLayout;  // 上下拉刷新
    private RecyclerView mPhotoListRecyclerView;    // 列表视图
    private PhotoListRecyclerViewAdapter newsListAdapter;       // 列表视图的适配器

    public static PhotoListFragment newInstance(String classid) {
        PhotoListFragment newFragment = new PhotoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("classid", classid);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    protected View prepareUI() {
        View view = View.inflate(mContext, R.layout.fragment_news_list, null);
        mPhotoListRecyclerView = (RecyclerView) view.findViewById(R.id.rv_news_list_recyclerview);
        refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.srl_news_list_refresh);
        return view;
    }

    @Override
    protected void loadData() {

        // 取出构造里的分类id
        Bundle args = getArguments();
        if (args != null) {
            classid = args.getString("classid");
        }

        // 配置recyclerView图库列表
        setupRecyclerView();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 设置刷新监听器
                setupRefresh();
            }
        }, 100);

    }

    /**
     * 配置recyclerView图库列表
     */
    private void setupRecyclerView() {
        mPhotoListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        newsListAdapter = new PhotoListRecyclerViewAdapter(mContext);
        mPhotoListRecyclerView.setAdapter(newsListAdapter);
        newsListAdapter.setOnItemTapListener(new PhotoListRecyclerViewAdapter.OnItemTapListener() {
            @Override
            public void onItemTapListener(ArticleListBean articleListBean) {
                openPhotoDetail(articleListBean);
            }
        });

        // 监听滚动
        mPhotoListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        if (!Fresco.getImagePipeline().isPaused()) {
                            Fresco.getImagePipeline().pause();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (Fresco.getImagePipeline().isPaused()) {
                            Fresco.getImagePipeline().resume();
                        }
                        break;
                }
            }
        });
    }

    /**
     * 配置刷新控件
     */
    private void setupRefresh() {

        // 顶部刷新视图
        SinaRefreshView sinaRefreshView = new SinaRefreshView(mContext);
        sinaRefreshView.setArrowResource(R.drawable.pull_refresh_arrow);
        refreshLayout.setHeaderView(sinaRefreshView);

        // 到达底部自动加载更多
        refreshLayout.setAutoLoadMore(true);

        // 监听刷新
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                // 下拉刷新数据时，在有网络情况下清除本地缓存的数据
                if (NetworkUtils.shared.isNetworkConnected(mContext)) {
                    NewsDALManager.shared.removeNewsList(classid);
                }

                // 重新加载并缓存数据
                loadNewsFromNetwork(classid, 1, 0);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                pageIndex += 1;
                loadNewsFromNetwork(classid, pageIndex, 1);
            }
        });

        // 默认加载一次数据 不使用下拉刷新
        loadNewsFromNetwork(classid, 1, 0);

    }

    /**
     * 加载列表数据
     *
     * @param classid   分类id
     * @param pageIndex 页码
     * @param method    加载方式 0下拉 1上拉
     */
    private void loadNewsFromNetwork(final String classid, int pageIndex, final int method) {

        // 从数据访问层加载数据
        NewsDALManager.shared.loadNewsList("photo", classid, pageIndex, new NewsDALManager.NewsListCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                try {
                    List<ArticleListBean> tempListBeans = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ArticleListBean bean = new ArticleListBean(jsonArray.getJSONObject(i));
                        tempListBeans.add(bean);
                    }
                    if (tempListBeans.size() == 0) {
                        ProgressHUD.showInfo(mContext, "没有数据了~");
                    } else {
                        // 刷新数据
                        newsListAdapter.updateData(tempListBeans, method);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (method == 0) {
                        refreshLayout.finishRefreshing();
                    } else {
                        refreshLayout.finishLoadmore();
                    }
                }
            }

            @Override
            public void onError(String tipString) {
                ProgressHUD.showInfo(mContext, tipString);
                if (method == 0) {
                    refreshLayout.finishRefreshing();
                } else {
                    refreshLayout.finishLoadmore();
                }
            }
        });

    }

    /**
     * 打开图片详情页面
     *
     * @param articleBean 文章模型
     */
    private void openPhotoDetail(ArticleListBean articleBean) {
        PhotoDetailActivity.start(getActivity(), articleBean.getClassid(), articleBean.getId());
    }

}

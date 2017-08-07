package acr.browser.lightning;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by weichao13 on 2017/8/5.
 */

public class GridAdapter extends BaseAdapter {

    private List<ItemData> dataList;

    private Context mContext;

    public GridAdapter(Context context) {
        this.mContext = context;
    }

    public void setDataList(List<ItemData> list) {
        this.dataList = list;
    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {

            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            view = inflater.inflate(R.layout.gridview_item, viewGroup, false);
            holder = new ViewHolder();
            holder.textView = view.findViewById(R.id.item_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.textView.setText(dataList.get(i).title);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebLauncherListener launcherListener = dataList.get(i).launcherListener;
                if (launcherListener!= null) {
                    launcherListener.launch(i, dataList.get(i).url);
                }
            }
        });

        return view;
    }

    private class ViewHolder {
        TextView textView;
    }

    public static class ItemData {
        public ItemData(String title, String url) {
            this.title = title;
            this.url = url;
        }


        public ItemData(String title, String url, WebLauncherListener launcherListener) {
            this.title = title;
            this.url = url;
            this.launcherListener = launcherListener;
        }

        public String title;

        public String url;

        public WebLauncherListener launcherListener;

    }

    public interface WebLauncherListener {
        void launch(int id, String url);
    }

}

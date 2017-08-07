package acr.browser.lightning;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weichao13 on 2017/8/5.
 */

public class HomeActivity extends Activity {


    private GridView gridView;

    private GridAdapter gridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        gridView = findViewById(R.id.home_gridview);

        gridAdapter = new GridAdapter(this);
        gridAdapter.setDataList(genData());
        gridView.setAdapter(gridAdapter);


        gridAdapter.notifyDataSetChanged();



    }

    public static List<GridAdapter.ItemData> genData() {

        List<GridAdapter.ItemData> list = new ArrayList<>();

        list.add(new GridAdapter.ItemData("头条", "https://m.toutiao.com/?W2atIF=1"));
        list.add(new GridAdapter.ItemData("头条", "https://m.toutiao.com/?W2atIF=1"));

        list.add(new GridAdapter.ItemData("头条", "https://m.toutiao.com/?W2atIF=1"));
        list.add(new GridAdapter.ItemData("头条", "https://m.toutiao.com/?W2atIF=1"));

        list.add(new GridAdapter.ItemData("头条", "https://m.toutiao.com/?W2atIF=1"));

        return list;
    }
}

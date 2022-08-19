package com.example.xiaomaibu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.xiaomaibu.Adapter.Myadapter;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.Dao.GoodsDao;
import com.example.xiaomaibu.goods.GoodsInfoActivity;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private GridView gridView;
    private ListView listView;
    private String  eamil;
    private List<Goods> goodsList;
    private Myadapter myadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView=findViewById(R.id.listView);
        Intent intent=getIntent();
        String like=intent.getStringExtra("like");
        GoodsDao goodsDao=new GoodsDao();
         goodsList=goodsDao.findlike(SearchActivity.this,like);
        if(like==null){
            String sort=intent.getStringExtra("sort");
            goodsList=goodsDao.findsort(SearchActivity.this,sort);
        }

        Myadapter myadapter=new Myadapter(SearchActivity.this,goodsList);
        listView.setAdapter(myadapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Goods goods=(Goods) adapterView.getItemAtPosition(i);
                Intent intent=new Intent(SearchActivity.this, GoodsInfoActivity.class);
                intent.putExtra("gid",goods.getGid());
                startActivity(intent);
            }
        });
    }

}

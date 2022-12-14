package com.example.xiaomaibu.AdminMainBlg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.xiaomaibu.Adapter.AdminBlgAdapter;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.Dao.GoodsDao;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.goods.AddGoodsActivity;

import java.util.List;

public class AdminBlgActivity extends AppCompatActivity {
    private ListView listView;
    private AdminBlgAdapter myadapter;
    private List<Goods> goodsList;
    private String gid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_blg);
        listView=findViewById(R.id.admin_listView);
        final GoodsDao goodsDao=new GoodsDao();
        goodsList=goodsDao.findall(AdminBlgActivity.this);
        myadapter=new AdminBlgAdapter(AdminBlgActivity.this,goodsList);
        listView.setAdapter(myadapter);
        setListViewHeightBasedOnChildren(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Goods goods=(Goods) adapterView.getItemAtPosition(i);
                Intent intent=new Intent(AdminBlgActivity.this, AlterGoodsBlgActivity.class);
                intent.putExtra("gid",goods.getGid());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Goods goods=(Goods) adapterView.getItemAtPosition(i);
                gid=goods.getGid();
                AlertDialog.Builder builder=new AlertDialog.Builder(AdminBlgActivity.this);
                builder.setMessage("?????????????");
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       boolean flag= goodsDao.Delete(AdminBlgActivity.this,gid);
                       if (flag){
                           Toast.makeText(AdminBlgActivity.this,"????????????",Toast.LENGTH_SHORT).show();
                           GoodsDao goodsDao=new GoodsDao();
                           goodsList=goodsDao.findall(AdminBlgActivity.this);
                           myadapter=new AdminBlgAdapter(AdminBlgActivity.this,goodsList);
                           listView.setAdapter(myadapter);
                           setListViewHeightBasedOnChildren(listView);
                       }else {
                           Toast.makeText(AdminBlgActivity.this,"????????????",Toast.LENGTH_SHORT).show();
                       }
                    }
                });
                builder.setNegativeButton("??????",null);
                builder.create().show();

                return true;
            }
        });

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "????????????").setIcon(android.R.drawable.ic_menu_delete);
        menu.add(Menu.NONE, Menu.FIRST + 10, 10, "??????").setIcon(android.R.drawable.ic_menu_delete);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST + 10:
                GoodsDao goodsDao=new GoodsDao();
                goodsList=goodsDao.findall(AdminBlgActivity.this);
               myadapter=new AdminBlgAdapter(AdminBlgActivity.this,goodsList);
                listView.setAdapter(myadapter);
                setListViewHeightBasedOnChildren(listView);
                break;

            case Menu.FIRST + 1:
                Intent intent=new Intent(AdminBlgActivity.this, AddGoodsActivity.class);
                startActivityForResult(intent,0);
                break;




        }
        return super.onOptionsItemSelected(item);
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // ??????ListView?????????Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()????????????????????????
            View listItem = listAdapter.getView(i, null, listView);
            // ????????????View ?????????
            listItem.measure(0, 0);
            // ??????????????????????????????
            totalHeight += listItem.getMeasuredHeight() + 20;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()???????????????????????????????????????
        // params.height??????????????????ListView???????????????????????????
        listView.setLayoutParams(params);
    }
}

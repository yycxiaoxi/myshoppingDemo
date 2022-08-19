package com.example.xiaomaibu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.example.xiaomaibu.Adapter.AdminBlgAdapter;
import com.example.xiaomaibu.AdminMainBlg.AdminBlgActivity;
import com.example.xiaomaibu.AdminMainBlg.AlterGoodsBlgActivity;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.Dao.GoodsDao;
import com.example.xiaomaibu.Login.LoginActivity;
import com.example.xiaomaibu.Util.OkhttpUtils;
import com.example.xiaomaibu.goods.AddGoodsActivity;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class AdminFirstFragment extends Fragment {
    private ListView listView;
    private AdminBlgAdapter myadapter;
    private List<Goods> goodsList;
    private String gid;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_admin_first, container, false);
        listView=root.findViewById(R.id.admin_listView);
        context=root.getContext();
        setHasOptionsMenu(true);//设置有菜单
        try {
            getGoods();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "添加商品").setIcon(android.R.drawable.ic_menu_delete);
        menu.add(Menu.NONE, Menu.FIRST + 10, 10, "刷新").setIcon(android.R.drawable.ic_menu_delete);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST + 10:
                GoodsDao goodsDao=new GoodsDao();
                goodsList=goodsDao.findall(context);
                myadapter=new AdminBlgAdapter(context,goodsList);
                listView.setAdapter(myadapter);
                setListViewHeightBasedOnChildren(listView);
                break;

            case Menu.FIRST + 1:
                Intent intent=new Intent(context, AddGoodsActivity.class);
                startActivityForResult(intent,0);
                break;




        }
        return super.onOptionsItemSelected(item);
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight() + 50;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==0x11){
            GoodsDao goodsDao=new GoodsDao();
            goodsList=goodsDao.findall(context);
            myadapter=new AdminBlgAdapter(context,goodsList);
            listView.setAdapter(myadapter);
            setListViewHeightBasedOnChildren(listView);
        }
    }

    private void getGoods() throws IOException {
        OkhttpUtils.httpGet(OkhttpUtils.IP + "/goods/findall", context, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                String status = OkhttpUtils.getCode(body);
                if ("200".equals(status)){
                    JSONArray jsonArray =OkhttpUtils.getData(body);
                    goodsList = JSON.parseObject(jsonArray.toJSONString(),new TypeReference<List<Goods>>(){});
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myadapter=new AdminBlgAdapter(context,goodsList);
                            listView.setAdapter(myadapter);
                            setListViewHeightBasedOnChildren(listView);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Goods goods=(Goods) adapterView.getItemAtPosition(i);
                                    Intent intent=new Intent(context, AlterGoodsBlgActivity.class);
                                    intent.putExtra("gid",goods.getGid());
                                    startActivity(intent);
                                }
                            });
                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Goods goods=(Goods) adapterView.getItemAtPosition(i);
                                    gid=goods.getGid();
                                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                                    builder.setMessage("确定删除?");
                                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (true){
                                                Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                                                GoodsDao goodsDao=new GoodsDao();
                                                goodsList=goodsDao.findall(context);
                                                myadapter=new AdminBlgAdapter(context,goodsList);
                                                listView.setAdapter(myadapter);
                                                setListViewHeightBasedOnChildren(listView);
                                            }else {
                                                Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("取消",null);
                                    builder.create().show();

                                    return true;
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}

package com.example.xiaomaibu.ui.home;

import android.content.Context;
import android.content.Intent;
import android.database.CursorWindow;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.xiaomaibu.Adapter.Myadapter;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.Dao.GoodsDao;
import com.example.xiaomaibu.Dao.TemploginDao;
import com.example.xiaomaibu.Login.AdminLoginActivity;
import com.example.xiaomaibu.Login.LoginActivity;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.Regiter.RegiterActivity;
import com.example.xiaomaibu.SearchActivity;
import com.example.xiaomaibu.Util.OkhttpUtils;
import com.example.xiaomaibu.goods.GoodsInfoActivity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private SearchView searchView;
    private GridView gridView;
    private ListView listView;
    private String  eamil;
    private List<Goods> goodsList;
    private Myadapter myadapter;
    private String[] names = new String[]{"电子产品", "生鲜", "零食", "乳制品", "水果"};
    private int[] irons = new int[]{R.drawable.main_computer, R.drawable.main_fish
            , R.drawable.main_milk, R.drawable.main_icec, R.drawable.main_paiapple};
    private List<Map<String, Object>> datas = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        View root = inflater.inflate(R.layout.fragment_home, container, false);
        gridView=root.findViewById(R.id.gridView);
        listView = root.findViewById(R.id.listView);
        searchView=root.findViewById(R.id.tvsearch);
        setHasOptionsMenu(true);
        init();
        SimpleAdapter adapter = new SimpleAdapter(getContext(), datas, R.layout.main_gridview,
                new String[]{"name", "iron"}, new int[]{R.id.main_iron_name, R.id.main_iron});
        gridView.setAdapter(adapter);


        //初始化数据
        try {
            getGoodsList();//初始化goodlist
        } catch (IOException e) {
            e.printStackTrace();
        }



        //网格框被点击触发事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(getContext(),SearchActivity.class);
                intent.putExtra("sort",i+"");
                startActivity(intent);
            }
        });
        //列表子项被点击触发事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Goods goods=(Goods) adapterView.getItemAtPosition(i);
                Intent intent=new Intent(getContext(), GoodsInfoActivity.class);
                intent.putExtra("gid",goods.getGid());
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent =new Intent(getContext(), SearchActivity.class);
                intent.putExtra("like",query);
                getContext().startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });



        return root;
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
            totalHeight += listItem.getMeasuredHeight() + 100;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    private void init() {
        datas.clear();
        for (int i = 0; i < names.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", names[i]);
            item.put("iron", irons[i]);
            datas.add(item);
        }

    }




    //菜单
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if(eamil==null){
            menu.add(Menu.NONE, Menu.FIRST + 1, 1, "登录").setIcon(android.R.drawable.ic_menu_delete);
            menu.add(Menu.NONE, Menu.FIRST + 2, 2, "注册").setIcon(android.R.drawable.ic_menu_delete);
            menu.add(Menu.NONE, Menu.FIRST + 10, 2, "商家登录").setIcon(android.R.drawable.btn_dialog);
            menu.add(Menu.NONE, Menu.FIRST + 11, 1, "刷新").setIcon(android.R.drawable.btn_default);
        }else{
            menu.add(Menu.NONE, Menu.FIRST + 3, 1, eamil).setIcon(android.R.drawable.btn_default);
            menu.add(Menu.NONE, Menu.FIRST + 30, 1, "注销").setIcon(android.R.drawable.btn_default);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST + 11:
                GoodsDao goodsDao=new GoodsDao();
                goodsList=goodsDao.findall(getContext());
                myadapter=new Myadapter(getContext(),goodsList);
                listView.setAdapter(myadapter);
                setListViewHeightBasedOnChildren(listView);
                break;
            case Menu.FIRST + 10:
                Intent adminLogin=new Intent(getContext(), AdminLoginActivity.class);
                startActivity(adminLogin);
                break;
            case Menu.FIRST + 1:
                Intent intent=new Intent(getContext(), LoginActivity.class);
                startActivityForResult(intent,0);
                break;

            case Menu.FIRST + 2:
                Intent regiter=new Intent(getContext(), RegiterActivity.class);
                startActivity(regiter);
                break;
            case Menu.FIRST + 30:
                eamil=null;
                TemploginDao temploginDao=new TemploginDao();
                temploginDao.delete(getContext());
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //activity回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(resultCode==0x11){
                eamil=data.getStringExtra("email");
                Toast.makeText(getContext(),"登录成功",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *
     * @throws IOException
     * //进入页面时拉去所有商品信息返回
     */
    private void getGoodsList() throws IOException {

        OkhttpUtils.httpGet(OkhttpUtils.IP + "/goods/findall", getContext(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
              /*  try {
                    getGoodsList();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"连接超时",Toast.LENGTH_SHORT).show();
                    }
                });
*/
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()){
                    String body =response.body().string();
                    JSONObject jsonObject=JSONObject.parseObject(body);
                    String status =  jsonObject.get("code").toString();
                    if (status.equals("200")){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        goodsList= JSON.parseObject(jsonArray.toJSONString()
                                ,new TypeReference<List<Goods>>(){});
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Myadapter myadapter=new Myadapter(getContext(),goodsList);
                                listView.setAdapter(myadapter);
                                setListViewHeightBasedOnChildren(listView);
                            }
                        });
                    }
                }
            }
        });

    }
}
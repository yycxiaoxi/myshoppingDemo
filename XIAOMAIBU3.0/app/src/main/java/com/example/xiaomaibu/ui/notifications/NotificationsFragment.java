package com.example.xiaomaibu.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.xiaomaibu.Bean.Refund;
import com.example.xiaomaibu.Dao.TemploginDao;
import com.example.xiaomaibu.Login.LoginActivity;
import com.example.xiaomaibu.PingJiaActivity;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.RefundActivity;
import com.example.xiaomaibu.daifahuoActivity;
import com.example.xiaomaibu.goods.GoodsInfoActivity;
import com.example.xiaomaibu.nopayActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsFragment extends Fragment {
    private List<Map<String, Object>> datas = new ArrayList<>();
    private String[] names = new String[]{"待付款", "待发货", "待收货", "待评价", "退款/售后"};
    private int[] irons = new int[]{R.drawable.main_computer, R.drawable.main_fish
            , R.drawable.main_milk, R.drawable.main_icec, R.drawable.main_paiapple};
    private GridView gridView;
    private Button login;
    private Button nologin;
    private String uid;
    private TemploginDao temploginDao;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        initDta();
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        SimpleAdapter simpleAdapter=new SimpleAdapter(getContext(),datas,R.layout.main_gridview,new String[]{
                "name","iron"},new int[]{R.id.main_iron_name,R.id.main_iron});
        login=root.findViewById(R.id.login);
        nologin=root.findViewById(R.id.nologin);
        gridView=root.findViewById(R.id.gridView);
        temploginDao=new TemploginDao();
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0: Intent intent=new Intent(getActivity(),nopayActivity.class);
                        intent.putExtra("status","0");
                        startActivity(intent);
                        break;
                    case 1: Intent intent1=new Intent(getActivity(), daifahuoActivity.class);
                        intent1.putExtra("status","1");
                        startActivity(intent1);

                        break;
                    case 2: Intent intent2=new Intent(getActivity(), daifahuoActivity.class);
                        intent2.putExtra("status","2");
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3=new Intent(getActivity(), PingJiaActivity.class);
                        intent3.putExtra("status","4");
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4=new Intent(getActivity(), RefundActivity.class);
                        intent4.putExtra("status","5");
                        startActivity(intent4);
                        break;
                }

            }
        });

        final TemploginDao temploginDao=new TemploginDao();
        uid=temploginDao.find(getContext());
        if(uid==null){
            login.setVisibility(View.VISIBLE);
            nologin.setVisibility(View.GONE);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    login.setVisibility(View.GONE);
                    nologin.setVisibility(View.VISIBLE);
                }
            });
        }else {
            login.setVisibility(View.GONE);
            nologin.setVisibility(View.VISIBLE);
            nologin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    temploginDao.delete(getContext());
                    login.setVisibility(View.VISIBLE);
                    nologin.setVisibility(View.GONE);
                }
            });
        }


        return root;
    }
    private void initDta(){
        for (int i=0;i<names.length;i++){
            Map<String,Object> map=new HashMap<>();
            map.put("name",names[i]);
            map.put("iron",irons[i]);
            datas.add(map);
        }
    }
}
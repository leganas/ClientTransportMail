package com.leganas.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.maps.SupportMapFragment;
import com.leganas.engine.Assets;
import com.leganas.engine.Setting;
import com.leganas.engine.controller.ClientController;
import com.leganas.engine.network.packeges.clientTOserver.ClientMessage;
import com.leganas.mobile.clienttransport.R;

/**
 * Created by AndreyLS on 26.01.2017.
 */

public class MainActivity extends AppCompatActivity{

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_drawer);

        Setting.IPAdressFromClient = "192.168.0.102";
        Assets.clientController = new ClientController("MobileClient");

        ClientMessage.RequestAutorization msg = new ClientMessage.RequestAutorization();
        msg.login = "MobileUser";
        Assets.clientController.addClientMessageToQuery(msg);

        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        FragmentManager fm = getSupportFragmentManager();
        // Ищем фрагмент (это если после поворота то он будет уже)
        Fragment fragment = fm.findFragmentById(R.id.content_frame);
        // Если не нашли то создаём новый
        if (fragment == null) {
            fragment = new NewsFragment();
            fm.beginTransaction()
                    .add(R.id.content_frame,fragment)
                    .commit();
        }


        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener() );


    }

    //  Слушатель для элементов списка в выдвижной панели
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    View mapView;

    /** Смена фрагментов в основном окне программы */
    private void selectItem(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position){
            case 0:
                Fragment fragmentNews = new NewsFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentNews).commit();
                break;
            case 1:
                SupportMapFragment fragment = new MapFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
        }
        // обновим выбранный элемент списка и закрываем панель
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
//        mTitle = title;
//        getActionBar().setTitle(mTitle);
    }
}

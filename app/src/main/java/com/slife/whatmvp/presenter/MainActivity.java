package com.slife.whatmvp.presenter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.slife.whatmvp.model.MainBiz;
import com.slife.whatmvp.R;
import com.slife.whatmvp.view.DataListView;

import org.loader.presenter.ActivityPresenterImpl;

import java.util.ArrayList;

public class MainActivity extends ActivityPresenterImpl<DataListView> implements
        AdapterView.OnItemClickListener,View.OnClickListener{

    @Override
    public void create(Bundle savedInstance) {

    }

    @Override
    public void created(Bundle savedInstance) {
    
    }
    

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.newdata) {
            newData();
        }else{
            addData();
        }
        Log.i("mainactivity","test");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mView.toast(position);
    }

    private void newData(){
        new MainBiz().data(new MainBiz.Callback<ArrayList<String>>() {
            @Override
            public void callback(ArrayList<String> data) {
                mView.setData(data);
            }
        });
    }

    private void addData(){
        new MainBiz().data(new MainBiz.Callback<ArrayList<String>>() {
            @Override
            public void callback(ArrayList<String> data) {
                mView.addData(data);
            }
        });
    }
}

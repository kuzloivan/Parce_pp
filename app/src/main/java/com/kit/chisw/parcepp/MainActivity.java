package com.kit.chisw.parcepp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class MainActivity extends ActionBarActivity {
    private static String APLICATION_ID = "MxyOrNfeQAbrt9r4BU9qmf2SiVOdVOJM4EadMhu6";
    private static String CLIENT_KEY = "s32kmritI8S19JZRteiXzw5i9uZI5Z8FXKFBvq5S";

    private static String CLASS_NAME="device_info";
    private static String FIELD_MAC = "device_mac";
    private static String FIELD_NAME = "device_name";
    private static String FIELD_EXTRA = "extra_params";


    List<Model> mModels = new ArrayList<>();
    List<String> mStrings = new ArrayList<>();
    HashMap<String,List<Model>> mListHashMap = new HashMap<>();
    Set<String> mMap = new HashSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, APLICATION_ID, CLIENT_KEY);


        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(CLASS_NAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> pList, ParseException e) {
                Gson gson = new Gson();
                for(ParseObject p:pList ){
                    if(p.getString(FIELD_EXTRA)==null){
                        continue;
                    }
                    try {
                        JSONArray jsonArray = new JSONArray(p.getString(FIELD_EXTRA));
                        for(int i = 0; i<jsonArray.length();i++){
                            mModels.add(gson.fromJson(jsonArray.get(i).toString(),Model.class));
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }


                }
                Log.d("PIZDA","pizda skolko modeley = " + mModels.size());
                Iterator<Model> iterator = mModels.iterator();
                while (iterator.hasNext()){
                    Model next = iterator.next();
                    if(next.param.equals("CONNECT")|next.param.equals("DISCONNECT")){
                        iterator.remove();
                        Log.d("", "remove model");
                    }
                }
                for(Model m:mModels){
                    if(mListHashMap.containsKey(m.address)){
                        mListHashMap.get(m.address).add(m);
                    }else {
                        mListHashMap.put(m.address, new ArrayList<Model>());
                        mListHashMap.get(m.address).add(m);
                    }
                }
                for(Model m:mModels){
                    mMap.add(m.name);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = "";
                        for(String ss: mMap){
                            text+=ss + "\n";
                        }
                        text+="TOTAL " + mMap.size();
                                ((TextView) findViewById(R.id.text)).setText(text);
                    }
                });



            }
        });
    }

    private class Model {
        String type;
        String param;
        String address;
        String name;
       //JSONArray extra;
    }

}

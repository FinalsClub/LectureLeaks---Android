package org.ale.lectureleaks;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LearnSubjectListViewActivity extends Activity{
    
    LearnAPIArrayListAdapter adapter;
    ListView lv;
    GetDataTask getDataTask;
    ArrayList<ArrayList<HashMap<String, String>>> mal;
    Context c;
    String u;
    ImageView headerButton;
    Animation rotate;
    
    View lastTouched;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detailed_menu_list);
        
        final String school = getIntent().getExtras().getString("School");
        final String subject = getIntent().getExtras().getString("Subject");
        
        TextView title = (TextView)findViewById(R.id.title_bar);
        title.setText(subject + " - " + school);
        
        mal = new ArrayList<ArrayList<HashMap<String,String>>>();
        HashMap<String, String> hm = new HashMap<String, String>();
        ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>();
        
        hm.put("name", "Loading..");
        hm.put("title", "Loading..");
        al.add(hm);
        mal.add(al);

        //Setup the adapter views;
        adapter = new LearnAPIArrayListAdapter(this, R.layout.list_view_row);
        adapter.setItems(mal);
        adapter.setContext(getBaseContext());
        adapter.setParent(this);
        lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter);
        
        
        final LearnSubjectListViewActivity c = LearnSubjectListViewActivity.this;
        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ArrayList<HashMap<String, String>> in = mal.get(arg2);
                HashMap<String, String> shmap = in.get(0);
 
                if(shmap.containsKey("head") && shmap.get("head").equals("true")|| shmap.get("name").equals("Loading..")) {
                    //fuck
                }
                else {
                    arg1.setBackgroundResource(R.layout.header_bar_gradient);
                    Intent i = new Intent(c, LearnCourseListViewActivity.class);
                    i.putExtra("School", school);
                    i.putExtra("Subject", subject);
                    i.putExtra("Course", shmap.get("name"));
                    startActivity(i);
                    lastTouched = arg1;
                }
                
            }});
        
        //Create an AsyncTask to update that data;
        getDataTask = new GetDataTask();
        getDataTask.setContext(this);
        getDataTask.setAdapter(adapter);
        getDataTask.setParser(new LectureLeaksJSONParser("IND_SCHOOL"));
        u = "http://lectureleaks.org/api4/school/" + school + "/subject/" + subject + "/" ;
        getDataTask.execute(u, school);
        
    }
    
    public void onResume() {
        super.onResume();
        ///XXX HACK CHANGE THIS OMG
        if(lastTouched != null) {
            lastTouched.setBackgroundColor(Color.WHITE);
        }
    }

}

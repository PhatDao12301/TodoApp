package com.example.phat_dep_trai.todoapp;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, OnClickListener, AdapterView.OnItemLongClickListener {

    public Button btOK;
    public EditText editText;
    public ListView list;
    public ArrayList<String> st;
    public ArrayAdapter<String> adapter;
    public static SQLiteDatabase database = null;
    private String tblName = "tblJob";
    private String databaseName = "DataBase.db";
    private String jobName = "JobName";
    private String title = "Title";
    private String content = "Content";

    public String resultTitle;
    public String resultContent;
    private boolean state = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doCreateDb();
        doCreateTable();

        btOK = (Button) findViewById(R.id.bttOK);
        editText = (EditText) findViewById(R.id.editText);

        list = (ListView) findViewById(R.id.list);

        st = new ArrayList<String>();
        loadAllTitle();

        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, st);

        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(this);
        btOK.setOnClickListener(this);

    }

    public void doCreateDb()
    {
        database = openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
    }


        @Override
    public void onClick(View v) {

            doInsertRecord();
            adapter.notifyDataSetChanged();
            editText.setText("");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK && requestCode == 1){

            if (data.hasExtra(title))
                resultTitle = data.getExtras().getString(title);

            if (data.hasExtra(content))
                resultContent = data.getExtras().getString(content);
        }
        doUpdateDb();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (state == true) {
            Intent intent = new Intent();
            ComponentName cn;
            cn = new ComponentName(this, EditItemActivity.class);

            intent.putExtra(title, st.get(position));

            Cursor crs = database.rawQuery("SELECT * FROM " + tblName + " WHERE Title = \"" + st.get(position) + "\"", null);
            crs.moveToFirst();
            intent.putExtra(content, crs.getString(1));

            setResult(RESULT_OK, intent);

            intent.setComponent(cn);
            startActivityForResult(intent, 1);
        }
        else state = true;
    }

    public void doCreateTable()
    {
        String sql="CREATE TABLE IF NOT EXISTS " + tblName + "(";
        sql+=" Title TEXT primary key, ";
        sql += "Content TEXT);";
        database.execSQL(sql);
    }

    public void doInsertRecord()
    {
        ContentValues values = new ContentValues();
        values.put(title, editText.getText().toString());
        //values.put(content, "Edit here...");

        String msg="";
        if(database.insert(tblName, null, values)==-1){
            msg="Failed to insert record";
        }
        else{
            msg="insert record is successful";
            adapter.add(editText.getText().toString());
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void loadAllTitle()
    {
        Cursor c = database.query(tblName, null, null, null, null, null, null);

        c.moveToFirst();
        String data="";

        while(c.isAfterLast()==false)
        {
            data = c.getString(0);
            st.add(data);
            c.moveToNext();
        }
        //Toast.makeText(this, data, Toast.LENGTH_LONG).show();
        c.close();
    }

    public void doDeleteRecord(String record)
    {
        String msg = "";
        if(database.delete(tblName , " Title = \"" + record + "\"", null ) == 0)
        {
            msg="Delete is failed";
        }
        else
        {
            msg="Delete is successful";
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        String string = st.get(position);

        doDeleteRecord(string);

        st.remove(position);

        adapter.notifyDataSetChanged();

        state = false;
        if (parent.getLastVisiblePosition() == position)
            state = true;
        return state;
    }

    public void doUpdateDb()
    {
        ContentValues values = new ContentValues();
        values.put(content, resultContent);

        String[] arg = new String[] {resultTitle};
        String msg="";

        if (database.update(tblName, values, "Title = ?", arg) == 0){
            msg="Failed to update";
        }
        else{
            msg= "Updated";
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }

}

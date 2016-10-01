package com.example.phat_dep_trai.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Phat_Dep_Trai on 22-Sep-16.
 */
public class EditItemActivity extends AppCompatActivity implements View.OnClickListener{


    public EditText editText;
    public TextView textView;

    public String sJob;
    public Bundle extra;
    private String title = "Title";
    private String content = "Content";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Button btReturn = (Button) findViewById(R.id.bttReturn1);

        btReturn.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.editText1);

        textView = (TextView) findViewById(R.id.textView);
        extra = getIntent().getExtras();
        if (extra == null)
            return;

        sJob = extra.getString(title);

        textView.setText("Title: " + sJob);

        String resultContent = extra.getString(content);
        if (resultContent != null)
            editText.setText(resultContent);

       // setText();

    }

    @Override
    public void onClick(View v) {

        Intent data = new Intent();
        data.putExtra(content, editText.getText().toString());
        data.putExtra(title, sJob);
        setResult(RESULT_OK , data);

        finish();
    }


    public void setText()
    {
        sJob = extra.getString(title);

        textView.setText("Title: " + sJob);

        String resultContent = extra.getString(content);
        if (resultContent != null)
            editText.setText(resultContent);
    }

}

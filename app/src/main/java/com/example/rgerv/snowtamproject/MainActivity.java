package com.example.rgerv.snowtamproject;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton validate;
    EditText searchCode;
    Toast infos ;
    Context context;
    CharSequence msg;
    int duration;
    ImageButton airplane_delete;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        duration = Toast.LENGTH_LONG ;
        msg = getString(R.string.code_missing);

        searchCode = (EditText)findViewById(R.id.search_code);
        validate = (ImageButton)findViewById(R.id.validate);
        layout = (LinearLayout)findViewById(R.id.layout2);

        validate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!searchCode.getText().toString().equals("")) {
                            //we have to check the airplane's existence
                            layout.addView(createNewTextView(searchCode.getText().toString()));

                        }else{
                            infos = Toast.makeText(context, msg, duration);
                            infos.show();
                        }
                    }
                }
        );

    }

    private TextView createNewTextView(String s) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView airplane_code = new TextView(this);
        airplane_code.setLayoutParams(lparams);
        airplane_code.setText(s);
        return airplane_code;
    }


}

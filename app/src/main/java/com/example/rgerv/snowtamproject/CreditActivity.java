package com.example.rgerv.snowtamproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CreditActivity extends AppCompatActivity {

    private TextView infoscredit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        infoscredit = findViewById(R.id.infoscredit);
        infoscredit.setText(R.string.infoscredit);
        setTitle(R.string.credit);
    }
}

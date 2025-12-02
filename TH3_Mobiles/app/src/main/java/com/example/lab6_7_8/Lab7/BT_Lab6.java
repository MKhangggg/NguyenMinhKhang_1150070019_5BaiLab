package com.example.lab6_7_8.Lab7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import com.example.lab6_7_8.R;

public class BT_Lab6 extends AppCompatActivity {

    Spinner spFrom, spTo;
    EditText edtValue;
    TextView tvResult;
    Button btnConvert, btnPrePage;
    String[] names;
    String[] units;
    double[][] lengthRate = {
            {1,      0.1,     0.01,     0.001,     0.000001},
            {10,     1,       0.1,      0.01,      0.00001},
            {100,    10,      1,        0.1,       0.0001},
            {1000,   100,     10,       1,         0.001},
            {1000000,100000,  10000,    1000,      1}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_lab6);

        spFrom = findViewById(R.id.spFrom);
        spTo = findViewById(R.id.spTo);
        edtValue = findViewById(R.id.edtValue);
        tvResult = findViewById(R.id.tvResult);
        btnConvert = findViewById(R.id.btnConvert);
        Button btnPrePage = findViewById(R.id.btnPrePage);

        initData();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spFrom.setAdapter(adapter);
        spTo.setAdapter(adapter);

        btnConvert.setOnClickListener(v -> convertLength());
        btnPrePage.setOnClickListener(v -> {
            Intent intent = new Intent(BT_Lab6.this, Lab6Activity.class);
            startActivity(intent);
        });
    }

    private void initData() {
        names = new String[]{
                getString(R.string.mm_name),
                getString(R.string.cm_name),
                getString(R.string.dm_name),
                getString(R.string.m_name),
                getString(R.string.km_name)
        };

        units = new String[]{
                getString(R.string.mm_unit),
                getString(R.string.cm_unit),
                getString(R.string.dm_unit),
                getString(R.string.m_unit),
                getString(R.string.km_unit)
        };
    }

    private void convertLength() {

        String input = edtValue.getText().toString().trim();
        if (input.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        double value;
        try {
            value = Double.parseDouble(input);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.error_invalid), Toast.LENGTH_SHORT).show();
            return;
        }

        int from = spFrom.getSelectedItemPosition();
        int to = spTo.getSelectedItemPosition();

        double rate = lengthRate[from][to];
        double result = value * rate;

        String text = value + " " + units[from] +
                " = " + result + " " + units[to];

        tvResult.setText(text);
    }
}

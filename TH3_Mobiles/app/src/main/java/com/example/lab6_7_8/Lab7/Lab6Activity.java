package com.example.lab6_7_8.Lab7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab6_7_8.R;

public class Lab6Activity extends AppCompatActivity {

    private Spinner spFrom, spTo;
    private EditText edtAmount;
    private TextView tvResult, tvAuthor, tvComputer;
    private Button btnConvert, btnNextPage;
    private String[] currencyNames;
    private String[] currencyUnits;
    private double[][] rateMatrix = {
            { 1,  0.92,  0.79,  1.50,  1.35, 18.50,  1.66,  83.00, 150.00, 25000.0 },
            { 1.09, 1,   0.86,  1.63,  1.47, 20.10,  1.80,  90.00, 163.00, 27000.0 },
            { 1.26, 1.16,  1,   1.90,  1.71, 23.40,  2.10, 104.00, 188.00, 31000.0 },
            { 0.67, 0.61,  0.53,  1,   0.90, 12.30,  1.10,  55.00, 100.00, 16500.0 },
            { 0.74, 0.68,  0.59,  1.11,  1,  13.70,  1.23,  61.00, 110.00, 18000.0 },
            { 0.054,0.050, 0.043, 0.081, 0.073, 1,   0.090,  4.40,   8.00, 1300.0 },
            { 0.60, 0.56,  0.48,  0.91,  0.81, 11.10,  1,   49.00,  88.00, 15000.0 },
            { 0.012,0.011, 0.0096,0.018, 0.016,0.23,  0.020,  1,    1.80,  310.0 },
            { 0.0067,0.0061,0.0053,0.010,0.0091,0.13,0.011,  0.56,   1,   170.0 },
            { 0.000040,0.000037,0.000032,0.000061,0.000056,0.00077,0.000067,0.0032,0.0059,1 }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab6);

        spFrom = findViewById(R.id.spFrom);
        spTo = findViewById(R.id.spTo);
        edtAmount = findViewById(R.id.edtAmount);
        tvResult = findViewById(R.id.tvResult);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvComputer = findViewById(R.id.tvComputer);
        btnConvert = findViewById(R.id.btnConvert);
        Button btnNextPage = findViewById(R.id.btnNextPage);

        initCurrencyData();
        tvAuthor.setText(getString(R.string.author));
        tvComputer.setText(getString(R.string.computer));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                currencyNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFrom.setAdapter(adapter);
        spTo.setAdapter(adapter);

        spFrom.setSelection(0);
        spTo.setSelection(9);
        btnConvert.setOnClickListener(v -> convertMoney());
        btnNextPage.setOnClickListener(v -> {
            Intent intent = new Intent(Lab6Activity.this, BT_Lab6.class);
            startActivity(intent);
        });
    }

    private void initCurrencyData() {
        currencyNames = new String[]{
                getString(R.string.usd_name),
                getString(R.string.eur_name),
                getString(R.string.gbp_name),
                getString(R.string.aud_name),
                getString(R.string.cad_name),
                getString(R.string.zar_name),
                getString(R.string.nzd_name),
                getString(R.string.inr_name),
                getString(R.string.jpy_name),
                getString(R.string.vnd_name)
        };

        currencyUnits = new String[]{
                getString(R.string.usd_unit),
                getString(R.string.eur_unit),
                getString(R.string.gbp_unit),
                getString(R.string.aud_unit),
                getString(R.string.cad_unit),
                getString(R.string.zar_unit),
                getString(R.string.nzd_unit),
                getString(R.string.inr_unit),
                getString(R.string.jpy_unit),
                getString(R.string.vnd_unit)
        };
    }

    private void convertMoney() {
        String amountStr = edtAmount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_amount), Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.error_invalid_amount), Toast.LENGTH_SHORT).show();
            return;
        }

        int fromIndex = spFrom.getSelectedItemPosition();
        int toIndex = spTo.getSelectedItemPosition();

        double rate = rateMatrix[fromIndex][toIndex];
        double result = amount * rate;

        String unitFrom = currencyUnits[fromIndex];
        String unitTo = currencyUnits[toIndex];

        String resultText = String.format("%.2f %s = %.2f %s", amount, unitFrom, result, unitTo);
        tvResult.setText(resultText);
    }
}

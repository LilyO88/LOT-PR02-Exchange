package com.example.lily.lot_pr02_exchange;

import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rgFromCurrency;
    private RadioGroup rgToCurrency;
    private RadioButton rbFromDollar;
    private RadioButton rbFromEuro;
    private RadioButton rbFromPound;
    private RadioButton rbToDollar;
    private RadioButton rbToEuro;
    private RadioButton rbToPound;
    private ImageView imgFrom;
    private ImageView imgTo;
    private Button btnExchange;
    private final Double DOLAREUROFACTOR = 0.86;
    private final Double DOLARPOUNDFACTOR = 0.77;
    private final Double EUROPOUNDFACTOR = 0.89;
    private EditText txtAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        rgFromCurrency = findViewById(R.id.rgFromCurrency);
        rgToCurrency = findViewById(R.id.rgToCurrency);
        rbFromDollar = findViewById(R.id.rbFromDollar);
        rbFromEuro = findViewById(R.id.rbFromEuro);
        rbFromPound = findViewById(R.id.rbFromPound);
        rbToDollar = findViewById(R.id.rbToDollar);
        rbToEuro = findViewById(R.id.rbToEuro);
        rbToPound = findViewById(R.id.rbToPound);
        imgFrom = findViewById(R.id.imgFrom);
        imgTo = findViewById(R.id.imgTo);
        btnExchange = findViewById(R.id.btnExchange);
        txtAmount = findViewById(R.id.txtAmount);

        btnExchange.setOnClickListener(v -> exchange(v));
        txtAmount.setOnClickListener(v -> exchange(v));
        rgFromCurrency.setOnCheckedChangeListener(this);
        rgToCurrency.setOnCheckedChangeListener(this);
    }

    private void exchange(View view) {
        if (view.getId() == R.id.btnExchange
                && !txtAmount.getText().toString().isEmpty()
                && !txtAmount.getText().toString().equals(".")) {
            if (Double.parseDouble(txtAmount.getText().toString()) != 0) {
                calculateChange();
            }
            reset();
        } else if (view.getId() == R.id.txtAmount) {
            txtAmount.selectAll();
        } else if(txtAmount.getText().toString().equals(".")) {
            reset();
        }
    }

    private void calculateChange() {
        int fromChecked = rgFromCurrency.getCheckedRadioButtonId();
        int toChecked = rgToCurrency.getCheckedRadioButtonId();
        double change = 0,
                amount = Double.parseDouble(txtAmount.getText().toString());
        char dollar = '\u0024', euro = '\u20AC', pound = '\u00A3',
                currencyFrom = ' ', currencyTo = ' ';
        switch (fromChecked) {
            case R.id.rbFromDollar:
                currencyFrom = dollar;
                switch (toChecked) {
                    case R.id.rbToDollar:
                        change = amount;
                        currencyTo = dollar;
                        break;
                    case R.id.rbToEuro:
                        change =  amount*DOLAREUROFACTOR;
                        currencyTo = euro;
                        break;
                    case R.id.rbToPound:
                        change = amount*DOLARPOUNDFACTOR;
                        currencyTo = pound;
                        break;
                }
                break;
            case R.id.rbFromEuro:
                currencyFrom = euro;
                switch (toChecked) {
                    case R.id.rbToDollar:
                        change =  amount/DOLAREUROFACTOR;
                        currencyTo = dollar;
                        break;
                    case R.id.rbToEuro:
                        change = amount;
                        currencyTo = euro;
                        break;
                    case R.id.rbToPound:
                        change = amount*EUROPOUNDFACTOR;
                        currencyTo = pound;
                        break;
                }
                break;
            case R.id.rbFromPound:
                currencyFrom = pound;
                switch (toChecked) {
                    case R.id.rbToDollar:
                        change = amount/DOLARPOUNDFACTOR;
                        currencyTo = dollar;
                        break;
                    case R.id.rbToEuro:
                        change = amount/EUROPOUNDFACTOR;
                        currencyTo = euro;
                        break;
                    case R.id.rbToPound:
                        change = amount;
                        currencyTo = pound;
                        break;
                }
                break;
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        Toast.makeText(this, df.format(amount) + " " + currencyFrom + " = "
                + df.format(change) + " " + currencyTo, Toast.LENGTH_LONG).show();

    }

    private void reset() {
        txtAmount.setText("0.00");
        rbFromDollar.setChecked(true);
        rbFromEuro.setEnabled(false);
        rbFromPound.setEnabled(true);
        rbToEuro.setChecked(true);
        rbToDollar.setEnabled(false);
        rbToPound.setEnabled(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        imageEnable(group, checkedId);
    }

    private void imageEnable(RadioGroup group, int checkedId) {
        if(group.getId() == rgFromCurrency.getId()){
            switch (checkedId) {
                case R.id.rbFromDollar:
                    rbToDollar.setEnabled(false);
                    rbToEuro.setEnabled(true);
                    rbToPound.setEnabled(true);
                    imgFrom.setImageResource(R.drawable.ic_dollar);
                    break;
                case R.id.rbFromEuro:
                    rbToDollar.setEnabled(true);
                    rbToEuro.setEnabled(false);
                    rbToPound.setEnabled(true);
                    imgFrom.setImageResource(R.drawable.ic_euro);
                    break;
                case R.id.rbFromPound:
                    rbToDollar.setEnabled(true);
                    rbToEuro.setEnabled(true);
                    rbToPound.setEnabled(false);
                    imgFrom.setImageResource(R.drawable.ic_pound);
                    break;
            }
        } else {
            switch (checkedId) {
                case R.id.rbToDollar:
                    rbFromDollar.setEnabled(false);
                    rbFromEuro.setEnabled(true);
                    rbFromPound.setEnabled(true);
                    imgTo.setImageResource(R.drawable.ic_dollar);
                    break;
                case R.id.rbToEuro:
                    rbFromDollar.setEnabled(true);
                    rbFromEuro.setEnabled(false);
                    rbFromPound.setEnabled(true);
                    imgTo.setImageResource(R.drawable.ic_euro);
                    break;
                case R.id.rbToPound:
                    rbFromDollar.setEnabled(true);
                    rbFromEuro.setEnabled(true);
                    rbFromPound.setEnabled(false);
                    imgTo.setImageResource(R.drawable.ic_pound);
                    break;
            }
        }
    }
}

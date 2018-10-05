package com.example.lily.lot_pr02_exchange;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DecimalFormat;

import androidx.appcompat.app.AppCompatActivity;

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
    // SI SÓLO VA A SER USADO EN UN MÉTODO DEFINELO COMO VARIABLE LOCAL AL MÉTODO.
    private Button btnExchange;
    // CAMBIO ESTE FACTOR DE CAMBIO PARA QUE PASE TEST
    private final Double DOLAREUROFACTOR = 0.8547;
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
        // Y AQUÍ POR QUÉ NO USAS LAMBDAS?
        rgFromCurrency.setOnCheckedChangeListener(this);
        rgToCurrency.setOnCheckedChangeListener(this);
        // AGREGO ESTAS LÍNEAS PARA QUE PASE ALGUNOS TESTS. FÍJATE
        imgFrom.setTag(R.drawable.ic_euro);
        imgTo.setTag(R.drawable.ic_dollar);
        // OJO, NO GESTIONAS BIEN EL ESTADO INICIAL DE DESHABILITACIÓN DE DETERMINADOS RadioButton.
        // POR EJEMPLO, rbToEuro DEBERÍA ESTAR DESHABILITADO INICIALMENTE Y rbFromDollar TAMBIÉN.
    }

    private void exchange(View view) {
        // USA TextUtils.isEmpty() Y TextUtis.equals() PORQUE CONTROLA AUTOMÁTICAMENTE QUE NO SEA
        // NULL.
        // TODA ESTA COMPROBACIÓN SE PUEDE EVITAR HACIENDO UN try catch DE LA LLAMADA A Double
        // .parseDouble()
        if (view.getId() == R.id.btnExchange
                && !txtAmount.getText().toString().isEmpty()
                && !txtAmount.getText().toString().equals(".")) {
            if (Double.parseDouble(txtAmount.getText().toString()) != 0) {
                calculateChange();
            }
            // Y SI EL USUARIO QUIERE MIRAR EL CAMBIO DE OTRA CANTIDAD, TIENE QUE VOLVER A
            // SELECCIONAR LA MONEDA DE ORIGEN Y LA DE DESTINO?
            reset();
        } else if (view.getId() == R.id.txtAmount) {
            txtAmount.selectAll();
        } else if(txtAmount.getText().toString().equals(".")) {
            // txtAmount DEBERÍA TOMAR 0.00 TAMBIÉN CUANDO LA CADENA ESTÉ VACÍA. EN REALIDAD
            // EN CUALQUIER CASO EN EL QUE NO SEA VÁLIDO LO INTRODUCIDO POR EL USUARIO.
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
                        // USA Code -> Reformat code PARA QUE EL CÓDIGO TE QUEDE BONITO
                        // AUTOMÁTICAMENTE
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
        // USA MEJOR RECURSOS DE CADENA CON PARÁMETROS.
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
                    // AGREGO ESTA LÍNEA PARA QUE PASE ALGUNOS TESTS. FÍJATE BIEN
                    imgFrom.setTag(R.drawable.ic_dollar);
                    break;
                case R.id.rbFromEuro:
                    rbToDollar.setEnabled(true);
                    rbToEuro.setEnabled(false);
                    rbToPound.setEnabled(true);
                    imgFrom.setImageResource(R.drawable.ic_euro);
                    // AGREGO ESTA LÍNEA PARA QUE PASE ALGUNOS TESTS. FÍJATE BIEN
                    imgFrom.setTag(R.drawable.ic_euro);
                    break;
                case R.id.rbFromPound:
                    rbToDollar.setEnabled(true);
                    rbToEuro.setEnabled(true);
                    rbToPound.setEnabled(false);
                    imgFrom.setImageResource(R.drawable.ic_pound);
                    // AGREGO ESTA LÍNEA PARA QUE PASE ALGUNOS TESTS. FÍJATE BIEN
                    imgFrom.setTag(R.drawable.ic_pound);
                    break;
            }
        } else {
            switch (checkedId) {
                case R.id.rbToDollar:
                    rbFromDollar.setEnabled(false);
                    rbFromEuro.setEnabled(true);
                    rbFromPound.setEnabled(true);
                    imgTo.setImageResource(R.drawable.ic_dollar);
                    // AGREGO ESTA LÍNEA PARA QUE PASE ALGUNOS TESTS. FÍJATE BIEN
                    imgTo.setTag(R.drawable.ic_dollar);
                    break;
                case R.id.rbToEuro:
                    rbFromDollar.setEnabled(true);
                    rbFromEuro.setEnabled(false);
                    rbFromPound.setEnabled(true);
                    imgTo.setImageResource(R.drawable.ic_euro);
                    // AGREGO ESTA LÍNEA PARA QUE PASE ALGUNOS TESTS. FÍJATE BIEN
                    imgTo.setTag(R.drawable.ic_euro);
                    break;
                case R.id.rbToPound:
                    rbFromDollar.setEnabled(true);
                    rbFromEuro.setEnabled(true);
                    rbFromPound.setEnabled(false);
                    imgTo.setImageResource(R.drawable.ic_pound);
                    // AGREGO ESTA LÍNEA PARA QUE PASE ALGUNOS TESTS. FÍJATE BIEN
                    imgTo.setTag(R.drawable.ic_pound);
                    break;
            }
        }
    }
}

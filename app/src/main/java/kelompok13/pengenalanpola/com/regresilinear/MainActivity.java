package kelompok13.pengenalanpola.com.regresilinear;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kelompok13.pengenalanpola.com.regresilinear.model.RegresiLinierGanda;

import static java.lang.Double.parseDouble;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<RegresiLinierGanda> mDataList;

    double sigma_x2_kuadrat = 0;
    double sigma_x1_kuadrat = 0;
    double sigma_x1y = 0;
    double sigma_x2y = 0;
    double sigma_x1 = 0;
    double sigma_x2 = 0;
    double sigma_x1x2 = 0;
    double sigma_y = 0;

    private double b1;
    private double b2;
    private double b0;

    private TextView tv_hasil_y;
    private EditText mEtX1;
    private EditText mEtX2;

    private TextView tv_b0;
    private TextView tv_b1;
    private TextView tv_b2;

    private Button mBtnHitung;
    private Button mBtnReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        mEtX1 = findViewById(R.id.et_x1);
        mEtX2 = findViewById(R.id.et_x2);

        tv_b0 = findViewById(R.id.tv_b0);
        tv_b1 = findViewById(R.id.tv_b1);
        tv_b2 = findViewById(R.id.tv_b2);

        tv_hasil_y = findViewById(R.id.tv_hasil_y);
        mBtnHitung = findViewById(R.id.btn_hitung);
        mBtnHitung.setOnClickListener(this);

        mBtnReset = findViewById(R.id.btn_reset);
        mBtnReset.setOnClickListener(this);

        prepareData();

        generateValues();


    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.btn_hitung) {

            String strX1 = mEtX1.getText().toString();
            String strX2 = mEtX2.getText().toString();

            if (TextUtils.isEmpty(strX1) || TextUtils.isEmpty(strX2)) {
                Toast.makeText(this, "Mohon input data.", Toast.LENGTH_SHORT).show();
            } else {
                double doubleX1 = Double.parseDouble(strX1);
                double doubleX2 = Double.parseDouble(strX2);
                hitungY(doubleX1, doubleX2);

                mBtnHitung.setVisibility(View.INVISIBLE);
            }
        }

        if (id == R.id.btn_reset) {
            // reset nilai
            finish();
            startActivity(getIntent());
        }
    }

    private void resetNilai() {

        sigma_x2_kuadrat = 0.0;
        sigma_x1_kuadrat = 0.0;
        sigma_x1y = 0.0;
        sigma_x2y = 0.0;
        sigma_x1 = 0.0;
        sigma_x2 = 0.0;
        sigma_x1x2 = 0.0;
        sigma_y = 0.0;

        b1 = 0.0;
        b2 = 0.0;
        b0 = 0.0;

    }

    private void hitungY(Double x1, double x2) {

        tv_b0.setText("b0 : " + String.valueOf(getB0()));
        tv_b1.setText("b1 : " + String.valueOf(getB1()));
        tv_b2.setText("b2 : " + String.valueOf(getB2()));

        Log.d("TAG", "hitungY: getB0: " + getB0());
        Log.d("TAG", "hitungY: getB1: " + getB1());
        Log.d("TAG", "hitungY: getB2: " + getB2());

        mDataList.add(new RegresiLinierGanda(x1, x2, -1));
        double hasilY = getY(x1, x2);
        tv_hasil_y.setText("y  : " + String.valueOf(hasilY));

        Log.d("TAG", "hitungY: hasilY: " + hasilY);
    }

    private void prepareData() {
        mDataList = new ArrayList<>();
        mDataList.add(new RegresiLinierGanda(2, 24, 10));
        mDataList.add(new RegresiLinierGanda(5, 21, 22));
        mDataList.add(new RegresiLinierGanda(7, 15, 31));
        mDataList.add(new RegresiLinierGanda(9, 13, 38));
        mDataList.add(new RegresiLinierGanda(10, 8, 49));
        mDataList.add(new RegresiLinierGanda(12, 4, 62));
    }

    public void generateValues() {
        double hasil = 0;

        for (RegresiLinierGanda data : mDataList) {
            sigma_x2_kuadrat += Math.pow(data.getX2(), 2);
            sigma_x1_kuadrat += Math.pow(data.getX1(), 2);
            sigma_x1y += data.getX1() * data.getY();
            sigma_x2y += data.getX2() * data.getY();
            sigma_x1 += data.getX1();
            sigma_x2 += data.getX2();
            sigma_x1x2 += data.getX1() * data.getX2();
            sigma_y += data.getY();
        }
    }

    private double getB1() {
        double hasil = ((sigma_x2_kuadrat * sigma_x1y) - (sigma_x1x2 * sigma_x2y)) / ((sigma_x1_kuadrat * sigma_x2_kuadrat) - Math.pow(sigma_x1x2, 2));

        Log.d("TAG_GET_B1", "getB1: atas : " + ((sigma_x2_kuadrat * sigma_x1y) - (sigma_x1x2 * sigma_x2y)));
        Log.d("TAG_GET_B1", "getB1: bawah: " + ((sigma_x1_kuadrat * sigma_x2_kuadrat) - Math.pow(sigma_x1x2, 2)));

        return hasil;
    }

    private double getB2() {
        double hasil = ((sigma_x1_kuadrat * sigma_x2y) - (sigma_x1x2 * sigma_x1y)) / ((sigma_x1_kuadrat * sigma_x2_kuadrat) - Math.pow(sigma_x1x2, 2));
        return hasil;
    }

    private double getB0() {

        double average_y = 0.0;
        double sigma_y = 0.0;

        for (RegresiLinierGanda data : mDataList) {
            sigma_y += data.getY();
        }

        average_y = sigma_y / mDataList.size();

        Log.d("TAG", "average_y: " + average_y);
        Log.d("TAG", "getB1: " + getB1());
        Log.d("TAG", "getAverageX1: " + getAverageX1());
        Log.d("TAG", "getB2: " + getB2());
        Log.d("TAG", "getAverageX2: " + getAverageX2());

        double hasil = average_y - (getB1() * getAverageX1()) - (getB2() * getAverageX2());
        Log.d("TAG", "hasil: " + hasil);
        return hasil;
    }

    private double getAverageX1() {
        double totalX1 = 0.0;
        for (RegresiLinierGanda data : mDataList) {
            totalX1 += data.getX1();
        }
        return totalX1 / mDataList.size();
    }

    private double getAverageX2() {
        double totalX2 = 0.0;
        for (RegresiLinierGanda data : mDataList) {
            totalX2 += data.getX2();
        }
        return totalX2 / mDataList.size();
    }

    private double getY(double X1, double X2) {
        return getB0() + (getB1() * X1) + (getB2() * X2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

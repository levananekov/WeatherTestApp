package levananenkov.myapplication.weathertestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import levananenkov.myapplication.weathertestapp.modules.weather.ui.WeatherActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, WeatherActivity.class));
    }
}

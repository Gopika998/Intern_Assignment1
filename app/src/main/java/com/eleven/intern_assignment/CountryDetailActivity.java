package com.eleven.intern_assignment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;


import com.eleven.intern_assignment.model.Country;

import java.util.List;
import java.util.Objects;
import com.ahmadrosid.svgloader.SvgLoader;

public class CountryDetailActivity extends AppCompatActivity {

    private ShareActionProvider shareActionProvider;

    private String countryName;
    private String countryCapital;
    private String countryRegion;
    private String countryPopulation;
    private String countryArea;
    private String countryBorders;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countrydetails);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Country c = (Country) Objects.requireNonNull(getIntent().getExtras()).getSerializable("country");

        // set flag
        ImageView flag = findViewById(R.id.c_flag);

        SvgLoader.pluck()
                .with(this)
                .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                .load(Objects.requireNonNull(c).getFlag(), flag);


        // set name
        TextView name = findViewById(R.id.c_name);
        countryName = "Name: " + Objects.requireNonNull(c).getName();
        name.setText(countryName);

        // set capital
        TextView capital = findViewById(R.id.c_capital);
        countryCapital = "Capital: " + c.getCapital();
        capital.setText(countryCapital);

        // set region
        TextView region = findViewById(R.id.c_region);
        countryRegion = "Region: " + c.getRegion();
        region.setText(countryRegion);

        // set population
        TextView population = findViewById(R.id.c_population);
        countryPopulation = "Population: " + String.valueOf(c.getPopulation());
        population.setText(countryPopulation);

        // set area
        TextView area = findViewById(R.id.c_area);
        countryArea = "Area: " + String.valueOf(c.getArea());
        area.setText(countryArea);

        // set borders
        TextView borders = findViewById(R.id.c_borders);

        StringBuilder borderText = new StringBuilder("Borders: ");
        List<String> bordersList = c.getBorders();

        if (bordersList.size() > 0) {
            for (int i = 0; i < bordersList.size() - 2; ++i) {
                borderText.append(bordersList.get(i)).append(", ");
            }
            borderText.append(bordersList.get(bordersList.size() - 1));
        }

        countryBorders = borderText.toString();

        borders.setText(countryBorders);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menulist, menu);

        MenuItem menuItem = menu.findItem(R.id.action_get_country_details);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareActionIntent(
                            countryName + "\n"
                            + countryCapital + "\n"
                            + countryRegion + "\n"
                            + countryPopulation + "\n"
                            + countryArea + "\n"
                            + countryBorders);

        return super.onCreateOptionsMenu(menu);
    }

    private void setShareActionIntent(String text) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String deviceData = "";
        deviceData += "Manufacturer: " + Build.MANUFACTURER + "\n";
        deviceData += "Model: " + Build.MODEL + "\n";
        deviceData += "Version: " + Build.VERSION.SDK_INT+ "\n";
        deviceData += "Version Release: " + Build.VERSION.RELEASE + "\n";
        deviceData += "Serial Number: " + Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Toast.makeText(getApplicationContext(), deviceData, Toast.LENGTH_LONG).show();
        return true;
    }
}

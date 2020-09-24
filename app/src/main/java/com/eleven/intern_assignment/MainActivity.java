package com.eleven.intern_assignment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eleven.intern_assignment.model.Country;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private String RESULTS;
    private String TAG = MainActivity.class.getSimpleName();
    private Set<String> regions;
    private TreeMap<String, ArrayList<Country>> filteredCountries = new TreeMap<>();

    static final String NO_REGION = "No Region";
    static final String URL = "https://restcountries.eu/rest/v2/all";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countrydetails);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get data from endpoint
        createRequestQueue();
    }

    /**
     * Creates a Volley RequestQueue to hit REST API endpoint.
     */
    private void createRequestQueue() {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                RESULTS = response;

                // parse JSON
                parseCountries();

                // set list elements
                setRegionsList();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError err) {
                Log.e(TAG, "Json parsing error: " + err.getMessage());
            }
        });

        // add request to queue
        queue.add(stringRequest);
    }

    /**
     * Parse countries from JSON.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void parseCountries() {

        if (RESULTS != null) {

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Country>>(){}.getType();
            List<Country> countries = gson.fromJson(RESULTS, listType);

            // parse regions
            for (Country c : countries) {

                if (!c.getRegion().isEmpty()) {

                    if (!filteredCountries.containsKey(c.getRegion())) {
                        ArrayList<Country> countriesList = new ArrayList<>();
                        countriesList.add(c);
                        filteredCountries.put(c.getRegion(), countriesList);

                    } else {
                        Objects.requireNonNull(filteredCountries.get(c.getRegion())).add(c);
                    }

                } else {

                    if (!filteredCountries.containsKey(NO_REGION)) {
                        ArrayList<Country> countriesList = new ArrayList<>();
                        countriesList.add(c);
                        filteredCountries.put(NO_REGION, countriesList);

                    } else {
                        Objects.requireNonNull(filteredCountries.get(NO_REGION)).add(c);
                    }
                }
            }

            // assign list of regions
            regions = filteredCountries.keySet();
        }
    }

    /**
     * Create regions ListView.
     */
    private void setRegionsList() {

        ListView list = findViewById(R.id.regions_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getBaseContext(), R.layout.region, R.id.region, regions.toArray(new String[0]));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getBaseContext(), CountryListActivity.class);
                intent.putExtra("countries", filteredCountries.get(Objects.requireNonNull(filteredCountries.keySet().toArray())[i]));
                startActivity(intent);
            }
        });

        list.setAdapter(adapter);
    }
}

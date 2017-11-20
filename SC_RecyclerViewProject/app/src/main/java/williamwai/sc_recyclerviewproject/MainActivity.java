package williamwai.sc_recyclerviewproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String API_KEY = "d057b296ebaefbc4ae7d79b8dbb0b6aa";
    private static String URL_DATA = null;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ListItem> listItems;
    static boolean sortByPop;

    public static final String LOG_TAG = MyAdapter.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();




    }

    private void initViews(){

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        listItems = new ArrayList<>();
        adapter = new MyAdapter(listItems, getApplicationContext());





        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


    public void loadRecyclerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data....");
        progressDialog.show();


        URL_DATA = "http://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("results");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                ListItem item = new ListItem(
                                        o.getString("title"),
                                        o.getString("overview"),
                                        o.getString("poster_path")
                                );
                                listItems.add(item);
                            }

                            adapter = new MyAdapter(listItems, getApplicationContext());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_settings:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                URL_DATA = null;
                loadRecyclerViewData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadRecyclerViewData1() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data....");
        progressDialog.show();

        URL_DATA = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&vote_count.gte=500&api_key=" + API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("results");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                ListItem item = new ListItem(
                                        o.getString("id"),
                                        o.getString("overview"),
                                        o.getString("poster_path")
                                );
                                listItems.add(item);
                            }

                            adapter = new MyAdapter(listItems, getApplicationContext());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    private void checkSortOrder() {


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sortOrder = preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular)
        );
        if (sortOrder.equals(this.getString(R.string.pref_sort_order_key))) {
            onResume();
            loadRecyclerViewData();
        } else {
            onResume();
            loadRecyclerViewData1();
        }


    }

    @Override
    public void onResume () {
        super.onResume();
        if (listItems.isEmpty()) {
            loadRecyclerViewData();
        } else {
            loadRecyclerViewData1();
        }
    }
}

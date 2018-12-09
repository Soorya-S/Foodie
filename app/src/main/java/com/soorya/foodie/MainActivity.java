package com.soorya.foodie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soorya.foodie.R;
import com.soorya.foodie.adapter.FoodListAdapter;
import com.soorya.foodie.interfaces.WebAPIService;
import com.soorya.foodie.model.FoodItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView foodRecyclerView;
    private FoodListAdapter foodListAdapter;
    private List<FoodItem> foodList = new ArrayList<>();
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foodRecyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebAPIService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        WebAPIService api = retrofit.create(WebAPIService.class);

        Call<List<FoodItem>> call = api.getFoodData();

        call.enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {

                foodList = response.body();
                foodListAdapter = new FoodListAdapter(MainActivity.this,foodList);
                foodRecyclerView.setAdapter(foodListAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                Log.d("Retrofit err" , t.getMessage());
            }
        });
    }
}
package com.soorya.foodie.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soorya.foodie.R;
import com.soorya.foodie.adapter.FoodListAdapter;
import com.soorya.foodie.interfaces.CartValueUpdater;
import com.soorya.foodie.interfaces.WebAPIService;
import com.soorya.foodie.model.FoodItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CartValueUpdater {

    private RecyclerView foodRecyclerView;
    private FoodListAdapter foodListAdapter;
    private List<FoodItem> foodList = new ArrayList<>();
    private List<FoodItem> mainFoodList = new ArrayList<>();
    private ProgressBar progressBar;
    private RelativeLayout sortButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foodRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL, false);
        foodRecyclerView.setLayoutManager(layoutManager);
        foodListAdapter = new FoodListAdapter(this,foodList);
        foodRecyclerView.setAdapter(foodListAdapter);
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

        final Call<List<FoodItem>> call = api.getFoodData();

        call.enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {

                foodList.clear();
                foodList.addAll(response.body());
                mainFoodList.clear();
                mainFoodList.addAll(response.body());
                foodListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                Log.d("Retrofit err" , t.getMessage());
            }
        });

        sortButton = findViewById(R.id.sort_button);

        final PopupMenu popup = new PopupMenu(MainActivity.this, sortButton);
        popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.price:
                        sortByPrice();
                        break;
                    case R.id.rating:
                        sortByRating();
                        break;
                    default:
                        sortDefault();
                }
                return true;
            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void sortByPrice()
    {
        Collections.sort(foodList, new Comparator<FoodItem>() {
            @Override
            public int compare(FoodItem o1, FoodItem o2) {
                return o1.getItemPrice().compareTo(o2.getItemPrice());
            }
        });
        foodListAdapter.notifyDataSetChanged();
    }

    private void sortByRating()
    {
        Collections.sort(foodList, new Comparator<FoodItem>() {
            @Override
            public int compare(FoodItem o1, FoodItem o2) {
                return o2.getAverageRating().compareTo(o1.getAverageRating());
            }
        });
        foodListAdapter.notifyDataSetChanged();
    }

    private void sortDefault()
    {
        foodList.clear();
        foodList.addAll(mainFoodList);
        foodListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCartValueChanged(int newVal) {

        Toast.makeText(MainActivity.this,String.valueOf(newVal),Toast.LENGTH_SHORT).show();

    }
}
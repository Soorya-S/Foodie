package com.soorya.foodie.activities;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.soorya.foodie.R;
import com.soorya.foodie.adapter.CartOrderDetailAdapter;
import com.soorya.foodie.model.CartItem;
import com.soorya.foodie.utils.CartDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity {


    private RecyclerView orederDetailRecyclerview;
    private TextView itemTotalText;
    private TextView extraChargesText;
    private TextView deliveryChargesText;
    private TextView totalChargesText;
    private EditText promoEdittext;
    private TextView applyPromoText;
    private TextView promoStatusText;
    private TextView promoDiscountText;
    private TextView grandTotalText;
    private Button payButton;
    private CartOrderDetailAdapter adapter;

    private CartDatabase cartDatabase;
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        orederDetailRecyclerview = findViewById(R.id.recycler_view);
        orederDetailRecyclerview.setLayoutManager(new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL, false));
        adapter = new CartOrderDetailAdapter(this,cartItems);
        orederDetailRecyclerview.setAdapter(adapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                cartDatabase = Room.databaseBuilder(CartActivity.this,CartDatabase.class,"cartdb").build();
                cartItems = cartDatabase.cartDB().getCartDetails();
                adapter.notifyDataSetChanged();
            }
        }).start();

        itemTotalText = findViewById(R.id.item_total_text);
        extraChargesText = findViewById(R.id.extra_charges_text);
        deliveryChargesText = findViewById(R.id.delivery_fee_text);
        totalChargesText = findViewById(R.id.total_text);
        promoEdittext = findViewById(R.id.promo_code_text);
        applyPromoText = findViewById(R.id.apply_text);
        promoStatusText = findViewById(R.id.promo_desc);
        promoDiscountText = findViewById(R.id.promo_discount_text);
        grandTotalText = findViewById(R.id.final_total_text);
        payButton = findViewById(R.id.pay_button);


    }
}

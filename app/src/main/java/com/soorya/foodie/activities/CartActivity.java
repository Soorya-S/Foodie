package com.soorya.foodie.activities;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soorya.foodie.R;
import com.soorya.foodie.adapter.CartOrderDetailAdapter;
import com.soorya.foodie.interfaces.CartValueUpdater;
import com.soorya.foodie.model.CartItem;
import com.soorya.foodie.utils.CartDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartValueUpdater {


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
    private ImageView back;
    private RelativeLayout emptyLayout;

    private boolean isPromoApplied = false;
    private CartDatabase cartDatabase;
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        orederDetailRecyclerview = findViewById(R.id.order_detail_recyclerview);
        orederDetailRecyclerview.setLayoutManager(new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL, false));
        ((SimpleItemAnimator) orederDetailRecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter = new CartOrderDetailAdapter(this,cartItems);
        orederDetailRecyclerview.setAdapter(adapter);

        initDatabase();

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
        back = findViewById(R.id.back);
        emptyLayout = findViewById(R.id.empty_layout);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemTotal>0)
                {
                    Toast.makeText(CartActivity.this,"order placed",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CartActivity.this,OrderPlacedActivity.class));
                    deleteAllDataFromCart();
                    finish();
                }
                else
                {
                    Toast.makeText(CartActivity.this,"Please add item to cart!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        promoStatusText.setText("");
        applyPromoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (promoEdittext!=null && !promoEdittext.getText().toString().equals(""))
                {
                    if (applyPromoText.getText().toString().equalsIgnoreCase("Apply"))
                    {
                        applyPromo(promoEdittext.getText().toString());
                        promoEdittext.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(promoEdittext.getWindowToken(), 0);
                    }
                    else
                    {
                        removePromo();
                    }

                    if (isPromoApplied)
                    {
                        applyPromoText.setText("Remove");
                        applyPromoText.setTextColor(getResources().getColor(R.color.red));
                    }
                    else
                    {
                        applyPromoText.setText("Apply");
                        applyPromoText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
                else
                {
                    promoStatusText.setText("Please enter promo code");
                    promoStatusText.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });
    }



    /*
     * initialize the database and read data from the 'cartdb'.
     */
    @SuppressLint("StaticFieldLeak")
    private void initDatabase()
    {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground( final Void ... params ) {

                cartDatabase = Room.databaseBuilder(CartActivity.this,CartDatabase.class,"cartdb").build();
                cartItems.addAll(cartDatabase.cartDB().getCartDetails());

                return null;
            }
            @Override
            protected void onPostExecute( final Void result ) {
                adapter.notifyDataSetChanged();
                if(cartItems.size()>0)
                {
                    calculateTotalCost();
                }
                else
                {
                    showCartEmptyMessage();
                }
            }
        }.execute();
    }

    /*
     * this call back is called when the cart item changed in CartAdapterClass
     */
    @Override
    public void onCartValueChanged(CartItem cartItem) {
        for (CartItem c : cartItems)
        {
            calculateTotalCost();
        }
    }

    private float itemTotal;
    private float deliveryCharges = 30;
    private float extraCharges = 15;
    private float grandTotal;

    /*
     * this function calculate the total cost, additional charges, delivery charges and grand total
     */
    private void calculateTotalCost()
    {
        float total=0;

        for (CartItem c : cartItems)
        {
            total += c.getFoodPrice()*c.getCartValue();
        }

        itemTotal = total;

        itemTotalText.setText(String.valueOf(itemTotal));
        extraChargesText.setText(String.valueOf(extraCharges));
        deliveryChargesText.setText(String.valueOf(deliveryCharges));
        total = total + deliveryCharges + extraCharges;
        totalChargesText.setText("\u20B9" + total);
        grandTotal = total;
        grandTotalText.setText("\u20B9" + grandTotal);
    }

    /*
     * This function will show a message when the cart is empty
     */
    private void showCartEmptyMessage()
    {
        emptyLayout.setVisibility(View.VISIBLE);
        payButton.setText("Add items to cart");
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    /*
     * function that validate and apply promo code
     */
    private void applyPromo(String promo)
    {
        if (promo.equalsIgnoreCase("F22LABS"))
        {
            if (itemTotal >= 400)
            {
                promoStatusText.setTextColor(getResources().getColor(R.color.green));
                promoStatusText.setText("Promo applied successfully.");
                isPromoApplied = true;

                float discount = (grandTotal/100)*20;
                float res = grandTotal - discount;

                grandTotal = res;
                grandTotalText.setText("\u20B9" +grandTotal);
                promoDiscountText.setText( " - " + discount);
            }
            else
            {
                Log.d("","This code is only applicable for the order amount above 400");
                promoStatusText.setTextColor(getResources().getColor(R.color.red));
                promoStatusText.setText("This code is only applicable for the order amount above 400");
                promoDiscountText.setText("0.00");
                isPromoApplied = false;
            }
        }
        else if (promo.equalsIgnoreCase("FREEDEL"))
        {
            if (itemTotal > 100)
            {
                deliveryChargesText.setText("Free");
                deliveryChargesText.setTextColor(getResources().getColor(R.color.green));
                promoStatusText.setTextColor(getResources().getColor(R.color.green));
                promoStatusText.setText("Promo applied successfully.");
                grandTotal -= deliveryCharges;
                deliveryCharges = 0;
                totalChargesText.setText("\u20B9" + grandTotal);
                promoDiscountText.setText("0.00");
                grandTotalText.setText("\u20B9" + grandTotal);
                isPromoApplied = true;
            }
            else
            {
                promoStatusText.setText("This code is only applicable for the order amount above 100");
                promoStatusText.setTextColor(getResources().getColor(R.color.red));
                promoDiscountText.setText("0.00");
                isPromoApplied = false;
            }
        }
        else {
            promoStatusText.setText("Invalid promo code");
            promoStatusText.setTextColor(getResources().getColor(R.color.red));
            promoDiscountText.setText("0.00");
            isPromoApplied = false;
        }
    }

    /*
     * this function will remove the promo code from the order
     */
    private void removePromo() {
        isPromoApplied = false;
        promoEdittext.setText("");
        promoStatusText.setText("");
        promoDiscountText.setText("0.00");
        deliveryCharges = 30;
        calculateTotalCost();
    }

    /*
     * this function is called when the user hit place order. So that all cart items will
     * be erased after successful placement of order
     */
    private void deleteAllDataFromCart()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                cartDatabase.cartDB().nukeTable();
            }
        }).start();
    }
}
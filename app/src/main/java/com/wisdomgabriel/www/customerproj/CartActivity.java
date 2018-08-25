package com.wisdomgabriel.www.customerproj;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wisdomgabriel.www.customerproj.DatabaseForCart.CartDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private Button order, calculate_price;
    private List<ShopModel> lists = new ArrayList<>();
    private List<ShopModel> liststhesecond = new ArrayList<>();
    private AdapterRecyclerview adapterRecyclerview;
    private CartDatabase cartDatabase;
    private String tablenumberstring;
    private TextView total_price;
    double priceonclick = 0;
    private EditText tableNumber;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        tableNumber = (EditText)findViewById(R.id.tablenumber);
        new FecthFood().execute();



        cartDatabase = new CartDatabase(getApplicationContext());



         tablenumberstring = tableNumber.getText().toString();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        adapterRecyclerview = new AdapterRecyclerview(CartActivity.this, lists);
        order = findViewById(R.id.order);
        total_price = findViewById(R.id.total_calculated_price);

        findViewById(R.id.calculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(ShopModel shopModel : lists){

                    priceonclick += Double.valueOf(shopModel.getFoodPrice());

                }
                total_price.setText("# "+priceonclick);
                priceonclick = 0;
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(tableNumber.getText().toString())) {

                   if(lists.size() >0) {
                       sendtoWaiter();
                   }else {
                       Toast.makeText(CartActivity.this, "Please make sure atleast one food item is selcted...", Toast.LENGTH_SHORT).show();
                   }

                }else {
                    Toast.makeText(CartActivity.this, "Please give a sit number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(CartActivity.this));
        recyclerView.setAdapter(adapterRecyclerview);
        for(ShopModel shopModel : lists){

            priceonclick += Double.valueOf(shopModel.getFoodPrice());

        }
        total_price.setText("# "+priceonclick);
        priceonclick = 0;
    }

    private void sendtoWaiter() {

        final ProgressDialog progressDialog = new ProgressDialog(CartActivity.this);
        progressDialog.setMessage("wait.. ordering your food...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        if (lists != null) {
            for (final ShopModel shopModel : lists) {
                Toast.makeText(this, tableNumber.getText().toString(), Toast.LENGTH_SHORT).show();
                shopModel.setSitnumber(tableNumber.getText().toString());
                FirebaseFirestore.getInstance().collection("orders").add(shopModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            cartDatabase.deleteContact();
                            Toast.makeText(CartActivity.this, shopModel.getFoodName() + "  food ordered", Toast.LENGTH_SHORT).show();
                        }else {

                            progressDialog.dismiss();
                            Toast.makeText(CartActivity.this, shopModel.getFoodName() + " food not ordered, please try again later,or call waiter attention..", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        }else {
            Toast.makeText(this, "Please make sure you select atleast a food from the menu...", Toast.LENGTH_SHORT).show();
        }
    }
       class FecthFood extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {

                if(lists.size() >0){
                    lists.clear();
                }
                lists.addAll(cartDatabase.getindivMessages());


                return null;
            }

            @Override
            public void onPostExecute(Void count) {
                adapterRecyclerview.notifyDataSetChanged();
            }
        }

}

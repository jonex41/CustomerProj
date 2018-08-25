package com.wisdomgabriel.www.customerproj;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wisdomgabriel.www.customerproj.DatabaseForCart.CartDatabase;

import com.wisdomgabriel.www.customerproj.count.Utils;


public class DetailShopActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageView;
    private TextView txtfor_description,cost_per_product,quantityofgoods,total_goods;


    private Button addbtn,subtractbtn,addtocart;


    private int mNotificationsCount = 0;
    int rating;
    Double price;
    private int mQuantity = 1;
    private double mTotalPrice;

    CartDatabase cartDatabase;
    private String nameExtra,imageUrl,prices, description;
    private int total =1;
    private int resultmoney = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailshop_activity);
        cartDatabase = new CartDatabase(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbardetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.detail_image);

        txtfor_description = (TextView) findViewById(R.id.itemdescreal);
        cost_per_product = (TextView) findViewById(R.id.real_price);
        quantityofgoods = (TextView) findViewById(R.id.count_quantity);
        total_goods = (TextView) findViewById(R.id.real_total);

        addbtn = (Button) findViewById(R.id.addquntity);
        subtractbtn = (Button) findViewById(R.id.subtratquntity);
        addtocart = (Button) findViewById(R.id.addtocart);

        addtocart.setOnClickListener(this);
        subtractbtn.setOnClickListener(this);
        addtocart.setOnClickListener(this);
        addbtn.setOnClickListener(this);


         nameExtra = getIntent().getStringExtra("foodname");
        getSupportActionBar().setTitle(nameExtra);

         prices = getIntent().getStringExtra("foodprice");
          resultmoney = Integer.parseInt(prices);
         description = getIntent().getStringExtra("fooddescription");
      //  sellerid = getIntent().getStringExtra("sellerid");
         if(description != null) {
             txtfor_description.setText(description);
         }
       /* DecimalFormat precision = new DecimalFormat("0.00");
        String koo = prices.toString();*/
        cost_per_product.setText("#"+Double.valueOf(prices));


         imageUrl = getIntent().getStringExtra("foodImage");

        GlideApp.with(getApplicationContext()).load(imageUrl)
                .into(imageView);
        price = Double.parseDouble(prices);
        if (mQuantity == 1){

            mTotalPrice = price;
            displayCost(price);
        }
        new FetchCountTask().execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.addquntity:
                increment();
                break;
            case R.id.subtratquntity:
                        decrement();
                break;

            case R.id.addtocart:

                addToCart();

                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shopping_menu, menu);

        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils.setBadgeCount(this, icon, mNotificationsCount);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_notifications:
                startActivity(new Intent(DetailShopActivity.this, CartActivity.class));
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        new FetchCountTask().execute();
    }

    public void increment(){
        total+=1;
        quantityofgoods.setText(total+"");

        resultmoney = total* Integer.parseInt(prices);
        total_goods.setText("#"+Double.valueOf(resultmoney));
       // price = getIntent().getExtras().getDouble(FRAGRANCE_PRICE);

    }

    public void decrement(){

        if(total >=2) {
            total -= 1;
            quantityofgoods.setText(total+"");
            resultmoney = total* Integer.parseInt(prices);
            total_goods.setText("#"+Double.valueOf(resultmoney));
        }
    }
    private void displayQuantity(int numberOfItems) {

        quantityofgoods.setText(String.valueOf(numberOfItems));
    }

    private void displayCost(double totalPrice) {


        total_goods.setText("#"+Double.valueOf(totalPrice));
    }

    public void addToCart() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("are you sure you want to add to cart");
        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ShopModel listOfShop = new ShopModel();


                listOfShop.setFoodName(nameExtra);
                listOfShop.setImageUrl(imageUrl);
                listOfShop.setFoodPrice(String.valueOf(resultmoney));
                listOfShop.setPlatesNumber(total+"");
              //  listOfShop.setPrice_per_one(prices);

                if(!cartDatabase.checkUser(listOfShop.getFoodName())) {
                    cartDatabase.addtoCart(listOfShop);
                }else {

                    Toast.makeText(DetailShopActivity.this, "This food is already on the list, please check your order...", Toast.LENGTH_LONG).show();
                }
                new FetchCountTask().execute();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the items.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void updateNotificationsBadge(int count) {
        mNotificationsCount = count;

        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }

    /*
Sample AsyncTask to fetch the notifications count
*/
    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {


            int counts = (int) cartDatabase.getProfilesCount();

            return counts;

        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }

}

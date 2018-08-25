package com.wisdomgabriel.www.customerproj;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public abstract class GroupClassFragment extends Fragment {


    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter<ShopModel, ShopViewHolder> mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.groupfragment, container, false);
        Toast.makeText(getContext(), getString(), Toast.LENGTH_SHORT).show();

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);

        setUpAdapter();
        recyclerView.setAdapter(mAdapter);
    return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }


    private void setUpAdapter() {

        Query query = FirebaseFirestore.getInstance().collection(getString());


        FirestoreRecyclerOptions<ShopModel> options = new FirestoreRecyclerOptions.Builder<ShopModel>().setQuery(query, ShopModel.class).build();


        mAdapter = new FirestoreRecyclerAdapter<ShopModel, ShopViewHolder>(options) {
            @Override
            protected void onBindViewHolder(final ShopViewHolder holder, int position, final ShopModel model) {

                holder.setFoodName(model.getFoodName());
                holder.setFoodPrice("#"+model.getFoodPrice());
                Glide.with(getActivity())
                        .load(model.getImageUrl())

                        .into(holder.foodIamge);

                    holder.foodclick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), DetailShopActivity.class);
                            intent.putExtra("foodname", model.getFoodName());
                            intent.putExtra("foodprice", model.getFoodPrice());
                            intent.putExtra("fooddescription", model.getFoodDescription());
                            intent.putExtra("foodImage", model.getImageUrl());
                            startActivity(intent);
                        }
                    });

            }

            @NonNull
            @Override
            public ShopViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
                return new ShopViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.food_card, parent, false));

            }
        };



    }

    public abstract String getString();

    class ShopViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public ImageView foodIamge;
        public TextView title, costs_of_product;
        public LinearLayout foodclick;



        public ShopViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            title = (TextView) mView.findViewById(R.id.foodname);
            costs_of_product = (TextView) mView.findViewById(R.id.foodPrice);
            foodIamge = (ImageView) mView.findViewById(R.id.foodImage);

            foodclick = (LinearLayout) mView.findViewById(R.id.click_shop);


        }


        public void setFoodName(String foodName) {
            title.setText(foodName);
        }

        public void setFoodPrice(String foodPrice) {
            costs_of_product.setText(foodPrice);
        }
    }
}

package com.wisdomgabriel.www.customerproj;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wisdomgabriel.www.customerproj.DatabaseForCart.CartDatabase;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterRecyclerview extends RecyclerView.Adapter<AdapterRecyclerview.ShopViewHolder> {

    private Context context;
    private List<ShopModel> lists;


    public AdapterRecyclerview(Context context, List<ShopModel> lists) {
        this.context = context;
        this.lists = lists;

    }

    @Override
    public ShopViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        return new ShopViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerecyclerviewforcart, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull  final ShopViewHolder holder, int position) {

        final ShopModel bookModel = lists.get(position);

        holder.food_name.setText(bookModel.getFoodName());
        holder.food_price.setText(bookModel.getFoodPrice());
       holder.plates_ordered.setText(bookModel.getPlatesNumber());


       /* holder.buttonSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(holder.plates_ordered.getText().toString()) >1){

                    int number = Integer.parseInt(holder.plates_ordered.getText().toString());
                    int decrease =number -1;
                    holder.plates_ordered.setText(decrease+"");
                    int priceleft = decrease * Integer.parseInt(bookModel.getPrice_per_one());

                    holder.food_price.setText(priceleft+"");
                    lists.get(holder.getAdapterPosition()).setFoodPrice(priceleft+"");
                }
            }
        });*/
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog));
                builder1.setMessage("Are you sure you want to delete this food...");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                ShopModel shopModel = lists.get(holder.getAdapterPosition());
                                CartDatabase cartDatabase = new CartDatabase(context);
                                cartDatabase.deleteRecord(bookModel.getFoodName());
                                lists.remove(shopModel);
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyDataSetChanged();
                            }
                        });

                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


                return true;
            }
        });

      /*  holder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    int number = Integer.parseInt(holder.plates_ordered.getText().toString());
                    int decrease =number +1;
                    holder.plates_ordered.setText(decrease+"");
                    int priceleft = decrease * Integer.parseInt(bookModel.getPrice_per_one());

                    holder.food_price.setText(priceleft+"");

            }
        });*/

        Glide.with(context).load(bookModel.getImageUrl()).into(holder.foodIamge);
    }

    @Override
    public int getItemCount() {
        return (lists != null)? lists.size(): 0;
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {

        private final TextView plates_ordered;
        public View mView;
        public ImageView foodIamge;
     //   private CircleImageView buttonAdd, buttonSubtract;
        public TextView food_name, food_price;
        public LinearLayout foodclick;




        public ShopViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            food_name = (TextView) mView.findViewById(R.id.foodName);
            food_price = (TextView) mView.findViewById(R.id.foodPrice);
            foodIamge = (ImageView) mView.findViewById(R.id.foodImage);
          plates_ordered = (TextView) mView.findViewById(R.id.plates_ordered);

            foodclick = (LinearLayout) mView.findViewById(R.id.click_shop);

          //  buttonAdd =  mView.findViewById(R.id.add);
           // buttonSubtract =  mView.findViewById(R.id.subtract);


        }



    }
}

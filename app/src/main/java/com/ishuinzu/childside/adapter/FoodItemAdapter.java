package com.ishuinzu.childside.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.ishuinzu.childside.R;
import com.ishuinzu.childside.object.FoodItemObject;

import java.util.List;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import de.hdodenhof.circleimageview.CircleImageView;
import io.alterac.blurkit.BlurLayout;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {
    private Context context;
    private List<FoodItemObject> foodItemObjects;
    private LayoutInflater inflater;
    private int calories;

    public FoodItemAdapter(Context context, List<FoodItemObject> foodItemObjects, LayoutInflater inflater, int calories) {
        this.context = context;
        this.foodItemObjects = foodItemObjects;
        this.inflater = inflater;
        this.calories = calories;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public FoodItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_food_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FoodItemAdapter.ViewHolder holder, int position) {
        holder.layoutFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        holder.blurLayout.startBlur();
        holder.txtTitle.setText(foodItemObjects.get(position).getTitle());
        holder.txtCalories.setText(foodItemObjects.get(position).getCalories() + " cal");
        holder.progressCalories.setProgress(foodItemObjects.get(position).getCalories(), calories);
        Glide.with(context).load(foodItemObjects.get(position).getImage_link()).into(holder.imgFood);
    }

    @Override
    public int getItemCount() {
        return foodItemObjects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView layoutFoodItem;
        private final TextView txtTitle;
        private final TextView txtCalories;
        private final CircularProgressIndicator progressCalories;
        private final CircleImageView imgFood;
        private final BlurLayout blurLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutFoodItem = itemView.findViewById(R.id.layoutFoodItem);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtCalories = itemView.findViewById(R.id.txtCalories);
            progressCalories = itemView.findViewById(R.id.progressCalories);
            imgFood = itemView.findViewById(R.id.imgFood);
            blurLayout = itemView.findViewById(R.id.blurLayout);
        }
    }
}
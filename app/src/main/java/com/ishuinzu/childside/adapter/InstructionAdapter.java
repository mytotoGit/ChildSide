package com.ishuinzu.childside.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.card.MaterialCardView;
import com.ishuinzu.childside.R;
import com.ishuinzu.childside.object.InstructionObject;

import java.util.List;

public class InstructionAdapter extends PagerAdapter {
    private Context context;
    private List<InstructionObject> instructionObjects;
    private LayoutInflater inflater;

    public InstructionAdapter(Context context, List<InstructionObject> instructionObjects) {
        this.context = context;
        this.instructionObjects = instructionObjects;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return instructionObjects.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (MaterialCardView) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.item_instruction, container, false);

        LottieAnimationView lottieInstruction = view.findViewById(R.id.lottieInstruction);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtDescription = view.findViewById(R.id.txtDescription);

        lottieInstruction.setAnimation(instructionObjects.get(position).getImg_id());
        txtTitle.setText(instructionObjects.get(position).getTitle());
        txtDescription.setText(instructionObjects.get(position).getDescription());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((MaterialCardView) object);
    }
}

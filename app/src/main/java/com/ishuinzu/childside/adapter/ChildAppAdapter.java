package com.ishuinzu.childside.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ishuinzu.childside.R;
import com.ishuinzu.childside.object.ChildAppObject;

import java.util.List;

public class ChildAppAdapter extends BaseAdapter {
    private Context context;
    private List<ChildAppObject> childAppObjects;
    private LayoutInflater inflater;
    private PackageManager packageManager;
    private int cellHeight;

    public ChildAppAdapter(Context context, List<ChildAppObject> childAppObjects, int cellHeight) {
        this.context = context;
        this.childAppObjects = childAppObjects;
        this.inflater = LayoutInflater.from(context);
        this.packageManager = context.getPackageManager();
        this.cellHeight = cellHeight;
    }

    @Override
    public int getCount() {
        return childAppObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return childAppObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = view = inflater.inflate(R.layout.item_child_app, parent, false);

        LinearLayout layoutChildApp = view.findViewById(R.id.layoutChildApp);
        ImageView iconApp = view.findViewById(R.id.iconApp);
        TextView nameApp = view.findViewById(R.id.nameApp);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, cellHeight);
        layoutChildApp.setLayoutParams(layoutParams);

        iconApp.setImageDrawable(childAppObjects.get(position).getIcon());
        nameApp.setText(childAppObjects.get(position).getName());
        layoutChildApp.setOnLongClickListener(view12 -> {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + childAppObjects.get(position).getPackage_name()));
            context.startActivity(intent);
            return true;
        });
        layoutChildApp.setOnClickListener(view1 -> {
            if (childAppObjects.get(position).getIs_selected_lock().equals("false")) {
                Intent launchIntent = packageManager.getLaunchIntentForPackage(childAppObjects.get(position).getPackage_name());
                if (launchIntent != null) {
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(launchIntent);
                } else {
                    Toast.makeText(context, "Package Not Found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Locked", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
package com.cybridz.ruxtictactoe.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BaseEmojiAdapter extends BaseAdapter {

    private final SharedPreferences preferences;
    private final String key;
    private Context context;
    private String[] emojis;

    private List<TextView> views = new ArrayList();

    private int selectedPosition;

    public BaseEmojiAdapter(Context context, String[] emojis, int selectedPosition, String key) {
        this.context = context;
        this.emojis = emojis;
        this.selectedPosition = selectedPosition;
        this.preferences = context.getSharedPreferences("ruxtictactoe_app_preferences", MODE_PRIVATE);
        this.key = key;
    }

    public  String getSelectedEmoji() {
        return emojis[selectedPosition];
    }

    @Override
    public int getCount() {
        return emojis.length;
    }

    @Override
    public Object getItem(int i) {
        return emojis[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TextView tv = viewHolder.textView;
        tv.setText(emojis[position]);
        tv.setTextSize(40);
        tv.setPadding(4,4,4,4);
        tv.setBackgroundColor(Color.BLACK);
        tv.setOnClickListener(view -> onClick(position));
        views.add(position, tv);
        if(position == selectedPosition){
            highlight(position);
        }
        return convertView;
    }

    public void highlight(Integer position) {
        for(TextView tv : views){
            tv.setBackgroundColor(Color.BLACK);
        }
        TextView tv = views.get(position);
        if(tv != null ){
            tv.setBackgroundColor(Color.rgb(104,194,246));
        }
    }

    public void onClick(Integer position){
        if( selectedPosition != position ){
            highlight(position);
            selectedPosition = position;
            preferences.edit().putInt(key, position).apply();
        }
    }

    static class ViewHolder {
        TextView textView;
    }
}

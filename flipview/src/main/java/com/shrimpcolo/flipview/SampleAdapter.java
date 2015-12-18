package com.shrimpcolo.flipview;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2015/12/17.
 */
public class SampleAdapter extends BaseAdapter {
//    private static final int HIGHLIGHT_COLOR = 0x999be6ff;
      private static final int HIGHLIGHT_COLOR = 0xff808080;

    private Context mContext;

    // declare the color generator and drawable builder
    private ColorGenerator mColorGenerator;
    private TextDrawable.IBuilder mDrawableBuilder;

    SampleAdapter(Context context) {
        mContext = context;

        mColorGenerator = ColorGenerator.MATERIAL;
        mDrawableBuilder = TextDrawable.builder().round();
    }

    // list of data items
    private List<ListData> mDataList = Arrays.asList(
            new ListData("Iron Man"),
            new ListData("Captain America"),
            new ListData("James Bond"),
            new ListData("Harry Potter"),
            new ListData("Sherlock Holmes"),
            new ListData("Black Widow"),
            new ListData("Hawk Eye"),
            new ListData("Iron Man"),
            new ListData("Guava"),
            new ListData("Tomato"),
            new ListData("Pineapple"),
            new ListData("Strawberry"),
            new ListData("Watermelon"),
            new ListData("Pears"),
            new ListData("Kiwi"),
            new ListData("Plums")
    );

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public ListData getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ListData item = getItem(position);

        // provide support for selected state
        updateCheckedState(holder, item);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when the image is clicked, update the selected state
                ListData data = getItem(position);
                data.setChecked(!data.isChecked);
                updateCheckedState(holder, data);
            }
        });
        holder.textView.setText(item.data);

        return convertView;
    }

    private static class ListData {

        private String data;

        private boolean isChecked;

        public ListData(String data) {
            this.data = data;
        }

        public void setChecked(boolean isChecked) {
            this.isChecked = isChecked;
        }
    }

    private static class ViewHolder {

        private View view;

        private ImageView imageView;

        private TextView textView;

        private ImageView checkIcon;

        private ViewHolder(View view) {
            this.view = view;
            imageView = (ImageView) view.findViewById(R.id.imageView);
            textView = (TextView) view.findViewById(R.id.textView);
            checkIcon = (ImageView) view.findViewById(R.id.check_icon);
        }
    }

    private void updateCheckedState(ViewHolder holder, ListData item) {
        if (item.isChecked) {
            holder.imageView.setImageDrawable(mDrawableBuilder.build(" ", 0xff616161));
            holder.view.setBackgroundColor(HIGHLIGHT_COLOR);
            holder.checkIcon.setVisibility(View.VISIBLE);
        } else {
            TextDrawable drawable = mDrawableBuilder.build(String.valueOf(item.data.charAt(0)), mColorGenerator.getColor(item.data));
            holder.imageView.setImageDrawable(drawable);
            holder.view.setBackgroundColor(Color.TRANSPARENT);
            holder.checkIcon.setVisibility(View.GONE);
        }
    }
}

package com.shrimpcolo.flipview;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private static final String TAG = "SmapleAdapter";

    private static final int HIGHLIGHT_COLOR = 0x999be6ff;
    //private static final int HIGHLIGHT_COLOR = 0xff808080;

    private Context mContext;
    private Animation flipAnim1;
    private Animation flipAnim2;
    private ImageView mFlipView;
    private ViewHolder mViewHold = null;

    // declare the color generator and drawable builder
    private ColorGenerator mColorGenerator;
    private TextDrawable.IBuilder mDrawableBuilder;


    SampleAdapter(Context context) {
        mContext = context;

        mColorGenerator = ColorGenerator.MATERIAL;
        mDrawableBuilder = TextDrawable.builder().round();

        flipAnim1 = AnimationUtils.loadAnimation(mContext, R.anim.flip_anim_to_middle);
        flipAnim2 = AnimationUtils.loadAnimation(mContext, R.anim.flip_anim_from_middle);

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

        holder.textView.setText(item.data);
        holder.imageView.setTag("" + position);//0,1,...
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start animation
                mFlipView = (ImageView) v;
                mFlipView.clearAnimation();
                mFlipView.setAnimation(flipAnim1);
                mFlipView.startAnimation(flipAnim1);

                try {
                    Log.e(TAG, "\n v.getTag = " + (Integer.parseInt(v.getTag().toString())));
                    setAnimListners(holder, mDataList.get(Integer.parseInt(v.getTag().toString())));
                }catch (NullPointerException exp){
                    Log.e(TAG, exp.getMessage());
                }


                // when the image is clicked, update the selected state
//                ListData data = getItem(position);
//                data.setChecked(!data.isChecked);
//                updateCheckedState(holder, data);
            }
        });


        return convertView;
    }

    private void setAnimListners(final ViewHolder holder, final ListData curListData) {
        Animation.AnimationListener animListner;
        animListner = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.e(TAG, "\n  onAnimationStart = " + animation);

                if (animation == flipAnim1) {
                    Log.e(TAG, "curListData.isChecked = " + curListData.isChecked);

                    if (curListData.isChecked) {
                        //mFlipView.setImageResource(R.drawable.cb_unchecked);
                        TextDrawable drawable = mDrawableBuilder.build(String.valueOf(curListData.data.charAt(0)), mColorGenerator.getColor(curListData.data));
                        holder.imageView.setImageDrawable(drawable);
                        holder.view.setBackgroundColor(Color.TRANSPARENT);

                    } else {
                        mFlipView.setBackgroundDrawable(mDrawableBuilder.build(" ", 0xff616161));
                        mFlipView.setImageResource(R.drawable.check_sm);
                        holder.view.setBackgroundColor(HIGHLIGHT_COLOR);
                    }
                    mFlipView.clearAnimation();
                    mFlipView.setAnimation(flipAnim2);
                    mFlipView.startAnimation(flipAnim2);
                } else {
                    Log.e(TAG, "\n  onAnimationStart animation != flipAnim1");
                    curListData.setChecked(!curListData.isChecked);
//                    setCount();
//                    setActionMode();
                }
            }

            // Set selected count
//            private void setCount() {
//                if (curListData.isChecked()) {
//                    checkedCount++;
//                } else {
//                    if (checkedCount != 0) {
//                        checkedCount--;
//                    }
//                }
//
//            }

            // Show/Hide action mode
//            private void setActionMode() {
//                if (checkedCount > 0) {
//                    if (!isActionModeShowing) {
//                        mMode = ((MainActivity) context).startActionMode(new MainActivity.AnActionModeOfEpicProportions(context));
//                        isActionModeShowing = true;
//                    }
//                } else if (mMode != null) {
//                    mMode.finish();
//                    isActionModeShowing = false;
//                }
//
//                // Set action mode title
//                if (mMode != null)
//                    mMode.setTitle(String.valueOf(checkedCount));
//
//                notifyDataSetChanged();
//
//            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub
                Log.e(TAG, "onAnimationRepeat");
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                // TODO Auto-generated method stub
                Log.e(TAG, "onAnimationEnd");
            }
        };

        flipAnim1.setAnimationListener(animListner);
        flipAnim2.setAnimationListener(animListner);

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
            holder.imageView.setBackgroundDrawable(mDrawableBuilder.build(" ", 0xff616161));
            holder.imageView.setImageResource(R.drawable.check_sm);
            holder.view.setBackgroundColor(HIGHLIGHT_COLOR);
            //holder.checkIcon.setVisibility(View.VISIBLE);
        } else {
            TextDrawable drawable = mDrawableBuilder.build(String.valueOf(item.data.charAt(0)), mColorGenerator.getColor(item.data));
            holder.imageView.setImageDrawable(drawable);
            holder.view.setBackgroundColor(Color.TRANSPARENT);
            //holder.checkIcon.setVisibility(View.GONE);
        }
    }



    }

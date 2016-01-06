###TextDrawable 实现 Gmail邮箱选中翻转效果

作者: shrimpcolo Email: shrimpcolo@gmail.com

本项目Fork自amulyakhare/TextDrawable，并在其基础上实现 类似Gmail邮箱选中翻转效果。
主要目的如下:
- 学习使用TextDrawable
- 基于TextDrawable实现Gmail邮箱选中翻转效果

###效果图
<p align="center">
<img src = "https://github.com/tancolo/TextDrawable/blob/dev/screens/Screenshot_05.png" width="350"/>
<img src = "https://github.com/tancolo/TextDrawable/blob/dev/screens/Screenshot_06.png" width="350"/>
</p>

###目录结构说明
- library       TextDrawable源码库
- sample        原作者的sample
- flipview      本人实现的Demo
- sample-apk    apk文件目录

master分支是fork原始分支，不做任何修改，仅仅是为同步作者后续可能的修改。
dev分支是个人学习分支，涉及：
- FlipView说明
- 基础代码搭建
- 如何集成TextDrawable到flipview独立工程中.
- 如何实现TextDrawable的自动翻转，达到类似Gmail翻转效果.
- 如何使用ActionMode添加actionbar，达到多选并显示数字.
- 如何修改ActionBar 背景颜色，title字体颜色, 添加ActionBar Menu.

###集成环境说明
- Win10 OS
- Android Studio1.4
- compileSdkVersion 23, minSdkVersion 15.
- gradle 2.2

###FlipView说明
先说明下Demo FlipView的代码结构。

-  HomeActivity是主Activity， 其中包含了ListView。
- SampleAdapter 自定义Adapter，用于显示Item，动画翻转效果。

###如何集成TextDrawable
TextDrawable是原作者写的小库，用各种图形显示文本信息。可以直接使用库文件代码或是使用Android Studio的
依赖集成。
在build.gradle（flipview文件夹）中添加如下内容：
```
    //add maven repositories
    repositories{
        maven {
            url 'http://dl.bintray.com/amulyakhare/maven'
        }
    }

    dependencies {
        compile fileTree(dir: 'libs', include: ['*.jar'])
        compile 'com.android.support:appcompat-v7:23.0.1'
        compile 'com.android.support:design:23.0.1'
        //add textdrawable
        compile 'com.amulyakhare:com.amulyakhare.textdrawable:1.0.1'
    }
```

###基础代码搭建
这里复用sample中的SampleAdapter
```
	public class SampleAdapter extends BaseAdapter {
		private static final int HIGHLIGHT_COLOR = 0x999be6ff;

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

```
基本的listview adapter模型。需要指出，使用了TextDrawable
```
	import com.amulyakhare.textdrawable.TextDrawable;
	import com.amulyakhare.textdrawable.util.ColorGenerator;
```
使用方法简单。需要决定Item imageView最左边图形形状以及随机的颜色, Demo使用圆形.
```
	mColorGenerator = ColorGenerator.MATERIAL;
	mDrawableBuilder = TextDrawable.builder().round();
```

原作者是没有实现翻转动画效果的，仅仅使用2张图片轮替。
```
	<FrameLayout
        android:layout_marginRight="16dp"
        android:layout_width="60dp"
        android:layout_height="60dp">

        <ImageView
            android:layout_width="60dp"
            android:layout_height="60dp"
            android:id="@+id/imageView"/>

        <ImageView
            android:visibility="gone"
            android:layout_width="60dp"
            android:layout_height="60dp"
            android:src="@drawable/check_sm"
            android:id="@+id/check_icon"/>
    </FrameLayout>

    <TextView
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="1"
        android:gravity="center_vertical"
        android:id="@+id/textView"/>
```
我的实现方式只需要一个imageview，让该imageview实现翻转。check_icon这个view没有再使用。

###类gmail动画翻转
仔细查看gmail翻转动画，发现选中和取消的动画不一样，要达到跟gmail同样的效果，使用了2个scale动画。
但是在实际测试过程中，发现达不到gmail那么好的效果。先把粗糙的动画展示出来，如有高手能实现，一定告知本人。
不胜感激！

####动画文件

flip_anim_from_middle.xml
```
	<?xml version="1.0" encoding="utf-8"?>
	<scale xmlns:android="http://schemas.android.com/apk/res/android"
		android:duration="200"
		android:fromXScale="0.0"
		android:fromYScale="1.0"
		android:pivotX="50%"
		android:pivotY="50%"
		android:toXScale="1.0"
		android:toYScale="1.0" />
```

flip_anim_to_middle.xml
```
	<?xml version="1.0" encoding="utf-8"?>
	<scale xmlns:android="http://schemas.android.com/apk/res/android"
		android:fromXScale="1.0"
		android:toXScale="0.0"
		android:pivotX="50%"
		android:fromYScale="1.0"
		android:toYScale="1.0"
		android:pivotY="50%"
		android:duration="150" />
```

上述2个动画文件都是scale动画(渐变尺寸伸缩动画效果)
使用x，y 4种属性:
```
	fromXScale 属性 为动画起始时 X坐标上的伸缩尺寸
	toXScale   属性 为动画结束时 X坐标上的伸缩尺寸

	fromYScale 属性 为动画起始时Y坐标上的伸缩尺寸
    toYScale   属性 为动画结束时Y坐标上的伸缩尺寸

			0.0表示收缩到没有
            1.0表示正常无伸缩
            值小于1.0表示收缩
            值大于1.0表示放大
```
```
	pivotX    属性 为动画相对于物件的X坐标的开始位置
    pivotY    属性 为动画相对于物件的Y坐标的开始位置

			以上两个属性值 从0%-100%中取值
            50%为物件的X或Y方向坐标上的中点位置
```
```
	长整型值：
            duration  属性 为动画持续时间
            说明:   时间以毫秒为单位
```
详细的参考blog: http://blog.csdn.net/ithomer/article/details/7523328

####动画代码
```
	初始化SampleAdapter时需要添加
	flipAnim1 = AnimationUtils.loadAnimation(mContext, R.anim.flip_anim_to_middle);
    flipAnim2 = AnimationUtils.loadAnimation(mContext, R.anim.flip_anim_from_middle);
```

在getview中clickListener中需要添加
```
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
```

```
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
                }
            }
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

```

###添加ActionBar
Demo flipview是基于SDK 23生成的， 本身就自带了toolbar。
现在需要添加actionbar。达到选中Item，在ActionBar上显示选中的条数，以及操作的菜单。

```
	private ActionModeTop mActionModeTop;
	... ...
	protected void onCreate(Bundle savedInstanceState) {
		...
		//add action mode
        mActionModeTop = new ActionModeTop(this);
	}
```

```
	public class ActionModeTop implements ActionMode.Callback {

        Context ctx;

        public ActionModeTop(Context context) {
            this.ctx = context;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_selection, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.edit_entry:
                    break;
                case R.id.delete_entry:
                    break;
                case R.id.finish_it:
                    mode.finish();
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }
```
以上代码创建ActionMode，并创建了菜单。
在onCreate中传入到SampleAdapter, 在其中进行处理。

SmapleAdapter.java
```
	private ActionMode mMode;
    private HomeActivity.ActionModeTop mActModeCallback;
	... ...

	SampleAdapter(Context context, HomeActivity.ActionModeTop callback) {
        ... ...
        mActModeCallback = callback;
	}
```

关键代码段
```
			// Set selected count
            private void setCount() {
                if (curListData.isChecked) {
                    checkedCount++;
                } else {
                    if (checkedCount != 0) {
                        checkedCount--;
                    }
                }
            }


            // Show/Hide action mode
            private void setActionMode() {
                Log.e(TAG, "setActionMode()-> checkedCount: " + checkedCount);

                if (checkedCount > 0) {
                    if (!isActionModeShowing) {
                        mMode = ((HomeActivity)mContext).startActionMode(mActModeCallback);
                        isActionModeShowing = true;
                    }
                } else if (mMode != null) {
                    mMode.finish();
                    isActionModeShowing = false;
                }

                // Set action mode title
                if (mMode != null)
                    mMode.setTitle(String.valueOf(checkedCount));

                notifyDataSetChanged();

            }
```

需要注意点，本例子中ActionBar 和 ToolBar并存，导致在选中item后，会同时出现。
解决方法， 在主题中加入
```
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        ... ...
        <item name="windowActionModeOverlay">true</item>
    </style>
```

###修改ActionBar


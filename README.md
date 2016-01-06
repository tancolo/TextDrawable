###TextDrawable 实现 Gmail邮箱选中翻转效果

作者: shrimpcolo Email: shrimpcolo@gmail.com

本项目Fork自amulyakhare/TextDrawable，并在其基础上实现 类似Gmail邮箱选中翻转效果。
主要目的如下:
- 学习使用TextDrawable
- 基于TextDrawable实现Gmail邮箱选中翻转效果

###效果图
To be Implemented.

###目录结构说明
- library       TextDrawable源码库
- sample        原作者的sample
- flipview      本人实现的Demo
- sample-apk    apk文件目录

master分支是fork原始分支，不做任何修改，仅仅是为同步作者后续可能的修改。
dev分支是个人学习分支，涉及：
- 如何集成TextDrawable到flipview独立工程中.
- 如何实现TextDrawable的自动翻转，达到类似Gmail翻转效果.
- 如何使用ActionMode添加actionbar，达到多选并显示数字.
- 如何修改ActionBar 背景颜色，title字体颜色, 添加ActionBar Menu.

###集成环境说明
- Android Studio1.4
- compileSdkVersion 23, minSdkVersion 15.
- gradle 2.2

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


###类gmail动画翻转

###添加ActionBar

###修改ActionBar

#### Import with Gradle:

```groovy
repositories{
    maven {
        url 'http://dl.bintray.com/amulyakhare/maven'
    }
}

dependencies {
    compile 'com.amulyakhare:com.amulyakhare.textdrawable:1.0.1'
}
```

####1. Create simple tile:

<p align="center"><img src ="https://github.com/amulyakhare/TextDrawable/blob/master/screens/screen3.png"/>
</p>

```xml
<ImageView android:layout_width="60dp"
	       android:layout_height="60dp"
	       android:id="@+id/image_view"/>
```
**Note:** Specify width/height for the `ImageView` and the `drawable` will auto-scale to fit the size.
```java
TextDrawable drawable = TextDrawable.builder()
                .buildRect("A", Color.RED);

ImageView image = (ImageView) findViewById(R.id.image_view);
image.setImageDrawable(drawable);
```

####2. Create rounded corner or circular tiles:

<p align="center"><img src ="https://github.com/amulyakhare/TextDrawable/blob/master/screens/screen6.png"/>
</p>

```java
TextDrawable drawable1 = TextDrawable.builder()
                .buildRoundRect("A", Color.RED, 10); // radius in px

TextDrawable drawable2 = TextDrawable.builder()
                .buildRound("A", Color.RED);
```

####3. Add border:

<p align="center"><img src ="https://github.com/amulyakhare/TextDrawable/blob/master/screens/screen5.png"/>
</p>

```java
TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                    .withBorder(4) /* thickness in px */
                .endConfig()
                .buildRoundRect("A", Color.RED, 10);
```

####4. Modify font style:

```java
TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
	                .textColor(Color.BLACK)
                    .useFont(Typeface.DEFAULT)
                    .fontSize(30) /* size in px */
                    .bold()
                    .toUpperCase()
                .endConfig()
                .buildRect("a", Color.RED)
```

####5. Built-in color generator:

```java
ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
int color1 = generator.getRandomColor();
// generate color based on a key (same key returns the same color), useful for list/grid views
int color2 = generator.getColor("user@gmail.com")

// declare the builder object once.
TextDrawable.IBuilder builder = TextDrawable.builder()
				.beginConfig()
					.withBorder(4)
				.endConfig()
				.rect();

// reuse the builder specs to create multiple drawables
TextDrawable ic1 = builder.build("A", color1);
TextDrawable ic2 = builder.build("B", color2);
``` 

####6. Specify the width / height:

```xml
<ImageView android:layout_width="wrap_content"
	       android:layout_height="wrap_content"
	       android:id="@+id/image_view"/>
```
**Note:**  The `ImageView` could use `wrap_content` width/height. You could set the width/height of the `drawable` using code.

```java
TextDrawable drawable = TextDrawable.builder()
				.beginConfig()
					.width(60)  // width in px
					.height(60) // height in px
				.endConfig()
                .buildRect("A", Color.RED);

ImageView image = (ImageView) findViewById(R.id.image_view);
image.setImageDrawable(drawable);
```

####7. Other features:

1. Mix-match with other drawables. Use it in conjunction with `LayerDrawable`, `InsetDrawable`, `AnimationDrawable`, `TransitionDrawable` etc.

2. Compatible with other views (not just `ImageView`). Use it as background drawable, compound drawable for `TextView`, `Button` etc.

3. Use multiple letters or `unicode` characters to create interesting tiles. 

<p align="center"><img src ="https://github.com/amulyakhare/TextDrawable/blob/master/screens/screen7.png" width="350"/></p>

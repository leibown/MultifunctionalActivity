

# MultifunctionalActivity

#### 一个带有透明状态栏的多功能Activity,可以在开发中当做BaseActivity来用

#### 1.透明状态栏

#### 2.设置状态栏颜色

#### 3.设置状态栏图片背景

#### 4.多状态的切换(Loading,Empty,Retry) 

## 导入方式

首先将它添加到你的根目录build.gradle中（如果已经有了就无需重复添加了）：

```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

然后在app目录下的build.gradle中添加：

```java
	dependencies {
	        compile 'com.github.leibown:MultifunctionalActivity:1.1'
	}
```

## 效果图

#### 修改头部**背景颜色**或**背景图片**:

<div width=100%>

<img src="https://raw.githubusercontent.com/leibown/MultifunctionalActivity/master/img/change_color_statusbar_bg.gif" width="250" float="left"></img>

<img src="https://raw.githubusercontent.com/leibown/MultifunctionalActivity/master/img/change_pic_statusbar_bg.gif" width="250" float="left"></img>

</div>

#### 修改**状态栏图**标和**文字**的颜色：

<img src="https://raw.githubusercontent.com/leibown/MultifunctionalActivity/master/img/change_mode_statusbar.gif" width="250"></img>

#### 变换各种加载状态：

<img src="https://raw.githubusercontent.com/leibown/MultifunctionalActivity/master/img/show_status.gif" width="250"></img>

## 使用方法

> 这个透明ActionBar的实现方式是让状态栏背景色透明且不占位置，就是我们自己的布局能放到状态栏下面去。

除了需要像上面那样导入，还需要做以下操作：

- 修改项目AndroidManifest里的application的theme属性，改为“@style/Theme.AppCompat.NoActionBar”

  ```java
   <application
          android:allowBackup="true"
          android:icon="@mipmap/ic_launcher"
          android:label="@string/app_name"
          android:supportsRtl="true"
          android:theme="@style/Theme.AppCompat.NoActionBar">
          <activity android:name=".MainActivity">
              <intent-filter>
                  <action android:name="android.intent.action.MAIN" />

                  <category android:name="android.intent.category.LAUNCHER" />
              </intent-filter>
          </activity>
      </application>
  ```

- 建议自定义一个BaseActivity来继承MultifunctionalActivity，然后做一些初始化操作:

  > 如果直接继承MultifunctionalActivity，需要每个Activity都执行一些初始化操作。

  ```java

  public abstract class BaseActivity extends MultifunctionalActivity {

      /**
       * 如果此方法返回false，那切换状态相关方法就会失效
       * @return
       */
      @Override
      public boolean isNeedStatusView() {
          return true;
      }

      @Override
      public void bindViews(Bundle savedInstanceState) {
          //设置没有数据时的提示文字
          setEmptyText("暂时没有数据返回");

          //设置正在加载时数据时的提示文字
          setLoadingText("正在玩命加载中");

          //设置加载失败，重试时的提示文字
          setReTryText("加载失败请稍后再试");

          //设置各种状态时中间显示的图片
          //setStatusImageViewImageResource(R.drawable.android);
        
        	View statusView = View.inflate(this, R.layout.layout_status, null);
          //设置各种状态时的View
          setStatusView(statusView, R.id.tv_status_content);
        
        	View view = View.inflate(this, R.layout.layout_actionbar, null);
       	//设置ActionBar，传入ActionBar布局
       	setActionBar(view);
        	view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContent();
            	}
        	});
    	}
  }
  ```


  MultifunctionalActivity是一个抽象类，里面含有几个抽象方法：	

  ```java
   	 /**
         * 是否需要装载能显示各种状态的ViewGroup
         */
        public abstract boolean isNeedStatusView();

        /**
         * 获取Activity布局文件Id
         *
         * @return
         */
        public abstract int getResId();

        /**
         * 子类初始化view的方法
         * (在MultifunctionalActivity的onCreat方法里面调用，所以用于一些初始化操作)
         *
         * @param savedInstanceState
         */
        public abstract void bindViews(Bundle savedInstanceState);
  ```

    这个地方我使用的BaseActivity也是抽象类，但是只实现了isNeedStatusView()和bindViews()方法。getResId()方法必须交给子类Activity来实现，因为这个是获取子类Activity布局文件Id用的。例如：	

  ```java
    public class MainActivity extends BaseActivity {

        @Override
        public int getResId() {
            return R.layout.activity_main;
        }

        @Override
        public void bindViews(Bundle savedInstanceState) {
            super.bindViews(savedInstanceState);
          	//调用setActionBar()过后也需要调用此方法才能显示出来
          	showActionBar();
        }
      
        @Override
        public void reTry() {
            Toast.makeText(this, "点击了重试", Toast.LENGTH_SHORT).show();
        }
    }
  ```

## api

设置相关：

```java
//设置ActionBar，传入ActionBar布局
void setActionBar(View actionBar);

//设置显示ActionBar
void showActionBar();

//设置ActionBar的背景颜色
void setActionBarBackgroudColor(int color);

//设置ActionBar的背景图片
void setActionBarBackgroudResource(int resId);

//设置状态栏图标和文字为黑色(Android 6.0以上)
void setStatusBarDarkMode();

//恢复状态栏图标和文字(Android 6.0以上)
void restoreStatusBarMode();
```

6.0的动态权限：

```java
/**
  * 检查是否缺少权限
  *
  * @param listener		 回调接口
  * @param permissions   选线字符串数组
  */
void checkPermissions(PermissionListener listener, String... permissions);
```

切换状态相关：

```java
//自定义Loading,ReTry,Empty这种状态时显示的View
void setStatusView(View view);

/**
  * 自定义Loading,ReTry,Empty这种状态时显示的View
  *
  * @param view          能装各种状态的View
  * @param textViewResId 来标识各种状态的TextView
  */
void setStatusView(View view, int textViewResId);


//设置没有数据时的提示文字
void setEmptyText(String emptyText);

//设置正在加载时数据时的提示文字
void setLoadingText(String loadingText);

//设置加载失败，重试时的提示文字
void setReTryText(String reTryText);

//设置各种状态时中间显示的图片
void setStatusImageViewImageResource(int resId);

//显示内容
void showContent();
//显示加载中状态
void showLoading();
//显示空数据状态
void showEmpty();
//显示加载失败,重试状态
void showRetry();
```

> 切换状态的使用场景如下图所示（本来想用文字总结的，发现本人表达能力确实有点差）：![](https://raw.githubusercontent.com/leibown/MultifunctionalActivity/master/img/status_img.png)
>
> 里面的retry()方法需要你在Activity里面重写，那样点击重试时就会执行这个reTry()方法，就像上面代码中的MainActivity一样。

还有些具体的用法可以去看Demo中BaseActivity和MainActivity中的使用。

要是遇到BUG、哪里不清楚、觉得我写得不合理的地方欢迎大家提Issues，我会及时解决。非常感谢 : )
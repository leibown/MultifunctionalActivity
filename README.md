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
	        compile 'com.github.leibown:MultifunctionalActivity:1.0'
	}
```

## 效果图

修改头部**背景颜色**或**背景图片**:

<div width=100%>

<img src="https://raw.githubusercontent.com/leibown/MultifunctionalActivity/master/img/change_color_statusbar_bg.gif" width="250" float="left"></img>

<img src="https://raw.githubusercontent.com/leibown/MultifunctionalActivity/master/img/change_pic_statusbar_bg.gif" width="250" float="left"></img>

</div>

修改**状态栏图**标和**文字**的颜色：

<img src="https://raw.githubusercontent.com/leibown/MultifunctionalActivity/master/img/change_mode_statusbar.gif" width="250"></img>

</div>

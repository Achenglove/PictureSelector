### MPictureSelector
这是一个图片选择器，包括单选，多选，是否开启拍照功能，是否可编辑，是否显示加号，是否允许拖拽排序等功能，还包括九宫格图片显示。
[![](https://jitpack.io/v/Achenglove/PictureSelector.svg)](https://jitpack.io/#Achenglove/PictureSelector)
### 基本使用

#### 1.添加 Gradle 依赖


```Acheng
dependencies {
	        implementation 'com.github.Achenglove:PictureSelector:v1.0.9'
	}
```

### 布局文件使用
```xml
<com.ccr.achenglibrary.photopicker.widget.CCRSortableNinePhotoLayout
            android:id="@+id/snpl_moment_add_photos"
            style="@style/WrapWrap"
            android:layout_margin="10dp"
            app:bga_snpl_deleteDrawable="@mipmap/bga_pp_ic_delete"
            app:bga_snpl_deleteDrawableOverlapQuarter="false"
            app:bga_snpl_editable="true"
            app:bga_snpl_itemCornerRadius="0dp"
            app:bga_snpl_itemSpanCount="3"
            app:bga_snpl_itemWhiteSpacing="4dp"
            app:bga_snpl_itemWidth="0dp"
            app:bga_snpl_maxItemCount="9"
            app:bga_snpl_otherWhiteSpacing="100dp"
            app:bga_snpl_placeholderDrawable="@mipmap/bga_pp_ic_holder_dark"
            app:bga_snpl_plusDrawable="@mipmap/bga_pp_ic_plus"
            app:bga_snpl_plusEnable="true"
            app:bga_snpl_sortable="true"/>

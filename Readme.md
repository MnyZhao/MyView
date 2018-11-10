### ChargeView
##### 间断进度条 类似电池充电效果
```xml
 <declare-styleable name="ChargeView">
        <!--进度条个数-->
        <attr name="cv_item_count" format="integer" />
        <!--间隔宽度-->
        <attr name="cv_split_width" format="dimension" />
        <!--进度条宽度-->
        <attr name="cv_item_width" format="dimension" />
        <!--进度条高度-->
        <attr name="cv_item_height" format="dimension" />
        <!--圆角半径-->
        <attr name="cv_border_cornor_radius" format="dimension" />
        <!--充电中开始的颜色-->
        <attr name="cv_item_charge_start_color" format="color" />
        <!--充电结束时的颜色-->
        <attr name="cv_item_charge_end_color" format="color" />
        <!--充电内模块的背景色，未充电的颜色-->
        <attr name="cv_item_unCharge_color" format="color" />
        <!--动画时长-->
        <attr name="cv_anim_duration" format="integer" />
        <!--progrss-->
        <attr name="cv_progress" format="integer" />
        <!--边框线的宽度-->
        <attr name="cv_storke_width" format="dimension" />
    </declare-styleable>
    
<myview.com.myview.view.ChargeView
        android:id="@+id/cv_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        app:cv_anim_duration="3"
        app:cv_item_charge_end_color="#95D8FF"
        app:cv_item_charge_start_color="#0E74A7"
        app:cv_item_count="20"
        app:cv_item_height="16dp"
        app:cv_border_cornor_radius="2dp"
        app:cv_item_width="6dp" />
```
###ClearEditTextHighlight 左侧图标高亮带删除edittext
- attr_edittext.xml 高亮控件属性
- dimens_pwd_edit.xml 高亮输入框 高亮密码框dimens
###ClearEditTextPwd 左侧图标高亮带删除带密码可见密码框 组合控件左侧为上面的高亮框 右侧为两个mageview 所以修改时只要按照下面描述修改即可

- 两个类似脱胎与一个高亮edittext 使用时只需要
  更改styles_edittext.xml中的input_edittext_style中的属性即可
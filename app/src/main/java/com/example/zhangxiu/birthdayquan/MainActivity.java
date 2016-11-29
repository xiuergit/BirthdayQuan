package com.example.zhangxiu.birthdayquan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.zhangxiu.birthdayquan.Love.Fragment.LoveFragment;
import com.example.zhangxiu.birthdayquan.Memory.Fragment.MemoryFragment;
import com.example.zhangxiu.birthdayquan.Tool.Fragment.ToolFragment;
import com.example.zhangxiu.birthdayquan.Widget.NavigationBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener{
    private  static String  TAG="MainActivity";

    @ViewInject(R.id.myBottom)
    private BottomNavigationBar  mBottomBar;

    @ViewInject(R.id.love_navigation)
    NavigationBar mTopBar;

    private ArrayList<Fragment>mFragments=new ArrayList<>(3);


    public  void initTopNav(){
        mTopBar.setBackgroundColor(Color.BLACK);
        mTopBar.setTitleItem("美好回忆");




       // mTopBar.setLeftItem(test);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initUI();
        mFragments.add(LoveFragment.newInstance("首页"));
        mFragments.add(MemoryFragment.newInstance("纪念"));
        mFragments.add(ToolFragment.newInstance("工具"));
        setFragment(0);
        initTopNav();

        View decorview=getWindow().getDecorView();//FrameLayout类型的ViewGroup
        //decorview.setBackgroundColor(Color.BLUE);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            ArrayList<View>views=new ArrayList<>();
//            TextView textView;
//            int colors[]={Color.RED,Color.CYAN,Color.GREEN,Color.YELLOW,Color.BLUE};
//            for (int i=0;i<5;i++){
//                textView=new TextView(this);
//                FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(100,100);
//                textView.setBackgroundColor(colors[i]);
//                textView.setText(i+"test");
//            }
//
//            decorview.addChildrenForAccessibility(views);
//
//        }


    }

    public  void initUI(){
        mBottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        mBottomBar.addItem(new BottomNavigationItem
                (com.ashokvarma.bottomnavigation.R.drawable.abc_btn_check_to_on_mtrl_000,"首页"));
        mBottomBar.addItem(new BottomNavigationItem(com.ashokvarma.bottomnavigation.R.drawable.abc_ic_star_black_16dp,"纪念"));
        mBottomBar.addItem(new BottomNavigationItem(android.R.drawable.ic_menu_view,"工具")).initialise();
        mBottomBar.setTabSelectedListener(this);


    }


   //当前页面显示的Fragment
    public void setFragment(int index){
        BaseFragment fragment= (BaseFragment) mFragments.get(index);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_main_content, fragment);
        transaction.commit();
    }

    //BottomNavigationBar.OnTabSelectedListener
    //底部菜单栏选中
    @Override
    public void onTabSelected(int position) {
        setFragment(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}







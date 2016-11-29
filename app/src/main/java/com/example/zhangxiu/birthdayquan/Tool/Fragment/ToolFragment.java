package com.example.zhangxiu.birthdayquan.Tool.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zhangxiu.birthdayquan.BaseFragment;
import com.example.zhangxiu.birthdayquan.R;
import com.example.zhangxiu.birthdayquan.Tool.Adapter.ToolAdapter;
import com.example.zhangxiu.birthdayquan.Tool.Model.ToolMenuModel;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhangxiu on 2016/10/24.
 */
public class ToolFragment extends BaseFragment{

    private  static  String TAG="ToolFragment";
   @ViewInject(R.id.rv_tool_list)
    private RecyclerView mList;

    private   ArrayList<ToolMenuModel>arrayList=new ArrayList<ToolMenuModel>();

    public ToolFragment(){
       
   }
    public static ToolFragment newInstance(String title) {
        ToolFragment fragment = new ToolFragment();
        Bundle args = new Bundle();
        args.putString(BaseFragment.BASE_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tool, container, false);
        x.view().inject(this,view);

        initUI();
        return view;
    }

    public void addModelforArray(String title,int imagID){
        ToolMenuModel model=new ToolMenuModel(imagID,title);
        arrayList.add(model);
    }
    public  void initUI(){


         addModelforArray("星座运势",R.drawable.xingzuopeidui);
         addModelforArray("开心一笑",R.drawable.xiaohua);
         addModelforArray("周公解梦",R.drawable.jiemeng);


        ToolAdapter toolAdapter=new ToolAdapter(arrayList,getActivity());
        toolAdapter.setmOnItemClickListener(new ToolAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(),"pos"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mList.setAdapter(toolAdapter);
        mList.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                for(int i=0;i<parent.getChildCount();i++){

                   Bitmap bitmap= Bitmap.createBitmap(100,2, Bitmap.Config.ARGB_8888);
//                    Canvas canvas=new Canvas(bitmap);
                    Paint paint=new Paint();
                    paint.setColor(Color.BLUE);
                    paint.setAntiAlias(true);
                    paint.setStrokeWidth(2);
                    c.drawLine(2,0,2,2,paint);
                }

               // mList.draw(canvas);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

                outRect.set(6,6,6,6);
            }
        });



    }
}

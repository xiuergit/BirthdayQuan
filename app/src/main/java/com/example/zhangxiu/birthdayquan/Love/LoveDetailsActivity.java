package com.example.zhangxiu.birthdayquan.Love;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.birthdaymodule.Adapter.BaseAdapter;
import com.example.zhangxiu.birthdayquan.Animal.RotateAnim3d;
import com.example.zhangxiu.birthdayquan.BaseActivity;
import com.example.zhangxiu.birthdayquan.Love.Adapter.LoveDetailsAdapter;
import com.example.zhangxiu.birthdayquan.Love.Fragment.LoveFragment;
import com.example.zhangxiu.birthdayquan.Love.Model.LoveCellModel;
import com.example.zhangxiu.birthdayquan.R;
import com.example.zhangxiu.birthdayquan.Widget.NavigationBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_love_details)
public class LoveDetailsActivity extends BaseActivity implements GestureDetector.OnGestureListener{

    private static  String TAG="LoveDetailsActivity";

    private  static  String  POISTION="poistion";

    private  final static int PlayFinish=100, UnPlayFinish=101,
            PlayPause=102;

    private  boolean mIsPlay;


    private  int mPosition=0;

    @ViewInject(R.id.vs_detailsView)
    private ViewSwitcher mDetailsAnimRootView;//播放动画的view

    @ViewInject(R.id.rv_detailsView)
    private RecyclerView mDetailsListView;//播放完成

    private GestureDetectorCompat mSlideGesture;//手势


    @ViewInject(R.id.love_nav)
    private NavigationBar mTopBar;//导航条

    private Handler  mHandler;//跟新UI

    private  LoveCellModel model;


    //定时线程
    TimerPlayThread mTimerThread;








    /**
     * 显示下一个图片
     * @param position
     */
   public  void  showNext(int position,ViewSwitcher viewSwitcher,LoveCellModel model){
       String imageUrl = model.getImageUrls().get(position);
       View view = viewSwitcher.getNextView();

       TextView content=(TextView)view.findViewById(R.id.tv_love_details_content);
       TextView title = (TextView) view.findViewById(R.id.tv_love_details_title);

       ImageView image=(ImageView)view.findViewById(R.id.iv_love_details);

       title.setText(model.getName());
       content.setText(model.getDescribe());

       Typeface typeface=Typeface.createFromAsset(getAssets(),"华康娃娃体.TTF");

       content.setTypeface(typeface);

       x.image().bind(image,imageUrl);


       RotateAnim3d rotateAnim3d=new RotateAnim3d(0,120,0,0,30,true);
       rotateAnim3d.setDuration(4000);

       RotateAnim3d rotateAnim3dOut=new RotateAnim3d(-120,0,0,0,30,true);
       rotateAnim3dOut.setDuration(4000);
       viewSwitcher.setInAnimation(LoveDetailsActivity.this,R.anim.lovevs_enter_anim);

       viewSwitcher.setOutAnimation(LoveDetailsActivity.this,R.anim.lovevs_exit_anim);

      // viewSwitcher.setInAnimation(rotateAnim3d);
       //viewSwitcher.setOutAnimation(rotateAnim3dOut);
       viewSwitcher.showNext();
   }


    /**
     * 导航条的设置
     */
    public  void initNav(){

        mTopBar.setBackgroundColor(Color.BLACK);
        mTopBar.setLeftItem("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoveDetailsActivity.this.finish();
            }
        });
    }

    /**
     * 初始化
     */
    public void init(){


         //显示的数据
         model=getIntent().getParcelableExtra(LoveFragment.LoveDetails);
          //更新ui
          mHandler=new MyHandler();
          //定时更新
          mTimerThread= new  TimerPlayThread(true,mHandler,model,0);
          mTimerThread.mPlayState=UnPlayFinish;
          mTimerThread.start();

        //手势
        mSlideGesture=new  GestureDetectorCompat(this,this);

        mDetailsAnimRootView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                View view = RelativeLayout.inflate(LoveDetailsActivity.this,
                        R.layout.love_details_anim_vs, null);
                return view;
            }
        });


    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //导航
        initNav();

        init();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        this.mSlideGesture.onTouchEvent(event);
        return true;
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {



         mTimerThread.isPlay=!mTimerThread.isPlay;


        if(mTimerThread.isPlay){

            TimerPlayThread  mTimerThread= new  TimerPlayThread(true,mHandler,model,mPosition);
            mTimerThread.mPlayState=UnPlayFinish;
            mTimerThread.start();

        }else {


            mTimerThread.mPlayState=PlayPause;



        }
        mTimerThread.run();

         return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

        float  x=motionEvent.getX();
        float  x1=motionEvent1.getX();

        if(x1-x>0){

            showNext(mPosition+1,mDetailsAnimRootView,model);
        }
        else  if(x1-x<0){
            showNext(mPosition-1,mDetailsAnimRootView,model);
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }




    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
        mTimerThread.interrupt();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        mTimerThread.interrupt();
       // mHandler.removeCallbacks();
    }


    /***
     * 自动播放
     */
    public class  TimerPlayThread extends  Thread{

        /**
         *  是否播放
         */
        private  boolean isPlay;


        public int getmPlayState() {
            return mPlayState;
        }

        public void setmPlayState(int mPlayState) {
            this.mPlayState = mPlayState;
        }

        private  int mPlayState;


        /**
         * 发送消息，告知ui更新
         */
        private   Handler  mHandler;

        /**
         * 播放的内容
         */
        private  LoveCellModel model;





        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        /**
         * 当前位置
         */
        private int position = 0;





        public TimerPlayThread(boolean isPlay, Handler mHandler, LoveCellModel model,int position) {
            this.isPlay = isPlay;
            this.mHandler = mHandler;
            this.model = model;
            this.position=position;
        }

        @Override
        public void run() {



            switch (mPlayState){
                case UnPlayFinish:
                    while (isPlay) {
                        //没有播放完
                        if (position < model.getImageUrls().size()-1) {

                            Log.i(TAG, "run: "+position);
                                isPlay= true;

                            Message message=new Message();
                            message=new Message();
                            message.what=UnPlayFinish;


                            Bundle  bundle=new Bundle();
                            bundle.putInt(POISTION,position);
                            message.setData(bundle);

                            //Log.i(TAG, "run: "+message.what+":"+mHandler);
                            mHandler.sendMessage(message);
                            //this.mHandler.sendMessageAtFrontOfQueue(message);
                            position++;
                            mPosition=position%model.getImageUrls().size();
                        }
                        //播放完了
                        else {

                            Message message=new Message();
                            message.what=PlayFinish;
                            mHandler.sendMessage(message);
                            isPlay =false ;

                        }
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
//

                  break;

                case PlayPause:
                    mPosition=position;
                    isPlay =false ;
                    Message message=new Message();
                    message.what=PlayPause;
                    mHandler.sendMessage(message);

                    break;
                default:
                    break;

            }








        }



    }




    public  class  MyHandler extends  Handler{


        public MyHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            switch (msg.what){

                case  PlayFinish:
                    Toast.makeText(LoveDetailsActivity.this,"播放完成", Toast.LENGTH_LONG).show();
                    mDetailsAnimRootView.setAlpha(0);
                    LoveDetailsAdapter adapter=new LoveDetailsAdapter(LoveDetailsActivity.this,model.getImageUrls());

                    adapter.setOnClickLister(new BaseAdapter.onClickLister() {
                        @Override
                        public void onItemClickLister(View itemView, int position) {
                            Log.i(TAG, "onItemClickLister: ");


                            View view= LinearLayout.inflate(LoveDetailsActivity.this,
                                    R.layout.imageview,null);

                            ImageView imageView=(ImageView)view.findViewById(R.id.iv_love_big);

                            LinearLayout.LayoutParams params1=
                                    new LinearLayout.LayoutParams(screenRect()[0]-50,
                                            (int)(screenRect()[1]*0.6));
                            imageView.setLayoutParams(params1);


//                            Animation animation=new ScaleAnimation(itemView.getX(),
//                                    screenRect()[0]/2,itemView.getY(),screenRect()[1]/2);
//                            animation.setDuration(50000);
//                            animation.setFillAfter(true);
//                            imageView.setAnimation(animation);




                            x.image().bind(imageView,model.getImageUrls().get(position));

                            AlertDialog dialog= new AlertDialog.Builder(LoveDetailsActivity.this).
                                    create();
                            dialog.setView(view);
                            Window dialogWindow=dialog.getWindow();
                            dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
                           // dialogWindow.setBackgroundDrawableResource(R.drawable.love2);

                            WindowManager.LayoutParams params=dialogWindow.getAttributes();

//                            params.x=0;
//                            params.y=0;
                            params.width=(int) (screenRect()[0]-50);
                            params.height=(int)(screenRect()[1]*0.6);
                            dialog.getWindow().setAttributes(params);

                            dialog.show();


                        }

                        @Override
                        public void onItemLongClickLister(View itemView, int position) {

                        }
                    });
                    mDetailsListView.setAdapter(adapter);

                    mDetailsListView.setLayoutManager(new GridLayoutManager(LoveDetailsActivity.this,2));

                    break;

                case  PlayPause:
                    final ImageButton imageButton=new ImageButton(LoveDetailsActivity.this);

                    imageButton.setAdjustViewBounds(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imageButton.setImageTintMode(PorterDuff.Mode.CLEAR);
                    }

                    imageButton.setBackgroundResource(R.drawable.playorpause);

                    imageButton.setBackgroundColor(Color.alpha(0));
                    imageButton.setFocusable(true);


                    break;

                case  UnPlayFinish:
                    Log.i(TAG, "handleMessage: "+"-----继续播放->");
                    mDetailsAnimRootView.setAlpha(1);
                    //更新ui
                    Bundle bundle=msg.getData();
                    int position= bundle.getInt(POISTION);
                    showNext(position,mDetailsAnimRootView,model);
                    break;

                default:
                    break;


            }
        }
    }

}

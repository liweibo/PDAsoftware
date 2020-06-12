package com.crrc.pdasoftware.wuxianzhuanchu.all.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.crrc.pdasoftware.MyApplication;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.wuxianzhuanchu.all.utils.ItemChooseDataFail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


public class FailDownAdapter extends BaseAdapter {
    MyApplication globalvaradapter ;
    // 用来控制CheckBox的选中状况
    public static HashMap<Integer, Boolean> isSelected;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;
    public  boolean[] checks; //用于保存checkBox的选择状态
    ViewHolder holder = new ViewHolder();
    ConcurrentLinkedQueue failQueue ;
    List<String> failQueueToList = new ArrayList<>();
    // 构造器
    public FailDownAdapter(Context context,
                           MyApplication globalvaradapter,
                           ConcurrentLinkedQueue failQueue) {
        this.context = context;
        this.failQueue = failQueue;

        this.globalvaradapter = globalvaradapter;
        inflater = LayoutInflater.from(context);
        checks = new boolean[failQueue.size()];
        isSelected = new HashMap<>();
        // 初始化数据
        initDate();


    }

    // 初始化isSelected的数据
    private void initDate() {
        failQueueToList.clear();
        for (int i = 0; i < failQueue.size(); i++) {
            checks[i]=false;
        }


        Iterator<String> iterator = failQueue.iterator();
        while (iterator.hasNext()){
            String tp =iterator.next();
            failQueueToList.add(tp);
        }

    }

    public void clearBoolList() {
        if (checks!=null&&checks.length>0){
            for (int i = 0; i < checks.length; i++) {
                if (checks[i]) {
                    checks[i] = !checks[i];
                }
            }
        }
    }

    @Override
    public int getCount() {
        return failQueueToList.size();
    }

    @Override
    public Object getItem(int position) {
        return failQueueToList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.item_fail_list, null);
            holder.tv = convertView.findViewById(R.id.item_tvFail);
            holder.cb = convertView.findViewById(R.id.item_cbFail);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }


        // 设置list中TextView的显示
        holder.tv.setText(failQueueToList.get(position));//文件名

        //记录当前页面 文件夹的个数
        final int pos = position; //pos必须声明为final

        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalvaradapter.setCheckfail(-1);
            }
        });

        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                 @Override
                                                 public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                                                     new Thread(new Runnable() {
                                                         @Override
                                                         public void run() {
                                                             checks[pos] = b;
                                                             if (!b) {
                                                                 //没选中的item，则从记录中删除对应
                                                                 if (failQueueToList.size() > 0) {
                                                                     ItemChooseDataFail.removeFilePath(position,failQueueToList);
                                                                 }
                                                             } else {//选中的item，则记录他的index
                                                                 //记录前，先判断选择的是否是文件，是则记录。非文件不记录
                                                                 if (failQueueToList.size() > 0) {
                                                                     ItemChooseDataFail.addFilePath(failQueueToList.get(position));
                                                                 }
                                                             }

                                                         }
                                                     }).start();


                                                 }
                                             }
        );

//------------------------------全选------------------------------------------------------------------------------
        //全选的逻辑   activity中还要做处理
        int checkInt = globalvaradapter.getCheckfail();
        if (checkInt == 0) {//未选中
            holder.cb.setChecked(false);
            for (int i = 0; i < checks.length; i++) {
                checks[i] = false;

                if (failQueueToList.size() > 0) {
                    final int finalI = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ItemChooseDataFail.removeFilePath(finalI,failQueueToList);

                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


                }
            }
        }
        if (checkInt == 1) {//全选
            holder.cb.setChecked(true);
            for (int i = 0; i < checks.length; i++) {
                checks[i] = true;
                if (failQueueToList.size() > 0) {
                    final int finalI = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ItemChooseDataFail.addFilePath(failQueueToList.get(finalI));
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


                }
            }
        }


        if (checkInt == -1) {//
            holder.cb.setChecked(checks[pos]);
        }


        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }



    public static class ViewHolder {
        public TextView tv;
        public CheckBox cb;
    }


}
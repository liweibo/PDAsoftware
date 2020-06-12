package com.crrc.pdasoftware.wuxianzhuanchu.all.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crrc.pdasoftware.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.VerticalViewHolder> {

    Context context;
//    List<String> lists = new ArrayList<>();
    JSONArray lists = new JSONArray();
    List<PackFile> mData;

    public VerticalAdapter(Context context, JSONArray lists ){
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.file, viewGroup, false);
        return new VerticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VerticalViewHolder holder, final int i) {
        try {
            holder.textView.setText( lists.getJSONObject(i).getString("name") );
            holder.fileTime.setText( lists.getJSONObject(i).getString("time") );
            holder.checkBox.setChecked( lists.getJSONObject(i).getBoolean("check") );
            if( lists.getJSONObject(i).getString("state") == "1" ){
                holder.uploadstate.setVisibility( View.VISIBLE );
            }else{
                holder.uploadstate.setVisibility( View.INVISIBLE );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!buttonView.isPressed())return;
                try{
                    for (int j=0;j<lists.length();j++){
                        lists.getJSONObject(j).put("check", false);
                    }
                    lists.getJSONObject(i).put("check", true);
                }catch ( Exception e ){

                }
                //刷新适配器
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return lists.length();
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder{

        TextView textView, fileTime, uploadstate;
        CheckBox checkBox;

        public VerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.fileName);
            fileTime = itemView.findViewById(R.id.filetime);
            checkBox = itemView.findViewById(R.id.filecheck);
            uploadstate = itemView.findViewById(R.id.uploadstate);
        }
    }

    /**
     * 时间戳转换成字符窜
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}

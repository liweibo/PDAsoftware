package com.crrc.pdasoftware.wuxianzhuanchu.all.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crrc.pdasoftware.R;

import org.json.JSONArray;
import org.json.JSONException;

public class NavbarAdapter extends RecyclerView.Adapter<NavbarAdapter.NavbarViewHolder> {

    Context context;
    JSONArray lists = new JSONArray();
    private  OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void tClick(int i);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public NavbarAdapter(Context context, JSONArray lists){
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public NavbarAdapter.NavbarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.breadcumbs, viewGroup, false);
        return new NavbarAdapter.NavbarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavbarViewHolder navbarViewHolder, final int i) {
        try {
            navbarViewHolder.textView.setText( lists.getJSONObject(i).getString("name") );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ViewGroup.LayoutParams layoutParams = navbarViewHolder.itemView.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

        navbarViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println( mOnItemClickListener );

                if( mOnItemClickListener != null ){
                    mOnItemClickListener.tClick( i );
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.length();
    }

    public class NavbarViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;

        public NavbarViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.breadname);
            imageView = itemView.findViewById(R.id.breadimg);
        }
    }

}

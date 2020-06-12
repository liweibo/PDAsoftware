/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crrc.pdasoftware.wuxianzhuanchu.all.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.wuxianzhuanchu.all.bean.DownloadindBean;
import com.crrc.pdasoftware.wuxianzhuanchu.all.utils.RingProgressBar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;


public class CommonRecyclerViewAdapter extends RecyclerView.Adapter<CommonRecyclerViewAdapter.ViewHolder> {

    public ConcurrentLinkedQueue downloadingFileList;//展示下载文件路径的列表
    public List<String> downloadingFileList2 = new ArrayList<>();//展示下载文件的列表
    public Context context;
    Map<String, DownloadindBean> downloadingMap;//下载进度

    public CommonRecyclerViewAdapter(ConcurrentLinkedQueue downloadingFileList,
                                     Context context,
                                     Map<String, DownloadindBean> downloadingMap) {
        this.downloadingFileList = downloadingFileList;
        this.downloadingMap = downloadingMap;
        this.context = context;

        Iterator<String> iterator = downloadingFileList.iterator();

        while (iterator.hasNext()) {
            String filename = iterator.next();
            downloadingFileList2.add(filename);
        }




//        Iterator<Map.Entry<String, DownloadindBean>> entries = downloadingMap.entrySet().iterator();
//        while(entries.hasNext()){
//            Map.Entry<String, DownloadindBean> entry = entries.next();
//            String key = entry.getKey();
//            DownloadindBean value = entry.getValue();
//            System.out.println("12121212:"+key+":"+value.getDownloaded());
//        }


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View root = inflater.inflate(R.layout.downloading_item_list, viewGroup, false);
        return new ViewHolder(root, this);
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int position) {
        // 设置list中TextView的显示
        holder.tv.setText("根目录" + downloadingFileList2.get(position));//文件名
        holder.im.setImageResource(R.mipmap.document);
        if (downloadingFileList2.get(position) != null &&
                downloadingMap.get(downloadingFileList2.get(position)) != null) {
            holder.progress.setProgress(downloadingMap.get(downloadingFileList2.get(position)).current);
        }
        if ((downloadingFileList2.get(position) != null) &&
                (downloadingMap.get(downloadingFileList2.get(position)) != null) &&
                downloadingMap.get(downloadingFileList2.get(position)).getDownloaded().equals("已下载")) {
            holder.tvSucFail.setBackground(
                    context.getResources().getDrawable(R.drawable.bt_shape_havedownload));
            holder.tvSucFail.setTextColor(Color.parseColor("#0098fd"));
            holder.tvSucFail.setText(downloadingMap.get(downloadingFileList2.get(position)).getDownloaded());
        } else if (
                (downloadingFileList2.get(position) != null) &&
                        (downloadingMap.get(downloadingFileList2.get(position)) != null) &&
                        downloadingMap.get(downloadingFileList2.get(position)).getDownloaded().equals("下载中..")) {
            holder.tvSucFail.setBackground(
                    context.getResources().getDrawable(R.drawable.bt_shape_havedownload_downing));
            holder.tvSucFail.setTextColor(Color.rgb(255, 0, 0));
            ;
            holder.tvSucFail.setText(downloadingMap.get(downloadingFileList2.get(position)).getDownloaded());
        }


    }

    @Override
    public int getItemCount() {
        return downloadingFileList2.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv;
        public TextView tvSucFail;
        public ImageView im;
        public RingProgressBar progress;


        public ViewHolder(View itemView, CommonRecyclerViewAdapter adapter) {
            super(itemView);

            tv = itemView.findViewById(R.id.item_tv2);
            tvSucFail = itemView.findViewById(R.id.item_tv_sucfail2);
            progress = itemView.findViewById(R.id.ringProgressBarAdapter2);
            im = itemView.findViewById(R.id.file_icon2);

        }


    }
}

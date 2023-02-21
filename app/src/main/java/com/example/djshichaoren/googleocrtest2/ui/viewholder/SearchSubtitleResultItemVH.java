package com.example.djshichaoren.googleocrtest2.ui.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.config.Constants;
import com.example.djshichaoren.googleocrtest2.database.SubtitleDatabaseUtil;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleEntity;
import com.example.djshichaoren.googleocrtest2.http.bean.SubtitleDetailResult;
import com.example.djshichaoren.googleocrtest2.http.bean.SubtitleSearchResult;
import com.example.djshichaoren.googleocrtest2.models.BaseEvent;
import com.example.djshichaoren.googleocrtest2.util.SubtitleDownloadUtil;
import com.example.djshichaoren.googleocrtest2.util.SubtitleHttpUtil;

import org.greenrobot.eventbus.EventBus;


public class SearchSubtitleResultItemVH extends RecyclerView.ViewHolder {

    private TextView tv_subtitle_name;
    private TextView tv_release_site;
    private TextView tv_lang_desc;
    private LinearLayout ll_detail_subtitle;

    public SearchSubtitleResultItemVH(@NonNull View itemView) {
        super(itemView);

        tv_subtitle_name = itemView.findViewById(R.id.tv_subtitle_name);
        tv_release_site = itemView.findViewById(R.id.tv_release_site);
        tv_lang_desc = itemView.findViewById(R.id.tv_lang_desc);
        ll_detail_subtitle = itemView.findViewById(R.id.ll_detail_subtitle);
    }

    public void setData(SubtitleSearchResult.SubEntity subEntity, SubtitleHttpUtil subtitleHttpUtil){
        if(subEntity == null) return;

        tv_subtitle_name.setText(subEntity.native_name);
        tv_release_site.setText(subEntity.release_site);

        if(subEntity.lang != null){
            tv_lang_desc.setText(subEntity.lang.desc);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ll_detail_subtitle.getChildCount() == 0){
                    ll_detail_subtitle.setVisibility(View.VISIBLE);
                    subtitleHttpUtil.queryDetail(subEntity.id, new SubtitleHttpUtil.QuerySubtitleDetailCallback() {
                        @Override
                        public void success(SubtitleDetailResult subtitleDetailResult) {
                            if(subtitleDetailResult != null && subtitleDetailResult.sub != null
                                    && subtitleDetailResult.sub.subs != null
                                    && subtitleDetailResult.sub.subs.size() > 0){

                                for (SubtitleDetailResult.SubEntity subEntity : subtitleDetailResult.sub.subs){
                                    if(subEntity != null && subEntity.filelist != null && subEntity.filelist.size() > 0){
                                        for(SubtitleDetailResult.FileEntity fileEntity : subEntity.filelist){
                                            createAndAddDetailTextView(fileEntity);
                                        }
                                    }
                                }

                            }
                        }

                        @Override
                        public void error() {

                        }
                    });
                }
                else{
                    ll_detail_subtitle.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    public TextView createAndAddDetailTextView(SubtitleDetailResult.FileEntity fileEntity){
        String fileName = fileEntity.f;
        TextView textView = new TextView(itemView.getContext());
        textView.setText(fileName);
        textView.setTextColor(itemView.getContext().getResources().getColor(R.color.search_subtitle_result_item_detail_content));
        textView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.search_subtitle_result_item_detail_content_background_not_download));
        ll_detail_subtitle.addView(textView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)textView.getLayoutParams();
        layoutParams.topMargin = 10;
        textView.setLayoutParams(layoutParams);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fileName.contains("srt")){
                    Toast.makeText(itemView.getContext(), "开始下载", Toast.LENGTH_SHORT).show();
                    SubtitleDownloadUtil.downloadFile(itemView.getContext(), fileEntity.url, fileName, new SubtitleDownloadUtil.FileDownloadCallback() {
                        @Override
                        public void onDownloading(int progress) {

                        }

                        @Override
                        public void onDownloadSuccess() {
                            itemView.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(itemView.getContext(), "下载成功", Toast.LENGTH_SHORT).show();

                                    // 添加数据库
                                    SubtitleEntity subtitleEntity = SubtitleDatabaseUtil.getSubtitleEntity(itemView.getContext(), fileName);
                                    if(subtitleEntity == null) {
                                        SubtitleDatabaseUtil.insertSubtitleEntity(itemView.getContext(), fileName);
                                        EventBus.getDefault().post(new BaseEvent(Constants.FLUSH_SUBTITLE_LIST_EVENT));
                                    }

                                    textView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.search_subtitle_result_item_detail_content_background_download));
                                }
                            });

                        }
                        @Override
                        public void onDownloadFailed() {
                            Toast.makeText(itemView.getContext(), "下载失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return textView;
    }

}

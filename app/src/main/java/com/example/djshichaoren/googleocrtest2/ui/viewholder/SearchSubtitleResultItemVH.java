package com.example.djshichaoren.googleocrtest2.ui.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.http.bean.SubtitleDetailResult;
import com.example.djshichaoren.googleocrtest2.http.bean.SubtitleSearchResult;
import com.example.djshichaoren.googleocrtest2.util.SubtitleHttpUtil;

public class SearchSubtitleResultItemVH extends RecyclerView.ViewHolder {

    private TextView tv_subtitle_name;
    private TextView tv_release_site;
    private TextView tv_lang_desc;

    public SearchSubtitleResultItemVH(@NonNull View itemView) {
        super(itemView);

        tv_subtitle_name = itemView.findViewById(R.id.tv_subtitle_name);
        tv_release_site = itemView.findViewById(R.id.tv_release_site);
        tv_lang_desc = itemView.findViewById(R.id.tv_lang_desc);


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
                subtitleHttpUtil.queryDetail(subEntity.id, new SubtitleHttpUtil.QuerySubtitleDetailCallback() {
                    @Override
                    public void success(SubtitleDetailResult subtitleDetailResult) {
                        if(subtitleDetailResult != null && subtitleDetailResult.sub != null
                                && subtitleDetailResult.sub.subs != null
                                && subtitleDetailResult.sub.subs.size() > 0){
                            boolean isFindUrl = false;
                            flag:
                            for (SubtitleDetailResult.SubEntity subEntity : subtitleDetailResult.sub.subs){
                                if(subEntity != null && subEntity.filelist != null && subEntity.filelist.size() > 0){
                                    for(SubtitleDetailResult.FileEntity fileEntity : subEntity.filelist){
                                        String fileName = fileEntity.f;
                                        if(fileName.contains("srt")){
                                            String url = fileEntity.url;
                                            isFindUrl = true;
                                            break flag;
                                        }
                                    }
                                }
                            }

                            if(!isFindUrl){
                                Toast.makeText(itemView.getContext(), "此字幕中没有发现合适的格式", Toast.LENGTH_SHORT);
                            }
                        }
                    }

                    @Override
                    public void error() {

                    }
                });
            }
        });
    }

}

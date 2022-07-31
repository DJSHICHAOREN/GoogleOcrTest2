package com.example.djshichaoren.googleocrtest2.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.http.bean.SubtitleSearchResult;

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

    public void setData(SubtitleSearchResult.SubEntity subEntity){
        if(subEntity == null) return;

        tv_subtitle_name.setText(subEntity.native_name);
        tv_release_site.setText(subEntity.release_site);

        if(subEntity.lang != null){
            tv_lang_desc.setText(subEntity.lang.desc);
        }
    }
}

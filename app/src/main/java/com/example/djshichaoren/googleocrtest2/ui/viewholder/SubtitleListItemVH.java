package com.example.djshichaoren.googleocrtest2.ui.viewholder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.SubtitleActivity;
import com.example.djshichaoren.googleocrtest2.config.Constants;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleEntity;
import com.example.djshichaoren.googleocrtest2.ui.fragment.SubtitleListFragment;

public class SubtitleListItemVH extends RecyclerView.ViewHolder {

    private TextView tv_subtitle_name;
    private SubtitleListFragment.SubtitleListItemClickCallback mSubtitleListItemClickCallback;

    public SubtitleListItemVH(@NonNull View itemView
            , SubtitleListFragment.SubtitleListItemClickCallback subtitleListItemClickCallback) {
        super(itemView);

        mSubtitleListItemClickCallback = subtitleListItemClickCallback;
        tv_subtitle_name = itemView.findViewById(R.id.tv_subtitle_name);
    }

    public void bind(SubtitleEntity subtitleEntity){
        if(subtitleEntity == null) return;

        tv_subtitle_name.setText(subtitleEntity.name);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mSubtitleListItemClickCallback.run(subtitleEntity)) {
                    Intent intent = new Intent(view.getContext(), SubtitleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.SUBTITLE_LIST_ITEM_VH_SUBTITLE_KEY, subtitleEntity);
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }

            }
        });

    }


}

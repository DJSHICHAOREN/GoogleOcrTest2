package com.example.djshichaoren.googleocrtest2.core.view.item_view;

import android.view.View;
import android.widget.TextView;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.models.TranslateResult;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TranslationContentVH extends BaseVH {
    private TextView otv_word;
    private TextView otv_translation;

    public TranslationContentVH(@NonNull View itemView) {
        super(itemView);

        otv_word = itemView.findViewById(R.id.otv_word);
        otv_translation = itemView.findViewById(R.id.otv_translation);
    }

    @Override
    public void bind(TranslateResult translateResult) {
        if(translateResult == null) return;

        otv_word.setText(translateResult.mWord);

        if(translateResult.mTranslationList != null && translateResult.mTranslationList.size() > 0){
            otv_translation.setText(translateResult.mTranslationList.get(0));
        }
        else{
            otv_translation.setText("");
        }

    }


}

package com.example.djshichaoren.googleocrtest2.core.view.item_view;

import android.view.View;
import android.widget.TextView;

import com.example.djshichaoren.googleocrtest2.R;
import com.example.djshichaoren.googleocrtest2.models.TranslateResult;

import androidx.annotation.NonNull;

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

        otv_word.setText(translateResult.getWord_name());

        otv_translation.setText(translateResult.short_translation);

    }


}

package com.ishuinzu.childside.provider.dictionary;

import android.content.Context;

import com.ishuinzu.childside.core.AbstractProvider;
import com.ishuinzu.childside.core.Data;

public class DictionaryProvider extends AbstractProvider {
    public DictionaryProvider(Context context) {
        super(context);
    }

    public Data<Word> getWords() {
        Data<Word> words = getContentTableData(Word.uri, Word.class);
        return words;
    }
}
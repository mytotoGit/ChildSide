package com.ishuinzu.childside.provider.dictionary;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.UserDictionary;

import com.ishuinzu.childside.core.Entity;
import com.ishuinzu.childside.core.FieldMapping;
import com.ishuinzu.childside.core.IgnoreMapping;

public class Word extends Entity {
    @IgnoreMapping
    public static Uri uri = UserDictionary.Words.CONTENT_URI;

    @FieldMapping(columnName = BaseColumns._ID, physicalType = FieldMapping.PhysicalType.Long)
    public long id;

    @FieldMapping(columnName = UserDictionary.Words.WORD, physicalType = FieldMapping.PhysicalType.String)
    public String word;

    @FieldMapping(columnName = UserDictionary.Words.FREQUENCY, physicalType = FieldMapping.PhysicalType.Int)
    public int frequency;

    @FieldMapping(columnName = UserDictionary.Words.LOCALE, physicalType = FieldMapping.PhysicalType.String)
    public String locale;

    @FieldMapping(columnName = UserDictionary.Words.APP_ID, physicalType = FieldMapping.PhysicalType.Int)
    public int appId;

    @FieldMapping(columnName = UserDictionary.Words.SHORTCUT, physicalType = FieldMapping.PhysicalType.String)
    public String shortcut;
}
package com.ishuinzu.childside.provider.media;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import com.ishuinzu.childside.core.Entity;
import com.ishuinzu.childside.core.FieldMapping;
import com.ishuinzu.childside.core.IgnoreMapping;

public class Genre extends Entity {
    @IgnoreMapping
    public static Uri uriExternal = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;

    @IgnoreMapping
    public static Uri uriInternal = MediaStore.Audio.Genres.INTERNAL_CONTENT_URI;

    @FieldMapping(columnName = BaseColumns._ID, physicalType = FieldMapping.PhysicalType.Long)
    public long id;

    @FieldMapping(columnName = MediaStore.Audio.GenresColumns.NAME, physicalType = FieldMapping.PhysicalType.String)
    public String name;
}
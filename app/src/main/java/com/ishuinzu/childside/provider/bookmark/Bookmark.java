package com.ishuinzu.childside.provider.bookmark;

import android.net.Uri;

import com.ishuinzu.childside.core.Entity;
import com.ishuinzu.childside.core.FieldMapping;
import com.ishuinzu.childside.core.IgnoreMapping;

public class Bookmark extends Entity {
    @IgnoreMapping
    public static Uri uri = Uri.parse("content://browser/bookmarks");

    @FieldMapping(columnName = "bookmark", physicalType = FieldMapping.PhysicalType.Int)
    public int bookmark;

    @FieldMapping(columnName = "created", physicalType = FieldMapping.PhysicalType.Long)
    public long created;

    @FieldMapping(columnName = "date", physicalType = FieldMapping.PhysicalType.Long)
    public long date;

    @FieldMapping(columnName = "favicon", physicalType = FieldMapping.PhysicalType.Blob)
    public byte[] favicon;

    @FieldMapping(columnName = "title", physicalType = FieldMapping.PhysicalType.String)
    public String title;

    @FieldMapping(columnName = "url", physicalType = FieldMapping.PhysicalType.String)
    public String url;

    @FieldMapping(columnName = "visits", physicalType = FieldMapping.PhysicalType.Int)
    public int visits;
}
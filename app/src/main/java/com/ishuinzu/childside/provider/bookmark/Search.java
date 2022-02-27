package com.ishuinzu.childside.provider.bookmark;

import android.net.Uri;

import com.ishuinzu.childside.core.Entity;
import com.ishuinzu.childside.core.FieldMapping;
import com.ishuinzu.childside.core.IgnoreMapping;

public class Search extends Entity {
    @IgnoreMapping
    public static Uri uri = Uri.parse("content://browser/searches");

    @FieldMapping(columnName = "search", physicalType = FieldMapping.PhysicalType.String)
    public String search;

    @FieldMapping(columnName = "date", physicalType = FieldMapping.PhysicalType.Long)
    public long date;
}

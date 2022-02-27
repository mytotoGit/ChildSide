package com.ishuinzu.childside.provider.calendar;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.CalendarContract;

import com.ishuinzu.childside.core.Entity;
import com.ishuinzu.childside.core.FieldMapping;
import com.ishuinzu.childside.core.IgnoreMapping;

public class Instance extends Entity {
    @IgnoreMapping
    public static Uri uri = CalendarContract.Instances.CONTENT_URI;

    @FieldMapping(columnName = BaseColumns._ID, physicalType = FieldMapping.PhysicalType.Long)
    public long id;

    @FieldMapping(columnName = CalendarContract.Instances.EVENT_ID, physicalType = FieldMapping.PhysicalType.Long)
    public long eventId;

    @FieldMapping(columnName = CalendarContract.Instances.BEGIN, physicalType = FieldMapping.PhysicalType.Long)
    public long begin;

    @FieldMapping(columnName = CalendarContract.Instances.END, physicalType = FieldMapping.PhysicalType.Long)
    public long end;

    @FieldMapping(columnName = CalendarContract.Instances.START_DAY, physicalType = FieldMapping.PhysicalType.Int)
    public long startDay;

    @FieldMapping(columnName = CalendarContract.Instances.START_MINUTE, physicalType = FieldMapping.PhysicalType.Int)
    public long startMinute;

    @FieldMapping(columnName = CalendarContract.Instances.END_DAY, physicalType = FieldMapping.PhysicalType.Int)
    public long endDay;

    @FieldMapping(columnName = CalendarContract.Instances.END_MINUTE, physicalType = FieldMapping.PhysicalType.Int)
    public long endMinute;
}
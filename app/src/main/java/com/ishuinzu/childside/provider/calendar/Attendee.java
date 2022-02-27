package com.ishuinzu.childside.provider.calendar;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.CalendarContract;

import com.ishuinzu.childside.core.Entity;
import com.ishuinzu.childside.core.FieldMapping;
import com.ishuinzu.childside.core.IgnoreMapping;

public class Attendee extends Entity {
    @IgnoreMapping
    public static Uri uri = CalendarContract.Attendees.CONTENT_URI;

    @FieldMapping(columnName = BaseColumns._ID, physicalType = FieldMapping.PhysicalType.Long)
    public long id;

    @FieldMapping(columnName = CalendarContract.Attendees.EVENT_ID, physicalType = FieldMapping.PhysicalType.Long)
    public long eventId;

    @FieldMapping(columnName = CalendarContract.Attendees.ATTENDEE_NAME, physicalType = FieldMapping.PhysicalType.String)
    public String name;

    @FieldMapping(columnName = CalendarContract.Attendees.ATTENDEE_EMAIL, physicalType = FieldMapping.PhysicalType.String)
    public String email;

    @FieldMapping(columnName = CalendarContract.Attendees.ATTENDEE_RELATIONSHIP, physicalType = FieldMapping.PhysicalType.Int)
    public int relationship;

    @FieldMapping(columnName = CalendarContract.Attendees.ATTENDEE_TYPE, physicalType = FieldMapping.PhysicalType.Int)
    public int type;

    @FieldMapping(columnName = CalendarContract.Attendees.ATTENDEE_STATUS, physicalType = FieldMapping.PhysicalType.String)
    public String status;
}
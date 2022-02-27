package com.ishuinzu.childside.provider.calendar;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.CalendarContract;

import com.ishuinzu.childside.core.Entity;
import com.ishuinzu.childside.core.FieldMapping;
import com.ishuinzu.childside.core.IgnoreMapping;

public class Calendar extends Entity {
    @IgnoreMapping
    public static Uri uri = CalendarContract.Calendars.CONTENT_URI;

    @FieldMapping(columnName = BaseColumns._ID, physicalType = FieldMapping.PhysicalType.Long)
    public long id;

    @FieldMapping(columnName = CalendarContract.Calendars.NAME, physicalType = FieldMapping.PhysicalType.String)
    public String name;

    @FieldMapping(columnName = CalendarContract.Calendars.ALLOWED_REMINDERS, physicalType = FieldMapping.PhysicalType.String)
    public String allowedReminders;

    @FieldMapping(columnName = CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, physicalType = FieldMapping.PhysicalType.Int)
    public int calendarAccessLevel;

    @FieldMapping(columnName = CalendarContract.Calendars.CALENDAR_COLOR, canUpdate = true, physicalType = FieldMapping.PhysicalType.Int)
    public int calendarColor;

    @FieldMapping(columnName = CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, canUpdate = true, physicalType = FieldMapping.PhysicalType.String)
    public String displayName;

    @FieldMapping(columnName = CalendarContract.Calendars.CALENDAR_TIME_ZONE, physicalType = FieldMapping.PhysicalType.String)
    public String calendarTimeZone;

    @FieldMapping(columnName = CalendarContract.Calendars.CAN_MODIFY_TIME_ZONE, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean canModifyTimeZone;

    @FieldMapping(columnName = CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean canOrginizerRespond;

    @FieldMapping(columnName = CalendarContract.Calendars.MAX_REMINDERS, physicalType = FieldMapping.PhysicalType.Int)
    public int maxReminders;

    @FieldMapping(columnName = CalendarContract.Calendars.OWNER_ACCOUNT, physicalType = FieldMapping.PhysicalType.String)
    public String ownerAccount;

    @FieldMapping(columnName = CalendarContract.Calendars.SYNC_EVENTS, physicalType = FieldMapping.PhysicalType.Int)
    public int syncEvents;

    @FieldMapping(columnName = CalendarContract.Calendars.VISIBLE, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean visible;

    @FieldMapping(columnName = CalendarContract.Calendars.ACCOUNT_NAME, physicalType = FieldMapping.PhysicalType.String)
    public String accountName;

    @FieldMapping(columnName = CalendarContract.Calendars.ACCOUNT_TYPE, physicalType = FieldMapping.PhysicalType.String)
    public String accountType;

    @FieldMapping(columnName = CalendarContract.Calendars.CAN_PARTIALLY_UPDATE, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean canPartiallyUpdate;

    @FieldMapping(columnName = CalendarContract.Calendars.DELETED, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean deleted;

    @FieldMapping(columnName = CalendarContract.Calendars.DIRTY, physicalType = FieldMapping.PhysicalType.Long)
    public long dirty;

    @FieldMapping(columnName = CalendarContract.Calendars._SYNC_ID, physicalType = FieldMapping.PhysicalType.String)
    public String syncId;

    @FieldMapping(columnName = CalendarContract.Calendars.CALENDAR_LOCATION, physicalType = FieldMapping.PhysicalType.String)
    public String location;

    @FieldMapping(columnName = CalendarContract.Calendars.DEFAULT_SORT_ORDER, physicalType = FieldMapping.PhysicalType.String)
    @IgnoreMapping
    public String sortOrder;
}
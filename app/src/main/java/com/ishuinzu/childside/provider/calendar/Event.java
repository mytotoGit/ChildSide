package com.ishuinzu.childside.provider.calendar;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.CalendarContract;

import com.ishuinzu.childside.core.Entity;
import com.ishuinzu.childside.core.FieldMapping;
import com.ishuinzu.childside.core.IgnoreMapping;

public class Event extends Entity {
    @IgnoreMapping
    public static Uri uri = CalendarContract.Events.CONTENT_URI;

    @FieldMapping(columnName = BaseColumns._ID, physicalType = FieldMapping.PhysicalType.Long)
    public long id;

    @FieldMapping(columnName = CalendarContract.Events.ALLOWED_REMINDERS, physicalType = FieldMapping.PhysicalType.String)
    public String allowedReminders;

    @FieldMapping(columnName = CalendarContract.Events.CALENDAR_ACCESS_LEVEL, physicalType = FieldMapping.PhysicalType.Int)
    public int calendarAccessLevel;

    @FieldMapping(columnName = CalendarContract.Events.CALENDAR_COLOR, physicalType = FieldMapping.PhysicalType.Int)
    public int calendarColor;

    @FieldMapping(columnName = CalendarContract.Events.CALENDAR_DISPLAY_NAME, physicalType = FieldMapping.PhysicalType.String)
    public String displayName;

    @FieldMapping(columnName = CalendarContract.Events.CALENDAR_TIME_ZONE, physicalType = FieldMapping.PhysicalType.String)
    public String calendarTimeZone;

    @FieldMapping(columnName = CalendarContract.Events.CAN_MODIFY_TIME_ZONE, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean canModifyTimeZone;

    @FieldMapping(columnName = CalendarContract.Events.CAN_ORGANIZER_RESPOND, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean canOrginizerRespond;

    @FieldMapping(columnName = CalendarContract.Events.MAX_REMINDERS, physicalType = FieldMapping.PhysicalType.Int)
    public int maxReminders;

    @FieldMapping(columnName = CalendarContract.Events.OWNER_ACCOUNT, physicalType = FieldMapping.PhysicalType.String)
    public String ownerAccount;

    @FieldMapping(columnName = CalendarContract.Events.VISIBLE, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean visible;

    @FieldMapping(columnName = CalendarContract.Events.ACCOUNT_NAME, physicalType = FieldMapping.PhysicalType.String)
    public String accountName;

    @FieldMapping(columnName = CalendarContract.Events.ACCOUNT_TYPE, physicalType = FieldMapping.PhysicalType.String)
    public String accountType;

    @FieldMapping(columnName = CalendarContract.Events.DELETED, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean deleted;

    @FieldMapping(columnName = CalendarContract.Events._SYNC_ID, physicalType = FieldMapping.PhysicalType.String)
    public String syncId;

    @FieldMapping(columnName = CalendarContract.Events.ACCESS_LEVEL, physicalType = FieldMapping.PhysicalType.Int)
    public int accessLevel;

    @FieldMapping(columnName = CalendarContract.Events.ALL_DAY, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean allDay;

    @FieldMapping(columnName = CalendarContract.Events.AVAILABILITY, physicalType = FieldMapping.PhysicalType.Int)
    public int availability;

    @FieldMapping(columnName = CalendarContract.Events.CALENDAR_ID, physicalType = FieldMapping.PhysicalType.Long)
    public long calendarId;

    @FieldMapping(columnName = CalendarContract.Events.DESCRIPTION, canUpdate = true, physicalType = FieldMapping.PhysicalType.String)
    public String description;

    @FieldMapping(columnName = CalendarContract.Events.DTEND, physicalType = FieldMapping.PhysicalType.Long)
    public long dTend;

    @FieldMapping(columnName = CalendarContract.Events.DTSTART, physicalType = FieldMapping.PhysicalType.Long)
    public long dTStart;

    @FieldMapping(columnName = CalendarContract.Events.DURATION, physicalType = FieldMapping.PhysicalType.String)
    public String duration;

    @FieldMapping(columnName = CalendarContract.Events.EVENT_COLOR, canUpdate = true, physicalType = FieldMapping.PhysicalType.Int)
    public int eventColor;

    @FieldMapping(columnName = CalendarContract.Events.EVENT_END_TIMEZONE, physicalType = FieldMapping.PhysicalType.String)
    public String eventEndTimeZone;

    @FieldMapping(columnName = CalendarContract.Events.EVENT_LOCATION, canUpdate = true, physicalType = FieldMapping.PhysicalType.String)
    public String eventLocation;

    @FieldMapping(columnName = CalendarContract.Events.EVENT_TIMEZONE, physicalType = FieldMapping.PhysicalType.String)
    public String eventTimeZone;

    @FieldMapping(columnName = CalendarContract.Events.EXDATE, physicalType = FieldMapping.PhysicalType.String)
    public String eventExDate;

    @FieldMapping(columnName = CalendarContract.Events.EXRULE, physicalType = FieldMapping.PhysicalType.String)
    public String eventExRule;

    @FieldMapping(columnName = CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, physicalType = FieldMapping.PhysicalType.Int)
    public int guestCanInviteOthers;

    @FieldMapping(columnName = CalendarContract.Events.GUESTS_CAN_MODIFY, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean guestCanModify;

    @FieldMapping(columnName = CalendarContract.Events.GUESTS_CAN_SEE_GUESTS, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean guestCanSeeQuests;

    @FieldMapping(columnName = CalendarContract.Events.HAS_ALARM, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean hasAlarm;

    @FieldMapping(columnName = CalendarContract.Events.HAS_ATTENDEE_DATA, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean hasAttendeeData;

    @FieldMapping(columnName = CalendarContract.Events.HAS_EXTENDED_PROPERTIES, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean hasExtendedProperties;

    @FieldMapping(columnName = CalendarContract.Events.LAST_DATE, physicalType = FieldMapping.PhysicalType.Long)
    public long lastDate;

    @FieldMapping(columnName = CalendarContract.Events.ORGANIZER, physicalType = FieldMapping.PhysicalType.String)
    public String organizer;

    @FieldMapping(columnName = CalendarContract.Events.ORIGINAL_ALL_DAY, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean originalAllDay;

    @FieldMapping(columnName = CalendarContract.Events.ORIGINAL_ID, physicalType = FieldMapping.PhysicalType.String)
    public String originalId;

    @FieldMapping(columnName = CalendarContract.Events.ORIGINAL_INSTANCE_TIME, physicalType = FieldMapping.PhysicalType.Long)
    public long originalInstanceTime;

    @FieldMapping(columnName = CalendarContract.Events.ORIGINAL_SYNC_ID, physicalType = FieldMapping.PhysicalType.String)
    public String originalSyncId;

    @FieldMapping(columnName = CalendarContract.Events.RDATE, physicalType = FieldMapping.PhysicalType.String)
    public String rDate;

    @FieldMapping(columnName = CalendarContract.Events.RRULE, physicalType = FieldMapping.PhysicalType.String)
    public String rRule;

    @FieldMapping(columnName = CalendarContract.Events.SELF_ATTENDEE_STATUS, physicalType = FieldMapping.PhysicalType.String)
    public String selfAttendeeStatus;

    @FieldMapping(columnName = CalendarContract.Events.STATUS, physicalType = FieldMapping.PhysicalType.String)
    public String status;

    @FieldMapping(columnName = CalendarContract.Events.TITLE, canUpdate = true, physicalType = FieldMapping.PhysicalType.String)
    public String title;
}
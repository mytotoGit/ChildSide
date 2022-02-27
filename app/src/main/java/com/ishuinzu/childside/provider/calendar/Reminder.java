package com.ishuinzu.childside.provider.calendar;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.CalendarContract;

import com.ishuinzu.childside.core.Entity;
import com.ishuinzu.childside.core.EnumInt;
import com.ishuinzu.childside.core.FieldMapping;
import com.ishuinzu.childside.core.IgnoreMapping;

public class Reminder extends Entity {
    @IgnoreMapping
    public static Uri uri = CalendarContract.Reminders.CONTENT_URI;

    @FieldMapping(columnName = BaseColumns._ID, physicalType = FieldMapping.PhysicalType.Long)
    public long id;

    @FieldMapping(columnName = CalendarContract.Reminders.EVENT_ID, physicalType = FieldMapping.PhysicalType.Long)
    public long eventId;

    @FieldMapping(columnName = CalendarContract.Reminders.MINUTES, physicalType = FieldMapping.PhysicalType.Int)
    public int minutes;

    @FieldMapping(columnName = CalendarContract.Reminders.METHOD, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.EnumInt)
    public MethodType method;

    public enum MethodType implements EnumInt {
        DEFAULT(0),
        ALERT(1),
        EMAIL(2),
        SMS(3),
        ALARM(4);

        int val;

        MethodType(int val) {
            this.val = val;
        }

        public static MethodType fromVal(int val) {
            for (MethodType methodType : values()) {
                if (methodType.val == val) {
                    return methodType;
                }
            }
            return DEFAULT;
        }
    }
}

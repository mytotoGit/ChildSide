package com.ishuinzu.childside.provider.calllog;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.CallLog;

import com.ishuinzu.childside.core.Entity;
import com.ishuinzu.childside.core.EnumInt;
import com.ishuinzu.childside.core.FieldMapping;
import com.ishuinzu.childside.core.IgnoreMapping;

public class Call extends Entity {
    @IgnoreMapping
    public static Uri uri = CallLog.Calls.CONTENT_URI;

    @FieldMapping(columnName = BaseColumns._ID, physicalType = FieldMapping.PhysicalType.Long)
    public long id;

    @FieldMapping(columnName = CallLog.Calls.CACHED_NAME, physicalType = FieldMapping.PhysicalType.String)
    public String name;

    @FieldMapping(columnName = CallLog.Calls.DATE, physicalType = FieldMapping.PhysicalType.Long)
    public long call_date;

    @FieldMapping(columnName = CallLog.Calls.DURATION, physicalType = FieldMapping.PhysicalType.Long)
    public long duration;

    @FieldMapping(columnName = CallLog.Calls.GEOCODED_LOCATION, physicalType = FieldMapping.PhysicalType.String, logicalType = FieldMapping.LogicalType.String)
    public String geo_code_location;

    @FieldMapping(columnName = CallLog.Calls.IS_READ, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean is_read;

    @FieldMapping(columnName = CallLog.Calls.NUMBER, physicalType = FieldMapping.PhysicalType.String)
    public String number;

    @FieldMapping(columnName = CallLog.Calls.TYPE, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.EnumInt)
    public CallType type;

    public enum CallType implements EnumInt {
        INCOMING(CallLog.Calls.INCOMING_TYPE),
        OUTGOING(CallLog.Calls.OUTGOING_TYPE),
        MISSED(CallLog.Calls.MISSED_TYPE);

        int val;

        CallType(int val) {
            this.val = val;
        }

        public static CallType fromVal(int val) {
            for (CallType messageStatus : values()) {
                if (messageStatus.val == val) {
                    return messageStatus;
                }
            }
            return null;
        }
    }
}
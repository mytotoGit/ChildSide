package com.ishuinzu.childside.provider.telephony;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.Telephony;

import com.ishuinzu.childside.core.Entity;
import com.ishuinzu.childside.core.EnumInt;
import com.ishuinzu.childside.core.FieldMapping;
import com.ishuinzu.childside.core.IgnoreMapping;

public class SMS extends Entity {
    @IgnoreMapping
    public static Uri uri = android.provider.Telephony.Sms.CONTENT_URI;

    @IgnoreMapping
    public static Uri uriInbox = android.provider.Telephony.Sms.Inbox.CONTENT_URI;

    @IgnoreMapping
    public static Uri uriOutbox = android.provider.Telephony.Sms.Outbox.CONTENT_URI;

    @IgnoreMapping
    public static Uri uriSent = android.provider.Telephony.Sms.Sent.CONTENT_URI;

    @IgnoreMapping
    public static Uri uriDraft = android.provider.Telephony.Sms.Draft.CONTENT_URI;

    @FieldMapping(columnName = BaseColumns._ID, physicalType = FieldMapping.PhysicalType.Long)
    public long id;

    @FieldMapping(columnName = Telephony.TextBasedSmsColumns.ADDRESS, physicalType = FieldMapping.PhysicalType.String)
    public String address;

    @FieldMapping(columnName = Telephony.TextBasedSmsColumns.BODY, physicalType = FieldMapping.PhysicalType.String)
    public String body;

    @FieldMapping(columnName = Telephony.TextBasedSmsColumns.DATE, physicalType = FieldMapping.PhysicalType.Long)
    public long receivedDate;

    @FieldMapping(columnName = Telephony.TextBasedSmsColumns.DATE_SENT, physicalType = FieldMapping.PhysicalType.Long)
    public long sentDate;

    @FieldMapping(columnName = Telephony.TextBasedSmsColumns.READ, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean read;

    @FieldMapping(columnName = Telephony.TextBasedSmsColumns.SEEN, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Boolean)
    public boolean seen;

    @FieldMapping(columnName = Telephony.TextBasedSmsColumns.TYPE, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.EnumInt)
    public MessageType type;

    public static enum MessageType implements EnumInt {
        ALL(Telephony.TextBasedSmsColumns.MESSAGE_TYPE_ALL),
        INBOX(Telephony.TextBasedSmsColumns.MESSAGE_TYPE_INBOX),
        SENT(Telephony.TextBasedSmsColumns.MESSAGE_TYPE_SENT),
        DRAFT(Telephony.TextBasedSmsColumns.MESSAGE_TYPE_DRAFT),
        OUTBOX(Telephony.TextBasedSmsColumns.MESSAGE_TYPE_OUTBOX),
        FAILED(Telephony.TextBasedSmsColumns.MESSAGE_TYPE_FAILED),
        QUEUED(Telephony.TextBasedSmsColumns.MESSAGE_TYPE_QUEUED);

        int val;

        private MessageType(int val) {
            this.val = val;
        }

        public static MessageType fromVal(int val) {
            for (MessageType messageType : values()) {
                if (messageType.val == val) {
                    return messageType;
                }
            }
            return null;
        }

        public int getValue() {
            return val;
        }
    }
}
package com.ishuinzu.childside.provider.contacts;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

import com.ishuinzu.childside.core.Entity;
import com.ishuinzu.childside.core.FieldMapping;
import com.ishuinzu.childside.core.IgnoreMapping;

public class Contact extends Entity {
    @IgnoreMapping
    public static Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    @IgnoreMapping
    public static Uri uriEmail = ContactsContract.CommonDataKinds.Email.CONTENT_URI;

    @FieldMapping(columnName = BaseColumns._ID, physicalType = FieldMapping.PhysicalType.Long)
    public long id;

    @FieldMapping(columnName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, physicalType = FieldMapping.PhysicalType.String)
    public String displayName;

    @FieldMapping(columnName = ContactsContract.CommonDataKinds.Phone.NUMBER, physicalType = FieldMapping.PhysicalType.String)
    public String phone;

    @FieldMapping(columnName = ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER, physicalType = FieldMapping.PhysicalType.String)
    public String normilizedPhone;

    @FieldMapping(columnName = ContactsContract.CommonDataKinds.Phone.PHOTO_URI, physicalType = FieldMapping.PhysicalType.String)
    public String uriPhoto;

    @FieldMapping(columnName = ContactsContract.CommonDataKinds.Email.ADDRESS, physicalType = FieldMapping.PhysicalType.String)
    public String email;
}
package com.ishuinzu.childside.provider.contacts;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.ishuinzu.childside.core.AbstractProvider;
import com.ishuinzu.childside.core.Data;

import java.io.ByteArrayInputStream;

public class ContactsProvider extends AbstractProvider {
    public ContactsProvider(Context context) {
        super(context);
    }

    public Data<Contact> getContacts() {
        Data<Contact> contactsNoEmail = getContentTableData(Contact.uri, Contact.class);
        return contactsNoEmail;
    }

    public Uri getPhotoUri(Context context, String contactId) {
        try {
            Cursor cur = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null,
                    ContactsContract.Data.CONTACT_ID + "=" + contactId + " AND " + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null, null);
            if (cur != null) {
                if (!cur.moveToFirst()) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
        return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }

    public Bitmap getContactPhoto(Context context, String contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }
}
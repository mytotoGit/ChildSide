package com.ishuinzu.childside.provider.telephony;

import android.content.Context;
import android.net.Uri;

import com.ishuinzu.childside.core.AbstractProvider;
import com.ishuinzu.childside.core.Data;

public class TelephonyProvider extends AbstractProvider {
    public TelephonyProvider(Context context) {
        super(context);
    }

    public enum Filter {
        ALL,
        INBOX,
        OUTBOX,
        SENT,
        DRAFT
    }

    public Data<SMS> getSms(Filter filter) {
        Uri uri = null;
        switch (filter) {
            case ALL:
                uri = SMS.uri;
                break;
            case INBOX:
                uri = SMS.uriInbox;
                break;
            case OUTBOX:
                uri = SMS.uriOutbox;
                break;
            case SENT:
                uri = SMS.uriSent;
                break;
            case DRAFT:
                uri = SMS.uriDraft;
                break;
        }
        Data<SMS> sms = getContentTableData(uri, SMS.class);
        return sms;
    }
}
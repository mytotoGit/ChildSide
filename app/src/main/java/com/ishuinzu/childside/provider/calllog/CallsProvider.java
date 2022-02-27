package com.ishuinzu.childside.provider.calllog;

import android.content.Context;

import com.ishuinzu.childside.core.AbstractProvider;
import com.ishuinzu.childside.core.Data;

public class CallsProvider extends AbstractProvider {
    public CallsProvider(Context context) {
        super(context);
    }

    public Data<Call> getCalls() {
        Data<Call> calls = getContentTableData(Call.uri, Call.class);
        return calls;
    }
}
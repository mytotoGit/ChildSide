package com.ishuinzu.childside.provider.bookmark;

import android.content.Context;

import com.ishuinzu.childside.core.AbstractProvider;
import com.ishuinzu.childside.core.Data;

public class BrowserProvider extends AbstractProvider {
    public BrowserProvider(Context context) {
        super(context);
    }

    public Data<Bookmark> getBookmarks() {
        Data<Bookmark> bookmarks = getContentTableData(Bookmark.uri, Bookmark.class);
        return bookmarks;
    }

    public Data<Search> getSearches() {
        Data<Search> searches = getContentTableData(Search.uri, Search.class);
        return searches;
    }
}

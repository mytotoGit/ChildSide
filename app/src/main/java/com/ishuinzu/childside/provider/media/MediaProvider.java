package com.ishuinzu.childside.provider.media;

import android.content.Context;
import android.provider.MediaStore;

import com.ishuinzu.childside.core.AbstractProvider;
import com.ishuinzu.childside.core.Data;

public class MediaProvider extends AbstractProvider {
    private final static int LIMIT = 250;
    private final static String ORDER_BY_COLUMN = MediaStore.MediaColumns.DATE_MODIFIED;

    public MediaProvider(Context context) {
        super(context);
    }

    public Data<File> getFiles(Storage storage) {
        if (storage == Storage.INTERNAL) {
            return getContentTableData(File.uriInternal, File.class);
        }
        return getContentTableData(File.uriExternal, null, null,
                ORDER_BY_COLUMN + " DESC" + " LIMIT " + LIMIT,
                File.class);
    }

    public Data<Image> getImages(Storage storage) {
        if (storage == Storage.INTERNAL) {
            return getContentTableData(Image.uriInternal, Image.class);
        }
        return getContentTableData(Image.uriExternal, null, null,
                ORDER_BY_COLUMN + " DESC" + " LIMIT " + LIMIT,
                Image.class);
    }

    public Data<Video> getVideos(Storage storage) {
        if (storage == Storage.INTERNAL) {
            return getContentTableData(Video.uriInternal, Video.class);
        }
        return getContentTableData(Video.uriExternal, null, null,
                ORDER_BY_COLUMN + " DESC" + " LIMIT " + LIMIT,
                Video.class);
    }

    public Data<Audio> getAudios(Storage storage) {
        if (storage == Storage.INTERNAL) {
            return getContentTableData(Audio.uriInternal, Audio.class);
        }
        return getContentTableData(Audio.uriExternal, null, null,
                ORDER_BY_COLUMN + " DESC" + " LIMIT " + LIMIT,
                Audio.class);
    }

    public Data<Album> getAlbums(Storage storage) {
        if (storage == Storage.INTERNAL) {
            return getContentTableData(Album.uriInternal, Album.class);
        }
        return getContentTableData(Album.uriExternal, Album.class);
    }

    public Data<Artist> getArtists(Storage storage) {
        if (storage == Storage.INTERNAL) {
            return getContentTableData(Artist.uriInternal, Artist.class);
        }
        return getContentTableData(Artist.uriExternal, Artist.class);
    }

    public Data<Genre> getGenres(Storage storage) {
        if (storage == Storage.INTERNAL) {
            return getContentTableData(Genre.uriInternal, Genre.class);
        }
        return getContentTableData(Genre.uriExternal, Genre.class);
    }

    public Data<PlayList> getPlaylists(Storage storage) {
        if (storage == Storage.INTERNAL) {
            return getContentTableData(PlayList.uriInternal, PlayList.class);
        }
        return getContentTableData(PlayList.uriExternal, PlayList.class);
    }

    public enum Storage {
        INTERNAL,
        EXTERNAL
    }
}
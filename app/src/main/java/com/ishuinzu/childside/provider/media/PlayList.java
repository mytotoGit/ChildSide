package com.ishuinzu.childside.provider.media;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import com.ishuinzu.childside.core.Entity;
import com.ishuinzu.childside.core.FieldMapping;
import com.ishuinzu.childside.core.IgnoreMapping;

public class PlayList extends Entity {
    @IgnoreMapping
    public static Uri uriExternal = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

    @IgnoreMapping
    public static Uri uriInternal = MediaStore.Audio.Playlists.INTERNAL_CONTENT_URI;

    @FieldMapping(columnName = BaseColumns._ID, physicalType = FieldMapping.PhysicalType.Long)
    public long id;

    @FieldMapping(columnName = MediaStore.Audio.PlaylistsColumns.NAME, physicalType = FieldMapping.PhysicalType.String)
    public String name;

    @FieldMapping(columnName = MediaStore.Audio.PlaylistsColumns.DATA, physicalType = FieldMapping.PhysicalType.Blob)
    public byte[] data;

    @FieldMapping(columnName = MediaStore.Audio.PlaylistsColumns.DATE_ADDED, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Long)
    public long dateAdded;

    @FieldMapping(columnName = MediaStore.Audio.PlaylistsColumns.DATE_MODIFIED, physicalType = FieldMapping.PhysicalType.Int, logicalType = FieldMapping.LogicalType.Long)
    public long dateModified;
}
package com.ishuinzu.childside.object;

public class LiveLockObject {
    private long end;
    private String id;
    private long start;

    public LiveLockObject() {}

    public LiveLockObject(long end, String id, long start) {
        this.end = end;
        this.id = id;
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }
}
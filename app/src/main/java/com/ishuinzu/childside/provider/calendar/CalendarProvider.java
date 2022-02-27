package com.ishuinzu.childside.provider.calendar;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;

import com.ishuinzu.childside.core.AbstractProvider;
import com.ishuinzu.childside.core.Data;

public class CalendarProvider extends AbstractProvider {
    public CalendarProvider(Context context) {
        super(context);
    }

    public Data<Calendar> getCalendars() {
        Data<Calendar> calendars = getContentTableData(Calendar.uri, Calendar.class);
        return calendars;
    }

    public Calendar getCalendar(long calendarId) {
        String selection = "(" + CalendarContract.Calendars._ID + " = ?)";
        String[] selectionArgs = new String[]{String.valueOf(calendarId)};
        Calendar calendar = getContentRowData(Calendar.uri, selection, selectionArgs, null, Calendar.class);
        return calendar;
    }

    public Data<Event> getEvents(long calendarId) {
        String selection = "(" + CalendarContract.Events.CALENDAR_ID + " = ?)";
        String[] selectionArgs = new String[]{String.valueOf(calendarId)};
        Data<Event> events = getContentTableData(Event.uri, selection, selectionArgs, null, Event.class);
        return events;
    }

    public Event getEvent(long eventId) {
        String selection = "(" + CalendarContract.Events._ID + " = ?)";
        String[] selectionArgs = new String[]{String.valueOf(eventId)};
        Event event = getContentRowData(Event.uri, selection, selectionArgs, null, Event.class);
        return event;
    }

    public Data<Instance> getInstances(long begin, long end) {
        Uri.Builder builder = Instance.uri.buildUpon();
        ContentUris.appendId(builder, begin);
        ContentUris.appendId(builder, end);
        Uri uri = builder.build();
        Data<Instance> instances = getContentTableData(uri, Instance.class);
        return instances;
    }

    public Data<Instance> getInstances(long eventId, long begin, long end) {
        String selection = "(" + CalendarContract.Instances.EVENT_ID + " = ?)";
        String[] selectionArgs = new String[]{String.valueOf(eventId)};
        Uri.Builder builder = Instance.uri.buildUpon();
        ContentUris.appendId(builder, begin);
        ContentUris.appendId(builder, end);
        Uri uri = builder.build();
        Data<Instance> instances = getContentTableData(uri, selection, selectionArgs, null, Instance.class);
        return instances;
    }

    public Data<Attendee> getAttendees(long eventId) {
        String selection = "(" + CalendarContract.Attendees.EVENT_ID + "=?)";
        String[] selectionArgs = new String[]{Long.toString(eventId)};
        Data<Attendee> attendees = getContentTableData(Attendee.uri, selection, selectionArgs, null, Attendee.class);
        return attendees;
    }

    public Data<Reminder> getReminders(long eventId) {
        String selection = "(" + CalendarContract.Reminders.EVENT_ID + "=?)";
        String[] selectionArgs = new String[]{Long.toString(eventId)};
        Data<Reminder> reminders = getContentTableData(Reminder.uri, selection, selectionArgs, null, Reminder.class);
        return reminders;
    }

    public int update(Calendar calendar) {
        return updateTableRow(Calendar.uri, calendar);
    }

    public int update(Event event) {
        return updateTableRow(Event.uri, event);
    }

    public int update(Instance instance) {
        return updateTableRow(Instance.uri, instance);
    }

    public int update(Reminder reminder) {
        return updateTableRow(Reminder.uri, reminder);
    }

    public int update(Attendee attendee) {
        return updateTableRow(Attendee.uri, attendee);
    }
}
package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;

import models.Client;
import models.Event;

public class EventDAO extends DataAccessObject<Event>
{
    SQLiteStatement stm;
    String sql;

    // Singleton Pattern
    private static EventDAO instance;
    public static synchronized EventDAO getInstance(@Nullable Context context)
    {
        if (instance == null)
            instance = new EventDAO(context);

        return instance;
    }

    private EventDAO(@Nullable Context context)
    {
        super(context);
    }

    @Override
    public long insert(Event object)
    {
        sql =
            "INSERT INTO " + TABLE_EVENT +
            " (" +
                COLUMN_EVENT_TITLE + ", " +
                COLUMN_EVENT_DESCRIPTION + ", " +
                COLUMN_CLIENT_ID + ", " +
                COLUMN_EVENT_DATETIME +
            ")" +
            "VALUES" +
            "(" +
                "?, " +
                "?, " +
                "?, " +
                "strftime('%s', ?)" +
            ");";

        stm = conn.compileStatement(sql);

        stm.bindString(1, object.getTitle());
        stm.bindString(2, object.getDescription());
        stm.bindLong(3, object.getClient().getId());
        stm.bindString(4, object.getDateTime().toString());

        return stm.executeInsert();
    }

    @Override
    public ArrayList<Event> select()
    {
        ArrayList<Event> events = new ArrayList<>();

        sql =
            "SELECT " +
                TABLE_EVENT + "." + COLUMN_EVENT_ID + ", " +
                TABLE_EVENT + "." + COLUMN_EVENT_TITLE + ", " +
                TABLE_EVENT + "." + COLUMN_EVENT_DESCRIPTION + ", " +
                "strftime('%Y-%m-%dT%H:%M', " + TABLE_EVENT + "." + COLUMN_EVENT_DATETIME + ", 'unixepoch') as " + COLUMN_EVENT_DATETIME + ", " +
                TABLE_EVENT + "." + COLUMN_EVENT_CLIENT_ID + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_ID + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_CNPJ + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_NAME_CONTACT + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_TELEPHONE + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_ADDRESS + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_EMAIL + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_OBSERVATION +
            " FROM " + TABLE_EVENT +
            " INNER JOIN " + TABLE_CLIENT + " ON " + TABLE_EVENT + "." + COLUMN_EVENT_CLIENT_ID + " = " + TABLE_CLIENT + "." + COLUMN_CLIENT_ID +
            " ORDER BY " + TABLE_EVENT + "." + COLUMN_EVENT_DATETIME + ";";

        Cursor cursor = reader.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            events.add(
                new Event(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_EVENT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION)),
                    new Client(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_CNPJ)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_NAME_CONTACT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_TELEPHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_OBSERVATION))
                    ),
                    LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DATETIME)))
                )
            );
        }

        cursor.close();
        return events;
    }

    @Override
    public Event select(long id)
    {
        Event event = null;

        sql =
            "SELECT " +
                TABLE_EVENT + "." + COLUMN_EVENT_ID + ", " +
                TABLE_EVENT + "." + COLUMN_EVENT_TITLE + ", " +
                TABLE_EVENT + "." + COLUMN_EVENT_DESCRIPTION + ", " +
                "strftime('%Y-%m-%dT%H:%M', " + TABLE_EVENT + "." + COLUMN_EVENT_DATETIME + ", 'unixepoch') as " + COLUMN_EVENT_DATETIME + ", " +
                TABLE_EVENT + "." + COLUMN_EVENT_CLIENT_ID + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_ID + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_CNPJ + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_NAME_CONTACT + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_TELEPHONE + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_ADDRESS + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_EMAIL + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_OBSERVATION +
            " FROM " + TABLE_EVENT +
            " INNER JOIN " + TABLE_CLIENT + " ON " + TABLE_EVENT + "." + COLUMN_EVENT_CLIENT_ID + " = " + TABLE_CLIENT + "." + COLUMN_CLIENT_ID +
            " WHERE " + COLUMN_EVENT_ID + " = ?;";

        String[] args = {String.valueOf(id)};

        Cursor cursor = reader.rawQuery(sql, args);

        if (cursor.moveToFirst())
        {
            event = new Event(
                cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_EVENT_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION)),
                new Client(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_CNPJ)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_NAME_CONTACT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_TELEPHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_OBSERVATION))
                ),
                LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DATETIME)))
            );
        }

        cursor.close();
        return event;
    }

    @Override
    public long update(Event object)
    {
        long amountAffectedRows;

        sql =
            "UPDATE " + TABLE_EVENT +
            " SET " +
                COLUMN_EVENT_TITLE + " = ?, " +
                COLUMN_EVENT_DESCRIPTION + " = ?, " +
                COLUMN_EVENT_CLIENT_ID + " = ?, " +
                COLUMN_EVENT_DATETIME + " = strftime('%s', ?) " +
            "WHERE " + COLUMN_EVENT_ID + " = ?;";

        stm = conn.compileStatement(sql);

        stm.bindString(1, object.getTitle());
        stm.bindString(2, object.getDescription());
        stm.bindLong(3, object.getClient().getId());
        stm.bindString(4, object.getDateTime().toString());
        stm.bindLong(5, object.getId());

        amountAffectedRows = stm.executeUpdateDelete();

        return amountAffectedRows;
    }

    @Override
    public long delete(long... ids)
    {
        long amountAffectedRows;

        sql = "DELETE FROM " + TABLE_EVENT + " WHERE " + COLUMN_EVENT_ID + " = ?;";

        stm = conn.compileStatement(sql);
        stm.bindLong(1, ids[0]);

        amountAffectedRows = stm.executeUpdateDelete();

        return amountAffectedRows;
    }
}

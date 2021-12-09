package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import models.Service;

public class ServiceDAO extends DataAccessObject<Service>
{
    SQLiteStatement stm;
    String sql;

    // Singleton Pattern
    private static ServiceDAO instance;
    public static synchronized ServiceDAO getInstance(@Nullable Context context)
    {
        if (instance == null)
            instance = new ServiceDAO(context);

        return instance;
    }

    private ServiceDAO(@Nullable Context context)
    {
        super(context);
    }

    @Override
    public long insert(Service object)
    {
        sql =
            "INSERT INTO " + TABLE_SERVICE +
            " (" +
                COLUMN_SERVICE_TITLE + ", " +
                COLUMN_SERVICE_DESCRIPTION +
            ")" +
            "VALUES" +
            "(" +
                "?," +
                "?" +
            ");";

        stm = conn.compileStatement(sql);

        stm.bindString(1, object.getTitle());
        stm.bindString(2, object.getDescription());

        return stm.executeInsert();
    }

    @Override
    public ArrayList<Service> select()
    {
        ArrayList<Service> services = new ArrayList<>();

        Cursor cursor = reader.rawQuery("SELECT * FROM " + TABLE_SERVICE + ";", null);

        while (cursor.moveToNext())
            services.add(
                new Service(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_DESCRIPTION))
                )
            );

        cursor.close();
        return services;
    }

    public Service select(long id)
    {
        Service service = null;

        sql = "SELECT * FROM " + TABLE_SERVICE + " WHERE " + COLUMN_SERVICE_ID + " = ?;";
        String[] args = {String.valueOf(id)};

        Cursor cursor = reader.rawQuery(sql, args);

        if (cursor.moveToFirst()) {
            service = new Service(
                id,
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_DESCRIPTION))
            );
        }
        cursor.close();
        return service;
    }

    @Override
    public long update(Service object)
    {
        long amountAffectedRows;

        sql =
            "UPDATE " + TABLE_SERVICE +
            " SET " +
                COLUMN_SERVICE_TITLE + " = ?, " +
                COLUMN_SERVICE_DESCRIPTION + " = ? " +
            "WHERE " + COLUMN_SERVICE_ID + " = ?;";

        stm = conn.compileStatement(sql);

        stm.bindString(1, object.getTitle());
        stm.bindString(2, object.getDescription());
        stm.bindLong(3, object.getId());

        amountAffectedRows = stm.executeUpdateDelete();

        return amountAffectedRows;
    }

    @Override
    public long delete(long... ids)
    {
        sql = "DELETE FROM " + TABLE_EMPLOYEE_SERVICE + " WHERE " + COLUMN_SERVICE_ID + " = ?;";
        stm = conn.compileStatement(sql);
        stm.bindLong(1, ids[0]);
        long amountAffectedRows = stm.executeUpdateDelete();

        sql = "DELETE FROM " + TABLE_CLIENT_SERVICE + " WHERE " + COLUMN_SERVICE_ID + " = ?;";
        stm = conn.compileStatement(sql);
        stm.bindLong(1, ids[0]);
        amountAffectedRows += stm.executeUpdateDelete();

        sql = "DELETE FROM " + TABLE_SERVICE + " WHERE " + COLUMN_SERVICE_ID + " = ?;";
        stm = conn.compileStatement(sql);
        stm.bindLong(1, ids[0]);
        amountAffectedRows += stm.executeUpdateDelete();

        return amountAffectedRows;
    }

    public Service getByTitle(String name)
    {
        sql =
            "SELECT * " +
            "FROM " + TABLE_SERVICE +
            " WHERE " + COLUMN_SERVICE_TITLE + " = ?;";

        String[] args = {name};

        Cursor cursor = conn.rawQuery(sql, args);

        if (cursor.moveToNext())
        {
            Service service = new Service(
                cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_DESCRIPTION))
            );

            cursor.close();
            return service;
        }

        cursor.close();
        return null;
    }
}

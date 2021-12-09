package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import models.Client;
import models.ClientService;
import models.Service;

public class ClientServiceDAO extends DataAccessObject<ClientService>
{
    SQLiteStatement stm;
    String sql;

    // Singleton Pattern
    private static ClientServiceDAO instance;
    public static synchronized ClientServiceDAO getInstance(@Nullable Context context)
    {
        if (instance == null)
            instance = new ClientServiceDAO(context);

        return instance;
    }

    public ClientServiceDAO(@Nullable Context context) {
        super(context);
    }

    @Override
    public long insert(ClientService object)
    {
        sql =
            "INSERT INTO " + TABLE_CLIENT_SERVICE +
            "(" +
                COLUMN_CLIENT_SERVICE_CLIENT_ID + ", " +
                COLUMN_CLIENT_SERVICE_SERVICE_ID +
            ") VALUES (" +
                "?, " +
                "?" +
            ");";

        stm = conn.compileStatement(sql);
        stm.bindLong(1, object.getClient().getId());
        stm.bindLong(2, object.getService().getId());

        return stm.executeInsert();
    }

    @Override
    public ArrayList<ClientService> select() {
        ArrayList<ClientService> result = new ArrayList<>();

        sql =
            "SELECT " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_ID + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_CNPJ + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_NAME_CONTACT + " ," +
                TABLE_CLIENT + "." + COLUMN_CLIENT_TELEPHONE + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_ADDRESS + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_EMAIL + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_OBSERVATION + ", " +
                TABLE_SERVICE + "." + COLUMN_SERVICE_ID + ", " +
                TABLE_SERVICE + "." + COLUMN_SERVICE_TITLE + ", " +
                TABLE_SERVICE + "." + COLUMN_SERVICE_DESCRIPTION + " " +
            "FROM " + TABLE_CLIENT_SERVICE + " " +
                "INNER JOIN " + TABLE_CLIENT + " ON " + TABLE_CLIENT + "." + COLUMN_CLIENT_ID + " = " + TABLE_CLIENT_SERVICE + "." + COLUMN_CLIENT_SERVICE_CLIENT_ID + " " +
                "INNER JOIN " + TABLE_SERVICE + " ON " + TABLE_SERVICE + "." + COLUMN_SERVICE_ID + " = " + TABLE_CLIENT_SERVICE + "." + COLUMN_CLIENT_SERVICE_SERVICE_ID + ";";

        Cursor cursor = reader.rawQuery(sql, null);

        while (cursor.moveToNext())
            result.add(
                new ClientService(
                    new Client(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_CNPJ)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_NAME_CONTACT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_TELEPHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_OBSERVATION))
                    ),
                    new Service(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_DESCRIPTION))
                    )
                )
            );

        cursor.close();
        return result;
    }

    @Override
    public ClientService select(long id) {
        return null;
    }

    public ArrayList<ClientService> getByClient(long id)
    {
        ArrayList<ClientService> result = new ArrayList<>();

        sql =
            "SELECT " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_ID + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_CNPJ + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_NAME_CONTACT + " ," +
                TABLE_CLIENT + "." + COLUMN_CLIENT_TELEPHONE + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_ADDRESS + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_EMAIL + ", " +
                TABLE_CLIENT + "." + COLUMN_CLIENT_OBSERVATION + ", " +
                TABLE_SERVICE + "." + COLUMN_SERVICE_ID + ", " +
                TABLE_SERVICE + "." + COLUMN_SERVICE_TITLE + ", " +
                TABLE_SERVICE + "." + COLUMN_SERVICE_DESCRIPTION + " " +
            "FROM " + TABLE_CLIENT_SERVICE + " " +
            "INNER JOIN " + TABLE_CLIENT + " ON " + TABLE_CLIENT + "." + COLUMN_CLIENT_ID + " = " + TABLE_CLIENT_SERVICE + "." + COLUMN_CLIENT_SERVICE_CLIENT_ID + " " +
            "INNER JOIN " + TABLE_SERVICE + " ON " + TABLE_SERVICE + "." + COLUMN_SERVICE_ID + " = " + TABLE_CLIENT_SERVICE + "." + COLUMN_CLIENT_SERVICE_SERVICE_ID + " " +
            "WHERE " + TABLE_CLIENT_SERVICE + "." + COLUMN_CLIENT_SERVICE_CLIENT_ID + " = ?;";

        String[] args = {String.valueOf(id)};

        Cursor cursor = conn.rawQuery(sql, args);

        while (cursor.moveToNext())
            result.add(
                new ClientService(
                    new Client(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_CNPJ)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_NAME_CONTACT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_TELEPHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_OBSERVATION))
                    ),
                    new Service(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_DESCRIPTION))
                    )
                )
            );

        cursor.close();
        return result;
    }

    @Override
    public long update(ClientService object)
    {
        return 0;
    }

    @Override
    public long delete(long... ids)
    {
        long amountAffectedRows;

        sql =
            "DELETE FROM " + TABLE_CLIENT_SERVICE +
            " WHERE " +
                COLUMN_CLIENT_SERVICE_CLIENT_ID + " = ? AND " +
                COLUMN_CLIENT_SERVICE_SERVICE_ID + " = ?;";

        stm = conn.compileStatement(sql);
        stm.bindLong(1, ids[0]);
        stm.bindLong(2, ids[1]);

        amountAffectedRows = stm.executeUpdateDelete();

        return amountAffectedRows;
    }

    public String[] getServicesTitles(long withoutClient)
    {
        sql =
            "SELECT " +
                COLUMN_SERVICE_TITLE +
            " FROM " + TABLE_SERVICE +
            " WHERE " + COLUMN_SERVICE_ID + " NOT IN (" +
                "SELECT " +
                    COLUMN_SERVICE_ID +
                " FROM " + TABLE_CLIENT_SERVICE +
                " WHERE " + COLUMN_CLIENT_SERVICE_CLIENT_ID + " = ?" +
            ");";

        String[] args = {String.valueOf(withoutClient)};

        Cursor cursor = conn.rawQuery(sql, args);

        String[] result = new String[cursor.getCount()];
        for (int i = 0; cursor.moveToNext(); ++i)
            result[i] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_TITLE));

        return result;
    }
}

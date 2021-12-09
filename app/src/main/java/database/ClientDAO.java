package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import models.Client;

public class ClientDAO extends DataAccessObject<Client>
{
    SQLiteStatement stm;
    String sql;

    // Singleton Pattern
    private static ClientDAO instance;
    public static synchronized ClientDAO getInstance(@Nullable Context context)
    {
        if (instance == null)
            instance = new ClientDAO(context);

        return instance;
    }

    private ClientDAO(@Nullable Context context)
    {
        super(context);
    }

    @Override
    public long insert(Client object)
    {
        sql =
            "INSERT INTO " + TABLE_CLIENT +
            " (" +
                COLUMN_CLIENT_CNPJ + ", " +
                COLUMN_CLIENT_NAME_CONTACT + ", " +
                COLUMN_CLIENT_TELEPHONE + ", " +
                COLUMN_CLIENT_ADDRESS + ", " +
                COLUMN_CLIENT_EMAIL + ", " +
                COLUMN_CLIENT_OBSERVATION +
            ")" +
            "VALUES" +
            "(" +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "?" +
            ");";

        stm = conn.compileStatement(sql);

        stm.bindString(1, object.getCnpj());
        stm.bindString(2, object.getNameContact());
        stm.bindString(3, object.getTelephone());
        stm.bindString(4, object.getAddress());
        stm.bindString(5, object.getEmail());
        stm.bindString(6, object.getObservation());

        return stm.executeInsert();
    }

    @Override
    public ArrayList<Client> select()
    {
        ArrayList<Client> clients = new ArrayList<>();

        Cursor cursor = reader.rawQuery("SELECT * FROM " + TABLE_CLIENT + ";", null);

        while (cursor.moveToNext())
            clients.add(
                new Client(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_CNPJ)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_NAME_CONTACT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_TELEPHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_OBSERVATION))
                )
            );

        cursor.close();
        return clients;
    }

    public Client select(long id)
    {
        Client client = null;

        sql = "SELECT * FROM " + TABLE_CLIENT + " WHERE " + COLUMN_CLIENT_ID + " = ?;";

        String[] args = {String.valueOf(id)};

        Cursor cursor = reader.rawQuery(sql, args);

        if (cursor.moveToFirst())
        {
            String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_ADDRESS));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_EMAIL));

            client = new Client(
                id,
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_CNPJ)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_NAME_CONTACT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_TELEPHONE)),
                address,
                email,
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_OBSERVATION))
            );
        }

        cursor.close();
        return client;
    }

    @Override
    public long update(Client object)
    {
        long amountAffectedRows;

        sql =
            "UPDATE " + TABLE_CLIENT +
            " SET " +
                    COLUMN_CLIENT_CNPJ + " = ?, " +
                COLUMN_CLIENT_NAME_CONTACT + " = ?, " +
                COLUMN_CLIENT_TELEPHONE + " = ?, " +
                COLUMN_CLIENT_ADDRESS + " = ?, " +
                COLUMN_CLIENT_EMAIL + " = ?, " +
                COLUMN_CLIENT_OBSERVATION + " = ? " +
            "WHERE " + COLUMN_CLIENT_ID + " = ?;";

        stm = conn.compileStatement(sql);

        stm.bindString(1, object.getCnpj());
        stm.bindString(2, object.getNameContact());
        stm.bindString(3, object.getTelephone());
        stm.bindString(4, object.getAddress());
        stm.bindString(5, object.getEmail());
        stm.bindString(6, object.getObservation());
        stm.bindLong(7, object.getId());

        amountAffectedRows = stm.executeUpdateDelete();

        return amountAffectedRows;
    }

    @Override
    public long delete(long... ids)
    {
        long amountAffectedRows = 0;

        sql = "DELETE FROM " + TABLE_CLIENT_SERVICE + " WHERE " + COLUMN_CLIENT_SERVICE_CLIENT_ID + " = ?;";
        stm = conn.compileStatement(sql);
        stm.bindLong(1, ids[0]);
        amountAffectedRows += stm.executeUpdateDelete();

        sql = "DELETE FROM " + TABLE_CLIENT + " WHERE " + COLUMN_CLIENT_ID + " = ?;";
        stm = conn.compileStatement(sql);
        stm.bindLong(1, ids[0]);
        amountAffectedRows += stm.executeUpdateDelete();

        return amountAffectedRows;
    }

    public String[] getClientsNames()
    {
        ArrayList<String> container = new ArrayList<>();

        sql = "SELECT " + COLUMN_CLIENT_NAME_CONTACT + " FROM " + TABLE_CLIENT;

        Cursor cursor = conn.rawQuery(sql, null);

        while (cursor.moveToNext())
            container.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_NAME_CONTACT)));

        String[] result = new String[container.size()];

        for (int i = 0; i < container.size(); i++)
            result[i] = container.get(i);

        cursor.close();
        return result;
    }

    public Client getByName(String name)
    {
        sql =
            "SELECT * " +
            "FROM " + TABLE_CLIENT +
            " WHERE " + COLUMN_CLIENT_NAME_CONTACT + " = ?";

        String[] args = {name};

        Cursor cursor = conn.rawQuery(sql, args);

        if (cursor.moveToFirst())
        {
            Client client = new Client(
                cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_CNPJ)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_NAME_CONTACT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_TELEPHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_ADDRESS)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_OBSERVATION))
            );

            cursor.close();
            return client;
        }

        cursor.close();
        return null;
    }
}

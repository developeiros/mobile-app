package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import models.Employee;
import models.EmployeeService;
import models.Service;

public class EmployeeServiceDAO extends DataAccessObject<EmployeeService>
{
    SQLiteStatement stm;
    String sql;

    // Singleton Pattern
    private static EmployeeServiceDAO instance;
    public static synchronized EmployeeServiceDAO getInstance(@Nullable Context context)
    {
        if (instance == null)
            instance = new EmployeeServiceDAO(context);

        return instance;
    }

    private EmployeeServiceDAO(@Nullable Context context)
    {
        super(context);
    }

    @Override
    public long insert(EmployeeService object)
    {
        sql =
            "INSERT INTO " + TABLE_EMPLOYEE_SERVICE +
            " (" +
                COLUMN_EMPLOYEE_SERVICE_EMPLOYEE_ID + ", " +
                COLUMN_EMPLOYEE_SERVICE_SERVICE_ID +
            ")" +
            "VALUES" +
            "(" +
                "?, " +
                "?" +
            ");";

        stm = conn.compileStatement(sql);

        stm.bindLong(1, object.getEmployee().getId());
        stm.bindLong(2, object.getService().getId());

        return stm.executeInsert();
    }

    @Override
    public ArrayList<EmployeeService> select()
    {
        ArrayList<EmployeeService> employeeServices = new ArrayList<>();

        sql =
            "SELECT " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_NAME + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_TELEPHONE + " ," +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ADDRESS + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_EMAIL + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_LOGIN + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_PASSWORD + ", " +
                TABLE_SERVICE + "." + COLUMN_SERVICE_ID + ", " +
                TABLE_SERVICE + "." + COLUMN_SERVICE_TITLE + ", " +
                TABLE_SERVICE + "." + COLUMN_SERVICE_DESCRIPTION +
            " FROM " + TABLE_EMPLOYEE_SERVICE +
            " INNER JOIN " + TABLE_EMPLOYEE + " ON " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + " = " + TABLE_EMPLOYEE_SERVICE + "." + COLUMN_EMPLOYEE_SERVICE_EMPLOYEE_ID +
            " INNER JOIN " + TABLE_SERVICE + " ON " + TABLE_SERVICE + "." + COLUMN_SERVICE_ID + " = " + TABLE_EMPLOYEE_SERVICE + "." + COLUMN_EMPLOYEE_SERVICE_SERVICE_ID + ";";

        Cursor cursor = reader.rawQuery(sql, null);

        while (cursor.moveToNext())
            employeeServices.add(
                new EmployeeService(
                    new Employee(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_TELEPHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_LOGIN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_PASSWORD))
                    ),
                    new Service(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_DESCRIPTION))
                    )
                )
            );

        cursor.close();
        return employeeServices;
    }

    @Override
    public EmployeeService select(long id)
    {
        return null;
    }

    public ArrayList<EmployeeService> getByEmployee(long id)
    {
        ArrayList<EmployeeService> result = new ArrayList<>();

        sql =
            "SELECT " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_NAME + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_TELEPHONE + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ADDRESS + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_EMAIL + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_LOGIN + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_PASSWORD + ", " +
                TABLE_SERVICE + "." + COLUMN_SERVICE_ID + ", " +
                TABLE_SERVICE + "." + COLUMN_SERVICE_TITLE + ", " +
                TABLE_SERVICE + "." + COLUMN_SERVICE_DESCRIPTION +
            " FROM " + TABLE_EMPLOYEE_SERVICE +
            " INNER JOIN " + TABLE_EMPLOYEE + " ON " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + " = " + TABLE_EMPLOYEE_SERVICE + "." + COLUMN_EMPLOYEE_SERVICE_EMPLOYEE_ID +
            " INNER JOIN " + TABLE_SERVICE + " ON " + TABLE_SERVICE + "." + COLUMN_SERVICE_ID + " = " + TABLE_EMPLOYEE_SERVICE + "." + COLUMN_EMPLOYEE_SERVICE_SERVICE_ID +
            " WHERE " + TABLE_EMPLOYEE_SERVICE + "." + COLUMN_EMPLOYEE_SERVICE_EMPLOYEE_ID + " = ?;";

        String[] args = {String.valueOf(id)};

        Cursor cursor = reader.rawQuery(sql, args);

        while (cursor.moveToNext())
            result.add(
                new EmployeeService(
                    new Employee(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_TELEPHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_LOGIN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_PASSWORD))
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

    public ArrayList<EmployeeService> getByService(long id)
    {
        ArrayList<EmployeeService> result = new ArrayList<>();

        sql =
            "SELECT " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_NAME + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_TELEPHONE + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ADDRESS + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_EMAIL + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_LOGIN + ", " +
                TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_PASSWORD + ", " +
                TABLE_SERVICE + "." + COLUMN_SERVICE_ID + ", " +
                TABLE_SERVICE + "." + COLUMN_SERVICE_TITLE + ", " +
                TABLE_SERVICE + "." + COLUMN_SERVICE_DESCRIPTION +
            " FROM " + TABLE_EMPLOYEE_SERVICE +
            " INNER JOIN " + TABLE_EMPLOYEE + " ON " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + " = " + TABLE_EMPLOYEE_SERVICE + "." + COLUMN_EMPLOYEE_SERVICE_EMPLOYEE_ID +
            " INNER JOIN " + TABLE_SERVICE + " ON " + TABLE_SERVICE + "." + COLUMN_SERVICE_ID + " = " + TABLE_EMPLOYEE_SERVICE + "." + COLUMN_EMPLOYEE_SERVICE_SERVICE_ID +
            " WHERE " + TABLE_EMPLOYEE_SERVICE + "." + COLUMN_EMPLOYEE_SERVICE_SERVICE_ID + " = ?;";

        String[] args = {String.valueOf(id)};

        Cursor cursor = reader.rawQuery(sql, args);

        while (cursor.moveToNext())
            result.add(
                new EmployeeService(
                    new Employee(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_TELEPHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_LOGIN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_PASSWORD))
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
    public long update(EmployeeService object)
    {
        // o update não é implementável nessa entidade associativa
        return 0;
    }

    @Override
    public long delete(long... ids)
    {
        if (ids.length != 2)
            throw new IllegalArgumentException("You must pass two parameters, according to the method documentation.");

        long amountAffectedRows;

        sql =
            "DELETE FROM " + TABLE_EMPLOYEE_SERVICE +
            " WHERE " +
                COLUMN_EMPLOYEE_SERVICE_EMPLOYEE_ID + " = ? AND " +
                COLUMN_EMPLOYEE_SERVICE_SERVICE_ID + " = ?;";

        stm = conn.compileStatement(sql);
        stm.bindLong(1, ids[0]);
        stm.bindLong(2, ids[1]);

        amountAffectedRows = stm.executeUpdateDelete();

        return amountAffectedRows;
    }

    public String[] getServicesTitles(long withoutEmployee)
    {
        sql =
            "SELECT " +
                COLUMN_SERVICE_TITLE +
            " FROM " + TABLE_SERVICE +
        " WHERE " + COLUMN_SERVICE_ID + " NOT IN (" +
            "SELECT " +
                COLUMN_SERVICE_ID +
            " FROM " + TABLE_EMPLOYEE_SERVICE +
            " WHERE " + COLUMN_EMPLOYEE_SERVICE_EMPLOYEE_ID + " = ?" +
        ");";

        String[] args = {String.valueOf(withoutEmployee)};

        Cursor cursor = conn.rawQuery(sql, args);

        String[] result = new String[cursor.getCount()];
        for (int i = 0; cursor.moveToNext(); ++i)
            result[i] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_TITLE));

        return result;
    }
}

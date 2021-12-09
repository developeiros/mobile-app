package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Random;

import email.EmailHelper;
import models.Employee;
import utils.Utils;

public class EmployeeDAO extends DataAccessObject<Employee>
{
    SQLiteStatement stm;
    String sql;

    // Singleton Pattern
    private static EmployeeDAO instance;
    public static synchronized EmployeeDAO getInstance(@Nullable Context context)
    {
        if (instance == null)
            instance = new EmployeeDAO(context);

        return instance;
    }

    private EmployeeDAO(@Nullable Context context)
    {
        super(context);
    }

    @Override
    public long insert(Employee object)
    {
        sql =
            "INSERT INTO " + TABLE_EMPLOYEE +
            " (" +
                COLUMN_EMPLOYEE_NAME + ", " +
                COLUMN_EMPLOYEE_TELEPHONE + ", " +
                COLUMN_EMPLOYEE_ADDRESS + ", " +
                COLUMN_EMPLOYEE_EMAIL + ", " +
                COLUMN_EMPLOYEE_LOGIN + ", " +
                COLUMN_EMPLOYEE_PASSWORD +
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

        stm.bindString(1, object.getName());
        stm.bindString(2, object.getTelephone());
        stm.bindString(3, object.getAddress());
        stm.bindString(4, object.getEmail());
        stm.bindString(5, object.getLogin());
        stm.bindString(6, BCrypt.hashpw(object.getPassword(), BCrypt.gensalt(HASH_COST)));

        return stm.executeInsert();
    }

    @Override
    public ArrayList<Employee> select()
    {
        ArrayList<Employee> employees = new ArrayList<>();

        Cursor cursor = reader.rawQuery("SELECT * FROM " + TABLE_EMPLOYEE + ";", null);

        while (cursor.moveToNext())
            employees.add(
                new Employee(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_TELEPHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_LOGIN)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_PASSWORD))
                )
            );

        cursor.close();
        return employees;
    }

    @Override
    public Employee select(long id)
    {
        Employee employee = null;

        sql = "SELECT * FROM " + TABLE_EMPLOYEE + " WHERE " + COLUMN_EMPLOYEE_ID + " = ?;";
        String[] args = {String.valueOf(id)};

        Cursor cursor = reader.rawQuery(sql, args);

        if (cursor.moveToFirst()) {
            employee = new Employee(
                id,
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_TELEPHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ADDRESS)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_LOGIN)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_PASSWORD))
            );
        }
        cursor.close();
        return employee;
    }

    @Override
    public long update(Employee object)
    {
        long amountAffectedRows;

        sql =
            "UPDATE " + TABLE_EMPLOYEE +
            " SET " +
                COLUMN_EMPLOYEE_NAME + " = ?, " +
                COLUMN_EMPLOYEE_TELEPHONE + " = ?, " +
                COLUMN_EMPLOYEE_ADDRESS + " = ?, " +
                COLUMN_EMPLOYEE_EMAIL + " = ?, " +
                COLUMN_EMPLOYEE_LOGIN + " = ?, " +
                COLUMN_EMPLOYEE_PASSWORD + " = ? " +
            "WHERE " + COLUMN_EMPLOYEE_ID + " = ?;";

        stm = conn.compileStatement(sql);

        stm.bindString(1, object.getName());
        stm.bindString(2, object.getTelephone());
        stm.bindString(3, object.getAddress());
        stm.bindString(4, object.getEmail());
        stm.bindString(5, object.getLogin());
        stm.bindString(6, BCrypt.hashpw(object.getPassword(), BCrypt.gensalt(HASH_COST)));
        stm.bindLong(7, object.getId());

        amountAffectedRows = stm.executeUpdateDelete();

        return amountAffectedRows;
    }

    @Override
    public long delete(long... ids)
    {
        sql = "DELETE FROM " + TABLE_EMPLOYEE_SERVICE + " WHERE " + COLUMN_EMPLOYEE_SERVICE_EMPLOYEE_ID + " = ?;";
        stm = conn.compileStatement(sql);
        stm.bindLong(1, ids[0]);
        long amountAffectedRows = stm.executeUpdateDelete();

        sql = "DELETE FROM " + TABLE_EMPLOYEE + " WHERE " + COLUMN_EMPLOYEE_ID + " =  ?;";
        stm = conn.compileStatement(sql);
        stm.bindLong(1, ids[0]);
        amountAffectedRows += stm.executeUpdateDelete();

        return amountAffectedRows;
    }

    public long authenticate(Employee newComer)
    {
        String[] args = { newComer.getLogin() };
        String hashedPassword;
        long result = -1;

        Cursor cursor =
            reader.rawQuery(
                "SELECT " +
                    COLUMN_EMPLOYEE_ID + " ," +
                    COLUMN_EMPLOYEE_PASSWORD + " " +
                "FROM " + TABLE_EMPLOYEE +
                " WHERE " + COLUMN_EMPLOYEE_LOGIN + " = ?;",
                args);

        // if the login exists:
        if (cursor.moveToFirst()) {
            hashedPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_PASSWORD));

            // if the password is right
            if (BCrypt.checkpw(newComer.getPassword(), hashedPassword))
                result = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID));
        }

        cursor.close();
        return result;
    }

    public Employee selectEmail(String email)
    {
        Employee employee = null;

        sql = "SELECT * FROM " + TABLE_EMPLOYEE + " WHERE " + COLUMN_EMPLOYEE_EMAIL + " = ?;";
        String[] args = {email};

        Cursor cursor = reader.rawQuery(sql, args);

        if (cursor.moveToFirst())
            employee = new Employee(
                cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_TELEPHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_ADDRESS)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_LOGIN)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLOYEE_PASSWORD))
            );

        cursor.close();
        return employee;
    }

    public void sendRecoveryEmail(Context context, Employee employee)
    {
        String newPassword = Utils.generatePassword(new Random().nextInt(7) + 10);
        employee.setPassword(newPassword);
        update(employee);

        String subject = "Developeiros: recuperação de senha";
        String message =
            "<h1>Recuperação de senha</h1>" +
            "<p>Olá, " + employee.getFirstName() + "!</p>" +
            "<p>Você requisitou a recuperação da sua senha. Utilize essa senha na próxima vez que tentar se logar no aplicativo:</p>" +
            "<p><strong><tt>" + employee.getPassword() + "</tt></strong></p>" +
            "<p>OBS: Não compartilhe essa senha.</p>" +
            "<p>Atenciosamente,<br />" +
            "Equipe Developeiros Corp.</p>";

        EmailHelper sender = new EmailHelper(context, employee.getEmail());
        sender.sendMail(subject, message);
    }
}

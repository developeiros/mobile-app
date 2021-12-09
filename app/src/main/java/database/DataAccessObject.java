package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class DataAccessObject<T> extends SQLiteOpenHelper
{
    protected static final String TABLE_CLIENT = "Cliente";
    protected static final String COLUMN_CLIENT_ID = "id_cliente";
    protected static final String COLUMN_CLIENT_CNPJ = "cnpj";
    protected static final String COLUMN_CLIENT_NAME_CONTACT = "nome_contato";
    protected static final String COLUMN_CLIENT_TELEPHONE = "telefone";
    protected static final String COLUMN_CLIENT_ADDRESS = "endereco";
    protected static final String COLUMN_CLIENT_EMAIL = "email";
    protected static final String COLUMN_CLIENT_OBSERVATION = "observacao";

    protected static final String TABLE_SERVICE = "Servico";
    protected static final String COLUMN_SERVICE_ID = "id_servico";
    protected static final String COLUMN_SERVICE_TITLE = "titulo";
    protected static final String COLUMN_SERVICE_DESCRIPTION = "descricao_servico";

    protected static final String TABLE_CLIENT_SERVICE = "Cliente_Servico";
    protected static final String COLUMN_CLIENT_SERVICE_CLIENT_ID = COLUMN_CLIENT_ID;
    protected static final String COLUMN_CLIENT_SERVICE_SERVICE_ID = COLUMN_SERVICE_ID;

    protected static final String TABLE_EVENT = "Evento";
    protected static final String COLUMN_EVENT_ID = "id_evento";
    protected static final String COLUMN_EVENT_TITLE = "titulo";
    protected static final String COLUMN_EVENT_DESCRIPTION = "descricao";
    protected static final String COLUMN_EVENT_CLIENT_ID = COLUMN_CLIENT_ID;
    protected static final String COLUMN_EVENT_DATETIME = "data";

    protected static final String TABLE_EMPLOYEE = "Funcionario";
    protected static final String COLUMN_EMPLOYEE_ID = "id_func";
    protected static final String COLUMN_EMPLOYEE_NAME = "nome_completo";
    protected static final String COLUMN_EMPLOYEE_TELEPHONE = "telefone";
    protected static final String COLUMN_EMPLOYEE_ADDRESS = "endereco";
    protected static final String COLUMN_EMPLOYEE_EMAIL = "email";
    protected static final String COLUMN_EMPLOYEE_LOGIN = "login";
    protected static final String COLUMN_EMPLOYEE_PASSWORD = "senha_hash";

    protected static final String TABLE_EMPLOYEE_SERVICE = "Funcionario_Servico";
    protected static final String COLUMN_EMPLOYEE_SERVICE_EMPLOYEE_ID = COLUMN_EMPLOYEE_ID;
    protected static final String COLUMN_EMPLOYEE_SERVICE_SERVICE_ID = COLUMN_SERVICE_ID;

    protected static final int HASH_COST = 10;

    protected final SQLiteDatabase conn = getWritableDatabase();
    protected final SQLiteDatabase reader = getReadableDatabase();


    public abstract long insert(T object);
    public abstract ArrayList<T> select();
    public abstract T select(long id);
    public abstract long update(T object);
    public abstract long delete(long... ids);

    public DataAccessObject(@Nullable Context context)
    {
        super(context, "developeiros", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase conn)
    {
        String sql;

        sql =
            "CREATE TABLE " + TABLE_CLIENT +
            " (" +
                COLUMN_CLIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CLIENT_CNPJ + " TEXT NOT NULL," +
                COLUMN_CLIENT_NAME_CONTACT + " TEXT NOT NULL," +
                COLUMN_CLIENT_TELEPHONE + " TEXT NOT NULL," +
                COLUMN_CLIENT_ADDRESS + " TEXT NOT NULL," +
                COLUMN_CLIENT_EMAIL + " TEXT NOT NULL," +
                COLUMN_CLIENT_OBSERVATION + " TEXT DEFAULT NULL" +
            ");";

        conn.execSQL(sql);


        sql =
            "CREATE TABLE " + TABLE_SERVICE +
            " (" +
                COLUMN_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_SERVICE_TITLE + " TEXT NOT NULL," +
                COLUMN_SERVICE_DESCRIPTION + " TEXT NOT NULL" +
            ");";

        conn.execSQL(sql);


        sql =
            "CREATE TABLE " + TABLE_CLIENT_SERVICE +
            " (" +
                COLUMN_CLIENT_ID + " INTEGER," +
                COLUMN_SERVICE_ID + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_CLIENT_SERVICE_CLIENT_ID + ") REFERENCES " + TABLE_CLIENT + "(" + COLUMN_CLIENT_ID + ")," +
                "FOREIGN KEY (" + COLUMN_CLIENT_SERVICE_SERVICE_ID + ") REFERENCES " + TABLE_SERVICE + "(" + COLUMN_SERVICE_ID + ")" +
            ");";

        conn.execSQL(sql);


        sql =
            "CREATE TABLE " + TABLE_EVENT +
            " (" +
                COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_EVENT_TITLE + " TEXT NOT NULL," +
                COLUMN_EVENT_DESCRIPTION + " TEXT NOT NULL," +
                COLUMN_EVENT_DATETIME + " INTEGER NOT NULL," +
                COLUMN_EVENT_CLIENT_ID + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_CLIENT_ID + ") REFERENCES " + TABLE_CLIENT + "(" + COLUMN_CLIENT_ID + ")" +
            ");";

        conn.execSQL(sql);


        sql =
            "CREATE TABLE " + TABLE_EMPLOYEE +
            " (" +
                COLUMN_EMPLOYEE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_EMPLOYEE_NAME + " TEXT NOT NULL," +
                COLUMN_EMPLOYEE_TELEPHONE + " TEXT NOT NULL," +
                COLUMN_EMPLOYEE_ADDRESS + " TEXT NOT NULL," +
                COLUMN_EMPLOYEE_EMAIL + " TEXT NOT NULL," +
                COLUMN_EMPLOYEE_LOGIN + " TEXT NOT NULL UNIQUE," +
                COLUMN_EMPLOYEE_PASSWORD + " TEXT NOT NULL" +
            ");";

        conn.execSQL(sql);


        sql =
            "CREATE TABLE " + TABLE_EMPLOYEE_SERVICE +
            " (" +
                COLUMN_EMPLOYEE_ID + " INTEGER," +
                COLUMN_SERVICE_ID + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_EMPLOYEE_SERVICE_EMPLOYEE_ID + ") REFERENCES " + TABLE_EMPLOYEE + " (" + COLUMN_EMPLOYEE_ID + ")," +
                "FOREIGN KEY (" + COLUMN_EMPLOYEE_SERVICE_SERVICE_ID + ") REFERENCES " + TABLE_SERVICE + " (" + COLUMN_SERVICE_ID + ")" +
            ");";

        conn.execSQL(sql);


        sql =
            "INSERT INTO " + TABLE_CLIENT +
            "(" +
                COLUMN_CLIENT_CNPJ + ", " +
                COLUMN_CLIENT_NAME_CONTACT + ", " +
                COLUMN_CLIENT_TELEPHONE + ", " +
                COLUMN_CLIENT_ADDRESS + ", " +
                COLUMN_CLIENT_EMAIL + ", " +
                COLUMN_CLIENT_OBSERVATION +
            ") VALUES (" +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "?" +
            ");";

        SQLiteStatement stm = conn.compileStatement(sql);

        stm.bindString(1, "55590984000108");
        stm.bindString(2, "Developeiros Corp");
        stm.bindString(3, "11984541261");
        stm.bindString(4, "R Guaipá 678 São Paulo SP 05089-000");
        stm.bindString(5, "developeiros@mail.com");
        stm.bindString(6, "Criado para organizar as tarefas internas da empresa.");

        stm.executeInsert();


        sql =
            "INSERT INTO " + TABLE_CLIENT +
            "(" +
                COLUMN_CLIENT_CNPJ + ", " +
                COLUMN_CLIENT_NAME_CONTACT + ", " +
                COLUMN_CLIENT_TELEPHONE + ", " +
                COLUMN_CLIENT_ADDRESS + ", " +
                COLUMN_CLIENT_EMAIL + ", " +
                COLUMN_CLIENT_OBSERVATION +
            ") VALUES (" +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "?" +
            ");";

        stm = conn.compileStatement(sql);

        stm.bindString(1, "55590984000108");
        stm.bindString(2, "Matsuo Sushi Bar");
        stm.bindString(3, "11984541261");
        stm.bindString(4, "R Guaipá 678 São Paulo SP 05089-000");
        stm.bindString(5, "matsuo@sushi.com");
        stm.bindString(6, "Restaurante japonês, gerenciado pelo Sr. Wellington Matsuo.");

        stm.executeInsert();


        sql =
            "INSERT INTO " + TABLE_EMPLOYEE +
            "(" +
                COLUMN_EMPLOYEE_NAME + ", " +
                COLUMN_EMPLOYEE_TELEPHONE + ", " +
                COLUMN_EMPLOYEE_ADDRESS + ", " +
                COLUMN_EMPLOYEE_EMAIL + ", " +
                COLUMN_EMPLOYEE_LOGIN + ", " +
                COLUMN_EMPLOYEE_PASSWORD +
            ") VALUES (" +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "?" +
            ");";

        stm = conn.compileStatement(sql);

        stm.bindString(1, "Gabriel Toshio Omiya");
        stm.bindString(2, "11941238004");
        stm.bindString(3, "Rua da Pitanga");
        stm.bindString(4, "gabrielomiya@gmail.com");
        stm.bindString(5, "gab");
        stm.bindString(6, BCrypt.hashpw("123", BCrypt.gensalt(HASH_COST)));

        stm.executeInsert();


        sql =
            "INSERT INTO " + TABLE_EMPLOYEE +
            "(" +
                COLUMN_EMPLOYEE_NAME + ", " +
                COLUMN_EMPLOYEE_TELEPHONE + ", " +
                COLUMN_EMPLOYEE_ADDRESS + ", " +
                COLUMN_EMPLOYEE_EMAIL + ", " +
                COLUMN_EMPLOYEE_LOGIN + ", " +
                COLUMN_EMPLOYEE_PASSWORD +
            ") VALUES (" +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "?" +
            ");";

        stm = conn.compileStatement(sql);

        stm.bindString(1, "Rafael Yukio Omiya");
        stm.bindString(2, "11942641285");
        stm.bindString(3, "Rua da Pitanga");
        stm.bindString(4, "zkanekiken@gmail.com");
        stm.bindString(5, "raf");
        stm.bindString(6, BCrypt.hashpw("123", BCrypt.gensalt(HASH_COST)));

        stm.executeInsert();


        sql =
            "INSERT INTO " + TABLE_EMPLOYEE +
            "(" +
                COLUMN_EMPLOYEE_NAME + ", " +
                COLUMN_EMPLOYEE_TELEPHONE + ", " +
                COLUMN_EMPLOYEE_ADDRESS + ", " +
                COLUMN_EMPLOYEE_EMAIL + ", " +
                COLUMN_EMPLOYEE_LOGIN + ", " +
                COLUMN_EMPLOYEE_PASSWORD +
            ") VALUES (" +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "?" +
            ");";

        stm = conn.compileStatement(sql);

        stm.bindString(1, "Luizínho");
        stm.bindString(2, "15614968658");
        stm.bindString(3, "Rua da Pitanga");
        stm.bindString(4, "luisfelipesdn12@gmail.com");
        stm.bindString(5, "lu");
        stm.bindString(6, BCrypt.hashpw("123", BCrypt.gensalt(HASH_COST)));

        stm.executeInsert();


        sql =
            "INSERT INTO " + TABLE_EVENT +
            " (" +
                COLUMN_EVENT_TITLE + ", " +
                COLUMN_EVENT_DESCRIPTION + ", " +
                COLUMN_EVENT_CLIENT_ID + ", " +
                COLUMN_EVENT_DATETIME +
            ") VALUES (" +
                "?, " +
                "?, " +
                "?, " +
                "strftime('%s', ?)" +
            ");";

        stm = conn.compileStatement(sql);

        stm.bindString(1, "Reunião interna");
        stm.bindString(2, "Reunião para debater a prototipagem.");
        stm.bindLong(3, 1);
        stm.bindString(4, LocalDateTime.now().toString());

        stm.executeInsert();


        sql =
            "INSERT INTO " + TABLE_SERVICE +
            "(" +
                COLUMN_SERVICE_TITLE + ", " +
                COLUMN_SERVICE_DESCRIPTION +
            ") VALUES (" +
                "?, " +
                "?" +
            ");";

        stm = conn.compileStatement(sql);

        stm.bindString(1, "Front-end");
        stm.bindString(2, "Descrição do front-end");

        stm.executeInsert();


        sql =
            "INSERT INTO " + TABLE_EMPLOYEE_SERVICE +
            " (" +
                COLUMN_EMPLOYEE_SERVICE_EMPLOYEE_ID + ", " +
                COLUMN_EMPLOYEE_SERVICE_SERVICE_ID +
            " ) VALUES (" +
                "?, " +
                "?" +
            ");";

        stm = conn.compileStatement(sql);

        stm.bindLong(1, 1);
        stm.bindLong(2, 1);

        stm.executeInsert();


        sql =
            "INSERT INTO " + TABLE_EMPLOYEE_SERVICE +
            " (" +
                COLUMN_EMPLOYEE_SERVICE_EMPLOYEE_ID + ", " +
                COLUMN_EMPLOYEE_SERVICE_SERVICE_ID +
            " ) VALUES (" +
                "?, " +
                "?" +
            ");";

        stm = conn.compileStatement(sql);

        stm.bindLong(1, 2);
        stm.bindLong(2, 1);

        stm.executeInsert();


        sql =
            "INSERT INTO " + TABLE_CLIENT_SERVICE +
            " (" +
                COLUMN_CLIENT_SERVICE_CLIENT_ID + ", " +
                COLUMN_CLIENT_SERVICE_SERVICE_ID +
            ") VALUES (" +
                "?," +
                "?" +
            ");";

        stm = conn.compileStatement(sql);

        stm.bindLong(1, 2);
        stm.bindLong(2, 1);

        stm.executeInsert();


        sql =
            "INSERT INTO " + TABLE_CLIENT_SERVICE +
            " (" +
                COLUMN_CLIENT_SERVICE_CLIENT_ID + ", " +
                COLUMN_CLIENT_SERVICE_SERVICE_ID +
            ") VALUES (" +
                "?," +
                "?" +
            ");";

        stm = conn.compileStatement(sql);

        stm.bindLong(1, 1);
        stm.bindLong(2, 1);

        stm.executeInsert();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    { }
}

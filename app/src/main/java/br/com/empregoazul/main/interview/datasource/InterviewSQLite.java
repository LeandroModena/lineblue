package br.com.empregoazul.main.interview.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import br.com.empregoazul.common.model.Company;
import br.com.empregoazul.common.model.Interview;

public class InterviewSQLite extends SQLiteOpenHelper {

    private static InterviewSQLite INSTANCE_CONNECTION;
    private static final int VERSION_DB = 1;
    private static final String NAME_DB = "db_interview";

    public InterviewSQLite(@Nullable Context context) {
        super(context, NAME_DB, null, VERSION_DB);
    }


    public static InterviewSQLite getInstance(Context context) {
        if (INSTANCE_CONNECTION == null) {
            INSTANCE_CONNECTION = new InterviewSQLite(context);
        }
        return INSTANCE_CONNECTION;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlTableInterview =
                "CREATE TABLE IF NOT EXISTS interview" +
                        "(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "company TEXT," +
                        "local TEXT," +
                        "opportunity TEXT," +
                        "date TEXT," +
                        "hour TEXT," +
                        "speak TEXT," +
                        "annotation TEXT," +
                        "status INTEGER" +
                        ")";
        db.execSQL(sqlTableInterview);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addItem(Interview interview) {
        long result = 0;
        SQLiteDatabase db = getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put("company", interview.getCompany());
            values.put("local", interview.getLocal());
            values.put("opportunity", interview.getOpportunity());
            values.put("date", interview.getDate());
            values.put("hour", interview.getHour());
            values.put("speak", interview.getSpeak());
            values.put("annotation", interview.getAnnotation());
            values.put("status", interview.getStatus());

            result = db.insert("interview", null, values);
            return result;

        } catch (SQLException e) {
            Log.e("SQLException", e.getMessage(), e);
        } finally {
            if (db != null) {
                db.close();
            }
            return result;
        }
    }

    public List<Interview> getListInterviewAll() {
        List<Interview> interviewList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor;
        String query = "SELECT * FROM interview;";

        try {
            db = getReadableDatabase();

            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                Interview interviewTemp = null;


                do {
                    interviewTemp = new Interview();
                    interviewTemp.setID(cursor.getInt(0));
                    interviewTemp.setCompany(cursor.getString(1));
                    interviewTemp.setLocal(cursor.getString(2));
                    interviewTemp.setOpportunity(cursor.getString(3));
                    interviewTemp.setDate(cursor.getString(4));
                    interviewTemp.setHour(cursor.getString(5));
                    interviewTemp.setSpeak(cursor.getString(6));
                    interviewTemp.setAnnotation(cursor.getString(7));
                    interviewTemp.setStatus(cursor.getInt(8));
                    interviewList.add(interviewTemp);

                } while (cursor.moveToNext());
            }

        } catch (SQLException e) {
            Log.d("SQLException", e.getMessage(), e);
            return null;

        } finally {
            if (db != null) {
                db.close();
            }
            return interviewList;
        }
    }

    public List<Interview> getListInterviewClose() {
        List<Interview> interviewList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor;
        String query = "SELECT * FROM interview;";

        try {
            db = getReadableDatabase();

            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                Interview interviewTemp = null;


                do {
                    interviewTemp = new Interview();
                    interviewTemp.setID(cursor.getInt(0));
                    interviewTemp.setCompany(cursor.getString(1));
                    interviewTemp.setLocal(cursor.getString(2));
                    interviewTemp.setOpportunity(cursor.getString(3));
                    interviewTemp.setDate(cursor.getString(4));
                    interviewTemp.setHour(cursor.getString(5));
                    interviewTemp.setSpeak(cursor.getString(6));
                    interviewTemp.setAnnotation(cursor.getString(7));
                    interviewTemp.setStatus(cursor.getInt(8));

                    if (interviewTemp.getStatus() == 1) {
                        interviewList.add(interviewTemp);
                    }

                } while (cursor.moveToNext());
            }

        } catch (SQLException e) {
            Log.d("SQLException", e.getMessage(), e);
            return null;

        } finally {
            if (db != null) {
                db.close();
            }
            return interviewList;
        }
    }

    public List<Interview> getListInterviewOpen() {
        List<Interview> interviewList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor;
        String query = "SELECT * FROM interview;";

        try {
            db = getReadableDatabase();

            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                Interview interviewTemp = null;


                do {
                    interviewTemp = new Interview();
                    interviewTemp.setID(cursor.getInt(0));
                    interviewTemp.setCompany(cursor.getString(1));
                    interviewTemp.setLocal(cursor.getString(2));
                    interviewTemp.setOpportunity(cursor.getString(3));
                    interviewTemp.setDate(cursor.getString(4));
                    interviewTemp.setHour(cursor.getString(5));
                    interviewTemp.setSpeak(cursor.getString(6));
                    interviewTemp.setAnnotation(cursor.getString(7));
                    interviewTemp.setStatus(cursor.getInt(8));

                    if (interviewTemp.getStatus() == 0) {
                        interviewList.add(interviewTemp);
                    }
                } while (cursor.moveToNext());
            }

        } catch (SQLException e) {
            Log.d("SQLException", e.getMessage(), e);
            return null;

        } finally {
            if (db != null) {
                db.close();
            }
            return interviewList;
        }
    }

    public boolean deleteInterviewDB(long ID) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.delete(
                    "interview",
                    "ID = ?",
                    new String[]{String.valueOf(ID)}
            );
        } catch (SQLException e) {
            Log.d("SQLException", e.getMessage(), e);
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return true;
    }

    public boolean updateInterviewDB(Interview interview) {
        SQLiteDatabase db = null;

        try {
            db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("company", interview.getCompany());
            values.put("local", interview.getLocal());
            values.put("opportunity", interview.getOpportunity());
            values.put("date", interview.getDate());
            values.put("hour", interview.getHour());
            values.put("speak", interview.getSpeak());
            values.put("annotation", interview.getAnnotation());
            values.put("status", interview.getStatus());

            int update = db.update("interview",
                    values,
                    "ID = ?",
                    new String[]{String.valueOf(interview.getID())}
            );

            if (update > 0) {
                return true;
            }

        } catch (SQLException e) {
            Log.d("SQLException", "Not update interview" + e.getMessage(), e);
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

}





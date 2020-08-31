package com.array.complainreportingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class PersonDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "people.db";
    private static final int DATABASE_VERSION = 3 ;
    public static final String TABLE_NAME = "People";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PERSON_CATEGORY = "category";
    public static final String COLUMN_PERSON_SEVERITY = "severity";
    public static final String COLUMN_PERSON_DESCRIPTION = "description";
    public static final String COLUMN_PERSON_IMAGE = "image";


    public PersonDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PERSON_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_PERSON_SEVERITY + " TEXT NOT NULL, " +
                COLUMN_PERSON_DESCRIPTION + " TEXT NOT NULL, " +
                COLUMN_PERSON_IMAGE + " BLOB NOT NULL);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
    /**create record**/
    public void saveNewPerson(Person person) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PERSON_CATEGORY, person.getCategory());
        values.put(COLUMN_PERSON_SEVERITY, person.getSeverity());
        values.put(COLUMN_PERSON_DESCRIPTION, person.getDescription());
        values.put(COLUMN_PERSON_IMAGE, person.getImageAsString());

        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    /**Query records, give options to filter results**/
    public List<Person> peopleList(String filter) {
        String query;
        if(filter.equals("")){
            //regular query
            query = "SELECT  * FROM " + TABLE_NAME;
        }else{
            //filter results by filter option provided
            query = "SELECT  * FROM " + TABLE_NAME + " ORDER BY "+ filter;
        }

        List<Person> personLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Person person;

        if (cursor.moveToFirst()) {
            do {
                person = new Person();

                person.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                person.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_CATEGORY)));
                person.setSeverity(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_SEVERITY)));
                person.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_DESCRIPTION)));
                person.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_IMAGE)));
                personLinkedList.add(person);
            } while (cursor.moveToNext());
        }


        return personLinkedList;
    }

    /**Query only 1 record**/
    public Person getPerson(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE _id="+ id;
        Cursor cursor = db.rawQuery(query, null);

        Person receivedPerson = new Person();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedPerson.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_CATEGORY)));
            receivedPerson.setSeverity(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_SEVERITY)));
            receivedPerson.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_DESCRIPTION)));
            receivedPerson.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_IMAGE)));
        }



        return receivedPerson;


    }


    /**delete record**/
    public void deletePersonRecord(long id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE _id='"+id+"'");
        Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();

    }

    /**update record**/
  /**  public void updatePersonRecord(long personId, Context context, Person updatedperson) {
        SQLiteDatabase db = this.getWritableDatabase();
        you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_NAME+" SET name ='"+ updatedperson.getName() + "', age ='" + updatedperson.getAge()+ "', occupation ='"+ updatedperson.getOccupation() + "', image ='"+ updatedperson.getImage() + "'  WHERE _id='" + personId + "'");
        Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();
*/

    }





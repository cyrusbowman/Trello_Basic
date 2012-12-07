package com.cyrusbowman.trello_basic;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "todoManager";
 
    // Contacts table name
    private static final String TABLE_TODO = "todo";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESC = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_DONE = "done";
    private static final String KEY_SYNCED = "synced";
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {    	
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + "("
                + KEY_ID + " VARCHAR(50) PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_DESC + " TEXT,"+ KEY_DATE + " VARCHAR(50)," + KEY_DONE + " INTEGER," + KEY_SYNCED + " INTEGER"  + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
 
        // Create tables again
        onCreate(db);
    }
    
    public void clearTable(){
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
    	// Create tables again
        onCreate(db);
    	db.close();
    }
	// Adding new contact
    public void addTodo(TodoItem todo) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        ContentValues values = new ContentValues();
        values.put(KEY_ID, todo.getId());
        values.put(KEY_TITLE, todo.getTitle());
        values.put(KEY_DESC, todo.getDescription());
        values.put(KEY_DATE, todo.getDate());
        values.put(KEY_DONE, todo.getDone());
        values.put(KEY_SYNCED, todo.getSynced());
        
        // Inserting Row
        db.insert(TABLE_TODO, null, values);
        db.close(); // Closing database connection
    }
     
    // Getting single contact
    public TodoItem getTodo(String id) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	 
        Cursor cursor = db.query(TABLE_TODO, new String[] { KEY_ID, KEY_TITLE, KEY_DESC, KEY_DATE, KEY_DONE, KEY_SYNCED }, KEY_ID + "=?", new String[] { id }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
        	return null;
        }
       
        if(cursor.getCount() == 0){
        	return null;
        }
        TodoItem todo = new TodoItem(cursor.getString(0),cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));
        
        // return contact
        return todo;
    }
    
    public void syncTodo(TodoItem sync){
    	int update = 0;
    	int add = 0;
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    	
    	TodoItem inDB = getTodo(sync.getId());
    	if(inDB != null){
	    	Date syncDate = null;
	    	Date inDBDate = null;
			try {
				syncDate = dateFormat.parse(sync.getDate().replace("T", " ").replace("Z", ""));
				inDBDate = dateFormat.parse(inDB.getDate().replace("T", " ").replace("Z", ""));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("Failed to parse","Could not parse date");
			}
			if(inDBDate.before(syncDate)){
	    		//sync date is newer replace with it
				update = 1;
	    	}
    	} else {
    		add = 1;
    	}
    	
    	if(update == 1){
    		this.updateTodo(sync);
    	}
    	if(add == 1){
    		this.addTodo(sync);
    	}    	
    }
     
    // Getting All Contacts
    public List<TodoItem> getAllTodos() {
    	List<TodoItem> todoList = new ArrayList<TodoItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TODO;
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	TodoItem todo = new TodoItem();
            	todo.setId(cursor.getString(0));
            	todo.setTitle(cursor.getString(1));
            	todo.setDescription(cursor.getString(2));
            	todo.setDate(cursor.getString(3));
        		todo.setDone(Integer.parseInt(cursor.getString(4)));
        		todo.setSynced(Integer.parseInt(cursor.getString(5)));
        		
                // Adding contact to list
            	todoList.add(todo);
            } while (cursor.moveToNext());
        }
     
        // return contact list
        return todoList;
    }
     
    // Getting contacts Count
    public int getTodoCount() {
    	String countQuery = "SELECT  * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }
    // Updating single contact
    public int updateTodo(TodoItem todo) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, todo.getTitle());
        values.put(KEY_DESC, todo.getDescription());
        values.put(KEY_DATE, todo.getDate());
        values.put(KEY_DONE, todo.getDone());
        values.put(KEY_SYNCED, todo.getSynced());
        
        // updating row
        return db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { todo.getId() });
    }
    
    public int updateTodoId(String oldId, String newId){
    	SQLiteDatabase db = this.getWritableDatabase();
   	 
        ContentValues values = new ContentValues();
        values.put(KEY_ID, newId);
        
        // updating row
        return db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { oldId });
    }
     
    // Deleting single contact
    public void deleteContact(TodoItem todo) {
    	SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] {todo.getId() });
        db.close();
    }
}

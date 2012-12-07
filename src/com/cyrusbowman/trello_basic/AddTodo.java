package com.cyrusbowman.trello_basic;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AddTodo extends Activity implements OnClickListener {
	
	private Button add, cancel;
	private EditText etTitle, etDescription;
	private CheckBox chkComplete;
	private DatabaseHandler db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_todo);
		
		db = new DatabaseHandler(this);

		add = (Button) findViewById(R.id.add);
		add.setOnClickListener(this);

		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		
		chkComplete = (CheckBox) findViewById(R.id.chkComplete);
		

		etTitle = (EditText) findViewById(R.id.title);
		etDescription = (EditText) findViewById(R.id.description);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.add) {
			//Add new todo
			String UDID = UUID.randomUUID().toString();
			
			
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
	        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
	        String theDate = dateFormatGmt.format(new Date());
	        Log.d("AddDate:",theDate);
			
			int done = 0;
			if(chkComplete.isChecked()){
				done = 1;
			}
			
			if(etTitle.getText().toString().length() > 0){
				db.addTodo(new TodoItem(UDID,etTitle.getText().toString(),etDescription.getText().toString(), theDate.replace(" ", "T"), done, 0));
				
				//Return to todo list
				Intent i = new Intent("com.cyrusbowman.trello_basic.TODOLIST");
				startActivity(i);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "Must specify a title", Toast.LENGTH_SHORT).show();
			}
		} else if (arg0.getId() == R.id.cancel) {
			//Return to todo list
			Intent i = new Intent("com.cyrusbowman.trello_basic.TODOLIST");
			startActivity(i);
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db.close();
	}
	

	
	
	
	
}

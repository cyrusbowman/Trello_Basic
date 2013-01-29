package com.cyrusbowman.trello_basic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class EditTodo extends Activity implements OnClickListener {

	private Button save, cancel;
	private EditText etTitle, etDescription;
	private CheckBox chkComplete;
	private DatabaseHandler db;
	private String todoId;
	private TodoItem itemEditing;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_todo);

		db = new DatabaseHandler(this);

		save = (Button) findViewById(R.id.editTodoSave);
		save.setOnClickListener(this);

		cancel = (Button) findViewById(R.id.editTodoCancel);
		cancel.setOnClickListener(this);

		chkComplete = (CheckBox) findViewById(R.id.editTodochkComplete);

		etTitle = (EditText) findViewById(R.id.editTodoTitle);
		etDescription = (EditText) findViewById(R.id.editTodoDesc);

		Bundle data = getIntent().getExtras();
		if (data != null && data.containsKey("todoId")) {
			todoId = data.getString("todoId");
			itemEditing = db.getTodo(todoId);
			if (itemEditing != null) {
				etTitle.setText(itemEditing.getTitle());
				etDescription.setText(itemEditing.getDescription());
				if (itemEditing.getDone() == 1) {
					chkComplete.setChecked(true);
				} else {
					chkComplete.setChecked(false);
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.editTodoSave) {
			// Save changes to todo

			int done = 0;
			if (chkComplete.isChecked()) {
				done = 1;
			}
			itemEditing.setDescription(etDescription.getText().toString());
			itemEditing.setDone(done);
			itemEditing.setSynced(0); // Need to sync again

			if (etTitle.getText().toString().length() > 0) {
				itemEditing.setTitle(etTitle.getText().toString());

				db.updateTodo(itemEditing);

				// Return to todo list
				Intent i = new Intent("com.cyrusbowman.trello_basic.TODOLIST");
				startActivity(i);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "Must specify a title",
						Toast.LENGTH_SHORT).show();
			}
		} else if (arg0.getId() == R.id.editTodoCancel) {
			// Return to todo list
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

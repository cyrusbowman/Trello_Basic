package com.cyrusbowman.trello_basic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TodoList extends ListActivity {


	private DatabaseHandler db;
	
	private List<TodoItem> todoItems = new ArrayList<TodoItem>();

	private String returnIntent = "com.mywayrtk.datatransfer.SETTINGS";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//Load todo items from sql
		db = new DatabaseHandler(this);
//		db.clearTable();
//		SharedPreferences prefs = PreferenceManager
//				.getDefaultSharedPreferences(getApplicationContext());
//		SharedPreferences.Editor editor = prefs.edit();
//		editor.putString("LastSync", "null");
//		editor.commit();
		
		
		fill();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.todolist_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menuSync:
			syncItems();
			break;
		case R.id.menuAdd:
			Intent i = new Intent("com.cyrusbowman.trello_basic.ADDTODO");
			startActivity(i);
			finish();
			break;
		}
		return false;
	}

	private void fill(){
		this.todoItems.clear();
		todoItems = db.getAllTodos();    
		TodoAdapter adapter = new TodoAdapter(this,R.layout.todo_item, this.todoItems);
		this.setListAdapter(adapter);
		
        for (TodoItem cn : todoItems) {
            String log = "Id: "+cn.getId()+" ,Title: " + cn.getTitle() + " ,Desc: " + cn.getDescription();
            Log.d("Name: ", log);
        }	
	}
	
	private void syncItems(){
		//Add all online items to db
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String since = prefs.getString("LastSync", "null");
		Log.d("Requst sicne", since);
		new DownloadBoard().execute("https://api.trello.com/1/board/50c1a8613a9a77a20400ec73/actions?key=b1ae1192adda1b5b61563d30d7ab403b&token=9f4879493d59a4f6779a1e024b53abb0b85700c5f69b46bc63f40505ca93c1e2&filter=createCard,updateCard&since="+since);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		int selectionRowID = (int) id;
		
		String selectedFileString = this.todoItems.get(selectionRowID).getTitle();
		int done = this.todoItems.get(selectionRowID).getDone();

//		if (selectedFileString.equals(".")) {
//			// Refresh, wont happen ever
//			this.browseTo(this.currentDirectory);
//		} else if (fileType == 3) {
//			this.upOneLevel();
//		} else {
//			File clickedFile = null;
//			switch (this.displayMode) {
//			case RELATIVE:
//				clickedFile = new File(this.currentDirectory.getAbsolutePath()
//						+ this.todoItems.get(selectionRowID).getName());
//				break;
//			case ABSOLUTE:
//				clickedFile = new File(this.directories.get(selectionRowID)
//						.getName());
//				break;
//			}
//			if (clickedFile != null)
//				this.browseTo(clickedFile);
//		}
		
	}

	public class TodoAdapter extends ArrayAdapter<TodoItem> {

		int resource;
		String response;
		Context context;

		// Initialize adapter
		public TodoAdapter(Context context, int resource,List<TodoItem> items) {
			super(context, resource, items);
			this.resource = resource;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout listline;
			// Get the current alert object
			TodoItem aTodo = getItem(position);

			// Inflate the view
			if (convertView == null) {
				listline = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater vi;
				vi = (LayoutInflater) getContext().getSystemService(inflater);
				vi.inflate(resource, listline, true);
			} else {
				listline = (LinearLayout) convertView;
			}
			
			// Get the text boxes from the listitem.xml file
			TextView todoTitle = (TextView) listline
					.findViewById(R.id.todoTitle);
			ImageView iconDoing = (ImageView) listline
					.findViewById(R.id.iconDoing);
			ImageView iconDone = (ImageView) listline
					.findViewById(R.id.iconDone);
			
			// Assign the appropriate data from our alert object above
//			fileName.setText(" " + aDirectory.getName().replaceFirst("/", ""));
//			if (aDirectory.getType() == 3) {
//				// upfolder
//				directoryIcon.setVisibility(8);
//				fileIcon.setVisibility(8);
//				upFolder.setVisibility(0);
//			} else if (aDirectory.getType() == 0) {
//				// File
//				// directoryIcon.setImageResource(R.drawable.ic_launcher);
//				directoryIcon.setVisibility(8);
//				upFolder.setVisibility(8);
//				fileIcon.setVisibility(0);
//			} else {
//				// Directory
//				directoryIcon.setVisibility(0);
//				upFolder.setVisibility(8);
//				fileIcon.setVisibility(8);
//			}
			
			todoTitle.setText(aTodo.getTitle());
			if(aTodo.getDone() == 1){
				iconDoing.setVisibility(8);
				iconDone.setVisibility(0);
			} else {
				iconDone.setVisibility(8);
				iconDoing.setVisibility(0);
			}

			return listline;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		db.close();
		super.onDestroy();
	}
	
	
	
	private class DownloadBoard extends AsyncTask<String, Integer, JSONArray> {
		protected JSONArray doInBackground(String... urls) {
			HttpResponse response = getBoard(urls[0]);

			String result = "";
			try {
				InputStream is = response.getEntity().getContent(); //Error here if no internet
				result = convertStreamToString(is);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("Result:", result);

			JSONArray json = null;
			try {
				json = new JSONArray(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return json;
		}

		protected void onPostExecute(JSONArray actions) {
			//Sync cards from web
			for (int i = 0; i < actions.length(); i++) {
				String date = null;
				JSONObject card = null;
				JSONObject action;
				JSONObject data;
				
				String cardId = "";
				String desc = "";
		        String title = "";
				try {
					action = actions.getJSONObject(i);
					data = action.getJSONObject("data");
					date = action.getString("date");
					
					card = data.getJSONObject("card");
					cardId = card.getString("id");
		        	title = card.getString("name");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					desc = card.getString("desc");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				TodoItem fromWeb = new TodoItem(cardId, title, desc, date, 0, 1);
				db.syncTodo(fromWeb);
			}
			
			//Sync unsynced todos
			List<TodoItem> todos = new ArrayList<TodoItem>();
			todos = db.getAllTodos();    
	        for (final TodoItem todo : todos) {
	            if(todo.getSynced() == 0){
	            	if(todo.getId().contains("-")){
	            		//New card
	            		new Thread(new Runnable() {
	        				public void run() {
	        					Log.d("Sending card", "New card sent to trello");
	        					HttpClient client = new DefaultHttpClient();
	        					HttpPost post = new HttpPost(
	        							"https://api.trello.com/1/cards");
	        					List<BasicNameValuePair> results = new ArrayList<BasicNameValuePair>();
	        					results.add(new BasicNameValuePair("key",
	        							"b1ae1192adda1b5b61563d30d7ab403b"));
	        					results.add(new BasicNameValuePair("token",
	        							"9f4879493d59a4f6779a1e024b53abb0b85700c5f69b46bc63f40505ca93c1e2"));
	        					results.add(new BasicNameValuePair("idList",
	        							"50c1a8613a9a77a20400ec74"));
	        					results.add(new BasicNameValuePair("name",todo.getTitle()));
	        					results.add(new BasicNameValuePair("desc",todo.getDescription()));

	        					try {
	        						post.setEntity(new UrlEncodedFormEntity(results));
	        					} catch (UnsupportedEncodingException e) {
	        						// Auto-generated catch block
	        						Log.e("Log Thread", "An error has occurred", e);
	        					}
	        					try {
	        						HttpResponse response = client.execute(post);
	        						String result = "";
	        						try {
	        							InputStream is = response.getEntity().getContent(); //Error here if no internet
	        							result = convertStreamToString(is);
	        						} catch (IllegalStateException e) {
	        							// TODO Auto-generated catch block
	        							e.printStackTrace();
	        						} catch (IOException e) {
	        							// TODO Auto-generated catch block
	        							e.printStackTrace();
	        						}
	        						Log.d("Result2:", result);
	        						JSONObject json;
	        						String newId = null;
	        						try {
	        							json = new JSONObject(result);
	        							newId = json.getString("id");
	        						} catch (JSONException e) {
	        							// TODO Auto-generated catch block
	        							e.printStackTrace();
	        						}
	        						if(newId != null){
	        							todo.setSynced(1);
	        							db.updateTodo(todo);
	        							db.updateTodoId(todo.getId(), newId);
	        						}
	        					} catch (ClientProtocolException e) {
	        						// Auto-generated catch block
	        						Log.e("Log Thread", "client protocol exception", e);
	        					} catch (IOException e) {
	        						// Auto-generated catch block
	        						Log.e("Log Thread", "io exception", e);
	        					}
	        				}
	        			}).start();
	            	} else {
	            		//Update card
	            		
	            	}
	            }
	        }	
			
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
	        String theDate = dateFormatGmt.format(new Date());
	        theDate = (theDate.replace(" ", "T") + "Z");
	        Log.d("LastSync:",theDate);
	      
	        
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("LastSync", theDate);
			editor.commit();
			fill();
		}
	}

	public static String convertStreamToString(InputStream inputStream)
			throws IOException {
		if (inputStream != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(
						inputStream, "UTF-8"), 1024);
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				inputStream.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	public HttpResponse getBoard(String url) {
		HttpResponse response = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			response = client.execute(request);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
}

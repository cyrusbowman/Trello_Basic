package com.cyrusbowman.trello_basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private Button getCards, putCard, show;
	private TextView tvLists, tvCards;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getCards = (Button) findViewById(R.id.getCard);
		getCards.setOnClickListener(this);

		putCard = (Button) findViewById(R.id.putCard);
		putCard.setOnClickListener(this);
		
		show = (Button) findViewById(R.id.showTodo);
		show.setOnClickListener(this);

		tvLists = (TextView) findViewById(R.id.tvLists);
		tvCards = (TextView) findViewById(R.id.tvCards);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.getCard) {
			Log.d("Testing", "Get Cards Pressed");

			new DownloadBoard()
					.execute("https://api.trello.com/1/board/50b8e2635571118301003340?key=b1ae1192adda1b5b61563d30d7ab403b&token=e18bc32a1791d39d0a9e1e1783114680c64ce14c5fc9b528e215fd86733d90eb&lists=open&cards=open");

		} else if (arg0.getId() == R.id.putCard) {

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
							"e18bc32a1791d39d0a9e1e1783114680c64ce14c5fc9b528e215fd86733d90eb"));
					results.add(new BasicNameValuePair("idList",
							"50b8e2635571118301003343"));
					results.add(new BasicNameValuePair("name",
							"Added this card"));
					results.add(new BasicNameValuePair("desc",
							"this card was added by android"));

					try {
						post.setEntity(new UrlEncodedFormEntity(results));
					} catch (UnsupportedEncodingException e) {
						// Auto-generated catch block
						Log.e("Log Thread", "An error has occurred", e);
					}
					try {
						client.execute(post);
					} catch (ClientProtocolException e) {
						// Auto-generated catch block
						Log.e("Log Thread", "client protocol exception", e);
					} catch (IOException e) {
						// Auto-generated catch block
						Log.e("Log Thread", "io exception", e);
					}

				}
			}).start();
		} else if(arg0.getId() == R.id.showTodo){
			Log.d("UI", "Starting");
			Intent i = new Intent("com.cyrusbowman.trello_basic.TODOLIST");
			startActivity(i);
		}
	}

	private class DownloadBoard extends AsyncTask<String, Integer, JSONObject> {
		protected JSONObject doInBackground(String... urls) {
			HttpResponse response = getBoard(urls[0]);

			String result = "";
			try {
				InputStream is = response.getEntity().getContent();
				result = convertStreamToString(is);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Log.d("Result:", result);

			JSONObject json = null;
			try {
				json = new JSONObject(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return json;
		}

		protected void onPostExecute(JSONObject result) {
			// mImageView.setImageBitmap(result);
			// result.get(name);
			try {
				Log.d("Name:", result.getString("name"));

				String strLists = "";
				JSONArray lists = result.getJSONArray("lists");
				// JSONArray NameArray = lists.getJSONArray("name");
				for (int i = 0; i < lists.length(); i++) {
					JSONObject list = lists.getJSONObject(i);
					Log.d("List " + Integer.toString(i) + " Name:",
							list.getString("name"));
					strLists = strLists + Integer.toString(i + 1) + ". "
							+ list.getString("name");
					if (i != lists.length() - 1) {
						strLists = strLists + "\n";
					}
				}
				tvLists.setText(strLists);

				String strCards = "";
				JSONArray cards = result.getJSONArray("cards");
				for (int i = 0; i < cards.length(); i++) {
					JSONObject card = cards.getJSONObject(i);
					Log.d("Card " + Integer.toString(i) + " Name:",
							card.getString("name"));
					strCards = strCards + Integer.toString(i + 1) + ". "
							+ card.getString("name");
					if (i != cards.length() - 1) {
						strCards = strCards + "\n";
					}
				}
				tvCards.setText(strCards);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

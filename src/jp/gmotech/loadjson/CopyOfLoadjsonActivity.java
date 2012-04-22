package jp.gmotech.loadjson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import android.app.ProgressDialog;
import android.content.res.Resources;

//-- debug
import android.util.Log;
@SuppressWarnings("unused")

public class CopyOfLoadjsonActivity extends Activity {
	
	private ProgressDialog dialog;
	private int numCode;
	private int count;
	private ListView view;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        setContentView(R.layout.main);
        // ListViewを取得
        //view = (ListView)findViewById(R.id.listView);

        if (isConnected(this)) {
        	
        	// ProgressDialogを作成
        	dialog = new ProgressDialog(this);
        	dialog.setTitle("Loading");
            dialog.setMessage("最新のデータを取得しています。");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(true);
            dialog.show();
        	
            try
            {
            	HttpClient client = new DefaultHttpClient();
            	String url = "http://android.saleapp.jp/getSalesAppData.php";
            	HttpGet get = new HttpGet(url);
            	HttpResponse response = client.execute(get);
        	 
            	// 接続が成功しているか確認
            	int status = response.getStatusLine().getStatusCode();
            	if (status != HttpStatus.SC_OK) throw new Exception("Error!");
            	
            	// 結果を取得
            	String source =  EntityUtils.toString( response.getEntity());
            	JSONObject json = new JSONObject(source);
            	// statがokなら続行
            	count = 0;
            	String stat = json.getString("stat");
            	if(stat.equals("ok"))
            	{
            		JSONObject apps = json.getJSONObject("apps");
            		JSONArray  appsArray = apps.getJSONArray("free");
            		numCode = appsArray.length();
            		for(int i = 0; i < numCode; i++)
            		{
            			JSONObject app = appsArray.getJSONObject(i);
            			String title = app.getString("app_title");
            			String id = app.getString("app_packagename");
            			
            		}
            	}
            	else dialog.dismiss();
            	// 接続を解除
            	client.getConnectionManager().shutdown();
            }
            catch (ClientProtocolException e){Log.d("ClientProtocolException", e.getMessage());}
            catch (IOException e){Log.d("IOException", e.getMessage());}
            catch (Exception e){Log.d("Exception", e.getMessage());}
        }
    }
    
    // リスト用のデータを保持するクラス
    public class CodeData
    {
       public String title;
       public String id;
  
       public CodeData(String title, String id)
       {
          this.title = title;
          this.id = id;
       }
    }
    
    // データをリストの表示させるためのアダプター
    public class DataAdapter extends ArrayAdapter<CodeData>
    {
       private LayoutInflater inflater;
       private List<CodeData> items;  
  
       public DataAdapter(Context context, int textViewResourceId, List<CodeData> objects)
       {
          super(context, textViewResourceId, objects);
          items = objects;
          inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       }
  
       @Override
       public View getView(int position, View convertView, ViewGroup parent)
       {
    	  CodeData item = items.get(position);
  
          View v = convertView;
          if(v == null)  v = inflater.inflate(R.layout.list_item, null);
  
          TextView tv = (TextView)v.findViewById(R.id.text);
          tv.setText(item.title);
  
          return v;
       }
    }
    
    
    /*　ネットワークの有無を確認 */
    protected boolean isConnected(Context context) {
    	ConnectivityManager connectivityManger = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo networkInfo = connectivityManger.getActiveNetworkInfo();
    	if(networkInfo == null){
            return false;
        }
    	return (networkInfo.isConnected());    
    }
    
    
}
package jp.gmotech.loadjson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class LoadjsonActivity extends Activity {
	
	private ProgressDialog dialog;
	private int numCode;
	private int count;
	private ListView view;
    private final String FILENAME = "jsondata";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        setContentView(R.layout.main);
        TextView tv = new TextView(this);
        setContentView(tv);
        
        //if (isConnected(this)) {
        	
        	// ProgressDialogを作成
        	dialog = new ProgressDialog(this);
        	dialog.setTitle("Loading");
            dialog.setMessage("最新のデータを取得しています。");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(true);
            dialog.show();
        	
            try
            {
            
            	HttpClient client = new DefaultHttpClient();
            	String url = "http://android.saleapp.jp/getdata";
            	HttpGet get = new HttpGet(url);
            	HttpResponse response = client.execute(get);
        	 
            	// 接続が成功しているか確認
            	int status = response.getStatusLine().getStatusCode();
            	if (status != HttpStatus.SC_OK) throw new Exception("Error!");
            	
            	// 結果を取得
            	String json =  EntityUtils.toString(response.getEntity());
           
            	    // ファイルを新規に作成し、データを書き込む
            	    try {
            	    	FileOutputStream mOutput = openFileOutput(FILENAME, Activity.MODE_PRIVATE);
            	    	mOutput.write(json.getBytes());
            	    	mOutput.close(); 
            	    } catch (FileNotFoundException e) {
            	    	Log.d("FileNotFoundException", e.getMessage());
            	    } catch (IOException e) {
            	    	Log.d("IOException", e.getMessage());
            	    }
            	 
            	    dialog.dismiss();    
            	    // ファイルを読み込んで表示
            	    try {
            	    	FileInputStream Input = openFileInput(FILENAME); // ストリームを開く
            	    	BufferedReader reader = new BufferedReader(new InputStreamReader(Input)); // 読み込み
            	    	StringBuffer strBuffer = new StringBuffer();
            	    	String line;
            	    	while ((line = reader.readLine()) != null) {  
            	            strBuffer.append(line);  
            	        }
            	    	reader.close(); // ストリームを閉じる
            	    	tv.setText(strBuffer.toString()); // 読み込んだ文字列を返す
            	    	
            	    } catch (FileNotFoundException e) {
            	    	Log.d("FileNotFoundException", e.getMessage());
            	    } catch (IOException e) {
            	    	Log.d("IOException", e.getMessage());
            	    }
            	    
            	// 接続を解除
            	client.getConnectionManager().shutdown();
            }
            //catch (ClientProtocolException e){Log.d("ClientProtocolException", e.getMessage());}
            //catch (IOException e){Log.d("IOException", e.getMessage());}
            catch (Exception e){Log.d("Exception", e.getMessage());}
        //}
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
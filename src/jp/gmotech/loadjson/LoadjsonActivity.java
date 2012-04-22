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
        	
        	// ProgressDialog���쐬
        	dialog = new ProgressDialog(this);
        	dialog.setTitle("Loading");
            dialog.setMessage("�ŐV�̃f�[�^���擾���Ă��܂��B");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(true);
            dialog.show();
        	
            try
            {
            
            	HttpClient client = new DefaultHttpClient();
            	String url = "http://android.saleapp.jp/getdata";
            	HttpGet get = new HttpGet(url);
            	HttpResponse response = client.execute(get);
        	 
            	// �ڑ����������Ă��邩�m�F
            	int status = response.getStatusLine().getStatusCode();
            	if (status != HttpStatus.SC_OK) throw new Exception("Error!");
            	
            	// ���ʂ��擾
            	String json =  EntityUtils.toString(response.getEntity());
           
            	    // �t�@�C����V�K�ɍ쐬���A�f�[�^����������
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
            	    // �t�@�C����ǂݍ���ŕ\��
            	    try {
            	    	FileInputStream Input = openFileInput(FILENAME); // �X�g���[�����J��
            	    	BufferedReader reader = new BufferedReader(new InputStreamReader(Input)); // �ǂݍ���
            	    	StringBuffer strBuffer = new StringBuffer();
            	    	String line;
            	    	while ((line = reader.readLine()) != null) {  
            	            strBuffer.append(line);  
            	        }
            	    	reader.close(); // �X�g���[�������
            	    	tv.setText(strBuffer.toString()); // �ǂݍ��񂾕������Ԃ�
            	    	
            	    } catch (FileNotFoundException e) {
            	    	Log.d("FileNotFoundException", e.getMessage());
            	    } catch (IOException e) {
            	    	Log.d("IOException", e.getMessage());
            	    }
            	    
            	// �ڑ�������
            	client.getConnectionManager().shutdown();
            }
            //catch (ClientProtocolException e){Log.d("ClientProtocolException", e.getMessage());}
            //catch (IOException e){Log.d("IOException", e.getMessage());}
            catch (Exception e){Log.d("Exception", e.getMessage());}
        //}
    }
     
    
    /*�@�l�b�g���[�N�̗L�����m�F */
    protected boolean isConnected(Context context) {
    	ConnectivityManager connectivityManger = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo networkInfo = connectivityManger.getActiveNetworkInfo();
    	if(networkInfo == null){
            return false;
        }
    	return (networkInfo.isConnected());    
    }
    
    
}
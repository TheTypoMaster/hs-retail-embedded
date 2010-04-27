package com.tobacco.onlinesrv.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class NetService extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

    private void getInfo(Vector cache)
    { 	
    	try
		 {			 
			 URL url = new URL(getFetchProfilesUrl());
			 InputSource stream = new InputSource(url.openStream());
			 Document doc = getDocument(stream);
			 parseDom(doc, cache);
		 }
		 catch (MalformedURLException e) 
		 {
			 e.printStackTrace();
		 }
		 catch (IOException e) {
			 e.printStackTrace();
	     }
    }
    
    private String getFetchProfilesUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	private void parseDom(Document doc, Vector cache) {
		// TODO Auto-generated method stub
		
	}

	private Document getDocument(InputSource stream) {
    	Document mDocument = null;
        if (mDocument == null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(false);
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                mDocument = builder.parse(stream);
            } catch (ParserConfigurationException e) {
                Log.d("Failed to create document builder for %1$s",
                		stream.toString());
            } catch (SAXException e) {
                Log.d("Failed to parse document %1$s", 
                		stream.toString());
            } catch (IOException e) {
                Log.d("Failed to read document %1$s", 
                		stream.toString());
            }
        }
        return mDocument;
    }

	public InputStream getHttpByGetMethod()
	{
		HttpUriRequest request =null;
		HttpResponse resp = null;
		InputStream is = null;
		DefaultHttpClient client = new DefaultHttpClient();
		
		request = new HttpGet("http://192.168.0.101:8080/AndroidServer/data.xml");
		try {
			resp = client.execute(request);

			HttpEntity entity = resp.getEntity();
			is =  entity.getContent();

			
			return is;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.i("net", "fail");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	 private void parseDom(Document doc) {
			NodeList nodelist = doc.getElementsByTagName("profile");
			for(int i =0;i<nodelist.getLength();i++){
				Element node = (Element)nodelist.item(i);
				NodeList eP = node.getElementsByTagName("name");
				Log.i("name", getNodeValue(eP));
				NodeList e2 = node.getElementsByTagName("number");
				Log.i("number", getNodeValue(e2));
			}
		}
		    private String getNodeValue(NodeList list)
		    {
		    	NodeList textlist = list.item(0).getChildNodes();
		        String str = "";
		    	for (int i = textlist.getLength(); i > 0; i--)
		    	{
		    		str = textlist.item(i-1).getNodeValue()+ str;    		
		    	}   
		    	return str;
		    }
}

package com.josenaves.facebookfriends;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphMultiResult;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;

public class FriendsActivity extends Activity {

	private static final String TAG = "FriendsActivity";
	
	private Session session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);

		session = ApplicationData.getInstance().getSession();
		
		/* make the API call to get all friends */
		requestFacebookFriends(session);
	}

	
	private void requestFacebookFriends(Session session) {
	    Request friendsRequest = createRequest(session);
	    friendsRequest.setCallback(new Request.Callback() {

	        @Override
	        public void onCompleted(Response response) {
	        	Log.d(TAG, response.toString());
	            List<GraphUser> friends = getResults(response);
	            parseUserFromFQLResponse(response);
	        }
	    });
	    friendsRequest.executeAsync();
	}
	
	
	private Request createRequest(Session session) {
	    Request request = Request.newGraphPathRequest(session, "me/friends", null);

	    Set<String> fields = new HashSet<String>();
	    String[] requiredFields = new String[] { "id", "name", "picture" };
	    fields.addAll(Arrays.asList(requiredFields));

	    Bundle parameters = request.getParameters();
	    parameters.putString("fields", TextUtils.join(",", fields));
	    request.setParameters(parameters);

	    return request;
	}
	
	private List<GraphUser> getResults(Response response) {
	    GraphMultiResult multiResult = response.getGraphObjectAs(GraphMultiResult.class);
	    GraphObjectList<GraphObject> data = multiResult.getData();
	    return data.castToListOf(GraphUser.class);
	}
	
	public static final void parseUserFromFQLResponse( Response response )
	{
	    try
	    {
	        GraphObject go  = response.getGraphObject();
	        JSONObject  jso = go.getInnerJSONObject();
	        JSONArray   arr = jso.getJSONArray( "data" );

	        for ( int i = 0; i < ( arr.length() ); i++ )
	        {
	            JSONObject json_obj = arr.getJSONObject( i );

	            String id     = json_obj.getString( "id"           );
	            String name   = json_obj.getString( "name"          );
	            String urlImg = json_obj.getString( "picture"    );
	            
	            Log.d(TAG, name);

	        }
	    }
	    catch ( Throwable t )
	    {
	        t.printStackTrace();
	    }
	}
}

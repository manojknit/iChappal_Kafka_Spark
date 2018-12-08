package sjsu.cohort9.chappal.subscriber.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firestore.v1beta1.WriteResult;

import sjsu.cohort9.chappal.subscriber.entity.UserProfile;

@Service
public class DBService {
	
	Firestore db;
	
	public DBService() {
	
		FileInputStream serviceAccount;
		try {
			serviceAccount = new FileInputStream("key415-341-24db31ba9bc4.json");
		
			GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
			FirebaseOptions options = new FirebaseOptions.Builder()
			    .setCredentials(credentials)
			    .build();
			FirebaseApp.initializeApp(options);
		
			db = FirestoreClient.getFirestore();
			
			System.out.println("**************DB Connection ***************************");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public UserProfile getUser(String userid) {
		UserProfile up = new UserProfile();
		try {
			ApiFuture<QuerySnapshot> query = db.collection("kf-user-profile").get();
			
			QuerySnapshot querySnapshot = query.get();
			List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
			for (QueryDocumentSnapshot document : documents) {
				if(document.getString("userid").equals(userid)) {
					up.setUserid(userid);
					up.setName(document.getString("Name"));
					up.setAge(document.getString("age"));
					up.setHeight(document.getString("height"));
					up.setWeight(document.getString("weight"));
					up.setGender(document.getString("gender"));
				}
			  
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return up;
	}
	
	public double getSteps(String userid) {
		double miles= 0;
		try {		
			DocumentReference docRef = db.collection("kf-step-detail").document(userid+ Calendar.getInstance().getTime().getDate());
			// asynchronously retrieve the document
			ApiFuture<DocumentSnapshot> future = docRef.get();
			
			DocumentSnapshot document = future.get();
			if (document.exists()) {
			  System.out.println("Document data: " + document.getData().get("steps"));
			  miles = Double.parseDouble(document.getData().get("steps").toString());
			} else {
			  System.out.println("No such document!");
			}		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return miles;
	}
	public void updateSteps(String userId, double steps, double minutes, double calories) {
		
		//update step details
        DocumentReference docRef = db.collection("kf-step-detail").document(userId+ Calendar.getInstance().getTime().getDate());
        System.out.println("doc ref " + docRef.get());
       
        Map<String, Object> data = new HashMap<>();
        data.put("userid", userId);
        data.put("steps", String.valueOf(steps));
        data.put("dateon", Calendar.getInstance().getTime());
        ApiFuture<com.google.cloud.firestore.WriteResult> result = docRef.set(data);
       
        //update kf-total-steps
        double miles= steps/2500; 
        DocumentReference docRef2 = db.collection("kf-total-steps").document(userId+Calendar.getInstance().getTime().getDate());
        
        Map<String, Object> data2 = new HashMap<>();
        data2.put("levels", "1");
        data2.put("miles", String.valueOf(miles));
        data2.put("totalcalories",String.valueOf(calories) );
        data2.put("totalminutes", String.valueOf(minutes));
        data2.put("userid", userId);
        data2.put("dateon", Calendar.getInstance().getTime());
        ApiFuture<com.google.cloud.firestore.WriteResult> result2 = docRef2.set(data2);        
	}
	
	public static void main(String args[]) {
//		UserProfile u = new DBService().getUser("yaminivijaya@gmail.com");
//		System.out.println(u.toString());
		
//		new DBService().updateSteps("yaminivijaya@gmail.com", 100, 10, 20);
		
		System.out.println(new DBService().getSteps("yaminivijaya@gmail.com"));
		
	}
	
}

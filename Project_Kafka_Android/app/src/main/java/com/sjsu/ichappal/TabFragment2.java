package com.sjsu.ichappal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class TabFragment2 extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userEmail;
    private TextView txtEmail, txtusername, txtage, txtheight, txtweight, txtgender;
    private String sEmail, sPassword, susername, sage, sheight, sweight, sgender;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tab2, container, false);
     txtEmail = (TextView) view.findViewById(R.id.emailval);
        txtusername = (TextView) view.findViewById(R.id.nameval);
        txtgender  = (TextView) view.findViewById(R.id.genderval);
        txtage = (TextView) view.findViewById(R.id.ageval);
        txtheight = (TextView) view.findViewById(R.id.heightval);
        txtweight = (TextView) view.findViewById(R.id.weightval);
        Log.d("useremail", userEmail);
        // Create a reference to the cities collection
        CollectionReference userprofile = db.collection("kf-user-profile");
        // Create a query against the collection.
        Query query = userprofile.whereEqualTo("userid", userEmail);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
              //  Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                if(!queryDocumentSnapshots.isEmpty()) {
                    // String use
                    Log.d("Query results" ,queryDocumentSnapshots.getDocuments().toString());
                    Log.d("query result 2 " ,queryDocumentSnapshots.getDocuments().get(0).toString());
                    DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                    Log.d("name ",doc.get("Name") + " ");
                    Log.d("age ",doc.get("age") + " ");
                    Log.d("gender ",doc.get("gender") + " ");
                    Log.d("height ",doc.get("height") + " ");
                    Log.d("weight ",doc.get("weight") + " ");
                    Log.d("userid ",doc.get("userid") + " ");
                    txtEmail.setText(doc.get("userid").toString());
                    txtusername.setText(doc.get("Name").toString());
                    txtgender.setText(doc.get("gender").toString());
                    txtage.setText(doc.get("age").toString());
                    txtheight.setText(doc.get("height").toString());
                    txtweight.setText(doc.get("weight").toString());

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
             //   Toast.makeText(getActivity(), "Failure", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userEmail = user.getEmail();


    }
    //   Log.d("query result", query.toString());

}

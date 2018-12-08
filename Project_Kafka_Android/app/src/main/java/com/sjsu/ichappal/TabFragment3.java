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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class TabFragment3 extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userid, useridappend;
    private TextView txtmiles, txtcalories, txtminutes, txtlevels, txttotalsteps;
    private String smiles, scalories, sminutes, slevels, stotalsteps;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      View view =  inflater.inflate(R.layout.fragment_tab3, container, false);
        txtmiles = (TextView) view.findViewById(R.id.milesval);
        txtcalories = (TextView) view.findViewById(R.id.caloriesval);
        txtminutes  = (TextView) view.findViewById(R.id.minutesval);
        txtlevels = (TextView) view.findViewById(R.id.levelval);
        txttotalsteps = (TextView) view.findViewById(R.id.totstepsval);
        addRealtimeUpdate();
    /*    CollectionReference usersteps = db.collection("kf-total-steps");
        // Create a query against the collection.
        Query query = usersteps.whereEqualTo("userid", userid);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                if(!queryDocumentSnapshots.isEmpty()) {
                    // String use
                    Log.d("Query results" ,queryDocumentSnapshots.getDocuments().toString());
                    Log.d("query result 2 " ,queryDocumentSnapshots.getDocuments().get(0).toString());
                    DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                    Log.d("miles ",doc.get("miles") + " ");
                    Log.d("calories ",doc.get("totalcalories") + " ");
                    Log.d("minutes ",doc.get("totalminutes") + " ");
                    Log.d("levels ",doc.get("levels") + " ");
                    Log.d("steps ",doc.get("levels") + " ");
                   // Log.d("userid ",doc.get("userid") + " ");
                    txtmiles.setText(doc.get("miles").toString());
                    txtcalories.setText(doc.get("totalcalories").toString());
                    txtminutes.setText(doc.get("totalminutes").toString());
                    txtlevels.setText(doc.get("levels").toString());
                    txttotalsteps.setText(doc.get("levels").toString());


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failure", Toast.LENGTH_LONG).show();
            }
        }); */



       // return view;


        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getEmail();
        useridappend = userid + "7";
    }


    private void addRealtimeUpdate() {
        DocumentReference totalsteplistener = db.collection("kf-total-steps").document(useridappend);
        totalsteplistener.addSnapshotListener(new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("ERROR", e.getMessage());
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                   // Toast.makeText(getActivity(), "Current data:" + documentSnapshot.getData().get("totalcalories"), Toast.LENGTH_SHORT).show();
                    txtmiles.setText(documentSnapshot.getData().get("miles").toString());
                    txtcalories.setText(documentSnapshot.getData().get("totalcalories").toString());
                    txtminutes.setText(documentSnapshot.getData().get("totalminutes").toString());
                    txtlevels.setText(documentSnapshot.getData().get("levels").toString());
                    //txttotalsteps.setText(documentSnapshot.getData().get("levels").toString());

                }
            }
        });

        DocumentReference stepdetaillistener = db.collection("kf-step-detail").document(useridappend);
        stepdetaillistener.addSnapshotListener(new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("ERROR", e.getMessage());
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                   // Toast.makeText(getActivity(), "Current data:" + documentSnapshot.getData().get("steps"), Toast.LENGTH_SHORT).show();

                    txttotalsteps.setText(documentSnapshot.getData().get("steps").toString());

                }
            }
        });




    }
}
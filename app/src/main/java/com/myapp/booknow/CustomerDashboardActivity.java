package com.myapp.booknow;

//import android.os.Bundle;
////import android.util.Log;
////
////import androidx.annotation.Nullable;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.recyclerview.widget.LinearLayoutManager;
////import androidx.recyclerview.widget.RecyclerView;
////
////import com.google.android.gms.tasks.OnSuccessListener;
////import com.google.firebase.auth.FirebaseAuth;
////
////import java.util.List;
////
/////**
//// * Dashboard (Main page) for a customer user.
//// */
////public class CustomerDashboardActivity extends AppCompatActivity {
////
////    private RecyclerView recyclerView;
////    private BusinessAdapter adapter;
////    private DBHelper dbHelper;
////
////    @Override
////    public void onCreate(@Nullable Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_customer_dashboard);
////
////        recyclerView = findViewById(R.id.businessRecyclerView);
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////
////
////        dbHelper = new DBHelper();
////
////        FirebaseAuth mAuth = FirebaseAuth.getInstance();
////        String customerId = mAuth.getCurrentUser().getUid();
////
////        dbHelper.fetchUpcomingAppointmentsForCustomer(customerId, new FirestoreCallback<List<Appointment>>() {
////
////            @Override
////            public void onSuccess(List<Appointment> result) {
////                    for (Appointment appointment : result) {
////                        Log.d("CHECK!!!!", appointment.toString());
////                    }
////
////            }
////
////            @Override
////            public void onFailure(Exception e) {
////
////            }
////        });
////
////
////        fetchBusinesses();
////
////    }
////
////    private void fetchBusinesses(){
////        dbHelper.viewBusinesses(new OnSuccessListener<List<User>>() {
////            @Override
////            public void onSuccess(List<User> businesses) {
////                adapter = new BusinessAdapter(businesses);
////                recyclerView.setAdapter(adapter);
////            }
////        });
////    }
////
////
////
////}




import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myapp.booknow.FirestoreCallback;
import com.myapp.booknow.R;
import com.myapp.booknow.Appointment;
import com.myapp.booknow.BusinessAdapter;
import com.myapp.booknow.CustomerAdapter;
import com.myapp.booknow.DBHelper;
import com.myapp.booknow.ServiceAdapter;
import com.myapp.booknow.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Dashboard (Main page) for a customer user.
 */
public class CustomerDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView recyclerView_app;
    private BusinessAdapter adapter;

    private CustomerAdapter customerAdapter;
    private List<Appointment> appointmentList;
    private DBHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        recyclerView = findViewById(R.id.businessRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView_app = findViewById(R.id.appoitmentsRecyclerView);
        recyclerView_app.setLayoutManager(new LinearLayoutManager(this));

        appointmentList = new ArrayList<>(); // Initialize with empty list or fetch from Firestore
        customerAdapter = new CustomerAdapter (appointmentList);
        recyclerView_app.setAdapter(customerAdapter);


        dbHelper = new DBHelper();
        fetchBusinesses();
        //
         fetchappointments();

    }

    private void fetchBusinesses(){
        dbHelper.viewBusinesses(new OnSuccessListener<List<User>>() {
            @Override
            public void onSuccess(List<User> businesses) {
                adapter = new BusinessAdapter(businesses);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    public void fetchappointments() {
        // Fetch services from Firestore and update the serviceItemList and serviceAdapter
        // Implement the logic to fetch services associated with the business

        //getting the businessID
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser curr_user = mAuth.getCurrentUser();
        String customer_id = null;
        if (curr_user != null) {
            customer_id = curr_user.getUid();
        }


        dbHelper.fetchUpcomingAppointmentsForCustomer(customer_id, new FirestoreCallback<List<Appointment>>() {
            @Override
            public void onSuccess(List<Appointment> result) {
                for(Appointment appointment : result){
                    Log.d("Check business names",appointment.toString()+" ");
                }
                appointmentList.clear();
                appointmentList.addAll(result);
                customerAdapter.notifyDataSetChanged();
                Log.d("aaaa",""+result.size());
            }

            @Override
            public void onFailure(Exception e) {

                Log.d("aaaa",e.getMessage());

            }
        });


    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        //refresh the list of services every time activity resumes
//        fetchappointments();
//    }
}
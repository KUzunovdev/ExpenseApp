package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.database.AppDatabase;
import com.google.android.material.button.MaterialButton;

import java.util.concurrent.Executors;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvTotalExpensesValue;
    private MaterialButton btnManageExpenses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dashboard), (v, insets) -> {

            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbarDashboard);
        setSupportActionBar(toolbar);

        // Reference the TextView from the layout
        tvTotalExpensesValue = findViewById(R.id.tvTotalExpensesValue);
        btnManageExpenses = findViewById(R.id.btnManageExpenses);

        // Get an instance of the database
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        // Query the database for total expenses in a background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            Double totalExpenses = db.expenseDao().getTotalExpenses();
            if (totalExpenses == null) {
                totalExpenses = 0.0;
            }
            double finalTotalExpenses = totalExpenses;
            runOnUiThread(() -> {
                // Update the TextView with formatted total expense value
                tvTotalExpensesValue.setText("$" + String.format("%.2f", finalTotalExpenses));
            });
        });

        // Set up button navigation: move to ExpenseListActivity when clicked
        btnManageExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ExpenseListActivity.class);
                startActivity(intent);
            }
        });

    }
}
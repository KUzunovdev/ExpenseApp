package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.models.Expense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.Executors;

public class ExpenseListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewExpenses;
    private FloatingActionButton fabAddExpense;
    private ExpenseAdapter expenseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_list);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.expense_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Set up the toolbar with back navigation
        Toolbar toolbar = findViewById(R.id.toolbarExpenseList);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        // RecyclerView and adapter setup
        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);
        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseAdapter(this);
        recyclerViewExpenses.setAdapter(expenseAdapter);


        //Adding expense
        fabAddExpense = findViewById(R.id.fabAddExpense);
        fabAddExpense.setOnClickListener(view -> {
            Intent intent = new Intent(ExpenseListActivity.this, ExpenseDetailActivity.class);
            startActivity(intent);
        });


    }

    // Reload the expense data each time the activity resumes
    @Override
    protected void onResume() {
        super.onResume();
        loadExpenses();
    }

    // Method to load expenses from the database
    private void loadExpenses() {
        // Get the database instance
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Expense> expenseList = db.expenseDao().getAllExpenses();
            runOnUiThread(() -> {
                expenseAdapter.setExpenses(expenseList);
            });
        });
    }

    //Back arrow button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
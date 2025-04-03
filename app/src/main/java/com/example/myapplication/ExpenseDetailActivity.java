package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.models.Expense;
import com.google.android.material.button.MaterialButton;

import java.util.concurrent.Executors;

public class ExpenseDetailActivity extends AppCompatActivity {

    private EditText editTextDescription, editTextAmount, editTextDate;
    private MaterialButton btnSaveExpense;
    private AppDatabase db;
    private int expenseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.expense_detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the toolbar and enable back navigation
        Toolbar toolbar = findViewById(R.id.toolbarExpenseDetail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            // This enables the back (up) button in the toolbar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        // Bind UI elements
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDate = findViewById(R.id.editTextDate);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);

        // Initialize Room database
        db = AppDatabase.getInstance(getApplicationContext());

        // Check if an expense_id was passed to this activity (for editing)
        expenseId = getIntent().getIntExtra("expense_id", -1);
        if (expenseId != -1) {
            // Edit mode: Load expense details from the database
            loadExpense(expenseId);
        }

        // Save button click listener
        btnSaveExpense.setOnClickListener(view -> saveExpense());



    }


    private void loadExpense(int id) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Expense expense = db.expenseDao().getExpenseById(id);
            if (expense != null) {
                runOnUiThread(() -> {
                    editTextDescription.setText(expense.getDescription());
                    editTextAmount.setText(String.valueOf(expense.getAmount()));
                    editTextDate.setText(expense.getDate());
                    btnSaveExpense.setText("Update Expense");
                });
            }
        });
    }
    private void saveExpense() {
        String description = editTextDescription.getText().toString().trim();
        String amountStr = editTextAmount.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();

        // Validate fields
        if (description.isEmpty() || amountStr.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an Expense object
        Expense expense = new Expense(description, amount, date);

        if (expenseId != -1) {
            // In edit mode, set the id so that Room knows to update instead of insert
            expense.setId(expenseId);
        }

        // Insert the expense in a background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            if (expenseId == -1) {
                // New expense: insert
                db.expenseDao().insert(expense);
            } else {
                // Existing expense: update
                db.expenseDao().update(expense);
            }
            runOnUiThread(() -> {
                Toast.makeText(ExpenseDetailActivity.this, expenseId == -1 ? "Expense saved" : "Expense updated", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    // Handle the back/up button click in the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
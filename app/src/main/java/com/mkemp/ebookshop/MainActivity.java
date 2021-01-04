package com.mkemp.ebookshop;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.mkemp.ebookshop.databinding.ActivityMainBinding;
import com.mkemp.ebookshop.model.Book;
import com.mkemp.ebookshop.model.Category;
import com.mkemp.ebookshop.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    private MainActivityViewModel mainActivityViewModel;
    private MainActivityClickHandlers clickHandlers;
    
    private Category selectedCategory;
    
    private ArrayList<Category> categoryList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    
        clickHandlers = new MainActivityClickHandlers();
        
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setClickHandlers(clickHandlers);
        
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mainActivityViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories)
            {
                categoryList = (ArrayList<Category>) categories;
                
                for (Category category : categories)
                {
                    Log.i("MYTAG", category.getCategoryName());
                }
                
                showOnSpinner();
            }
        });
        
        mainActivityViewModel.getBooksOfASelectedCategory(3).observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books)
            {
                for (Book book : books)
                {
                    Log.i("MYTAG", book.getBookName());
                }
            }
        });
    }
    
    private void showOnSpinner()
    {
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, categoryList);
        categoryArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.setSpinnerAdapter(categoryArrayAdapter);
    }
    
    public class MainActivityClickHandlers
    {
        public void onFABClicked(View view)
        {
            Toast.makeText(getApplicationContext(), "FAB clicked", Toast.LENGTH_LONG).show();
        }
        
        public void onSelectItem(AdapterView<?> parent, View view, int pos, long id)
        {
            selectedCategory = (Category) parent.getItemAtPosition(pos);
            
            String message = "ID is " + selectedCategory.getId() +
                    "\nName is " + selectedCategory.getCategoryName() +
                    "\nDescription is " + selectedCategory.getCategoryDescription();
            
            Toast.makeText(parent.getContext(), message, Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        //noinspection SimplifiableIfStatement
        if ( id == R.id.action_settings )
        {
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
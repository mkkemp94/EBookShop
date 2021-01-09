package com.mkemp.ebookshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.mkemp.ebookshop.databinding.ActivityMainBinding;
import com.mkemp.ebookshop.model.Book;
import com.mkemp.ebookshop.model.Category;
import com.mkemp.ebookshop.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    private MainActivityViewModel mainActivityViewModel;
    private MainActivityClickHandlers clickHandlers;
    
    private RecyclerView booksRecyclerView;
    private BooksAdapter booksAdapter;
    
    private Category selectedCategory;
    
    private ArrayList<Category> categoryList;
    private ArrayList<Book> bookList;
    
    public static final int ADD_BOOK_REQUEST_CODE = 1;
    public static final int EDIT_BOOK_REQUEST_CODE = 2;
    
    private int selectedBookId;
    
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
                showOnSpinner();
            }
        });
    }
    
    private void showOnSpinner()
    {
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, categoryList);
        categoryArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.setSpinnerAdapter(categoryArrayAdapter);
    }
    
    private void loadBooksArrayList(int categoryId)
    {
        mainActivityViewModel.getBooksOfASelectedCategory(categoryId).observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books)
            {
                bookList = (ArrayList<Book>) books;
                loadRecyclerView();
            }
        });
    }
    
    private void loadRecyclerView()
    {
        booksRecyclerView = binding.secondaryLayout.rvBooks;
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        booksRecyclerView.setHasFixedSize(true);
        
        booksAdapter = new BooksAdapter();
        booksRecyclerView.setAdapter(booksAdapter);
        
        booksAdapter.setBooks(bookList);
        booksAdapter.setListener(new BooksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book)
            {
                selectedBookId = book.getBookId();
                
                Intent intent = new Intent(MainActivity.this, AddAndEditActivity.class);
                intent.putExtra(AddAndEditActivity.BOOK_ID, selectedBookId);
                intent.putExtra(AddAndEditActivity.BOOK_NAME, book.getBookName());
                intent.putExtra(AddAndEditActivity.UNIT_PRICE, book.getUnitPrice());
                startActivityForResult(intent, EDIT_BOOK_REQUEST_CODE);
            }
        });
        
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
            {
                return false;
            }
    
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
            {
                Book bookToDelete = bookList.get(viewHolder.getAdapterPosition());
                mainActivityViewModel.deleteBook(bookToDelete);
            }
        }).attachToRecyclerView(booksRecyclerView);
    }
    
    public class MainActivityClickHandlers
    {
        public void onFABClicked(View view)
        {
            Intent intent = new Intent(MainActivity.this, AddAndEditActivity.class);
            startActivityForResult(intent, ADD_BOOK_REQUEST_CODE);
        }
        
        public void onSelectItem(AdapterView<?> parent, View view, int pos, long id)
        {
            selectedCategory = (Category) parent.getItemAtPosition(pos);
            loadBooksArrayList(selectedCategory.getId());
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    
        int selectedCategoryId = selectedCategory.getId();
        if ( requestCode == ADD_BOOK_REQUEST_CODE && resultCode == RESULT_OK )
        {
            Book book = new Book();
            book.setCategoryId(selectedCategoryId);
            book.setBookName(data.getStringExtra(AddAndEditActivity.BOOK_NAME));
            book.setUnitPrice(data.getStringExtra(AddAndEditActivity.UNIT_PRICE));
            mainActivityViewModel.addNewBook(book);
        }
        else if ( requestCode == EDIT_BOOK_REQUEST_CODE && resultCode == RESULT_OK )
        {
            Book book = new Book();
            book.setCategoryId(selectedCategoryId);
            book.setBookName(data.getStringExtra(AddAndEditActivity.BOOK_NAME));
            book.setUnitPrice(data.getStringExtra(AddAndEditActivity.UNIT_PRICE));
            book.setBookId(selectedBookId);
            mainActivityViewModel.updateBook(book);
        }
    }
}
package com.mkemp.ebookshop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mkemp.ebookshop.databinding.ActivityAddAndEditBinding;
import com.mkemp.ebookshop.model.Book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class AddAndEditActivity extends AppCompatActivity
{
    private Book book;
    public static final String BOOK_ID = "bookId";
    public static final String BOOK_NAME = "bookName";
    public static final String UNIT_PRICE = "bookPrice";
    
    private ActivityAddAndEditBinding binding;
    private AddAndEditActivityClickHandlers clickHandlers;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_and_edit);
    
        book = new Book();
        binding.setBook(book);
        
        clickHandlers = new AddAndEditActivityClickHandlers(this);
        binding.setClickHandler(clickHandlers);
    
        Intent intent = getIntent();
        if (intent.hasExtra(BOOK_ID))
        {
            setTitle("Edit Book");
            book.setBookName(intent.getStringExtra(BOOK_NAME));
            book.setUnitPrice(intent.getStringExtra(UNIT_PRICE));
        }
        else
        {
            setTitle("Add New Book");
        }
    }
    
    public class AddAndEditActivityClickHandlers
    {
        Context context;
    
        public AddAndEditActivityClickHandlers(Context context)
        {
            this.context = context;
        }
        
        public void onSubmitButtonClicked(View view)
        {
            if (book.getBookName() == null || book.getBookName().isEmpty())
            {
                Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_LONG).show();
            }
            else
            {
                Intent intent = new Intent();
                intent.putExtra(BOOK_NAME, book.getBookName());
                intent.putExtra(UNIT_PRICE, book.getUnitPrice());
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
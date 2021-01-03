package com.mkemp.ebookshop.viewmodel;

import android.app.Application;

import com.mkemp.ebookshop.model.Book;
import com.mkemp.ebookshop.model.BookRepository;
import com.mkemp.ebookshop.model.Category;
import com.mkemp.ebookshop.model.CategoryRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainActivityViewModel extends AndroidViewModel
{
    private CategoryRepository categoryRepository;
    private BookRepository bookRepository;
    private LiveData<List<Category>> allCategories;
    private LiveData<List<Book>> booksOfASelectedCategory;
    
    public MainActivityViewModel(@NonNull Application application)
    {
        super(application);
        
        categoryRepository = new CategoryRepository(application);
        bookRepository = new BookRepository(application);
    }
    
    public LiveData<List<Category>> getAllCategories()
    {
        allCategories = categoryRepository.getCategories();
        return allCategories;
    }
    
    public LiveData<List<Book>> getBooksOfASelectedCategory(int categoryId)
    {
        booksOfASelectedCategory = bookRepository.getBooks(categoryId);
        return booksOfASelectedCategory;
    }
    
    public void addNewBook(Book book)
    {
        bookRepository.insertBook(book);
    }
    
    public void updateBook(Book book)
    {
        bookRepository.updateBook(book);
    }
    
    public void deleteBook(Book book)
    {
        bookRepository.deleteBook(book);
    }
}

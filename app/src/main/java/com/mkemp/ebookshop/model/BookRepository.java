package com.mkemp.ebookshop.model;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class BookRepository
{
    private BookDao bookDao;
    private LiveData<List<Book>> books;
    
    public BookRepository(Application application)
    {
        BooksDatabase booksDatabase = BooksDatabase.getInstance(application);
        bookDao = booksDatabase.bookDao();
    }
    
    public LiveData<List<Book>> getBooks(int categoryId)
    {
        return bookDao.getBooks(categoryId);
    }
    
    public void insertBook(Book book)
    {
        new BookRepository.InsertBookAsyncTask(bookDao).execute(book);
    }
    
    public void deleteBook(Book book)
    {
        new BookRepository.DeleteBookAsyncTask(bookDao).execute(book);
    }
    
    public void updateBook(Book book)
    {
        new BookRepository.UpdateBookAsyncTask(bookDao).execute(book);
    }
    
    private static class InsertBookAsyncTask extends AsyncTask<Book, Void, Void>
    {
        private BookDao bookDao;
        
        public InsertBookAsyncTask(BookDao bookDao)
        {
            this.bookDao = bookDao;
        }
        
        @Override
        protected Void doInBackground(Book... books)
        {
            bookDao.insert(books[0]);
            return null;
        }
    }
    
    private static class DeleteBookAsyncTask extends AsyncTask<Book, Void, Void>
    {
        private BookDao bookDao;
        
        public DeleteBookAsyncTask(BookDao bookDao)
        {
            this.bookDao = bookDao;
        }
        
        @Override
        protected Void doInBackground(Book... books)
        {
            bookDao.delete(books[0]);
            return null;
        }
    }
    
    private static class UpdateBookAsyncTask extends AsyncTask<Book, Void, Void>
    {
        private BookDao bookDao;
        
        public UpdateBookAsyncTask(BookDao bookDao)
        {
            this.bookDao = bookDao;
        }
        
        @Override
        protected Void doInBackground(Book... books)
        {
            bookDao.update(books[0]);
            return null;
        }
    }
}

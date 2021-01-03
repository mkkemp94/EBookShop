package com.mkemp.ebookshop.model;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class CategoryRepository
{
    private CategoryDao categoryDao;
    private LiveData<List<Category>> categories;
    
    public CategoryRepository(Application application)
    {
        BooksDatabase booksDatabase = BooksDatabase.getInstance(application);
        categoryDao = booksDatabase.categoryDao();
    }
    
    public LiveData<List<Category>> getCategories()
    {
        return categoryDao.getAllCategories();
    }
    
    public void insertCategory(Category category)
    {
        new InsertCategoryAsyncTask(categoryDao).execute(category);
    }
    
    public void deleteCategory(Category category)
    {
        new DeleteCategoryAsyncTask(categoryDao).execute(category);
    }
    
    public void updateCategory(Category category)
    {
        new UpdateCategoryAsyncTask(categoryDao).execute(category);
    }
    
    private static class InsertCategoryAsyncTask extends AsyncTask<Category, Void, Void>
    {
        private CategoryDao categoryDao;
    
        public InsertCategoryAsyncTask(CategoryDao categoryDao)
        {
            this.categoryDao = categoryDao;
        }
    
        @Override
        protected Void doInBackground(Category... categories)
        {
            categoryDao.insert(categories[0]);
            return null;
        }
    }
    
    private static class DeleteCategoryAsyncTask extends AsyncTask<Category, Void, Void>
    {
        private CategoryDao categoryDao;
        
        public DeleteCategoryAsyncTask(CategoryDao categoryDao)
        {
            this.categoryDao = categoryDao;
        }
        
        @Override
        protected Void doInBackground(Category... categories)
        {
            categoryDao.delete(categories[0]);
            return null;
        }
    }
    
    private static class UpdateCategoryAsyncTask extends AsyncTask<Category, Void, Void>
    {
        private CategoryDao categoryDao;
        
        public UpdateCategoryAsyncTask(CategoryDao categoryDao)
        {
            this.categoryDao = categoryDao;
        }
        
        @Override
        protected Void doInBackground(Category... categories)
        {
            categoryDao.update(categories[0]);
            return null;
        }
    }
}

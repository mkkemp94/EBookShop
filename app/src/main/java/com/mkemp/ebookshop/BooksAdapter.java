package com.mkemp.ebookshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mkemp.ebookshop.databinding.BookListItemBinding;
import com.mkemp.ebookshop.model.Book;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder>
{
    private OnItemClickListener listener;
    private ArrayList<Book> books = new ArrayList<>();
    
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        BookListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.book_list_item,
                parent,
                false
        );
        return new BookViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position)
    {
            Book book = books.get(position);
            holder.binding.setBook(book);
    }
    
    @Override
    public int getItemCount()
    {
        return books.size();
    }
    
    public void setBooks(ArrayList<Book> newBooks)
    {
        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(
                new BooksDiffCallback(books, newBooks),
                false
        );
        books = newBooks;
        result.dispatchUpdatesTo(BooksAdapter.this);
    }
    
    class BookViewHolder extends RecyclerView.ViewHolder
    {
        private BookListItemBinding binding;
    
        public BookViewHolder(@NonNull BookListItemBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    int clickedPos = getAdapterPosition();
                    if (listener != null && clickedPos != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(books.get(clickedPos));
                    }
                }
            });
        }
    }
    
    public void setListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }
    
    public interface OnItemClickListener
    {
        void onItemClick(Book book);
    }
}

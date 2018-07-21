package com.andyshon.bookshelf.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.andyshon.bookshelf.R;
import com.andyshon.bookshelf.databinding.BookItemBinding;
import com.andyshon.bookshelf.model.Book;

import java.util.List;
import java.util.Objects;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    List<? extends Book> mBookList;

    @Nullable
    private final BookClickCallback mBookClickCallback;
    private final BookLongClickCallback bookLongClickCallback;

    public BookAdapter(@Nullable BookClickCallback clickCallback, @Nullable BookLongClickCallback longClickCallback) {
        mBookClickCallback = clickCallback;
        bookLongClickCallback = longClickCallback;
    }

    public void setProductList(final List<? extends Book> bookList) {
        if (mBookList == null) {
            mBookList = bookList;
            notifyItemRangeInserted(0, bookList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mBookList.size();
                }

                @Override
                public int getNewListSize() {
                    return bookList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mBookList.get(oldItemPosition).getId() == bookList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Book newBook = bookList.get(newItemPosition);
                    Book oldBook = mBookList.get(oldItemPosition);
                    return newBook.getId() == oldBook.getId()
                            && Objects.equals(newBook.getDescription(), oldBook.getDescription())
                            && Objects.equals(newBook.getName(), oldBook.getName())
                            && newBook.getPrice() == oldBook.getPrice();
                }
            });
            mBookList = bookList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BookItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.book_item, parent, false);
        binding.setCallback(mBookClickCallback);
        binding.setLongCallback(bookLongClickCallback);
        return new BookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        holder.binding.setBook(mBookList.get(position));
        System.out.println("NAME:" + mBookList.get(position).getName());
        holder.binding.name.setText(mBookList.get(position).getName());
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mBookList == null ? 0 : mBookList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {

        final BookItemBinding binding;

        public BookViewHolder(BookItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

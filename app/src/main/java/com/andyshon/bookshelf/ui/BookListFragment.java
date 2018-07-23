package com.andyshon.bookshelf.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andyshon.bookshelf.R;
import com.andyshon.bookshelf.databinding.ListFragmentBinding;
import com.andyshon.bookshelf.db.entity.BookEntity;
import com.andyshon.bookshelf.model.Book;
import com.andyshon.bookshelf.viewmodel.BookListViewModel;

import java.util.List;

public class BookListFragment extends Fragment {

    public static final String TAG = "BookListViewModel";

    private BookAdapter mBookAdapter;

    private ListFragmentBinding mBinding;

    private BookListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false);

        mBookAdapter = new BookAdapter(mBookClickCallback, bookLongClickCallback);
        mBinding.productsList.setAdapter(mBookAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(BookListViewModel.class);

        subscribeUi(viewModel);
    }

    private void subscribeUi(BookListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getBooks().observe(this, new Observer<List<BookEntity>>() {
            @Override
            public void onChanged(@Nullable List<BookEntity> myBooks) {
                if (myBooks != null) {
                    mBinding.setIsLoading(false);
                    mBookAdapter.setProductList(myBooks);
                } else {
                    mBinding.setIsLoading(true);
                }
                mBinding.executePendingBindings();
            }
        });
    }

    private final BookLongClickCallback bookLongClickCallback = book -> {
        showConfirmDialog(book);
        return true;
    };

    private final BookClickCallback mBookClickCallback = book -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            ((MainActivity) getActivity()).show(book);
        }
    };

    private void showConfirmDialog(final Book book) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Delete");
        alertDialogBuilder.setMessage("Are you sure, You wanted to delete " + book.getName() + "?");
        alertDialogBuilder.setPositiveButton("Yes",
                (arg0, arg1) -> {
                    BookEntity bookEntity = (BookEntity)book;
                    Handler handler = new Handler();
                    Thread thread = new Thread(() -> {
                        viewModel.deleteBook(bookEntity);
                        handler.post(() -> Toast.makeText(getContext(), bookEntity.getName() + " was deleted!", Toast.LENGTH_SHORT).show());
                    }); thread.start();
                });

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

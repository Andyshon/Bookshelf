package com.andyshon.bookshelf.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andyshon.bookshelf.R;
import com.andyshon.bookshelf.databinding.BookAddFragmentBinding;
import com.andyshon.bookshelf.db.entity.BookEntity;
import com.andyshon.bookshelf.viewmodel.BookListViewModel;

public class BookAddFragment extends Fragment {

    private BookListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        BookAddFragmentBinding mBinding = DataBindingUtil.inflate(inflater, R.layout.book_add_fragment, container, false);

        mBinding.setBook(new BookEntity());
        mBinding.setCallback(callback);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(BookListViewModel.class);
    }

    BookAddCallback callback = new BookAddCallback() {
        @Override
        public void bookAdd(BookEntity bookEntity) {
            Thread thread = new Thread(() -> {
                viewModel.addBook(bookEntity);
                getActivity().runOnUiThread(() -> getFragmentManager().popBackStack());
            });
            thread.start();
        }
    };

}

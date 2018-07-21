package com.andyshon.bookshelf.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false);

        mBookAdapter = new BookAdapter(mBookClickCallback, bookLongClickCallback);
        mBinding.productsList.setAdapter(mBookAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final BookListViewModel viewModel = ViewModelProviders.of(this).get(BookListViewModel.class);

        /*BookListViewModel.Factory factory = new BookListViewModel.Factory(getActivity().getApplication());
                //getActivity().getApplication(), getArguments().getInt(KEY_PRODUCT_ID));

        final BookListViewModel viewModel = ViewModelProviders.of(this, factory)
                .get(BookListViewModel.class);*/

        subscribeUi(viewModel);
    }

    private void subscribeUi(BookListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getBooks().observe(this, new Observer<List<BookEntity>>() {
            @Override
            public void onChanged(@Nullable List<BookEntity> myProducts) {
                if (myProducts != null) {
                    mBinding.setIsLoading(false);
                    mBookAdapter.setProductList(myProducts);
                } else {
                    mBinding.setIsLoading(true);
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                mBinding.executePendingBindings();
            }
        });
    }

    private final BookLongClickCallback bookLongClickCallback = new BookLongClickCallback() {
        @Override
        public boolean onLongClickMy(Book book) {
            Toast.makeText(getActivity(), "Long click on book:" + book.getName(), Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    private final BookClickCallback mBookClickCallback = new BookClickCallback() {
        @Override
        public void onClick(Book book) {

            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).show(book);
            }
        }
    };
}

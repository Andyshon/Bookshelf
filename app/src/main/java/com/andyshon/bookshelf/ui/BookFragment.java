package com.andyshon.bookshelf.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.andyshon.bookshelf.R;
import com.andyshon.bookshelf.databinding.BookFragmentBinding;
import com.andyshon.bookshelf.db.entity.BookEntity;
import com.andyshon.bookshelf.db.entity.CommentEntity;
import com.andyshon.bookshelf.model.Comment;
import com.andyshon.bookshelf.viewmodel.BookViewModel;

import java.util.List;

public class BookFragment extends Fragment {

    private static final String KEY_PRODUCT_ID = "book_id";

    private BookFragmentBinding mBinding;

    private CommentAdapter mCommentAdapter;
    private BookViewModel model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.book_fragment, container, false);

        // Create and set the adapter for the RecyclerView.
        mCommentAdapter = new CommentAdapter(mCommentClickCallback);
        mBinding.commentList.setAdapter(mCommentAdapter);
        return mBinding.getRoot();
    }

    private final CommentClickCallback mCommentClickCallback = new CommentClickCallback() {
        @Override
        public void onClick(Comment comment) {
            System.out.println("BookFragment callback call");
            CommentEntity commentEntity = (CommentEntity)comment;
            //model.deleteComment(commentEntity);

            Handler handler = new Handler();
            Thread thread = new Thread(() -> {
                model.deleteComment(commentEntity);
                handler.post(() -> Toast.makeText(getContext(), "Delete done!", Toast.LENGTH_SHORT).show());
            }); thread.start();
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BookViewModel.Factory factory = new BookViewModel.Factory(
                getActivity().getApplication(), getArguments().getInt(KEY_PRODUCT_ID));

        model = ViewModelProviders.of(this, factory).get(BookViewModel.class);


        mBinding.setBookViewModel(model);

        subscribeToModel(model);
    }

    private void subscribeToModel(final BookViewModel model) {

        // Observe product data
        model.getObservableBook().observe(this, new Observer<BookEntity>() {
            @Override
            public void onChanged(@Nullable BookEntity bookEntity) {
                model.setBook(bookEntity);
            }
        });

        // Observe comments
        model.getComments().observe(this, new Observer<List<CommentEntity>>() {
            @Override
            public void onChanged(@Nullable List<CommentEntity> commentEntities) {
                if (commentEntities != null) {
                    mBinding.setIsLoading(false);
                    mCommentAdapter.setCommentList(commentEntities);
                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });
    }

    /** Creates product fragment for specific product ID */
    public static BookFragment forProduct(int productId) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }
}

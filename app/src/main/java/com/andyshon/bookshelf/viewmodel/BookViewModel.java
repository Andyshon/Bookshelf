package com.andyshon.bookshelf.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.andyshon.bookshelf.BasicApp;
import com.andyshon.bookshelf.DataRepository;
import com.andyshon.bookshelf.db.entity.BookEntity;
import com.andyshon.bookshelf.db.entity.CommentEntity;

import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private final LiveData<BookEntity> mObservableBook;

    public ObservableField<BookEntity> book = new ObservableField<>();

    private final int mBookId;

    private final LiveData<List<CommentEntity>> mObservableComments;

    private final DataRepository dataRepository;

    public BookViewModel(@NonNull Application application, DataRepository repository, final int bookId) {
        super(application);
        mBookId = bookId;
        this.dataRepository = repository;

        mObservableComments = repository.loadComments(mBookId);
        mObservableBook = repository.loadBook(mBookId);
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    public LiveData<List<CommentEntity>> getComments() {
        return mObservableComments;
    }

    public LiveData<BookEntity> getObservableBook() {
        return mObservableBook;
    }

    public void setBook(BookEntity book) {
        this.book.set(book);
    }

    public void deleteComment(CommentEntity commentEntity) {
        System.out.println("BookViewModel deleteComment call");
        dataRepository.deleteComment(commentEntity);
    }

    /**
     * A creator is used to inject the book ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the product ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mBookId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int bookId) {
            mApplication = application;
            mBookId = bookId;
            mRepository = ((BasicApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new BookViewModel(mApplication, mRepository, mBookId);
        }
    }
}

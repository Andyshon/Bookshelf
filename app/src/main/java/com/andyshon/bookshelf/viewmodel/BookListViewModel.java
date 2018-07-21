package com.andyshon.bookshelf.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.andyshon.bookshelf.BasicApp;
import com.andyshon.bookshelf.db.entity.BookEntity;

import java.util.List;

public class BookListViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<BookEntity>> mObservableBooks;

    public BookListViewModel(Application application) {
        super(application);

        mObservableBooks = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableBooks.setValue(null);

        LiveData<List<BookEntity>> products = ((BasicApp) application).getRepository().getBooks();

        // observe the changes of the books from the database and forward them
        mObservableBooks.addSource(products, new Observer<List<BookEntity>>() {
            @Override
            public void onChanged(@Nullable List<BookEntity> productEntities) {
                mObservableBooks.setValue(productEntities);
            }
        });
    }

    /**
     * Expose the LiveData Books query so the UI can observe it.
     */
    public LiveData<List<BookEntity>> getBooks() {
        return mObservableBooks;
    }
}

package com.andyshon.bookshelf.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.andyshon.bookshelf.BasicApp;
import com.andyshon.bookshelf.DataRepository;
import com.andyshon.bookshelf.db.entity.BookEntity;

import java.util.List;

public class BookListViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<BookEntity>> mObservableBooks;

    private final DataRepository dataRepository;

    public BookListViewModel(Application application) {
        super(application);

        dataRepository = ((BasicApp) application).getRepository();

        mObservableBooks = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableBooks.setValue(null);

        LiveData<List<BookEntity>> books = ((BasicApp) application).getRepository().getBooks();

        // observe the changes of the books from the database and forward them
        mObservableBooks.addSource(books, new Observer<List<BookEntity>>() {
            @Override
            public void onChanged(@Nullable List<BookEntity> bookEntities) {
                mObservableBooks.setValue(bookEntities);
            }
        });
    }

    /**
     * Expose the LiveData Books query so the UI can observe it.
     */
    public LiveData<List<BookEntity>> getBooks() {
        return mObservableBooks;
    }

    public void deleteBook(BookEntity bookEntity) {
        dataRepository.deleteBook(bookEntity);
    }
}

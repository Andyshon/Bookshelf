package com.andyshon.bookshelf;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.andyshon.bookshelf.db.AppDatabase;
import com.andyshon.bookshelf.db.entity.BookEntity;
import com.andyshon.bookshelf.db.entity.CommentEntity;

import java.util.List;

/**
 * Repository handling the work with books and comments.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private MediatorLiveData<List<BookEntity>> mObservableBooks;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;
        mObservableBooks = new MediatorLiveData<>();

        mObservableBooks.addSource(mDatabase.bookDao().loadAllBooks(),
                productEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableBooks.postValue(productEntities);
                    }
                });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<BookEntity>> getBooks() {
        return mObservableBooks;
    }

    public LiveData<BookEntity> loadBook(final int bookId) {
        return mDatabase.bookDao().loadBook(bookId);
    }

    public LiveData<List<CommentEntity>> loadComments(final int bookId) {
        return mDatabase.commentDao().loadComments(bookId);
    }

    public void deleteComment(final CommentEntity commentEntity) {
        mDatabase.commentDao().deleteComment(commentEntity);
    }

    public void deleteBook(final BookEntity bookEntity) {
        mDatabase.bookDao().deleteBook(bookEntity);
    }

    public void addBook(final BookEntity bookEntity) {
        mDatabase.bookDao().insertSingleBook(bookEntity);
    }
}

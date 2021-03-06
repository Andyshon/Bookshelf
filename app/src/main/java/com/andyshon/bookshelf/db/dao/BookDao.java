package com.andyshon.bookshelf.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.andyshon.bookshelf.db.entity.BookEntity;

import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM books")
    LiveData<List<BookEntity>> loadAllBooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BookEntity> books);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleBook(BookEntity book);

    @Query("select * from books where id = :bookId")
    LiveData<BookEntity> loadBook(int bookId);

    @Delete
    void deleteBook(BookEntity book);
}

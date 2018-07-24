package com.andyshon.bookshelf.ui;

import com.andyshon.bookshelf.db.entity.BookEntity;

/**
 * Created by andyshon on 23.07.18.
 */

public interface BookAddCallback {
    void bookAdd(BookEntity bookEntity);
}

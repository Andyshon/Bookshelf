package com.andyshon.bookshelf.model;

import java.util.Date;

public interface Comment {
    int getId();
    int getBookId();
    String getText();
    Date getPostedAt();
}

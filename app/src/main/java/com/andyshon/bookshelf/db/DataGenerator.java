package com.andyshon.bookshelf.db;

import com.andyshon.bookshelf.db.entity.BookEntity;
import com.andyshon.bookshelf.db.entity.CommentEntity;
import com.andyshon.bookshelf.model.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Generates data to pre-populate the database
 */
public class DataGenerator {

    private static final String[] FIRST = new String[]{
            "The Harry Potter series", "To Kill a Mockingbird", "A Game of Thrones", "The Lord of the Rings", "The Martian"};
    private static final String[] SECOND = new String[]{
            "by J.K. Rowling", "by Harper Lee", "by George R.R. Martin", "by J.R.R. Tolkien", "by Andy Weir"};
    private static final String[] DESCRIPTION = new String[]{
            "Bestseller", "Bestseller", "is the best sold product on Amazon", "is \uD83D\uDCAF", "is ❤️", "is fine"};
    private static final String[] COMMENTS = new String[]{
            "Comment 1", "Comment 2", "Comment 3", "Comment 4", "Comment 5", "Comment 6"};

    public static List<BookEntity> generateBooks() {
        List<BookEntity> products = new ArrayList<>(FIRST.length * SECOND.length);
        Random rnd = new Random();
        for (int i = 0; i < FIRST.length; i++) {
            for (int j = 0; j < SECOND.length; j++) {
                BookEntity books = new BookEntity();
                books.setName(FIRST[i] + " " + SECOND[j]);
                books.setDescription(books.getName() + " " + DESCRIPTION[j]);
                books.setPrice(rnd.nextInt(140));
                books.setId(FIRST.length * i + j + 1);
                products.add(books);
            }
        }
        return products;
    }

    public static List<CommentEntity> generateCommentsForBooks(
            final List<BookEntity> books) {
        List<CommentEntity> comments = new ArrayList<>();
        Random rnd = new Random();

        for (Book book : books) {
            int commentsNumber = rnd.nextInt(5) + 1;
            for (int i = 0; i < commentsNumber; i++) {
                CommentEntity comment = new CommentEntity();
                comment.setBookId(book.getId());
                comment.setText(COMMENTS[i] + " for " + book.getName());
                comment.setPostedAt(new Date(System.currentTimeMillis()
                        - TimeUnit.DAYS.toMillis(commentsNumber - i) + TimeUnit.HOURS.toMillis(i)));
                comments.add(comment);
            }
        }

        return comments;
    }
}

package com.andyshon.bookshelf.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.andyshon.bookshelf.R;
import com.andyshon.bookshelf.model.Book;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //deleteDatabase(AppDatabase.DATABASE_NAME);
        //System.out.println("delete database");

        // Add book list fragment if this is first creation
        if (savedInstanceState == null) {
            BookListFragment fragment = new BookListFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment, BookListFragment.TAG)
                    .commit();
        }
    }

    /** Shows the book detail fragment */
    public void show(Book book) {

        BookFragment bookFragment = BookFragment.forBook(book.getId());

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("book")
                .replace(R.id.fragment_container,
                        bookFragment, null).commit();
    }

    /** Show the book add fragment */
    public void showBookAddFragment() {

        BookAddFragment bookAddFragment = new BookAddFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("bookAdd")
                .replace(R.id.fragment_container, bookAddFragment, null).commit();
    }
}

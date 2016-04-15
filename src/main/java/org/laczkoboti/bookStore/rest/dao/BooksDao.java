package org.laczkoboti.bookStore.rest.dao;

import java.util.List;

public interface BooksDao {

    public List<BookEntity> getBooks();

    public BookEntity getBookById(Long id);

    public void deleteBookById(Long id);

    public Long createBook(BookEntity book);

    public void updateBook(BookEntity book);

    public void deleteBooks();

}

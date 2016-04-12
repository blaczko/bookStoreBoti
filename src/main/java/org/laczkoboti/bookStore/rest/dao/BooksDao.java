package org.laczkoboti.bookStore.rest.dao;

import java.util.List;

/**
 *
 * @author ama
 * @see <a href="http://www.codingpedia.org/ama/spring-mybatis-integration-example/">http://www.codingpedia.org/ama/spring-mybatis-integration-example/</a>
 */
public interface BooksDao {

    public List<BookEntity> getBooks();

    public BookEntity getBookById(Long id);

    public void deleteBookById(Long id);

    public Long createBook(BookEntity book);

    public void updateBook(BookEntity book);

    public void deleteBooks();

}

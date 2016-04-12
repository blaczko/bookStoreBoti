package org.laczkoboti.bookStore.rest.dao;

import java.util.List;

/**
 *
 * @author ama
 * @see <a href="http://www.codingpedia.org/ama/spring-mybatis-integration-example/">http://www.codingpedia.org/ama/spring-mybatis-integration-example/</a>
 */
public interface BooksDao {

    public List<BookEntity> getBooks(String orderByInsertionDate);

    public List<BookEntity> getRecentBooks(int numberOfDaysToLookBack);

    /**
     * Returns a book given its id
     *
     * @param id
     * @return
     */
    public BookEntity getBookById(Long id);

    /**
     * Find book by feed
     *
     * @param feed
     * @return the book with the feed specified feed or null if not existent
     */
    public BookEntity getBookByFeed(String feed);

    public void deleteBookById(Long id);

    public Long createBook(BookEntity book);

    public void updateBook(BookEntity book);

    /** removes all books */
    public void deleteBooks();

}

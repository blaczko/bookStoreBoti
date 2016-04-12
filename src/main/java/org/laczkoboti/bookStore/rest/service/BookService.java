package org.laczkoboti.bookStore.rest.service;

import java.util.List;

import org.laczkoboti.bookStore.rest.errorhandling.AppException;
import org.laczkoboti.bookStore.rest.errorhandling.CustomReasonPhraseException;
import org.laczkoboti.bookStore.rest.resource.book.Book;

/**
 *
 * @author ama
 * @see <a href="http://www.codingpedia.org/ama/spring-mybatis-integration-example/">http://www.codingpedia.org/ama/spring-mybatis-integration-example/</a>
 */
public interface BookService {

    /*
     * ******************** Create related methods **********************
     * */
    public Long createBook(Book book) throws AppException;
    public void createBooks(List<Book> books) throws AppException;


	/*
	 ******************** Read related methods ********************
	  */
    /**
     *
     * @param orderByInsertionDate - if set, it represents the order by criteria (ASC or DESC) for displaying books
     * @param numberDaysToLookBack - if set, it represents number of days to look back for books, null
     * @return list with books coressponding to search criterias
     * @throws AppException
     */
    public List<Book> getBooks() throws AppException;

    /**
     * Returns a book given its id
     *
     * @param id
     * @return
     * @throws AppException
     */
    public Book getBookById(Long id) throws AppException;

    /*
     * ******************** Update related methods **********************
     * */
    public void updateFullyBook(Book book) throws AppException;
    public void updatePartiallyBook(Book book) throws AppException;


    /*
     * ******************** Delete related methods **********************
     * */
    public void deleteBookById(Long id);

    /** removes all books */
    public void deleteBooks();

    /*
     * ******************** Helper methods **********************
     * */
    public Book verifyBookExistenceById(Long id);

    /**
     * Empty method generating a Business Exception
     * @throws CustomReasonPhraseException
     */
    public void generateCustomReasonPhraseException() throws CustomReasonPhraseException;

}

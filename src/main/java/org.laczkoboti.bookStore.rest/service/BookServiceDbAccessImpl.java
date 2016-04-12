package org.laczkoboti.bookStore.rest.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.laczkoboti.bookStore.rest.dao.BooksDao;
import org.laczkoboti.bookStore.rest.dao.BookEntity;
import org.laczkoboti.bookStore.rest.errorhandling.AppException;
import org.laczkoboti.bookStore.rest.errorhandling.CustomReasonPhraseException;
import org.laczkoboti.bookStore.rest.filters.AppConstants;
import org.laczkoboti.bookStore.rest.helpers.NullAwareBeanUtilsBean;
import org.laczkoboti.bookStore.rest.resource.book.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class BookServiceDbAccessImpl implements BookService {

    @Autowired
    BooksDao booksDao;

    /********************* Create related methods implementation ***********************/
    @Transactional("transactionManager")
    public Long createBook(Book book) throws AppException {

        validateInputForCreation(book);

        //verify existence of resource in the db (feed must be unique)
        BookEntity bookByFeed = booksDao.getBookByFeed(book.getFeed());
        if(bookByFeed != null){
            throw new AppException(Response.Status.CONFLICT.getStatusCode(), 409, "Book with feed already existing in the database with the id " + bookByFeed.getId(),
                    "Please verify that the feed and title are properly generated", AppConstants.BLOG_POST_URL);
        }

        return booksDao.createBook(new BookEntity(book));
    }

    private void validateInputForCreation(Book book) throws AppException {
        if(book.getFeed() == null){
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 400, "Provided data not sufficient for insertion",
                    "Please verify that the feed is properly generated/set", AppConstants.BLOG_POST_URL);
        }
        if(book.getTitle() == null){
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 400, "Provided data not sufficient for insertion",
                    "Please verify that the title is properly generated/set", AppConstants.BLOG_POST_URL);
        }
        //etc...
    }

    @Transactional("transactionManager")
    public void createBooks(List<Book> books) throws AppException {
        for (Book book : books) {
            createBook(book);
        }
    }


    // ******************** Read related methods implementation **********************
    public List<Book> getBooks(String orderByInsertionDate, Integer numberDaysToLookBack) throws AppException {

        //verify optional parameter numberDaysToLookBack first
        if(numberDaysToLookBack!=null){
            List<BookEntity> recentBooks = booksDao.getRecentBooks(numberDaysToLookBack);
            return getBooksFromEntities(recentBooks);
        }

        if(isOrderByInsertionDateParameterValid(orderByInsertionDate)){
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 400, "Please set either ASC or DESC for the orderByInsertionDate parameter", null , AppConstants.BLOG_POST_URL);
        }
        List<BookEntity> books = booksDao.getBooks(orderByInsertionDate);

        return getBooksFromEntities(books);
    }

    private boolean isOrderByInsertionDateParameterValid(
            String orderByInsertionDate) {
        return orderByInsertionDate!=null
                && !("ASC".equalsIgnoreCase(orderByInsertionDate) || "DESC".equalsIgnoreCase(orderByInsertionDate));
    }

    public Book getBookById(Long id) throws AppException {
        BookEntity bookById = booksDao.getBookById(id);
        if(bookById == null){
            throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
                    404,
                    "The book you requested with id " + id + " was not found in the database",
                    "Verify the existence of the book with the id " + id + " in the database",
                    AppConstants.BLOG_POST_URL);
        }

        return new Book(booksDao.getBookById(id));
    }

    private List<Book> getBooksFromEntities(List<BookEntity> bookEntities) {
        List<Book> response = new ArrayList<Book>();
        for(BookEntity bookEntity : bookEntities){
            response.add(new Book(bookEntity));
        }

        return response;
    }

    public List<Book> getRecentBooks(int numberOfDaysToLookBack) {
        List<BookEntity> recentBooks = booksDao.getRecentBooks(numberOfDaysToLookBack);

        return getBooksFromEntities(recentBooks);
    }



    /********************* UPDATE-related methods implementation ***********************/
    @Transactional("transactionManager")
    public void updateFullyBook(Book book) throws AppException {
        //do a validation to verify FULL update with PUT
        if(isFullUpdate(book)){
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Please specify all properties for Full UPDATE",
                    "required properties - id, title, feed, lnkOnBookpedia, description" ,
                    AppConstants.BLOG_POST_URL);
        }

        Book verifyBookExistenceById = verifyBookExistenceById(book.getId());
        if(verifyBookExistenceById == null){
            throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
                    404,
                    "The resource you are trying to update does not exist in the database",
                    "Please verify existence of data in the database for the id - " + book.getId(),
                    AppConstants.BLOG_POST_URL);
        }

        booksDao.updateBook(new BookEntity(book));
    }

    /**
     * Verifies the "completeness" of book resource sent over the wire
     *
     * @param book
     * @return
     */
    private boolean isFullUpdate(Book book) {
        return book.getId() == null
                || book.getFeed() == null
                || book.getLinkOnBookpedia() == null
                || book.getTitle() == null
                || book.getDescription() == null;
    }

    /********************* DELETE-related methods implementation ***********************/
    @Transactional("transactionManager")
    public void deleteBookById(Long id) {
        booksDao.deleteBookById(id);
    }

    @Transactional("transactionManager")
    public void deleteBooks() {
        booksDao.deleteBooks();
    }

    public Book verifyBookExistenceById(Long id) {
        BookEntity bookById = booksDao.getBookById(id);
        if(bookById == null){
            return null;
        } else {
            return new Book(bookById);
        }
    }

    @Transactional("transactionManager")
    public void updatePartiallyBook(Book book) throws AppException {
        //do a validation to verify existence of the resource
        Book verifyBookExistenceById = verifyBookExistenceById(book.getId());
        if(verifyBookExistenceById == null){
            throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
                    404,
                    "The resource you are trying to update does not exist in the database",
                    "Please verify existence of data in the database for the id - " + book.getId(),
                    AppConstants.BLOG_POST_URL);
        }
        copyPartialProperties(verifyBookExistenceById, book);
        booksDao.updateBook(new BookEntity(verifyBookExistenceById));

    }

    private void copyPartialProperties(Book verifyBookExistenceById,
                                       Book book) {

        BeanUtilsBean notNull=new NullAwareBeanUtilsBean();
        try {
            notNull.copyProperties(verifyBookExistenceById, book);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void generateCustomReasonPhraseException() throws CustomReasonPhraseException {
        throw new CustomReasonPhraseException(4000, "message attached to the Custom Reason Phrase Exception");
    }

    public void setBooksDao(BooksDao booksDao) {
        this.booksDao = booksDao;
    }

}

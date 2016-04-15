package org.laczkoboti.bookStore.rest.service.integration;

import static org.mockito.Mockito.*;

import org.laczkoboti.bookStore.rest.dao.BooksDao;
import org.laczkoboti.bookStore.rest.dao.BookEntity;
import org.laczkoboti.bookStore.rest.errorhandling.AppException;
import org.laczkoboti.bookStore.rest.resource.book.Book;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.laczkoboti.bookStore.rest.service.BookServiceDbAccessImpl;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceDbAccessImplTest {

    private static final Long CREATED_PODCAST_RESOURCE_ID = Long.valueOf(1);
    private static final String SOME_FEED = "some_feed";
    private static final String SOME_TITLE = "some title";
    private static final String EXISTING_FEED = "http://quarks.de/feed";
    private static final Long SOME_ID = 13L;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    BookServiceDbAccessImpl sut;//system under test

    @Mock
    BooksDao booksDao;

    @Before
    public void setUp() throws Exception {
        sut = new BookServiceDbAccessImpl();
        sut.setBooksDao(booksDao);
    }

    @Test
    public void testCreateBook_successful() throws AppException {

        when(booksDao.createBook(any(BookEntity.class))).thenReturn(CREATED_PODCAST_RESOURCE_ID);

        Book book = new Book();
        book.setTitle(SOME_TITLE);
        Long createBook = sut.createBook(book);

        verify(booksDao, times(1)).createBook(any(BookEntity.class));

        Assert.assertTrue(createBook == CREATED_PODCAST_RESOURCE_ID);
    }

    @Test(expected=AppException.class)
    public void testCreateBook_error() throws AppException {

        Book book = new Book();
        book.setTitle(SOME_TITLE);
        sut.createBook(book);

    }

    @Test
    public void testCreateBook_validation_missingFeed() throws AppException {

        exception.expect(AppException.class);
        exception.expectMessage("Provided data not sufficient for insertion");

        sut.createBook(new Book());

    }

    @Test
    public void testCreateBook_validation_missingTitle() throws AppException {

        exception.expect(AppException.class);
        exception.expectMessage("Provided data not sufficient for insertion");

        Book book = new Book();
        sut.createBook(book);

    }


    @Test
    public void testUpdatePartiallyBook_successful() throws AppException {

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(SOME_ID);
        when(booksDao.getBookById(SOME_ID)).thenReturn(bookEntity);
        doNothing().when(booksDao).updateBook(any(BookEntity.class));

        Book book = new Book(bookEntity, true);
        book.setTitle(SOME_TITLE);
        sut.updatePartiallyBook(book);

        verify(booksDao).getBookById(SOME_ID);//verifies if the method booksDao.getBookById has been called exactly once with that exact input parameter
        verify(booksDao).updateBook(any(BookEntity.class));

        Assert.assertTrue(book.getTitle() == SOME_TITLE);
    }

    @Test
    public void testUpdatePartiallyBook_not_existing_book() {

        when(booksDao.getBookById(SOME_ID)).thenReturn(null);

        Book book = new Book();
        book.setId(SOME_ID);
        try {
            sut.updatePartiallyBook(book);
            Assert.fail("Should have thrown an exception");
        } catch (AppException e) {
            verify(booksDao).getBookById(SOME_ID);//verifies if the method booksDao.getBookById has been called exactly once with that exact input parameter
            Assert.assertEquals(e.getCode(), 404);
        }

    }

}

package org.laczkoboti.bookStore.rest.resource.book;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.laczkoboti.bookStore.rest.errorhandling.AppException;
import org.laczkoboti.bookStore.rest.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 *
 * Service class that handles REST requests
 *
 * @author amacoder
 *
 */
@Component
@Path("/books")
public class BookResource {

    @Autowired
    private BookService bookService;

	/*
	 * *********************************** CREATE ***********************************
	 */

    /**
     * Adds a new resource (book) from the given json format (at least title
     * and feed elements are required at the DB level)
     *
     * @param book
     * @return
     * @throws AppException
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.TEXT_HTML })
    public Response createBook(Book book) throws AppException {
        Long createBookId = bookService.createBook(book);
        return Response.status(Response.Status.CREATED)// 201
                .entity("A new book has been created")
                .header("Location",
                        "http://localhost:8888/demo-rest-jersey-spring/books/"
                                + String.valueOf(createBookId)).build();
    }

    /**
     * Adds a new book (resource) from "form" (at least title and feed
     * elements are required at the DB level)
     *
     * @param title
     * @param linkOnBookpedia
     * @param feed
     * @param description
     * @return
     * @throws AppException
     */
    @POST
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    @Produces({ MediaType.TEXT_HTML })
    public Response createBookFromApplicationFormURLencoded(
            @FormParam("title") String title,
            @FormParam("linkOnBookpedia") String linkOnBookpedia,
            @FormParam("feed") String feed,
            @FormParam("description") String description) throws AppException {

        Book book = new Book(title, linkOnBookpedia, feed,
                description);
        Long createBookid = bookService.createBook(book);

        return Response
                .status(Response.Status.CREATED)// 201
                .entity("A new book/resource has been created at /demo-rest-jersey-spring/books/"
                        + createBookid)
                .header("Location",
                        "http://localhost:8888/demo-rest-jersey-spring/books/"
                                + String.valueOf(createBookid)).build();
    }

    /**
     * A list of resources (here books) provided in json format will be added
     * to the database.
     *
     * @param books
     * @return
     * @throws AppException
     */
    @POST
    @Path("list")
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response createBooks(List<Book> books) throws AppException {
        bookService.createBooks(books);
        return Response.status(Response.Status.CREATED) // 201
                .entity("List of books was successfully created").build();
    }

	/*
	 * *********************************** READ ***********************************
	 */
    /**
     * Returns all resources (books) from the database
     *
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws AppException
     */
    @GET
    //@Compress //can be used only if you want to SELECTIVELY enable compression at the method level. By using the EncodingFilter everything is compressed now.
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<Book> getBooks(
            @QueryParam("orderByInsertionDate") String orderByInsertionDate,
            @QueryParam("numberDaysToLookBack") Integer numberDaysToLookBack)
            throws IOException,	AppException {
        List<Book> books = bookService.getBooks(
                orderByInsertionDate, numberDaysToLookBack);
        return books;
    }

    @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response getBookById(@PathParam("id") Long id, @QueryParam("detailed") boolean detailed)
            throws IOException,	AppException {
        Book bookById = bookService.getBookById(id);
        return Response.status(200)
                .entity(bookById, detailed ? new Annotation[]{BookDetailedView.Factory.get()} : new Annotation[0])
                .header("Access-Control-Allow-Headers", "X-extra-header")
                .allow("OPTIONS").build();
    }

//	@GET
//	@Path("{id}")
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	@BookDetailedView
//	public Book getBookById(@PathParam("id") Long id, @QueryParam("detailed") boolean detailed)
//			throws IOException,	AppException {
//		Book bookById = bookService.getBookById(id);
//
//		return bookById;
////		return Response.status(200)
////				.entity(bookById, detailed ? new Annotation[]{BookDetailedView.Factory.get()} : new Annotation[0])
////				.header("Access-Control-Allow-Headers", "X-extra-header")
////				.allow("OPTIONS").build();
//	}

	/*
	 * *********************************** UPDATE ***********************************
	 */

    /**
     * The method offers both Creation and Update resource functionality. If
     * there is no resource yet at the specified location, then a book
     * creation is executed and if there is then the resource will be full
     * updated.
     *
     * @param id
     * @param book
     * @return
     * @throws AppException
     */
    @PUT
    @Path("{id}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.TEXT_HTML })
    public Response putBookById(@PathParam("id") Long id, Book book)
            throws AppException {

        Book bookById = bookService.verifyBookExistenceById(id);

        if (bookById == null) {
            // resource not existent yet, and should be created under the
            // specified URI
            Long createBookId = bookService.createBook(book);
            return Response
                    .status(Response.Status.CREATED)
                    // 201
                    .entity("A new book has been created AT THE LOCATION you specified")
                    .header("Location",
                            "http://localhost:8888/demo-rest-jersey-spring/books/"
                                    + String.valueOf(createBookId)).build();
        } else {
            // resource is existent and a full update should occur
            bookService.updateFullyBook(book);
            return Response
                    .status(Response.Status.OK)
                    // 200
                    .entity("The book you specified has been fully updated created AT THE LOCATION you specified")
                    .header("Location",
                            "http://localhost:8888/demo-rest-jersey-spring/books/"
                                    + String.valueOf(id)).build();
        }
    }

    // PARTIAL update
    @POST
    @Path("{id}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.TEXT_HTML })
    public Response partialUpdateBook(@PathParam("id") Long id,
                                         Book book) throws AppException {
        book.setId(id);
        bookService.updatePartiallyBook(book);
        return Response
                .status(Response.Status.OK)
                // 200
                .entity("The book you specified has been successfully updated")
                .build();
    }

    /*
     * *********************************** DELETE ***********************************
     */
    @DELETE
    @Path("{id}")
    @Produces({ MediaType.TEXT_HTML })
    public Response deleteBookById(@PathParam("id") Long id) {
        bookService.deleteBookById(id);
        return Response.status(Response.Status.NO_CONTENT)// 204
                .entity("Book successfully removed from database").build();
    }

    @DELETE
    @Produces({ MediaType.TEXT_HTML })
    public Response deleteBooks() {
        bookService.deleteBooks();
        return Response.status(Response.Status.NO_CONTENT)// 204
                .entity("All books have been successfully removed").build();
    }

    public void setbookService(BookService bookService) {
        this.bookService = bookService;
    }

}

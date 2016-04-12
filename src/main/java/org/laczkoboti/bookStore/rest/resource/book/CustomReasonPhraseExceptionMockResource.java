package org.laczkoboti.bookStore.rest.resource.book;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.laczkoboti.bookStore.rest.errorhandling.CustomReasonPhraseException;
import org.laczkoboti.bookStore.rest.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/mocked-custom-reason-phrase-exception")
public class CustomReasonPhraseExceptionMockResource {

    @Autowired
    private BookService bookService;

    @GET
    public void testReasonChangedInResponse() throws CustomReasonPhraseException{
        bookService.generateCustomReasonPhraseException();
    }
}
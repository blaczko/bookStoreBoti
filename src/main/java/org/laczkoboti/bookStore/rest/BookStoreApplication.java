package org.laczkoboti.bookStore.rest;

import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.EncodingFilter;

/**
 * Registers the components to be used by the JAX-RS application
 *
 * @author ama
 *
 */
public class BookStoreApplication extends ResourceConfig {

    /**
     * Register JAX-RS application components.
     */
    public BookStoreApplication() {

        packages("org.laczkoboti.bookStore.rest");

//		// register application resources
//		register(BooksResource.class);
//
//		// register filters
//		register(RequestContextFilter.class);
//		register(LoggingResponseFilter.class);
//		register(CORSResponseFilter.class);
//
//		// register exception mappers
//		register(GenericExceptionMapper.class);
//		register(AppExceptionMapper.class);
//      register(CustomReasonPhraseExceptionMapper.class);
//		register(NotFoundExceptionMapper.class);
//
//		// register features
//		register(JacksonFeature.class);
        register(EntityFilteringFeature.class);
        EncodingFilter.enableFor(this, GZipEncoder.class);

//		property(EntityFilteringFeature.ENTITY_FILTERING_SCOPE, new Annotation[] {BookDetailedView.Factory.get()});
    }
}

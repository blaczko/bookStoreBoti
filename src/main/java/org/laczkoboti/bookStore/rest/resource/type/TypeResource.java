package org.laczkoboti.bookStore.rest.resource.type;

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
import org.laczkoboti.bookStore.rest.resource.type.Type;
import org.laczkoboti.bookStore.rest.service.TypeService;
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
@Path("/types")
public class TypeResource {

    @Autowired
    private TypeService typeService;

	/*
	 * *********************************** CREATE ***********************************
	 */

    /**
     * Adds a new resource (type) from the given json format (at least title
     * and feed elements are required at the DB level)
     *
     * @param type
     * @return
     * @throws AppException
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.TEXT_HTML })
    public Response createType(Type type) throws AppException {
        Long createTypeId = typeService.createType(type);
        return Response.status(Response.Status.CREATED)// 201
                .entity("A new type has been created")
                .header("Location",
                        "http://localhost:8888/typeStore/types/"
                                + String.valueOf(createTypeId)).build();
    }

    /**
     * Adds a new type (resource) from "form" (at least title and feed
     * elements are required at the DB level)
     *
     * @param types
     * @return
     * @throws AppException
     */
    @POST
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    @Produces({ MediaType.TEXT_HTML })
    public Response createTypeFromApplicationFormURLencoded(
            @FormParam("type") String types) throws AppException {

        Type type = new Type(types);
        Long createTypeid = typeService.createType(type);

        return Response
                .status(Response.Status.CREATED)// 201
                .entity("A new type/resource has been created at /demo-rest-jersey-spring/types/"
                        + createTypeid)
                .header("Location",
                        "http://localhost:8888/typeStore/types/"
                                + String.valueOf(createTypeid)).build();
    }

    /**
     * A list of resources (here types) provided in json format will be added
     * to the database.
     *
     * @param types
     * @return
     * @throws AppException
     */
    @POST
    @Path("list")
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response createTypes(List<Type> types) throws AppException {
        typeService.createTypes(types);
        return Response.status(Response.Status.CREATED) // 201
                .entity("List of types was successfully created").build();
    }

	/*
	 * *********************************** READ ***********************************
	 */
    /**
     * Returns all resources (types) from the database
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
    public List<Type> getTypes()
            throws IOException,	AppException {
        List<Type> types = typeService.getTypes();
        return types;
    }

    @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response getTypeById(@PathParam("id") Long id, @QueryParam("detailed") boolean detailed)
            throws IOException,	AppException {
        Type typeById = typeService.getTypeById(id);
        return Response.status(200)
                .entity(typeById, new Annotation[0])
                .header("Access-Control-Allow-Headers", "X-extra-header")
                .allow("OPTIONS").build();
    }

//	@GET
//	@Path("{id}")
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	@TypeDetailedView
//	public Type getTypeById(@PathParam("id") Long id, @QueryParam("detailed") boolean detailed)
//			throws IOException,	AppException {
//		Type typeById = typeService.getTypeById(id);
//
//		return typeById;
////		return Response.status(200)
////				.entity(typeById, detailed ? new Annotation[]{TypeDetailedView.Factory.get()} : new Annotation[0])
////				.header("Access-Control-Allow-Headers", "X-extra-header")
////				.allow("OPTIONS").build();
//	}

	/*
	 * *********************************** UPDATE ***********************************
	 */

    /**
     * The method offers both Creation and Update resource functionality. If
     * there is no resource yet at the specified location, then a type
     * creation is executed and if there is then the resource will be full
     * updated.
     *
     * @param id
     * @param type
     * @return
     * @throws AppException
     */
    @PUT
    @Path("{id}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.TEXT_HTML })
    public Response putTypeById(@PathParam("id") Long id, Type type)
            throws AppException {

        Type typeById = typeService.verifyTypeExistenceById(id);

        if (typeById == null) {
            // resource not existent yet, and should be created under the
            // specified URI
            Long createTypeId = typeService.createType(type);
            return Response
                    .status(Response.Status.CREATED)
                    // 201
                    .entity("A new type has been created AT THE LOCATION you specified")
                    .header("Location",
                            "http://localhost:8888/demo-rest-jersey-spring/types/"
                                    + String.valueOf(createTypeId)).build();
        } else {
            // resource is existent and a full update should occur
            typeService.updateFullyType(type);
            return Response
                    .status(Response.Status.OK)
                    // 200
                    .entity("The type you specified has been fully updated created AT THE LOCATION you specified")
                    .header("Location",
                            "http://localhost:8888/demo-rest-jersey-spring/types/"
                                    + String.valueOf(id)).build();
        }
    }

    // PARTIAL update
    @POST
    @Path("{id}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.TEXT_HTML })
    public Response partialUpdateType(@PathParam("id") Long id,
                                      Type type) throws AppException {
        type.setId(id);
        typeService.updatePartiallyType(type);
        return Response
                .status(Response.Status.OK)
                // 200
                .entity("The type you specified has been successfully updated")
                .build();
    }

    /*
     * *********************************** DELETE ***********************************
     */
    @DELETE
    @Path("{id}")
    @Produces({ MediaType.TEXT_HTML })
    public Response deleteTypeById(@PathParam("id") Long id) {
        typeService.deleteTypeById(id);
        return Response.status(Response.Status.NO_CONTENT)// 204
                .entity("Type successfully removed from database").build();
    }

    @DELETE
    @Produces({ MediaType.TEXT_HTML })
    public Response deleteTypes() {
        typeService.deleteTypes();
        return Response.status(Response.Status.NO_CONTENT)// 204
                .entity("All types have been successfully removed").build();
    }

    public void settypeService(TypeService typeService) {
        this.typeService = typeService;
    }

}

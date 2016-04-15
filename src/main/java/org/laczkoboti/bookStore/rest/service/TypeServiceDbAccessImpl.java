package org.laczkoboti.bookStore.rest.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.laczkoboti.bookStore.rest.dao.TypesDao;
import org.laczkoboti.bookStore.rest.dao.TypeEntity;
import org.laczkoboti.bookStore.rest.errorhandling.AppException;
import org.laczkoboti.bookStore.rest.errorhandling.CustomReasonPhraseException;
import org.laczkoboti.bookStore.rest.filters.AppConstants;
import org.laczkoboti.bookStore.rest.helpers.NullAwareBeanUtilsBean;
import org.laczkoboti.bookStore.rest.resource.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class TypeServiceDbAccessImpl implements TypeService {

    @Autowired
    TypesDao typesDao;

    /********************* Create related methods implementation ***********************/
    @Transactional("transactionManager")
    public Long createType(Type type) throws AppException {

        validateInputForCreation(type);
        return typesDao.createType(new TypeEntity(type));
    }

    private void validateInputForCreation(Type type) throws AppException {

        if(type.getType() == null){
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 400, "Provided data not sufficient for insertion",
                    "Please verify that the title is properly generated/set", AppConstants.BLOG_POST_URL);
        }
        //etc...
    }

    @Transactional("transactionManager")
    public void createTypes(List<Type> types) throws AppException {
        for (Type type : types) {
            createType(type);
        }
    }


    // ******************** Read related methods implementation **********************
    public List<Type> getTypes() throws AppException {

        List<TypeEntity> types = typesDao.getTypes();

        return getTypesFromEntities(types);
    }

    private boolean isOrderByInsertionDateParameterValid(
            String orderByInsertionDate) {
        return orderByInsertionDate!=null
                && !("ASC".equalsIgnoreCase(orderByInsertionDate) || "DESC".equalsIgnoreCase(orderByInsertionDate));
    }

    public Type getTypeById(Long id) throws AppException {
        TypeEntity typeById = typesDao.getTypeById(id);
        if(typeById == null){
            throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
                    404,
                    "The type you requested with id " + id + " was not found in the database",
                    "Verify the existence of the type with the id " + id + " in the database",
                    AppConstants.BLOG_POST_URL);
        }

        return new Type(typesDao.getTypeById(id), true);
    }

    private List<Type> getTypesFromEntities(List<TypeEntity> typeEntities) {
        List<Type> response = new ArrayList<Type>();
        for(TypeEntity typeEntity : typeEntities){
            response.add(new Type(typeEntity, true));
        }

        return response;
    }

    /********************* UPDATE-related methods implementation ***********************/
    @Transactional("transactionManager")
    public void updateFullyType(Type type) throws AppException {
        //do a validation to verify FULL update with PUT
        if(isFullUpdate(type)){
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),
                    400,
                    "Please specify all properties for Full UPDATE",
                    "required properties - id, title, feed, lnkOnTypepedia, description" ,
                    AppConstants.BLOG_POST_URL);
        }

        Type verifyTypeExistenceById = verifyTypeExistenceById(type.getId());
        if(verifyTypeExistenceById == null){
            throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
                    404,
                    "The resource you are trying to update does not exist in the database",
                    "Please verify existence of data in the database for the id - " + type.getId(),
                    AppConstants.BLOG_POST_URL);
        }

        typesDao.updateType(new TypeEntity(type));
    }

    /**
     * Verifies the "completeness" of type resource sent over the wire
     *
     * @param type
     * @return
     */
    private boolean isFullUpdate(Type type) {
        return type.getId() == null
                || type.getType() == null;
    }

    /********************* DELETE-related methods implementation ***********************/
    @Transactional("transactionManager")
    public void deleteTypeById(Long id) {
        typesDao.deleteTypeById(id);
    }

    @Transactional("transactionManager")
    public void deleteTypes() {
        typesDao.deleteTypes();
    }

    public Type verifyTypeExistenceById(Long id) {
        TypeEntity typeById = typesDao.getTypeById(id);
        if(typeById == null){
            return null;
        } else {
            return new Type(typeById, true);
        }
    }

    @Transactional("transactionManager")
    public void updatePartiallyType(Type type) throws AppException {
        //do a validation to verify existence of the resource
        Type verifyTypeExistenceById = verifyTypeExistenceById(type.getId());
        if(verifyTypeExistenceById == null){
            throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
                    404,
                    "The resource you are trying to update does not exist in the database",
                    "Please verify existence of data in the database for the id - " + type.getId(),
                    AppConstants.BLOG_POST_URL);
        }
        copyPartialProperties(verifyTypeExistenceById, type);
        typesDao.updateType(new TypeEntity(verifyTypeExistenceById));

    }

    private void copyPartialProperties(Type verifyTypeExistenceById,
                                       Type type) {

        BeanUtilsBean notNull=new NullAwareBeanUtilsBean();
        try {
            notNull.copyProperties(verifyTypeExistenceById, type);
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

    public void setTypesDao(TypesDao typesDao) {
        this.typesDao = typesDao;
    }

}

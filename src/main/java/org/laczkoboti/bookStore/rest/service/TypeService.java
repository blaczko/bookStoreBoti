package org.laczkoboti.bookStore.rest.service;

import java.util.List;

import org.laczkoboti.bookStore.rest.errorhandling.AppException;
import org.laczkoboti.bookStore.rest.errorhandling.CustomReasonPhraseException;
import org.laczkoboti.bookStore.rest.resource.type.Type;


public interface TypeService {

    /*
     * ******************** Create related methods **********************
     * */
    public Long createType(Type type) throws AppException;
    public void createTypes(List<Type> types) throws AppException;


	/*
	 ******************** Read related methods ********************
	  */
    /**

     * @return list with types coressponding to search criterias
     * @throws AppException
     */
    public List<Type> getTypes() throws AppException;

    /**
     * Returns a type given its id
     *
     * @param id
     * @return
     * @throws AppException
     */
    public Type getTypeById(Long id) throws AppException;

    /*
     * ******************** Update related methods **********************
     * */
    public void updateFullyType(Type type) throws AppException;
    public void updatePartiallyType(Type type) throws AppException;


    /*
     * ******************** Delete related methods **********************
     * */
    public void deleteTypeById(Long id);

    /** removes all types */
    public void deleteTypes();

    /*
     * ******************** Helper methods **********************
     * */
    public Type verifyTypeExistenceById(Long id);

    /**
     * Empty method generating a Business Exception
     * @throws CustomReasonPhraseException
     */
    public void generateCustomReasonPhraseException() throws CustomReasonPhraseException;

}

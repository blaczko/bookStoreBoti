package org.laczkoboti.bookStore.rest.dao;

import java.util.List;


public interface TypesDao {

    public List<TypeEntity> getTypes();

    public TypeEntity getTypeById(Long id);

    public void deleteTypeById(Long id);

    public Long createType(TypeEntity book);

    public void updateType(TypeEntity book);

    public void deleteTypes();

}

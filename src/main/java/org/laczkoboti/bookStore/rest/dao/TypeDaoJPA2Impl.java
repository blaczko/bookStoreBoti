package org.laczkoboti.bookStore.rest.dao;

import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class TypeDaoJPA2Impl implements TypesDao {

    @PersistenceContext(unitName="demoRestPersistence")
    private EntityManager entityManager;

    public List<TypeEntity> getTypes() {
        String sqlString = null;

        sqlString = "SELECT p FROM TypeEntity p";

        TypedQuery<TypeEntity> query = entityManager.createQuery(sqlString, TypeEntity.class);

        return query.getResultList();
    }

    public TypeEntity getTypeById(Long id) {

        try {
            String qlString = "SELECT p FROM TypeEntity p WHERE p.id = ?1";
            TypedQuery<TypeEntity> query = entityManager.createQuery(qlString, TypeEntity.class);
            query.setParameter(1, id);

            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void deleteTypeById(Long id) {

        TypeEntity type = entityManager.find(TypeEntity.class, id);
        for (BookEntity book : type.getBooks()) {
            book.getTypes().remove(type);
            entityManager.persist(book);
        }
        type.setBooks(new HashSet<BookEntity>());
        entityManager.persist(type);
        entityManager.flush();
        entityManager.remove(type);
    }


    public Long createType(TypeEntity type) {

        entityManager.merge(type);
        entityManager.flush();//force insert to receive the id of the type

        return type.getId();
    }

    public void updateType(TypeEntity type) {
        //TODO think about partial update and full update
        entityManager.merge(type);
    }

    public void deleteTypes() {
        Query query = entityManager.createNativeQuery("TRUNCATE TABLE types");
        query.executeUpdate();
    }

}

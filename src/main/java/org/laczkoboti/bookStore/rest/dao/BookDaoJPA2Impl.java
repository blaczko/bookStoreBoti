package org.laczkoboti.bookStore.rest.dao;

import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class BookDaoJPA2Impl implements BooksDao {

    @PersistenceContext(unitName="demoRestPersistence")
    private EntityManager entityManager;

    public List<BookEntity> getBooks() {
        String sqlString = null;

        sqlString = "SELECT p FROM BookEntity p";

        TypedQuery<BookEntity> query = entityManager.createQuery(sqlString, BookEntity.class);

        return query.getResultList();
    }

    public BookEntity getBookById(Long id) {

        try {
            String qlString = "SELECT p FROM BookEntity p WHERE p.id = ?1";
            TypedQuery<BookEntity> query = entityManager.createQuery(qlString, BookEntity.class);
            query.setParameter(1, id);

            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void deleteBookById(Long id) {

        BookEntity book = entityManager.find(BookEntity.class, id);
        for (TypeEntity type : book.getTypes()) {
            type.getBooks().remove(book);
            entityManager.persist(book);
        }
        book.setTypes(new HashSet<TypeEntity>());
        entityManager.persist(book);
        entityManager.flush();
        entityManager.remove(book);
    }


    public Long createBook(BookEntity book) {

        entityManager.merge(book);
        entityManager.flush();//force insert to receive the id of the book

        return book.getId();
    }

    public void updateBook(BookEntity book) {
        //TODO think about partial update and full update
        entityManager.merge(book);
    }

    public void deleteBooks() {
        Query query = entityManager.createNativeQuery("TRUNCATE TABLE books");
        query.executeUpdate();
    }

}

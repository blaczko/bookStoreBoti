package org.laczkoboti.bookStore.rest.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.laczkoboti.bookStore.rest.resource.book.Book;
import org.laczkoboti.bookStore.rest.resource.type.Type;


@Entity
@Table(name="types")
public class TypeEntity implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;

    /** id of the type */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="type")
    private String typeString;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "types")
    private Collection<BookEntity> books = new ArrayList<BookEntity>();

    public TypeEntity(){}

    public TypeEntity(String type) {

        this.typeString = type;

    }

    public TypeEntity(Type type){
        this.id = type.getId();
        this.typeString = type.getType();
        for (Book b:type.getBooks()){
            this.getBooks().add(new BookEntity(b));
        }
    }

    public Collection<BookEntity> getBooks() {
        return books;
    }

    public void setBooks(Collection<BookEntity> books) {
        this.books = books;
    }

    public String getType() {
        return typeString;
    }

    public void setType(String type) {
        this.typeString = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}

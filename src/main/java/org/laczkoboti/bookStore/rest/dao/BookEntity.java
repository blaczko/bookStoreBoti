package org.laczkoboti.bookStore.rest.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.annotations.Fetch;
import org.laczkoboti.bookStore.rest.resource.book.Book;
import org.laczkoboti.bookStore.rest.resource.type.Type;


@Entity
@Table(name="books")
public class BookEntity implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;

    /** id of the book */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "booktotype",
    joinColumns = { @JoinColumn(name = "bookId", nullable = false, updatable = false) },
    inverseJoinColumns = { @JoinColumn(name = "typeId", nullable = false, updatable = false) })
    private Collection<TypeEntity> types = new ArrayList<TypeEntity>();

    public BookEntity(){}

    public BookEntity(String title, String description) {

        this.title = title;
        this.description = description;

    }

    public BookEntity(Book book){
        this.id = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        for (Type t:book.getTypes()){
            this.getTypes().add(new TypeEntity(t));
        }
    }

    public Collection<TypeEntity> getTypes() {
        return types;
    }

    public void setTypes(Collection<TypeEntity> types) {
        this.types = types;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookEntity that = (BookEntity) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

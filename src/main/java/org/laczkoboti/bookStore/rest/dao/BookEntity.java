package org.laczkoboti.bookStore.rest.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.persistence.*;

import org.apache.commons.beanutils.BeanUtils;
import org.laczkoboti.bookStore.rest.resource.book.Book;


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

    public BookEntity(){}

    public BookEntity(String title, String description) {

        this.title = title;
        this.description = description;

    }

    public BookEntity(Book book){
        try {
            BeanUtils.copyProperties(this, book);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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


}

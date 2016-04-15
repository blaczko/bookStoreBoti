package org.laczkoboti.bookStore.rest.resource.type;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtils;
import org.laczkoboti.bookStore.rest.dao.BookEntity;
import org.laczkoboti.bookStore.rest.dao.TypeEntity;
import org.laczkoboti.bookStore.rest.resource.book.Book;

/**
 * Type resource placeholder for json/xml representation
 *
 * @author ama
 *
 */
@SuppressWarnings("restriction")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Type implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;

    @XmlElement(name = "id")
    private Long id;

    @XmlElement(name = "type")
    private String type;

    private Collection<Book> books = new ArrayList<Book>();

    public Type(TypeEntity typeEntity, boolean recursive){
        this.id = typeEntity.getId();
        this.type = typeEntity.getType();
        if (recursive) {
            for (BookEntity b : typeEntity.getBooks()) {
                this.getBooks().add(new Book(b, false));
            }
        }
    }

    public Type(String type) {

        this.type = type;
    }

    public Type(){}

    public Collection<Book> getBooks() {
        return books;
    }

    public void setBooks(Collection<Book> books) {
        this.books = books;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

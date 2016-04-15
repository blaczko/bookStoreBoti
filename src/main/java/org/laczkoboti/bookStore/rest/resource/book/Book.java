package org.laczkoboti.bookStore.rest.resource.book;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtils;
import org.laczkoboti.bookStore.rest.dao.BookEntity;
import org.laczkoboti.bookStore.rest.dao.TypeEntity;
import org.laczkoboti.bookStore.rest.resource.type.Type;

/**
 * Book resource placeholder for json/xml representation
 *
 * @author ama
 *
 */
@SuppressWarnings("restriction")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Book implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;

    @XmlElement(name = "id")
    private Long id;

    @XmlElement(name = "title")
    private String title;

    @XmlElement(name = "description")
    private String description;

    private Collection<Type> types = new ArrayList<Type>();

    public Book(BookEntity bookEntity, boolean recursive){
        this.id = bookEntity.getId();
        this.title = bookEntity.getTitle();
        this.description = bookEntity.getDescription();
        if (recursive) {
            for (TypeEntity t : bookEntity.getTypes()) {
                this.getTypes().add(new Type(t, false));
            }
        }
    }

    public Book(String title, String description) {

        this.title = title;
        this.description = description;

    }

    public Book(){}

    public Collection<Type> getTypes() {
        return types;
    }

    public void setTypes(Collection<Type> types) {
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

}

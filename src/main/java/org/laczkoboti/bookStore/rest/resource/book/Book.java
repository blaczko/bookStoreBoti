package org.laczkoboti.bookStore.rest.resource.book;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtils;
import org.laczkoboti.bookStore.rest.dao.BookEntity;

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

    public Book(BookEntity bookEntity){
        try {
            BeanUtils.copyProperties(this, bookEntity);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Book(String title, String description) {

        this.title = title;
        this.description = description;

    }

    public Book(){}

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

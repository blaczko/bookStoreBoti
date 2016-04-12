package org.laczkoboti.bookStore.rest.resource.book;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.beanutils.BeanUtils;
import org.laczkoboti.bookStore.rest.dao.BookEntity;
import org.laczkoboti.bookStore.rest.helpers.DateISO8601Adapter;

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

    /** id of the book */
    @XmlElement(name = "id")
    private Long id;

    /** title of the book */
    @XmlElement(name = "title")
    private String title;

    /** link of the book on Bookpedia.org */
    @XmlElement(name = "linkOnBookpedia")
    private String linkOnBookpedia;

    /** url of the feed */
    @XmlElement(name = "feed")
    private String feed;

    /** description of the book */
    @XmlElement(name = "description")
    @BookDetailedView
    private String description;

    /** insertion date in the database */
    @XmlElement(name = "insertionDate")
    @XmlJavaTypeAdapter(DateISO8601Adapter.class)
    @BookDetailedView
    private Date insertionDate;

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

    public Book(String title, String linkOnBookpedia, String feed,
                   String description) {

        this.title = title;
        this.linkOnBookpedia = linkOnBookpedia;
        this.feed = feed;
        this.description = description;

    }

    public Book(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkOnBookpedia() {
        return linkOnBookpedia;
    }

    public void setLinkOnBookpedia(String linkOnBookpedia) {
        this.linkOnBookpedia = linkOnBookpedia;
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

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

}

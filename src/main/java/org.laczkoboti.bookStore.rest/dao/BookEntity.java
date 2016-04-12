package org.laczkoboti.bookStore.rest.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;
import org.laczkoboti.bookStore.rest.resource.book.Book;


@Entity
@Table(name="books")
public class BookEntity implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;

    /** id of the book */
    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="link_on_bookpedia")
    private String linkOnBookpedia;

    /** url of the feed */
    @Column(name="feed")
    private String feed;

    @Column(name="description")
    private String description;

    /** insertion date in the database */
    @Column(name="insertion_date")
    private Date insertionDate;

    public BookEntity(){}

    public BookEntity(String title, String linkOnBookpedia, String feed,
                         String description) {

        this.title = title;
        this.linkOnBookpedia = linkOnBookpedia;
        this.feed = feed;
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

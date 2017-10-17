package com.example.demo.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * An entity User composed by three fields (id, email, name).
 * The Entity annotation indicates that this class is a JPA entity.
 * The Table annotation specifies the name for the table in the db.
 *
 * @author des
 */
@Entity
@Table(name = "user")
public class User implements Comparable {

    @Override
    public int compareTo(Object o) {
        return this.userName.compareTo(((User)o).getUserName());
    }
    // ------------------------
    // PRIVATE FIELDS
    // ------------------------

    // An auto generated id (unique for each user in the db)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // The user's user name
    @NotNull
    private String userName;

    // The indicator if the user is active or disabled
    @NotNull
    private Boolean isActive;

    // The user's password
    @NotNull
    private String password;

    // The user's session key
    private String sessionKey;

    // Expiration the user's session key
    private Date sessionExpiration;

    // ------------------------
    // PUBLIC METHODS
    // ------------------------

    public User() { }

    public User(long id) {
        this.id = id;
    }

    public User(String userName, Boolean isActive, String password, String sessionKey, Date sessionExpiration) {
        this.userName=userName;
        this.isActive=isActive;
        this.password=password;
        this.sessionKey=sessionKey;
        this.sessionExpiration=sessionExpiration;
    }

    // Getter and setter methods


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Date getSessionExpiration() {
        return sessionExpiration;
    }

    public void setSessionExpiration(Date sessionExpiration) {
        this.sessionExpiration = sessionExpiration;
    }
} // class User
package com.elrealo.firstProject.model;

import com.elrealo.firstProject.Helper.RandomString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

/*
*
* Type = 1 => confirmation registration by Email
* Type = 2 => confirmation changing Email of user
* Type = 3 => forgot password confirmation
*
*/


@Entity
@Table(name="verification_token")
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "token")
    private String token;

    @Column(name = "type")
    private int type;

    @Column(name = "email")
    private String email;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private Date expiryDate;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public static int getEXPIRATION() {
        return EXPIRATION;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void generateToken(){
        this.token = new RandomString().nextString();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(int expiryTimeInMinutes) {
        this.expiryDate = calculateExpiryDate(expiryTimeInMinutes);
    }

}
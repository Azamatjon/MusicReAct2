package com.ReAct.MusicReAct.model;

import java.util.Set;

import javax.persistence.*;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

@Entity
@Table(name = "user")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;

    @Column(name = "password")
    @Transient
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "image")
    private String image;

    @Column(name = "active")
    private int active;

    @Column(name = "is_locked", nullable = false)
    private boolean isLocked = false;

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    @ManyToMany()
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany()
    private Set<VerificationToken> verificationToken;

    public Set<VerificationToken> getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(Set<VerificationToken> verificationToken) {
        this.verificationToken = verificationToken;
    }

    @PreRemove
    void preRemove() {
        //verificationTokenRepository.deleteByUser(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isAdmin(){
        for (Role userRole: roles) {
            if (userRole.getRole().equals("ADMIN")) return true;
        }
        return false;
    }

    public String getRoleName(){
        for (Role userRole: roles) {
            return userRole.getRole();
        }
        return "empty";
    }

    public String getAvatar(){
        return "/avatars/" + ((this.getImage() == null)?"default_avatar.png":this.getImage());
    }

}
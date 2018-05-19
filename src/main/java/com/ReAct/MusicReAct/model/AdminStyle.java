package com.ReAct.MusicReAct.model;

import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

@Entity
@Table(name = "styles")
public class AdminStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "custom_style")
    private String customStyle;

    @Column(name = "f_header")
    private boolean fHeader;

    @Column(name = "f_sidebar")
    private boolean fSidebar;

    @Column(name = "h_bar")
    private boolean hBar;

    @Column(name = "t_sidebar")
    private boolean tSidebar;

    @Column(name = "c_menu")
    private boolean cMenu;

    @Column(name = "h_menu")
    private boolean hMenu;

    @Column(name = "b_layout")
    private boolean bLayout;

    /*
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    */


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCustomStyle() {
        return customStyle;
    }

    public void setCustomStyle(String customStyle) {
        this.customStyle = customStyle;
    }

    public boolean isfHeader() {
        return fHeader;
    }

    public void setfHeader(boolean fHeader) {
        this.fHeader = fHeader;
    }

    public boolean isfSidebar() {
        return fSidebar;
    }

    public void setfSidebar(boolean fSidebar) {
        this.fSidebar = fSidebar;
    }

    public boolean ishBar() {
        return hBar;
    }

    public void sethBar(boolean hBar) {
        this.hBar = hBar;
    }

    public boolean istSidebar() {
        return tSidebar;
    }

    public void settSidebar(boolean tSidebar) {
        this.tSidebar = tSidebar;
    }

    public boolean iscMenu() {
        return cMenu;
    }

    public void setcMenu(boolean cMenu) {
        this.cMenu = cMenu;
    }

    public boolean ishMenu() {
        return hMenu;
    }

    public void sethMenu(boolean hMenu) {
        this.hMenu = hMenu;
    }

    public boolean isbLayout() {
        return bLayout;
    }

    public void setbLayout(boolean bLayout) {
        this.bLayout = bLayout;
    }
}
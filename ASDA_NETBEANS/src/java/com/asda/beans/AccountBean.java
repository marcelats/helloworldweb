package com.asda.beans;

import com.asda.utils.Md5Hash;
import java.io.Serializable;
import java.util.List;
import java.util.Calendar;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Account bean.
 * 
 * @author Felipe Osorio Thomé
 */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name="users.findUser",
                query="SELECT u FROM AccountBean u WHERE u.email = :email AND u.passwordHash = :password"),
})
public class AccountBean implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    
    @OneToMany(mappedBy="user", cascade=CascadeType.PERSIST, orphanRemoval=true,
            fetch=FetchType.LAZY)
    private List<GraphBean> graphs;
    
    @Column(unique = true, nullable = false)
    @Pattern(regexp = ".+@.+\\.[a-z]+")
    @NotEmpty
    private String email;
    
    @Column(name = "user_name")
    private String name;
    
    @Column(name = "user_class")
    private String userClass;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registration_date")
    private Calendar registrationDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login")
    private Calendar lastLogin;
    
    @Column(name = "password_hash")
    @NotEmpty
    private String passwordHash;
    
    private String course;
    private String permission;
    
    public AccountBean() {
    }

    /* Getters and Setters */
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public List<GraphBean> getGraphs() {
        return graphs;
    }

    public void setGraphs(List<GraphBean> graphs) {
        this.graphs = graphs;
    }

    public Calendar getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Calendar registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Calendar getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Calendar lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String password) {
        String passHash = Md5Hash.generator(password.getBytes());
        this.passwordHash = passHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}

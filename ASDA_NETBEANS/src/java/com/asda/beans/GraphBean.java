package com.asda.beans;

import java.io.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * GraphBean bean.
 *
 * @author Felipe Osorio Thomé
 */
@Entity
@Table(name = "graphs",
        uniqueConstraints= @UniqueConstraint(columnNames={"user_id", "graph_name"}))
@NamedQueries({
    @NamedQuery(name="graphs.findGraph",
                query="SELECT g FROM GraphBean g WHERE g.graphName = :name AND g.user = :user"),
}) 
public class GraphBean implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "graph_id")
    private Long graphId;
    
    @ManyToOne(optional=false)
    @JoinColumn(name="user_id", nullable=false)
    private AccountBean user;
    
    @Column(name = "graph_name")
    @NotEmpty
    private String graphName;
    
    /* @Lob does not work here (hibernate bug).
     * Attention: this solution is not portable.
     */
    @Column(name = "graph_json", columnDefinition="TEXT")
    @NotEmpty
    private String graphJson;

    public GraphBean() {
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public AccountBean getUser() {
        return user;
    }

    public void setUser(AccountBean user) {
        this.user = user;
    }

    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public String getGraphJson() {
        return graphJson;
    }

    public void setGraphJson(String graphJson) {
        this.graphJson = graphJson;
    }
}

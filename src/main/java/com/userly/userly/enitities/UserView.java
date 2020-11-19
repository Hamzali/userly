package com.userly.userly.enitities;

import javax.persistence.*;

@Entity
@Table(name = "user_views")
public class UserView {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "viewer_id", nullable = false)
    private  long viewerId;

    @Column(name = "viewed_id", nullable = false)
    private  long viewedId;

    @Column(name = "viewed_at", nullable = false)
    private  long viewedAt;

    public UserView() {

    }

    public UserView(long viewerId, long viewedId, long viewedAt) {
        this.viewerId = viewerId;
        this.viewedId = viewedId;
        this.viewedAt = viewedAt;
    }


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getViewerId() {
        return viewerId;
    }

    public void setViewerId(long id) {
        this.viewerId = id;
    }

    public long getViewedId() {
        return viewedId;
    }

    public void setViewedId(long viewed_id) {
        this.viewedId = viewed_id;
    }

    public long getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(long s) {
        viewedAt = s;
    }
}

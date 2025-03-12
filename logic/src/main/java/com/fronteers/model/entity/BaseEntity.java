package com.fronteers.model.entity;

import com.fronteers.utils.TimeUtil;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.sql.Timestamp;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at", nullable = false)
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @Basic
  @Column(name = "created_at", nullable = false)
  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  @Basic
  @Column(name = "updated_at", nullable = true)
  public Timestamp getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Timestamp updatedAt) {
    this.updatedAt = updatedAt;
  }

  @PrePersist
  public void beforeSave() {
    this.createdAt = TimeUtil.nowByTimeZoneLagos();
  }

  @PreUpdate
  private void beforeUpdate() {
    this.updatedAt = TimeUtil.nowByTimeZoneLagos();
  }
}

package com.soze.lifegameserver.game.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "entity_template")
@TypeDefs({
  @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class),
})
public class EntityTemplate {

  @Id
  @Column(name = "id")
  private String id;
  
  @Type(type = "jsonb")
  @Column(name = "data", columnDefinition = "jsonb")
  private PersistentEntity data;
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public PersistentEntity getData() {
    return data;
  }
  
  public void setData(PersistentEntity data) {
    this.data = data;
  }
}

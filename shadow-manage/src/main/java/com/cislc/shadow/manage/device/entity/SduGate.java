package com.cislc.shadow.manage.device.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.cislc.shadow.manage.core.bean.entity.ShadowEntity;
import com.cislc.shadow.manage.core.bean.field.DatabaseField;
import com.cislc.shadow.manage.core.bean.field.EntityField;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(
    name = "sdu_cabinet_gate_"
)
public class SduGate extends ShadowEntity {
  @Transient
  private static final Map<String, DatabaseField> databaseFieldMap = new HashMap<>();

  static {
    databaseFieldMap.put("mode", new DatabaseField("sdu_cabinet_gate_", "mode"));
    databaseFieldMap.put("lockNum", new DatabaseField("sdu_cabinet_gate_", "lock_num"));
    databaseFieldMap.put("cabinetId", new DatabaseField("sdu_cabinet_gate_", "cabinet_id"));
    databaseFieldMap.put("openState", new DatabaseField("sdu_cabinet_gate_", "open_state"));
    databaseFieldMap.put("rightTopX", new DatabaseField("sdu_cabinet_gate_", "right_top_x"));
    databaseFieldMap.put("tableNum", new DatabaseField("sdu_cabinet_gate_", "table_num"));
    databaseFieldMap.put("leftBottomY", new DatabaseField("sdu_cabinet_gate_", "left_bottom_y"));
    databaseFieldMap.put("leftBottomX", new DatabaseField("sdu_cabinet_gate_", "left_bottom_x"));
    databaseFieldMap.put("rightTopY", new DatabaseField("sdu_cabinet_gate_", "right_top_y"));
    databaseFieldMap.put("id", new DatabaseField("sdu_cabinet_gate_", "id"));
    databaseFieldMap.put("type", new DatabaseField("sdu_cabinet_gate_", "type"));
  }

  @Column(
      name = "mode"
  )
  private String mode;

  @Column(
      name = "lock_num"
  )
  private Integer lockNum;

  @Column(
      name = "cabinet_id"
  )
  private String cabinetId;

  @Column(
      name = "open_state"
  )
  private Integer openState;

  @Column(
      name = "right_top_x"
  )
  private String rightTopX;

  @Column(
      name = "table_num"
  )
  private Integer tableNum;

  @Column(
      name = "left_bottom_y"
  )
  private String leftBottomY;

  @Column(
      name = "left_bottom_x"
  )
  private String leftBottomX;

  @Column(
      name = "right_top_y"
  )
  private String rightTopY;

  @Column(
      name = "id"
  )
  private String id;

  @Column(
      name = "type"
  )
  private String type;

  public SduGate() {
    super();
  }

  public SduGate(String deviceId) {
    super(deviceId);
    setDeviceId(deviceId);
  }

  @JSONField(
      serialize = false
  )
  @Transient
  @Override
  public boolean isDevice() {
    return false;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    EntityField field = new EntityField("SduGate", "mode", this.mode);
    this.mode = mode;
    field.setFieldValue(mode);
    notifyObservers(databaseFieldMap.get("mode"), field);
  }

  public Integer getLockNum() {
    return lockNum;
  }

  public void setLockNum(Integer lockNum) {
    EntityField field = new EntityField("SduGate", "lockNum", this.lockNum);
    this.lockNum = lockNum;
    field.setFieldValue(lockNum);
    notifyObservers(databaseFieldMap.get("lockNum"), field);
  }

  public String getCabinetId() {
    return cabinetId;
  }

  public void setCabinetId(String cabinetId) {
    EntityField field = new EntityField("SduGate", "cabinetId", this.cabinetId);
    this.cabinetId = cabinetId;
    field.setFieldValue(cabinetId);
    notifyObservers(databaseFieldMap.get("cabinetId"), field);
  }

  public Integer getOpenState() {
    return openState;
  }

  public void setOpenState(Integer openState) {
    EntityField field = new EntityField("SduGate", "openState", this.openState);
    this.openState = openState;
    field.setFieldValue(openState);
    notifyObservers(databaseFieldMap.get("openState"), field);
  }

  public String getRightTopX() {
    return rightTopX;
  }

  public void setRightTopX(String rightTopX) {
    EntityField field = new EntityField("SduGate", "rightTopX", this.rightTopX);
    this.rightTopX = rightTopX;
    field.setFieldValue(rightTopX);
    notifyObservers(databaseFieldMap.get("rightTopX"), field);
  }

  public Integer getTableNum() {
    return tableNum;
  }

  public void setTableNum(Integer tableNum) {
    EntityField field = new EntityField("SduGate", "tableNum", this.tableNum);
    this.tableNum = tableNum;
    field.setFieldValue(tableNum);
    notifyObservers(databaseFieldMap.get("tableNum"), field);
  }

  public String getLeftBottomY() {
    return leftBottomY;
  }

  public void setLeftBottomY(String leftBottomY) {
    EntityField field = new EntityField("SduGate", "leftBottomY", this.leftBottomY);
    this.leftBottomY = leftBottomY;
    field.setFieldValue(leftBottomY);
    notifyObservers(databaseFieldMap.get("leftBottomY"), field);
  }

  public String getLeftBottomX() {
    return leftBottomX;
  }

  public void setLeftBottomX(String leftBottomX) {
    EntityField field = new EntityField("SduGate", "leftBottomX", this.leftBottomX);
    this.leftBottomX = leftBottomX;
    field.setFieldValue(leftBottomX);
    notifyObservers(databaseFieldMap.get("leftBottomX"), field);
  }

  public String getRightTopY() {
    return rightTopY;
  }

  public void setRightTopY(String rightTopY) {
    EntityField field = new EntityField("SduGate", "rightTopY", this.rightTopY);
    this.rightTopY = rightTopY;
    field.setFieldValue(rightTopY);
    notifyObservers(databaseFieldMap.get("rightTopY"), field);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    EntityField field = new EntityField("SduGate", "id", this.id);
    this.id = id;
    field.setFieldValue(id);
    notifyObservers(databaseFieldMap.get("id"), field);
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    EntityField field = new EntityField("SduGate", "type", this.type);
    this.type = type;
    field.setFieldValue(type);
    notifyObservers(databaseFieldMap.get("type"), field);
  }
}

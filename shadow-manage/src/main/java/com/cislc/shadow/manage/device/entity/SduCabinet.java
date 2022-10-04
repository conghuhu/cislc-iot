package com.cislc.shadow.manage.device.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.cislc.shadow.manage.core.bean.entity.ShadowEntity;
import com.cislc.shadow.manage.core.bean.field.DatabaseField;
import com.cislc.shadow.manage.core.bean.field.EntityField;
import com.cislc.shadow.utils.enums.Encryption;
import com.cislc.shadow.utils.enums.Protocol;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(
    name = "sdu_cabinet_"
)
public class SduCabinet extends ShadowEntity {
  @Transient
  private static final Map<String, DatabaseField> databaseFieldMap = new HashMap<>();

  static {
    databaseFieldMap.put("cabinetTypeId", new DatabaseField("sdu_cabinet_", "cabinet_type_id"));
    databaseFieldMap.put("manager", new DatabaseField("sdu_cabinet_", "manager"));
    databaseFieldMap.put("initLockState", new DatabaseField("sdu_cabinet_", "init_lock_state"));
    databaseFieldMap.put("name", new DatabaseField("sdu_cabinet_", "name"));
    databaseFieldMap.put("description", new DatabaseField("sdu_cabinet_", "description"));
    databaseFieldMap.put("lon", new DatabaseField("sdu_cabinet_", "lon"));
    databaseFieldMap.put("location", new DatabaseField("sdu_cabinet_", "location"));
    databaseFieldMap.put("id", new DatabaseField("sdu_cabinet_", "id"));
    databaseFieldMap.put("conductImg", new DatabaseField("sdu_cabinet_", "conduct_img"));
    databaseFieldMap.put("lat", new DatabaseField("sdu_cabinet_", "lat"));
  }

  @Column(
      name = "cabinet_type_id"
  )
  private String cabinetTypeId;

  @Column(
      name = "manager"
  )
  private String manager;

  private String ip;

  @Column(
      name = "description"
  )
  private String description;

  @Column(
      name = "lon"
  )
  private String lon;

  @OneToMany(
      fetch = FetchType.EAGER,
      cascade = CascadeType.ALL
  )
  @JoinColumn(
      name = "videos_sdu_cabinet_id"
  )
  private Set<SduMedia> videos;

  @OneToOne(
      fetch = FetchType.EAGER
  )
  private SduMedia conductImg;

  private String deviceId;

  @OneToMany(
      fetch = FetchType.EAGER,
      cascade = CascadeType.ALL
  )
  @JoinColumn(
      name = "gates_sdu_cabinet_id"
  )
  private Set<SduGate> gates;

  @Column(
      name = "init_lock_state"
  )
  private String initLockState;

  @OneToMany(
      fetch = FetchType.EAGER,
      cascade = CascadeType.ALL
  )
  @JoinColumn(
      name = "rotation_img_sdu_cabinet_id"
  )
  private Set<SduMedia> rotationImg;

  @Column(
      name = "name"
  )
  private String name;

  private String topic;

  @Column(
      name = "location"
  )
  private String location;

  @Column(
      name = "id"
  )
  private String id;

  private String bindCode;

  @Column(
      name = "lat"
  )
  private String lat;

  public SduCabinet() {
    super();
  }

  public SduCabinet(String deviceId) {
    super(deviceId);
    setDeviceId(deviceId);
  }

  @JSONField(
      serialize = false
  )
  @Transient
  @Override
  public Protocol getProtocol() {
    return Protocol.MQTT;
  }

  @JSONField(
      serialize = false
  )
  @Transient
  @Override
  public Encryption getEncryption() {
    return Encryption.NONE;
  }

  @JSONField(
      serialize = false
  )
  @Transient
  @Override
  public boolean isDevice() {
    return true;
  }

  public String getCabinetTypeId() {
    return cabinetTypeId;
  }

  public void setCabinetTypeId(String cabinetTypeId) {
    EntityField field = new EntityField("SduCabinet", "cabinetTypeId", this.cabinetTypeId);
    this.cabinetTypeId = cabinetTypeId;
    field.setFieldValue(cabinetTypeId);
    notifyObservers(databaseFieldMap.get("cabinetTypeId"), field);
  }

  public String getManager() {
    return manager;
  }

  public void setManager(String manager) {
    EntityField field = new EntityField("SduCabinet", "manager", this.manager);
    this.manager = manager;
    field.setFieldValue(manager);
    notifyObservers(databaseFieldMap.get("manager"), field);
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
    super.setIp(ip);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    EntityField field = new EntityField("SduCabinet", "description", this.description);
    this.description = description;
    field.setFieldValue(description);
    notifyObservers(databaseFieldMap.get("description"), field);
  }

  public String getLon() {
    return lon;
  }

  public void setLon(String lon) {
    EntityField field = new EntityField("SduCabinet", "lon", this.lon);
    this.lon = lon;
    field.setFieldValue(lon);
    notifyObservers(databaseFieldMap.get("lon"), field);
  }

  public Set<SduMedia> getVideos() {
    return videos;
  }

  public void setVideos(Set<SduMedia> videos) {
    this.videos = videos;
  }

  public SduMedia getConductImg() {
    return conductImg;
  }

  public void setConductImg(SduMedia conductImg) {
    EntityField field = new EntityField("SduCabinet", "conductImg", this.conductImg);
    this.conductImg = conductImg;
    field.setFieldValue(conductImg);
    notifyObservers(databaseFieldMap.get("conductImg"), field);
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
    super.setDeviceId(deviceId);
  }

  public Set<SduGate> getGates() {
    return gates;
  }

  public void setGates(Set<SduGate> gates) {
    this.gates = gates;
  }

  public String getInitLockState() {
    return initLockState;
  }

  public void setInitLockState(String initLockState) {
    EntityField field = new EntityField("SduCabinet", "initLockState", this.initLockState);
    this.initLockState = initLockState;
    field.setFieldValue(initLockState);
    notifyObservers(databaseFieldMap.get("initLockState"), field);
  }

  public Set<SduMedia> getRotationImg() {
    return rotationImg;
  }

  public void setRotationImg(Set<SduMedia> rotationImg) {
    this.rotationImg = rotationImg;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    EntityField field = new EntityField("SduCabinet", "name", this.name);
    this.name = name;
    field.setFieldValue(name);
    notifyObservers(databaseFieldMap.get("name"), field);
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
    super.setTopic(topic);
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    EntityField field = new EntityField("SduCabinet", "location", this.location);
    this.location = location;
    field.setFieldValue(location);
    notifyObservers(databaseFieldMap.get("location"), field);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    EntityField field = new EntityField("SduCabinet", "id", this.id);
    this.id = id;
    field.setFieldValue(id);
    notifyObservers(databaseFieldMap.get("id"), field);
  }

  public String getBindCode() {
    return bindCode;
  }

  public void setBindCode(String bindCode) {
    this.bindCode = bindCode;
    super.setBindCode(bindCode);
  }

  public String getLat() {
    return lat;
  }

  public void setLat(String lat) {
    EntityField field = new EntityField("SduCabinet", "lat", this.lat);
    this.lat = lat;
    field.setFieldValue(lat);
    notifyObservers(databaseFieldMap.get("lat"), field);
  }
}

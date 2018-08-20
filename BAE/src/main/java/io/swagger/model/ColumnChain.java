/*
 * Boardwalk Cuboid Services
 * Boardwalk Rest API
 *
 * OpenAPI spec version: 1.0.2
 * Contact: apisupport@boardwalltech.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.validation.constraints.*;

/**
 * ColumnChain
 */

public class ColumnChain   {
  @JsonProperty("gridId")
  private Integer gridId = null;

  @JsonProperty("columnId")
  private Integer columnId = null;

  @JsonProperty("sequenceNumber")
  private Integer sequenceNumber = null;

  @JsonProperty("columnType")
  private String columnType = null;

  @JsonProperty("columnWidth")
  private Integer columnWidth = null;

  @JsonProperty("columnSource")
  private String columnSource = null;

  @JsonProperty("isActive")
  private Boolean isActive = null;

  @JsonProperty("ownerId")
  private Integer ownerId = null;

  @JsonProperty("ownerEmail")
  private String ownerEmail = null;

  @JsonProperty("creationTxId")
  private Integer creationTxId = null;

  @JsonProperty("creationDateTime")
  private BigDecimal creationDateTime = null;

  @JsonProperty("deletionTxId")
  private Integer deletionTxId = null;

  @JsonProperty("deletionDateTime")
  private BigDecimal deletionDateTime = null;

  public ColumnChain gridId(Integer gridId) {
    this.gridId = gridId;
    return this;
  }

  /**
   * Get gridId
   * @return gridId
   **/
  @JsonProperty("gridId")
  @ApiModelProperty(value = "")
  public Integer getGridId() {
    return gridId;
  }

  public void setGridId(Integer gridId) {
    this.gridId = gridId;
  }

  public ColumnChain columnId(Integer columnId) {
    this.columnId = columnId;
    return this;
  }

  /**
   * Get columnId
   * @return columnId
   **/
  @JsonProperty("columnId")
  @ApiModelProperty(value = "")
  public Integer getColumnId() {
    return columnId;
  }

  public void setColumnId(Integer columnId) {
    this.columnId = columnId;
  }

  public ColumnChain sequenceNumber(Integer sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
    return this;
  }

  /**
   * Get sequenceNumber
   * @return sequenceNumber
   **/
  @JsonProperty("sequenceNumber")
  @ApiModelProperty(value = "")
  public Integer getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(Integer sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public ColumnChain columnType(String columnType) {
    this.columnType = columnType;
    return this;
  }

  /**
   * Get columnType
   * @return columnType
   **/
  @JsonProperty("columnType")
  @ApiModelProperty(value = "")
  public String getColumnType() {
    return columnType;
  }

  public void setColumnType(String columnType) {
    this.columnType = columnType;
  }

  public ColumnChain columnWidth(Integer columnWidth) {
    this.columnWidth = columnWidth;
    return this;
  }

  /**
   * Get columnWidth
   * @return columnWidth
   **/
  @JsonProperty("columnWidth")
  @ApiModelProperty(value = "")
  public Integer getColumnWidth() {
    return columnWidth;
  }

  public void setColumnWidth(Integer columnWidth) {
    this.columnWidth = columnWidth;
  }

  public ColumnChain columnSource(String columnSource) {
    this.columnSource = columnSource;
    return this;
  }

  /**
   * Get columnSource
   * @return columnSource
   **/
  @JsonProperty("columnSource")
  @ApiModelProperty(value = "")
  public String getColumnSource() {
    return columnSource;
  }

  public void setColumnSource(String columnSource) {
    this.columnSource = columnSource;
  }

  public ColumnChain isActive(Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  /**
   * Get isActive
   * @return isActive
   **/
  @JsonProperty("isActive")
  @ApiModelProperty(value = "")
  public Boolean isIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public ColumnChain ownerId(Integer ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * Get ownerId
   * @return ownerId
   **/
  @JsonProperty("ownerId")
  @ApiModelProperty(value = "")
  public Integer getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Integer ownerId) {
    this.ownerId = ownerId;
  }

  public ColumnChain ownerEmail(String ownerEmail) {
    this.ownerEmail = ownerEmail;
    return this;
  }

  /**
   * Get ownerEmail
   * @return ownerEmail
   **/
  @JsonProperty("ownerEmail")
  @ApiModelProperty(value = "")
  public String getOwnerEmail() {
    return ownerEmail;
  }

  public void setOwnerEmail(String ownerEmail) {
    this.ownerEmail = ownerEmail;
  }

  public ColumnChain creationTxId(Integer creationTxId) {
    this.creationTxId = creationTxId;
    return this;
  }

  /**
   * Get creationTxId
   * @return creationTxId
   **/
  @JsonProperty("creationTxId")
  @ApiModelProperty(value = "")
  public Integer getCreationTxId() {
    return creationTxId;
  }

  public void setCreationTxId(Integer creationTxId) {
    this.creationTxId = creationTxId;
  }

  public ColumnChain creationDateTime(BigDecimal creationDateTime) {
    this.creationDateTime = creationDateTime;
    return this;
  }

  /**
   * Get creationDateTime
   * @return creationDateTime
   **/
  @JsonProperty("creationDateTime")
  @ApiModelProperty(value = "")
  public BigDecimal getCreationDateTime() {
    return creationDateTime;
  }

  public void setCreationDateTime(BigDecimal creationDateTime) {
    this.creationDateTime = creationDateTime;
  }

  public ColumnChain deletionTxId(Integer deletionTxId) {
    this.deletionTxId = deletionTxId;
    return this;
  }

  /**
   * Get deletionTxId
   * @return deletionTxId
   **/
  @JsonProperty("deletionTxId")
  @ApiModelProperty(value = "")
  public Integer getDeletionTxId() {
    return deletionTxId;
  }

  public void setDeletionTxId(Integer deletionTxId) {
    this.deletionTxId = deletionTxId;
  }

  public ColumnChain deletionDateTime(BigDecimal deletionDateTime) {
    this.deletionDateTime = deletionDateTime;
    return this;
  }

  /**
   * Get deletionDateTime
   * @return deletionDateTime
   **/
  @JsonProperty("deletionDateTime")
  @ApiModelProperty(value = "")
  public BigDecimal getDeletionDateTime() {
    return deletionDateTime;
  }

  public void setDeletionDateTime(BigDecimal deletionDateTime) {
    this.deletionDateTime = deletionDateTime;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ColumnChain columnChain = (ColumnChain) o;
    return Objects.equals(this.gridId, columnChain.gridId) &&
        Objects.equals(this.columnId, columnChain.columnId) &&
        Objects.equals(this.sequenceNumber, columnChain.sequenceNumber) &&
        Objects.equals(this.columnType, columnChain.columnType) &&
        Objects.equals(this.columnWidth, columnChain.columnWidth) &&
        Objects.equals(this.columnSource, columnChain.columnSource) &&
        Objects.equals(this.isActive, columnChain.isActive) &&
        Objects.equals(this.ownerId, columnChain.ownerId) &&
        Objects.equals(this.ownerEmail, columnChain.ownerEmail) &&
        Objects.equals(this.creationTxId, columnChain.creationTxId) &&
        Objects.equals(this.creationDateTime, columnChain.creationDateTime) &&
        Objects.equals(this.deletionTxId, columnChain.deletionTxId) &&
        Objects.equals(this.deletionDateTime, columnChain.deletionDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gridId, columnId, sequenceNumber, columnType, columnWidth, columnSource, isActive, ownerId, ownerEmail, creationTxId, creationDateTime, deletionTxId, deletionDateTime);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ColumnChain {\n");
    
    sb.append("    gridId: ").append(toIndentedString(gridId)).append("\n");
    sb.append("    columnId: ").append(toIndentedString(columnId)).append("\n");
    sb.append("    sequenceNumber: ").append(toIndentedString(sequenceNumber)).append("\n");
    sb.append("    columnType: ").append(toIndentedString(columnType)).append("\n");
    sb.append("    columnWidth: ").append(toIndentedString(columnWidth)).append("\n");
    sb.append("    columnSource: ").append(toIndentedString(columnSource)).append("\n");
    sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    ownerEmail: ").append(toIndentedString(ownerEmail)).append("\n");
    sb.append("    creationTxId: ").append(toIndentedString(creationTxId)).append("\n");
    sb.append("    creationDateTime: ").append(toIndentedString(creationDateTime)).append("\n");
    sb.append("    deletionTxId: ").append(toIndentedString(deletionTxId)).append("\n");
    sb.append("    deletionDateTime: ").append(toIndentedString(deletionDateTime)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


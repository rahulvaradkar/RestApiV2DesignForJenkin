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
import javax.validation.constraints.*;

/**
 * RowChain
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-09-18T12:35:03.779Z")
public class RowChain   {
  @JsonProperty("gridId")
  private Integer gridId = null;

  @JsonProperty("rowId")
  private Integer rowId = null;

  @JsonProperty("sequNo")
  private Float sequNo = null;

  @JsonProperty("isActive")
  private Boolean isActive = null;

  @JsonProperty("ownerId")
  private Integer ownerId = null;

  @JsonProperty("ownerEmail")
  private String ownerEmail = null;

  @JsonProperty("creationTxId")
  private Integer creationTxId = null;

  @JsonProperty("deletionTxId")
  private Integer deletionTxId = null;

  public RowChain gridId(Integer gridId) {
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

  public RowChain rowId(Integer rowId) {
    this.rowId = rowId;
    return this;
  }

  /**
   * Get rowId
   * @return rowId
   **/
  @JsonProperty("rowId")
  @ApiModelProperty(value = "")
  public Integer getRowId() {
    return rowId;
  }

  public void setRowId(Integer rowId) {
    this.rowId = rowId;
  }

  public RowChain sequNo(Float sequNo) {
    this.sequNo = sequNo;
    return this;
  }

  /**
   * Get sequNo
   * @return sequNo
   **/
  @JsonProperty("sequNo")
  @ApiModelProperty(value = "")
  public Float getSequNo() {
    return sequNo;
  }

  public void setSequNo(Float sequNo) {
    this.sequNo = sequNo;
  }

  public RowChain isActive(Boolean isActive) {
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

  public RowChain ownerId(Integer ownerId) {
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

  public RowChain ownerEmail(String ownerEmail) {
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

  public RowChain creationTxId(Integer creationTxId) {
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

  public RowChain deletionTxId(Integer deletionTxId) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RowChain rowChain = (RowChain) o;
    return Objects.equals(this.gridId, rowChain.gridId) &&
        Objects.equals(this.rowId, rowChain.rowId) &&
        Objects.equals(this.sequNo, rowChain.sequNo) &&
        Objects.equals(this.isActive, rowChain.isActive) &&
        Objects.equals(this.ownerId, rowChain.ownerId) &&
        Objects.equals(this.ownerEmail, rowChain.ownerEmail) &&
        Objects.equals(this.creationTxId, rowChain.creationTxId) &&
        Objects.equals(this.deletionTxId, rowChain.deletionTxId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gridId, rowId, sequNo, isActive, ownerId, ownerEmail, creationTxId, deletionTxId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RowChain {\n");
    
    sb.append("    gridId: ").append(toIndentedString(gridId)).append("\n");
    sb.append("    rowId: ").append(toIndentedString(rowId)).append("\n");
    sb.append("    sequNo: ").append(toIndentedString(sequNo)).append("\n");
    sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    ownerEmail: ").append(toIndentedString(ownerEmail)).append("\n");
    sb.append("    creationTxId: ").append(toIndentedString(creationTxId)).append("\n");
    sb.append("    deletionTxId: ").append(toIndentedString(deletionTxId)).append("\n");
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


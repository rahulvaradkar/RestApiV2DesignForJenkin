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
import io.swagger.model.Transaction;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

/**
 * CellStatus
 */

public class CellStatus   {
  @JsonProperty("cellStatusId")
  private Integer cellStatusId = null;

  @JsonProperty("cellId")
  private Integer cellId = null;

  @JsonProperty("isActive")
  private Integer isActive = null;

  @JsonProperty("statusTransaction")
  private List<Transaction> statusTransaction = null;

  public CellStatus cellStatusId(Integer cellStatusId) {
    this.cellStatusId = cellStatusId;
    return this;
  }

  /**
   * Get cellStatusId
   * @return cellStatusId
   **/
  @JsonProperty("cellStatusId")
  @ApiModelProperty(value = "")
  public Integer getCellStatusId() {
    return cellStatusId;
  }

  public void setCellStatusId(Integer cellStatusId) {
    this.cellStatusId = cellStatusId;
  }

  public CellStatus cellId(Integer cellId) {
    this.cellId = cellId;
    return this;
  }

  /**
   * Get cellId
   * @return cellId
   **/
  @JsonProperty("cellId")
  @ApiModelProperty(value = "")
  public Integer getCellId() {
    return cellId;
  }

  public void setCellId(Integer cellId) {
    this.cellId = cellId;
  }

  public CellStatus isActive(Integer isActive) {
    this.isActive = isActive;
    return this;
  }

  /**
   * Get isActive
   * @return isActive
   **/
  @JsonProperty("isActive")
  @ApiModelProperty(value = "")
  public Integer getIsActive() {
    return isActive;
  }

  public void setIsActive(Integer isActive) {
    this.isActive = isActive;
  }

  public CellStatus statusTransaction(List<Transaction> statusTransaction) {
    this.statusTransaction = statusTransaction;
    return this;
  }

  public CellStatus addStatusTransactionItem(Transaction statusTransactionItem) {
    if (this.statusTransaction == null) {
      this.statusTransaction = new ArrayList<Transaction>();
    }
    this.statusTransaction.add(statusTransactionItem);
    return this;
  }

  /**
   * Get statusTransaction
   * @return statusTransaction
   **/
  @JsonProperty("statusTransaction")
  @ApiModelProperty(value = "")
  public List<Transaction> getStatusTransaction() {
    return statusTransaction;
  }

  public void setStatusTransaction(List<Transaction> statusTransaction) {
    this.statusTransaction = statusTransaction;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CellStatus cellStatus = (CellStatus) o;
    return Objects.equals(this.cellStatusId, cellStatus.cellStatusId) &&
        Objects.equals(this.cellId, cellStatus.cellId) &&
        Objects.equals(this.isActive, cellStatus.isActive) &&
        Objects.equals(this.statusTransaction, cellStatus.statusTransaction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cellStatusId, cellId, isActive, statusTransaction);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CellStatus {\n");
    
    sb.append("    cellStatusId: ").append(toIndentedString(cellStatusId)).append("\n");
    sb.append("    cellId: ").append(toIndentedString(cellId)).append("\n");
    sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
    sb.append("    statusTransaction: ").append(toIndentedString(statusTransaction)).append("\n");
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


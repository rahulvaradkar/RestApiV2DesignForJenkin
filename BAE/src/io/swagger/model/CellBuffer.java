/*
 * Boardwalk Cuboid Services
 * Boardwalk Rest API
 *
 * OpenAPI spec version: 1.0.0
 * Contact: support@boardwalltech.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Cell;
import io.swagger.model.Column;
import io.swagger.model.GridInfo;
import io.swagger.model.Row;
import io.swagger.model.SequencedCellArray;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

/**
 * CellBuffer
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-07T09:34:58.805Z")
public class CellBuffer   {
  @JsonProperty("info")
  private GridInfo info = null;

  @JsonProperty("rowArray")
  private List<Integer> rowArray = null;

  @JsonProperty("columnArray")
  private List<Integer> columnArray = null;

  @JsonProperty("columnCellArrays")
  private List<SequencedCellArray> columnCellArrays = null;

  @JsonProperty("rows")
  private List<Row> rows = null;

  @JsonProperty("columns")
  private List<Column> columns = null;

  @JsonProperty("cells")
  private List<Cell> cells = null;

  @JsonProperty("GridChangeBuffer")
  private Object gridChangeBuffer = null;

  public CellBuffer info(GridInfo info) {
    this.info = info;
    return this;
  }

  /**
   * Get info
   * @return info
   **/
  @JsonProperty("info")
  @ApiModelProperty(value = "")
  public GridInfo getInfo() {
    return info;
  }

  public void setInfo(GridInfo info) {
    this.info = info;
  }

  public CellBuffer rowArray(List<Integer> rowArray) {
    this.rowArray = rowArray;
    return this;
  }

  public CellBuffer addRowArrayItem(Integer rowArrayItem) {
    if (this.rowArray == null) {
      this.rowArray = new ArrayList<Integer>();
    }
    this.rowArray.add(rowArrayItem);
    return this;
  }

  /**
   * Get rowArray
   * @return rowArray
   **/
  @JsonProperty("rowArray")
  @ApiModelProperty(value = "")
  public List<Integer> getRowArray() {
    return rowArray;
  }

  public void setRowArray(List<Integer> rowArray) {
    this.rowArray = rowArray;
  }

  public CellBuffer columnArray(List<Integer> columnArray) {
    this.columnArray = columnArray;
    return this;
  }

  public CellBuffer addColumnArrayItem(Integer columnArrayItem) {
    if (this.columnArray == null) {
      this.columnArray = new ArrayList<Integer>();
    }
    this.columnArray.add(columnArrayItem);
    return this;
  }

  /**
   * Get columnArray
   * @return columnArray
   **/
  @JsonProperty("columnArray")
  @ApiModelProperty(value = "")
  public List<Integer> getColumnArray() {
    return columnArray;
  }

  public void setColumnArray(List<Integer> columnArray) {
    this.columnArray = columnArray;
  }

  public CellBuffer columnCellArrays(List<SequencedCellArray> columnCellArrays) {
    this.columnCellArrays = columnCellArrays;
    return this;
  }

  public CellBuffer addColumnCellArraysItem(SequencedCellArray columnCellArraysItem) {
    if (this.columnCellArrays == null) {
      this.columnCellArrays = new ArrayList<SequencedCellArray>();
    }
    this.columnCellArrays.add(columnCellArraysItem);
    return this;
  }

  /**
   * Get columnCellArrays
   * @return columnCellArrays
   **/
  @JsonProperty("columnCellArrays")
  @ApiModelProperty(value = "")
  public List<SequencedCellArray> getColumnCellArrays() {
    return columnCellArrays;
  }

  public void setColumnCellArrays(List<SequencedCellArray> columnCellArrays) {
    this.columnCellArrays = columnCellArrays;
  }

  public CellBuffer rows(List<Row> rows) {
    this.rows = rows;
    return this;
  }

  public CellBuffer addRowsItem(Row rowsItem) {
    if (this.rows == null) {
      this.rows = new ArrayList<Row>();
    }
    this.rows.add(rowsItem);
    return this;
  }

  /**
   * Get rows
   * @return rows
   **/
  @JsonProperty("rows")
  @ApiModelProperty(value = "")
  public List<Row> getRows() {
    return rows;
  }

  public void setRows(List<Row> rows) {
    this.rows = rows;
  }

  public CellBuffer columns(List<Column> columns) {
    this.columns = columns;
    return this;
  }

  public CellBuffer addColumnsItem(Column columnsItem) {
    if (this.columns == null) {
      this.columns = new ArrayList<Column>();
    }
    this.columns.add(columnsItem);
    return this;
  }

  /**
   * Get columns
   * @return columns
   **/
  @JsonProperty("columns")
  @ApiModelProperty(value = "")
  public List<Column> getColumns() {
    return columns;
  }

  public void setColumns(List<Column> columns) {
    this.columns = columns;
  }

  public CellBuffer cells(List<Cell> cells) {
    this.cells = cells;
    return this;
  }

  public CellBuffer addCellsItem(Cell cellsItem) {
    if (this.cells == null) {
      this.cells = new ArrayList<Cell>();
    }
    this.cells.add(cellsItem);
    return this;
  }

  /**
   * Get cells
   * @return cells
   **/
  @JsonProperty("cells")
  @ApiModelProperty(value = "")
  public List<Cell> getCells() {
    return cells;
  }

  public void setCells(List<Cell> cells) {
    this.cells = cells;
  }

  public CellBuffer gridChangeBuffer(Object gridChangeBuffer) {
    this.gridChangeBuffer = gridChangeBuffer;
    return this;
  }

  /**
   * Get gridChangeBuffer
   * @return gridChangeBuffer
   **/
  @JsonProperty("GridChangeBuffer")
  @ApiModelProperty(value = "")
  public Object getGridChangeBuffer() {
    return gridChangeBuffer;
  }

  public void setGridChangeBuffer(Object gridChangeBuffer) {
    this.gridChangeBuffer = gridChangeBuffer;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CellBuffer cellBuffer = (CellBuffer) o;
    return Objects.equals(this.info, cellBuffer.info) &&
        Objects.equals(this.rowArray, cellBuffer.rowArray) &&
        Objects.equals(this.columnArray, cellBuffer.columnArray) &&
        Objects.equals(this.columnCellArrays, cellBuffer.columnCellArrays) &&
        Objects.equals(this.rows, cellBuffer.rows) &&
        Objects.equals(this.columns, cellBuffer.columns) &&
        Objects.equals(this.cells, cellBuffer.cells) &&
        Objects.equals(this.gridChangeBuffer, cellBuffer.gridChangeBuffer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(info, rowArray, columnArray, columnCellArrays, rows, columns, cells, gridChangeBuffer);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CellBuffer {\n");
    
    sb.append("    info: ").append(toIndentedString(info)).append("\n");
    sb.append("    rowArray: ").append(toIndentedString(rowArray)).append("\n");
    sb.append("    columnArray: ").append(toIndentedString(columnArray)).append("\n");
    sb.append("    columnCellArrays: ").append(toIndentedString(columnCellArrays)).append("\n");
    sb.append("    rows: ").append(toIndentedString(rows)).append("\n");
    sb.append("    columns: ").append(toIndentedString(columns)).append("\n");
    sb.append("    cells: ").append(toIndentedString(cells)).append("\n");
    sb.append("    gridChangeBuffer: ").append(toIndentedString(gridChangeBuffer)).append("\n");
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


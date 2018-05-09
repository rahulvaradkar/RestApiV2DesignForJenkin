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
import io.swagger.model.NeighborhoodPath;
import javax.validation.constraints.*;

/**
 * GridInfo
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-07T10:02:31.749Z")
public class GridInfo   {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("view")
  private String view = null;

  @JsonProperty("importTid")
  private Integer importTid = null;

  @JsonProperty("exportTid")
  private Integer exportTid = null;

  @JsonProperty("filter")
  private String filter = null;

  @JsonProperty("asOfTid")
  private Integer asOfTid = null;

  @JsonProperty("baselineId")
  private Integer baselineId = null;

  @JsonProperty("serverName")
  private String serverName = null;

  @JsonProperty("serverURL")
  private String serverURL = null;

  @JsonProperty("collabId")
  private Integer collabId = null;

  @JsonProperty("wbId")
  private Integer wbId = null;

  @JsonProperty("memberId")
  private Integer memberId = null;

  @JsonProperty("neighborhoodHeirarchy")
  private NeighborhoodPath neighborhoodHeirarchy = null;

  public GridInfo id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @JsonProperty("id")
  @ApiModelProperty(value = "")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public GridInfo name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   **/
  @JsonProperty("name")
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GridInfo view(String view) {
    this.view = view;
    return this;
  }

  /**
   * Get view
   * @return view
   **/
  @JsonProperty("view")
  @ApiModelProperty(value = "")
  public String getView() {
    return view;
  }

  public void setView(String view) {
    this.view = view;
  }

  public GridInfo importTid(Integer importTid) {
    this.importTid = importTid;
    return this;
  }

  /**
   * Get importTid
   * @return importTid
   **/
  @JsonProperty("importTid")
  @ApiModelProperty(value = "")
  public Integer getImportTid() {
    return importTid;
  }

  public void setImportTid(Integer importTid) {
    this.importTid = importTid;
  }

  public GridInfo exportTid(Integer exportTid) {
    this.exportTid = exportTid;
    return this;
  }

  /**
   * Get exportTid
   * @return exportTid
   **/
  @JsonProperty("exportTid")
  @ApiModelProperty(value = "")
  public Integer getExportTid() {
    return exportTid;
  }

  public void setExportTid(Integer exportTid) {
    this.exportTid = exportTid;
  }

  public GridInfo filter(String filter) {
    this.filter = filter;
    return this;
  }

  /**
   * Get filter
   * @return filter
   **/
  @JsonProperty("filter")
  @ApiModelProperty(value = "")
  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  public GridInfo asOfTid(Integer asOfTid) {
    this.asOfTid = asOfTid;
    return this;
  }

  /**
   * Get asOfTid
   * @return asOfTid
   **/
  @JsonProperty("asOfTid")
  @ApiModelProperty(value = "")
  public Integer getAsOfTid() {
    return asOfTid;
  }

  public void setAsOfTid(Integer asOfTid) {
    this.asOfTid = asOfTid;
  }

  public GridInfo baselineId(Integer baselineId) {
    this.baselineId = baselineId;
    return this;
  }

  /**
   * Get baselineId
   * @return baselineId
   **/
  @JsonProperty("baselineId")
  @ApiModelProperty(value = "")
  public Integer getBaselineId() {
    return baselineId;
  }

  public void setBaselineId(Integer baselineId) {
    this.baselineId = baselineId;
  }

  public GridInfo serverName(String serverName) {
    this.serverName = serverName;
    return this;
  }

  /**
   * Get serverName
   * @return serverName
   **/
  @JsonProperty("serverName")
  @ApiModelProperty(value = "")
  public String getServerName() {
    return serverName;
  }

  public void setServerName(String serverName) {
    this.serverName = serverName;
  }

  public GridInfo serverURL(String serverURL) {
    this.serverURL = serverURL;
    return this;
  }

  /**
   * Get serverURL
   * @return serverURL
   **/
  @JsonProperty("serverURL")
  @ApiModelProperty(value = "")
  public String getServerURL() {
    return serverURL;
  }

  public void setServerURL(String serverURL) {
    this.serverURL = serverURL;
  }

  public GridInfo collabId(Integer collabId) {
    this.collabId = collabId;
    return this;
  }

  /**
   * Get collabId
   * @return collabId
   **/
  @JsonProperty("collabId")
  @ApiModelProperty(value = "")
  public Integer getCollabId() {
    return collabId;
  }

  public void setCollabId(Integer collabId) {
    this.collabId = collabId;
  }

  public GridInfo wbId(Integer wbId) {
    this.wbId = wbId;
    return this;
  }

  /**
   * Get wbId
   * @return wbId
   **/
  @JsonProperty("wbId")
  @ApiModelProperty(value = "")
  public Integer getWbId() {
    return wbId;
  }

  public void setWbId(Integer wbId) {
    this.wbId = wbId;
  }

  public GridInfo memberId(Integer memberId) {
    this.memberId = memberId;
    return this;
  }

  /**
   * Get memberId
   * @return memberId
   **/
  @JsonProperty("memberId")
  @ApiModelProperty(value = "")
  public Integer getMemberId() {
    return memberId;
  }

  public void setMemberId(Integer memberId) {
    this.memberId = memberId;
  }

  public GridInfo neighborhoodHeirarchy(NeighborhoodPath neighborhoodHeirarchy) {
    this.neighborhoodHeirarchy = neighborhoodHeirarchy;
    return this;
  }

  /**
   * Get neighborhoodHeirarchy
   * @return neighborhoodHeirarchy
   **/
  @JsonProperty("neighborhoodHeirarchy")
  @ApiModelProperty(value = "")
  public NeighborhoodPath getNeighborhoodHeirarchy() {
    return neighborhoodHeirarchy;
  }

  public void setNeighborhoodHeirarchy(NeighborhoodPath neighborhoodHeirarchy) {
    this.neighborhoodHeirarchy = neighborhoodHeirarchy;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GridInfo gridInfo = (GridInfo) o;
    return Objects.equals(this.id, gridInfo.id) &&
        Objects.equals(this.name, gridInfo.name) &&
        Objects.equals(this.view, gridInfo.view) &&
        Objects.equals(this.importTid, gridInfo.importTid) &&
        Objects.equals(this.exportTid, gridInfo.exportTid) &&
        Objects.equals(this.filter, gridInfo.filter) &&
        Objects.equals(this.asOfTid, gridInfo.asOfTid) &&
        Objects.equals(this.baselineId, gridInfo.baselineId) &&
        Objects.equals(this.serverName, gridInfo.serverName) &&
        Objects.equals(this.serverURL, gridInfo.serverURL) &&
        Objects.equals(this.collabId, gridInfo.collabId) &&
        Objects.equals(this.wbId, gridInfo.wbId) &&
        Objects.equals(this.memberId, gridInfo.memberId) &&
        Objects.equals(this.neighborhoodHeirarchy, gridInfo.neighborhoodHeirarchy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, view, importTid, exportTid, filter, asOfTid, baselineId, serverName, serverURL, collabId, wbId, memberId, neighborhoodHeirarchy);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GridInfo {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    view: ").append(toIndentedString(view)).append("\n");
    sb.append("    importTid: ").append(toIndentedString(importTid)).append("\n");
    sb.append("    exportTid: ").append(toIndentedString(exportTid)).append("\n");
    sb.append("    filter: ").append(toIndentedString(filter)).append("\n");
    sb.append("    asOfTid: ").append(toIndentedString(asOfTid)).append("\n");
    sb.append("    baselineId: ").append(toIndentedString(baselineId)).append("\n");
    sb.append("    serverName: ").append(toIndentedString(serverName)).append("\n");
    sb.append("    serverURL: ").append(toIndentedString(serverURL)).append("\n");
    sb.append("    collabId: ").append(toIndentedString(collabId)).append("\n");
    sb.append("    wbId: ").append(toIndentedString(wbId)).append("\n");
    sb.append("    memberId: ").append(toIndentedString(memberId)).append("\n");
    sb.append("    neighborhoodHeirarchy: ").append(toIndentedString(neighborhoodHeirarchy)).append("\n");
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


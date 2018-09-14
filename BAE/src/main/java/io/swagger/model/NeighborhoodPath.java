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
import javax.validation.constraints.*;

/**
 * NeighborhoodPath
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-05-10T14:51:11.960Z")
public class NeighborhoodPath   {
  @JsonProperty("levels")
  private Integer levels = null;

  @JsonProperty("nh_Level_0")
  private Integer nhLevel0 = null;

  @JsonProperty("nh_Level_1")
  private Integer nhLevel1 = null;

  @JsonProperty("nh_level_2")
  private Integer nhLevel2 = null;

  @JsonProperty("nh_level_3")
  private Integer nhLevel3 = null;

  public NeighborhoodPath levels(Integer levels) {
    this.levels = levels;
    return this;
  }

  /**
   * Get levels
   * @return levels
   **/
  @JsonProperty("levels")
  @ApiModelProperty(value = "")
  public Integer getLevels() {
    return levels;
  }

  public void setLevels(Integer levels) {
    this.levels = levels;
  }

  public NeighborhoodPath nhLevel0(Integer nhLevel0) {
    this.nhLevel0 = nhLevel0;
    return this;
  }

  /**
   * Get nhLevel0
   * @return nhLevel0
   **/
  @JsonProperty("nh_Level_0")
  @ApiModelProperty(value = "")
  public Integer getNhLevel0() {
    return nhLevel0;
  }

  public void setNhLevel0(Integer nhLevel0) {
    this.nhLevel0 = nhLevel0;
  }

  public NeighborhoodPath nhLevel1(Integer nhLevel1) {
    this.nhLevel1 = nhLevel1;
    return this;
  }

  /**
   * Get nhLevel1
   * @return nhLevel1
   **/
  @JsonProperty("nh_Level_1")
  @ApiModelProperty(value = "")
  public Integer getNhLevel1() {
    return nhLevel1;
  }

  public void setNhLevel1(Integer nhLevel1) {
    this.nhLevel1 = nhLevel1;
  }

  public NeighborhoodPath nhLevel2(Integer nhLevel2) {
    this.nhLevel2 = nhLevel2;
    return this;
  }

  /**
   * Get nhLevel2
   * @return nhLevel2
   **/
  @JsonProperty("nh_level_2")
  @ApiModelProperty(value = "")
  public Integer getNhLevel2() {
    return nhLevel2;
  }

  public void setNhLevel2(Integer nhLevel2) {
    this.nhLevel2 = nhLevel2;
  }

  public NeighborhoodPath nhLevel3(Integer nhLevel3) {
    this.nhLevel3 = nhLevel3;
    return this;
  }

  /**
   * Get nhLevel3
   * @return nhLevel3
   **/
  @JsonProperty("nh_level_3")
  @ApiModelProperty(value = "")
  public Integer getNhLevel3() {
    return nhLevel3;
  }

  public void setNhLevel3(Integer nhLevel3) {
    this.nhLevel3 = nhLevel3;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NeighborhoodPath neighborhoodPath = (NeighborhoodPath) o;
    return Objects.equals(this.levels, neighborhoodPath.levels) &&
        Objects.equals(this.nhLevel0, neighborhoodPath.nhLevel0) &&
        Objects.equals(this.nhLevel1, neighborhoodPath.nhLevel1) &&
        Objects.equals(this.nhLevel2, neighborhoodPath.nhLevel2) &&
        Objects.equals(this.nhLevel3, neighborhoodPath.nhLevel3);
  }

  @Override
  public int hashCode() {
    return Objects.hash(levels, nhLevel0, nhLevel1, nhLevel2, nhLevel3);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NeighborhoodPath {\n");
    
    sb.append("    levels: ").append(toIndentedString(levels)).append("\n");
    sb.append("    nhLevel0: ").append(toIndentedString(nhLevel0)).append("\n");
    sb.append("    nhLevel1: ").append(toIndentedString(nhLevel1)).append("\n");
    sb.append("    nhLevel2: ").append(toIndentedString(nhLevel2)).append("\n");
    sb.append("    nhLevel3: ").append(toIndentedString(nhLevel3)).append("\n");
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


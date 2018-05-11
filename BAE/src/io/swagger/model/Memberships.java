/*
 * Boardwalk Collaboration Platform API
 * API for Boardwalk Collaboration Platform APIs
 *
 * OpenAPI spec version: 1.0.0
 * Contact: rahul_var@yahoo.com
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
 * Memberships
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-04-23T07:19:11.481Z")
public class Memberships   {
  @JsonProperty("memberId")
  private Integer memberId = null;

  @JsonProperty("nhId")
  private Integer nhId = null;

  @JsonProperty("nhName")
  private String nhName = null;

  public Memberships memberId(Integer memberId) {
    this.memberId = memberId;
    return this;
  }

  /**
   * Get memberId
   * @return memberId
   **/
  @JsonProperty("memberId")
  @ApiModelProperty(example = "54545", required = true, value = "")
  @NotNull
  public Integer getMemberId() {
    return memberId;
  }

  public void setMemberId(Integer memberId) {
    this.memberId = memberId;
  }

  public Memberships nhId(Integer nhId) {
    this.nhId = nhId;
    return this;
  }

  /**
   * Get nhId
   * @return nhId
   **/
  @JsonProperty("nhId")
  @ApiModelProperty(example = "455", required = true, value = "")
  @NotNull
  public Integer getNhId() {
    return nhId;
  }

  public void setNhId(Integer nhId) {
    this.nhId = nhId;
  }

  public Memberships nhName(String nhName) {
    this.nhName = nhName;
    return this;
  }

  /**
   * Get nhName
   * @return nhName
   **/
  @JsonProperty("nhName")
  @ApiModelProperty(example = "APAC", required = true, value = "")
  @NotNull
  public String getNhName() {
    return nhName;
  }

  public void setNhName(String nhName) {
    this.nhName = nhName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Memberships memberships = (Memberships) o;
    return Objects.equals(this.memberId, memberships.memberId) &&
        Objects.equals(this.nhId, memberships.nhId) &&
        Objects.equals(this.nhName, memberships.nhName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(memberId, nhId, nhName);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Memberships {\n");
    
    sb.append("    memberId: ").append(toIndentedString(memberId)).append("\n");
    sb.append("    nhId: ").append(toIndentedString(nhId)).append("\n");
    sb.append("    nhName: ").append(toIndentedString(nhName)).append("\n");
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


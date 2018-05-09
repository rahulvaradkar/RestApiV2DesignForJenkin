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
import io.swagger.model.NhChild;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

/**
 * NhHierarchy
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-04-23T07:19:11.481Z")
public class NhHierarchy   {
  @JsonProperty("this_nh")
  private Object thisNh = null;

  @JsonProperty("nh_children")
  private List<NhChild> nhChildren = new ArrayList<NhChild>();

  public NhHierarchy thisNh(Object thisNh) {
    this.thisNh = thisNh;
    return this;
  }

  /**
   * Get thisNh
   * @return thisNh
   **/
  @JsonProperty("this_nh")
  @ApiModelProperty(required = true, value = "")
  @NotNull
  public Object getThisNh() {
    return thisNh;
  }

  public void setThisNh(Object thisNh) {
    this.thisNh = thisNh;
  }

  public NhHierarchy nhChildren(List<NhChild> nhChildren) {
    this.nhChildren = nhChildren;
    return this;
  }

  public NhHierarchy addNhChildrenItem(NhChild nhChildrenItem) {
    this.nhChildren.add(nhChildrenItem);
    return this;
  }

  /**
   * Get nhChildren
   * @return nhChildren
   **/
  @JsonProperty("nh_children")
  @ApiModelProperty(required = true, value = "")
  @NotNull
  public List<NhChild> getNhChildren() {
    return nhChildren;
  }

  public void setNhChildren(List<NhChild> nhChildren) {
    this.nhChildren = nhChildren;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NhHierarchy nhHierarchy = (NhHierarchy) o;
    return Objects.equals(this.thisNh, nhHierarchy.thisNh) &&
        Objects.equals(this.nhChildren, nhHierarchy.nhChildren);
  }

  @Override
  public int hashCode() {
    return Objects.hash(thisNh, nhChildren);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NhHierarchy {\n");
    
    sb.append("    thisNh: ").append(toIndentedString(thisNh)).append("\n");
    sb.append("    nhChildren: ").append(toIndentedString(nhChildren)).append("\n");
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


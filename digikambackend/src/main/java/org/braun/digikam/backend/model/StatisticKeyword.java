/*
 * Digikam
 * RESTful Backend for Digikam
 *
 * The version of the OpenAPI document: 1.0
 * Contact: braun.h.michael@gmail.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.braun.digikam.backend.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

/**
 * StatisticKeyword
 */
@JsonPropertyOrder({
  StatisticKeyword.JSON_PROPERTY_NAME,
  StatisticKeyword.JSON_PROPERTY_COUNT
})
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-12-27T20:04:01.965701513+01:00[Europe/Berlin]")
public class StatisticKeyword   {
  public static final String JSON_PROPERTY_NAME = "name";
  @JsonProperty(JSON_PROPERTY_NAME)
  private String name;

  public static final String JSON_PROPERTY_COUNT = "count";
  @JsonProperty(JSON_PROPERTY_COUNT)
  private Integer count;

  public StatisticKeyword name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   **/
  @JsonProperty(value = "name")
  @ApiModelProperty(required = true, value = "")
  @NotNull 
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public StatisticKeyword count(Integer count) {
    this.count = count;
    return this;
  }

  /**
   * Get count
   * @return count
   **/
  @JsonProperty(value = "count")
  @ApiModelProperty(required = true, value = "")
  @NotNull 
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatisticKeyword statisticKeyword = (StatisticKeyword) o;
    return Objects.equals(this.name, statisticKeyword.name) &&
        Objects.equals(this.count, statisticKeyword.count);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, count);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatisticKeyword {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


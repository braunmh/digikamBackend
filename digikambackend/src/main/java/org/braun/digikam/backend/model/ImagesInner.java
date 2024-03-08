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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * ImagesInner
 */
@JsonPropertyOrder({
  ImagesInner.JSON_PROPERTY_IMAGE_ID,
  ImagesInner.JSON_PROPERTY_CREATION_DATE
})
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-12-27T20:04:01.965701513+01:00[Europe/Berlin]")
public class ImagesInner   {
  public static final String JSON_PROPERTY_IMAGE_ID = "imageId";
  @JsonProperty(JSON_PROPERTY_IMAGE_ID)
  private Integer imageId;

  public static final String JSON_PROPERTY_CREATION_DATE = "creationDate";
  @JsonProperty(JSON_PROPERTY_CREATION_DATE)
  private LocalDateTime creationDate;

  public ImagesInner imageId(Integer imageId) {
    this.imageId = imageId;
    return this;
  }

  /**
   * Get imageId
   * @return imageId
   **/
  @JsonProperty(value = "imageId")
  @ApiModelProperty(value = "")
  
  public Integer getImageId() {
    return imageId;
  }

  public void setImageId(Integer imageId) {
    this.imageId = imageId;
  }

  public ImagesInner creationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
    return this;
  }

  /**
   * Date Image was taken
   * @return creationDate
   **/
  @JsonProperty(value = "creationDate")
  @ApiModelProperty(value = "Date Image was taken")
  
  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImagesInner imagesInner = (ImagesInner) o;
    return Objects.equals(this.imageId, imagesInner.imageId) &&
        Objects.equals(this.creationDate, imagesInner.creationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(imageId, creationDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImagesInner {\n");
    
    sb.append("    imageId: ").append(toIndentedString(imageId)).append("\n");
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
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


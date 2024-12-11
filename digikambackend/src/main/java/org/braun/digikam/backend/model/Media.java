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
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;

/**
 * Media
 */
@JsonPropertyOrder({
  Media.JSON_PROPERTY_ID,
  Media.JSON_PROPERTY_NAME,
  Media.JSON_PROPERTY_CREATION_DATE,
  Media.JSON_PROPERTY_IMAGE,
  Media.JSON_PROPERTY_SCORE,
  Media.JSON_PROPERTY_WIDTH,
  Media.JSON_PROPERTY_HEIGHT,
  Media.JSON_PROPERTY_ORIENTATION
})
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-12-11T15:10:07.644747436+01:00[Europe/Berlin]", comments = "Generator version: 7.7.0")
public class Media   {
  public static final String JSON_PROPERTY_ID = "id";
  @JsonProperty(JSON_PROPERTY_ID)
  private Long id;

  public static final String JSON_PROPERTY_NAME = "name";
  @JsonProperty(JSON_PROPERTY_NAME)
  private String name;

  public static final String JSON_PROPERTY_CREATION_DATE = "creationDate";
  @JsonProperty(JSON_PROPERTY_CREATION_DATE)
  private LocalDateTime creationDate;

  public static final String JSON_PROPERTY_IMAGE = "image";
  @JsonProperty(JSON_PROPERTY_IMAGE)
  private Boolean image;

  public static final String JSON_PROPERTY_SCORE = "score";
  @JsonProperty(JSON_PROPERTY_SCORE)
  private Double score;

  public static final String JSON_PROPERTY_WIDTH = "width";
  @JsonProperty(JSON_PROPERTY_WIDTH)
  private Integer width;

  public static final String JSON_PROPERTY_HEIGHT = "height";
  @JsonProperty(JSON_PROPERTY_HEIGHT)
  private Integer height;

  public static final String JSON_PROPERTY_ORIENTATION = "orientation";
  @JsonProperty(JSON_PROPERTY_ORIENTATION)
  private Integer orientation;

  public Media id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @JsonProperty(value = "id")
  @ApiModelProperty(required = true, value = "")
  @NotNull 
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Media name(String name) {
    this.name = name;
    return this;
  }

  /**
   * File-Name of the Video
   * @return name
   **/
  @JsonProperty(value = "name")
  @ApiModelProperty(required = true, value = "File-Name of the Video")
  @NotNull 
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Media creationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
    return this;
  }

  /**
   * Date Image was taken
   * @return creationDate
   **/
  @JsonProperty(value = "creationDate")
  @ApiModelProperty(required = true, value = "Date Image was taken")
  @NotNull 
  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public Media image(Boolean image) {
    this.image = image;
    return this;
  }

  /**
   * Get image
   * @return image
   **/
  @JsonProperty(value = "image")
  @ApiModelProperty(required = true, value = "")
  @NotNull 
  public Boolean getImage() {
    return image;
  }

  public void setImage(Boolean image) {
    this.image = image;
  }

  public Media score(Double score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   **/
  @JsonProperty(value = "score")
  @ApiModelProperty(required = true, value = "")
  @NotNull 
  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public Media width(Integer width) {
    this.width = width;
    return this;
  }

  /**
   * Get width
   * @return width
   **/
  @JsonProperty(value = "width")
  @ApiModelProperty(required = true, value = "")
  @NotNull 
  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public Media height(Integer height) {
    this.height = height;
    return this;
  }

  /**
   * Get height
   * @return height
   **/
  @JsonProperty(value = "height")
  @ApiModelProperty(required = true, value = "")
  @NotNull 
  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public Media orientation(Integer orientation) {
    this.orientation = orientation;
    return this;
  }

  /**
   * Get orientation
   * @return orientation
   **/
  @JsonProperty(value = "orientation")
  @ApiModelProperty(required = true, value = "")
  @NotNull 
  public Integer getOrientation() {
    return orientation;
  }

  public void setOrientation(Integer orientation) {
    this.orientation = orientation;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Media media = (Media) o;
    return Objects.equals(this.id, media.id) &&
        Objects.equals(this.name, media.name) &&
        Objects.equals(this.creationDate, media.creationDate) &&
        Objects.equals(this.image, media.image) &&
        Objects.equals(this.score, media.score) &&
        Objects.equals(this.width, media.width) &&
        Objects.equals(this.height, media.height) &&
        Objects.equals(this.orientation, media.orientation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, creationDate, image, score, width, height, orientation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Media {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    image: ").append(toIndentedString(image)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    orientation: ").append(toIndentedString(orientation)).append("\n");
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
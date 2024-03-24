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
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.braun.digikam.backend.model.Keyword;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

/**
 * Image
 */
@JsonPropertyOrder({
  Image.JSON_PROPERTY_CREATOR,
  Image.JSON_PROPERTY_ORIENTATION,
  Image.JSON_PROPERTY_EXPOSURE_TIME,
  Image.JSON_PROPERTY_LATITUDE,
  Image.JSON_PROPERTY_FOCAL_LENGTH35,
  Image.JSON_PROPERTY_LENS,
  Image.JSON_PROPERTY_CREATION_DATE,
  Image.JSON_PROPERTY_APERTURE,
  Image.JSON_PROPERTY_WIDTH,
  Image.JSON_PROPERTY_MODEL,
  Image.JSON_PROPERTY_ID,
  Image.JSON_PROPERTY_MAKE,
  Image.JSON_PROPERTY_HEIGHT,
  Image.JSON_PROPERTY_FOCAL_LENGTH,
  Image.JSON_PROPERTY_LONGITUDE,
  Image.JSON_PROPERTY_ISO,
  Image.JSON_PROPERTY_RATING,
  Image.JSON_PROPERTY_KEYWORDS,
  Image.JSON_PROPERTY_TITLE,
  Image.JSON_PROPERTY_DESCRIPTION,
  Image.JSON_PROPERTY_RELATIVE_PATH,
  Image.JSON_PROPERTY_ROOT,
  Image.JSON_PROPERTY_NAME
})
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-03-20T12:51:17.342362153+01:00[Europe/Berlin]")
public class Image   {
  public static final String JSON_PROPERTY_CREATOR = "creator";
  @JsonProperty(JSON_PROPERTY_CREATOR)
  private String creator;

  /**
   * Gets or Sets orientation
   */
  public enum OrientationEnum {
    PORTRAIT("portrait"),
    
    LANDSCAPE("landscape");

    private String value;

    OrientationEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OrientationEnum fromValue(String value) {
      for (OrientationEnum b : OrientationEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_ORIENTATION = "orientation";
  @JsonProperty(JSON_PROPERTY_ORIENTATION)
  private OrientationEnum orientation;

  public static final String JSON_PROPERTY_EXPOSURE_TIME = "exposureTime";
  @JsonProperty(JSON_PROPERTY_EXPOSURE_TIME)
  private Double exposureTime;

  public static final String JSON_PROPERTY_LATITUDE = "latitude";
  @JsonProperty(JSON_PROPERTY_LATITUDE)
  private Double latitude;

  public static final String JSON_PROPERTY_FOCAL_LENGTH35 = "focalLength35";
  @JsonProperty(JSON_PROPERTY_FOCAL_LENGTH35)
  private Double focalLength35;

  public static final String JSON_PROPERTY_LENS = "lens";
  @JsonProperty(JSON_PROPERTY_LENS)
  private String lens;

  public static final String JSON_PROPERTY_CREATION_DATE = "creationDate";
  @JsonProperty(JSON_PROPERTY_CREATION_DATE)
  private LocalDateTime creationDate;

  public static final String JSON_PROPERTY_APERTURE = "aperture";
  @JsonProperty(JSON_PROPERTY_APERTURE)
  private Double aperture;

  public static final String JSON_PROPERTY_WIDTH = "width";
  @JsonProperty(JSON_PROPERTY_WIDTH)
  private Integer width;

  public static final String JSON_PROPERTY_MODEL = "model";
  @JsonProperty(JSON_PROPERTY_MODEL)
  private String model;

  public static final String JSON_PROPERTY_ID = "id";
  @JsonProperty(JSON_PROPERTY_ID)
  private Integer id;

  public static final String JSON_PROPERTY_MAKE = "make";
  @JsonProperty(JSON_PROPERTY_MAKE)
  private String make;

  public static final String JSON_PROPERTY_HEIGHT = "height";
  @JsonProperty(JSON_PROPERTY_HEIGHT)
  private Integer height;

  public static final String JSON_PROPERTY_FOCAL_LENGTH = "focalLength";
  @JsonProperty(JSON_PROPERTY_FOCAL_LENGTH)
  private Double focalLength;

  public static final String JSON_PROPERTY_LONGITUDE = "longitude";
  @JsonProperty(JSON_PROPERTY_LONGITUDE)
  private Double longitude;

  public static final String JSON_PROPERTY_ISO = "iso";
  @JsonProperty(JSON_PROPERTY_ISO)
  private Integer iso;

  public static final String JSON_PROPERTY_RATING = "rating";
  @JsonProperty(JSON_PROPERTY_RATING)
  private Integer rating;

  public static final String JSON_PROPERTY_KEYWORDS = "keywords";
  @JsonProperty(JSON_PROPERTY_KEYWORDS)
  private List<@Valid Keyword> keywords;

  public static final String JSON_PROPERTY_TITLE = "title";
  @JsonProperty(JSON_PROPERTY_TITLE)
  private String title;

  public static final String JSON_PROPERTY_DESCRIPTION = "description";
  @JsonProperty(JSON_PROPERTY_DESCRIPTION)
  private String description;

  public static final String JSON_PROPERTY_RELATIVE_PATH = "relativePath";
  @JsonProperty(JSON_PROPERTY_RELATIVE_PATH)
  private String relativePath;

  public static final String JSON_PROPERTY_ROOT = "root";
  @JsonProperty(JSON_PROPERTY_ROOT)
  private String root;

  public static final String JSON_PROPERTY_NAME = "name";
  @JsonProperty(JSON_PROPERTY_NAME)
  private String name;

  public Image creator(String creator) {
    this.creator = creator;
    return this;
  }

  /**
   * Name of the person, who has taken the picture
   * @return creator
   **/
  @JsonProperty(value = "creator")
  @ApiModelProperty(value = "Name of the person, who has taken the picture")
  
  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public Image orientation(OrientationEnum orientation) {
    this.orientation = orientation;
    return this;
  }

  /**
   * Get orientation
   * @return orientation
   **/
  @JsonProperty(value = "orientation")
  @ApiModelProperty(value = "")
  
  public OrientationEnum getOrientation() {
    return orientation;
  }

  public void setOrientation(OrientationEnum orientation) {
    this.orientation = orientation;
  }

  public Image exposureTime(Double exposureTime) {
    this.exposureTime = exposureTime;
    return this;
  }

  /**
   * Get exposureTime
   * @return exposureTime
   **/
  @JsonProperty(value = "exposureTime")
  @ApiModelProperty(value = "")
  
  public Double getExposureTime() {
    return exposureTime;
  }

  public void setExposureTime(Double exposureTime) {
    this.exposureTime = exposureTime;
  }

  public Image latitude(Double latitude) {
    this.latitude = latitude;
    return this;
  }

  /**
   * Get latitude
   * @return latitude
   **/
  @JsonProperty(value = "latitude")
  @ApiModelProperty(value = "")
  
  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Image focalLength35(Double focalLength35) {
    this.focalLength35 = focalLength35;
    return this;
  }

  /**
   * Get focalLength35
   * @return focalLength35
   **/
  @JsonProperty(value = "focalLength35")
  @ApiModelProperty(value = "")
  
  public Double getFocalLength35() {
    return focalLength35;
  }

  public void setFocalLength35(Double focalLength35) {
    this.focalLength35 = focalLength35;
  }

  public Image lens(String lens) {
    this.lens = lens;
    return this;
  }

  /**
   * Get lens
   * @return lens
   **/
  @JsonProperty(value = "lens")
  @ApiModelProperty(value = "")
  
  public String getLens() {
    return lens;
  }

  public void setLens(String lens) {
    this.lens = lens;
  }

  public Image creationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
    return this;
  }

  /**
   * Get creationDate
   * @return creationDate
   **/
  @JsonProperty(value = "creationDate")
  @ApiModelProperty(value = "")
  
  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public Image aperture(Double aperture) {
    this.aperture = aperture;
    return this;
  }

  /**
   * Get aperture
   * @return aperture
   **/
  @JsonProperty(value = "aperture")
  @ApiModelProperty(value = "")
  
  public Double getAperture() {
    return aperture;
  }

  public void setAperture(Double aperture) {
    this.aperture = aperture;
  }

  public Image width(Integer width) {
    this.width = width;
    return this;
  }

  /**
   * Get width
   * @return width
   **/
  @JsonProperty(value = "width")
  @ApiModelProperty(value = "")
  
  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public Image model(String model) {
    this.model = model;
    return this;
  }

  /**
   * Get model
   * @return model
   **/
  @JsonProperty(value = "model")
  @ApiModelProperty(value = "")
  
  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public Image id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * unique Id identifying an Image
   * @return id
   **/
  @JsonProperty(value = "id")
  @ApiModelProperty(value = "unique Id identifying an Image")
  
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Image make(String make) {
    this.make = make;
    return this;
  }

  /**
   * Get make
   * @return make
   **/
  @JsonProperty(value = "make")
  @ApiModelProperty(value = "")
  
  public String getMake() {
    return make;
  }

  public void setMake(String make) {
    this.make = make;
  }

  public Image height(Integer height) {
    this.height = height;
    return this;
  }

  /**
   * Get height
   * @return height
   **/
  @JsonProperty(value = "height")
  @ApiModelProperty(value = "")
  
  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public Image focalLength(Double focalLength) {
    this.focalLength = focalLength;
    return this;
  }

  /**
   * Get focalLength
   * @return focalLength
   **/
  @JsonProperty(value = "focalLength")
  @ApiModelProperty(value = "")
  
  public Double getFocalLength() {
    return focalLength;
  }

  public void setFocalLength(Double focalLength) {
    this.focalLength = focalLength;
  }

  public Image longitude(Double longitude) {
    this.longitude = longitude;
    return this;
  }

  /**
   * Get longitude
   * @return longitude
   **/
  @JsonProperty(value = "longitude")
  @ApiModelProperty(value = "")
  
  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Image iso(Integer iso) {
    this.iso = iso;
    return this;
  }

  /**
   * Get iso
   * @return iso
   **/
  @JsonProperty(value = "iso")
  @ApiModelProperty(value = "")
  
  public Integer getIso() {
    return iso;
  }

  public void setIso(Integer iso) {
    this.iso = iso;
  }

  public Image rating(Integer rating) {
    this.rating = rating;
    return this;
  }

  /**
   * Get rating
   * @return rating
   **/
  @JsonProperty(value = "rating")
  @ApiModelProperty(value = "")
  
  public Integer getRating() {
    return rating;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
  }

  public Image keywords(List<@Valid Keyword> keywords) {
    this.keywords = keywords;
    return this;
  }

  public Image addKeywordsItem(Keyword keywordsItem) {
    if (this.keywords == null) {
      this.keywords = new ArrayList<>();
    }
    this.keywords.add(keywordsItem);
    return this;
  }

  /**
   * Get keywords
   * @return keywords
   **/
  @JsonProperty(value = "keywords")
  @ApiModelProperty(value = "")
  @Valid 
  public List<@Valid Keyword> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<@Valid Keyword> keywords) {
    this.keywords = keywords;
  }

  public Image title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   **/
  @JsonProperty(value = "title")
  @ApiModelProperty(value = "")
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Image description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   **/
  @JsonProperty(value = "description")
  @ApiModelProperty(value = "")
  
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Image relativePath(String relativePath) {
    this.relativePath = relativePath;
    return this;
  }

  /**
   * Get relativePath
   * @return relativePath
   **/
  @JsonProperty(value = "relativePath")
  @ApiModelProperty(value = "")
  
  public String getRelativePath() {
    return relativePath;
  }

  public void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

  public Image root(String root) {
    this.root = root;
    return this;
  }

  /**
   * Get root
   * @return root
   **/
  @JsonProperty(value = "root")
  @ApiModelProperty(value = "")
  
  public String getRoot() {
    return root;
  }

  public void setRoot(String root) {
    this.root = root;
  }

  public Image name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   **/
  @JsonProperty(value = "name")
  @ApiModelProperty(value = "")
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Image image = (Image) o;
    return Objects.equals(this.creator, image.creator) &&
        Objects.equals(this.orientation, image.orientation) &&
        Objects.equals(this.exposureTime, image.exposureTime) &&
        Objects.equals(this.latitude, image.latitude) &&
        Objects.equals(this.focalLength35, image.focalLength35) &&
        Objects.equals(this.lens, image.lens) &&
        Objects.equals(this.creationDate, image.creationDate) &&
        Objects.equals(this.aperture, image.aperture) &&
        Objects.equals(this.width, image.width) &&
        Objects.equals(this.model, image.model) &&
        Objects.equals(this.id, image.id) &&
        Objects.equals(this.make, image.make) &&
        Objects.equals(this.height, image.height) &&
        Objects.equals(this.focalLength, image.focalLength) &&
        Objects.equals(this.longitude, image.longitude) &&
        Objects.equals(this.iso, image.iso) &&
        Objects.equals(this.rating, image.rating) &&
        Objects.equals(this.keywords, image.keywords) &&
        Objects.equals(this.title, image.title) &&
        Objects.equals(this.description, image.description) &&
        Objects.equals(this.relativePath, image.relativePath) &&
        Objects.equals(this.root, image.root) &&
        Objects.equals(this.name, image.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(creator, orientation, exposureTime, latitude, focalLength35, lens, creationDate, aperture, width, model, id, make, height, focalLength, longitude, iso, rating, keywords, title, description, relativePath, root, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Image {\n");
    
    sb.append("    creator: ").append(toIndentedString(creator)).append("\n");
    sb.append("    orientation: ").append(toIndentedString(orientation)).append("\n");
    sb.append("    exposureTime: ").append(toIndentedString(exposureTime)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    focalLength35: ").append(toIndentedString(focalLength35)).append("\n");
    sb.append("    lens: ").append(toIndentedString(lens)).append("\n");
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    aperture: ").append(toIndentedString(aperture)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    model: ").append(toIndentedString(model)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    make: ").append(toIndentedString(make)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    focalLength: ").append(toIndentedString(focalLength)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    iso: ").append(toIndentedString(iso)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
    sb.append("    keywords: ").append(toIndentedString(keywords)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    relativePath: ").append(toIndentedString(relativePath)).append("\n");
    sb.append("    root: ").append(toIndentedString(root)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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


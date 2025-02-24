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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;

/**
 * CameraLens
 */
@JsonPropertyOrder({
  CameraLens.JSON_PROPERTY_CAMERA,
  CameraLens.JSON_PROPERTY_LENS
})
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-02-24T10:26:30.300369408+01:00[Europe/Berlin]", comments = "Generator version: 7.7.0")
public class CameraLens   {
  public static final String JSON_PROPERTY_CAMERA = "camera";
  @JsonProperty(JSON_PROPERTY_CAMERA)
  private String camera;

  public static final String JSON_PROPERTY_LENS = "lens";
  @JsonProperty(JSON_PROPERTY_LENS)
  private String lens;

  public CameraLens camera(String camera) {
    this.camera = camera;
    return this;
  }

  /**
   * Get camera
   * @return camera
   **/
  @JsonProperty(value = "camera")
  @ApiModelProperty(required = true, value = "")
  @NotNull 
  public String getCamera() {
    return camera;
  }

  public void setCamera(String camera) {
    this.camera = camera;
  }

  public CameraLens lens(String lens) {
    this.lens = lens;
    return this;
  }

  /**
   * Get lens
   * @return lens
   **/
  @JsonProperty(value = "lens")
  @ApiModelProperty(required = true, value = "")
  @NotNull 
  public String getLens() {
    return lens;
  }

  public void setLens(String lens) {
    this.lens = lens;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CameraLens cameraLens = (CameraLens) o;
    return Objects.equals(this.camera, cameraLens.camera) &&
        Objects.equals(this.lens, cameraLens.lens);
  }

  @Override
  public int hashCode() {
    return Objects.hash(camera, lens);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CameraLens {\n");
    
    sb.append("    camera: ").append(toIndentedString(camera)).append("\n");
    sb.append("    lens: ").append(toIndentedString(lens)).append("\n");
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


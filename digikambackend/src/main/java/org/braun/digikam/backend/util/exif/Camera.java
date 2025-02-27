package org.braun.digikam.backend.util.exif;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 *
 * @author mbraun
 */
@JsonPropertyOrder({
    Camera.PROP_MAKE, Camera.PROP_MODEL, Camera.PROP_CROP
})
public class Camera {

    public static final String PROP_MAKE = "make";
    public static final String PROP_MODEL = "model";
    public static final String PROP_CROP = "crop";

    @JsonProperty(value = Camera.PROP_MAKE)
    private String make;
    @JsonProperty(value = Camera.PROP_MODEL)
    private String model;
    @JsonProperty(value = Camera.PROP_CROP)
    private float crop;

    @JsonProperty(value = Camera.PROP_MAKE)
    @ApiModelProperty(required = true, value = "")
    @NotNull
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public Camera make(String value) {
        make = value;
        return this;
    }

    @JsonProperty(value = Camera.PROP_MODEL)
    @ApiModelProperty(required = true, value = "")
    @NotNull
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Camera model(String value) {
        model = value;
        return this;
    }

    @JsonProperty(value = Camera.PROP_CROP)
    @ApiModelProperty(required = true, value = "")
    @NotNull
    public float getCrop() {
        return crop;
    }

    public void setCrop(float crop) {
        this.crop = crop;
    }

    public Camera crop(float value) {
        crop = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Camera cameras = (Camera) o;
        return Objects.equals(this.make, cameras.make)
                && Objects.equals(this.model, cameras.model)
                && Objects.equals(this.crop, cameras.crop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(make, model, crop);
    }

}

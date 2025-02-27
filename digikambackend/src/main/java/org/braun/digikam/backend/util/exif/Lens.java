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
    Lens.PROP_LENS, Lens.PROP_FOCAL_LENGTH
})
public class Lens {

    public static final String PROP_LENS = "lens";
    public static final String PROP_FOCAL_LENGTH = "focalLength";

    @JsonProperty(value = Lens.PROP_LENS)
    private String lens;

    @JsonProperty(value = Lens.PROP_FOCAL_LENGTH)
    private double focalLength;

    @JsonProperty(value = Lens.PROP_LENS)
    @ApiModelProperty(required = true, value = "")
    @NotNull
    public String getLens() {
        return lens;
    }

    public void setLens(String lens) {
        this.lens = lens;
    }

    public Lens lens(String value) {
        lens = value;
        return this;
    }

    @JsonProperty(value = Lens.PROP_FOCAL_LENGTH)
    @ApiModelProperty(required = true, value = "")
    @NotNull
    public double getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(double focalLength) {
        this.focalLength = focalLength;
    }

    public Lens focalLength(double value) {
        focalLength = value;
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
        Lens lenses = (Lens) o;
        return Objects.equals(this.lens, lenses.lens)
                && Objects.equals(this.focalLength, lenses.focalLength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lens, focalLength);
    }

    @Override
    public String toString() {
        return "Lens{" + "lens=" + lens + ", focalLength=" + focalLength + '}';
    }

}

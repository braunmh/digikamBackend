package org.braun.digikam.backend.util.exif;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author mbraun
 */
@JsonPropertyOrder({
    Cameras.PROP_CAMERAS
})
public class Cameras {

    public static final String PROP_CAMERAS = "cameras";

    @JsonProperty(value = Cameras.PROP_CAMERAS)
    private List<Camera> cameras;

    @JsonProperty(value = Cameras.PROP_CAMERAS)
    @ApiModelProperty(value = "")
    @Valid
    public List<@Valid Camera> getCameras() {
        return cameras;
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }

    public Cameras cameras(List<@Valid Camera> cameras) {
        this.cameras = cameras;
        return this;
    }

    public Cameras addCamerasItem(Camera camera) {
        if (cameras == null) {
            cameras = new ArrayList<>();
        }
        cameras.add(camera);
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
        Cameras oCameras = (Cameras) o;
        return Objects.equals(this.cameras, oCameras.cameras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cameras);
    }

    @Override
    public String toString() {
        return "Cameras{" + "cameras=" + cameras + '}';
    }

}

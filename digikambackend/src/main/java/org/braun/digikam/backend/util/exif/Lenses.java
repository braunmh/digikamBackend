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
    Lenses.PROP_LENSES
})
public class Lenses {

    public static final String PROP_LENSES = "lenses";

    @JsonProperty(value = Lenses.PROP_LENSES)
    private List<Lens> lenses;

    @JsonProperty(value = Lenses.PROP_LENSES)
    @ApiModelProperty(value = "")
    @Valid
    public List<@Valid Lens> getLenses() {
        return lenses;
    }

    public void setLenses(List<Lens> lenses) {
        this.lenses = lenses;
    }

    public Lenses lenses(List<@Valid Lens> lenses) {
        this.lenses = lenses;
        return this;
    }

    public Lenses addLensesItem(Lens lens) {
        if (lenses == null) {
            lenses = new ArrayList<>();
        }
        lenses.add(lens);
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
        Lenses oLenses = (Lenses) o;
        return Objects.equals(this.lenses, oLenses.lenses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lenses);
    }

    @Override
    public String toString() {
        return "Lenses{" + "lenses=" + lenses + '}';
    }

}

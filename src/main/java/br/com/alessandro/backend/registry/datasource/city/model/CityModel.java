package br.com.alessandro.backend.registry.datasource.city.model;

import br.com.alessandro.backend.registry.datasource.state.model.StateModel;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnTransformer;

import java.util.Objects;

@Entity
@Table(name = "city", schema = "public")
public class CityModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", length = 120)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uf", referencedColumnName = "id")
    private StateModel state;

    @Column(name = "ibge")
    private Integer ibge;

    @Column(name = "lat_lon", columnDefinition = "point")
    @ColumnTransformer(read = "lat_lon::text", write = "?::point")
    private String latLon;

    @Column(name = "cod_tom", columnDefinition = "smallint default 0")
    private Short codTom = 0;

    public CityModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public StateModel getState() { return state; }
    public void setState(StateModel state) { this.state = state; }

    public Integer getIbge() { return ibge; }
    public void setIbge(Integer ibge) { this.ibge = ibge; }

    public String getLatLon() { return latLon; }
    public void setLatLon(String latLon) { this.latLon = latLon; }

    public Short getCodTom() { return codTom; }
    public void setCodTom(Short codTom) { this.codTom = codTom; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityModel that = (CityModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

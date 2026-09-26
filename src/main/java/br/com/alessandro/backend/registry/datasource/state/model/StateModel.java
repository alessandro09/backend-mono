package br.com.alessandro.backend.registry.datasource.state.model;

import br.com.alessandro.backend.registry.datasource.country.model.CountryModel;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "state", schema = "public")
public class StateModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 60)
    private String name;

    @Column(name = "acronym", length = 2)
    private String acronym;

    @Column(name = "ibge")
    private Integer ibge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country", referencedColumnName = "id")
    private CountryModel country;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ddd", columnDefinition = "json")
    private List<Integer> ddd;

    public StateModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAcronym() { return acronym; }
    public void setAcronym(String acronym) { this.acronym = acronym; }

    public Integer getIbge() { return ibge; }
    public void setIbge(Integer ibge) { this.ibge = ibge; }

    public CountryModel getCountry() { return country; }
    public void setCountry(CountryModel country) { this.country = country; }

    public List<Integer> getDdd() { return ddd; }
    public void setDdd(List<Integer> ddd) { this.ddd = ddd; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateModel that = (StateModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

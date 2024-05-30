package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Country {
  String name;
  String continent;
  int taxFees;
  List<Country> adjacentCountries;

  public Country(String name, String continent, int taxFees) {
    this.name = name;
    this.continent = continent;
    this.taxFees = taxFees;
    this.adjacentCountries = new ArrayList<>();
  }

  public List<Country> getAdjacentCountries() {
    return adjacentCountries;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Country country = (Country) obj;
    return Objects.equals(name, country.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}

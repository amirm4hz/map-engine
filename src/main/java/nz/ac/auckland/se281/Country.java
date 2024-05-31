package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;

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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((continent == null) ? 0 : continent.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Country other = (Country) obj;
    if (name == null) {
      if (other.name != null) return false;
    } else if (!name.equals(other.name)) return false;
    if (continent == null) {
      if (other.continent != null) return false;
    } else if (!continent.equals(other.continent)) return false;
    return true;
  }
}

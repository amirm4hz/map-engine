package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;

public class Country {
  String name;
  String continent;
  double taxFees;
  List<Country> adjacentCountries;

  public Country(String name, String continent, double taxFees) {
    this.name = name;
    this.continent = continent;
    this.taxFees = taxFees;
    this.adjacentCountries = new ArrayList<>();
  }

  public List<Country> getAdjacentCountries() {
    return adjacentCountries;
  }
}

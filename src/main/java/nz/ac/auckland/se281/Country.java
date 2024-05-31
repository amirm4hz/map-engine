package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;

/** Represents a country with a name, continent, tax fees, and adjacent countries. */
public class Country {
  private String name;
  private String continent;
  private int taxFees;
  private List<Country> adjacentCountries;

  /**
   * Constructs a new Country with the given name, continent, and tax fees.
   *
   * @param name the name of the country
   * @param continent the continent of the country
   * @param taxFees the tax fees of the country
   */
  public Country(String name, String continent, int taxFees) {
    this.name = name;
    this.continent = continent;
    this.taxFees = taxFees;
    this.adjacentCountries = new ArrayList<>();
  }

  public String getContinent() {
    return continent;
  }

  public int getTaxFees() {
    return taxFees;
  }

  public String getName() {
    return name;
  }

  /**
   * Returns the list of countries adjacent to this country.
   *
   * @return the list of adjacent countries
   */
  public List<Country> getAdjacentCountries() {
    return adjacentCountries;
  }

  /**
   * Returns a hash code value for the country.
   *
   * @return a hash code value for this object
   */
  @Override
  public int hashCode() {
    // The hash code value is calculated based on the name and continent of the country
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((continent == null) ? 0 : continent.hashCode());
    return result;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the reference object with which to compare
   * @return true if this object is the same as the obj argument; false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    // Check if the object is the same as this object
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Country other = (Country) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (continent == null) {
      if (other.continent != null) {
        return false;
      }
    } else if (!continent.equals(other.continent)) {
      return false;
    }
    return true;
  }
}

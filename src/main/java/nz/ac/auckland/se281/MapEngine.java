package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** This class is the main entry point. */
public class MapEngine {

  private Map<String, Country> countriesMap;

  public MapEngine() {
    // add other code here if you want
    loadMap(); // keep this mehtod invocation
  }

  /** invoked one time only when constracting the MapEngine class. */
  private void loadMap() {
    List<String> countries = Utils.readCountries();
    List<String> adjacencies = Utils.readAdjacencies();

    Map<String, List<String>> graph = new HashMap<>(); // Declare the graph variable

    countriesMap = new HashMap<>();

    for (String countryInfo : countries) {
      String[] info = countryInfo.split(",");
      String countryName = info[0];
      String continent = info[1];
      double taxFees = Double.parseDouble(info[2]);
      Country country = new Country(countryName, continent, taxFees);
      countriesMap.put(countryName, country);
      graph.put(countryName, new ArrayList<>());
    }

    for (String adjacency : adjacencies) {
      String[] adjacencyCountries = adjacency.split(",");
      graph.get(adjacencyCountries[0]).add(adjacencyCountries[1]);
      graph.get(adjacencyCountries[1]).add(adjacencyCountries[0]);
    }
  }

  /** this method is invoked when the user run the command info-country. */
  public void showInfoCountry() {
    String country = "";

    MessageCli.INSERT_COUNTRY.printMessage();
    country = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine().toLowerCase());

    if (countriesMap.containsKey(country)) {
      Country countryInfo = countriesMap.get(country);
      MessageCli.COUNTRY_INFO.printMessage(
          countryInfo.name, countryInfo.continent, String.valueOf(countryInfo.taxFees));
    }
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {}
}

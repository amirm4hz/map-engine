package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

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

    // Link the adjacent countries to each Country object
    for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
      Country country = countriesMap.get(entry.getKey());
      for (String adjacentCountryName : entry.getValue()) {
        Country adjacentCountry = countriesMap.get(adjacentCountryName);
        if (adjacentCountry != null) {
          country.getAdjacentCountries().add(adjacentCountry);
        }
      }
    }
  }

  /** this method is invoked when the user run the command info-country. */
  public void showInfoCountry() {
    String country = askForValidCountry(MessageCli.INSERT_COUNTRY.getMessage());
    Country countryInfo = countriesMap.get(country);
    MessageCli.COUNTRY_INFO.printMessage(
        countryInfo.name, countryInfo.continent, String.valueOf(countryInfo.taxFees));
  }

  public String askForValidCountry(String customMessage) {
    String country = "";
    boolean validCountry = false;

    while (!validCountry) {
      try {
        System.out.println(customMessage);
        country = countryAsker();

        if (!countriesMap.containsKey(country)) {
          throw new CountryNotFoundException(MessageCli.INVALID_COUNTRY.getMessage(country));
        }

        validCountry = true;
      } catch (CountryNotFoundException e) {
        System.out.println(e.getMessage());
      }
    }

    return country;
  }

  public String countryAsker() {
    String country = "";
    country = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
    return country;
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {
    String source = askForValidCountry(MessageCli.INSERT_SOURCE.getMessage());
    Country sourceInfo = countriesMap.get(source);
    String destination = askForValidCountry(MessageCli.INSERT_DESTINATION.getMessage());
    Country destinationInfo = countriesMap.get(destination);

    if (source.equals(destination)) {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
      return;
    }

    List<String> route = calculateRoute(sourceInfo, destinationInfo);
    List<String> continents =
        route.stream()
            .map(country -> countriesMap.get(country).continent)
            .distinct()
            .collect(Collectors.toList());
    int totalTaxes = calculateTaxes(route);

    String routeStr = "[" + String.join(", ", route) + "]";
    MessageCli.ROUTE_INFO.printMessage(routeStr);
    String continentsStr = "[" + String.join(", ", continents) + "]";
    MessageCli.CONTINENT_INFO.printMessage(continentsStr);
    MessageCli.TAX_INFO.printMessage(Integer.toString(totalTaxes));
  }

  private List<String> calculateRoute(Country source, Country destination) {
    Map<Country, Country> predecessorMap = new HashMap<>();
    Queue<Country> queue = new LinkedList<>();
    Set<Country> visited = new HashSet<>();

    queue.add(source);
    visited.add(source);

    while (!queue.isEmpty()) {
      Country current = queue.poll();

      if (current.equals(destination)) {
        break;
      }

      for (Country neighbour : current.adjacentCountries) {
        if (!visited.contains(neighbour)) {
          queue.add(neighbour);
          visited.add(neighbour);
          predecessorMap.put(neighbour, current);
        }
      }
    }
    List<String> route = new ArrayList<>();
    for (Country country = destination; country != null; country = predecessorMap.get(country)) {
      route.add(country.name);
    }
    Collections.reverse(route);
    return route;
  }

  public int calculateTaxes(List<String> route) {
    // Sum up the tax fees for each country in the route, except for the starting country.
    int totalTaxes = 0;
    for (int i = 1; i < route.size(); i++) { // Start from 1 to skip the starting country
      String countryName = route.get(i);
      Country country = countriesMap.get(countryName);
      totalTaxes += country.taxFees;
    }
    return totalTaxes;
  }
}

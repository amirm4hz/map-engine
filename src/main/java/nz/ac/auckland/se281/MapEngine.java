package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is the main entry point for the map engine. It loads the map data and provides methods
 * to interact with it.
 */
public class MapEngine {

  private Map<String, Country> countriesMap;

  /** Constructs a new MapEngine and loads the map data. */
  public MapEngine() {
    // add other code here if you want
    loadMap(); // keep this mehtod invocation
  }

  /**
   * Loads the map data from the Utils class. This method is invoked one time only when constructing
   * the MapEngine class.
   */
  private void loadMap() {
    List<String> countries = Utils.readCountries();
    List<String> adjacencies = Utils.readAdjacencies();

    Map<String, List<String>> graph = new LinkedHashMap<>(); // Declare the graph variable

    countriesMap = new LinkedHashMap<>();
    // Create a map to store the country name and the country object
    for (String countryInfo : countries) {
      String[] info = countryInfo.split(",");
      String countryName = info[0];
      String continent = info[1];
      int taxFees = Integer.parseInt(info[2]); // Convert the string value to an integer
      Country country = new Country(countryName, continent, taxFees);
      countriesMap.put(countryName, country);
      graph.put(countryName, new ArrayList<>());
    }

    // Create the graph by adding the adjacent countries to each country
    for (String adjacency : adjacencies) {
      String[] adjacencyCountries = adjacency.split(",");
      String country = adjacencyCountries[0];
      for (int i = 1; i < adjacencyCountries.length; i++) {
        graph.get(country).add(adjacencyCountries[i]);
      }
    }

    // Add the adjacent countries to each country object
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

  /**
   * Displays information about a country. This method is invoked when the user runs the command
   * info-country.
   */
  public void showInfoCountry() {
    String country = askForValidCountry(MessageCli.INSERT_COUNTRY.getMessage());
    Country countryInfo = countriesMap.get(country);
    MessageCli.COUNTRY_INFO.printMessage(
        countryInfo.getName(),
        countryInfo.getContinent(),
        String.valueOf(countryInfo.getTaxFees()));
  }

  /**
   * Asks the user for a valid country name.
   *
   * @param customMessage the message to display to the user
   * @return the name of the valid country
   */
  public String askForValidCountry(String customMessage) {
    String country = "";
    boolean validCountry = false;

    // Keep asking the user for a country name until a valid country is entered
    while (!validCountry) {
      try {
        System.out.println(customMessage);
        country = askForCountryName();

        // Check if the country is valid
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

  /**
   * Asks the user for a country name.
   *
   * @return the name of the country
   */
  public String askForCountryName() {
    // Capitalize the first letter of each word in the country name
    String country = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
    return country;
  }

  /**
   * Displays the route between two countries. This method is invoked when the user runs the command
   * route.
   */
  public void showRoute() {
    // Ask the user for the source and destination countries
    String source = askForValidCountry(MessageCli.INSERT_SOURCE.getMessage());
    Country sourceInfo = countriesMap.get(source);
    String destination = askForValidCountry(MessageCli.INSERT_DESTINATION.getMessage());
    Country destinationInfo = countriesMap.get(destination);

    if (source.equals(destination)) {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
      return;
    }

    // Calculate the route, continents, and total taxes
    List<String> route = calculateRoute(sourceInfo, destinationInfo);
    List<String> continents =
        route.stream()
            .map(country -> countriesMap.get(country).getContinent())
            .distinct()
            .collect(Collectors.toList());
    int totalTaxes = calculateTaxes(route);

    // Display the route information
    String routeStr = "[" + String.join(", ", route) + "]";
    MessageCli.ROUTE_INFO.printMessage(routeStr);
    String continentsStr = "[" + String.join(", ", continents) + "]";
    MessageCli.CONTINENT_INFO.printMessage(continentsStr);
    MessageCli.TAX_INFO.printMessage(Integer.toString(totalTaxes));
  }

  /**
   * Calculates the route between two countries.
   *
   * @param source the source country
   * @param destination the destination country
   * @return the list of country names in the route
   */
  private List<String> calculateRoute(Country source, Country destination) {
    Queue<Country> queue = new LinkedList<>();
    Map<Country, Country> predecessors = new LinkedHashMap<>();
    Set<Country> visited = new HashSet<>();

    // Perform a breadth-first search to find the route between the source and destination countries
    queue.add(source);
    visited.add(source);

    while (!queue.isEmpty()) {
      Country current = queue.poll();
      if (current.equals(destination)) {
        break;
      }

      // Add the adjacent countries to the queue
      List<Country> neighbors = current.getAdjacentCountries();
      for (Country neighbor : neighbors) {
        if (!visited.contains(neighbor)) {
          visited.add(neighbor);
          predecessors.put(neighbor, current);
          queue.add(neighbor);
        }
      }
    }

    // Construct the route by backtracking from the destination to the source
    List<String> path = new LinkedList<>();
    Country step = destination;
    if (predecessors.containsKey(step) || step.equals(source)) {
      while (step != null) {
        path.add(0, step.getName());
        step = predecessors.get(step);
      }
    }
    return path;
  }

  /**
   * Calculates the total tax fees for a route.
   *
   * @param route the list of country names in the route
   * @return the total tax fees
   */
  public int calculateTaxes(List<String> route) {
    // Sum up the tax fees for each country in the route, except for the starting country.
    int totalTaxes = 0;
    for (int i = 1; i < route.size(); i++) { // Start from 1 to skip the starting country
      String countryName = route.get(i);
      Country country = countriesMap.get(countryName);
      totalTaxes += country.getTaxFees(); // Add the tax fees of the country
    }
    return totalTaxes;
  }
}

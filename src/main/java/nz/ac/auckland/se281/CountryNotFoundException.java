package nz.ac.auckland.se281;

/** Exception thrown when a country is not found. */
public class CountryNotFoundException extends Exception {

  /**
   * Constructs a new CountryNotFoundException with the specified detail message.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     Throwable.getMessage() method.
   */
  public CountryNotFoundException(String message) {
    super(message);
  }
}

import java.util.ArrayList;

public class TaxiService {

  ArrayList<Taxi> taxis;

  public TaxiService(int numTaxis) {
    taxis = new ArrayList<Taxi>();

    Taxi tempTaxi;
    for (int i = 1; i <= numTaxis; i++) {
      tempTaxi = new Taxi("Taxi "+i);
      taxis.add(tempTaxi);
      Main.simulation.addTaxi(tempTaxi);
    }
  }


  public void addTaxis(int numTaxis) {
    Taxi tempTaxi;
    for (int i = 1; i <= numTaxis; i++) {
      tempTaxi = new Taxi("Taxi "+i);
      taxis.add(tempTaxi);
      Main.simulation.addTaxi(tempTaxi);
    }
  }


  public void removeTaxi(Taxi taxi) {
    taxis.remove(taxi);
    Main.simulation.removeMapItem(taxi);
  }

  /**
   * Call a taxi for a person. Person is associated with the taxi.
   * @param p
   * @return The closest taxi object if there is one free. Null if they're all busy.
   */
  public Taxi callTaxi(Person p) {
    if (!isTaxiFree()) {
      return null;
    }

    ArrayList<Taxi> freeTaxis = new ArrayList<Taxi>();
    for(int i = 0; i < taxis.size(); i++)
      if(taxis.get(i).isIdle()) freeTaxis.add(taxis.get(i));

    Taxi closestTaxi = freeTaxis.get(0);
    for (int i = 1; i < freeTaxis.size(); i++) {
      if (p.getLocation().distance(freeTaxis.get(i).getLocation()) < closestTaxi.getLocation().distance(p.getLocation())) {
        closestTaxi = freeTaxis.get(i);
      }
    }

    closestTaxi.collectPerson(p);
    return closestTaxi;
  }

  /**
   * Find out if there's a taxi who's state is idle.
   * @return True if there is. False otherwise.
   */
  public boolean isTaxiFree() {
    for (int i = 0; i < taxis.size(); i++) {
      if (taxis.get(i).isIdle()) {
        return true;
      }
    }
    return false;
  }


}

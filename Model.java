/**
 * CS349 Winter 2014
 * Bingcheng Zhu
 * University of Waterloo
 */
import java.util.ArrayList;
import java.util.Vector;
import java.util.Timer;
import java.util.TimerTask;
//import javax.swing.*;



/*
 * Class the contains a list of fruit to display.
 * Follows MVC pattern, with methods to add observers,
 * and notify them when the fruit list changes.
 */
public class Model {
  // Observer list
  private Vector<ModelListener> views = new Vector();
  
  
  // Fruit that we want to display
  private ArrayList<Fruit> shapes = new ArrayList();
  
  // make a timer
  private Timer timer = new Timer(); 
  
  // make a timer task
  private fruit_task Ftask = new fruit_task();

  private int sumOFGame = 0;
  // Constructor
  Model() {
    shapes.clear();
    timer.scheduleAtFixedRate(Ftask, 0, (long) (1000.0 / 60.0));
  }

  private class fruit_task extends TimerTask {
	  private int T = 0;
	  public int get_T(){
		  return T;
	  }
	  public void run() {
		  T++; 
		  notifyObservers();
	  }
  }
  
  // MVC methods
  // These likely don't need to change, they're just an implementation of the
  // basic MVC methods to bind view and model together.
  public void addObserver(ModelListener view) {
    views.add(view);
  }

  public void notifyObservers() {
    for (ModelListener v : views) {
      v.update();
    }
  }

  // Model methods
  // You may need to add more methods here, depending on required functionality.
  // For instance, this sample makes to effort to discard fruit from the list.
  public void add(Fruit s) {
    shapes.add(s);
    notifyObservers();
  }
  public void clean(Fruit s) {
	  shapes.remove(s);
	  notifyObservers();
  }
  
  public void reset() {
	  
  }
  

  
  /*
   *  get the current Timer.
   */
  public int get_time() {
	  return Ftask.get_T();
  }
  
  /*
   * 
   */
  public void terminate() {
	  
	  Ftask.cancel();
  }

  public ArrayList<Fruit> getShapes() {
      return (ArrayList<Fruit>)shapes.clone();
  }
  
}

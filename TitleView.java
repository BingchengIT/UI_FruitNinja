/**
 * CS349 Winter 2014
 * Bingcheng Zhu
 * University of Waterloo
 */
import javax.swing.*;
import java.awt.*;

/*
 * View to display the Title, and Score
 * Score currently just increments every time we get an update
 * from the model (i.e. a new fruit is added).
 */
public class TitleView extends JPanel implements ModelListener {
  private Model model;
  private JLabel title, score;
  private float count = 0;
 // private final long start = System.currentTimeMillis();

  // Constructor requires model reference
  TitleView (Model model) {
    // register with model so that we get updates
    this.model = model;
    this.model.addObserver(this);

    // draw something
    setBorder(BorderFactory.createLineBorder(Color.black));
    setBackground(Color.YELLOW);
    // You may want a better name for this game!
    title = new JLabel(" Super Fruit Ninja !!!");
    score = new JLabel();

    // use border layout so that we can position labels on the left and right
    this.setLayout(new BorderLayout());
    this.add(title, BorderLayout.WEST);
    this.add(score, BorderLayout.EAST);
  }

  // Panel size
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(500,35);
  }

  // Update from model
  // This is ONLY really useful for testing that the view notifications work
  // You likely want something more meaningful here.
  @Override
  public void update() {
    //count = (this.model.get_time() / (1000 / 60));
	
    //paint(getGraphics());
	  
  }

  // Paint method
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
//    long now = System.currentTimeMillis(); 
//	  count = (now - start) / 1000;
//    score.setText("Count: " + count + "  ");
  }
}

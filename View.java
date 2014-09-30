/**
 * CS349 Winter 2014
 * Bingcheng Zhu
 * University of Waterloo
 */
import javax.swing.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


/*
 * View of the main play area.
 * Displays pieces of fruit, and allows players to slice them.
 */
public class View extends JPanel implements ModelListener {
    private Model model;
    private final MouseDrag drag;
    private final long start = System.currentTimeMillis();
    private JLabel Time_min, Total_score, Game_Over;
    private int sec = 0;
    private int min = 0;
    private int Score = 0;
    private int game_limit = 0;

    // Constructor
    View (Model m) {
        model = m;
        model.addObserver(this);

        setBackground(Color.WHITE);
        	
        // add a couple of fruit instances for test purposes
        // in a real game, you want to spawn fruit in random locations from the bottom of the screen
        // we use ellipse2D for simple shapes, you might consider something more complex
        Fruit f1 = new Fruit(new Area(new Ellipse2D.Double(50, 600, 50, 50)));
        f1.setFillColor(Color.GRAY);
        model.add(f1);

        Fruit f2 = new Fruit(new Area(new Ellipse2D.Double(200, 800, 50, 50)));
        f2.setFillColor(Color.BLACK);
        model.add(f2);
        
        Fruit f3 = new Fruit(new Area(new Ellipse2D.Double(300, 1000, 50, 50)));
        f3.setFillColor(Color.RED);
        model.add(f3);
        
        Fruit f4 = new Fruit(new Area(new Ellipse2D.Double(400, 1200, 50, 50)));
        f4.setFillColor(Color.orange);
        model.add(f4);
        
        
        // drag represents the last drag performed, which we will need to calculate the angle of the slice
        drag = new MouseDrag();
        // add mouse listener
        addMouseListener(mouseListener);
        //addActionListener(actionListener);
        
        Time_min = new JLabel();
        Total_score = new JLabel();
        Game_Over = new JLabel();
        this.setLayout(new BorderLayout());
        this.add(Time_min, BorderLayout.NORTH);
        this.add(Total_score, BorderLayout.SOUTH);
        this.add(Game_Over, BorderLayout.WEST);
}

    // Update fired from model
    @Override
    public void update() {
    	long now = System.currentTimeMillis(); 
   	  	sec = (int) ((now - start) / 1000) % 60;
   	  	min = (int)	((now - start) / 1000) / 60;
//   	  	for (Fruit s: model.getShapes()) {
//   	  		//System.out.println(game_limit);
//   	  		game_limit = game_limit + s.game_limit();
//   	  	}
//   	  	System.out.println(game_limit);
//   	  	if (!model.Sum())
//   	  		model.terminate();
   	  //	model.Sum();
        this.repaint();
    }
    
    // Panel size
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500,400);
    }

    // Paint this panel
    @Override
    public void paintComponent(Graphics g) {
    	
    	Time_min.setText("elapsed time" + " : "+ min + " : " + sec + "  ");
    	Total_score.setText("Total Score: " + Score);
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // draw all pieces of fruit
        // note that fruit is responsible for figuring out where and how to draw itself
        for (Fruit s : model.getShapes()) {
        	s.translate(s.H_Velocity(), s.Velocity());
        	if (s.Fruit_shouldbeRemoved()) {
        		model.clean(s);
        		game_limit ++;
        		if (game_limit == 5) { 
        			Game_Over.setText("GAME OVER !!!");
        			model.terminate();
        		}
        		Random r = new Random();
            	int Low = 50;
            	int High = 450;
            	int ver_low = 600;
            	int ver_high = 1000;
            	int R1 = r.nextInt(High-Low) + Low;
            	int R2 = r.nextInt(ver_high-ver_low) + ver_low;
            	Fruit New_fruit = new Fruit(new Area(new Ellipse2D.Double(R1, R2, 50, 50)));
            	Random randomGenerator = new Random();
            	int red = randomGenerator.nextInt(255);
            	int green = randomGenerator.nextInt(255);
            	int blue = randomGenerator.nextInt(255);
            	Color randomColour = new Color(red,green,blue);
            	New_fruit.setFillColor(randomColour);
            	model.add(New_fruit);
        	}
        	//System.out.println(s.IsSliced_IsDirectory_down());   // 
        	if (s.IsSliced_IsDirectory_down() && s.Getdistance_between_topbound() > 300) {
        		model.clean(s);
        	}
        	
        	g2.transform(s.getTransform());
            s.draw(g2);
            AffineTransform get_back = s.getTransform();
            try {
				get_back.invert();
				g2.transform(get_back);
			} catch (NoninvertibleTransformException e) {
				e.printStackTrace();
			}
        }
    }
    


    
    private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {	
			
			// TODO Auto-generated method stub
			
		}
    	
    };
    // Mouse handler
    // This does most of the work: capturing mouse movement, and determining if we intersect a shape
    // Fruit is responsible for determining if it's been sliced and drawing itself, but we still
    // need to figure out what fruit we've intersected.
    private MouseAdapter mouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
//            System.out.print("press_Point ");
//            System.out.print(e.getX());
//            System.out.print(" ");
//            System.out.println(e.getY());
            drag.start(e.getPoint());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            drag.stop(e.getPoint());

            // you could do something like this to draw a line for testing
            // not a perfect implementation, but works for 99% of the angles drawn
            
             //int[] x = { (int) drag.getStart().getX(), (int) drag.getEnd().getX(), (int) drag.getEnd().getX(), (int) drag.getStart().getX()};
             //int[] y = { (int) drag.getStart().getY()-1, (int) drag.getEnd().getY()-1, (int) drag.getEnd().getY()+1, (int) drag.getStart().getY()+1};
             //model.add(new Fruit(new Area(new Polygon(x, y, x.length))));
            	// find intersected shapes
            for (Fruit s : model.getShapes()) {
                if (s.intersects(drag.getStart(), drag.getEnd()) && !s.IsSliced_IsDirectory_down()) {
                	Random r = new Random();
                	int Low = 50;
                	int High = 450;
                	int ver_low = 600;
                	int ver_high = 1000;
                	int R1 = r.nextInt(High-Low) + Low;
                	int R2 = r.nextInt(ver_high-ver_low) + ver_low;
                	Fruit New_fruit = new Fruit(new Area(new Ellipse2D.Double(R1, R2, 50, 50)));
                	Random randomGenerator = new Random();
                	int red = randomGenerator.nextInt(255);
                	int green = randomGenerator.nextInt(255);
                	int blue = randomGenerator.nextInt(255);
                	Color randomColour = new Color(red,green,blue);
                	New_fruit.setFillColor(randomColour);
                	model.add(New_fruit);
                    //System.out.println("sliced");
                    Score = Score + 10;
                   // s.setFillColor(New_fruit.getFillColor());
                    try {
                        Fruit[] newFruits = s.split(drag.getStart(), drag.getEnd());
            
                        // add offset so we can see them split - this is used for demo purposes only!
                        // you should change so that new pieces appear close to the same position as the original piece
                        model.clean(s);
                        for (Fruit f : newFruits) {
                        	f.setFillColor(s.getFillColor());
                            model.add(f);
                        }
                    } catch (Exception ex) {
                        System.err.println("Caught error: " + ex.getMessage());
                    }
                } else {
                	//System.out.println("not sliced");
                }
            }
        }
    };

    /*
     * Track starting and ending positions for the drag operation
     * Needed to calculate angle of the slice
     */
    private class MouseDrag {
        private Point2D start;
        private Point2D end;
        private float theta;
        MouseDrag() {
        }
        
        protected void start(Point2D start) { this.start = start; }
        protected void stop(Point2D end) { this.end = end; }
        protected float Angel() {
        	this.theta = (float) Math.toDegrees(Math.atan2(this.start.getY()-this.end.getY(), this.start.getX()-this.end.getX()));
        	this.theta = 180 - this.theta;   // assume drag from bottom to top firstly
        	return this.theta;
        }

        protected Point2D getStart() { return start; }
        protected Point2D getEnd() { return end; }

    }
}

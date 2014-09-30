/**
 * CS349 Winter 2014
 * Bingcheng Zhu
 * University of Waterloo
 */
import java.awt.*;
import java.awt.geom.*;

/**
 * Class that represents a Fruit. Can be split into two separate fruits.
 */
public class Fruit implements FruitInterface {
    private Area            fruitShape   = null;
    private Color           fillColor    = Color.RED;
    private Color           outlineColor = Color.BLACK;
    private AffineTransform transform    = new AffineTransform();
    private double          outlineWidth = 5;
    private int 			move_directory = 1;   // get to know the move directory of fruit
    private boolean 		piece_fruit = false;
    private int 			fruit_down_count = 0;
    private boolean 		shouldbeRemove = false;
    /**
     * A fruit is represented using any arbitrary geometric shape.
     */
    Fruit (Area fruitShape) {
        this.fruitShape = (Area)fruitShape.clone();
    }

    /**
     * The color used to paint the interior of the Fruit.
     */
    public Color getFillColor() {
        return fillColor;
    }
    /**
     * The color used to paint the interior of the Fruit.
     */
    public void setFillColor(Color color) {
        fillColor = color;
    }
    /**
     * The color used to paint the outline of the Fruit.
     */
    public Color getOutlineColor() {
        return outlineColor;
    }
    /**
     * The color used to paint the outline of the Fruit.
     */
    public void setOutlineColor(Color color) {
        outlineColor = color;
    }
    
    /**
     * Gets the width of the outline stroke used when painting.
     */
    public double getOutlineWidth() {
        return outlineWidth;
    }

    /**
     * Sets the width of the outline stroke used when painting.
     */
    public void setOutlineWidth(double newWidth) {
        outlineWidth = newWidth;
    }

    /**
     * Concatenates a rotation transform to the Fruit's affine transform
     */
    public void rotate(double theta) {
        transform.rotate(theta);
    }

    /**
     * Concatenates a scale transform to the Fruit's affine transform
     */
    public void scale(double x, double y) {
        transform.scale(x, y);
    }

    /**
     * Concatenates a translation transform to the Fruit's affine transform
     */
    public void translate(double tx, double ty) {
        transform.translate(tx, ty);
    }

    /**
     * Returns the Fruit's affine transform that is used when painting
     */
    public AffineTransform getTransform() {
        return (AffineTransform)transform.clone();
    }

    /**
     * Creates a transformed version of the fruit. Used for painting
     * and intersection testing.
     */
    public Area getTransformedShape() {
        return fruitShape.createTransformedArea(transform);
    }

    /**
     * Paints the Fruit to the screen using its current affine
     * transform and paint settings (fill, outline)
     */
    public void draw(Graphics2D g2) {
        // TODO BEGIN CS349
    	g2.setColor(fillColor);
    	g2.fill(fruitShape);
        // TODO END CS349
    }

    /**
     * Tests whether the line represented by the two points intersects
     * this Fruit.
     */
	public boolean intersects(Point2D p1, Point2D p2) {
        // TODO BEGIN CS349
    	Rectangle2D r = new Rectangle();
		r = this.getTransformedShape().getBounds2D();
		double radius = r.getWidth() / 2;
		double distance_P1 = p1.distance(r.getCenterX(), r.getCenterY());
		double distance_P2 = p2.distance(r.getCenterX(), r.getCenterY());
    	if (r.intersectsLine(p1.getX(),p1.getY(), p2.getX(), p2.getY())
    		&& distance_P1 > radius && distance_P2 > radius) {
    		//System.out.println("omg world");
    		return true;
    	}
    	else {
        // TODO END CS349
        return false;
    	}
    }

    /**
     * Returns whether the given point is within the Fruit's shape.
     */
    public boolean contains(Point2D p1) {
        return this.getTransformedShape().contains(p1);
    }
    
    /*
     * This method is get the distance away from the top bound
     * I set top bound is y = 100
     */
    public double Getdistance_between_topbound() {
    	double Bounding1 = 100;
    	return this.getTransformedShape().getBounds2D().getCenterY() - Bounding1;
    }
    
    
    /*
     *  Get the constant Gravity
     */
    public double Get_gravity() {
    	return 0.04;
    }
    
    /*
     * 
     */
    public boolean IsSliced_IsDirectory_down() {
    	return (this.move_directory == 0 && this.piece_fruit);
    }
    
    /*
     *  Get the fruit game limit count 
     */
    public int game_limit(){
    	return this.fruit_down_count;
    }
    
    /*
     * 
     */
    public boolean Fruit_shouldbeRemoved(){
    	return this.shouldbeRemove;
    }
    
    /*
     *  Get the current vertical_Velocity of fruit
     */
    public double Velocity() {
    	if(this.Getdistance_between_topbound() <= 0.05 || this.move_directory == 0) {
    		this.move_directory = 0;
    		//System.out.println("hello world");
    		//System.out.println(this.Getdistance_between_topbound());
    		if (this.Getdistance_between_topbound() > 400 && this.Getdistance_between_topbound() < 404) {
    			this.fruit_down_count = this.fruit_down_count + 1;
    			//System.out.println(this.fruit_down_count);
    			this.shouldbeRemove = true;
    			//System.out.println(this.shouldbeRemove);
    		}
    		if (this.Getdistance_between_topbound() >= 500) {
    			this.move_directory = 1;
    		}
    		return Math.sqrt(Math.abs(this.Getdistance_between_topbound()) * this.Get_gravity());
    	}
    	if(this.Getdistance_between_topbound() >=500 || this.move_directory == 1) {
    		//System.out.println("OMG WORLD");
    		this.move_directory = 1;
    		return 0 - Math.sqrt(this.Getdistance_between_topbound() * this.Get_gravity());
    	}
    	else {
    		this.move_directory = 0;
    		return Math.sqrt(this.Getdistance_between_topbound() * this.Get_gravity());
    	}


    }
    
    /*
     * 	Get the current horizontal_Velocity of fruit
     */
    public double H_Velocity() {
    	if (this.getTransformedShape().getBounds().getCenterX() <= 250
    		&& this.getTransformedShape().getBounds().getCenterY() <= 400) {
    		return 0.2;
    	}
    	else {
    		return -0.2;
    	}
    }
    
    
    /**
     * This method assumes that the line represented by the two points
     * intersects the fruit. If not, unpredictable results will occur.
     * Returns two new Fruits, split by the line represented by the
     * two points given.
     */

	public Fruit[] split(Point2D p1, Point2D p2) throws NoninvertibleTransformException {
        Area R_topArea = null;
        Area R_botArea = null;
        
        
        // TODO BEGIN CS349
        double theta;
        double translateX = 0;
        double translateY = 0;
        if (p1.getY() >= p2.getY()) {
        	translateX = p1.getX();
        	translateY = p1.getY();
            theta = (float) Math.toDegrees(Math.atan2(p1.getY()-p2.getY(), p1.getX()-p2.getX()));
            theta = 180 - theta;
        }
        else {
        	translateX = p2.getX();
        	translateY = p2.getY();
            theta = (float) Math.toDegrees(Math.atan2(p2.getY()-p1.getY(), p2.getX()-p1.getX()));
            theta = 180 - theta;
        }

        //System.out.println("theta to rotate: " + theta);
        
        AffineTransform T = new AffineTransform();
        T.rotate(Math.toRadians(theta));
        T.translate(-translateX, -translateY);
        Area a = this.getTransformedShape();
        a.transform(T);
        
        //System.out.print("after translateX: "+ a.getBounds2D().getCenterX()+" ");  //test
        //System.out.println("translateY: "+ a.getBounds2D().getCenterY());
        Rectangle2D R = new Rectangle();
        R = a.getBounds2D();
 
        double WIDTH = R.getWidth();
        double HEIGHT = R.getHeight();
        double topArea_height = Math.abs(R.getMinY());
        double botArea_height = HEIGHT - topArea_height;
	    R_topArea = new Area(new Rectangle.Double(R.getMinX(), R.getMinY(), WIDTH, topArea_height));
	    //System.out.print("the MinX: "+R.getMinX()+" the MinY: "+R.getMinY());
	    //System.out.println("the MaxX: "+R.getMaxX()+" the MaxY: "+R.getMaxY());
        R_botArea = new Area(new Rectangle.Double(R.getMinX(), 0, WIDTH, botArea_height));
        R_topArea.intersect(a);
        //System.out.print("the top_Area MinY "+R_topArea.getBounds2D().getMaxY());
        R_botArea.intersect(a);
        //System.out.println(" the bot_Area MinY "+R_botArea.getBounds2D().getMinY());
        // Rotate shape to align slice with x-axis
        // Bisect shape above/below x-axis (look at intersection methods!)
        // TODO END CS349
        if (R_topArea != null && R_botArea != null) {
        	if ((theta >= 0 && theta <= 10)) { 
               	AffineTransform T0 = new AffineTransform();
            	T0.translate(0, -10);
            	T0.translate(translateX, translateY);
            	T0.rotate(-Math.toRadians(theta));
            	R_topArea.transform(T0);
            	
               	AffineTransform T3 = new AffineTransform();
            	T3.translate(0, 10);
            	T3.translate(translateX, translateY);
            	T3.rotate(-Math.toRadians(theta));
            	R_botArea.transform(T3);
            	
            	Fruit f1 = new Fruit(R_topArea);
            	Fruit f2 = new Fruit(R_botArea);
            	f1.move_directory = 0;
            	f1.piece_fruit = true;
            	f2.move_directory = 0;
            	f2.piece_fruit = true;
            	return new Fruit[] { f1, f2 };
        	}
        	if  ((theta <= 180 && theta >=170)) {
               	AffineTransform T0 = new AffineTransform();
            	T0.translate(0, 10);
            	T0.translate(translateX, translateY);
            	T0.rotate(-Math.toRadians(theta));
            	R_topArea.transform(T0);
            	
               	AffineTransform T3 = new AffineTransform();
            	T3.translate(0, -10);
            	T3.translate(translateX, translateY);
            	T3.rotate(-Math.toRadians(theta));
            	R_botArea.transform(T3);
            	
            	Fruit f1 = new Fruit(R_topArea);
            	Fruit f2 = new Fruit(R_botArea);
            	f1.move_directory = 0;
            	f1.piece_fruit = true;
            	f2.move_directory = 0;
            	f2.piece_fruit = true;
            	return new Fruit[] { f1, f2 };
        	}
        	else {
        	
        		AffineTransform T1 = new AffineTransform();
        		T1.translate(-10, 0);
        		T1.translate(translateX, translateY);
        		T1.rotate(-Math.toRadians(theta));
        		R_topArea.transform(T1);
        	
        		AffineTransform T2 = new AffineTransform();
        		T2.translate(10, 0);
        		T2.translate(translateX, translateY);
        		T2.rotate(-Math.toRadians(theta));
        		R_botArea.transform(T2);
        		
            	Fruit f1 = new Fruit(R_topArea);
            	Fruit f2 = new Fruit(R_botArea);
            	f1.move_directory = 0;
            	f1.piece_fruit = true;
            	f2.move_directory = 0;
            	f2.piece_fruit = true;
            	return new Fruit[] { f1, f2 };
        	}
        }	
        return new Fruit[0];
     }
}

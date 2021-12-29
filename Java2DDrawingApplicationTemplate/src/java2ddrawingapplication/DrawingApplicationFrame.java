/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author acv
 */
public class DrawingApplicationFrame extends JFrame
{

    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.
    
    private JPanel line1 = new JPanel();
    private JPanel line2 = new JPanel();
    
    private JPanel topPanel = new JPanel();
    
    
    
    

    // create the widgets for the firstLine Panel.
    //Created widgets for firstline on panel
    
    private final JButton undoButton = new JButton("Undo");
    
    private final JButton clearButton = new JButton("Clear");
    
    private JComboBox<String> shapesComboBox;
    private static final String[] shapes =
    {"Line", "Oval", "Rectangle"};
    
    private JLabel shapeLabel = new JLabel("Shape:");
    
    private JCheckBox filledCheckBox = new JCheckBox("Filled");

    //create the widgets for the secondLine Panel.
    
    private JCheckBox gradientCheckBox = new JCheckBox("Use Gradient");
    
    private JButton colorOneButton = new JButton("Color 1");
    
    private JButton colorTwoButton = new JButton("Color 2");
    
    Color color1 = Color.black;
    
    Color color2 = Color.white;
    
    private JLabel widthLabel = new JLabel("Line Width:");
 
    private JTextField widthField = new JTextField(2);
    
    private JLabel dashLabel = new JLabel("Dash Length");
    
    private JTextField dashField = new JTextField(2);
    
    private JCheckBox dashCheckBox = new JCheckBox("Dashed");
    
    // Create event handlers, if needed
    //Created 4 event handlers for button
    
    private class ClearButtonHandler implements ActionListener{
        
        @Override
        public void actionPerformed(ActionEvent event){
            
            ShapesList.clear();
            
            repaint();
            
        }
        
        
    }
    
    private class UndoButtonHandler implements ActionListener{
        
        @Override
        public void actionPerformed(ActionEvent event){

            if (ShapesList.isEmpty() == false){
                ShapesList.remove(ShapesList.size()-1);

            }
            
            repaint();
       
        }
        
    }


    private class ColorHandlerOne implements ActionListener{
        
        

        @Override
        public void actionPerformed(ActionEvent event){
            
         color1 = JColorChooser.showDialog(drawPanel,"Choose Color 1", color1);
         
         //If Cancel is hit, color reverts to defualt
         if (color1 == null){
             color1 = Color.black;
         }

        }
    }
    
    
    private class ColorHandlerTwo implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            
         color2 = JColorChooser.showDialog(drawPanel,"Choose Color 2", color2);     
         if (color2 == null){
             color2 = Color.white;
         }        

        }
    }
    



    // Variables for drawPanel.
    
    private DrawPanel drawPanel = new DrawPanel();
    
    private boolean useGradient;
    

    
    private float lineWidth = 5;
    

    private float[] dashLength = {10}; 
       
    ArrayList<MyShapes> ShapesList = new ArrayList<MyShapes>();
   
    MyShapes currentShape;
    
    Stroke stroke;
    
    Paint paint;
    
    private boolean filled;
    

    // add status label
    
    private JLabel statusLabel = new JLabel("(x,y)");
    
           
  
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {
        super("Java 2D Drawings");
        
        setLayout(new BorderLayout());
        
        topPanel.setLayout(new GridLayout(2,1));
        // add widgets to panels
        
        
        line1.add(undoButton);
        line1.add(clearButton);
        shapesComboBox = new JComboBox<String>(shapes);
        line1.add(shapeLabel);
        line1.add(shapesComboBox);
        
        line1.add(filledCheckBox);
        
        line2.add(gradientCheckBox);
        
        line2.add(colorOneButton);
        line2.add(colorTwoButton);
        line2.add(widthLabel);
        line2.add(widthField);

        
        line2.add(dashLabel);
        line2.add(dashField);
        
        line2.add(dashCheckBox);
        

        
        // firstLine widgets
        topPanel.add(line1);

        // secondLine widgets
        
        topPanel.add(line2);

        // add top panel of two panels
        
                
        add(topPanel, BorderLayout.NORTH);
       
        
        
        

        
        

        
        
        
        
        

        // add topPanel to North, drawPanel to Center, and statusLabel to South
        

        add(drawPanel, BorderLayout.CENTER);
        
        drawPanel.setBackground(Color.white);
        
        add(statusLabel, BorderLayout.SOUTH);
        


        
        
        
        

      
        //add listeners and event handlers
        ActionListener colorHandlerOne = new ColorHandlerOne();
        
        colorOneButton.addActionListener(colorHandlerOne);
        
        ActionListener colorHandlerTwo = new ColorHandlerTwo();
        
        colorTwoButton.addActionListener(colorHandlerTwo);
        
        ActionListener undoButtonHandler = new UndoButtonHandler();
        
        undoButton.addActionListener(undoButtonHandler);
        
        ActionListener clearButtonHandler = new ClearButtonHandler();
        
        clearButton.addActionListener(clearButtonHandler);
        
           
        
    }
    
    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {
        //Mouse Handlers intitialized through constructor
        public DrawPanel()
        {
           MouseHandler handler = new MouseHandler();
           this.addMouseListener(handler);
           this.addMouseMotionListener(handler);
       }
        
       
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            //loop through and draw each shape in the shapes arraylist
            for (MyShapes loopShape : ShapesList ){
                    
                loopShape.draw(g2d);

            }

        }


        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {

            public void mousePressed(MouseEvent event)
            {
                
            
            //Sets up dashLength if field is written   
            if (dashField.getText().isEmpty() == false){               
                String stringLength = dashField.getText();              
                float length = Float.parseFloat(stringLength);            
                dashLength[0] = length;
            }

            //Sets up lineWidth if field is written
            if (widthField.getText().isEmpty() == false){               
                String stringLength = widthField.getText();              
                float length = Float.parseFloat(stringLength);            
                lineWidth = length;   
            }
            
            //Sets up filled check box
            if (filledCheckBox.isSelected()){
                
                filled = true;
            }
            else{
                filled = false;
            }
            
            //Sets up gradientCheckBox, default paint is color1
            if (gradientCheckBox.isSelected()){
                
                paint = new GradientPaint(0,0,color1, 50,50, color2, true);
            }
            else{   
                paint = color1;
            }

                
            //Sets up dash check box
            if (dashCheckBox.isSelected())
            {
            stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashLength, 0);
            } else
            {
                stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            }
            
            //Gets first point    
            Point pointOne = event.getPoint();
            
            
            //Creates currentShape based on cases from shapesComboBox
            if ((shapesComboBox.getSelectedItem()) == "Line")
                currentShape = new MyLine(pointOne, pointOne, paint, stroke);  
            
            if ((shapesComboBox.getSelectedItem()) == "Rectangle")
                currentShape = new MyRectangle(pointOne, pointOne, paint, stroke, filled);
                
            
            if ((shapesComboBox.getSelectedItem()) == "Oval") 
                currentShape = new MyOval(pointOne, pointOne, paint, stroke, filled);
                
    
            //Adds shape to ShapeList
            ShapesList.add(currentShape);

            }

            public void mouseReleased(MouseEvent event)
            {       
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                
            MyShapes tempShape = currentShape;

            //Gets start and end point of new shape
            Point pointOne = tempShape.getStartPoint();
                
            Point pointTwo = event.getPoint();
                
                
                
            //Updats current shape based on new points
            if ((shapesComboBox.getSelectedItem()) == "Line")
                currentShape = new MyLine(pointOne, pointTwo, paint, stroke);
                      
            if ((shapesComboBox.getSelectedItem()) == "Rectangle")
                currentShape = new MyRectangle(pointOne, pointTwo, paint, stroke, filled);

            
            if ((shapesComboBox.getSelectedItem()) == "Oval")
                currentShape = new MyOval(pointOne, pointTwo, paint, stroke, filled);
            
            


            //Updates shape on ShapesList
            ShapesList.set(ShapesList.size()-1, currentShape);
            statusLabel.setText(String.format("(%d,%d)", event.getX(), event.getY()));
                repaint();
      
            }

            @Override
            public void mouseMoved(MouseEvent event)
            {
                statusLabel.setText(String.format("(%d,%d)", event.getX(), event.getY()));
            }
        }

    }
}

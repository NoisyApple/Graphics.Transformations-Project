package com.noisyapple;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.BasicStroke;

// Models a dynamic layered figure, each figure can have any amount of layers and each layer any
// amount of points.
public class LayeredFigure {

    private ArrayList<Point[]> layers = new ArrayList<Point[]>(); // Layers array list.
    private Color[] layerColors; // Layer colors.
    private int width, height;

    public LayeredFigure(ArrayList<Point[]> layers, Color[] layerColors) {
        this.layers = new ArrayList<Point[]>();
        this.layerColors = layerColors;

        // Initual values for min and max of x and y.
        int minX = (int) layers.get(0)[0].getX();
        int minY = (int) layers.get(0)[0].getY();

        int maxX = 0;
        int maxY = 0;

        // Iterates through each point to get min and max values for x and y.
        for (Point[] layer : layers) {
            for (Point p : layer) {
                minX = (p.getX() < minX) ? (int) p.getX() : minX;
                minY = (p.getY() < minY) ? (int) p.getY() : minY;
                maxX = (p.getX() > maxX) ? (int) p.getX() : maxX;
                maxY = (p.getY() > maxY) ? (int) p.getY() : maxY;
            }
        }

        width = maxX - minX; // Width of the figure.
        height = maxY - minY; // Height of the figure.

        // All points are modified so the figure'center is at 0, 0.
        for (Point[] layer : layers) {
            int innerMinX = minX;
            int innerMinY = minY;
            this.layers
                    .add(Arrays.stream(layer)
                            .map(p -> new Point((int) p.getX() - innerMinX - (width / 2),
                                    (int) p.getY() - innerMinY - (height / 2)))
                            .toArray(Point[]::new));
        }

    }

    // Draws every layer with its correspondant color.
    public void drawLayeredFigure(Graphics2D ctx) {

        int colorIndex = 0;

        ctx.setStroke(new BasicStroke(2));

        for (Point[] layer : layers) {
            ctx.setColor(layerColors[colorIndex]);

            int[] xCoords = Arrays.stream(layer).mapToInt(p -> (int) (p.getX())).toArray();

            int[] yCoords = Arrays.stream(layer).mapToInt(p -> (int) (p.getY())).toArray();

            ctx.drawPolygon(xCoords, yCoords, layer.length);

            colorIndex++;
        }
    }

    // Returns figure width.
    public int getWidth() {
        return width;
    }

    // Returns figure height.
    public int getHeight() {
        return height;
    }

    // To string handler method.
    public String toString() {
        String output = "";

        int index = 1;

        for (Point[] layer : layers) {
            output += "[LAYER " + index++ + "]" + Arrays.toString(layer) + "\n";
        }

        return output;
    }

}

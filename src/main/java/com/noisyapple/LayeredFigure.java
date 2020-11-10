package com.noisyapple;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

public class LayeredFigure {

    private ArrayList<Point[]> layers = new ArrayList<Point[]>();
    private Color[] layerColors;
    private int width, height;

    public LayeredFigure(ArrayList<Point[]> layers, Color[] layerColors) {
        this.layers = new ArrayList<Point[]>();
        this.layerColors = layerColors;

        int minX = (int) layers.get(0)[0].getX();
        int minY = (int) layers.get(0)[0].getY();

        int maxX = 0;
        int maxY = 0;

        for (Point[] layer : layers) {
            for (Point p : layer) {
                minX = (p.getX() < minX) ? (int) p.getX() : minX;
                minY = (p.getY() < minY) ? (int) p.getY() : minY;
                maxX = (p.getX() > maxX) ? (int) p.getX() : maxX;
                maxY = (p.getY() > maxY) ? (int) p.getY() : maxY;
            }
        }

        for (Point[] layer : layers) {
            int innerMinX = minX;
            int innerMinY = minY;
            this.layers.add(Arrays.stream(layer)
                    .map(p -> new Point((int) p.getX() - innerMinX, (int) p.getY() - innerMinY))
                    .toArray(Point[]::new));
        }

        width = maxX - minX;
        height = maxY - minY;
    }

    public void drawLayeredFigure(Graphics2D ctx) {

        int colorIndex = 0;

        for (Point[] layer : layers) {
            ctx.setColor(layerColors[colorIndex]);

            int[] xCoords = Arrays.stream(layer).mapToInt(p -> (int) (p.getX())).toArray();

            int[] yCoords = Arrays.stream(layer).mapToInt(p -> (int) (p.getY())).toArray();

            ctx.drawPolygon(xCoords, yCoords, layer.length);

            colorIndex++;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String toString() {
        String output = "";

        int index = 1;

        for (Point[] layer : layers) {
            output += "[LAYER " + index++ + "]" + Arrays.toString(layer) + "\n";
        }

        return output;
    }

}

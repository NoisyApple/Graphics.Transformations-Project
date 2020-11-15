package com.noisyapple;

import java.util.ArrayList;
import java.awt.Point;
import java.awt.Color;

public class App {
        public static void main(String[] args) {

                ArrayList<Point[]> layers = new ArrayList<Point[]>();

                layers.add(new Point[] {new Point(100, 100), new Point(200, 100),
                                new Point(200, 200), new Point(100, 200)});

                layers.add(new Point[] {new Point(150, 150), new Point(250, 150),
                                new Point(250, 250), new Point(150, 250)});

                layers.add(new Point[] {new Point(200, 100), new Point(250, 200),
                                new Point(150, 200)});

                Color[] layerColors = new Color[] {Color.decode("#8888ff"), Color.decode("#ff8888"),
                                Color.decode("#88ff88")};

                LayeredFigure lFigure = new LayeredFigure(layers, layerColors);

                new Preview(lFigure);

        }
}

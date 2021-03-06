/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.aura;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Random;

/**
 *
 * @author kieda
 */
public class Fire extends Applet
{

    public void init()
    {
        // width and height of fire that is calculated.
        // The fire is tiled to fit the width of the applet's canvas.
        width = 320;
        height = 200 + DOWN_SHIFT;

        // These two maps hold buffers where the fire intensity is stored.
        fire_map1 = new byte[(width + 1) * height + 2];
        fire_map2 = new byte[(width + 1) * height + 2];

        // Generate a nice looking pallette for the fire.
        byte abyte0[] = new byte[256];
        byte abyte1[] = new byte[256];
        byte abyte2[] = new byte[256];
        for(int j = 0; j < 256; j++)
            abyte0[j] = 0;

        for(int k = 0; k < 256; k++)
            abyte1[k] = 0;

        for(int l = 0; l < 256; l++)
            abyte2[l] = 0;

        for(int i1 = 64; i1 < 128; i1++)
            abyte1[i1] = (byte)((i1 - 64) * 4);

        for(int j1 = 0; j1 < 128; j1++)
            abyte0[j1] = (byte)(j1 * 2);

        // Create a color model based on the palette that was created.
        cm = new IndexColorModel(8, 256, abyte0, abyte1, abyte2);
        // Create a random number generator
        gen = new Random();

        // Create a MemoryImageSource object that is generated by the fire
        // map.
        mis = new MemoryImageSource(width, height, cm, fire_map1, 0, width);
        mis.setAnimated(true);
        fire_img = createImage(mis);

        // Start drawing the fire.
        draw_fire();
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void draw_fire()
    {
        byte base = 0;
        byte noise;
        int i = width;

        gen.setSeed( (frame/SEED_PERIOD) * 23464891 );
        for(int j = 0; j < width; j++)
        {
            // Create a seed line at the bottom of the buffer.
            if(j % SEED_WIDTH == 0)
            {
                base = (byte)
                    (gen.nextDouble() >= 0.59999999999999996D ? 50 : 96);
            }
            fire_map2[width * (height - 1) + j] = base;
            fire_map2[width * (height - 2) + j] = base;


            // Create some noise at the bottom of the screen.
            noise = (byte)(gen.nextInt(32));
            fire_map2[width * (height - SECOND_SEED ) + j] += noise;
        }
        frame++;

        for(int l = 1; l < height - 2; l++)
        {
            if( l % COOL_SKIP == 0 && l < (height - FADE_SHIFT) )
            {
                // Smooth and fade the line in the buffer
                for(int j1 = 0; j1 < width + 1; j1++)
                {
                    i++;
                    fire_map1[i] = (byte)((fire_map2[i] + 
                                           fire_map2[i + 1] +
                                           fire_map2[i - 1] +
                                           fire_map2[i + width] +
                                           fire_map2[(i + width) - 1] +
                                           fire_map2[i + width + 1] +
                                           fire_map2[(i + width * 2) - 1] +
                                           fire_map2[i + width * 2] +
                                           fire_map2[i + width * 2 + 1]) / 9);
                    if( fire_map1[i] != 0)
                    {
                        fire_map1[i]--;
                    }
                }
            }
            else
            {
                // Smooth the line in the buffer
                for(int j1 = 0; j1 < width + 1; j1++)
                {
                    i++;
                    fire_map1[i] = (byte)((fire_map2[i] + 
                                           fire_map2[i + 1] +
                                           fire_map2[i - 1] +
                                           fire_map2[i + width] +
                                           fire_map2[(i + width) - 1] +
                                           fire_map2[i + width + 1] +
                                           fire_map2[(i + width * 2) - 1] +
                                           fire_map2[i + width * 2] +
                                           fire_map2[i + width * 2 + 1]) / 9);
                }
            }

        }

        // Update the memory image
        mis.newPixels( fire_map1, cm, 0, width );

        // Swap the two buffers
        byte map[] = fire_map1;
        fire_map1 = fire_map2;
        fire_map2 = map;

        repaint();
    }

    public void paint(Graphics g)
    {
        // Tile the fire image at the bottom of the canvas
        for(int i = 0; i < getSize().width; i += width)
            g.drawImage(fire_img, i, getSize().height - height + DOWN_SHIFT,
                    this);

        // Clear the top of the canvas with black
        g.setColor(Color.black);
        g.fillRect(0, 0, getSize().width,
                getSize().height - height + DOWN_SHIFT);
        draw_fire();
    }

    public Fire()
    {
    }

    final static int SEED_WIDTH = 10;
    final static int SEED_PERIOD = 10;
    final static int DOWN_SHIFT = 20;
    final static int FADE_SHIFT = 40;
    final static int SECOND_SEED = 20;
    final static int COOL_SKIP = 4;

    int width;
    int height;
    int frame;
    byte fire_map1[];
    byte fire_map2[];
    Image fire_img;
    IndexColorModel cm;
    MemoryImageSource mis;
    Random gen;
}
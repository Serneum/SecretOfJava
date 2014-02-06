/**
 * @(#)Text1.java
 *
 *
 * @author
 * @version 1.00 2009/3/3
 */
package com.serneum.soj.map;

import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

import com.serneum.soj.core.GameBase;


public class ScreenManager
{
    private static GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private static GraphicsDevice device = environment.getDefaultScreenDevice();;

    public ScreenManager()
    {

    }

    public static void setFullScreen(DisplayMode displayMode)
    {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setIgnoreRepaint(true);
        frame.setResizable(false);

        device.setFullScreenWindow(frame);

        if (displayMode != null &&
            device.isDisplayChangeSupported())
        {
            try
            {
                device.setDisplayMode(displayMode);
            }
            catch (IllegalArgumentException ex) { }
            // fix for mac os x
            frame.setSize(displayMode.getWidth(),
                displayMode.getHeight());
            GameBase.screenHeight = displayMode.getHeight();
            GameBase.screenWidth = displayMode.getWidth();
        }

        // avoid potential deadlock in 1.4.1_02
        try
        {
            EventQueue.invokeAndWait(new Runnable()
            {
                @Override
                public void run()
                {
                    frame.createBufferStrategy(2);
                }
            });
        }
        catch (InterruptedException ex)
        {
            // ignore
        }
        catch (InvocationTargetException  ex)
        {
            // ignore
        }
    }

    //Checks the window to be active, and if it is,
    //disposes of it
    public static void restoreScreen()
    {
        Window window = device.getFullScreenWindow();
        if (window != null)
        {
            window.dispose();
        }
        device.setFullScreenWindow(null);
    }
    public static DisplayMode findFirstCompatibleMode(DisplayMode modes[])
        {
            DisplayMode goodModes[] = device.getDisplayModes();
            for (int i = 0; i < modes.length; i++) {
                for (int j = 0; j < goodModes.length; j++) {
                    if (displayModesMatch(modes[i], goodModes[j])) {
                        return modes[i];
                    }
                }

            }

            return null;
        }
    /**
        Returns the window currently used in full screen mode.
        Returns null if the device is not in full screen mode.
     */
    public static JFrame getFullScreenWindow()
    {
        return (JFrame)device.getFullScreenWindow();
    }

    public static Graphics2D getGraphics()
    {
        Window window = device.getFullScreenWindow();
        if (window != null) {
            BufferStrategy strategy = window.getBufferStrategy();
            return (Graphics2D)strategy.getDrawGraphics();
        }
        else {
            return null;
        }
    }

    /**
        Updates the display.
     */
    public static void update()
    {
        Window window = device.getFullScreenWindow();
        if (window != null)
        {
            BufferStrategy strategy = window.getBufferStrategy();
            if (!strategy.contentsLost())
            {
                strategy.show();
            }
        }
        // Sync the display on some systems.
        // (on Linux, this fixes event queue problems)
        Toolkit.getDefaultToolkit().sync();
    }


    /**
        Determines if two display modes "match". Two display
        modes match if they have the same resolution, bit depth,
        and refresh rate. The bit depth is ignored if one of the
        modes has a bit depth of DisplayMode.BIT_DEPTH_MULTI.
        Likewise, the refresh rate is ignored if one of the
        modes has a refresh rate of
        DisplayMode.REFRESH_RATE_UNKNOWN.
     */

    public static boolean displayModesMatch(DisplayMode mode1, DisplayMode mode2)
        {
            if (mode1.getWidth() != mode2.getWidth() ||
                mode1.getHeight() != mode2.getHeight())
            {
                return false;
            }

            if (mode1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI &&
                mode2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI &&
                mode1.getBitDepth() != mode2.getBitDepth())
            {
                return false;
            }

            if (mode1.getRefreshRate() !=
                DisplayMode.REFRESH_RATE_UNKNOWN &&
                mode2.getRefreshRate() !=
                DisplayMode.REFRESH_RATE_UNKNOWN &&
                mode1.getRefreshRate() != mode2.getRefreshRate())
             {
                 return false;
             }

             return true;
        }
}
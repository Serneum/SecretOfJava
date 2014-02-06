//Author: Zulu

package com.serneum.soj.core;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.sound.midi.Sequence;

import com.serneum.soj.creatures.Creature;
import com.serneum.soj.creatures.Player;
import com.serneum.soj.map.MapDeterminer;
import com.serneum.soj.map.MapDraw;
import com.serneum.soj.map.ScreenCoord;
import com.serneum.soj.map.ScreenManager;
import com.serneum.soj.object.GameObject;
import com.serneum.soj.object.GraphicObject;
import com.serneum.soj.object.Melee;
import com.serneum.soj.object.Spells;
import com.serneum.soj.object.Walls;
import com.serneum.soj.sound.MidiPlayer;
import com.serneum.soj.sound.SoundManager;
import com.serneum.soj.threads.IntroScreen;
import com.serneum.soj.threads.PauseScreen;
import com.serneum.soj.util.Collisions;

public class GameBase
{
    protected static final int FONT_SIZE = 24;

    private static final DisplayMode POSSIBLE_MODES[] =
    {
        new DisplayMode(800, 600, 16, 0),
        new DisplayMode(800, 600, 32, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(640, 480, 16, 0),
        new DisplayMode(640, 480, 32, 0),
        new DisplayMode(640, 480, 24, 0),
        new DisplayMode(1024, 768, 16, 0),
        new DisplayMode(1024, 768, 32, 0),
        new DisplayMode(1024, 768, 24, 0),
    };

    private boolean isRunning;

    public static Vector<GameObject> masterList;
    public static Vector<GameObject> screenList;
    public static Vector<Creature> creatureList;
    public static Vector<GraphicObject> graphicList;
    public static Vector<GameObject> layers;

    private static long elapsedTime;
    private static long currTime;

    private static String mode, skillUsed;

    public static Player player;
    public GameObject box;

    private InputManager inputManager;
    private GameAction moveLeft;
    private GameAction moveRight;
    private GameAction moveUp;
    private GameAction moveDown;
    private GameAction jump;
    private GameAction spellCast;
    private GameAction spellCancel;
    private GameAction hotkey1;
    private GameAction hotkey2;
    private GameAction hotkey3;
    private GameAction hotkey4;
    private GameAction hotkey5;
    private GameAction exit;
    public static GameAction pause;
    private MidiPlayer midiPlayer;
    public static SoundManager soundManager;
    public static int screenWidth, screenHeight;
    private MapDeterminer map = new MapDeterminer();

    /**
        Signals the game loop that it's time to quit
    */
    public void stop()
    {
        isRunning = false;
    }

    /**
        Calls init() and gameLoop()
    */
    public void run()
    {
        try
        {
            init();
            gameLoop();
        }
        finally
        {
            ScreenManager.restoreScreen();
            lazilyExit();
        }
    }


        /**
            Exits the VM from a daemon thread. The daemon thread waits
            2 seconds then calls System.exit(0). Since the VM should
            exit when only daemon threads are running, this makes sure
            System.exit(0) is only called if necessary. It's necessary
            if the Java Sound system is running.
         */
    public void lazilyExit()
    {
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                // first, wait for the VM exit on its own.
                try
                {
                    Thread.sleep(2000);
                }
                catch (InterruptedException ex) { }
                // system is still running, so force an exit
                System.exit(0);
            }
        };
        thread.setDaemon(true);
        thread.start();
    }


    /**
        Sets full screen mode and initiates and objects.
    */
    public void init()
    {
        DisplayMode displayMode =
            ScreenManager.findFirstCompatibleMode(POSSIBLE_MODES);
        ScreenManager.setFullScreen(displayMode);

        Window window = ScreenManager.getFullScreenWindow();
        window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
        window.setBackground(Color.black);
        window.setForeground(Color.green);

        //Initialize the max values ONLY ONCE
        //Caused many problems before it was placed here
        ScreenCoord.init();


//masterList keeps track of all 'things' on the current map
//screenList keeps track of all 'things' on screen
        masterList = new Vector<GameObject>();
        screenList = new Vector<GameObject>();
        creatureList = new Vector<Creature>();
        graphicList = new Vector<GraphicObject>();
        layers = new Vector<GameObject>();

        masterList.removeAllElements();
        screenList.removeAllElements();
        graphicList.removeAllElements();
        layers.removeAllElements();

//TestPlayer
        player = new Player("Player", 75, 75, 32, 34, 34, 1);
        //masterList.add(Playah);
//Test Object
        box = new GameObject("Box", 250, 250, 27, 23, 23);
        //masterList.add(Box);
//Test Stairs
        //GameObject stairs = new GameObject("stairs", 100, 100, 25, 24, 999);

        map.map();

        //Initiate the input manager to default
        //Having it a separate method allows us to switch
        //input types on the fly, such as for 'Aiming a spell',
        //for spots where the user isn't allowed any actions,
        //among others
        inputSet("Initiate");

        // start music
        midiPlayer = new MidiPlayer();
        Sequence sequence =
            midiPlayer.getSequence("sounds/dungeon.midi");
        midiPlayer.play(sequence, true);

        isRunning = true;

        Thread intro = new Thread(new IntroScreen());
        intro.start();
        try
        {
            intro.join();
        }
        catch(InterruptedException IE)
        {
        }

    }

    private void inputSet(String action)
    {
        if (action.equals("Initiate"))
        {
            moveLeft = new GameAction("moveLeft");
            moveRight = new GameAction("moveRight");
            moveUp = new GameAction("moveUp");
            moveDown = new GameAction("moveDown");

            jump = new GameAction("jump",
                    GameAction.DETECT_INITIAL_PRESS_ONLY);
            hotkey1 = new GameAction("Thunderstorm", GameAction.DETECT_INITIAL_PRESS_ONLY);
            hotkey2 = new GameAction("Explosion", GameAction.DETECT_INITIAL_PRESS_ONLY);
            hotkey3 = new GameAction("Fire Ball", GameAction.DETECT_INITIAL_PRESS_ONLY);
            hotkey4 = new GameAction("Cleave", GameAction.DETECT_INITIAL_PRESS_ONLY);
            hotkey5 = new GameAction("XSlash", GameAction.DETECT_INITIAL_PRESS_ONLY);
            pause = new GameAction("pause", GameAction.DETECT_INITIAL_PRESS_ONLY);
            exit = new GameAction("exit",
                GameAction.DETECT_INITIAL_PRESS_ONLY);
            spellCast = new GameAction("spellCast", GameAction.DETECT_INITIAL_PRESS_ONLY);
            spellCancel = new GameAction("spellCancel", GameAction.DETECT_INITIAL_PRESS_ONLY);

            inputManager = new InputManager(ScreenManager.getFullScreenWindow());
            inputManager.setCursor(InputManager.INVISIBLE_CURSOR);

            inputManager.mapToKey(moveLeft, KeyEvent.VK_A);
            inputManager.mapToKey(moveRight, KeyEvent.VK_D);
            inputManager.mapToKey(moveUp, KeyEvent.VK_W);
            inputManager.mapToKey(moveDown, KeyEvent.VK_S);
            inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
            inputManager.mapToKey(hotkey1, KeyEvent.VK_1);
            inputManager.mapToKey(hotkey2, KeyEvent.VK_2);
            inputManager.mapToKey(hotkey3, KeyEvent.VK_3);
            inputManager.mapToKey(hotkey4, KeyEvent.VK_4);
            inputManager.mapToKey(hotkey5, KeyEvent.VK_5);
            inputManager.mapToKey(pause, KeyEvent.VK_P);
            inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
            inputManager.mapToMouse(spellCast, InputManager.MOUSE_BUTTON_1);
            inputManager.mapToMouse(spellCancel, InputManager.MOUSE_BUTTON_3);
            mode = "Default";
        }
    }

    //This method will be moved later on to its own class
    //which will handle all game actions and then pass the
    //values back
    private void checkInput()
    {
        if (exit.isPressed())
        {
           stop();
        }

        if (!player.dead)
        {
            float velocityX = 0, velocityZ = 0;

            if (moveLeft.isPressed())
            {
                velocityX -= player.getMaxSpeed();
            }

            if (moveRight.isPressed())
            {
                velocityX += player.getMaxSpeed();
            }

            if (moveUp.isPressed())
            {
                velocityZ -= player.getMaxSpeed();
            }

            if (moveDown.isPressed())
            {
                velocityZ += player.getMaxSpeed();
            }

            if (jump.isPressed())
            {
                player.jumping = true;
            }

            if (hotkey1.isPressed())
            {
                skillUsed = hotkey1.name;
                inputManager.setCursor(InputManager.SPELL_AIM_CURSOR);
                mode = "Casting";
            }

            if (hotkey2.isPressed())
            {
                skillUsed = hotkey2.name;
                inputManager.setCursor(InputManager.SPELL_AIM_CURSOR);
                mode = "Casting";
            }

            if (hotkey3.isPressed())
            {
                skillUsed = hotkey3.name;
                inputManager.setCursor(InputManager.SPELL_AIM_CURSOR);
                mode = "Casting";
            }

            if (hotkey4.isPressed())
            {
                skillUsed = hotkey4.name;
                Melee.cast(skillUsed, player);
            }

            if (hotkey5.isPressed())
            {
                skillUsed = hotkey5.name;
                Melee.cast(skillUsed, player);
            }

            if (pause.isPressed())
            {
                Thread p = new Thread(new PauseScreen());
                p.start();
                try
                {
                    p.join();
                    currTime = System.currentTimeMillis();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            if (mode.equals("Casting"))
            {
                if (spellCast.isPressed())
                {
                    Spells.cast(skillUsed, player, inputManager.getMouseX(), inputManager.getMouseY());
                    inputManager.setCursor(InputManager.INVISIBLE_CURSOR);
                    mode = "Default";
                }
                else if (spellCancel.isPressed())
                {
                    inputManager.setCursor(InputManager.INVISIBLE_CURSOR);
                    mode = "Default";
                }
            }

            player.setVelocityX(velocityX);
            player.setVelocityZ(velocityZ);
        }
    }

    /**
        Runs through the game loop until stop() is called.
    */
    public void gameLoop()
    {
        long startTime = System.currentTimeMillis();
        currTime = startTime;

        elapsedTime =
            System.currentTimeMillis() - currTime;
        currTime += elapsedTime;

        while (isRunning)
        {
            elapsedTime =
                System.currentTimeMillis() - currTime;
            currTime += elapsedTime;

            Graphics g = ScreenManager.getGraphics();
            Graphics2D g2 = ScreenManager.getGraphics();

            Walls.drawWalls();

            //Send player data
            Creature.PlayerLocator(player);

            // draw the map
            MapDraw.paint(g);

            // update
            update();

            /*
            g.drawString("Player: " + Playah.BottomRange.getX() + "," + Playah.BottomRange.getZ(), 20, 40);
            g.drawString("Player: " + Playah.UpperRange.getX() + "," + Playah.UpperRange.getZ(), 20, 60);
            g.drawString("Box: " + Box.BottomRange.getX() + "," + Box.BottomRange.getZ(), 20, 510);
            g.drawString("Box: " + Box.UpperRange.getX() + "," + Box.UpperRange.getZ(), 20, 530);
            g.drawString("Shadow: " + dot.BottomRange.getX() + "," + dot.BottomRange.getZ(), 560, 40);
            g.drawString("Shadow: " + dot.UpperRange.getX() + "," + dot.UpperRange.getZ(), 560, 60);
            */

            //Initiates a loop to draw all objects currently on the screen
            for (int counter = 0; counter < screenList.size(); counter++)
            {
                screenList.elementAt(counter).paint(g2);

                if (screenList.elementAt(counter) instanceof Creature)
                {
                    Creature temp = new Creature();
                    temp = (Creature)screenList.elementAt(counter);

                    if (screenList.elementAt(counter) instanceof Player)
                    {
                        g.setColor(Color.green);
                        g.fillRect(10, 12, 200 * temp.currentHealth/temp.maxHealth, 10);
                        g.drawString(temp.currentHealth + "/" + temp.maxHealth, 14, 40);
                    }
                    else
                    {
                        g.setColor(Color.red);
                        g.fillRect((int)(temp.getX() - (temp.getLength() / 2)), (int)temp.upperRange.getZ() + 2, (int)(temp.getLength() * temp.currentHealth/temp.maxHealth), 4);
                    }

                    if(player.levelUp)
                    {
                        new GraphicObject(player, "Level Up", null);
                        player.levelUp = false;
                    }
                }
            }
            for (int counter = 0; counter < graphicList.size(); counter++)
            {
                graphicList.elementAt(counter).paint(g2);
            }

            /*
            g.setColor(Color.green);
            g.drawString("Health: " + Playah.currentHealth, 20, 40);
            g.drawString("EXP: " + Playah.EXP, 20, 60);
            g.drawString("Level: " + Playah.level, 20, 80);
            g.dispose();
            */

            ScreenManager.update();
        }
    }

    public static long getTime()
    {
        return elapsedTime;
    }

    /**
        Updates the state of the game/animation based on the
        amount of elapsed time that has passed.
    */
    public void update()
    {

        // get keyboard/mouse input
        checkInput();

        screenList.removeAllElements();
        creatureList.removeAllElements();
        layers.removeAllElements();

        //update enemies and objects as well as graphics
        //In this update creatures are also added and removed
        //from the screenList as necessary before doing collisions
        for (int count = 0; count < masterList.size(); count++)
        {
            masterList.elementAt(count).update();
        }
        for (int count = 0; count < graphicList.size(); count++)
        {
            graphicList.elementAt(count).update();
        }

        //Sets all creatures on the screen to the creatureList
        for (int count = 0; count < screenList.size(); count++)
        {
            if (screenList.elementAt(count) instanceof Creature) {
                creatureList.add((Creature)screenList.elementAt(count));
            }
        }

        //Collisions
        //x is used in order to prevent object A from being checked with object B
        //more than one time. As collisions are checked in a certain order.

        for(int count1 = 0; count1 < screenList.size(); count1++)
        {
            for (int count2 = count1; count2 < screenList.size(); count2++)
            {
                if(screenList.elementAt(count1) != screenList.elementAt(count2))
                    {
                        Collisions.checkCollision(screenList.elementAt(count1),
                                screenList.elementAt(count2));
                    }
            }
        }

        //Sorts the screenList array from smallest Z to largest,
        //so they may be drawn in that same order in the paint method.
        //This makes it so objects 'in front' are drawn last and thus
        //'over' objects which are 'behind' them
        GameObject temp = new GameObject();

        int index;
        int smallestIndex;
        int minIndex;

        for (index = 0; index < screenList.size() - 1; index++)
        {
            smallestIndex = index;
            for (minIndex = index +1; minIndex < screenList.size(); minIndex++)
            {
                if (screenList.elementAt(minIndex).getZ() < screenList.elementAt(smallestIndex).getZ())
                {
                    smallestIndex = minIndex;
                }
                temp = screenList.elementAt(smallestIndex);
                screenList.set(smallestIndex, screenList.elementAt(index));
                screenList.set(index, temp);
            }
        }

    }

    public static void main(String[] args)
    {
        new GameBase().run();
    }
}
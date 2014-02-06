package com.serneum.soj.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import com.serneum.soj.core.GameBase;
import com.serneum.soj.creatures.Creature;
import com.serneum.soj.creatures.Player;
import com.serneum.soj.map.ScreenCoord;
import com.serneum.soj.util.Animation;

public class GraphicObject
{
    protected float length, width, height, velocityX, velocityZ, timeAlive, totalDuration;
    public float X, Z, newX, newZ, mapX, mapZ, dX, dZ;
    public int frames = 0, lastMoveX, lastMoveZ;
    public String name, type, method, damage;
    private boolean imageLoaded;
    protected Animation animation;
    protected Image sprite;
    private int lastMove;
    private Color textColor;

    public GraphicObject(String name, String type, int frames,  float totalDuration, int length, int height, int X, int Z,
            float velocityX, float velocityZ, int last)
    {
        method = "Graphic";
        GameBase.graphicList.add(this);

        this.name = name;
        this.type = type;
        this.frames = frames;
        this.length = length;
        this.height = height;
        this.mapX = X;
        this.mapZ = Z;
        this.velocityX = velocityX;
        this.velocityZ = velocityZ;
        this.totalDuration = totalDuration;
        this.lastMove = last;

        animation = new Animation();
        loadSprite();

        init();
    }

    public GraphicObject(Creature creature, String damage, String type)
    {
        method = "Damage";
        GameBase.graphicList.add(this);

        if (damage.equals("Miss"))
        {
            if (creature instanceof Player) {
                textColor = Color.blue;
            }
            else {
                textColor = Color.red;
            }
        }
        else if (damage.equals("Level Up"))
        {
            textColor = Color.blue;
        }
        else
        {
            if (creature instanceof Player) {
                textColor = Color.red;
            }
            else {
                textColor = Color.green;
            }
        }


        this.damage = damage;
        this.mapX = creature.mapX - creature.getLength()/2;
        this.mapZ = creature.mapZ - creature.getWidth()/2;
        this.velocityX = 0;
        this.velocityZ = -.05f;
        this.totalDuration = 1000;

        init();
    }

    public void init()
    {
        getLoc();
        timeAlive = GameBase.getTime();
    }

    public void loadSprite()
    {
        imageLoaded = true;
        String path = "";
        int frameDuration = (int)(totalDuration/frames);

        if (type.equals("skill"))
        {
            path = "images/skills/"+ name + "/";
        }

        for (int count = 1; count <= frames; count++)
        {
            animation.addFrame(new ImageIcon(path + name + count + ".png").getImage(), frameDuration);
        }

    }

    public void setVelocityX(float newVelocityX)
    {
        velocityX = newVelocityX;
    }

    public void setVelocityZ(float newVelocityZ)
    {
        velocityZ = newVelocityZ;
    }

    public void changeMapXZ()
    {
        dX = velocityX * GameBase.getTime();
        dZ = velocityZ * GameBase.getTime();

        mapX += dX;
        mapZ += dZ;
    }

    public void update()
    {
        timeAlive += GameBase.getTime();
        if (totalDuration - timeAlive <= 0) {
            stop();
        }

        if(method.equals("Graphic"))
        {
            animation.update(GameBase.getTime());
            sprite = animation.getImage();

            changeMapXZ();

            getLoc();

            if (dX > 0) {
                lastMoveX = 1;
            }
            else if (dX < 0) {
                lastMoveX = -1;
            }
            if (dZ > 0) {
                lastMoveZ = 1;
            }
            else if (dZ < 0) {
                lastMoveZ = -1;
            }
            else {
                ;
            }
        }

        else if(method.equals("Damage"))
        {
            changeMapXZ();
            getLoc();
        }
    }

    public void paint(Graphics2D g)
    {
        if (method.equals("Graphic"))
        {
            if (imageLoaded) {
                if(name.equals("cleave"))
                {
                    if(lastMove == 1) {
                        g.drawImage(sprite, (int)(X - length/2), (int)(Z - height/2), null);
                    }

                    if(lastMove == 2)
                    {
                        g.rotate(Math.PI, X, Z);
                        g.drawImage(sprite, (int)(X - length/2), (int)(Z - height/2), null);
                        g.rotate(-1 * Math.PI, X, Z);
                    }

                    if(lastMove == 3)
                    {
                        g.rotate(Math.PI/2, X, Z);
                        g.drawImage(sprite, (int)(X - length/2), (int)(Z - height/2), null);
                        g.rotate(-1* (Math.PI/2), X, Z);
                    }

                    if(lastMove == 4)
                    {
                        g.rotate(-1 * Math.PI/2, X, Z);
                        g.drawImage(sprite, (int)(X - length/2), (int)(Z - height/2), null);
                        g.rotate(Math.PI/2, X, Z);
                    }
                }

                else if(name.equals("xslash"))
                {
                    for(int count = 0; count < 4; count++)
                    {
                        g.rotate(Math.PI/2, X, Z);
                        g.drawImage(sprite, (int)(X - length/2), (int)(Z - height/2), null);
                    }
                }
                else {
                    g.drawImage(sprite, (int)(X - length/2), (int)(Z - height/2), null);
//    }
//else;
                }
            }
            else {
                loadSprite();
            }
        }
        else if(method.equals("Damage"))
        {
            g.setColor(textColor);
            g.drawString(damage, this.X, this.Z);
        }
    }

    public boolean onScreen()
    {
        if (mapX + length > ScreenCoord.screenMinX &&
                mapX < ScreenCoord.screenMaxX &&
                    mapZ + width > ScreenCoord.screenMinY &&
                        mapZ < ScreenCoord.screenMaxY) {
            return true;
        }
        else {
            return false;
        }
    }

    public void stop()
    {
        GameBase.graphicList.removeElement(this);
    }

    public void getLoc()
    {
        X = GameBase.screenWidth - (ScreenCoord.screenMaxX - mapX);
        Z = GameBase.screenHeight - (ScreenCoord.screenMaxY - mapZ);
    }
}

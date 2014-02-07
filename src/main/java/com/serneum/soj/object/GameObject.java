//Author: Zulu

package com.serneum.soj.object;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.net.URL;

import javax.swing.ImageIcon;

import com.serneum.soj.core.GameBase;
import com.serneum.soj.creatures.Player;
import com.serneum.soj.map.ScreenCoord;
import com.serneum.soj.map.ScreenReset;
import com.serneum.soj.util.Animation;
import com.serneum.soj.util.TriplePoint;

public class GameObject
{
    protected float length, width, height, maxSpeed,
        jumpHeight, velocityX, velocityY, velocityZ, elevation;
    public float X, Y, Z, newX, newY, newZ, mapX, mapZ, dX, dY, dZ;
    public int frames = 1, lastMove = 1, damMax;
    public String name;
    public boolean jumping, falling, collides, moves;
    public TriplePoint bottomRange, upperRange;
    public Animation animation;
    protected Image sprite, objectShadow;

    //Constructor for creating shells
    public GameObject()
    {

    }

    public GameObject (String name, int locationX, int locationZ, int length,
            int width, int height)
    {
        GameBase.masterList.add(this);

        this.name = name;
        animation = new Animation();
        sprite = animation.getImage();
        if (!(name.startsWith("wall"))) {
            URL imageUrl = GameObject.class.getResource("/object/Shadow1.png");
            objectShadow = new ImageIcon(imageUrl).getImage();
        }

        velocityX = 0;
        velocityZ = 0;
        elevation = 0;
        maxSpeed = 0;
        newX = 0;
        newY = 0;
        newZ = 0;

        dX = 0;
        dY = 0;
        dZ = 0;

        Y = 0;

        mapX = locationX;
        mapZ = locationZ;

        this.length = length;
        this.width = width;
        this.height = height;

        bottomRange = new TriplePoint(0, 0, 0);
        upperRange = new TriplePoint(0, 0, 0);

        jumping = false;
        falling = false;

        collides = true;

        if(!name.equals("Box")) {
            moves = false;
        }
        else {
            moves = true;
        }

        frames = 1;

        init();
    }

    @Override
    public String toString()
    {
        return "object";
    }

    public void init()
    {
        //Over-ridden by subclasses
        loadSprite();
        getLoc();
    }

    public float getX()
    {
        return X;
    }

    public float getY()
    {
        return Y;
    }

    public float getZ()
    {
        return Z;
    }

    public float getLength()
    {
        return length;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public Image getSprite()
    {
        return sprite;
    }

    public float getMaxSpeed()
    {
        return maxSpeed;
    }

    public float getVelocityX()
    {
        return velocityX;
    }

    public float getVelocityZ()
    {
        return velocityZ;
    }

    public boolean collides()
    {
        return collides;
    }

    public boolean canMove()
    {
        return moves;
    }

    public void setX(float newX)
    {
        X = newX;
    }

    public void setY(float newY)
    {
        Y = newY;
    }

    public void setZ(float newZ)
    {
        Z = newZ;
    }

    public void setElevation(float newElevation)
    {
        elevation = newElevation;
    }

    public void changeXZ()
    {
        dX = velocityX * GameBase.getTime();
        dZ = velocityZ * GameBase.getTime();

        newX = X + dX;
        newZ = Z + dZ;
    }

    public void changeY()
    {
        dY = velocityY * GameBase.getTime();

        newY = Y + dY;

        setY(newY);
    }

    public void changeMapXZ()
    {
        dX = velocityX * GameBase.getTime();
        dZ = velocityZ * GameBase.getTime();

        mapX += dX;
        mapZ += dZ;
    }

    //Works
    public void jump()
    {    //Makes the player jump up to the maximum height, then fall
        //if that height has been reached
        if (jumping && !falling)
        {
            if (Y <= jumpHeight)
            {
                newY = Y + 2f;
                setY(newY);
            }
            else {
                falling = true;
            }
        }

        if (falling)
        {
            newY = Y - 2f;
            setY(newY);
        }

        if (Y <= 0)
        {
            Y = 0;
            jumping = false;
            falling = false;
        }

        getLoc();
    }

    public void setVelocityX(float newVelocityX)
    {
        velocityX = newVelocityX;
    }

    public void setVelocityY(float newVelocityY)
    {
        velocityY = newVelocityY;
    }
    public void setVelocityZ(float newVelocityZ)
    {
        velocityZ = newVelocityZ;
    }

    public void move(float moveX, float moveZ)
    {
        //Moves the object at a certain direction and speed

        dX = moveX * GameBase.getTime();
        dZ = moveZ * GameBase.getTime();

        newX = X + dX;
        newZ = Z + dZ;

        mapX += dX;
        mapZ += dZ;

        setX(newX);
        setZ(newZ);

        if (this instanceof Player) {
            ScreenCoord.playerCoord((Player)this);
        }

        this.getLoc();
    }

    public boolean onScreen()
    {
        if (mapX + length > ScreenCoord.screenMinX &&
                mapX - length < ScreenCoord.screenMaxX &&
                    mapZ + width > ScreenCoord.screenMinY &&
                        mapZ - width < ScreenCoord.screenMaxY) {
            return true;
        }
        else {
            return false;
        }
    }

    public void paint(Graphics2D g)
    {


        if (onScreen()||this instanceof Player)
        {

            g.drawImage(objectShadow, (int)(X - 8.5), (int)(bottomRange.getZ() + this.getHeight()/1.1), null);

            AffineTransform transform = new AffineTransform();

            transform.setToTranslation((int)bottomRange.getX(), (int)(bottomRange.getZ() - bottomRange.getY()));
            if(lastMove == 1) {
                g.drawImage(sprite, (int)bottomRange.getX(), (int)(bottomRange.getZ() - bottomRange.getY()), null);
            }
            else if(lastMove == 2)
            {
                transform.scale(-1, 1);
                transform.translate(-getLength(), 0);
                g.drawImage(sprite, transform, null);
            }
            else {
                g.drawImage(sprite, (int)bottomRange.getX(), (int)(bottomRange.getZ() - bottomRange.getY()), null);
            }

            //g.drawImage(new ImageIcon("images/GameObject/dot.gif").getImage(), (int)X, (int)Z, null);
        }
        else {
            ;
        }

    }

    public void loadSprite()
    {
        String path = "/" + toString() + "/";

        for (int count = 1; count <= frames; count++)
        {
            String objectPath = path + name + count + ".png";
            URL imageUrl = GameObject.class.getResource(objectPath);
            animation.addFrame(new ImageIcon(imageUrl).getImage(), 150);
        }
    }

    public void changeAnimation(Animation newAnimation)
    {
        animation.changeTo(newAnimation);
    }

    public void collisionEffect(GameObject obj)
    {
        //do nothing

        if(this.name.equals("stairs") && obj instanceof Player)
        {
            GameBase.player.floor++;
            ScreenReset.stairReset(GameBase.player);
        }

    }

    public void update()
    {
        if (onScreen())
        {
            GameBase.screenList.add(this);

            animation.update(GameBase.getTime());
            sprite = animation.getImage();

            changeMapXZ();

            this.getLoc();

        }
        else
        {
            bottomRange.setAll(-999, -999, -999);
            upperRange.setAll(-999, -999, -999);
        }
    }

    public void checkLastMove()
    {

        if (dX > 0) {
            lastMove = 1;
        }
        else if (dX < 0) {
            lastMove = 2;
        }
    }

    //Used to set the "screen" position of a mob from anywhere on the map
    public void getLoc()
    {
        X = GameBase.screenWidth - (ScreenCoord.screenMaxX - mapX);
        Z = GameBase.screenHeight - (ScreenCoord.screenMaxY - mapZ);

        bottomRange.setAll(X - (length/2), Y + elevation, Z - (width/2));
        upperRange.setAll(X + (length/2), Y + height + elevation, Z + (width/2));
    }
}
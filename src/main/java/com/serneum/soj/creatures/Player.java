//Author: Zulu

package com.serneum.soj.creatures;

import com.serneum.soj.core.GameBase;
import com.serneum.soj.map.ScreenCoord;
import com.serneum.soj.map.ScreenReset;

/**
    The Player.
*/
public class Player extends Creature
{
    public String pName = "GreenPlayer";
    public int floor = 1;
    public int currentEXP = 0;
    public int lastLvlEXP = 0;
    public int nextLvlEXP = 0;
    public boolean levelUp = false;

    public Player(String name, int locationX, int locationZ, int length,
            int width, int height, int level)
    {
        super(name, locationX, locationZ, length, width, height, level);
        collides = true;
        moves = false;
    }

    @Override
    public void init()
    {
        this.level = 1;

        X = mapX;
        Y = 0;
        Z = mapZ;
        elevation = 0;

        maxSpeed = .5f;
        jumpHeight = 50;
        velocityX = 0;
        velocityZ = 0;
        maxHealth = 2000;

        idleFrames = 1;
        walkingFrames = 5;
        attackFrames = 7;
        currentHealth = maxHealth;

        nextLvlEXP = 100;

        damMax = 10 * level;
        //System.out.println("Player: " + damMax);

        bInit();
    }

    //This method will attempt to draw the player in the correct location after
    //evaluating the player's location. Collisions are detected and acted upon
    //in this method
    @Override
    public void update()
    {
        if (dead)
        {
            die();
        }
        GameBase.screenList.add(this);

        if (System.currentTimeMillis() - attackStart >= attackDelay && inAttack)
        {
            inAttack = false;
        }

        if (jumping)
        {
            jump();
        }

        if (!inAttack)
        {
            changeXZ();

            //If moving right...
            if(velocityX > 0)
            {
                //Set limit for how far player can move before
                //screen scrolls
                if(X < .65 * GameBase.screenWidth)
                {
                    setX(newX);
                }

                //When the boundary is hit, scroll screen
                else
                {
                    if(ScreenCoord.screenMaxX > ScreenCoord.mapMaxX) {
                        ScreenCoord.setScreenX(ScreenCoord.screenMaxX - ScreenCoord.mapMaxX);
                        //ScreenCoord.screenMaxX = ScreenCoord.mapMaxX;
                    }
                    else
                    {
                        if(ScreenCoord.screenMaxX < ScreenCoord.mapMaxX) {
                            ScreenCoord.setScreenX((int)(X - newX));
                        }
                        else
                            if(X < .99 * GameBase.screenWidth) {
                                setX(newX);
                            }
                            else {
                                ScreenReset.xReset(this);
                            }
                    }
                }
            }

            //If moving left...
            else
            {
                //If not at 0...
                if(getX() > 0)
                {
                    //Move character unless screen has been scrolled
                    if(X > .35 * GameBase.screenWidth) {
                        setX(newX);
                    }
                    else
                    {
                        if(ScreenCoord.screenMinX < ScreenCoord.mapMinX) {
                            ScreenCoord.setScreenX(ScreenCoord.screenMinX - ScreenCoord.mapMinX);
                            //ScreenCoord.screenMinX = ScreenCoord.mapMinX;
                        }
                        else
                        {
                            //Scrolls screen back until map is back in 0,0 position
                            if(ScreenCoord.screenMinX > ScreenCoord.mapMinX) {
                                ScreenCoord.setScreenX((int)(X - newX));
                            //Allow player to return to 0,0
                            }
                            else
                                if(X > 0) {
                                    setX(newX);
                                }
                        }
                    }
                }
                else {
                    ScreenReset.xReset(this);
                }
            }

            //If moving down...
            if(velocityZ > 0)
            {
                //Set how far player can go before screen scrolls
                if(Z < .65 * GameBase.screenHeight)
                {
                    setZ(newZ);
                }

                //When screen scrolls, allow it to hit the end of the map
                else
                {
                    if(ScreenCoord.screenMaxY > ScreenCoord.mapMaxY) {
                        ScreenCoord.setScreenY(ScreenCoord.screenMaxY - ScreenCoord.mapMaxY);
                        //ScreenCoord.screenMaxY = ScreenCoord.mapMaxY;
                    }
                    else
                    {
                        if(ScreenCoord.screenMaxY < ScreenCoord.mapMaxY) {
                            ScreenCoord.setScreenY((int)(Z - newZ));
                        }
                        else
                            if(Z < .99 * GameBase.screenHeight) {
                                setZ(newZ);
                            }
                            else {
                                ScreenReset.zReset(this);
                            }
                    }
                }
            }

            //If moving up...
            else
            {
                //If not at 0...
                if(getZ() > 0)
                {
                    //Allow player to move up
                    if(Z > .35 * GameBase.screenHeight) {
                        setZ(newZ);
                    }
                    else
                    {
                        if(ScreenCoord.screenMinY < ScreenCoord.mapMinY) {
                            ScreenCoord.setScreenY(ScreenCoord.screenMinY - ScreenCoord.mapMinY);
                            //ScreenCoord.screenMinY = ScreenCoord.mapMinY;
                        }
                        else
                        {
                            if(ScreenCoord.screenMinY > ScreenCoord.mapMinY) {
                                ScreenCoord.setScreenY((int)(Z - newZ));
                            }
                            else
                                if(Z > 0) {
                                    setZ(newZ);
                                }
                        }
                    }
                }
                else {
                    ScreenReset.zReset(this);
                }

                ScreenCoord.playerCoord(this);
                mapX = ScreenCoord.pX;
                mapZ = ScreenCoord.pZ;
            }

        if (dX != 0 || dZ != 0)
        {
            currStatus = "Moving";
        }
        else {
            currStatus = "Idle";
        }

        }


        //This checks the creature for movement different than
        //its last check, and sets its animation accordingly

        if (currStatus.equals(lastStatus))
        {}
        else
        {
            lastStatus = currStatus;
            if (lastStatus.equals("Moving")) {
                changeAnimation(moving);
            }
            else if (lastStatus.equals("Idle")) {
                changeAnimation(idle);
            }
        }


        bottomRange.setAll(X - (length/2), Y + elevation, Z - (width/2));
        upperRange.setAll(X + (length/2), Y + height + elevation, Z + (width/2));

        animation.update(GameBase.getTime());
        sprite = animation.getImage();

        ScreenCoord.playerCoord(this);
        mapX = ScreenCoord.pX;
        mapZ = ScreenCoord.pZ;

        checkLastMove();

    }

    public void gainEXP(int amount)
    {
        currentEXP += amount;

        System.out.println(nextLvlEXP);

        if (currentEXP >= nextLvlEXP)
        {
            levelUp = true;
            level++;

            lastLvlEXP = nextLvlEXP;
            nextLvlEXP = (level * 25) + 75 + lastLvlEXP;

            maxHealth += 10 * level;
            currentHealth = maxHealth;
            damMax = 10 * level;
        }
    }

    @Override
    public String toString()
    {
        return "object/creature/player";
    }
}
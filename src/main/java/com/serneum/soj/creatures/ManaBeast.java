/***********************************************************************
 *Author: Chris Rees
 *Date: 4/27/09
 *File Name: ManaBeast.java
 *Purpose: Creates the basic AI for the ManaBeast
***********************************************************************/

package com.serneum.soj.creatures;

import java.util.Random;

import com.serneum.soj.ai.AIBase;
import com.serneum.soj.core.GameBase;
import com.serneum.soj.map.ScreenCoord;
import com.serneum.soj.object.GameObject;
import com.serneum.soj.object.Spells;

public class ManaBeast extends Creature implements AIBase
{
    public int r = 0;
    public float speed = 0;
    private static Random gen = new Random();

    public ManaBeast(String name, int locationX, int locationZ, int length,
            int width, int height, int level)
    {
        super(name, locationX, locationZ, length, width, height, level);

        cInit();
    }

    public void cInit()
    {
        jumpHeight = 20;
        attackFrames = 4;
        attackDelay = 500;
        walkingFrames = 4;
        idleFrames = 4;
        level = GameBase.player.floor + (GameBase.player.level * 2);

        loadSprite();
        setAI();
    }

    @Override
    public void setAI()
    {
        damMax = 15 * level;
        maxHealth = 250 * level;
        currentHealth = maxHealth;
        speed = .2f;

        //System.out.println("ManaBeast: " + damMax);
        this.setElevation(50);

        movement();
    }

    @Override
    public void movement()
    {
        moveZ();
    }

    @Override
    public void attack(Creature target)
    {
        inAttack = true;
        attackStart = System.currentTimeMillis();
        changeAnimation(attacking);

        r = gen.nextInt(100) + 1;

        if(r > 50) {
            Spells.cast("Fire Ball", this, (int)pX, (int)pY);
        }
    }

    @Override
    public void moveX()
    {
        //Not required
    }

    @Override
    public void moveZ()
    {
        if(ScreenCoord.screenMinY + Z <= (ScreenCoord.mapMaxY / 2) - 200) {
            this.setVelocityZ(speed);
        }
        else if(ScreenCoord.screenMinY + Z >= (ScreenCoord.mapMaxY / 2) + 200) {
            this.setVelocityZ(-speed);
        }
    }

    @Override
    public void update()
    {
        if (dead)
        {
            die();
            new GameObject("stairs", ScreenCoord.mapMaxX - 50, ScreenCoord.mapMaxY / 2, 25, 24, 999);
        }

        if (onScreen())
        {
            GameBase.screenList.add(this);

            if (System.currentTimeMillis() - attackStart >= attackDelay && inAttack)
            {
                inAttack = false;
                changeAnimation(moving);
            }

            if (!inAttack)
            {
                attack(GameBase.player);
                if (dX != 0 || dZ != 0)
                {
                    currStatus = "Moving";
                }
                else {
                    currStatus = "Idle";
                }
            }

            movement();
            changeMapXZ();

            //jumping = true;
            if (jumping)
            {
                jump();
            }

            this.getLoc();

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

            animation.update(GameBase.getTime());
            sprite = animation.getImage();
        }
        else
        {
            bottomRange.setAll(-999, -999, -999);
            upperRange.setAll(-999, -999, -999);
        }

        checkLastMove();
    }

    @Override
    public String toString()
    {
        return "object/creature/manabeast";
    }

    /*
    public void collisionEffect(GameObject obj)
    {
        if (obj instanceof Player)
        {
            attack((Creature)obj);
        }
    }
    */
}
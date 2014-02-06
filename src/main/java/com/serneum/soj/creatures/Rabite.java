/***********************************************************************
 *Author: Chris Rees
 *Date: 4/27/09
 *File Name: Rabite.java
 *Purpose: Sets basic AI for the Rabite creature
***********************************************************************/

package com.serneum.soj.creatures;

import java.util.Random;

import com.serneum.soj.ai.AIBase;
import com.serneum.soj.core.GameBase;
import com.serneum.soj.object.GameObject;

public class Rabite extends Creature implements AIBase
{
    public int damage = 0;
    private  float speed;
    private static Random gen = new Random();

    public Rabite(String name, int locationX, int locationZ, int length,
            int width, int height, int level)
    {
        super(name, locationX, locationZ, length, width, height, level);
        this.level = level;

        cInit();
    }

    public void cInit()
    {
        jumpHeight = 20;
        attackFrames = 9;
        attackDelay = 1350;
        walkingFrames = 5;
        idleFrames = 1;
        level = GameBase.player.level;

        loadSprite();
        setAI();
    }

    @Override
    public void setAI()
    {
        damMax = 5 * level;
        maxHealth =  20 * level;
        currentHealth = maxHealth;
        speed = .2f;

        //System.out.println("Rabite: " + damMax);
        movement();
    }

    @Override
    public void movement()
    {
        if(X < pX - length/2 || X > pX + length/2) {
            moveX();
        }
        else {
            setVelocityX(0);
        }
        if(Z < pY - width/2 || Z > pY + width/2) {
            moveZ();
        }
        else {
            setVelocityZ(0);
        }
    }

    public void attack(Creature target)
    {
        inAttack = true;
        currStatus = "Attacking";
        attackStart = System.currentTimeMillis();
        changeAnimation(attacking);
        damage = gen.nextInt(damMax) + 1;
        target.struckFor(damage, "Biting");
    }

    @Override
    public void moveX()
    {
        if(pX - X < 0) {
            this.setVelocityX(-speed);
        }
        else if(pX - X > 0) {
            this.setVelocityX(speed);
        }
        else {
            this.setVelocityX(0);
        }
    }

    @Override
    public void moveZ()
    {
        if(pY - Z < 0) {
            this.setVelocityZ(-speed);
        }
        else if(pY - Z > 0) {
            this.setVelocityZ(speed);
        }
        else {
            this.setVelocityZ(0);
        }
    }

    @Override
    public void update()
    {
        if (dead)
        {
            die();
        }

        if (onScreen())
        {
            GameBase.screenList.add(this);

            if (System.currentTimeMillis() - attackStart >= attackDelay && inAttack)
            {
                inAttack = false;
            }

            if (!inAttack)
            {
                movement();
                changeMapXZ();
                if (dX != 0 || dZ != 0)
                {
                    currStatus = "Moving";
                }
                else {
                    currStatus = "Idle";
                }
            }

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
    public void collisionEffect(GameObject obj)
    {

        if (obj instanceof Player && !inAttack)
        {
            attack((Creature)obj);
        }
    }

    @Override
    public String toString()
    {
        return "GameObject/Creature/Rabite";
    }
}
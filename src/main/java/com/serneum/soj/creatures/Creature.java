//Author: Zulu

package com.serneum.soj.creatures;

import java.util.Random;

import javax.swing.ImageIcon;

import com.serneum.soj.core.GameBase;
import com.serneum.soj.object.GameObject;
import com.serneum.soj.object.GraphicObject;
import com.serneum.soj.util.Animation;

public class Creature extends GameObject
{
    public int maxHealth, currentHealth, maxMana, currentMana, idleFrames, attackFrames, walkingFrames, level = 1;
    private double expPenalty = 0;
    public boolean dead = false;
    public String currStatus, lastStatus, path = "";
    public Animation idle, moving, attacking, death;
    public static float pX = 0, pY = 0;
    private int r;
    private Random gen = new Random();
    public long attackStart;
    public int attackDelay;
    public boolean inAttack = false;

    //Constructor for empty creature shells
    public Creature()
    {
        super();
    }

    public Creature(String name, int locationX, int locationZ, int length,
            int width, int height, int level)
    {
        super(name, locationX, locationZ, length, width, height);

        this.level = level;
        collides = true;
        moves = false;

        bInit();
    }

    public void bInit()
    {
        attacking = new Animation();
        moving = new Animation();
        idle = new Animation();

        loadSprite();
    }

    @Override
    public void loadSprite()
    {
        path = "images/" + toString() + "/";

        for (int count = 1; count <= idleFrames; count++)
        {
            //System.out.println(path + name + "Idle" + count + ".png");
            idle.addFrame(new ImageIcon(path + name + "Idle" + count + ".png").getImage(), 1);
        }

        if (attackFrames > 0) {
            for (int count = 1; count <= attackFrames; count++)
            {
                //System.out.println(path + name + "Attack" + count + ".png");
                attacking.addFrame(new ImageIcon(path + name + "Attack" + count + ".png").getImage(), 150);
            }
        }

        if (walkingFrames > 0) {
            for (int count = 1; count <= walkingFrames; count++)
            {
                //System.out.println(path + name + "Walk" + count + ".png");
                moving.addFrame(new ImageIcon(path + name + "Walk" + count + ".png").getImage(), 150);
            }
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

            changeMapXZ();

            this.getLoc();

            if (dX != 0 || dZ != 0) {
                changeAnimation(moving);
            }
            else {
                changeAnimation(idle);
            }

            animation.update(GameBase.getTime());
            sprite = animation.getImage();

            checkLastMove();

        }
        else
        {
            bottomRange.setAll(-999, -999, -999);
            upperRange.setAll(-999, -999, -999);
        }
    }

    @Override
    public String toString()
    {
        return "GameObject/Creature";
    }

    public void struckFor(int damage, String type)
    {
        r = gen.nextInt(100) + 1;

        if(r > 25 - (.5 * level))
        {
            this.currentHealth -= damage;
            System.out.print(name + " Struck for " + damage + " " + type + "\n");

            new GraphicObject(this, "" + damage, type);
            if (currentHealth <= 0)
            {
                currentHealth = 0;
                dead = true;
            }
        }
        else
        {
            new GraphicObject(this, "Miss", null);
        }
    }

    public void die()
    {
        expPenaltySet();
        //EXP growth/decay is built into the gainEXP formula
        GameBase.player.gainEXP((int)((this.level * 10) - expPenalty));
        GameBase.masterList.removeElement(this);
    }

    public static void PlayerLocator(Player player)
    {
        pX = player.getX();
        pY = player.getZ();
    }

    //Creates a "penalty" of exp for every 3 levels difference between the player and the mob.
    //Penalty for each 3 levels is 25% of the exp, meaning it IS possible to gain 0 exp
    private void expPenaltySet()
    {
        expPenalty = ((GameBase.player.level - this.level) / 3) * (.25 * (this.level * 10));
    }
}
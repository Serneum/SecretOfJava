/***********************************************************************
 *Author: Chris Rees
 *Date: 4/27/09
 *File Name: Melee.java
 *Purpose: Based on Wilfredo's Spells file. Creates animations for
 *melee skills and determines damage.
***********************************************************************/

package com.serneum.soj.object;

import java.util.Random;

import com.serneum.soj.core.GameBase;
import com.serneum.soj.creatures.Creature;
import com.serneum.soj.creatures.Player;
import com.serneum.soj.util.Collisions;
import com.serneum.soj.util.TriplePoint;

public class Melee
{
    private static Random gen = new Random();
    static int damage = 0;

    public static void cast(String skillName, Creature caster)
    {
        if (skillName.equals("Cleave"))
        {
            cleave(caster);
        }

        else if(skillName.equals("XSlash"))
        {
            xSlash(caster);
        }
    }

    public static void cleave(Creature caster)
    {
        System.out.print("Cleaving stuff\n");
        caster.inAttack = true;
        caster.currStatus = "Attacking";
        caster.attackDelay = 500;
        caster.attackStart = System.currentTimeMillis();
        caster.changeAnimation(caster.attacking);

        int bottomX = -999, bottomZ = -999;
        TriplePoint BottomRange = new TriplePoint(0, 0, 0);
        TriplePoint UpperRange = new TriplePoint(0, 0, 0);

        if (caster.lastMove == 1)
        {
            bottomX = (int)(caster.mapX + caster.getLength());
            bottomZ = (int)caster.mapZ;
        }

        else if (caster.lastMove == 2)
        {
            bottomX = (int)(caster.mapX - caster.getLength());
            bottomZ = (int)caster.mapZ;
        }

        else if (caster.lastMove == 3)
        {
            bottomZ = (int)(caster.mapZ + caster.getHeight());
            bottomX = (int)caster.mapX;
        }
        else if (caster.lastMove == 4)
        {
            bottomZ = (int)(caster.mapZ - caster.getHeight());
            bottomX = (int)caster.mapX;
        }

        BottomRange.setY(caster.bottomRange.getY());
        UpperRange.setY(caster.upperRange.getY());

        bottomZ -= BottomRange.getY();

        GraphicObject cleave = new GraphicObject("cleave", "skill", 8, 600, 20, 20, bottomX,
                bottomZ, 0, 0, caster.lastMove);

        cleave.getLoc();

        BottomRange.setX(cleave.X - (float)(caster.getLength()/1.5));
        UpperRange.setX(cleave.X + (float)(caster.getLength()/1.5));

        BottomRange.setZ(cleave.Z - (float)(caster.getHeight()/1.5));
        UpperRange.setZ(cleave.Z + (float)(caster.getHeight()/1.5));

        for(int count = 0; count < GameBase.creatureList.size(); count++)
        {
                if (Collisions.collidesWith(GameBase.creatureList.elementAt(count).bottomRange,
                    GameBase.creatureList.elementAt(count).upperRange, BottomRange, UpperRange) != 0
                        && GameBase.creatureList.elementAt(count) != caster)
                {
                    if(caster instanceof Player || caster instanceof Creature) {
                        damage = gen.nextInt((int)(caster.damMax * 1.5)) + 1;
                    }
                    GameBase.creatureList.elementAt(count).struckFor(damage, "Face-Smashing Cleave Damage");
                }
        }

    }

    public static void xSlash(Creature caster)
    {
        System.out.print("Cross Slashing stuff\n");
        caster.inAttack = true;
        caster.currStatus = "Attacking";
        caster.attackDelay = 500;
        caster.attackStart = System.currentTimeMillis();
        caster.changeAnimation(caster.attacking);

        int bottomX = -999, bottomZ = -999;
        TriplePoint BottomRange = new TriplePoint(0, 0, 0);
        TriplePoint UpperRange = new TriplePoint(0, 999, 0);


        if (caster.lastMove == 1)
        {
            bottomX = (int)(caster.mapX + caster.getLength());
            bottomZ = (int)caster.mapZ;
        }
        else if (caster.lastMove == 2)
        {
            bottomX = (int)(caster.mapX - caster.getLength());
            bottomZ = (int)caster.mapZ;
        }

        else if (caster.lastMove == 3)
        {
            bottomZ = (int)(caster.mapZ + caster.getHeight());
            bottomX = (int)caster.mapX;
        }
        else if (caster.lastMove == 4)
        {
            bottomZ = (int)(caster.mapZ - caster.getHeight());
            bottomX = (int)caster.mapX;
        }

        BottomRange.setY(caster.bottomRange.getY());
        UpperRange.setY(caster.upperRange.getY());

        bottomZ -= BottomRange.getY();

        GraphicObject xSlash = new GraphicObject("xslash", "skill", 8, 600, 20, 20, bottomX,
                bottomZ, 0, 0, caster.lastMove);

        xSlash.getLoc();

        BottomRange.setX(xSlash.X - (float)(caster.getLength()/1.5));
        UpperRange.setX(xSlash.X + (float)(caster.getLength()/1.5));
        BottomRange.setZ(xSlash.Z - (float)(caster.getHeight()/1.5));
        UpperRange.setZ(xSlash.Z + (float)(caster.getHeight()/1.5));

        for(int count = 0; count < GameBase.creatureList.size(); count++)
        {
                if (Collisions.collidesWith(GameBase.creatureList.elementAt(count).bottomRange,
                    GameBase.creatureList.elementAt(count).upperRange, BottomRange, UpperRange) != 0
                        && GameBase.creatureList.elementAt(count) != caster)
                {
                    if(caster instanceof Player || caster instanceof Creature) {
                        damage = gen.nextInt((int)(caster.damMax * 2.5)) + 1;
                    }
                    GameBase.creatureList.elementAt(count).struckFor(damage, "Face-Melting Slash Damage");
                }
        }

    }
}
package com.serneum.soj.object;

import java.awt.Point;
import java.util.Random;

import com.serneum.soj.core.GameBase;
import com.serneum.soj.creatures.Creature;
import com.serneum.soj.creatures.Player;
import com.serneum.soj.map.ScreenCoord;
import com.serneum.soj.util.Collisions;
import com.serneum.soj.util.TriplePoint;

public class Spells
{
    private static Random gen = new Random();
    static int damage = 0;

    public static void cast(String spellName, Creature caster, int x, int z)
    {
        if (spellName.equals("Thunderstorm"))
        {
            thunderstorm(caster, x, z);
        }
        else if (spellName.equals("Explosion"))
        {
            explosion(caster, x, z);
        }
        else if (spellName.equals("Fire Ball"))
        {
            fireBall(caster, x, z);
        }
    }
    public static void explosion(Creature caster, int x, int z)
    {

        System.out.print("Explosion at " + x + "," + z + "\n");
        TriplePoint BottomRange = new TriplePoint(x - 79, 0, z - 75);
        TriplePoint UpperRange = new TriplePoint(x + 79, 999, z + 75);

        Point location = ScreenCoord.objectCoord(new Point(x, z));

        new GraphicObject("explosion", "skill", 10, 1350, 158,148, location.x,
                location.y, 0, 0, 1);


        for(int count = 0; count < GameBase.creatureList.size(); count++)
        {
                if (Collisions.collidesWith(GameBase.creatureList.elementAt(count).bottomRange,
                    GameBase.creatureList.elementAt(count).upperRange, BottomRange, UpperRange) != 0
                        && GameBase.creatureList.elementAt(count) != caster)
                {
                    if(caster instanceof Player || caster instanceof Creature) {
                        damage = gen.nextInt(caster.damMax * 5) + 1;
                    }
                    GameBase.creatureList.elementAt(count).struckFor(damage, "Fire");
                }
            }

    }

    public static void thunderstorm(Creature caster, int x, int z)
    {

        System.out.print("Thunderstorm at " + x + "," + z + "\n");
        TriplePoint BottomRange = new TriplePoint(x - 55, 0, z - 45);
        TriplePoint UpperRange = new TriplePoint(x + 55, 999, z + 45);

        Point location = ScreenCoord.objectCoord(new Point(x, z));

        new GraphicObject("thunderstormBase", "skill", 6, 600, 110, 80, location.x,
                location.y, 0, 0, 1);

        new GraphicObject("thunderstorm", "skill", 6, 600, 110, 260, location.x,
                location.y - 130, 0, 0, 1);


        for(int count = 0; count < GameBase.creatureList.size(); count++)
        {
                if (Collisions.collidesWith(GameBase.creatureList.elementAt(count).bottomRange,
                    GameBase.creatureList.elementAt(count).upperRange, BottomRange, UpperRange) != 0
                        && GameBase.creatureList.elementAt(count) != caster)
                {
                    if(caster instanceof Player || caster instanceof Creature) {
                        damage = gen.nextInt((int)(caster.damMax * 6.5)) + 1;
                    }
                    GameBase.creatureList.elementAt(count).struckFor(damage, "Lightning");
                }
            }

    }

    public static void fireBall(Creature caster, int x, int z)
    {
        caster.inAttack = true;
        caster.currStatus = "Attacking";
        caster.attackDelay = 400;
        caster.attackStart = System.currentTimeMillis();
        caster.changeAnimation(caster.attacking);

        if (x < caster.getX())
        {
            caster.lastMove = 2;
        }
        else {
            caster.lastMove = 1;
        }


        System.out.print("Casting fireball towards " + x + "," + z + "\n");
        SpellObject fireBall = new SpellObject("fireBall", (int)(caster.mapX), (int)(caster.bottomRange.getY()),
                (int)(caster.mapZ), 20, 20, 20, "Explode");

        if (fireBall.getY() == 0) {
            fireBall.setY(1);
        }
        float fireBallSpeed = .5f;
        float totalDistance = (Math.abs(x - fireBall.getX()) + Math.abs(z - fireBall.getZ()));

        float speedPercentageX = (x - fireBall.getX())/(totalDistance);
        float speedPercentageY = -((fireBall.getY())/totalDistance);
        float speedPercentageZ = (z - fireBall.getZ())/(totalDistance);

        fireBall.setVelocityX(fireBallSpeed * speedPercentageX);
        fireBall.setVelocityY(speedPercentageY * .5f);
        fireBall.setVelocityZ(fireBallSpeed * speedPercentageZ);

        fireBall.caster = caster;

    }

}
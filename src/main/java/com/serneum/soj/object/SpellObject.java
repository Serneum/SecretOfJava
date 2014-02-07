package com.serneum.soj.object;

import java.util.Random;

import com.serneum.soj.core.GameBase;
import com.serneum.soj.creatures.Creature;
import com.serneum.soj.creatures.Player;
import com.serneum.soj.util.Collisions;
import com.serneum.soj.util.TriplePoint;

public class SpellObject extends GameObject
{
    String collideAction;
    public GameObject caster;
    private int damage = 0;
    private Random gen = new Random();

    public SpellObject(String name, int locationX, int locationY, int locationZ, int length,
            int width, int height, String collideAction)
    {
        super(name, locationX, locationZ, length, width, height);
        this.Y = locationY;
        collides = true;
        moves = false;
        this.collideAction = collideAction;
    }

    //Here the different collision effects are listed and their general behavior used
    //currently, only one behavior is used per effect, though that can easily be changed
    public void collisionEffect()
    {
        if (collideAction.equals("Explode"))
        {
            TriplePoint explBottom = new TriplePoint(bottomRange.getX() - getLength()* 2,
                    bottomRange.getY(), bottomRange.getZ() - getWidth()* 2);
            TriplePoint explUpper = new TriplePoint(upperRange.getX() + getLength()* 2,
                    upperRange.getY() + this.getHeight()* 3,
                    upperRange.getZ() + getWidth() * 2);

            for(int count = 0; count < GameBase.creatureList.size(); count++)
            {
                if (Collisions.collidesWith(GameBase.screenList.elementAt(count).bottomRange,
                        GameBase.creatureList.elementAt(count).upperRange,
                        explBottom, explUpper) != 0 && GameBase.screenList.elementAt(count) != caster)
                {
                    if(caster instanceof Player || caster instanceof Creature) {
                        damage = gen.nextInt(caster.damMax) + 1;
                    }
                    GameBase.creatureList.elementAt(count).struckFor(damage, "Fire");
                }

            }

            System.out.print("Exploding Fireball\n");
            GameBase.masterList.remove(this);
        }
    }

    @Override
    public void update()
    {
        if (onScreen())
        {
            if (this.Y <= 0) {
                this.collisionEffect();
            }
            GameBase.screenList.add(this);

            animation.update(GameBase.getTime());
            sprite = animation.getImage();

            changeMapXZ();
            changeY();

            this.getLoc();

        }
        else
        {
            collisionEffect();
        }
    }

    @Override
    public String toString()
    {
        return "skills/"+ name;
    }

}

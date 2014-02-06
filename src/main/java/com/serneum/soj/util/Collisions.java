//Author: Zulu

/*
 * This class isn't used at the moment, it will be used once a list
 * of all objects and creatures on screen are obtained, and then will
 * be used in order to check and handle collisions of those in the methods
 * below. Each type of collision has its own method
 */

package com.serneum.soj.util;

import com.serneum.soj.creatures.Player;
import com.serneum.soj.object.GameObject;
import com.serneum.soj.object.SpellObject;

public class Collisions
{
    public static void checkCollision(GameObject X, GameObject Y)
    {
        /*
         System.out.print(X.name + "\n");
        System.out.print(X.BottomRange.getX() + ", " + X.BottomRange.getY() + ", " + X.BottomRange.getZ() + "\n");
        System.out.print(X.UpperRange.getX() + ", " + X.UpperRange.getY() + ", " + X.UpperRange.getZ() + "\n");
        System.out.print("Checking with\n");
        System.out.print(Y.name + "\n");
        System.out.print(Y.BottomRange.getX() + ", " + Y.BottomRange.getY() + ", " + Y.BottomRange.getZ() + "\n");
        System.out.print(Y.UpperRange.getX() + ", " + Y.UpperRange.getY() + ", " + Y.UpperRange.getZ() + "\n");
        */

        if (X.collides && Y.collides)
        {
            int collisionTest = collidesWith(X.bottomRange, X.upperRange,
                    Y.bottomRange, Y.upperRange);

            if (collisionTest == 0)//Works
            {
                //System.out.print(X.name + " Collided 0 with " + Y.name + "\n");
                //Do nothing, no collision
            }
            else if (collisionTest == 1)//Works in part
            {
//                System.out.print(X.name + " Collided 1 with " + Y.name + "\n");
                collide(X, Y, collisionTest);
            }
            else if (collisionTest == 2)//Needs work
            {
                //System.out.print(X.name + " Collided 2 with " + Y.name + "\n");
//                X.setElevation(Y.UpperRange.getZ());
            }
            else if (collisionTest== 3)//Needs work
            {
                //System.out.print(X.name + " Collided 3 with " + Y.name + "\n");
//                Y.setElevation(X.UpperRange.getZ());
            }
        }
    }

    /*
     * Compares these TripplePoints in  ways addressed below
     * to detect collision.
    */
    public static int collidesWith(TriplePoint BottomRange1, TriplePoint UpperRange1,
            TriplePoint BottomRange2, TriplePoint UpperRange2)
    {
        int collisionTracker = 0;

        //This statement checks for the object to be in the other object's vertical
        //space. If either's minimum point is above the other's maximum point, no
        //further collision testing is needed.

        if ((((BottomRange1.getY() >= BottomRange2.getY() && BottomRange1.getY() <= UpperRange2.getY())) ||
                (BottomRange2.getY() >= BottomRange1.getY() && BottomRange2.getY() <= UpperRange1.getY())) ||
                (((UpperRange1.getY() >= BottomRange2.getY() && UpperRange1.getY() <= UpperRange2.getY())) ||
                (UpperRange2.getY() >= BottomRange1.getY() && UpperRange2.getY() <= UpperRange1.getY())))
        {
            //This statement checks for the creature's to intercept at the X axis
            //if any of the first creature's 'collision points', defined by any points
            //lying in between the creature's minimum and maximum values, it moves to the
            //final test, the Z or 'depth' axis.

            if ((((BottomRange1.getX() >= BottomRange2.getX() && BottomRange1.getX() <= UpperRange2.getX())) ||
                    (BottomRange2.getX() >= BottomRange1.getX() && BottomRange2.getX() <= UpperRange1.getX())) ||
                    (((UpperRange1.getX() >= BottomRange2.getX() && UpperRange1.getX() <= UpperRange2.getX())) ||
                    (UpperRange2.getX() >= BottomRange1.getX() && UpperRange2.getX() <= UpperRange1.getX())))
                {
                    if ((((BottomRange1.getZ() >= BottomRange2.getZ() && BottomRange1.getZ() <= UpperRange2.getZ())) ||
                            (BottomRange2.getZ() >= BottomRange1.getZ() && BottomRange2.getZ() <= UpperRange1.getZ())) ||
                            (((UpperRange1.getZ() >= BottomRange2.getZ() && UpperRange1.getZ() <= UpperRange2.getZ())) ||
                            (UpperRange2.getZ() >= BottomRange1.getZ() && UpperRange2.getZ() <= UpperRange1.getZ())))
                    {
                        collisionTracker = 1;
                    }
                }
        }


        return collisionTracker;
    }

    //Handles the collision
    //Needs work for objects with no initial velocity
    //pushing other objects with no initial velocity
    public static void collide(GameObject A, GameObject B, int collisionType)
    {
        //Single detection
        if (A instanceof SpellObject || B instanceof SpellObject)
        {
            if (A instanceof SpellObject && !B.equals(((SpellObject)A).caster))
            {
                ((SpellObject) A).collisionEffect();
            }
            if (B instanceof SpellObject && !A.equals(((SpellObject)B).caster))
            {
                ((SpellObject) B).collisionEffect();
            }
        }


        else if (A instanceof Player || B instanceof Player)
        {
            A.collisionEffect(B);
            B.collisionEffect(A);

            if (A instanceof Player)
            {
                if (B.canMove())
                {
                    B.move(A.getVelocityX()/2,A.getVelocityZ()/2);
                    A.move(-A.getVelocityX()/2,-A.getVelocityZ()/2);
                }
                else
                {
                    A.move(-A.getVelocityX(), -A.getVelocityZ());
                }
            }
            else {
                ;
            }

            if (B instanceof Player)
            {
                if (A.canMove())
                {
                    A.move(B.getVelocityX()/2, B.getVelocityZ()/2);
                    B.move(-B.getVelocityX()/2, -B.getVelocityZ()/2);
                }
                else
                {
                    B.move(-B.getVelocityX(), -B.getVelocityZ());
                }
            }
            else {
                ;
            }
        }

        //They 'bump' into each other
        else
        {
            A.collisionEffect(B);
            B.collisionEffect(A);

            if (A.canMove() && (B.dX != 0 || B.dZ !=0))
            {
                A.mapX += B.dX;
                A.mapZ += B.dZ;
                A.getLoc();

            }

            else
            {
                A.mapX += -A.dX;
                A.mapZ += -A.dZ;
                A.getLoc();
            }

            if (B.canMove() && (A.dX != 0 || A.dZ !=0))
            {
                B.mapX += A.dX;
                B.mapZ += A.dZ;
                B.getLoc();
            }

            else
            {
                B.mapX += -B.dX;
                B.mapZ += -B.dZ;
                B.getLoc();
            }
        }



        //Double detection
        /*
        if (A instanceof Player || B instanceof Player)
        {
            if (A instanceof Player)
            {
                if (B.canMove())
                {
                    B.setVelocityX(A.getVelocityX()/2);
                    B.setVelocityZ(A.getVelocityZ()/2);
                    B.changeXZ();
                }
                else;
            }
            else;

            if (B instanceof Player)
            {
                if (A.canMove())
                {
                    A.setVelocityX(B.getVelocityX()/2);
                    A.setVelocityZ(B.getVelocityZ()/2);
                    A.changeXZ();
                }
                else;
            }
            else;
        }

        //They 'bump' into each other
        else
        {
            if (A.canMove() && B.dX != 0 || B.dZ !=0)
            {
                A.mapX += B.dX;
                A.mapZ += B.dZ;
                A.getLoc();
                System.out.print("1\n");

            }

            else
            {
                A.mapX += -A.dX;
                A.mapZ += -A.dZ;
                A.getLoc();
                System.out.print("2\n");
            }

            if (B.canMove() && A.dX != 0 || A.dZ !=0)
            {
                System.out.print("3\n");
                B.mapX += A.dX;
                B.mapZ += A.dZ;
                B.getLoc();
            }

            else
            {
                System.out.print("4\n");
                B.mapX += -B.dX;
                B.mapZ += -B.dZ;
                B.getLoc();
            }
        }
        */
    }

}


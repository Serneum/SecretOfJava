/***********************************************************************
 *Author: Chris Rees
 *Date: 4/27/09
 *File Name: Walls.java
 *Purpose: Creates walls and allows for random "doorways." Also provides
 *a way to guarantee a doorway when a player enters/changes rooms.
***********************************************************************/

package com.serneum.soj.object;

import java.util.Random;

import com.serneum.soj.map.ScreenCoord;

public class Walls
{
    private static int coord = 25, numRange = 0, rolled = 0, lastWall = 999, num = (ScreenCoord.mapMaxX / 25);
    public static int count = 0;
    private static String path = "", wallPath = "";
    private static Random rand = new Random();
    private static boolean imagesLoaded = false, isCopy = false;
    public static boolean ranOnce = false;

    //Create 2D arrays
    private static int[][] walls = new int[4][num], wallCopy = new int[4][num];
    private static GameObject[][] wallsDraw = new GameObject[4][num];

    public static void drawWalls()
    {
        if(ranOnce == false)
        {
            if(imagesLoaded == true)
            {
                //i represents walls, l represents wall image positioning
                //for i, 0 is left, 1 is top, 2 is right, 3 is bottom
                for(int i = 0; i < 4; i++)
                {
                    for(int l = 0; l < wallsDraw[i].length; l++)
                    {
                        if(isCopy == true && i == lastWall)
                        {
                            //Copy existing wall over from wallCopy into walls
                            walls[i][l] = wallCopy[i][l];

                            if(l == wallsDraw[i].length - 1) {
                                isCopy = false;
                            }
                        }
                        else
                        {
                            //If l is at the middle of the wall, rolls
                            //for a chance to create a "doorway" that is
                            //two spaces wide
                            if(l == num / 2)
                            {
                                numRange = roll();
                                rolled = rand.nextInt(100) + 1;

                                //If the roll was high enough, create doorway
                                //Also, if no other doors exist by the time
                                //that the bottom wall is being determined,
                                //create a doorway for a guaranteed exit
                                if(rolled > numRange || (i == 3 && count == 0))
                                {
                                    //don't draw
                                    walls[i][l] = 0;
                                    walls[i][++l] = 0;
                                    count++;
                                }

                                else
                                {
                                    walls[i][l] = 1;
                                    walls[i][++l] = 1;
                                }
                            }
                            else {
                                walls[i][l] = 1;
                            }
                        }
                    }

                }

                //Creates GameObjects out of the walls
                for(int i = 0; i < 4; i++)
                   {
                    for(int l = 0; l < walls[i].length; l++)
                       {
                           if(walls[i][l] == 1)
                           {
                               if(i == 0) {
                                wallsDraw[i][l] = new GameObject(wallPath, 25/2, l * coord + 25/2, 25, 25, 999);
                            }
                               if(i == 1) {
                                wallsDraw[i][l] = new GameObject(wallPath, l * coord + 25/2, 25/2, 25, 25, 999);
                            }
                               if(i == 2) {
                                wallsDraw[i][l] = new GameObject(wallPath, ScreenCoord.mapMaxX - 25/2, l * coord + 25/2, 25, 25, 999);
                            }
                               if(i == 3) {
                                wallsDraw[i][l] = new GameObject(wallPath, l * coord + 25/2, ScreenCoord.mapMaxY - 25/2, 25, 25, 999);
                            }
                        }

                           wallCopy[i][l] = walls[i][l];
                    }
                }

                ranOnce = true;
            }
            else
            {
                loadWall("1.png");
                imagesLoaded = true;
            }
        }
    }

    //Determines the number that the roll has to be higher than
    //to create a doorway. If other doors exist, the chances are
    //smaller that another will be created
    private static int roll()
    {
        if(count == 0) {
            return 50;
        }
        if(count == 1) {
            return 70;
        }
        if(count == 2) {
            return 80;
        }
        else {
            return 95;
        }
    }

    public static void loadWall(String path)
    {
        imagesLoaded = true;
        wallPath = "wall" + path;
    }

    public static void setImage(int r)
    {
        path = "" + r;
        loadWall(path);
    }

    public static void copy(int wall)
    {
        //Set the inverse wall so allow for "copying" doorways
        if(wall == 0) {
            lastWall = 2;
        }
        if(wall == 1) {
            lastWall = 3;
        }
        if(wall == 2) {
            lastWall = 0;
        }
        if(wall == 3) {
            lastWall = 1;
        }

        //Copy existing wall with opening into wallCopy for holding
        for(int l = 0; l < wallCopy[lastWall].length; l++) {
            wallCopy[lastWall][l] = walls[wall][l];
        }

        isCopy = true;
        //set count to 1 because there is a guaranteed opening already
        count = 1;
    }
}
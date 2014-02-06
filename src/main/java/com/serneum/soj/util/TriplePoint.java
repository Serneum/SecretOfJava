package com.serneum.soj.util;

public class TriplePoint
{
    float x, y, z;
    
    public TriplePoint (float a, float b, float c)
    {
        x = a;
        y = b;
        z = c;
    }
    public TriplePoint (int a, int b, int c)
    {
        x = a;
        y = b;
        z = c;
    }
    
    //Changes the first value (Length)
    public void setX(float newX)
    {
        x = newX;
    }
    
    //Changes the second value (Height)
    public void setY(float newY)
    {
        y = newY;
    }
    
    //Changes the third value (Depth)
    public void setZ(float newZ)
    {
        z = newZ;
    }
    
    public void setAll(float newX, float newY, float newZ)
    {
        x = newX;
        y = newY;
        z = newZ;
    }
    public float getX()
    {
        return x;
    }
    
    public float getY()
    {
        return y;
    }
    
    public float getZ()
    {
        return z;
    }
    
    public boolean inBetween(TriplePoint BottomRange, TriplePoint UpperRange)
    {        
        if ((this.getY() >= BottomRange.getY() && this.getY() <= UpperRange.getY()))
        {
            
            if ((this.getX() >= BottomRange.getX() && this.getX() <= UpperRange.getX()))
                {
                    if ((this.getZ() >= BottomRange.getZ() && this.getZ() <= UpperRange.getZ()))
                        return true;
                    
                    else return false;
                }
            else return false;
        }
        
        
        else return false;
    }
}
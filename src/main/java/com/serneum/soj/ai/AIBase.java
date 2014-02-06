/***********************************************************************
 *Author: Chris Rees
 *Date: 4/27/09
 *File Name: AIBase.java
 *Purpose: Interface for all AI files.
***********************************************************************/

package com.serneum.soj.ai;

import com.serneum.soj.creatures.Creature;

public interface AIBase
{
    void setAI();

    void movement();

    void attack(Creature obj);

    void moveX();

    void moveZ();

    void update();
}
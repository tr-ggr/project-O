package com.hydrozoa.pokemon.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Controller that interacts with what is in front of the player Actor.
 *
 * @author hydrozoa
 */
public class interactionController extends InputAdapter {

    private Actor a;

    public interactionController(Actor a) {
        this.a = a;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.X) {
           // interact with what is in front of the player

            return false;
        }
        return false;
    }

}
package com.mygdx.game.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;

public class XboxControllerListener extends ControllerAdapter {
    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        // Handle button press
//        System.out.println("Button " + buttonCode + " pressed!");
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        // Handle button release
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        // Handle axis movement
        return false;
    }
}
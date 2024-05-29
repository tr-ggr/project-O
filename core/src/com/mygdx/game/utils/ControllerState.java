package com.mygdx.game.utils;

import com.badlogic.gdx.controllers.Controllers;

public class ControllerState {
    private boolean wasPressed;
    private int buttonCode;

    public ControllerState(int buttonCode) {
        this.buttonCode = buttonCode;
        this.wasPressed = false;
    }

    public boolean isJustPressed() {
        boolean currentlyPressed = Controllers.getCurrent().getButton(buttonCode);
        boolean justPressed = !wasPressed && currentlyPressed;
        wasPressed = currentlyPressed;
        return justPressed;
    }
}
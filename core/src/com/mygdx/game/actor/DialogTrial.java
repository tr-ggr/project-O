package com.mygdx.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class DialogTrial extends Table {
    private Skin skin;

    public DialogTrial(Stage stage, String title, String message) {
        this.skin = new Skin(Gdx.files.internal("level_plane/level-plane-ui.json"));

        this.setFillParent(true);

        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.WHITE);
        bgPixmap.fill();
        TextureRegionDrawable textureRegionDrawableBg = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
        Table table = new Table();
        table.setBackground(textureRegionDrawableBg);
        this.add(table).fill().width(500).height(300);

        Label label = new Label(title, skin, "title-1");
        label.setFontScale(2f);
        table.add(label).center().expand();

        table.row();

        Label label2 = new Label(message, skin);
        label2.setWrap(true);
        label2.setFontScale(1.5f);
        label2.setAlignment(1);

        table.add(label2).fill().expand().padLeft(10).padRight(10);

        table.row();
        TextButton button = new TextButton("OK", skin);
        table.add(button).expand();

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.getRoot().removeActor(DialogTrial.this);
            }
        });

        this.row();
        this.pack();
    }
}

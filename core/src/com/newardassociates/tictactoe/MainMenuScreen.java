package com.newardassociates.tictactoe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * This class displays some options for the player to select
 * from: New Game, Credits, Help. (More?) Based on the user's
 * choice, move to that appropriate screen.
 */
public class MainMenuScreen extends ScreenAdapter {
    private static final String TAG = MainMenuScreen.class.getSimpleName();

    private static final float WORLD_WIDTH = 640;
    private static final float WORLD_HEIGHT = 480;

    private Stage stage;
    private BitmapFont bitmapFont;
    //private Texture backgroundTexture;
    private Skin skin;
    private SpriteBatch batch;

    private Game game;

    public MainMenuScreen(Game game) { 
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.app.log(TAG, "Showing");

        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

		//batch = new SpriteBatch();

		// A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
		// recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
		skin = new Skin();

		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		// Store the default libgdx font under the name "default".
		skin.add("default", new BitmapFont());

		// Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);

		// Create a table that fills the screen. Everything else will go inside this table.
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		// Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
		final TextButton button = new TextButton("Click me!", skin);
		table.add(button);

		// Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
		// Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
		// ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
		// revert the checked state.
		button.addListener(new ChangeListener(){
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Clicked! Is checked: " + button.isChecked());
				button.setText("Good job!");
			}
		});

		// Add an image actor. Have to set the size, else it would be the size of the drawable (which is the 1x1 texture).
		table.add(new Image(skin.newDrawable("white", Color.RED))).size(64);

        /*
        skin = new Skin();
        skin.add("default", new BitmapFont());

        bitmapFont = new BitmapFont();
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = bitmapFont;
        buttonStyle.fontColor = Color.RED;

        //backgroundTexture = new Texture(Gdx.files.internal("mainMenuBG.png"));
        //Image background = new Image(backgroundTexture);
        //stage.addActor(background);

        Label titleLabel = new Label("TIC-TAC-TOE", skin);
        titleLabel.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() / 12);
        titleLabel.setPosition(0, Gdx.graphics.getHeight() - (Gdx.graphics.getHeight()/12) * 2);
        titleLabel.setAlignment(Align.center);
        stage.addActor(titleLabel);
        */
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(TAG, "Resizing: " + width + "x" + height);

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        //backgroundTexture.dispose();
    }
}

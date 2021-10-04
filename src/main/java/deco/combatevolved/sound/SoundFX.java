package deco.combatevolved.sound;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;

public class SoundFX extends ApplicationAdapter {
    Sound sound;

    @Override
    public void create() {
        sound = Gdx.audio.newSound(Gdx.files.internal("resources/sounds/Background Music/BGM_BattlePlay_01_Inactive.mp3"));

//        long id = sound.play(1.0f, 1.0f, 0.0f);

        final long soundID = sound.loop(1.0f, 1.0f, 0.0f);

        Timer.schedule(new Timer.Task(){
            @Override
            public void run(){
                sound.pause(soundID);

                // global sound pause
//                sound.pause();

            }
            }, 10
        );


        // sound.setVolume(id, 1.0f);
        // sound.setPitch(id, 0.2f);
        // sound.setPan

    }

    @Override
    public void render() {

    }


}

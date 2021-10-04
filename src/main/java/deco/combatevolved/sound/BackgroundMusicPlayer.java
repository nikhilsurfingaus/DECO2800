package deco.combatevolved.sound;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Timer;

public class BackgroundMusicPlayer extends ApplicationAdapter{


    Music bgm1, bgm2;

    @Override
    public void create() {
        bgm1 = Gdx.audio.newMusic(Gdx.files.internal("resources/sounds/Background Music/BGM_SafeSituation_01_Inactive.mp3"));
        bgm2 = Gdx.audio.newMusic(Gdx.files.internal("resources/sounds/Background Music/BGM_NormalPlay_01_Inactive.mp3"));

        bgm1.play();

        bgm1.setOnCompletionListener(new Music.OnCompletionListener(){
            @Override
            public void onCompletion(Music music) {
                bgm2.play();
            }
        });

        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                if(bgm1.isPlaying())
                    if(bgm1.getPosition() >= 10.0f)
                        bgm1.setVolume(bgm1.getVolume() - 0.2f);
            }
        }, 10,1,4);
    }

    @Override
    public void render() {

    }

}

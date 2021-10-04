package deco.combatevolved.managers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager extends AbstractManager {

    private float musicVolume;

    private float sfxVolume;

    private Map<String, Sound> sounds = new ConcurrentHashMap<>();

    public void playSound(String soundName) {
        String key = "resources/sounds/" + soundName;
        if (!sounds.containsKey(key)) {
            sounds.put(
                    key,
                    Gdx.audio.newSound(
                            Gdx.files.internal(key)));
        }
        sounds.get(key).play(GameManager.get().getManager(SoundManager.class).getSfxVolume());
    }

    public void playEnemySound(String soundName) {
        String key = "resources/enemies/sounds/" + soundName;
        if (!sounds.containsKey(key)) {
            sounds.put(
                    key,
                    Gdx.audio.newSound(
                            Gdx.files.internal(key)));
        }
        sounds.get(key).play(GameManager.get().getManager(SoundManager.class).getSfxVolume());
    }

    public void playWeaponSound(String soundName) {
        String key = "resources/sounds/" + soundName;
        if (!sounds.containsKey(key)) {
            sounds.put(
                    key,
                    Gdx.audio.newSound(
                            Gdx.files.internal(key)));
        }
        sounds.get(key).play(GameManager.get().getManager(SoundManager.class).getSfxVolume());
    }

    public void playAnimationSound(String soundName) {
        String key = "resources/" + soundName;
        if (!sounds.containsKey(key)) {
            sounds.put(
                    key,
                    Gdx.audio.newSound(
                            Gdx.files.internal(key)));
        }
        sounds.get(key).play(GameManager.get().getManager(SoundManager.class).getSfxVolume());
    }

    public void playTowerSound(String soundName) {
        String key = "resources/sounds/towers/" + soundName;
        if (!sounds.containsKey(key)) {
            sounds.put(
                    key,
                    Gdx.audio.newSound(
                            Gdx.files.internal(key)));
        }
        sounds.get(key).play(GameManager.get().getManager(SoundManager.class).getSfxVolume());
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = volume;
    }

    public void setSfxVolume(float volume) {
        this.sfxVolume = volume;
    }

    public float getMusicVolume() {
        return this.musicVolume;
    }

    public float getSfxVolume() {
        return this.sfxVolume;
    }

    public void dispose() {
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
    }
}

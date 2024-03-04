package controllers;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TextToSpeech {
    public static void main(String[] args) {
        // Définir la propriété du système pour utiliser la voix "kevin16"
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        Voice voice;
        VoiceManager voiceManager = VoiceManager.getInstance();

        // Obtenir la voix "kevin16"
        voice = voiceManager.getVoice("kevin16");

        // Allouer la voix
        voice.allocate();

        // Faire parler la voix
        voice.speak("ahla bik");
    }
}

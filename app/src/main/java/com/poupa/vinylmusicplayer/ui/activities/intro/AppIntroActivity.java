package com.poupa.vinylmusicplayer.ui.activities.intro;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.poupa.vinylmusicplayer.R;

public class AppIntroActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonCtaVisible(true);
        setButtonNextVisible(false);
        setButtonBackVisible(false);

        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);

        addSlide(new SimpleSlide.Builder()
                .title(R.string.app_name)
                .description(R.string.welcome_to_vinyl_music_player)
                .image(R.mipmap.ic_music)
                .background(R.color.md_blue_grey_100)
                .backgroundDark(R.color.md_blue_grey_200)
                .build());
    }
}

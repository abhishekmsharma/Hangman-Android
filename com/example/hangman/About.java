package com.example.hangman;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

@SuppressLint({"NewApi"})
public class About extends Activity {
    public About() {
        Activity activity = this;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        View view = r6;
        View textView = new TextView(this);
        View textview = view;
        view = textview;
        ScrollingMovementMethod scrollingMovementMethod = r6;
        ScrollingMovementMethod scrollingMovementMethod2 = new ScrollingMovementMethod();
        view.setMovementMethod(scrollingMovementMethod);
        textview.setText("This is a simple Hangman game. Thank you for trying it out, I hope you like it.\n\n---------------------------\n\nSpecial Features:\n\n- Scores, wrong attempts left, attempted letters, and the hangman word all visible at a glance\n\n- Several categories of games to choose from (Cricket, Bollywood, World Countries, World Capitals and India Capitals)\n\n- Three different difficulty levels, namely, Easy, Normal, and Hard\n\n- An option to research on the word if the player fails to identify it\n\n- Quick toggle to enable and disable sound effects\n\n\n---------------------------\n\nThis is just the initial version of the game, and if people like it you can expect a lot more to come. If you want your custom database to be added to the game, then you can contact me on Twitter (@AbhishekMSharma).\nI'm still a learner, so the UI is basic. But still the game will keep you entertained due to many categories and difficulty levels.\n\nI would like to thank Madhavan Govind and Simon for providing me a base to work on the Cricket and Football databases respectively.\n\nRegards,\nAbhishek Sharma\n(@AbhishekMSharma)");
        setContentView(textview);
        if (VERSION.SDK_INT >= 11) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Activity this = this;
        MenuItem item = menuItem;
        switch (item.getItemId()) {
            case 16908332:
                NavUtils.navigateUpFromSameTask(this);
                return 1;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

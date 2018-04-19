package com.example.hangman;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.internal.view.SupportMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import bolts.Task;
import com.madsharma.hangman.C0064R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class MainActivity extends Activity {
    int adminMode;
    String category;
    int countDone;
    int countDoneNames;
    public int diff = 5;
    int[] doneNames;
    public int enterPressed = 0;
    public int hp = 0;
    boolean loaded = false;
    private Handler mHandler;
    int newLength;
    int scoreScored;
    char[] selected;
    String selectedLetters;
    private int soundID;
    private int soundID2;
    int soundOn;
    private SoundPool soundPool;
    String temp;
    String temp2;
    int wrongLeft;

    class C00551 implements OnKeyListener {
        C00551() {
            C00551 c00551 = this;
        }

        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            C00551 this = this;
            View v = view;
            int keyCode = i;
            if (keyEvent.getAction() == 0) {
                switch (keyCode) {
                    case 23:
                    case 66:
                        try {
                            MainActivity.this.enterPressed = 1;
                            MainActivity.this.checkValue(v);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return 1;
                }
            }
            return null;
        }
    }

    class C00584 implements OnLoadCompleteListener {
        C00584() {
            C00584 c00584 = this;
        }

        public void onLoadComplete(SoundPool soundPool, int i, int i2) {
            SoundPool soundPool2 = soundPool;
            int sampleId = i;
            int status = i2;
            MainActivity.this.loaded = true;
        }
    }

    class C00606 implements OnClickListener {
        C00606() {
            C00606 c00606 = this;
        }

        public void onClick(DialogInterface dialog, int whichButton) {
        }
    }

    class C00628 implements OnClickListener {
        C00628() {
            C00628 c00628 = this;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            int which = i;
            dialogInterface.dismiss();
        }
    }

    public MainActivity() {
        Activity activity = this;
        Handler handler = r4;
        Handler handler2 = new Handler();
        this.mHandler = handler;
        this.adminMode = 0;
        this.temp2 = "";
        this.newLength = 0;
        this.countDone = 0;
        this.temp = "";
        this.soundOn = 1;
        this.wrongLeft = this.diff;
        this.selected = new char[26];
        this.selectedLetters = "";
        this.scoreScored = 0;
        this.category = "cricket.txt";
        this.countDoneNames = 0;
        this.doneNames = new int[300];
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0064R.layout.activity_main);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "hcApB7Y3rvQxVbe0wz42ZtiQcQVzhzcUNCShATtY", "g84mI3PnGmVp4ZOuBnFcB6vwVqz5OCeGXM6SJKFr");
        ParseObject parseObject = r13;
        ParseObject parseObject2 = new ParseObject("TestObject");
        ParseObject testObject = parseObject;
        testObject.put("foo", "bar");
        Task saveInBackground = testObject.saveInBackground();
        EditText editText = (EditText) findViewById(C0064R.id.editText1);
        C00551 c00551 = r13;
        C00551 c005512 = new C00551();
        editText.setOnKeyListener(c00551);
        ImageButton button2 = (ImageButton) findViewById(C0064R.id.button2);
        ImageButton imageButton = button2;
        C00562 c00562 = r13;
        final ImageButton imageButton2 = button2;
        C00562 c005622 = new OnTouchListener(this) {
            float TheImpactPointX;
            float TheImpactPointY;
            final /* synthetic */ MainActivity this$0;
            View f3v = null;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                C00562 this = this;
                View arg0 = view;
                MotionEvent me = motionEvent;
                if (me.getAction() == 0) {
                    this.TheImpactPointX = me.getX();
                    this.TheImpactPointY = me.getY();
                    imageButton2.setColorFilter(Color.argb(150, ParseException.REQUEST_LIMIT_EXCEEDED, ParseException.REQUEST_LIMIT_EXCEEDED, ParseException.REQUEST_LIMIT_EXCEEDED));
                    return 1;
                } else if (me.getAction() != 1) {
                    return null;
                } else {
                    float distanceX = this.TheImpactPointX - me.getX();
                    float distanceY = this.TheImpactPointY - me.getY();
                    if ((distanceX <= 15.0f && distanceX >= -15.0f) || (distanceY <= 15.0f && distanceY >= -15.0f)) {
                        try {
                            this.this$0.checkValue(this.f3v);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    imageButton2.setColorFilter(Color.argb(0, ParseException.REQUEST_LIMIT_EXCEEDED, ParseException.REQUEST_LIMIT_EXCEEDED, ParseException.REQUEST_LIMIT_EXCEEDED));
                    return 1;
                }
            }
        };
        imageButton.setOnTouchListener(c00562);
        ImageButton ikib = (ImageButton) findViewById(C0064R.id.ikib);
        imageButton = ikib;
        C00573 c00573 = r13;
        imageButton2 = ikib;
        C00573 c005732 = new OnTouchListener(this) {
            float TheImpactPointX;
            float TheImpactPointY;
            final /* synthetic */ MainActivity this$0;
            View f4v = null;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                C00573 this = this;
                View arg0 = view;
                MotionEvent me = motionEvent;
                if (me.getAction() == 0) {
                    this.TheImpactPointX = me.getX();
                    this.TheImpactPointY = me.getY();
                    imageButton2.setColorFilter(Color.argb(150, ParseException.REQUEST_LIMIT_EXCEEDED, ParseException.REQUEST_LIMIT_EXCEEDED, ParseException.REQUEST_LIMIT_EXCEEDED));
                    return 1;
                } else if (me.getAction() != 1) {
                    return null;
                } else {
                    float distanceX = this.TheImpactPointX - me.getX();
                    float distanceY = this.TheImpactPointY - me.getY();
                    if ((distanceX <= 15.0f && distanceX >= -15.0f) || (distanceY <= 15.0f && distanceY >= -15.0f)) {
                        this.this$0.iKnowIt();
                    }
                    imageButton2.setColorFilter(Color.argb(0, ParseException.REQUEST_LIMIT_EXCEEDED, ParseException.REQUEST_LIMIT_EXCEEDED, ParseException.REQUEST_LIMIT_EXCEEDED));
                    return 1;
                }
            }
        };
        imageButton.setOnTouchListener(c00573);
        newGame(null);
        setVolumeControlStream(3);
        SoundPool soundPool = r13;
        SoundPool soundPool2 = new SoundPool(10, 3, 0);
        this.soundPool = soundPool;
        SoundPool soundPool3 = this.soundPool;
        C00584 c00584 = r13;
        C00584 c005842 = new C00584();
        soundPool3.setOnLoadCompleteListener(c00584);
        this.soundID = this.soundPool.load(this, C0064R.raw.yn, 1);
        this.soundID2 = this.soundPool.load(this, C0064R.raw.n, 2);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0064R.menu.activity_main, menu);
        return 1;
    }

    public void iKnowIt() {
        Builder builder = r9;
        Builder builder2 = new Builder(this);
        Builder alert = builder;
        builder = alert.setTitle("Enter the word");
        builder = alert.setMessage("Even if you are wrong, the game won't get over!");
        View view = r9;
        View editText = new EditText(this);
        View input = view;
        builder = alert.setView(input);
        C00595 c00595 = r9;
        final View view2 = input;
        C00595 c005952 = new OnClickListener(this) {
            final /* synthetic */ MainActivity this$0;

            public void onClick(DialogInterface dialogInterface, int i) {
                DialogInterface dialog = dialogInterface;
                int whichButton = i;
                this.this$0.checkIKnowIt(view2.getText().toString());
            }
        };
        builder = alert.setPositiveButton("Submit", c00595);
        C00606 c00606 = r9;
        C00606 c006062 = new C00606();
        builder = alert.setNegativeButton("Cancel", c00606);
        AlertDialog show = alert.show();
    }

    public void checkIKnowIt(String str) {
        float volume;
        if (str.toUpperCase().equals(this.temp)) {
            if (this.soundOn == 1) {
                AudioManager audioManager = (AudioManager) getSystemService("audio");
                volume = ((float) audioManager.getStreamVolume(3)) / ((float) audioManager.getStreamMaxVolume(3));
                if (this.loaded) {
                    int play = this.soundPool.play(this.soundID, volume, volume, 1, 0, 1.0f);
                }
            }
            ((TextView) findViewById(C0064R.id.textView1)).setText(this.temp);
            correctlyGuessed();
            return;
        }
        if (this.soundOn == 1) {
            audioManager = (AudioManager) getSystemService("audio");
            volume = ((float) audioManager.getStreamVolume(3)) / ((float) audioManager.getStreamMaxVolume(3));
            if (this.loaded) {
                play = this.soundPool.play(this.soundID2, volume, volume, 1, 0, 1.0f);
            }
        }
        Toast.makeText(this, "Sorry! You don't know it yet", 0).show();
    }

    public void giveCategory() {
        View v = null;
        Builder builder = r11;
        Builder builder2 = new Builder(this);
        Builder builderSingle = builder;
        builder = builderSingle.setIcon(C0064R.drawable.ic_launcher);
        builder = builderSingle.setTitle("Select Category");
        ArrayAdapter<String> arrayAdapter = r11;
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter(this, 17367057);
        ArrayAdapter<String> arrayAdapter3 = arrayAdapter;
        arrayAdapter3.add("Cricket");
        arrayAdapter3.add("Bollywood");
        arrayAdapter3.add("Countries");
        arrayAdapter3.add("World Capitals");
        arrayAdapter3.add("India Capitals");
        arrayAdapter3.add("Football");
        builder = builderSingle;
        arrayAdapter2 = arrayAdapter3;
        C00617 c00617 = r11;
        final ArrayAdapter<String> arrayAdapter4 = arrayAdapter3;
        final View view = v;
        C00617 c006172 = new OnClickListener(this) {
            final /* synthetic */ MainActivity this$0;

            public void onClick(DialogInterface dialogInterface, int i) {
                DialogInterface dialog = dialogInterface;
                String strName = (String) arrayAdapter4.getItem(i);
                if (strName.equals("Cricket")) {
                    this.this$0.category = "cricket.txt";
                    this.this$0.newGame(view);
                } else if (strName.equals("Bollywood")) {
                    this.this$0.category = "bollywood.txt";
                    this.this$0.newGame(view);
                } else if (strName.equals("Countries")) {
                    this.this$0.category = "countries.txt";
                    this.this$0.newGame(view);
                } else if (strName.equals("World Capitals")) {
                    this.this$0.category = "world_capitals.txt";
                    this.this$0.newGame(view);
                } else if (strName.equals("India Capitals")) {
                    this.this$0.category = "india_capitals.txt";
                    this.this$0.newGame(view);
                } else if (strName.equals("Football")) {
                    this.this$0.category = "football.txt";
                    this.this$0.newGame(view);
                }
            }
        };
        builder = builder.setAdapter(arrayAdapter2, c00617);
        AlertDialog show = builderSingle.show();
    }

    public void giveSound() {
        Builder builder = r9;
        Builder builder2 = new Builder(this);
        Builder builderSingle = builder;
        builder = builderSingle.setIcon(C0064R.drawable.ic_launcher);
        builder = builderSingle.setTitle("Sound");
        ArrayAdapter<String> arrayAdapter = r9;
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter(this, 17367057);
        ArrayAdapter<String> arrayAdapter3 = arrayAdapter;
        arrayAdapter3.add("On");
        arrayAdapter3.add("Off");
        C00628 c00628 = r9;
        C00628 c006282 = new C00628();
        builder = builderSingle.setNegativeButton("Cancel", c00628);
        builder = builderSingle;
        arrayAdapter2 = arrayAdapter3;
        C00639 c00639 = r9;
        final ArrayAdapter<String> arrayAdapter4 = arrayAdapter3;
        C00639 c006392 = new OnClickListener(this) {
            final /* synthetic */ MainActivity this$0;

            public void onClick(DialogInterface dialogInterface, int i) {
                DialogInterface dialog = dialogInterface;
                String strName = (String) arrayAdapter4.getItem(i);
                if (strName.equals("On")) {
                    this.this$0.soundOn = 1;
                } else if (strName.equals("Off")) {
                    this.this$0.soundOn = 0;
                }
            }
        };
        builder = builder.setAdapter(arrayAdapter2, c00639);
        AlertDialog show = builderSingle.show();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Context this = this;
        MenuItem item = menuItem;
        View v = null;
        switch (item.getItemId()) {
            case C0064R.id.menu_settings:
                Intent intent = r8;
                Intent intent2 = new Intent(this, About.class);
                startActivity(intent);
                return 1;
            case C0064R.id.new_game:
                newGame(v);
                return 1;
            case C0064R.id.menu_category:
                giveCategory();
                return 1;
            case C0064R.id.menu_Difficulty:
                giveDifficulty();
                return 1;
            case C0064R.id.menu_Sound:
                giveSound();
                return 1;
            case C0064R.id.menu_changeFont:
                giveFont();
                return 1;
            case C0064R.id.menu_HangmanP:
                givePopups();
                return 1;
            case C0064R.id.menu_admin:
                break;
            case C0064R.id.menu_exit:
                finish();
                System.exit(0);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        giveAdminMode();
        return 1;
    }

    public void giveFont() {
        View v = null;
        Builder builder = r10;
        Builder builder2 = new Builder(this);
        Builder builderSingle = builder;
        builder = builderSingle.setIcon(C0064R.drawable.ic_launcher);
        builder = builderSingle.setTitle("Hangman Popups");
        ArrayAdapter<String> arrayAdapter = r10;
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter(this, 17367057);
        ArrayAdapter<String> arrayAdapter3 = arrayAdapter;
        arrayAdapter3.add("Android Default");
        arrayAdapter3.add("Eraser");
        arrayAdapter3.add("Forte");
        arrayAdapter3.add("Park");
        builder = builderSingle;
        arrayAdapter2 = arrayAdapter3;
        AnonymousClass10 anonymousClass10 = r10;
        final ArrayAdapter<String> arrayAdapter4 = arrayAdapter3;
        AnonymousClass10 anonymousClass102 = new OnClickListener(this) {
            final /* synthetic */ MainActivity this$0;

            public void onClick(DialogInterface dialogInterface, int i) {
                TextView tvtoChangeFont;
                Typeface face;
                DialogInterface dialog = dialogInterface;
                String strName = (String) arrayAdapter4.getItem(i);
                if (strName.equals("Android Default")) {
                    tvtoChangeFont = (TextView) this.this$0.findViewById(C0064R.id.textView2);
                    face = Typeface.DEFAULT;
                    tvtoChangeFont.setTypeface(face);
                    ((TextView) this.this$0.findViewById(C0064R.id.textView5)).setTypeface(face);
                    ((TextView) this.this$0.findViewById(C0064R.id.textView1)).setTypeface(face);
                    ((TextView) this.this$0.findViewById(C0064R.id.textView4)).setTypeface(face);
                } else if (strName.equals("Eraser")) {
                    tvtoChangeFont = (TextView) this.this$0.findViewById(C0064R.id.textView2);
                    face = Typeface.createFromAsset(this.this$0.getAssets(), "fonts/EraserRegular.ttf");
                    tvtoChangeFont.setTypeface(face);
                    ((TextView) this.this$0.findViewById(C0064R.id.textView5)).setTypeface(face);
                    ((TextView) this.this$0.findViewById(C0064R.id.textView1)).setTypeface(face);
                    ((TextView) this.this$0.findViewById(C0064R.id.textView4)).setTypeface(face);
                } else if (strName.equals("Forte")) {
                    tvtoChangeFont = (TextView) this.this$0.findViewById(C0064R.id.textView2);
                    face = Typeface.createFromAsset(this.this$0.getAssets(), "fonts/FORTE.TTF");
                    tvtoChangeFont.setTypeface(face);
                    ((TextView) this.this$0.findViewById(C0064R.id.textView5)).setTypeface(face);
                    ((TextView) this.this$0.findViewById(C0064R.id.textView1)).setTypeface(face);
                    ((TextView) this.this$0.findViewById(C0064R.id.textView4)).setTypeface(face);
                }
                if (strName.equals("Park")) {
                    tvtoChangeFont = (TextView) this.this$0.findViewById(C0064R.id.textView2);
                    face = Typeface.createFromAsset(this.this$0.getAssets(), "fonts/SHOWG.TTF");
                    tvtoChangeFont.setTypeface(face);
                    ((TextView) this.this$0.findViewById(C0064R.id.textView5)).setTypeface(face);
                    ((TextView) this.this$0.findViewById(C0064R.id.textView1)).setTypeface(face);
                    ((TextView) this.this$0.findViewById(C0064R.id.textView4)).setTypeface(face);
                }
            }
        };
        builder = builder.setAdapter(arrayAdapter2, anonymousClass10);
        AlertDialog show = builderSingle.show();
    }

    public void givePopups() {
        View v = null;
        Builder builder = r10;
        Builder builder2 = new Builder(this);
        Builder builderSingle = builder;
        builder = builderSingle.setIcon(C0064R.drawable.ic_launcher);
        builder = builderSingle.setTitle("Hangman Popups");
        ArrayAdapter<String> arrayAdapter = r10;
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter(this, 17367057);
        ArrayAdapter<String> arrayAdapter3 = arrayAdapter;
        arrayAdapter3.add("Enable");
        arrayAdapter3.add("Disable");
        builder = builderSingle;
        arrayAdapter2 = arrayAdapter3;
        AnonymousClass11 anonymousClass11 = r10;
        final ArrayAdapter<String> arrayAdapter4 = arrayAdapter3;
        AnonymousClass11 anonymousClass112 = new OnClickListener(this) {
            final /* synthetic */ MainActivity this$0;

            public void onClick(DialogInterface dialogInterface, int i) {
                DialogInterface dialog = dialogInterface;
                String strName = (String) arrayAdapter4.getItem(i);
                if (strName.equals("Enable")) {
                    this.this$0.hp = 1;
                } else if (strName.equals("Disable")) {
                    this.this$0.hp = 0;
                }
            }
        };
        builder = builder.setAdapter(arrayAdapter2, anonymousClass11);
        AlertDialog show = builderSingle.show();
    }

    public void giveDifficulty() {
        View v = null;
        Builder builder = r11;
        Builder builder2 = new Builder(this);
        Builder builderSingle = builder;
        builder = builderSingle.setIcon(C0064R.drawable.ic_launcher);
        builder = builderSingle.setTitle("Choose Difficulty (Will start a new game)");
        ArrayAdapter<String> arrayAdapter = r11;
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter(this, 17367057);
        ArrayAdapter<String> arrayAdapter3 = arrayAdapter;
        arrayAdapter3.add("Easy");
        arrayAdapter3.add("Normal (Default)");
        arrayAdapter3.add("Hard");
        builder = builderSingle;
        arrayAdapter2 = arrayAdapter3;
        AnonymousClass12 anonymousClass12 = r11;
        final ArrayAdapter<String> arrayAdapter4 = arrayAdapter3;
        final View view = v;
        AnonymousClass12 anonymousClass122 = new OnClickListener(this) {
            final /* synthetic */ MainActivity this$0;

            public void onClick(DialogInterface dialogInterface, int i) {
                DialogInterface dialog = dialogInterface;
                String strName = (String) arrayAdapter4.getItem(i);
                if (strName.equals("Easy")) {
                    this.this$0.diff = 8;
                    this.this$0.newGame(view);
                } else if (strName.equals("Normal (Default)")) {
                    this.this$0.diff = 5;
                    this.this$0.newGame(view);
                } else if (strName.equals("Hard")) {
                    this.this$0.diff = 3;
                    this.this$0.newGame(view);
                }
            }
        };
        builder = builder.setAdapter(arrayAdapter2, anonymousClass12);
        AlertDialog show = builderSingle.show();
    }

    public void giveAdminMode() {
        Builder builder = r9;
        Builder builder2 = new Builder(this);
        Builder alert = builder;
        builder = alert.setTitle("Enter password");
        builder = alert.setMessage("Enter password for Admin Mode");
        View view = r9;
        View editText = new EditText(this);
        View input = view;
        builder = alert.setView(input);
        input.setInputType(129);
        input.setSelection(input.getText().length());
        AnonymousClass13 anonymousClass13 = r9;
        final View view2 = input;
        AnonymousClass13 anonymousClass132 = new OnClickListener(this) {
            final /* synthetic */ MainActivity this$0;

            public void onClick(DialogInterface dialogInterface, int i) {
                DialogInterface dialog = dialogInterface;
                int whichButton = i;
                this.this$0.checkAdminRights(view2.getText().toString());
            }
        };
        builder = alert.setPositiveButton("Submit", anonymousClass13);
        AnonymousClass14 anonymousClass14 = r9;
        AnonymousClass14 anonymousClass142 = new OnClickListener() {
            {
                AnonymousClass14 anonymousClass14 = this;
            }

            public void onClick(DialogInterface dialog, int whichButton) {
            }
        };
        builder = alert.setNegativeButton("Cancel", anonymousClass14);
        AlertDialog show = alert.show();
    }

    public void checkAdminRights(String str) {
        Context this = this;
        if (str.equals("thereisnopassword")) {
            Toast.makeText(this, "Correct Password", 0).show();
            this.adminMode = 1;
            return;
        }
        Toast.makeText(this, "Incorrect Password", 0).show();
        this.adminMode = 0;
    }

    public void newGame(View view) {
        View v = view;
        ((TextView) findViewById(C0064R.id.textView5)).setText("Score: 0");
        ((EditText) findViewById(C0064R.id.editText1)).setEnabled(true);
        Button bnew = (Button) findViewById(C0064R.id.buttonNew);
        bnew.setText("Next Name");
        Button button = bnew;
        AnonymousClass15 anonymousClass15 = r13;
        AnonymousClass15 anonymousClass152 = new View.OnClickListener() {
            final View f1v = null;

            {
                AnonymousClass15 anonymousClass15 = this;
            }

            public void onClick(View view) {
                View arg0 = view;
                MainActivity.this.readFile(this.f1v);
            }
        };
        button.setOnClickListener(anonymousClass15);
        if (this.category.equals("cricket.txt")) {
            Toast.makeText(this, "New Game - Category: Cricket", 0).show();
        } else if (this.category.equals("bollywood.txt")) {
            Toast.makeText(this, "New Game - Category: Bollywood", 0).show();
        } else if (this.category.equals("countries.txt")) {
            Toast.makeText(this, "New Game - Category: Countries", 0).show();
        } else if (this.category.equals("world_capitals.txt")) {
            Toast.makeText(this, "New Game - Category: World Capitals", 0).show();
        } else if (this.category.equals("india_capitals.txt")) {
            Toast.makeText(this, "New Game - Category: India Capitals", 0).show();
        } else if (this.category.equals("football.txt")) {
            Toast.makeText(this, "New Game - Category: Football", 0).show();
        }
        ((ImageButton) findViewById(C0064R.id.button2)).setVisibility(0);
        ((Button) findViewById(C0064R.id.goButton)).setVisibility(0);
        ((ImageButton) findViewById(C0064R.id.ikib)).setVisibility(0);
        this.temp = "";
        this.temp2 = "";
        this.scoreScored = 0;
        this.wrongLeft = this.diff;
        this.selectedLetters = "";
        this.countDoneNames = 0;
        for (int i = 0; i < 26; i++) {
            this.selected[i] = '0';
        }
        this.countDone = 0;
        readFile(v);
    }

    public void readFile(View view) {
        View v = view;
        EditText et = (EditText) findViewById(C0064R.id.editText1);
        et.setEnabled(true);
        et.setHint("Enter next character");
        ((TextView) findViewById(C0064R.id.textView1)).setTextColor(SupportMenu.CATEGORY_MASK);
        et.setEnabled(true);
        this.countDone = 0;
        for (int i = 0; i < 26; i++) {
            this.selected[i] = '0';
        }
        this.selectedLetters = "";
        TextView sc = (TextView) findViewById(C0064R.id.textView5);
        this.wrongLeft = this.diff;
        TextView textView = (TextView) findViewById(C0064R.id.textView4);
        StringBuilder stringBuilder = r21;
        StringBuilder stringBuilder2 = new StringBuilder();
        textView.setText(stringBuilder.append("Wrong Left: ").append(this.wrongLeft).toString());
        ((TextView) findViewById(C0064R.id.textView2)).setText("");
        String s = "";
        ((ImageButton) findViewById(C0064R.id.button2)).setVisibility(0);
        ((Button) findViewById(C0064R.id.goButton)).setVisibility(0);
        ((ImageButton) findViewById(C0064R.id.ikib)).setVisibility(0);
        ((Button) findViewById(C0064R.id.buttonNew)).setVisibility(4);
        s = "";
        try {
            BufferedReader bufferedReader = r21;
            Reader reader = r21;
            Reader inputStreamReader = new InputStreamReader(getAssets().open(this.category));
            BufferedReader bufferedReader2 = new BufferedReader(reader);
            BufferedReader in = bufferedReader;
            while (true) {
                String readLine = in.readLine();
                String str = readLine;
                if (readLine == null) {
                    break;
                }
                StringBuilder stringBuilder3 = r21;
                stringBuilder = new StringBuilder();
                s = stringBuilder3.append(s).append(str).toString();
            }
            in.close();
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (IOException e2) {
            System.out.println(e2.getMessage());
        } catch (Exception e3) {
            System.out.println(e3.getMessage());
        }
        displayName(s);
    }

    public void displayName(String str) {
        String DB = str;
        this.temp2 = "";
        this.temp = "";
        TextView et1 = (TextView) findViewById(C0064R.id.textView1);
        Random random = r22;
        Random random2 = new Random();
        Random r = random;
        int n = 2;
        int i = 0;
        int count = 0;
        int countStars = 0;
        n = DB.length();
        for (i = 0; i < n; i++) {
            if (DB.substring(i, i + 1).equals("*")) {
                countStars++;
            }
        }
        if (this.scoreScored == countStars) {
            View v = null;
            Builder builder = r22;
            Builder builder2 = new Builder(this);
            Builder alertDialog = builder;
            builder = alertDialog;
            StringBuilder stringBuilder = r22;
            StringBuilder stringBuilder2 = new StringBuilder();
            builder = builder.setTitle(stringBuilder.append("Score: ").append(this.scoreScored).toString());
            builder = alertDialog.setMessage("Well done!! You have successfully completed this category. \nYou are a genius");
            AnonymousClass16 anonymousClass16 = r22;
            AnonymousClass16 anonymousClass162 = new OnClickListener() {
                {
                    AnonymousClass16 anonymousClass16 = this;
                }

                public void onClick(DialogInterface dialog, int which) {
                }
            };
            builder = alertDialog.setPositiveButton("Start a new game", anonymousClass16);
            AlertDialog show = alertDialog.show();
            newGame(v);
            return;
        }
        countStars -= 2;
        int n2 = -1 + ((int) (Math.random() * ((double) ((countStars + 1) + 1))));
        int checkIt = -1;
        do {
            checkIt = checkN2(n2);
            if (checkIt == 0) {
                n2 = -1 + ((int) (Math.random() * ((double) ((countStars + 1) + 1))));
            }
        } while (checkIt != 1);
        this.doneNames[this.countDoneNames] = n2;
        this.countDoneNames++;
        i = 0;
        n = DB.length();
        while (i < n && count <= n2) {
            if (DB.substring(i, i + 1).equals("*")) {
                count++;
            }
            i++;
        }
        char c = 'a';
        while (c != '*') {
            c = DB.charAt(i);
            String c2 = DB.substring(i, i + 1);
            stringBuilder = r22;
            stringBuilder2 = new StringBuilder();
            this.temp = stringBuilder.append(this.temp).append(c2).toString();
            i++;
        }
        if (this.adminMode == 1) {
            Toast.makeText(this, this.temp, 0).show();
        }
        this.temp = this.temp.substring(0, this.temp.length() - 1);
        int newLength = this.temp.length();
        char[] temp2ch = this.temp.toCharArray();
        char[] tempch = this.temp.toCharArray();
        for (int j = 0; j < newLength; j++) {
            if (tempch[j] != ' ') {
                temp2ch[j] = '-';
            } else {
                temp2ch[j] = '/';
            }
        }
        this.temp2 = String.valueOf(temp2ch);
        et1.setText(this.temp2);
    }

    public int checkN2(int i) {
        MainActivity this = this;
        int c = i;
        for (int i2 = 0; i2 < this.countDoneNames; i2++) {
            if (this.doneNames[i2] == c) {
                return null;
            }
        }
        return 1;
    }

    public void checkValue(View view) throws IOException {
        View v = view;
        int flag = 0;
        TextView et1 = (TextView) findViewById(C0064R.id.textView1);
        EditText eT1 = (EditText) findViewById(C0064R.id.editText1);
        String in = eT1.getText().toString();
        Handler handler;
        final EditText editText;
        boolean post;
        if (in.matches("")) {
            Toast.makeText(this, "Please enter a character", 0).show();
            if (this.enterPressed == 1) {
                EditText yourEditText = (EditText) findViewById(C0064R.id.editText1);
                handler = this.mHandler;
                AnonymousClass17 anonymousClass17 = r27;
                editText = yourEditText;
                AnonymousClass17 anonymousClass172 = new Runnable(this) {
                    final /* synthetic */ MainActivity this$0;

                    public void run() {
                        ((InputMethodManager) this.this$0.getSystemService("input_method")).toggleSoftInputFromWindow(editText.getApplicationWindowToken(), 2, 0);
                        boolean requestFocus = editText.requestFocus();
                    }
                };
                post = handler.post(anonymousClass17);
            }
        } else {
            in = in.toUpperCase();
            eT1.setText("");
            char takeInput = in.charAt(0);
            EditText yourEditText2;
            if (!Character.isLetter(in.charAt(0)) || in.matches("")) {
                Toast.makeText(this, "Only characters input", 1).show();
                if (this.enterPressed == 1) {
                    yourEditText2 = (EditText) findViewById(C0064R.id.editText1);
                    handler = this.mHandler;
                    AnonymousClass18 anonymousClass18 = r27;
                    editText = yourEditText2;
                    AnonymousClass18 anonymousClass182 = new Runnable(this) {
                        final /* synthetic */ MainActivity this$0;

                        public void run() {
                            ((InputMethodManager) this.this$0.getSystemService("input_method")).toggleSoftInputFromWindow(editText.getApplicationWindowToken(), 2, 0);
                            boolean requestFocus = editText.requestFocus();
                        }
                    };
                    post = handler.post(anonymousClass18);
                }
            } else {
                for (int i = 0; i < this.countDone; i++) {
                    if (this.selected[i] == takeInput) {
                        flag = 1;
                        break;
                    }
                }
                StringBuilder stringBuilder;
                StringBuilder stringBuilder2;
                if (flag == 1) {
                    stringBuilder = r27;
                    stringBuilder2 = new StringBuilder();
                    Toast.makeText(this, stringBuilder.append("You have already selected ").append(takeInput).toString(), 0).show();
                    if (this.enterPressed == 1) {
                        yourEditText2 = (EditText) findViewById(C0064R.id.editText1);
                        handler = this.mHandler;
                        AnonymousClass19 anonymousClass19 = r27;
                        editText = yourEditText2;
                        AnonymousClass19 anonymousClass192 = new Runnable(this) {
                            final /* synthetic */ MainActivity this$0;

                            public void run() {
                                ((InputMethodManager) this.this$0.getSystemService("input_method")).toggleSoftInputFromWindow(editText.getApplicationWindowToken(), 2, 0);
                                boolean requestFocus = editText.requestFocus();
                            }
                        };
                        post = handler.post(anonymousClass19);
                    }
                } else {
                    this.selected[this.countDone] = takeInput;
                    this.countDone++;
                    String tempToAdd = String.valueOf(takeInput);
                    stringBuilder = r27;
                    stringBuilder2 = new StringBuilder();
                    this.selectedLetters = stringBuilder.append(this.selectedLetters).append(" - ").append(tempToAdd).toString();
                    ((TextView) findViewById(C0064R.id.textView2)).setText(this.selectedLetters);
                    char[] tempChars = this.temp.toCharArray();
                    char[] temp2Chars = this.temp2.toCharArray();
                    String tempo = "";
                    int j = 0;
                    int goodGuessFlag = 0;
                    for (j = 0; j < this.temp2.length(); j++) {
                        if (tempChars[j] == takeInput) {
                            temp2Chars[j] = takeInput;
                            this.temp2 = String.valueOf(temp2Chars);
                            StringBuilder stringBuilder3 = r27;
                            stringBuilder = new StringBuilder();
                            tempo = stringBuilder3.append(tempo).append(this.temp2).append("new").toString();
                            goodGuessFlag = 1;
                        }
                    }
                    AudioManager audioManager;
                    float volume;
                    int play;
                    if (goodGuessFlag == 0) {
                        this.wrongLeft--;
                        if (this.soundOn == 1) {
                            audioManager = (AudioManager) getSystemService("audio");
                            volume = ((float) audioManager.getStreamVolume(3)) / ((float) audioManager.getStreamMaxVolume(3));
                            if (this.loaded) {
                                play = this.soundPool.play(this.soundID2, volume, volume, 1, 0, 1.0f);
                            }
                        }
                        if (this.wrongLeft != 0) {
                            if (this.hp != 1) {
                                Toast.makeText(this, "Oops! That was a wrong guess", 1).show();
                            } else if (this.wrongLeft == 7) {
                                view = getLayoutInflater().inflate(C0064R.layout.cust_toast_layout, (ViewGroup) findViewById(C0064R.id.relativeLayout7));
                                r20 = r27;
                                r21 = new Toast(this);
                                toast = r20;
                                toast.setView(view);
                                toast.show();
                            } else if (this.wrongLeft == 6) {
                                view = getLayoutInflater().inflate(C0064R.layout.cust_toast_layout6, (ViewGroup) findViewById(C0064R.id.relativeLayout6));
                                r20 = r27;
                                r21 = new Toast(this);
                                toast = r20;
                                toast.setView(view);
                                toast.show();
                            } else if (this.wrongLeft == 5) {
                                view = getLayoutInflater().inflate(C0064R.layout.cust_toast_layout5, (ViewGroup) findViewById(C0064R.id.relativeLayout5));
                                r20 = r27;
                                r21 = new Toast(this);
                                toast = r20;
                                toast.setView(view);
                                toast.show();
                            } else if (this.wrongLeft == 4) {
                                view = getLayoutInflater().inflate(C0064R.layout.cust_toast_layout4, (ViewGroup) findViewById(C0064R.id.relativeLayout4));
                                r20 = r27;
                                r21 = new Toast(this);
                                toast = r20;
                                toast.setView(view);
                                toast.show();
                            } else if (this.wrongLeft == 3) {
                                view = getLayoutInflater().inflate(C0064R.layout.cust_toast_layout3, (ViewGroup) findViewById(C0064R.id.relativeLayout3));
                                r20 = r27;
                                r21 = new Toast(this);
                                toast = r20;
                                toast.setView(view);
                                toast.show();
                            } else if (this.wrongLeft == 2) {
                                view = getLayoutInflater().inflate(C0064R.layout.cust_toast_layout2, (ViewGroup) findViewById(C0064R.id.relativeLayout2));
                                r20 = r27;
                                r21 = new Toast(this);
                                toast = r20;
                                toast.setView(view);
                                toast.show();
                            } else if (this.wrongLeft == 1) {
                                view = getLayoutInflater().inflate(C0064R.layout.cust_toast_layout1, (ViewGroup) findViewById(C0064R.id.relativeLayout1));
                                r20 = r27;
                                r21 = new Toast(this);
                                toast = r20;
                                toast.setView(view);
                                toast.show();
                            }
                        }
                    } else if (this.soundOn == 1) {
                        audioManager = (AudioManager) getSystemService("audio");
                        volume = ((float) audioManager.getStreamVolume(3)) / ((float) audioManager.getStreamMaxVolume(3));
                        if (this.loaded) {
                            play = this.soundPool.play(this.soundID, volume, volume, 1, 0, 1.0f);
                        }
                    }
                    if (this.wrongLeft <= 0) {
                        gameOver();
                    } else {
                        if (this.enterPressed == 1) {
                            EditText yourEditText3 = (EditText) findViewById(C0064R.id.editText1);
                            handler = this.mHandler;
                            AnonymousClass20 anonymousClass20 = r27;
                            editText = yourEditText3;
                            AnonymousClass20 anonymousClass202 = new Runnable(this) {
                                final /* synthetic */ MainActivity this$0;

                                public void run() {
                                    ((InputMethodManager) this.this$0.getSystemService("input_method")).toggleSoftInputFromWindow(editText.getApplicationWindowToken(), 2, 0);
                                    boolean requestFocus = editText.requestFocus();
                                }
                            };
                            post = handler.post(anonymousClass20);
                        }
                        TextView textView = (TextView) findViewById(C0064R.id.textView4);
                        stringBuilder = r27;
                        stringBuilder2 = new StringBuilder();
                        textView.setText(stringBuilder.append("Wrong Left: ").append(this.wrongLeft).toString());
                        et1.setText(this.temp2);
                        int completeFlag = 0;
                        for (int k = 0; k < this.temp2.length(); k++) {
                            if (temp2Chars[k] != '-') {
                                completeFlag++;
                            }
                        }
                        if (completeFlag == this.temp2.length()) {
                            correctlyGuessed();
                        }
                    }
                }
            }
        }
        this.enterPressed = 0;
    }

    public void gameOver() {
        Button bnew = (Button) findViewById(C0064R.id.buttonNew);
        bnew.setText("New Game");
        Button button = bnew;
        AnonymousClass21 anonymousClass21 = r12;
        AnonymousClass21 anonymousClass212 = new View.OnClickListener() {
            final View f2v = null;

            {
                AnonymousClass21 anonymousClass21 = this;
            }

            public void onClick(View view) {
                View arg0 = view;
                MainActivity.this.newGame(this.f2v);
            }
        };
        button.setOnClickListener(anonymousClass21);
        ((EditText) findViewById(C0064R.id.editText1)).setEnabled(false);
        bnew.setVisibility(0);
        ((ImageButton) findViewById(C0064R.id.button2)).setVisibility(4);
        ((Button) findViewById(C0064R.id.goButton)).setVisibility(4);
        ((ImageButton) findViewById(C0064R.id.ikib)).setVisibility(4);
        Builder builder = r12;
        Builder builder2 = new Builder(this);
        Builder alertDialog = builder;
        builder = alertDialog.setTitle("Game over");
        builder = alertDialog;
        StringBuilder stringBuilder = r12;
        StringBuilder stringBuilder2 = new StringBuilder();
        builder = builder.setMessage(stringBuilder.append("Score: ").append(this.scoreScored).append("\nAnswer was: ").append(this.temp).toString());
        AnonymousClass22 anonymousClass22 = r12;
        AnonymousClass22 anonymousClass222 = new OnClickListener() {
            {
                AnonymousClass22 anonymousClass22 = this;
            }

            public void onClick(DialogInterface dialog, int which) {
            }
        };
        builder = alertDialog.setNegativeButton("OK", anonymousClass22);
        AnonymousClass23 anonymousClass23 = r12;
        AnonymousClass23 anonymousClass232 = new OnClickListener() {
            {
                AnonymousClass23 anonymousClass23 = this;
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                DialogInterface dialog = dialogInterface;
                int which = i;
                char[] cTemp = MainActivity.this.temp.toCharArray();
                for (int i2 = 0; i2 < MainActivity.this.temp.length(); i2++) {
                    if (cTemp[i2] == ' ') {
                        cTemp[i2] = '+';
                    }
                }
                String addr = String.valueOf(cTemp);
                StringBuilder stringBuilder = r10;
                StringBuilder stringBuilder2 = new StringBuilder();
                addr = stringBuilder.append("http://www.google.com/search?q=").append(addr).toString();
                Intent intent = r10;
                Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(addr));
                MainActivity.this.startActivity(intent);
            }
        };
        builder = alertDialog.setPositiveButton("Google It!", anonymousClass23);
        AlertDialog show = alertDialog.show();
    }

    public void correctlyGuessed() {
        this.scoreScored++;
        TextView textView = (TextView) findViewById(C0064R.id.textView5);
        StringBuilder stringBuilder = r11;
        StringBuilder stringBuilder2 = new StringBuilder();
        textView.setText(stringBuilder.append("Score: ").append(this.scoreScored).toString());
        ((Button) findViewById(C0064R.id.buttonNew)).setVisibility(0);
        ((ImageButton) findViewById(C0064R.id.button2)).setVisibility(4);
        ((Button) findViewById(C0064R.id.goButton)).setVisibility(4);
        ((ImageButton) findViewById(C0064R.id.ikib)).setVisibility(4);
        Toast.makeText(this, "Well done", 1).show();
        EditText et = (EditText) findViewById(C0064R.id.editText1);
        et.setEnabled(false);
        et.setHint("");
        ((TextView) findViewById(C0064R.id.textView1)).setTextColor(-16711936);
    }
}

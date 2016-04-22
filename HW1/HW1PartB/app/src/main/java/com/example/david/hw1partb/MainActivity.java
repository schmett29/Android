package com.example.david.hw1partb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public String[] wordlist = {"handler","against","horizon","chops","junkyard","amoeba","academy","roast",
            "countryside","children","strange","best","drumbeat","amnesiac","chant","amphibian","smuggler","fetish"};
    public String chosenword = "";
    public String hiddenword = "";
    public String returnstring = "";
    public int numguesses = 0;
    public int numused = 0;
    TextView showResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showResult = (TextView)findViewById(R.id.Output);
        initialize();
    }

    public void choseWord(){
        Random r = new Random();
        chosenword = wordlist[r.nextInt(wordlist.length)];
        numguesses = chosenword.length();
    }

    public void initialize(){
        chosenword = "";
        hiddenword = "";
        returnstring = "";
        numguesses = 0;
        numused = 0;
        choseWord();
        for(int i = 0; i<chosenword.length(); i++){
            hiddenword = hiddenword + "-";
        }
        returnstring = returnstring + hiddenword + " consists of " + Integer.toString(chosenword.length()) + " letters.";
        showResult.setText(returnstring);
    }

    public void clickDigit(View view) {
        int buttonId = view.getId();
        switch (buttonId) {
            case R.id.BtnA:
                guess('A');
                break;
            case R.id.BtnB:
                guess('B');
                break;
            case R.id.BtnC:
                guess('C');
                break;
            case R.id.BtnD:
                guess('D');
                break;
            case R.id.BtnE:
                guess('E');
                break;
            case R.id.BtnF:
                guess('F');
                break;
            case R.id.BtnG:
                guess('G');
                break;
            case R.id.BtnH:
                guess('H');
                break;
            case R.id.BtnI:
                guess('I');
                break;
            case R.id.BtnJ:
                guess('J');
                break;
            case R.id.BtnK:
                guess('K');
                break;
            case R.id.BtnL:
                guess('L');
                break;
            case R.id.BtnM:
                guess('M');
                break;
            case R.id.BtnN:
                guess('N');
                break;
            case R.id.BtnO:
                guess('O');
                break;
            case R.id.BtnP:
                guess('P');
                break;
            case R.id.BtnQ:
                guess('Q');
                break;
            case R.id.BtnR:
                guess('R');
                break;
            case R.id.BtnS:
                guess('S');
                break;
            case R.id.BtnT:
                guess('T');
                break;
            case R.id.BtnU:
                guess('U');
                break;
            case R.id.BtnV:
                guess('V');
                break;
            case R.id.BtnW:
                guess('W');
                break;
            case R.id.BtnX:
                guess('X');
                break;
            case R.id.BtnY:
                guess('Y');
                break;
            case R.id.BtnZ:
                guess('Z');
                break;
            case R.id.BtnReset:
                reset();
                break;


        }
    }

    public void guess(Character letter){
        letter = Character.toLowerCase(letter);
        char[] guessChars = hiddenword.toCharArray();
        if(numguesses == numused){
            gameOver();
            return;
        }
        else if(chosenword.indexOf(String.valueOf(letter)) == -1){
            numused += 1;
            returnstring = hiddenword + " used " + Integer.toString(numused) + " of " + Integer.toString(numguesses) + " guesses";
        }
        else{
            int index = chosenword.indexOf(String.valueOf(letter));
            guessChars[index] = letter;
            while(chosenword.indexOf(String.valueOf(letter), index+1) != -1){
                int temp = chosenword.indexOf(String.valueOf(letter), index+1);
                guessChars[temp] = letter;
                index +=1;
            }
            hiddenword = String.valueOf(guessChars);
            returnstring = hiddenword + " used " + Integer.toString(numused) + " of " + Integer.toString(numguesses) + " guesses";
        }
        if(hiddenword.equals(chosenword)){
            win();
            return;
        }
        showResult.setText(returnstring);
    }

    public void reset(){
        initialize();
    }

    public void gameOver(){
        showResult.setText("You lost, the word was: " + chosenword + ". You used " + numused + " guesses.");
    }

    public void win(){
        showResult.setText("you WIN, the word was: " + chosenword + ". You used " + numused + " guesses.");
    }
}

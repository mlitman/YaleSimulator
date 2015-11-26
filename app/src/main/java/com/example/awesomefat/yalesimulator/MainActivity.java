package com.example.awesomefat.yalesimulator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    private EditText rowsTF;
    private EditText colsTF;
    private TextView outputLabel;
    private TextView aLabel;
    private TextView iaLabel;
    private TextView jaLabel;
    private int[][] theMatrix;
    private int[] A;
    private int[] IA;
    private int[] JA;
    private Random r;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this.rowsTF = (EditText) this.findViewById(R.id.rowsTF);
        this.colsTF = (EditText) this.findViewById(R.id.colsTF);
        this.outputLabel = (TextView)this.findViewById(R.id.outputLabel);
        this.aLabel = (TextView)this.findViewById(R.id.aLabel);
        this.iaLabel = (TextView)this.findViewById(R.id.iaLabel);
        this.jaLabel = (TextView)this.findViewById(R.id.jaLabel);
        this.r = new Random();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private String pad(int number, int spaces)
    {
        int numSpaces = spaces - ("" + number).length();
        String answer = "";
        answer += number;
        for(int i = 0; i < numSpaces; i++)
        {
            answer += " ";
        }
        return answer;
    }

    private String getMatrixAsString()
    {
        String answer = "";
        for(int i = 0; i < this.theMatrix.length; i++)
        {
            for(int j = 0; j < this.theMatrix[i].length; j++)
            {
                answer += this.pad(this.theMatrix[i][j], 5);
            }
            answer += "\n";
        }
        return answer;
    }

    private void genA_Array()
    {
        String answer = "";
        for(int i = 0; i < this.theMatrix.length; i++)
        {
            for (int j = 0; j < this.theMatrix[i].length; j++)
            {
                if(this.theMatrix[i][j] != 0)
                {
                    answer += this.theMatrix[i][j] + " ";
                }
            }
        }
        String[] theNums = answer.trim().split(" ");
        this.A = new int[theNums.length];
        for(int i = 0; i < this.A.length; i++)
        {
            this.A[i] = Integer.parseInt(theNums[i]);
        }
    }

    private void genIA_Array()
    {
        this.IA = new int[this.theMatrix.length+1];
        int runningTotal = 0;
        for(int i = 0; i < this.IA.length-1; i++)
        {
            this.IA[i] = runningTotal;
            for(int j = 0; j < this.theMatrix[i].length; j++)
            {
                if(this.theMatrix[i][j] != 0)
                {
                    runningTotal++;
                }
            }
        }
        this.IA[this.IA.length-1] = runningTotal;
    }

    private int findPosInRow(int num, int row)
    {
        Log.i("*****FindPosInRow****", "searching for " + num + " in row: " + row);
        for(int i = 0; i < this.theMatrix[row].length; i++)
        {
            if(this.theMatrix[row][i] == num)
            {
                return i;
            }
        }
        return -1;
    }

    private void genJA_Array()
    {
        int currPosInA = 0;
        this.JA = new int[this.A.length];
        int numInThisRow;
        for(int i = 1; i < this.IA.length; i++)
        {
            //how many non-zero elements are in row i-1?
            numInThisRow = this.IA[i] - this.IA[i-1];

            //search for column positions
            int maxIndex = currPosInA + numInThisRow;
            for(int j = currPosInA; j < this.A.length && j < maxIndex; j++)
            {
                this.JA[j] = this.findPosInRow(this.A[j], i-1);
                currPosInA++;
            }
        }
    }

    private String arrayToString(int[] ar)
    {
        String answer = "[ ";
        for(int i = 0; i < ar.length; i++)
        {
            answer += ar[i] + " ";
        }
        answer += "]";
        return answer;
    }

    public void generateYaleButtonClicked(View v)
    {
        this.genA_Array();
        this.genIA_Array();
        this.genJA_Array();
        this.aLabel.setText(this.arrayToString(this.A));
        this.iaLabel.setText(this.arrayToString(this.IA));
        this.jaLabel.setText(this.arrayToString(this.JA));

    }

    //IBAction
    public void generateMatrixButtonClicked(View v)
    {
        int rows = Integer.parseInt(this.rowsTF.getText().toString());
        int cols = Integer.parseInt(this.colsTF.getText().toString());
        this.theMatrix = new int[rows][cols];
        for(int i = 0; i < this.theMatrix.length; i++)
        {
            for(int j = 0; j < this.theMatrix[i].length; j++)
            {
                if(r.nextInt(100) < 20)
                {
                    this.theMatrix[i][j] = r.nextInt(500)+1;
                }
                else
                {
                    this.theMatrix[i][j] = 0;
                }
            }
        }
        this.outputLabel.setText(this.getMatrixAsString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

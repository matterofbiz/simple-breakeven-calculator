/*
 * Copyright 2014 Business Research & Applications Limited
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */


package biz.matterof.simpleBEcalculator;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.androidplot.Plot.BorderStyle;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

import java.text.DecimalFormat;
import java.util.Arrays;


public class MainActivity extends Activity
{

    private XYPlot myBEPlot = null;
    private SimpleXYSeries seriesSales = null;
    private SimpleXYSeries seriesFC = null;
    private SimpleXYSeries seriesTC = null;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        // android boilerplate stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppRater.app_launched(this);

        // initialise the XYPlot reference:
        myBEPlot = (XYPlot) findViewById(R.id.plotBE);

        // changes the borders
        myBEPlot.setBorderStyle(BorderStyle.SQUARE, null, null);

        // set white background
        myBEPlot.setBackgroundPaint(null);

        // only display whole numbers in labels
        myBEPlot.setDomainValueFormat(new DecimalFormat("0.0"));
        myBEPlot.setRangeValueFormat(new DecimalFormat("0"));

        // customize labels
        myBEPlot.setTitle("Break-even Calculation");
        myBEPlot.setDomainLabel("Units");
        myBEPlot.setRangeLabel("$");

        // draw a domain tick for each year:
        myBEPlot.setDomainStep(XYStepMode.SUBDIVIDE, 6);
        myBEPlot.setRangeStep(XYStepMode.SUBDIVIDE, 6);

    }

    public void Calc(View button)
    {
        double FC;
        FC = 0;
        double VC;
        VC = 0;
        double UP;
        UP = 0;
        double BEU;
        BEU = 0;
        double BE;
        BE = 0;
        double Ticks;


        // get the references to the widgets
        EditText FCtxt = (EditText) findViewById(R.id.editFixedCosts);
        EditText VCtxt = (EditText) findViewById(R.id.editVariableCosts);
        EditText UPtxt = (EditText) findViewById(R.id.editPrice);
        TextView BEtxt = (TextView) findViewById(R.id.textView4);

        // get the users values from the widget references
        if (FCtxt.getText().length() > 0) FC = Float.parseFloat(FCtxt.getText().toString());
        if (VCtxt.getText().length() > 0) VC = Float.parseFloat(VCtxt.getText().toString());
        if (UPtxt.getText().length() > 0) UP = Float.parseFloat(UPtxt.getText().toString());

        //Calculate break-even point
        if(UP-VC > 0){
            BEU = FC / (UP - VC);			// given price
            BE = BEU * UP;
        }

        String txtBEU;
        txtBEU = "Break-even: " + String.format("%.2f", BEU) + " units\nSales: $" + String.format("%.2f", BE);

        // BEtxt.setVisibility(1);
        BEtxt.setText(txtBEU);

        //Prepares the datasets for better graph alignment
        Ticks = BEU*2/6;

        // get rid the oldest series:
        if (myBEPlot.isShown()) {
            myBEPlot.removeSeries(seriesSales);
            myBEPlot.removeSeries(seriesFC);
            myBEPlot.removeSeries(seriesTC);
        }

        // myBEPlot.setVisibility(1);

        // Create the arrays of y-values to plot:
        Number[] UPNumbers = {0, 1*Ticks*UP, 2*Ticks*UP, 3*Ticks*UP, 4*Ticks*UP, 5*Ticks*UP};
        Number[] FCNumbers = {FC, FC, FC, FC, FC, FC};
        Number[] TCNumbers = {FC, FC+1*Ticks*VC, FC+2*Ticks*VC, FC+3*Ticks*VC, FC+4*Ticks*VC, FC+5*Ticks*VC};
        Number[] TicksNumbers = {0, 1*Ticks, 2*Ticks, 3*Ticks, 4*Ticks, 5*Ticks};


        // Turn the above arrays into XYSeries':
        seriesSales = new SimpleXYSeries(Arrays.asList(TicksNumbers),Arrays.asList(UPNumbers),"Sales");
        seriesFC = new SimpleXYSeries(Arrays.asList(TicksNumbers), Arrays.asList(FCNumbers), "Fixed Cost");
        seriesTC = new SimpleXYSeries(Arrays.asList(TicksNumbers), Arrays.asList(TCNumbers), "Total Cost");

        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        LineAndPointFormatter series1Format = new LineAndPointFormatter(
                Color.rgb(0, 100, 0),
                Color.rgb(0, 100, 0),
                null,
                (PointLabelFormatter) null);

        // add a new series' to the xyplot:
        myBEPlot.addSeries(seriesSales, series1Format);
        myBEPlot.addSeries(seriesFC,
                new LineAndPointFormatter(
                        Color.rgb(0, 0, 250),
                        Color.rgb(0, 0, 250),
                        null,
                        (PointLabelFormatter) null));

        myBEPlot.addSeries(seriesTC,
                new LineAndPointFormatter(
                        Color.rgb(0, 0, 400),
                        Color.rgb(0, 0, 400),
                        null,
                        (PointLabelFormatter) null));

        // reduce the number of labels
        myBEPlot.setTicksPerRangeLabel(1);
        myBEPlot.setTicksPerDomainLabel(1);

        myBEPlot.redraw();
    }
}
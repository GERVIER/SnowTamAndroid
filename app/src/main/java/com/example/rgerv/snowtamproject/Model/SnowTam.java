package com.example.rgerv.snowtamproject.Model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.rgerv.snowtamproject.R;

import java.text.SimpleDateFormat;

/**
 * Created by rgerv on 21/11/2017.
 * Describe a SnowTam and contains some method to decode it
 */

public class SnowTam {
    private String codedSnowTam;
    private String decodedSnowTam;

    public SnowTam(String codedSnowTam){
        this.codedSnowTam = codedSnowTam;
    }


    public String getCodedSnowTam() {
        return codedSnowTam;
    }

    public void setCodedSnowTam(String codedSnowTam) {
        this.codedSnowTam = codedSnowTam;
    }

    public String getDecodedSnowTam() {
        return decodedSnowTam;
    }

    /**
     * decode a snowtam and put the result on the decodedSnowTam attribute accessible by calling getDecodedSnowTam.
     * @param airportLocation location of the aiport to where snowtam belong
     * @param context context of the calling activity
     */
    public void decodeSnowTam(String airportLocation, Context context) {
        decodedSnowTam = "";
        StringBuilder sb = new StringBuilder();
        sb.append("");
        if(this.codedSnowTam != null){
            String[] lines = codedSnowTam.split("\n");

            String line;

            for(int i = 0; i<lines.length; i++){
                line = lines[i];
                sb.append(analyseSnowTamLine(line, airportLocation, context));
            }
        }

        decodedSnowTam = sb.toString();
    }

    /**
     * decode a line of a snowtime
     * @param line line to decode
     * @param airportLocation location of the airport
     * @param context context of the calling activity
     * @return the line decoded
     */
    private String analyseSnowTamLine(String line, String airportLocation, Context context){
        StringBuilder sb = new StringBuilder();
        sb.append("");
        String[] caracs = line.split(" ");
        String carac;
        for(int j = 0; j<caracs.length; j++){
            carac = caracs[j];

            if(carac.equals("A)")){
                sb.append("A) ");
                sb.append(airportLocation);
                sb.append("\n");
            }

            if(carac.equals("B)")){
                String timeCode = caracs[j+1];
                SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmm");
                try{
                    sb.append("B) ");
                    sb.append(formatter.parse(timeCode));
                    sb.append(" ");
                }catch (Exception e){
                    sb.append(context.getString(R.string.parsing_date_error));
                }
            }

            if(carac.equals("C)")){
                int runWayNumber = Integer.parseInt(caracs[j+1]);
                sb.append(context.getString(R.string.runway));
                if(runWayNumber<36){
                    sb.append(runWayNumber);
                    sb.append("L\n");
                }else{
                    sb.append(runWayNumber-50);
                    sb.append("L\n");
                }
            }

            if(carac.equals("D)")){
                sb.append(context.getString(R.string.cleared_runway_length));
                sb.append(caracs[j+1]);
                sb.append("M ");
            }

            if(carac.equals("E)")){
                sb.append(context.getString(R.string.cleared_runway_width));
                sb.append(caracs[j+1]);
                sb.append("M ");
            }

            if(carac.equals("F)")){
                sb.append(analyseFdata(caracs[j+1], context));
                sb.append(" ");
            }

            if(carac.equals("G)")){
                sb.append(analyseGdata(caracs[j+1], context));
                sb.append("\n");
            }

            if(carac.equals("H)")){
                try{
                    sb.append(analyseHdata(context, caracs[j+1], caracs[j+2]));
                }catch (ArrayIndexOutOfBoundsException e){
                    sb.append(analyseHdata(context, caracs[j+1]));
                }
            }

        }

        return sb.toString();
    }

    /**
     * decode the data of the F group
     * @param carac the group to decode
     * @param context context of the calling activity
     * @return the decoded line
     */
    @NonNull
    private String analyseFdata(String carac, Context context){
        StringBuilder sb = new StringBuilder();
        sb.append("F) ");
        String[] conditions = carac.split("/");
        for(int i = 0; i<conditions.length; i++){
            int condition;
            try{
                condition = Integer.parseInt(conditions[i]);
            }catch (Exception e){
                condition = -1;
            }

            switch (i){
                case 0:
                    sb.append(context.getString(R.string.threshold));
                    break;
                case 1:
                    sb.append(context.getString(R.string.mid_runway));
                    break;
                case 2:
                    sb.append(context.getString(R.string.roll_out));
                    break;

                default:
                    sb.append("");
            }

            switch (condition) {
                case 0:
                    sb.append(context.getString(R.string.clear_and_dry));
                    break;
                case 1:
                    sb.append(context.getString(R.string.damp));
                    break;
                case 2:
                    sb.append(context.getString(R.string.wet));
                    break;
                case 3:
                    sb.append(context.getString(R.string.rime));
                    break;
                case 4:
                    sb.append(context.getString(R.string.dry_snow));
                    break;
                case 5:
                    sb.append(context.getString(R.string.wet_snow));
                    break;
                case 6:
                    sb.append(context.getString(R.string.slush));
                    break;
                case 7:
                    sb.append(context.getString(R.string.ice));
                    break;
                case 8:
                    sb.append(context.getString(R.string.compacted_snow));
                    break;
                case 9:
                    sb.append(context.getString(R.string.frozen_ruts));
                    break;

                default:
                    sb.append("NA");
            }

            if(i < 2) sb.append(" / ");

        }
        return sb.toString();
    }

    /**
     * decode the data of the G group
     * @param carac the group to decode
     * @param context context of the calling activity
     * @return the decoded line
     */
    private String analyseGdata(String carac, Context context){
        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(R.string.mean_depth));

        String[] conditions = carac.split("/");
        for(int i = 0; i<conditions.length; i++){

            switch (i){
                case 0:
                    sb.append(context.getString(R.string.threshold));
                    break;
                case 1:
                    sb.append(context.getString(R.string.mid_runway));
                    break;
                case 2:
                    sb.append(context.getString(R.string.roll_out));
                    break;

                default:
                    sb.append("");
            }

            sb.append(conditions[i]);
            if(!conditions[i].equals("XX"))
                sb.append("mm");

            if(i < 2) sb.append(" / ");

        }
        return sb.toString();
    }

    /**
     * decode the data of the G group
     * @param context context of the calling activity
     * @param carac the group to decode followed if needed by the tool used
     * @return the decoded line
     */
    private String analyseHdata(Context context, String... carac){
        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(R.string.braking_action));

        String[] conditions = carac[0].split("/");
        for(int i = 0; i<conditions.length; i++){
            String condition = conditions[i];
            switch (i){
                case 0:
                    sb.append(context.getString(R.string.threshold));
                    break;
                case 1:
                    sb.append(context.getString(R.string.mid_runway));
                    break;
                case 2:
                    sb.append(context.getString(R.string.roll_out));
                    break;

                default:
                    sb.append("");
            }

            switch (condition) {
                case "5":
                    sb.append(context.getString(R.string.good));
                    break;
                case "4":
                    sb.append(context.getString(R.string.medium_good));
                    break;
                case "3":
                    sb.append(context.getString(R.string.medium));
                    break;
                case "2":
                    sb.append(context.getString(R.string.medium_poor));
                    break;
                case "1":
                    sb.append(context.getString(R.string.poor));
                    break;
                case "9":
                    sb.append(context.getString(R.string.condition_not_suitable));
                    break;
                default:
                    sb.append("NA");
            }
            if(i < 2) sb.append(" / ");
        }

        if(carac.length == 2){
            sb.append("\n");
            switch (carac[1]){
                case "BRD" :
                    sb.append(context.getString(R.string.BRD));
                    break;
                case "GRT" :
                    sb.append(context.getString(R.string.GRT));
                    break;
                case "MUM" :
                    sb.append(context.getString(R.string.MUM));
                    break;
                case "RFT" :
                    sb.append(context.getString(R.string.RFT));
                    break;
                case "SFH" :
                    sb.append(context.getString(R.string.SFH));
                    break;
                case "SFL" :
                    sb.append(context.getString(R.string.SFL));
                    break;
                case "SKH" :
                    sb.append(context.getString(R.string.SKH));
                    break;
                case "SKL" :
                    sb.append(context.getString(R.string.SKL));
                    break;
                case "TAP" :
                    sb.append(context.getString(R.string.TAP));
                    break;
            }
        }
        return sb.toString();
    }
}


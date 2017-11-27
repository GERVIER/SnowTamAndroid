package com.example.rgerv.snowtamproject.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.rgerv.snowtamproject.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rgerv on 21/11/2017.
 * Describe a SnowTam and contains some method to decode it
 */

public class SnowTam {
    private String codedSnowTam;
    private String decodedSnowTam;

    public SnowTam(String codedSnowTam){
        this.codedSnowTam = codedSnowTam;
        this.decodedSnowTam = codedSnowTam;
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
            }

            if(carac.equals("B)")){
                String timeCode = caracs[j+1];
                SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmm");
                SimpleDateFormat formatter2 = new SimpleDateFormat("dd MM HH:mm");

                sb.append("\n\n");
                sb.append("B) ");
                try{
                    Date date = formatter.parse(timeCode);
                    String date1 = formatter2.format(date);
                    sb.append(date1);
                }catch (Exception e){
                    sb.append(context.getString(R.string.parsing_date_error));
                }
                sb.append("\n");
            }

            if(carac.equals("C)")){
                try{
                    int runWayNumber = Integer.parseInt(caracs[j+1]);
                    sb.append(context.getString(R.string.runway));
                    sb.append(" ");
                    if(runWayNumber<36){
                        sb.append(runWayNumber);
                        sb.append("L\n");
                    }else{
                        sb.append(runWayNumber-50);
                        sb.append("L\n");
                    }
                }catch (Exception e){
                    sb.append(context.getString(R.string.runway));
                    sb.append(" ");
                    sb.append(caracs[j+1]);
                    sb.append("\n");
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
                sb.append("\n");
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
                sb.append("\n");
            }

            if (carac.equals("J)")){
                sb.append(analyseJdata(context, caracs[j+1]));
                sb.append("\n");
            }

            if (carac.equals("K)")){
                try{
                    sb.append(analyseKdata(context, caracs[j+1], caracs[j+2]));
                    sb.append("\n");
                }catch (ArrayIndexOutOfBoundsException e){
                    sb.append(context.getString(R.string.error_analyse));
                }
            }

            if (carac.equals("L)")){
                sb.append(analyseLdata(context, caracs[j+1]));
                sb.append("\n");
            }

            if (carac.equals("M)")){
                sb.append(analyseMdata(context, caracs[j+1]));
                sb.append("\n");
            }

            if (carac.equals("N)")){
                sb.append(analyseNdata(context, caracs[j+1]));
                sb.append("\n");
            }

            if (carac.equals("P)")){
                sb.append(analysePdata(context, caracs[j+1]));
                sb.append("\n");
            }

            if (carac.equals("R)")){
                try{
                    sb.append(analyseRdata(context, caracs[j+1], caracs[j+2]));
                    sb.append("\n");
                }catch (ArrayIndexOutOfBoundsException e){
                    sb.append(context.getString(R.string.error_analyse));
                }
            }

            if (carac.equals("S)")){
                String timeCode = caracs[j+1];
                SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmm");
                SimpleDateFormat formatter2 = new SimpleDateFormat("dd MM HH:mm");

                sb.append("\n\n");
                sb.append("S) ");
                try{
                    Date date = formatter.parse(timeCode);
                    String date1 = formatter2.format(date);
                    sb.append(context.getString(R.string.next_obs));
                    sb.append(" ");
                    sb.append(date1);
                }catch (Exception e){
                    sb.append(context.getString(R.string.parsing_date_error));
                }
                sb.append("\n");
            }

            if (carac.equals("T)")){
                String[] data = getCodedSnowTam().split("T[)]");
                String[] tData = data[1].split("[)]");
                sb.append("T) ");
                sb.append(tData[0]);
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

            sb.append(analyseRunwayCondition(context, condition));

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
        sb.append(" ");
        String[] conditions = carac[0].split("/");
        for(int i = 0; i<conditions.length; i++){
            int condition = Integer.parseInt(conditions[i]);

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
                case 5:
                    sb.append(context.getString(R.string.good));
                    break;
                case 4:
                    sb.append(context.getString(R.string.medium_good));
                    break;
                case 3:
                    sb.append(context.getString(R.string.medium));
                    break;
                case 2:
                    sb.append(context.getString(R.string.medium_poor));
                    break;
                case 1:
                    sb.append(context.getString(R.string.poor));
                    break;
                case 9:
                    sb.append(context.getString(R.string.condition_not_suitable));
                    break;
                default:{
                    if(condition > 40){
                        sb.append(context.getString(R.string.good));
                    }
                    else{
                        if(condition > 36){
                            sb.append(context.getString(R.string.medium_good));
                        }
                        else{
                            if(condition > 30){
                                sb.append(context.getString(R.string.medium));
                            }
                            else{
                                if(condition > 25){
                                    sb.append(context.getString(R.string.medium_poor));
                                }else {
                                    if(condition <= 25)
                                        sb.append(context.getString(R.string.poor));
                                }
                            }
                        }
                    }
                }
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

    /**
     * decode the data of the J group
     * @param context of the calling activity
     * @param carac the group to decode
     * @return the decoded line
     */
    private String analyseJdata(Context context, String carac){
        StringBuilder sb = new StringBuilder();
        sb.append("J) ");
        sb.append(context.getString(R.string.critical_snow_bank));
        String[] detailedCarac = carac.split("/");
        String distance = detailedCarac[1];


        distance = distance.replace("L", "");
        distance = distance.replace("R", "");
        distance = distance.replace("LR", "");

        sb.append(" ");
        sb.append(detailedCarac[0]);
        sb.append("cm / ");
        sb.append(distance);
        sb.append("m ");
        if(detailedCarac[1].contains("LR")){
           sb.append(context.getString(R.string.left_right));
        }
        else{
            if (detailedCarac[1].contains("R")){
                sb.append(context.getString(R.string.right));
            }
            else{
                sb.append(context.getString(R.string.left));
            }
        }

        return sb.toString();
    }

    /**
     * decode the data of the K group
     * @param context of the calling activity
     * @param carac the group to decode
     * @param where L, R, or LR
     * @return the decoded line
     */
    private String analyseKdata(Context context, String carac, String where){
        StringBuilder sb = new StringBuilder();

        if(carac.contains("YES")){
            sb.append("K) ");
            sb.append(context.getString(R.string.lights_obscured));

            if(where.contains("LR")){
                sb.append(context.getString(R.string.left_right));
            }
            else{
                if (where.contains("R")){
                    sb.append(context.getString(R.string.right));
                }
                else{
                    sb.append(context.getString(R.string.left));
                }
            }
        }

        return sb.toString();
    }


    /**
     * decode the data of the L group
     * @param context of the calling activity
     * @param carac the group to decode
     * @return the decoded line
     */
    private String analyseLdata(Context context, String carac){
        StringBuilder sb = new StringBuilder();
        sb.append("L) ");
        sb.append(context.getString(R.string.further_clearance));
        sb.append(" ");

        String[] details = carac.split("/");
        sb.append(details[0]);
        sb.append("m / ");
        sb.append(details[1]);
        sb.append("m ");

        return sb.toString();
    }

    /**
     * decode the data of the M group
     * @param context of the calling activity
     * @param carac the group to decode
     * @return the decoded line
     */
    private String analyseMdata(Context context, String carac){
        StringBuilder sb = new StringBuilder();
        sb.append("M) ");
        sb.append(context.getString(R.string.anticipated_time_completion));
        sb.append(" ");
        sb.append(carac);
        sb.append(" UTC");
        return sb.toString();
    }

    /**
     * decode the data of the  group
     * @param context of the calling activity
     * @param carac the group to decode
     * @return the decoded line
     */
    private String analyseNdata(Context context, String carac){
        StringBuilder sb = new StringBuilder();
        sb.append("N) ");

        if(carac.equals("NO")){
            sb.append(context.getString(R.string.taxiway_none));
        }else{
            char taxiway = carac.charAt(0);
            String condition = carac.charAt(1)+"";

            sb.append(context.getString(R.string.taxiway));
            sb.append(" ");
            sb.append(taxiway);
            sb.append(" ");
            sb.append(analyseRunwayCondition(context, condition));
        }

        return sb.toString();
    }

    /**
     * decode the data of the P group
     * @param context of the calling activity
     * @param carac the group to decode
     * @return the decoded line
     */
    private String analysePdata(Context context, String carac){
        StringBuilder sb = new StringBuilder();
        sb.append("P) ");

        if(carac.contains("YES")){
            sb.append(context.getString(R.string.snow_bank));
            sb.append(" ");
            sb.append(context.getString(R.string.yes));
            sb.append(" ");
            sb.append(carac.replace("YES", ""));
            sb.append("m");
        }
        else{
            sb.append(carac);
        }
        return sb.toString();
    }

    /**
     * decode the data of the R group
     * @param context of the calling activity
     * @param carac the group to decode
     * @return the decoded line
     */
    private String analyseRdata(Context context, String carac, String condition){
        StringBuilder sb = new StringBuilder();
        sb.append("R) ");
        sb.append(context.getString(R.string.parking));
        sb.append(" ");
        sb.append(carac);
        sb.append(" ");
        if(condition .equals("NO")){
            sb.append(context.getString(R.string.unusable));
        }
        else{
            sb.append(analyseRunwayCondition(context, condition));
        }
        return sb.toString();
    }

    /**
     * decode the data of the  group
     * @param context of the calling activity
     * @param carac the group to decode
     * @return the decoded line
     */
    private String analysedata(Context context, String carac){
        StringBuilder sb = new StringBuilder();
        sb.append("J) ");

        return sb.toString();
    }

    private String analyseRunwayCondition(Context context, String condition){
        StringBuilder sb = new StringBuilder();
        switch (condition) {
            case "0":
                sb.append(context.getString(R.string.clear_and_dry));
                break;
            case "1":
                sb.append(context.getString(R.string.damp));
                break;
            case "2":
                sb.append(context.getString(R.string.wet));
                break;
            case "3":
                sb.append(context.getString(R.string.rime));
                break;
            case "4":
                sb.append(context.getString(R.string.dry_snow));
                break;
            case "5":
                sb.append(context.getString(R.string.wet_snow));
                break;
            case "6":
                sb.append(context.getString(R.string.slush));
                break;
            case "7":
                sb.append(context.getString(R.string.ice));
                break;
            case "8":
                sb.append(context.getString(R.string.compacted_snow));
                break;
            case "9":
                sb.append(context.getString(R.string.frozen_ruts));
                break;
            default:
                sb.append("NA");
        }

        return sb.toString();
    }
}


package com.example.rgerv.snowtamproject.Model;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;

/**
 * Created by rgerv on 21/11/2017.
 * Describe a SnowTam
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

    public void decodeSnowTam(String airportLocation) {
        decodedSnowTam = "";
        StringBuilder sb = new StringBuilder();
        sb.append("");
        if(this.codedSnowTam != null){
            String[] lines = codedSnowTam.split("\n");

            String line;

            for(int i = 0; i<lines.length; i++){
                line = lines[i];
                sb.append(analyseSnowTamLine(line, airportLocation));
            }
        }

        decodedSnowTam = sb.toString();
    }

    private String analyseSnowTamLine(String line, String airportLocation){
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
                    sb.append("B) Error Parsing Date");
                }
            }

            if(carac.equals("C)")){
                int runWayNumber = Integer.parseInt(caracs[j+1]);
                if(runWayNumber<36){
                    sb.append("C) RUNWAY ");
                    sb.append(runWayNumber);
                    sb.append("L\n");
                }else{
                    sb.append("C) RUNWAY ");
                    sb.append(runWayNumber-50);
                    sb.append("L\n");
                }
            }

            if(carac.equals("D)")){
                sb.append("D) CLEARED RUNWAY LENGTH");
                sb.append(caracs[j+1]);
                sb.append("M ");
            }

            if(carac.equals("E)")){
                sb.append("E) CLEARED RUNWAY WIDTH");
                sb.append(caracs[j+1]);
                sb.append("M ");
            }

            if(carac.equals("F)")){
                sb.append(analyseFdata(caracs[j+1]));
                sb.append(" ");
            }

            if(carac.equals("G)")){
                sb.append(analyseGdata(caracs[j+1]));
                sb.append(" ");
            }

            if(carac.equals("H)")){
                sb.append(analyseHdata(caracs[j+1], caracs[j+1]));
            }

        }

        return sb.toString();
    }

    @NonNull
    private String analyseFdata(String carac){
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
                    sb.append("Threshold: ");
                    break;
                case 1:
                    sb.append("Mid runway: ");
                    break;
                case 2:
                    sb.append("Roll out: ");
                    break;

                default:
                    sb.append("");
            }

            switch (condition) {
                case 0:
                    sb.append("CLEAR AND DRY");
                    break;
                case 1:
                    sb.append("DAMP");
                    break;
                case 2:
                    sb.append("WET or WATER PATCHES");
                    break;
                case 3:
                    sb.append("RIME OR FROST COVERED");
                    break;
                default:
                    sb.append("NA");
            }

            if(i < 2) sb.append(" / ");

        }
        return sb.toString();
    }

    private String analyseGdata(String carac){
        StringBuilder sb = new StringBuilder();
        sb.append("G) MEAN DEPTH ");

        String[] conditions = carac.split("/");
        for(int i = 0; i<conditions.length; i++){

            switch (i){
                case 0:
                    sb.append("Threshold: ");
                    break;
                case 1:
                    sb.append("Mid runway: ");
                    break;
                case 2:
                    sb.append("Roll out: ");
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

    private String analyseHdata(String carac, String instru){
        StringBuilder sb = new StringBuilder();
        sb.append("H) BRAKING ACTION ");

        String[] conditions = carac.split("/");
        for(int i = 0; i<conditions.length; i++){

            String condition = conditions[i];
            switch (i){
                case 0:
                    sb.append("Threshold: ");
                    break;
                case 1:
                    sb.append("Mid runway: ");
                    break;
                case 2:
                    sb.append("Roll out: ");
                    break;

                default:
                    sb.append("");
            }

            switch (condition) {
                case "5":
                    sb.append("GOOD");
                    break;
                case "4":
                    sb.append("MEDIUM TO GOOD");
                    break;
                case "3":
                    sb.append("MEDIUM)");
                    break;
                case "2":
                    sb.append("(MEDIUM TO POOR");
                    break;
                case "1":
                    sb.append("POOR");
                    break;
                case "9":
                    sb.append("RIME OR FROST COVERED");
                    break;
                default:
                    sb.append("NA");
            }
            if(i < 2) sb.append(" / ");
        }
        return sb.toString();
    }
}


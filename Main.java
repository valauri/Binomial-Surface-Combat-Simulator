package Simulaattori;

/**
 *
 * @author l_vas
 */

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.apache.commons.math3.distribution.BetaDistribution;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main implements Cloneable {
    static ArrayList<Combatant> blue = new ArrayList<>();
    static ArrayList<Combatant> red = new ArrayList<>();
    
    static BetaDistribution beta = new BetaDistribution(3, 1);
    
    static int surviving_blue;
    
    static int iterations = 100000;
    
    static int suwUSV = 0;
    static int isrUSV = 0;
    static int BC = 2;
    static int BFAC = 2;
    static int RC = 5;
    
    static int usv_missiles = 8;
    static int corvette_missiles = 8;
    static int fac_missiles = 4;
    
    static int usv_defcab = 1;
    static int corv_defcab = 4; //modified from 2 to 4
    static int fac_defcab = 2; // modified from 1 to 2
    
    static int bmsl = 0;
    static int rmsl = 0;
    
    static HashMap<Combatant, Boolean> msl_hits = new HashMap<Combatant, Boolean>();
    
    public static void main(String[] args) throws CloneNotSupportedException, IOException {
    //corvette: 146.26M
    //uswUSV = 47.3M
    //isrUSV = 35.5M
    //total cost of SAG: max 942.52
        Scanner obj = new Scanner(System.in);
        
        System.out.println("Choose number of encounter iterations: ");
        
        String iter = obj.nextLine();
        
        int iterations = Integer.parseInt(iter);
        
        System.out.println("Do you want to create alternative compositions to a budget? (Y/N)");
        
        String ans1 = obj.nextLine();
        
        if(ans1.equals("Y")){
            System.out.println("State desired budget: ");
            String budget = obj.nextLine();
            int ceiling = Integer.parseInt(budget);            

            System.out.println("\n\n");

            ArrayList<Solver.Item> menu = new ArrayList<>();
            menu.add(new Solver.Item("Corvette", 325.0));
            menu.add(new Solver.Item("SUW-USV", 47.3));
            menu.add(new Solver.Item("ISR-USV", 35.5));
            menu.add(new Solver.Item("FAC", 146.26));

            Solver solver = new Solver(menu);

            ArrayList<String[]> solutions = solver.solve(ceiling);//942.52);

            int[][] compositions = new int[solutions.size()+1][4];

            int l = solutions.size()+1;

            for(int i=0;i<solutions.size();i++){
                String[] classes = Arrays.toString(solutions.get(i)).split("\\[|\\,|\\ ");

                int suw = 0;
                int isr = 0;
                int corv = 0;
                int fac = 0;

                for(int j = 0; j < classes.length;j++){
                    if(classes[j].equals("ISR-USV")){
                        isr++;
                    }
                    if(classes[j].equals("SUW-USV")){
                        suw++;
                    }
                    if(classes[j].equals("Corvette")){
                        corv++;
                    }
                    if(classes[j].equals("FAC")){
                        fac++;
                    }
                }
                int[] arr = {corv, fac, isr, suw};

                for(int j=0; j <4; j++){
                    compositions[i][j] = arr[j];
                }
            }
            compositions[solutions.size()][0] = 2; //add original composition 2 corvettes
            compositions[solutions.size()][1] = 2; //add original composition 2 FAC
            
            for(int i = 0; i < l; i++){
                for(int j = 0; j < 4; j++){
                    System.out.print(compositions[i][j] + ",");
                    }
                    System.out.println("");
                }
    
                System.out.println(" ");

                double[][] results = new double[l][4];

                for(int r = 0; r < l; r++){
                    BC = compositions[r][0];
                    BFAC = compositions[r][1];
                    isrUSV = compositions[r][2];
                    suwUSV = compositions[r][3];
        
        
                //counters for wins and draws
                    double bwins = 0;
                    double rwins = 0;
                    double draw = 0;

                    surviving_blue = 0;
                //execution of combat iterations
                    for(int k = 0; k < iterations; k++){
                        String g = simulate();
                        if(g.equals("draw")){
                            draw++;
                        }
                        if(g.equals("red")){
                            rwins++;
                        }
                        if(g.equals("blue")){
                            bwins++;
                            }

                        surviving_blue = surviving_blue + blue.size();
                        }

                        results[r][0] = bwins/iterations*100;
                        results[r][1] = rwins/iterations*100;
                        results[r][2] = draw/iterations*100;
                        results[r][3] = surviving_blue/iterations;
        }
        
        double combined_res[][] = new double[l][8];
                
        for(int i = 0; i < l; i++){
            for(int j = 0; j <4;j++){
                combined_res[i][j] = compositions[i][j];
                combined_res[i][j+4] = results[i][j];
            }
        }
                
        
        for(int i = 0; i < l; i++){
            for(int j = 0; j <8;j++){
                System.out.print(combined_res[i][j] + ",");
            }
            System.out.println("");
        }
        System.out.println("Save to csv file? (Y/N)");
        
        String path = "";
        
        if(obj.nextLine().equals("Y")){
            //
                System.out.println("Use default path or CWD? (default/CWD)");
                if(obj.nextLine().equals("default")){
                    path = "C:\\Users\\Public\\Documents\\sim.csv";
                    System.out.println("Saving to " + path);
                }else{
                    Path currentpath = Paths.get("");
                    path = currentpath.toAbsolutePath().toString();
                    System.out.println("Saving to " + path);
                }
                try (FileWriter writer = new FileWriter(path)){//)) {
                    writer.append("Corvette, FAC, ISR USV, SUW USV, Blue win, Red Win, Draw, Surviving Blue units");
                    writer.append("\n");
                    for(int i = 0; i < l; i++){
                        for(int j = 0; j <8;j++){
                            writer.append(String.valueOf(combined_res[i][j]+","));
                        }
                        writer.append("\n");
                    }   
                }
        }
        
        }else{
            System.out.println("State number of corvettes: ");
            
            BC = Integer.parseInt(obj.nextLine());
            
            System.out.println("State number of fast attack crafts: ");
            
            BFAC = Integer.parseInt(obj.nextLine());
            
            System.out.println("State number of ISR USV's: ");
            
            isrUSV = Integer.parseInt(obj.nextLine());
            
            System.out.println("State number of surface warfare USV's: ");
            
            suwUSV = Integer.parseInt(obj.nextLine());
            
            System.out.println("\nExecuting simulation\n");
            //counters for wins and draws
            double bwins = 0;
            double rwins = 0;
            double draw = 0;

            surviving_blue = 0;
            
            //execution of combat iterations
            
            for(int k = 0; k < iterations; k++){
                String g = simulate();
                if(g.equals("draw")){
                    draw++;
                }
                if(g.equals("red")){
                    rwins++;
                }
                if(g.equals("blue")){
                    bwins++;
                    }

                surviving_blue = surviving_blue + blue.size();
                
                
            }
            System.out.println("Blue wins: " + bwins/iterations*100 + "\nRed wins: " + rwins/iterations*100 + "\nDraws: " + draw/iterations*100 + "\nAverage surviving Blue units: " + surviving_blue/iterations);
        }
    }
    
    public static void red_engagement(){
        Random random = new Random();
        
        for(Combatant b : blue){
            if(b.inAction()){ //checking each opposing combatant that is still operational
                for(Combatant r : red){
                    if(r.available()){ //checking the availability of effectors for each red unit
                        if(b.loa() < 100){ //determining the effective salvo size (2) for smaller vessels (smaller than 100m in length)
                            int n = Math.min(r.msl_cap(), 2); //selecting 2 missiles 
                            r.fire(n); //firing missiles i.e. reducing amount of missiles left
                            rmsl = rmsl - n;
                            double p = random.nextDouble();
                            double threshold = 0;
                            if(p < 0.345){    //selecting whether the engagement has been detected or not
                                for(int j = 1; j <= n; j++){
                                    int f = binomial(n, j);
                                    double t = f*(Math.pow(0.68,j))*(Math.pow((1-0.68),(n-j))); //calculating the probability of impact
                                    threshold = threshold+t;
                                    }
                            }else{ //if the engagement is detected, the probability of succesful engagement is lower
                                for(int j = 1; j <= n; j++){
                                    int f = binomial(n, j);
                                    double t = f*(Math.pow(0.26,j))*(Math.pow((1-0.26),(n-j)));
                                    threshold = threshold+t;
                                    }
                                }
                                double p1 = random.nextDouble();
                                if(p1 < threshold){
                                    msl_hits.put(b, true);
                                }
                            break;
                            }
                        
                        if(b.loa() >= 100){
                            int n = Math.min(r.msl_cap(), 4); //selecting 4 missile salvo for larger vessels
                            r.fire(n);
                            rmsl = rmsl - n;
                            double p = random.nextDouble();
                            double threshold = 0;
                            
                            if(p < 0.345){    
                                for(int j = 1; j <= n; j++){
                                    int f = binomial(n, j);
                                    double t = f*(Math.pow(0.68,j))*(Math.pow((1-0.68),(n-j)));
                                    threshold = threshold+t;
                                    }
                            }else{
                                for(int j = 1; j <= n; j++){
                                    int f = binomial(n, j);
                                    double t = f*(Math.pow(0.26,j))*(Math.pow((1-0.26),(n-j)));
                                    threshold = threshold+t;
                                    }
                                }
                                double p1 = random.nextDouble();
                                if(p1 < threshold){
                                    msl_hits.put(b, true);
                                }
                            break;
                            }
                    }
                }
            }
        }
    }
    
    public static void blue_engagement(){
        Random random = new Random();
        
        double d = 1;
        
        for(Combatant r : red){
            if(r.inAction()){
                for(Combatant b : blue){
                    if(b.available() && b.was() == false){
                        if(b.unmanned()){
                            d = beta.sample();
                            }
                            if(d >= 0.5){
                                if(r.loa() < 100){
                                    int n = Math.min(b.msl_cap(), 2);
                                    b.fire(n);
                                    bmsl = bmsl-n;
                                    double p = random.nextDouble();
                                    double threshold = 0;
                                    if(p < 0.345){    
                                        for(int j = 1; j <= n; j++){
                                            int f = binomial(n, j);
                                            double t = f*(Math.pow(0.68,j))*(Math.pow((1-0.68),(n-j)));
                                            threshold = threshold+t;
                                            }
                                    }else{
                                        for(int j = 1; j <= n; j++){
                                            int f = binomial(n, j);
                                            double t = f*(Math.pow(0.26,j))*(Math.pow((1-0.26),(n-j)));
                                            threshold = threshold+t;
                                            }
                                        }
                                        double p1 = random.nextDouble();
                                        if(p1 < threshold){
                                            msl_hits.put(r, true);
                                        }
                                    break;
                                    }


                                if(r.loa() >= 100){
                                    int n = Math.min(b.msl_cap(), 4);
                                    b.fire(n);
                                    bmsl = bmsl-n;
                                    double p = random.nextDouble();
                                    double threshold = 0;

                                    if(p < 0.345){    
                                        for(int j = 1; j <= n; j++){
                                            int f = binomial(n, j);
                                            double t = f*(Math.pow(0.68,j))*(Math.pow((1-0.68),(n-j)));
                                            threshold = threshold+t;
                                            }
                                    }else{
                                        for(int j = 1; j <= n; j++){
                                            int f = binomial(n, j);
                                            double t = f*(Math.pow(0.26,j))*(Math.pow((1-0.26),(n-j)));
                                            threshold = threshold+t;
                                            }
                                        }
                                        double p1 = random.nextDouble();
                                        if(p1 < threshold){
                                            msl_hits.put(r, true);
                                        }
                                    break;
                                    }
                            }else{
                                continue;
                        }
                    }
                }
            }
        }
    }
    
    static int binomial(int n, int k){
        if(k > n){
            return 0;
        }if(k == 0 || k == n){
            return 1;
        }
        
        return binomial(n-1, k-1)+binomial(n-1, k);
    }
    
    public static String simulate(){
        boolean complete = false;
        
        blue.clear();
        red.clear();
        
        for(int i = 0; i < suwUSV; i++){
            String name = "SUW"+i;
            Combatant usv = new Combatant(name, 50, 250, usv_missiles, usv_defcab, true);
            blue.add(usv);
        }
        
        for(int i = 0; i < isrUSV; i++){
            String name = "ISR"+i;
            Combatant usv = new Combatant(name, 50, 250, 0, usv_defcab, true);
            blue.add(usv);
        }
        
        for(int i = 0; i < BC; i++){
            String name = "Corvette"+i;
            Combatant corv = new Combatant(name, 120, 3500, corvette_missiles, corv_defcab, false);
            blue.add(corv);
        }
        
        for(int i = 0; i < BFAC; i++){
            String name = "FAC"+i;
            Combatant fac = new Combatant(name, 70, 500, fac_missiles, fac_defcab, false);
            blue.add(fac);
        }
        
        for(int i = 0; i < RC; i++){
            String name = "Light fregate"+i;
            Combatant corv = new Combatant(name, 120, 3500, corvette_missiles, corv_defcab, false);
            red.add(corv);
        }

        int w = 0;
     
        while(complete == false){
                if(w > 10){
                    return "draw";
                }
                msl_hits.clear();
                
                red_engagement();
                blue_engagement();
                                
                w++;
                
                msl_hits.forEach(
                    (vessel, hit)
                        -> vessel.takeHit(hit));

                for(int j = 0; j < blue.size(); j++){
                    Combatant o = blue.get(j);
                    if(o.inAction() == false){
                        blue.remove(j);
                    }
                }

                for(int j = 0; j < red.size(); j++){
                    Combatant o = red.get(j);
                    if(o.inAction() == false){
                        red.remove(j);
                    }
                }
                
                if(blue.isEmpty()){
                    if(red.isEmpty() == false){
                        return "red";
                    }
                    else{
                        return "draw";
                    }
                }
                
                if(red.isEmpty()){
                    if(blue.isEmpty() == false){
                        return "blue";
                    }else{
                        return "draw";
                    }
                }
                
                boolean red_msl = false;
                boolean blue_msl = false;
                
                for(Combatant bm : blue){
                    if(bm.available()){
                        blue_msl = true;
                        break;
                    }
                }
                
                for(Combatant rm : red){
                    if(rm.available()){
                        red_msl = true;
                        break;
                    }
                }
                
                if(blue_msl == false && red_msl == false){
                    if(blue.size() > red.size()){
                        return "blue";
                    }   
                    if(red.size() > blue.size()){
                        return "red";
                    }
                }
            }
        return null;
        }
    
@Override
protected Object clone() throws CloneNotSupportedException{
    return super.clone();
    }
}




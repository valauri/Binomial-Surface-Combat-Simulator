/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulaattori;

/**
 *
 * @author l_vas
 */
public class Combatant {
    private String name;
    private double loa;
    private double tonnage;
    private int missiles;
    private int defcab;
    private boolean operational;
    private boolean was;
    private boolean status;
    
    public Combatant(String n, double a, double b, int m, int d, boolean um){
        name = n;
        loa = a;
        tonnage = b;
        missiles = m;
        defcab = d;
        operational = true;
        status = um;
    }
    
    public String pennant(){
        return name;
    }
    
    public boolean was(){
        return was;
    }
    
    public void assign(){
        was = true;
    }
    
    public void deassign(){
        was = false;
    }
    
    public void fire(int a){
        if(missiles - a > 0){
            missiles = missiles - a;
        }else{
            missiles = 0;
        }
    }
    
    public int msl_cap(){
        return missiles;
    }
    
    public boolean available(){
        if(missiles > 0 && operational){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean inAction(){
        return operational;
    }
    
    public void takeHit(boolean t){
        if(t == true){
            operational = false;
            missiles = 0;
        }
    }
    
    public double loa(){
        return(loa);
    }
    
    public boolean unmanned(){
        return status;
    }

}

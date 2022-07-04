/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Simulaattori;

/**
 *
 * @author l_vas
 */
import java.util.*;

public class Solver extends Main
{
    private ArrayList<Item> composition;
    private ArrayList<String[]> solutions;

    Solver() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static class Item
    {
        public String name;
        public double price;

        public Item(String name, double price)
        {
            this.name = name;
            this.price = price;
        }
    }

    public Solver(ArrayList<Item> menu)
    {
        this.composition = menu;
    }

    public ArrayList<String[]> solve(double budget)
    {
        solutions = new ArrayList<String[]>();
        solve(new ArrayList<Item>(), 0, budget);
        return solutions;
    }

    public void solve(ArrayList<Item> items, int first, double budget)
    {
        if(budget<=10 && budget >= 0)
        {
            // We have found a solution, store it
            solutions.add(items.stream().map(e -> e.name).toArray(String[]::new));
        }
        else
        {
            // Search for an item that fits in the budget
            for(int i=first;i<composition.size();i++)
            {
                Item item = composition.get(i);
                if(item.price<=budget)
                {
                    items.add(item);
                    solve(items, i, budget-item.price);
                    items.remove(items.size()-1);
                }
            }
        }
    }
}
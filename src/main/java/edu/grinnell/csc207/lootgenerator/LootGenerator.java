package edu.grinnell.csc207.lootgenerator;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class LootGenerator {
    /** The path to the dataset (either the small or large set). */
    private static final String DATA_SET = "data/small";
    
    public static class Node{
        String Class;
        String Type;
        int Level;
        String TreasureClass;
        public Node(String Class, String Type, int Level, String TreasureClass){
            this.Class = Class;
            this.Type = Type;
            this.Level = Level;
            this.TreasureClass = TreasureClass;
        }
    }

    public static ArrayList<Node> scanMonstats(Scanner text){
        ArrayList<Node> arr = new ArrayList<Node>();
        text.useDelimiter("\t");
        while(text.hasNext() && text.hasNextLine()){
            int n = 0;
            if(text.hasNextInt()){
                n = text.nextInt();
            }
            String Class = text.next();
            String Type = text.next();
            n = Integer.parseInt(text.next());
            String Treasure = text.next();
            Node each = new Node(Class.trim(), Type.trim(), n, Treasure.trim());
            arr.add(each);
        }
        return arr;
    }

    public static String scanTC(Scanner text, String TC){
        HashMap<String, ArrayList<String>> TCmap =new HashMap<String, ArrayList<String>>();
        text.useDelimiter("\t");
        while(text.hasNext() && text.hasNextLine()){
            String Key = text.next().trim();
            String Item1 = text.next().trim();
            String Item2 = text.next().trim();
            String Item3 = text.next().trim();
            ArrayList<String> arr = new ArrayList<String>();
            arr.add(Item1);
            arr.add(Item2);
            arr.add(Item3);
            TCmap.put(Key, arr);
        }
        Random rand = new Random();
        
        while(TCmap.containsKey(TC)){
            TC = TCmap.get(TC).get(rand.nextInt(2));
        }
        return TC;
    }

    public static int scanArmor(Scanner text, String Item){
        HashMap<String, ArrayList<String>> Armmap =new HashMap<String, ArrayList<String>>();
        text.useDelimiter("\t");
        while(text.hasNext() && text.hasNextLine()){
            String Key = text.next().trim();
            String Min = text.next().trim();
            String Max = text.next().trim();
            ArrayList<String> arr = new ArrayList<String>();
            arr.add(Min);
            arr.add(Max);
            Armmap.put(Key, arr);
        }
        Random rand = new Random();
        
        int min = Integer.parseInt(Armmap.get(Item).get(0));
        int max = Integer.parseInt(Armmap.get(Item).get(1));
        if(min == max){
            return min;
        }
        int def = rand.nextInt(min, max);
        return def;
    }

    public static ArrayList<String> scanPrefix(Scanner text){
        HashMap<String, ArrayList<String>> premap =new HashMap<String, ArrayList<String>>();
        text.useDelimiter("[\t\n]");
        ArrayList<String> possibility = new ArrayList<String>();
        while(text.hasNext() && text.hasNextLine()){
            String Key = text.next().trim();
            String value = text.next().trim();
            String Min = text.next().trim();
            String Max = text.next().trim();
            ArrayList<String> arr = new ArrayList<String>();
            arr.add(value);
            arr.add(Min);
            arr.add(Max);
            arr.add(Key);
            possibility.add(Key);
            premap.put(Key, arr);
        }
        Random rand = new Random();
        String Prefix = possibility.get(rand.nextInt(possibility.size()));

        return premap.get(Prefix);
    }

    public static ArrayList<String> scanSuffix(Scanner text){
        HashMap<String, ArrayList<String>> sufmap =new HashMap<String, ArrayList<String>>();
        text.useDelimiter("[\t\n]");
        ArrayList<String> possibility = new ArrayList<String>();
        while(text.hasNext() && text.hasNextLine()){
            String Key = text.next().trim();
            String value = text.next().trim();
            String Min = text.next().trim();
            String Max = text.next().trim();
            ArrayList<String> arr = new ArrayList<String>();
            arr.add(value);
            arr.add(Min);
            arr.add(Max);
            arr.add(Key);
            possibility.add(Key);
            sufmap.put(Key, arr);
        }
        Random rand = new Random();
        String Suffix = possibility.get(rand.nextInt(possibility.size()));

        return sufmap.get(Suffix);
    }

    public static int affixStat(ArrayList<String> affix){
        int min = Integer.parseInt(affix.get(1));
        int max = Integer.parseInt(affix.get(2));
        Random rand = new Random();
        if(min == max){
            return min;
        }
        return rand.nextInt(min,max);
    }

    public static void main(String[] args) throws IOException{
        Scanner monstats = new Scanner(new File("/home/davidson2/csc207/LootGenerator/data/large/monstats.txt"));
        Scanner TC = new Scanner (new File("/home/davidson2/csc207/LootGenerator/data/large/TreasureClassEx.txt"));
        Scanner armor = new Scanner (new File("/home/davidson2/csc207/LootGenerator/data/large/armor.txt"));
        Scanner prefscanner = new Scanner(new File("/home/davidson2/csc207/LootGenerator/data/small/MagicPrefix.txt"));
        Scanner suffscanner = new Scanner(new File("/home/davidson2/csc207/LootGenerator/data/small/MagicSuffix.txt"));

        System.out.println("This program kills monsters and generates loot!");
        Random rand = new Random();
        ArrayList<Node> monsters = scanMonstats(monstats);
        int mon = rand.nextInt(monsters.size());
        System.out.println("Fighting " + monsters.get(mon).Class);

        System.out.println("You have slain the " + monsters.get(mon).Class + "!");
        System.out.println(monsters.get(rand.nextInt(monsters.size())).Class + " Dropped:\n");

        String item = scanTC(TC, monsters.get(mon).TreasureClass);

        ArrayList<String> Prefix = scanPrefix(prefscanner);
        ArrayList<String> Suffix = scanPrefix(suffscanner);
        int include = rand.nextInt(3);
        if(include == 0){
            System.out.println(item);
            System.out.println("Defense: " + scanArmor(armor, item));
        }else if(include == 1){
            System.out.println(Prefix.get(3)+ " " + item);
            System.out.println("Defense: " + scanArmor(armor, item));
            System.out.println(affixStat(Prefix) + " " + Prefix.get(0));
        }else if(include == 2){
            System.out.println(item + " " + Suffix.get(3));
            System.out.println("Defense: " + scanArmor(armor, item));
            System.out.println(affixStat(Suffix) + " " + Suffix.get(0));
        }else{
            System.out.println(Prefix.get(3)+ " " + item + " " + Suffix.get(3));
            System.out.println("Defense: " + scanArmor(armor, item));
            System.out.println(affixStat(Prefix) + " " + Prefix.get(0));
            System.out.println(affixStat(Suffix) + " " + Suffix.get(0));
        }

    }
}
 
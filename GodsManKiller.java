package scripts.tribot.ManKiller;

import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Login;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Login.STATE;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.AntiBan;
import scripts.PersistentABCUtil;
import scripts.tribot.lib.game.observers.FCInventoryListener;
import scripts.tribot.lib.game.observers.FCInventoryObserver;
import scripts.tribot.ManKiller.Node;
import scripts.tribot.ManKiller.Methods.*;
import scripts.tribot.ManKiller.SkillTracker.SkillTracker;
import scripts.tribot.ManKiller.Utilities.Constants;
import scripts.tribot.ManKiller.Utilities.Variables;

@ScriptManifest(authors = { "Godspower33" }, category = "Combat", name = "Gods Man Killer", 
description = "Kills oridinary men in edgeville for their evil deeds and steals their herb.")

public class GodsManKiller extends Script implements Painting, FCInventoryListener {

	public ArrayList<Node> nodes = new ArrayList<Node>();
	private final FCInventoryObserver INV_OBSERVER = new FCInventoryObserver();
	private SkillTracker XP = new SkillTracker();
	
	// --- ABC --- \\
    public static ABCUtil abc = new ABCUtil();
    public static PersistentABCUtil abc2 = new PersistentABCUtil();
    public static int eatAt = abc.generateEatAtHP();
	
	// ---- Xp stuff ---- \\
	int mageXp;
	int rangeXp;
	int defXp;
	int attXp;
	int strXp;
	
    // ---- Ints for counting herbs ---- \\
    private int harr, ranarr, irit, avantoe, kwuarm, cadantine, dwarf, lanta;
	
    // --- Get prices --- \\
    int harralanderP = getPrice(Constants.GRIMY_HARRALANDER);
    int ranarrP = getPrice(Constants.GRIMY_RANARR);
    int iritP = getPrice(Constants.GRIMY_IRIT);
    int avantoeP = getPrice(Constants.GRIMY_AVANTOE);
    int kwuarmP = getPrice(Constants.GRIMY_KWUARM);
    int cadantineP = getPrice(Constants.GRIMY_CADANTINE);
    int dwarfP = getPrice(Constants.GRIMY_DWARF);
    int lantaP = getPrice(Constants.GRIMY_LANTA);
    
	public static boolean stopScript = false;
	public static String status;
	
	@Override
	public void run() {
		Mouse.setSpeed(General.random(88, 108));
		General.useAntiBanCompliance(true);
		INV_OBSERVER.addListener(this);
		ThreadSettings.get().setClickingAPIUseDynamic(true);
		if (Login.getLoginState() == STATE.INGAME) {
			initialize();
		}
		while (!stopScript) {
			if (Login.getLoginState() != STATE.LOGINSCREEN) {
				for (final Node n : nodes) {
					if (n.isNodeValid()) {
						n.execute();
					} else {
						status = "AntiBan";
			            AntiBan.timedActions();
					}
					General.sleep(20, 35);
				}
			}
			General.sleep(75, 105);
		}		
	}
	
	private void initialize() {		
		mageXp = Skills.SKILLS.MAGIC.getXP();
		rangeXp = Skills.SKILLS.RANGED.getXP();
		defXp = Skills.SKILLS.DEFENCE.getXP();
		attXp = Skills.SKILLS.ATTACK.getXP();
		strXp = Skills.SKILLS.STRENGTH.getXP();
        Variables.foodID = Integer.parseInt(JOptionPane.showInputDialog( "Enter food ID: "));
        Variables.foodAmount = Integer.parseInt(JOptionPane.showInputDialog( "Enter food amount to withdraw: "));
        Variables.hpToEatAt = Integer.parseInt(JOptionPane.showInputDialog( "Enter HP percentage to eat at: "));
		
		Collections.addAll(nodes, new Attack(), new Bank(),
				new HandleWalking(), new Loot(), new EatFood(),
				new StuckUpLadder());
	}
	
    private static final long startTime = System.currentTimeMillis();
    Font font = new Font("Verdana", Font.BOLD, 12);
    
    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }
    private final Image IMG = getImage("http://imgur.com/sNhCwQT.png"); // http://i.imgur.com/TUlYgDr.png
    
	public void onPaint(Graphics g) {
		Graphics2D gg = (Graphics2D)g;
        gg.drawImage(IMG, 200, 345, null);
        
		long timeRan = System.currentTimeMillis() - startTime;
		long xpPerHr = XP.getTotalGainedXp() * (3600000 / timeRan);
		int profit = (harralanderP * harr) + (ranarrP * ranarr) + (iritP * irit) + (avantoeP * avantoe) 
		+ (kwuarmP * kwuarm) + (cadantineP * cadantine) + (dwarfP * dwarf) + (lantaP * lanta);
		int profitPerHr = (int) (profit * 3600000d / timeRan);
		
		   g.setFont(font);
	       g.setColor(Color.WHITE);
	       g.drawString("Men Killer V1.0", 279, 360);
	       g.drawString("Running for: " + Timing.msToString(timeRan), 279, 375);
	       g.drawString("Status : " + status, 279, 390);
	       g.drawString("Xp Gained: " + (XP.getTotalGainedXp()) + " (" + xpPerHr + "/hr)", 279, 405);
	       g.drawString("Profit: " + profit + " (" + profitPerHr + "/hr)", 279, 420);
	}

	@Override
	public void inventoryAdded(String i, int count) {
		if (i.equals("Grimy avantoe") && count == 1) {
			avantoe++;
		}
		if (i.equals("Grimy cadantine") && count == 1) {
			cadantine++;
		}
		if (i.equals("Grimy harralander") && count == 1) {
			harr++;
		}
		if (i.equals("Grimy ranarr weed") && count == 1) {
			ranarr++;
		}
		if (i.equals("Grimy irit leaf") && count == 1) {
			irit++;
		}
		if (i.equals("Grimy kwuarm")  && count == 1) {
			kwuarm++;
		}
		if (i.equals("Grimy dwarf weed") && count == 1) {
			dwarf++;
		}
		if (i.equals("Grimey lantadyme") && count == 1) {
			lanta++;
		}
	}

	@Override
	public void inventoryRemoved(String id, int count) {
		// TODO Auto-generated method stub
		
	}
	
    // Gets the price of an item from the rsbuddy api
    private int getPrice(int id){
        try {
            URL url = new URL("http://api.rsbuddy.com/grandExchange?a=guidePrice&i=" + id);
            URLConnection con = url.openConnection();
            con.setUseCaches(true);
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String[] data = br.readLine().replace("{", "").replace("}", "").split(",");
            return Integer.parseInt(data[0].split(":")[1]);
        } catch(Exception e){
        }
        return -1;
    }
}

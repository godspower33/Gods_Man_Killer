package scripts.tribot.ManKiller.Methods;

import java.util.ArrayList;
import java.util.List;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;

import scripts.AntiBan;
import scripts.tribot.ManKiller.GodsManKiller;
import scripts.tribot.ManKiller.Node;
import scripts.tribot.ManKiller.Locations.Locations;
import scripts.tribot.ManKiller.Utilities.Utils;

public class HandleWalking extends Node {

	// Holder table for all previous generated times.
	List<Integer> abc2WaitTimes = new ArrayList<>();
	
	@Override
	public boolean isNodeValid() {
		return (Inventory.isFull() || !Utils.doWeHaveFood()) 
				|| (!Locations.MENS_HOUSE.getArea().contains(Player.getPosition()) 
					&& Utils.doWeHaveFood() && !Utils.isUpLadder() 
					&& !Inventory.isFull());
	}

	@Override
	public void execute() {
		abc2WaitTimes.add(General.random(1200, 2800));
		if (Inventory.isFull() || !Utils.doWeHaveFood()) {
			walkToBank();
		}
		if (!Locations.MENS_HOUSE.getArea().contains(Player.getPosition()) 
					&& Utils.doWeHaveFood() && !Utils.isUpLadder() 
					&& !Inventory.isFull()) {
			walkToMen();
		}
	}
	
	private void walkToMen() {
		GodsManKiller.status = "Walking to Men";
		if (WebWalking.walkTo(Locations.MEN_MINI_AREA.getArea().getRandomTile())) {
			AntiBan.generateTrackers(Utils.calculateAverage(abc2WaitTimes), false);
			abc2WaitTimes.add(AntiBan.getReactionTime());
			General.println("Sleeping for " + AntiBan.getReactionTime());
			AntiBan.sleepReactionTime();
	      	Timing.waitCondition(new Condition() {
    			@Override
    			public boolean active() {
    				General.sleep(100, 200);
    				return Locations.MENS_HOUSE.getArea().contains(Player.getPosition());
    			}
    		}, General.random(6000, 10900));
		}
	}
	
	private void walkToBank() {
		GodsManKiller.status = "Walking to Bank";
		if (WebWalking.walkTo(Locations.BANK.getArea().getRandomTile())) {
			AntiBan.generateTrackers(Utils.calculateAverage(abc2WaitTimes), false);
			abc2WaitTimes.add(AntiBan.getReactionTime());
			General.println("Sleeping for " + AntiBan.getReactionTime());
			AntiBan.sleepReactionTime();
			Timing.waitCondition(new Condition() {
				public boolean active() {
					General.sleep(100, 200);
					return Locations.BANK.getArea().contains(Player.getPosition());
				}
			}, General.random(1500, 2500));
		}
	}
	
}

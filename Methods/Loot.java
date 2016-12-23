package scripts.tribot.ManKiller.Methods;

import java.util.ArrayList;
import java.util.List;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItemDefinition;

import scripts.AntiBan;
import scripts.tribot.ManKiller.GodsManKiller;
import scripts.tribot.ManKiller.Node;
import scripts.tribot.ManKiller.Utilities.Utils;

public class Loot extends Node {

	// Holder table for all previous generated times.
	List<Integer> abc2WaitTimes = new ArrayList<>();
	
	// --- Looting --- \\
    private final String[] NAME = { "Grimy avantoe", "Grimy irit leaf", "Grimy kwuarm", "Chaos rune", 
    		"Grimy ranarr weed", "Grimy lantadyme", "Grimy dwarf weed", "Grimy cadantine", "Grimy harralander"};
	
	@Override
	public boolean isNodeValid() {
		return lootOnScreen() && !Inventory.isFull();
	}

	@Override
	public void execute() {
		GodsManKiller.status = "Looting";
		abc2WaitTimes.add(General.random(1200, 2800));
		final RSGroundItem[] HERB = GroundItems.findNearest(NAME);
		if (HERB.length > 0) {
			if (Inventory.isFull() && Utils.doWeHaveFood()) {
				EatFood.eat();
			}
			if (!HERB[0].isOnScreen())
				WebWalking.walkTo(HERB[0].getPosition());
			Camera.turnToTile(HERB[0]);

			RSItemDefinition def = HERB[0].getDefinition();
			String itemName = null;
			if (def != null) {
				itemName = def.getName();
			}
			if (Clicking.click("Take " + itemName, HERB[0])) {
				AntiBan.generateTrackers(Utils.calculateAverage(abc2WaitTimes), false);
				abc2WaitTimes.add(AntiBan.getReactionTime());
				General.println("Sleeping for " + AntiBan.getReactionTime());
				AntiBan.sleepReactionTime();
				Timing.waitCondition(new Condition() {
					public boolean active() {
						General.sleep(100, 200);
						return GroundItems.getAt(HERB[0].getPosition()).length < 1;
					}
				}, General.random(1600, 2050));
			}
		}
	}
	
	private boolean lootOnScreen() {
		return GroundItems.findNearest(NAME).length > 0;
	}
}

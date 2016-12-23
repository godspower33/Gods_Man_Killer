package scripts.tribot.ManKiller.Methods;

import java.util.ArrayList;
import java.util.List;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;

import scripts.AntiBan;
import scripts.tribot.ManKiller.GodsManKiller;
import scripts.tribot.ManKiller.Node;
import scripts.tribot.ManKiller.Locations.Locations;
import scripts.tribot.ManKiller.Utilities.Constants;
import scripts.tribot.ManKiller.Utilities.Utils;

public class StuckUpLadder extends Node {

	// Holder table for all previous generated times.
	List<Integer> abc2WaitTimes = new ArrayList<>();
	
	@Override
	public boolean isNodeValid() {
		return Utils.isUpLadder();
	}

	@Override
	public void execute() {
		GodsManKiller.status = "Stuck, going back down ladder";
		abc2WaitTimes.add(General.random(1200, 2800));
		RSObject[] ladder = Objects.find(9, Constants.MENS_HOUSE_UPSTAIRS_LADDER);
		if (ladder.length > 0) {
            if (Clicking.click("Climb-down", ladder[0])) {
    			AntiBan.generateTrackers(Utils.calculateAverage(abc2WaitTimes), false);
    			abc2WaitTimes.add(AntiBan.getReactionTime());
    			General.println("Sleeping for " + AntiBan.getReactionTime());
    			AntiBan.sleepReactionTime();
	            Timing.waitCondition(new Condition() {
	                public boolean active() {
	                    General.sleep(100);
	                    return Locations.MENS_HOUSE.getArea().contains(Player.getPosition());
	                }
	            }, General.random(1500, 2200));	
			}
		}
	}

}

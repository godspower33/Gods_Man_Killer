package scripts.tribot.ManKiller.Methods;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;

import scripts.tribot.ManKiller.GodsManKiller;
import scripts.tribot.ManKiller.Node;
import scripts.tribot.ManKiller.Utilities.Constants;
import scripts.tribot.ManKiller.Utilities.Utils;
import scripts.tribot.ManKiller.Utilities.Variables;

public class Bank extends Node {

	@Override
	public boolean isNodeValid() {
		return nearBooth() && (!Utils.doWeHaveFood() || Inventory.isFull());
	}

	@Override
	public void execute() {
		GodsManKiller.status = "Banking";
		if (!Banking.isBankScreenOpen()) {
			if (Banking.openBank()) {
				Timing.waitCondition(new Condition() {
					public boolean active() {
						General.sleep(100, 200);
						return Banking.isBankScreenOpen();
					}
				}, General.random(2500, 3500));
			}
		}
		if (Banking.isBankScreenOpen()) {
			if (Utils.isBankItemsLoaded()) {
				if (!doesBankHaveFood()) {
					GodsManKiller.stopScript = true;
				}
			}
			if (Inventory.isFull() || !Utils.doWeHaveFood()) {
				Banking.depositAll();
				Timing.waitCondition(new Condition() {
					public boolean active() {
						General.sleep(100, 200);
						return !Inventory.isFull();
					}
				}, General.random(1500, 2500));
			}
			if (!Inventory.isFull()) {
				if (Banking.withdraw(Variables.foodAmount, Variables.foodID)) {
		            Timing.waitCondition(new Condition() {
		                public boolean active() {
		                    General.sleep(100, 200);
		                    return Inventory.getCount(Variables.foodID) > 0;
		                }
		            }, General.random(3500, 4000));
				}
			}
		}
	}

	// near bank booth?
	private boolean nearBooth() {
		RSObject[] booth = Objects.findNearest(10, "Bank Booth");
		if (booth.length > 0) {
			if (PathFinding.canReach(booth[0].getPosition(), true) && booth[0].getPosition().distanceTo(Player.getPosition()) < 8 
					&& Player.getPosition().distanceTo(Constants.BANK_SPOT) < 8) {
				return true;
			}
		}
		return false;
	}
	
	// do we have food in the bank
	private boolean doesBankHaveFood() {
		return Banking.find(Variables.foodID).length > 0;
	}
}

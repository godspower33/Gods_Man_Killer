package scripts.tribot.ManKiller.Methods;

import java.util.ArrayList;
import java.util.List;

import org.tribot.api.Clicking;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSNPC;

import scripts.AntiBan;
import scripts.tribot.ManKiller.GodsManKiller;
import scripts.tribot.ManKiller.Node;
import scripts.tribot.ManKiller.Locations.Locations;
import scripts.tribot.ManKiller.Utilities.Utils;

public class Attack extends Node {

	// Holder table for all previous generated times.
	List<Integer> abc2WaitTimes = new ArrayList<>();

	@Override
	public boolean isNodeValid() {
		return Locations.MENS_HOUSE.getArea().contains(Player.getPosition())
				&& (Utils.doWeHaveFood() && !Inventory.isFull())
				&& !Player.getRSPlayer().isInCombat()
				&& Combat.getAttackingEntities() != null
				&& Player.getRSPlayer().getInteractingCharacter() == null;
	}

	@Override
	public void execute() {
		GodsManKiller.status = "Killing a man";
		RSNPC[] man = NPCs.find("Man");
		if (man.length > 0) {
			if (abc2WaitTimes.isEmpty()) {
				AntiBan.generateTrackers(General.random(800, 1200), false);
			}
			if (!abc2WaitTimes.isEmpty()) {
				AntiBan.generateTrackers(Utils.calculateAverage(abc2WaitTimes), false);
			}
			int reactionTime = AntiBan.getReactionTime();
			abc2WaitTimes.add(reactionTime);
			AntiBan.sleepReactionTime();
			final RSNPC target = AntiBan.selectNextTarget(man);
			if (!target.isOnScreen())
				Camera.turnToTile(target);
			if (!PathFinding.canReach(target, true)) {
				if (WebWalking.walkTo(target)) {
					AntiBan.generateTrackers(Utils.calculateAverage(abc2WaitTimes), false);
					abc2WaitTimes.add(AntiBan.getReactionTime());
					General.println("Sleeping for " + AntiBan.getReactionTime());
					AntiBan.sleepReactionTime();
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return PathFinding.canReach(target, true);
						}
					}, General.random(2500, 3700));
				}
			}
			long startTime = System.currentTimeMillis();
			if (!target.isInCombat() && !Utils.isInCombat()) {
				if (Clicking.click("Attack Man", target)) {
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Utils.isInCombat();
						}
					}, General.random(2500, 3700));
				}
			}
			while (Player.getRSPlayer().isInCombat()) {
				AntiBan.timedActions();
				if (Combat.getTargetEntity().getHealthPercent() > 30) {
					if (AntiBan.getShouldHover()) {
						AntiBan.hoverEntity(man);
						AntiBan.resetShouldHover();
					}
				}
				if (Combat.getTargetEntity().getHealthPercent() < 30) {
					if (AntiBan.getShouldOpenMenu()) {
						if (DynamicClicking.clickRSNPC(target, 3)) {
							Timing.waitCondition(new Condition() {
								@Override
								public boolean active() {
									General.sleep(100, 200);
									return ChooseOption.isOpen();
								}
							}, General.random(2500, 3700));
						}
						AntiBan.resetShouldOpenMenu();
					}
				}
			}
			if (!Player.getRSPlayer().isInCombat()) {
				AntiBan.generateTrackers((int) (System.currentTimeMillis() - startTime), false);
				abc2WaitTimes.add(AntiBan.getReactionTime());
			}
		}
	}
}

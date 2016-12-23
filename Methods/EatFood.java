package scripts.tribot.ManKiller.Methods;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSItem;

import scripts.tribot.ManKiller.GodsManKiller;
import scripts.tribot.ManKiller.Node;
import scripts.tribot.ManKiller.Utilities.Utils;
import scripts.tribot.ManKiller.Utilities.Variables;

public class EatFood extends Node {

	@Override
	public boolean isNodeValid() {
		return Utils.hpPercent() <= GodsManKiller.eatAt && Utils.doWeHaveFood();
	}

	@Override
	public void execute() {
		eat();
        GodsManKiller.eatAt = GodsManKiller.abc.generateEatAtHP();
	}
	
	// Method for eating food
	public static void eat() {
		GodsManKiller.status = "eating";
		RSItem[] food = Inventory.find(Variables.foodID);
		if (food.length > 0) {
			if (Clicking.click("Eat", food[0])) {
				Timing.waitCondition(new Condition() {
					public boolean active() {
						General.sleep(100, 200);
						return (Skills.SKILLS.HITPOINTS.getCurrentLevel() > Variables.hpToEatAt);
					}
				}, General.random(3500, 4000));
			}
		}
	}
	
}

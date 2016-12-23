package scripts.tribot.ManKiller.Utilities;

import java.util.List;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSInterface;


public class Utils {
	
	// Method for determining if we have food
	public static boolean doWeHaveFood() {
		return Inventory.find(Variables.foodID).length > 0;
	}
	
	// Method to get hp percent
    public static double hpPercent(){
        double currentHP = Skills.SKILLS.HITPOINTS.getCurrentLevel();
        double totalHP = Skills.SKILLS.HITPOINTS.getActualLevel();
        return currentHP / totalHP * 100;   
    }
    
    // Are we in combat?
	public static boolean isInCombat() {
		return Combat.getTargetEntity() != null || Combat.getAttackingEntities().length > 0;
	}
	
	// Determine whether accidently misclicked and went up the ladder in house
	public static boolean isUpLadder() {
		return Player.getPosition().getPlane() == 1;
	}
	
	// Checks if bank items are loaded or not
	public static boolean isBankItemsLoaded() {
        return getCurrentBankSpace() == Banking.getAll().length;
    }
	
	// Gets current bank space
	public static int getCurrentBankSpace() {
		RSInterface amount = Interfaces.get(Constants.BANK_MASTER,Constants.BANK_AMOUNT_CHILD);
		if (amount != null) {
			String txt = amount.getText();
			if (txt != null) {
				try {
					int toInt = Integer.parseInt(txt);
					if (toInt > 0)
						return toInt;
				} catch (NumberFormatException e) {
					return -1;
				}
			}
		}
		return -1;
	}
	
	/**
	 * Method to calculate average wait time from a List of times
	 * @param times 
	 * @return
	 */
	public static int calculateAverage(List<Integer> times) {
		Integer sum = 0;
		if (!times.isEmpty()) {
			for (Integer holder : times) {
				sum += holder;
			}
			return sum.intValue() / times.size();
		}
		return sum;
	}
}

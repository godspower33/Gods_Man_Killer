package scripts.tribot.ManKiller;

public abstract class Node {
	/**
	 * Is action valid?
	 * 
	 * @return
	 */
	public abstract boolean isNodeValid();

	/**
	 * Execute action.
	 */
	public abstract void execute();

	public boolean stringArrayContains(String[] array, String str) {
		for (int i = 0; i < array.length; i++)
			if (array[i].equalsIgnoreCase(str))
				return true;
		return false;
	}
}
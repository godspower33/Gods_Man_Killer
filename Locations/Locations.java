package scripts.tribot.ManKiller.Locations;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public enum Locations {

	MENS_HOUSE("Mens House", new RSArea(new RSTile(3104, 3506, 0), new RSTile(3089, 3514 ,0))),
	MEN_MINI_AREA("Mens house inside", new RSArea(new RSTile(3098, 3511 , 0), new RSTile(3095, 3509 , 0))),
	BANK("Edge Bank", new RSArea(new RSTile(3097, 3497, 0), new RSTile(3094, 3494, 0))),;
	
	@SuppressWarnings("unused")
	private String name;
	private RSArea rsarea;

	Locations(String name, RSArea rsarea) {
		this.name = name;
		this.rsarea = rsarea;
	}

	public RSArea getArea() {
		return this.rsarea;
	}

	public String getName() {
		return this.name();
	}

	public boolean atLocation(RSTile tile) {
		for (RSTile til : this.rsarea.getAllTiles()) {
			if (til.distanceTo(tile) < 1) {
				return true;
			}
		}
		return false;
	}

	public void setArea(RSArea area) {
		this.rsarea = area;
	}
}

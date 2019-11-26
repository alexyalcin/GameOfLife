/**
 * Coord.java 1.0 Nov 11, 2019
 *
 * Copyright (c) 2019 Alexander Yalcin. All rights reserved.
 */
package a8;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex
 *
 */
public class Coord{
	private static List<Coord> created = null;
	
	protected int x, y;
	
	public static Coord newCoord(int _x, int _y) {
		if (created == null) {
			created = new ArrayList<Coord>();
		}
		for (Coord c : created) {
			if (c.equals(new Coord (_x, _y))) {
				return c;
			} 
		}
		Coord c = new Coord(_x, _y);
		created.add(c);
		return c;
	}
	
	public Coord(int _x, int _y) {
		this.x = _x;
		this.y = _y;
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public Coord add(Coord c) {
		return newCoord(this.x + c.x, this.y + c.y);
	}
	
	public String toString() {
		return "X: " + x + "Y: " + y;
	}
	
	@Override
	public boolean equals(Object o) {
		Coord c = (Coord) o;
		return (c.x == x && c.y == y);
	}
}

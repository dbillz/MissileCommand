package com.dan.missileCommand.types;

import java.time.Instant;

public class Triangulation {
	private final Target target;
	private final Location location;
	private final Instant time;
	
	public Triangulation(Target target, Location location, Instant time) {
		super();
		this.target = target;
		this.location = location;
		this.time = time;
	}
	public Target getTarget() {
		return target;
	}
	public Location getLocation() {
		return location;
	}
	public Instant getTime() {
		return time;
	}
	@Override
	public String toString() {
		return "Triangulation [target=" + target + ", location=" + location + ", time=" + time + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Triangulation other = (Triangulation) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}
	
}
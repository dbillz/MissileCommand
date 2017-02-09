package com.dan.missileCommand.types;

import java.time.Instant;

public class Spotting {
	private final Target target; //TODO: replace id with spotting type and confidence, instead of hand waving perfect identification systems.
	private final HeadingVector vector;
	private final Instant time;
	
	private Spotting() {
		target = null;
		vector = null;
		time = null;
	}
	
	public Spotting(Target target, HeadingVector vector, Instant time) {
		super();
		this.target = target;
		this.vector = vector;
		this.time = time;
	}
	
	public Target getTarget() {
		return target;
	}
	
	public HeadingVector getVector() {
		return vector;
	}
	
	public Instant getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "Spotting [target=" + target + ", vector=" + vector + " time=" + time + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((vector == null) ? 0 : vector.hashCode());
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
		Spotting other = (Spotting) obj;
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
		if (vector == null) {
			if (other.vector != null)
				return false;
		} else if (!vector.equals(other.vector))
			return false;
		return true;
	}
	
	
}

package com.dan.missileCommand.worldObjects;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.dan.missileCommand.types.HeadingVector;
import com.dan.missileCommand.types.Location;
import com.dan.missileCommand.types.Spotting;
import com.dan.missileCommand.types.Target;
import com.dan.missileCommand.utils.Constants;

public class World {

	private final int xSize;
	private final int ySize;
	private Map<Target, Location> targets;
	private Map<Location, Target> reverseMapping;
	
	public World(int xSize, int ySize) {
		this.xSize = xSize;
		this.ySize = ySize;
		targets = new HashMap<>();
		reverseMapping = new HashMap<>();
	}
	
	/**
	 * Exclusive add. Multiple targets can't be spawned on the same location.
	 * @param target
	 * @param location
	 * @throws InvalidParameterException
	 */
	public synchronized void addTarget(Target target, Location location) throws InvalidParameterException {
		if (!isLocationInBounds(location)) {
			throw new InvalidParameterException("Location is out of bounds");
		}
		if (reverseMapping.containsKey(location)) {
			throw new InvalidParameterException("Cannot spawn a target on an occupied location");
		}
		targets.put(target,  location);
		reverseMapping.put(location, target);
		System.out.println(target + " created at " + location);
	}
	
	/**
	 * Idempotent remove
	 * @param target
	 */
	public synchronized void removeTarget(Target target) {
		if (targets.containsKey(target)) {
			reverseMapping.remove(targets.get(target));
		}
		targets.remove(target);
		System.out.println(target + " removed");
	}
	
	/**
	 * Secure move. Requires target to exist at the expected location.
	 * @param target
	 * @param currentLocation
	 * @param newLocation
	 */
	public synchronized void moveTarget(Target target, Location currentLocation, Location newLocation) {
		if (!isLocationInBounds(newLocation)) {
			throw new InvalidParameterException("new location is out of bounds");
		}
		if (!targets.containsKey(target)) {
			throw new InvalidParameterException("The target does not exist");
		} 
		if (!reverseMapping.get(currentLocation).equals(target)) {
			throw new InvalidParameterException("The target is not at the expected location");
		}
		reverseMapping.put(newLocation, target);
		targets.put(target, newLocation);
		System.out.println(target + " moved from " + currentLocation + " to " + newLocation);
	}
	
	public synchronized void updateTargetLocations() {
		Set<Target> targetSet = new HashSet<>(targets.keySet());
		for (Target target : targetSet) {
			Location currentLocation = targets.get(target);
			Location newLocation = getNextLocation(currentLocation, target.getHeading(), target.getVelocity());
			if (isLocationInBounds(newLocation)) {
				moveTarget(target, currentLocation, newLocation);
			} else {
				removeTarget(target);
			}
		}
	}
	
	public Optional<Spotting> scanHeading(HeadingVector vector) {
		final Location startingLocation = vector.getSource();
		Location currentLocation = vector.getSource();
		double distance = 0;
		while (isLocationInBounds(currentLocation) && distance < Constants.MAX_DISTANCE) {
			currentLocation = getNextLocation(currentLocation, vector.getHeading(), 1);
			distance = getDistance(currentLocation, startingLocation);
			if (reverseMapping.containsKey(currentLocation)) {
				Target target = reverseMapping.get(currentLocation);
				Spotting spotting = new Spotting(target, vector, Instant.now());
				return Optional.of(spotting);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * returns true if location is in bounds of the world.
	 * @param location
	 * @return
	 */
	private boolean isLocationInBounds(Location location) {
		return location.getxLoc() < xSize 
			&& location.getyLoc() < ySize 
			&& location.getxLoc() >= 0 
			&& location.getyLoc() >= 0;
	}
	
	private Location getNextLocation(Location currentLocation, int heading, int velocity) {
		final double radians = heading * Math.PI / 180.0;
		final int dx = (int) Math.round(Math.cos(radians)) * velocity;
		final int dy = (int) Math.round(Math.sin(radians)) * velocity;
		return new Location(currentLocation.getxLoc() + dx , currentLocation.getyLoc() + dy);
	}
	
	private double getDistance(Location loc1, Location loc2) {
		double dx = Math.pow(loc1.getxLoc() - loc2.getxLoc(), 2);
		double dy = Math.pow(loc1.getyLoc() - loc2.getyLoc(), 2);
		return Math.sqrt(dx + dy);
	}
	
}

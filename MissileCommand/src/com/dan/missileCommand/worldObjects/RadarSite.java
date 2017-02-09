package com.dan.missileCommand.worldObjects;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.IntStream;

import com.dan.missileCommand.interfaces.Observer;
import com.dan.missileCommand.interfaces.Subject;
import com.dan.missileCommand.types.HeadingVector;
import com.dan.missileCommand.types.Location;
import com.dan.missileCommand.types.Spotting;
import com.dan.missileCommand.types.Target;
import com.dan.missileCommand.utils.Constants;

public class RadarSite implements Subject<Spotting> {

	private final Location location;
	private List<Observer<Spotting>> observers;
	private List<Spotting> spottings;
	
	public RadarSite(Location location) {
		this.location = location;
		observers = new ArrayList<>();
		spottings = new ArrayList<>();
	}
	
	public void scan(World world) {
		IntStream.rangeClosed(Constants.MIN_DIRECTION, Constants.MAX_DIRECTIONS).forEach(dir -> scanDirection(dir, world));
		if (!spottings.isEmpty()) {
			dedupeSpottings();
			notifyObserver();
			spottings.clear();
		}
	}
	
	private void dedupeSpottings() {
		//Should use a multimap, didn't pull in guava.
		Map<Target, List<Spotting>> spottingMap = new HashMap<>();
		for (Spotting thisSpotting : spottings) {
			final Target target = thisSpotting.getTarget();
			if (spottingMap.containsKey(target)) {
				List<Spotting> currentList = new ArrayList<>(spottingMap.get(target));
				currentList.add(thisSpotting);
				spottingMap.put(target, currentList);
			} else {
				spottingMap.put(target, Arrays.asList(thisSpotting));
			}
		}
		spottings.clear();
		for (Entry<Target, List<Spotting>> thisEntry : spottingMap.entrySet()) {
			spottings.add(getAverageSpotting(thisEntry.getValue()));
		}
	}
	
	private Spotting getAverageSpotting(List<Spotting> spottings) {
		int headingSum = 0;
		for (Spotting thisSpotting : spottings) {
			headingSum += thisSpotting.getVector().getHeading();
		}
		int averageHeading = headingSum / spottings.size();
		
		Target target = spottings.get(0).getTarget();
		Location sourceLocation = spottings.get(0).getVector().getSource();
		Instant timestamp = spottings.get(0).getTime();
		HeadingVector vector = new HeadingVector(sourceLocation, averageHeading);
		return new Spotting(target, vector, timestamp);
	}
	
	private void scanDirection(int heading, World world) {
		final HeadingVector vector = new HeadingVector(location, heading);
		final Optional<Spotting> thisSpotting = world.scanHeading(vector);
		if (thisSpotting.isPresent()) {
			spottings.add(thisSpotting.get());
		}
	}
	
	@Override
	public void notifyObserver() {
		spottings.forEach(spotting -> reportSpotting(spotting));
	}
	
	private void reportSpotting(Spotting spotting) {
		System.out.println(toString() + " reported " + spotting);
		observers.forEach(thisObserver -> thisObserver.update(spotting));
	}

	@Override
	public void register(Observer<Spotting> observer) {
		observers.add(observer);
	}

	@Override
	public void deregister(Observer<Spotting> observer) {
		observers.remove(observer);
	}
}

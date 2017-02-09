package com.dan.missileCommand.worldObjects;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import com.dan.missileCommand.interfaces.Observer;
import com.dan.missileCommand.types.HeadingVector;
import com.dan.missileCommand.types.Location;
import com.dan.missileCommand.types.Spotting;
import com.dan.missileCommand.types.Target;
import com.dan.missileCommand.types.Triangulation;

public class Listener implements Observer<Spotting> {

	private List<Spotting> spottings;
	private Map<Target, List<Spotting>> targetTrackingMap;
	
	public Listener() {
		spottings = new ArrayList<>();
		targetTrackingMap = new HashMap<>();
	}
	
	@Override
	public void update(Spotting observation) {
		spottings.add(observation);
		
		Target target = observation.getTarget();
		if (targetTrackingMap.containsKey(target)) {
			List<Spotting> spottings = new ArrayList<>(targetTrackingMap.get(target));
			spottings.add(observation);
			targetTrackingMap.put(target, spottings);
		} else {
			targetTrackingMap.put(target, Arrays.asList(observation));
		}
		
	}
	
	public void printSpottings() {
		System.out.println("Spottings: ");
		spottings.forEach(sp -> System.out.println(sp));
	}
	
	/**
	 * Returns the triangulated location for any targets which were observed by two or more radar sites at the same time.
	 * @return
	 */
	public List<Triangulation> getTriangulatedLocations() {
		List<Triangulation> triangulations = new ArrayList<>();
		for (Entry<Target, List<Spotting>> entry : targetTrackingMap.entrySet()) {
			Set<Instant> timestamps = getTimestamps(entry.getValue());
			for (Instant time : timestamps) {
				List<Spotting> spottingsAtTime = getSpottingsAtTime(spottings, time);
				if (spottingsAtTime.size() > 1) {
					Optional<Triangulation> newTriangulation = triangulateSpottings(spottingsAtTime);
/*whoa indentation*/if (newTriangulation.isPresent()) {
						triangulations.add(newTriangulation.get());
					}
				}
			}
		}
		return triangulations;
	}
	
	public Set<Instant> getTimestamps(List<Spotting> spottings) {
		//TODO: use a stream
		Set<Instant> times = new HashSet<>();
		for (Spotting spotting : spottings) {
			times.add(spotting.getTime());
		}
		return times;
	}
	
	public List<Spotting> getSpottingsAtTime(List<Spotting> spottings, Instant time) {
		//TODO: use streams
		List<Spotting> spottingsAtTime = new ArrayList<>();
		for(Spotting spotting: spottings) {
			if (spotting.getTime().equals(time)) {
				spottingsAtTime.add(spotting);
			}
		}
		return spottingsAtTime;
	}
	
	public Optional<Triangulation> triangulateSpottings(List<Spotting> spottings) {
		HeadingVector firstVector = spottings.get(0).getVector();
		HeadingVector secondVector = spottings.get(1).getVector();
		//Swap if first is to right of left.
		if (firstVector.getSource().getxLoc() > secondVector.getSource().getyLoc()) {
			HeadingVector swap = firstVector;
			firstVector = secondVector;
			secondVector = swap;
		}
		int xA = firstVector.getSource().getxLoc();
		int yA = firstVector.getSource().getyLoc();
		int thetaA = firstVector.getHeading();
		int xB = secondVector.getSource().getxLoc();
		int yB = secondVector.getSource().getyLoc();
		int thetaB = secondVector.getHeading();
		
		//points exist in a straight horizontal line
		if (yA == yB && thetaA == thetaB) {
			return Optional.empty();
		}
		
		//points exist in a straight vertical line
		if (xA == xB && thetaA == thetaB) {
			return Optional.empty();
		}
		
		double dyab = Math.abs(yB - yA);
		double dxab = Math.abs(xB- xA);
		double dab = Math.sqrt(Math.pow(dyab, 2) + Math.pow(dxab, 2));
		
		double thetaAB = Math.atan2(dyab, dxab);
		double thetaBA = 90 - thetaAB;
		
		double psiA = thetaA - thetaAB;
		double psiB = 360 - 90 - thetaB - thetaBA;
		double psiT = 180 - psiA - psiB;
		
		double dAT = Math.sin(psiB) * dab / Math.sin(psiT);
		
		double dxAT = dAT / Math.sin(thetaA);
		double dyAT = dAT / Math.cos(thetaA);
		
		double doubleXt = xA + dxAT;
		double doubleYt = yA + dyAT;
		
		int xT = (int) Math.round(doubleXt);
		int yT = (int) Math.round(doubleYt);
		
		Location location = new Location(xT, yT);
		Target target = spottings.get(0).getTarget();
		Instant time = spottings.get(0).getTime();
		
		return Optional.of(new Triangulation(target, location, time));
	}


}



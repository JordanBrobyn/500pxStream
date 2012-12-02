package com.FiveHundredPX.Testpx;

import java.util.ArrayList;

public class Groups {

	public ArrayList<Pair> pairGroup;
	
	public Groups(ArrayList<Pair> _pairs){
		pairGroup = _pairs;
	}
	
	public void addPair(Pair pair){
		pairGroup.add(pair);
	}
}

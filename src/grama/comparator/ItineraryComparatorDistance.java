/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grama.comparator;

import java.util.Comparator;
import java.util.HashMap;
import grama.model.Node;

/**
 *
 * @author etulyon1
 */
public class ItineraryComparatorDistance implements Comparator<Node> {

	HashMap<Node, Integer> map = null;
	
	public ItineraryComparatorDistance(HashMap map) {
		this.map = map;
	}

	@Override
	public int compare(Node arg0, Node arg1) {
		if(map.get(arg0) > map.get(arg1)){
			return 1;
		}else if(map.get(arg0) < map.get(arg1)){
			return -1;
		}
		return 0;
	}
	
}

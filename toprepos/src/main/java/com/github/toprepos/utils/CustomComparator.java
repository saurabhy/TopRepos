package com.github.toprepos.utils;

import java.util.Comparator;

import com.github.toprepos.model.PopularRepo;
/*
 * Comparator used during sorting of repos
 */
public class CustomComparator implements Comparator<PopularRepo>{

	@Override
	public int compare(PopularRepo o1, PopularRepo o2) {
		if(o1.getForks()<o2.getForks())
			return -1;
		else if(o1.getForks()>o2.getForks())
			return 1;
		return 0;
	}

}

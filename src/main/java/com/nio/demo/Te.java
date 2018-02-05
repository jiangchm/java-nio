package com.nio.demo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Te {
	
	public static void main(String[] args) {
		Set<String> set = new HashSet<String>() ;
		set.add("abc") ;
		set.add("123") ;
		Iterator<String> it = set.iterator() ;
		while(it.hasNext()){
			System.out.println(it.next()) ;
			System.out.println(set.size());
			it.remove();
			System.out.println(set.size());
		}
	}

}

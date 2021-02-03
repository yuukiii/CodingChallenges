package com.cfa.exercise;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class AutoCompleteStates {

    /*
        For the given text, return the names of the States that are considered matches.
        There should be no duplicates in the collection.
      A State is a match if it meets one of the following conditions.  Results should be returned
      in the priority listed below where one is highest priority and three is lowest.  If two matches
      are the same priority, those matches should be sorted alphabetically.
      1. Match exact abbreviation (Should be case insensitive)
      2. Match start of state name
      3. Match any part of state name
    */
    public List<String> filterStates(String textEntered) {
        if(null == textEntered || textEntered.isBlank()){
            return new ArrayList<>();
        }
        String searchTerm = textEntered.toLowerCase(Locale.ROOT);
        Predicate<String> prExactAb = s -> s.matches(searchTerm);
        Predicate<String> prStartsWithTxt = s -> s.startsWith(searchTerm);
        Predicate<String> prAnyTxt = s -> s.contains(searchTerm);

        Stream<String> sts = Arrays.stream(State.values()).map(s -> {
            return s.getStateName().toLowerCase(Locale.ROOT);
        });
        Stream<String> sts_abbr = Arrays.stream(State.values()).map(s -> {
            return s.getAbbreviation().toLowerCase(Locale.ROOT);
        });

        Stream<String> lcSts = Arrays.stream(Arrays.stream(State.values()).map(s -> s.getStateName().toLowerCase(Locale.ROOT)).toArray(String[]::new));


        String[] exactAbbrStrm = sts_abbr.filter(prExactAb).toArray(String[]::new);

        ///Priority 1
        List<String> rtnList = Arrays.stream(exactAbbrStrm).map(s -> {
            return State.valueOf(s.toUpperCase(Locale.ROOT)).getStateName();
        }).collect(Collectors.toList());

        //Priority 2
        String[] neSts = sts.filter(prStartsWithTxt).toArray(String[]::new);
        Arrays.stream(neSts).forEach(s -> {
            String stName =  capitalizeString(s);
            if (!rtnList.contains(stName))
                rtnList.add(stName);
        });

        //Priority 3

        String[] neStsLc = lcSts.filter(prAnyTxt).toArray(String[]::new);
        Arrays.stream(neStsLc).sorted().forEach(s -> {
            String stName =  capitalizeString(s);
            if (!rtnList.contains(stName))
                rtnList.add(stName);
        });


        return rtnList;
    }

     String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

}

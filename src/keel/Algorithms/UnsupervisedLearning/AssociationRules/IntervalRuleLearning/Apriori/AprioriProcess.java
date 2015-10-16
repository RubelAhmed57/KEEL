/***********************************************************************

	This file is part of KEEL-software, the Data Mining tool for regression, 
	classification, clustering, pattern mining and so on.

	Copyright (C) 2004-2010
	
	F. Herrera (herrera@decsai.ugr.es)
    L. Sánchez (luciano@uniovi.es)
    J. Alcalá-Fdez (jalcala@decsai.ugr.es)
    S. García (sglopez@ujaen.es)
    A. Fernández (alberto.fernandez@ujaen.es)
    J. Luengo (julianlm@decsai.ugr.es)

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see http://www.gnu.org/licenses/
  
**********************************************************************/

package keel.Algorithms.UnsupervisedLearning.AssociationRules.IntervalRuleLearning.Apriori;

/**
 * <p>
 * @author Written by Nicolò Flugy Papè (Politecnico di Milano) 24/03/2009
 * @author Modified by Diana Martín (dmartin@ceis.cujae.edu.cu) 
 * @version 1.0
 * @since JDK1.6
 * </p>
 */

import java.io.PrintWriter;
import java.util.*;
import java.math.*;

public class AprioriProcess {
  /**
   * <p>
   * It provides the implementation of the algorithm to be run in a process
   * </p>
   */
  
  private double minSupport;
  private double minConfidence;
  private myDataset dataset;
  
  private int nAttr;
  private int nTrans;
  private int pass;
  private int[][] transactions;
  private Item root;
  private int nFrequentItemsets;
  private int nCoveredRecords;
  
  /**
   * <p>
   * It creates a new process for the algorithm by setting up its parameters
   * </p>
   * @param dataset The instance of the dataset for dealing with its records
   * @param minSupport The user-specified minimum support for the mined association rules
   * @param minConfidence The user-specified minimum confidence for the mined association rules
   */
  public AprioriProcess(myDataset dataset, double minSupport, double minConfidence) {
	  this.minSupport = minSupport;
	  this.minConfidence = minConfidence;
	  this.dataset = dataset;
	  
	  this.nAttr = dataset.getnVars();
	  this.nTrans = dataset.getnTrans(); 
	  this.pass = 1;
	  this.transactions = dataset.getFakeTransactions();
	  this.root = new Item(-1);
  }
  
  /**
   * <p>
   * It runs the algorithm for mining association rules
   * </p>
   */
  public void run() {
	  int candidates, pruned;
	  
	  candidates = this.generateFirstCandidates();
	  pruned = this.checkMinSupport(this.root);
	  this.nFrequentItemsets = candidates - pruned;
	    
	  System.out.println("\nPass: " + this.pass + "; Candidate Itemsets: " + candidates + "; Pruned Itemsets: " + pruned + "; Total Frequent Itemsets: " + this.nFrequentItemsets);
	  
	  while( ((candidates-pruned) > 1) && (this.pass < this.nAttr) ) { 
		  this.pass++;
		  
	      candidates = this.generateCandidates(this.root, new ArrayList<Item>(), 1);
	      this.countSupport();
	      pruned = this.checkMinSupport(this.root);
	      this.nFrequentItemsets += (candidates - pruned);
	      
	      System.out.println("Pass: " + this.pass + "; Candidate Itemsets: " + candidates + "; Pruned Itemsets: " + pruned + "; Total Frequent Itemsets: " + this.nFrequentItemsets);
	  }
  }

  /**
   * <p>
   * It constructs a rules set once the algorithm has been carried out
   * </p>
   * @return An array of association rules having both minimum confidence and support
   */
  public ArrayList<AssociationRule> generateRulesSet() {
	  ArrayList<AssociationRule> rules = new ArrayList<AssociationRule>();
	  HashSet<Integer> covered_records = new HashSet<Integer>();

	  this.generateRules(this.root, new ArrayList<Item>(), rules, covered_records);
	  this.nCoveredRecords = covered_records.size();

	  return rules;
  }
  
   /**
   * <p>
   * It prints out on screen relevant information regarding the mined association rules
   * </p>
   * @param rules The array of association rules from which gathering relevant information
   */
  public void printReport(ArrayList<AssociationRule> rules) {
	  int r;
	  double avg_sup = 0.0, avg_yulesQ = 0.0, avg_conf = 0.0,avg_lift = 0.0,avg_conv = 0.0, avg_CF = 0.0, avg_netConf = 0.0, avg_ant_length = 0.0;	  
	  AssociationRule ar;
	  
	  for (r=0; r < rules.size(); r++) {
		  ar = rules.get(r);
		  
		  avg_sup += ar.getRuleSupport();
		  avg_conf += ar.getConfidence();
		  avg_lift += ar.getLift();
		  avg_conv += ar.getConv();
		  avg_CF += ar.getCF();
		  avg_netConf += ar.getNetConf();
		  avg_yulesQ += ar.getYulesQ();
		  avg_ant_length += ar.getAntecedent().size()+ ar.getConsequent().size();

	  }
	  
	  System.out.println("\nNumber of Frequent Itemsets found: " + this.nFrequentItemsets);
	  System.out.println("Number of Association Rules generated: " + rules.size());
	  
	  if (! rules.isEmpty()) {
		  System.out.println("Average Support: " + roundDouble(( avg_sup / rules.size() ),2));
		  System.out.println("Average Confidence: " + roundDouble(( avg_conf / rules.size() ),2));
		  System.out.println("Average Lift: " + roundDouble(( avg_lift / rules.size() ),2));
		  System.out.println("Average Conviction: " + roundDouble(( avg_conv/ rules.size() ),2));
		  System.out.println("Average Certain Factor: " + roundDouble(( avg_CF/ rules.size()),2));
		  System.out.println("Average Netconf: " + roundDouble(( avg_netConf/ rules.size()),2));
		  System.out.println("Average YulesQ: " + roundDouble(( avg_yulesQ/ rules.size()),2));
		  System.out.println("Average Number of Antecedents: " + roundDouble(( avg_ant_length / rules.size() ),2));
		  System.out.println("Number of Covered Records (%): " + roundDouble(( (100.0 * this.nCoveredRecords) / this.nTrans),2) );
	  }
  }
  
  public void saveReport(ArrayList<AssociationRule> rules,PrintWriter w) {
	  int r;
	  double avg_sup = 0.0, avg_yulesQ = 0.0, avg_conf = 0.0,avg_lift = 0.0,avg_conv = 0.0, avg_CF = 0.0, avg_netConf = 0.0, avg_ant_length = 0.0;	  
	  AssociationRule ar;
	  
	  for (r=0; r < rules.size(); r++) {
		  ar = rules.get(r);
		  
		  avg_sup += ar.getRuleSupport();
		  avg_conf += ar.getConfidence();
		  avg_lift += ar.getLift();
		  avg_conv += ar.getConv();
		  avg_CF += ar.getCF();
		  avg_netConf += ar.getNetConf();
		  avg_yulesQ += ar.getYulesQ();
		  avg_ant_length += ar.getAntecedent().size()+ ar.getConsequent().size();
		  
	  }
	  
	  w.println("\nNumber of Frequent Itemsets found: " + this.nFrequentItemsets);	
	  System.out.println("\nNumber of Frequent Itemsets found: " + this.nFrequentItemsets);
	  w.println("\nNumber of Association Rules generated: " + rules.size());	 
	  System.out.println("Number of Association Rules generated: " + rules.size());
	  
	  if (! rules.isEmpty()) {
		  w.println("Average Support: " + roundDouble(( avg_sup / rules.size() ),2));
		  System.out.println("Average Support: " + roundDouble(( avg_sup / rules.size() ),2));
		  w.println("Average Confidence: " + roundDouble(( avg_conf / rules.size() ),2));
		  System.out.println("Average Confidence: " + roundDouble(( avg_conf / rules.size() ),2));
		  w.println("Average Lift: " + roundDouble(( avg_lift / rules.size() ),2));
		  System.out.println("Average Lift: " + roundDouble(( avg_lift / rules.size() ),2));
		  w.println("Average Conviction: " + roundDouble(( avg_conv / rules.size() ),2));
		  System.out.println("Average Conviction: " + roundDouble(( avg_conv/ rules.size() ),2));
		  w.println("Average Certain Factor: " + roundDouble(( avg_CF/ rules.size() ),2));
		  System.out.println("Average Certain Factor: " + roundDouble(( avg_CF/ rules.size()),2));
		  w.println("Average Netconf: " + roundDouble(( avg_netConf/ rules.size() ),2));
		  System.out.println("Average Netconf: " + roundDouble(( avg_netConf/ rules.size()),2));
		  w.println("Average YulesQ: " + roundDouble(( avg_yulesQ/ rules.size() ),2));
		  System.out.println("Average YulesQ: " + roundDouble(( avg_yulesQ/ rules.size()),2));
		  w.println("Average Number of Antecedents: " + roundDouble(( avg_ant_length / rules.size() ),2));
		  System.out.println("Average Number of Antecedents: " + roundDouble(( avg_ant_length / rules.size() ),2));
		  w.println("Number of Covered Records (%): " + roundDouble(( (100.0 * this.nCoveredRecords) / this.nTrans),2));
		  System.out.println("Number of Covered Records (%): " + roundDouble(( (100.0 * this.nCoveredRecords) / this.nTrans),2) );
	  }
	  else{
			  w.println("Average Support: " + ( 0.0 ));
			  System.out.println("Average Support: " + (0.0));
			  w.println("Average Confidence: " + ( 0.0 ));
			  System.out.println("Average Confidence: " + (0.0 ));
			  w.println("Average Lift: " + (0.0 ));
			  System.out.println("Average Lift: " + ( 0.0 ));
			  w.println("Average Conviction: " + ( 0.0  ));
			  System.out.println("Average Conviction: " + ( 0.0 ));
			  w.println("Average Certain Factor: " + ( 0.0  ));
			  System.out.println("Average Certain Factor: " + ( 0.0 ));
			  w.println("Average Netconf: " + ( 0.0 ));
			  System.out.println("Average Netconf: " + (0.0));
			  w.println("Average YulesQ: " + ( 0.0 ));
			  System.out.println("Average YulesQ: " + (0.0));
			  w.println("Average Number of Antecedents: " + ( 0.0  ));
			  System.out.println("Average Number of Antecedents: " + ( 0.0 ));
			  w.println("Number of Covered Records (%): " +  (0.0));
			  System.out.println("Number of Covered Records (%): " + (0.0) );
		  }
  }
  
  private int generateFirstCandidates() {
	int i, j, index, generated = 0;
	Item child;
	ArrayList<Item> v = this.root.getChildren();
	
	for (i=0; i < this.nTrans; i++) {
        for (j=0; j < this.nAttr; j++) {
        	child = new Item( this.transactions[i][j] );
        	
        	if ( (index = v.indexOf(child)) == -1 ) {	
        		child.incSupport();
        		this.root.addChild(child);
        		generated++;
        	}
        	else v.get(index).incSupport();
        }
    }
    
    return generated;
  }
  
  private int checkMinSupport(Item item) {
	int i, pruned = 0;
	Item child;
	ArrayList<Item> v = item.getChildren(); 
    ArrayList<Item> v_tmp = new ArrayList<Item>(v);
    
    for (i=0; i < v_tmp.size(); i++) { 
    	child = v_tmp.get(i);
    	
    	if ( ((double)child.getSupport() / (double)this.nTrans) < this.minSupport ) {
    		v.remove(child);
    		pruned++;
    	}
    	else {
    		pruned += checkMinSupport(child);
    	}
    }
    
    return pruned;
  }
  
  private int generateCandidates(Item item, ArrayList<Item> current, int depth) {
	int i, generated = 0;
	Item child;
	ArrayList<Item> v = item.getChildren(); 
    
    for (i=0; i < v.size(); i++) {
    	child = v.get(i);
    	current.add(child);
    	
    	if (depth == this.pass-1) {
    		generated += this.copySiblings(child, v, current);
    	}
    	else {
    		generated += this.generateCandidates(child, current, depth+1);
    	}
    	
    	current.remove(child);
    }
    
    return generated;
  }
  
  private int copySiblings(Item item, ArrayList<Item> siblings, ArrayList<Item> current) {
    int i, mod_item, mod_sibling, copied = 0;
	Item sibling;
	Item parent = item;
    
    mod_item = item.getLabel() % this.nAttr;
    
    for (i=0; i < siblings.size(); i++) {
    	sibling = siblings.get(i);
    	mod_sibling = sibling.getLabel() % this.nAttr;
    	
    	if (mod_sibling > mod_item) {
    		current.add(sibling);
    		
    		if ( this.checkSubsets(current, this.root.getChildren(), 0, 1) ) {
    			parent.addChild( new Item( sibling.getLabel() ) );
    			copied++;
    		}
    		
    		current.remove(sibling);
    	}
    }
    
    return copied;
  }
  
  private boolean checkSubsets(ArrayList<Item> current, ArrayList<Item> children, int mark, int depth) {
    Item child;
    int i, index;
    boolean ok = true;
    
    if ( children.isEmpty() ) ok = false;
    
    i = depth;
    
    while (ok && (mark <= i)) {
    	index = children.indexOf( current.get(i) );
    	
    	if (index >= 0) {
    		if (depth < this.pass-1) {
    			child = children.get(index);
    			ok = this.checkSubsets(current, child.getChildren(), i + 1, depth + 1);
    		}
    	}
    	else {
    		ok = false;
    	}
    	
    	i--;
    }
    
    return ok;
  }
  
  private void countSupport() {
    int i;
	
    for (i=0; i < this.nTrans; i++) {
		this.countSupport(this.root, this.transactions[i], 1, i);
    }
	
  }
  
  private void countSupport(Item item, int[] items, int depth, int id_trans) {
	int i, mod_child;
	ArrayList<Item> v = item.getChildren(); 
    Item child;
    
    for (i=0; i < v.size(); i++) {
    	child = v.get(i);
    	mod_child = child.getLabel() % this.nAttr;
    	
    	if (child.getLabel() == items[mod_child]) {
    		if (depth == this.pass) {
    			child.incSupport();
    		} 
    		else {
    			this.countSupport(child, items, depth + 1, id_trans);
    		}
    	}
    }
  }
  
  public static double roundDouble(double number, int decimalPlace){
	  double numberRound;
	  
	  if(!Double.isInfinite(number)&&(!Double.isNaN(number))){
		  BigDecimal bd = new BigDecimal(number);
		  bd = bd.setScale(decimalPlace, BigDecimal.ROUND_UP);
		  numberRound = bd.doubleValue();
		  return numberRound;
	  }else return number;
	 
	  
  }
 
  
  private void generateRules(Item item, ArrayList<Item> itemset, ArrayList<AssociationRule> rules, HashSet<Integer> cov_recs) {
	int f, i, j;
	double rule_sup, ant_sup,cons_sup, conf, lift, conv, CF, netConf, numeratorYules, denominatorYules,yulesQ;
	AssociationRule ar;
	ArrayList<Item> ant, v = item.getChildren();
    
	for (f=0; f < v.size(); f++) {
    	item = v.get(f);
    	itemset.add(item);
    	
    	if (itemset.size() > 1) {
    		for (i=0; i < itemset.size(); i++) {
    			ant = new ArrayList<Item>();
    			    			
    			for (j=0; j < itemset.size(); j++) {
        			if (i != j) ant.add( itemset.get(j) );
        		}
    			        		
    			rule_sup = (double)item.getSupport() / (double)this.nTrans;
    			ant_sup = (double)searchItemsetIntoTrie(this.root, ant, 0) / (double)this.nTrans;
    			cons_sup = itemset.get(i).getSupport() / (double)this.nTrans;;
    			conf = rule_sup / ant_sup;
    			
    			//compute lift
    		    if((cons_sup == 0) || (ant_sup == 0))
    		    	lift = 1;
      			else lift = rule_sup / (ant_sup*cons_sup);
      			
    		   //compute conviction
      			if((cons_sup == 1)||(ant_sup == 0))
      				conv = 1;
      			else conv = (ant_sup*(1-cons_sup))/(ant_sup-rule_sup);
      			
      		    //compute netconf
      			if((ant_sup == 0)||(ant_sup == 1)||(Math.abs((ant_sup * (1-ant_sup))) <= 0.001))
      				netConf = 0;
      			else netConf = (rule_sup - (ant_sup*cons_sup))/(ant_sup * (1-ant_sup));
      				  
      		    //compute yulesQ
    			numeratorYules = ((rule_sup * (1 - cons_sup - ant_sup + rule_sup)) - ((ant_sup - rule_sup)* (cons_sup - rule_sup)));
    			denominatorYules = ((rule_sup * (1 - cons_sup - ant_sup + rule_sup)) + ((ant_sup - rule_sup)* (cons_sup - rule_sup)));
    			
    			if((ant_sup == 0)||(ant_sup == 1)|| (cons_sup == 0)||(cons_sup == 1)||(Math.abs(denominatorYules) <= 0.001))
    				yulesQ = 0;
    			else yulesQ = numeratorYules/denominatorYules;
    			
    			//compute Certain Factor(CF)
    			CF = 0;
    			if(conf > cons_sup)
    				CF = (conf - cons_sup)/(1-cons_sup);	
    			else 
    				if(conf < cons_sup)
    					CF = (conf - cons_sup)/(cons_sup);	
    				
    			if (conf >= this.minConfidence) {			
    				ar = new AssociationRule();
    				
    				for (j=0; j < ant.size(); j++) {
    					ar.addAntecedent( ant.get(j).getLabel() );
    				}
    				
        			ar.addConsequent( itemset.get(i).getLabel() );
    				
        			ar.setRuleSupport(rule_sup);
    				ar.setAntecedentSupport(ant_sup);
    				ar.setConsequentSupport(cons_sup);
    				ar.setConfidence(conf);
    				ar.setLift(lift);
    				ar.setConv(conv);
    				ar.setCF(CF);
    				ar.setNetConf(netConf);
    				ar.setYulesQ(yulesQ);
    				
    				cov_recs.addAll( this.countCoveredRecords(itemset) );
    				
    				rules.add(ar);
    			}
    		}	
    	}
    		
    	if ( item.hasChildren() ) this.generateRules(item, itemset, rules, cov_recs);
    	
    	itemset.remove(item);
    }
 }
  
  private int searchItemsetIntoTrie(Item item, ArrayList<Item> itemset, int index) {
	int i, support = 0;
	ArrayList<Item> v = item.getChildren();
     
	for (i=0; i < v.size(); i++) {
    	item = v.get(i);
    	
    	if ( item.equals( itemset.get(index) ) ) {
    		if (index == (itemset.size()-1)) return ( item.getSupport() );
    		else if ( item.hasChildren() ) support = searchItemsetIntoTrie(item, itemset, index + 1);
    		
    		break;
    	}
    }
      
    return support;
  }
  
  private HashSet<Integer> countCoveredRecords(ArrayList<Item> itemset) {
	int i, k;
	ArrayList<HashSet<Integer>> v_tid_lst = new ArrayList<HashSet<Integer>>();
	
	for (i=0; i < itemset.size(); i++) {
		Item item = itemset.get(i);
		v_tid_lst.add( this.dataset.getTIDList().get( item.getLabel() ) );
	}
	
	HashSet<Integer> toIntersect = new HashSet<Integer>( v_tid_lst.get(0) );
	
	for (k=1; k < v_tid_lst.size(); k++) {
		toIntersect.retainAll( v_tid_lst.get(k) );
		if ( toIntersect.isEmpty() ) break;
	}
	
	return toIntersect;
  }
  
}

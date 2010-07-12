package keel.Algorithms.LQD.methods.FGFS_Rule_Weight_Penalty;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;


/**
 *
 * File: fuzzy.java
 *
 * Properties and functions of individual of the population
 *
 * @author Written by Ana Palacios Jimenez (University of Oviedo) 25/006/2010
 * @version 1.0
 */


public class IndMichigan {

	  fuzzy[][] X;
	  Vector<Vector<Float>> Y;  
	  rule individuo;
	  Interval fitness;
	 
	  
	  IndMichigan(fuzzy[][] x,Vector<Vector<Float>> y,Vector<partition> pentradas,int classes, int COST, int alfa,
			  Vector<Float> values_classes,Vector<Vector<Float>> costs,int instance, int asign_weight_rule) throws IOException 
	  {
		  individuo= new rule(pentradas,classes,asign_weight_rule);
		  if(instance<x.length && instance<10)
			  individuo.obtain_rule(x,y,pentradas,classes,COST, alfa,values_classes,costs,instance);
		  else
			  individuo.obtain_rule_random(x,y,pentradas,classes,COST, alfa,values_classes,costs);
		  
		  X=x;
		  Y=y;
	  }
    
	
	  IndMichigan(Vector<Integer> ant,fuzzy[][] x,Vector<Vector<Float>> y,Vector<partition> pentradas,int classes, int COST, int alfa,
			  Vector<Float> values_classes,Vector<Vector<Float>> costs,int asign_weight_rule) throws IOException 
	  {
//		 

		  individuo= new rule(pentradas,classes, asign_weight_rule);
		  Integer[]a= new Integer[ant.size()];
		  for(int i=0;i<ant.size();i++)
		  {
			  a[i]= ant.get(i);
		  }
		  individuo.setAntecedente(a);
		  
		  individuo.calcularconsequent(x, y, pentradas, classes,COST,alfa,values_classes,costs);

		  X=x;
		  Y=y;  
	  }
       
	
	  public rule getregla(){return individuo;}
	  public int size() { return individuo.size(); }
	  public Interval getfitness()
	  {
		  return fitness;
	  }
	  public fuzzy[][] getX()
	  {
		  return X;
	  }
	  public Vector<Vector<Float>> getY()
	  {
		  return Y;
	  }
	  public void asigninstances(fuzzy[][] x,Vector<Vector<Float>> y) 
	  {
		  X=x;
		  Y=y;
	  }

}
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

package keel.Algorithms.PSO_Learning.LDWPSO;

/**
 * <p>Title: Dataset</p>
 *
 * <p>Description: It contains the methods to read a Classification/Regression Dataset</p>
 *
 *
 * <p>Company: KEEL </p>
 *
 * @author Jose A. Saez Munoz
 * @version 1.0
 */

import java.io.IOException;
import keel.Dataset.*;

public class myDataset {

    public static final int REAL = 0;
    public static final int INTEGER = 1;
    public static final int NOMINAL = 2;

    private double[][] X = null; //examples array
    private boolean[][] missing = null; //possible missing values
    private int[] outputInteger = null; //output of the data-set as integer values
    private double[] outputReal = null; //output of the data-set as double values
    private String[] output = null; //output of the data-set as string values
    private double[] emax; //max value of an attribute
    private double[] emin; //min value of an attribute

    private int nData; // Number of examples
    private int nVars; // Numer of variables
    private int nInputs; // Number of inputs
    private int nClasses; // Number of outputs

    private InstanceSet IS; //The whole instance set

    private double stdev[], average[]; //standard deviation and average of each attribute
    private int instancesCl[];
    private Boolean Removed[];

    

    /**
     * Init a new set of instances
     */
    public myDataset() {
        IS = new InstanceSet();
    }

    /**
     * Outputs an array of examples with their corresponding attribute values.
     * @return double[][] an array of examples with their corresponding attribute values
     */
    public double[][] getX() {
        return X;
    }

    /**
     * Output a specific example
     * @param pos int position (id) of the example in the data-set
     * @return double[] the attributes of the given example
     */
    public double[] getExample(int pos) {
        return X[pos];
    }

    /**
     * Returns the output of the data-set as integer values
     * @return int[] an array of integer values corresponding to the output values of the dataset
     */
    public int[] getOutputAsInteger() {
        int[] output = new int[outputInteger.length];
        for (int i = 0; i < outputInteger.length; i++) {
            output[i] = outputInteger[i];
        }
        return output;
    }

    /**
     * Returns the output of the data-set as real values
     * @return double[] an array of real values corresponding to the output values of the dataset
     */
    public double[] getOutputAsReal() {
        double[] output = new double[outputReal.length];
        for (int i = 0; i < outputReal.length; i++) {
            output[i] = outputInteger[i];
        }
        return output;
    }

    /**
     * Returns the output of the data-set as nominal values
     * @return String[] an array of nomianl values corresponding to the output values of the dataset
     */
    public String[] getOutputAsString() {
        String[] output = new String[this.output.length];
        for (int i = 0; i < this.output.length; i++) {
            output[i] = this.output[i];
        }
        return output;
    }

    /**
     * It returns the output value of the example "pos"
     * @param pos int the position (id) of the example
     * @return String a string containing the output value
     */
    public String getOutputAsString(int pos) {
        return output[pos];
    }

    /**
     * It returns the output value of the example "pos"
     * @param pos int the position (id) of the example
     * @return int an integer containing the output value
     */
    public int getOutputAsInteger(int pos) {
        return outputInteger[pos];
    }

    /**
     * It returns the output value of the example "pos"
     * @param pos int the position (id) of the example
     * @return double a real containing the output value
     */
    public double getOutputAsReal(int pos) {
        return outputReal[pos];
    }

    /**
     * It returns an array with the maximum values of the attributes
     * @return double[] an array with the maximum values of the attributes
     */
    public double[] getemax() {
        return emax;
    }

    /**
     * It returns an array with the minimum values of the attributes
     * @return double[] an array with the minimum values of the attributes
     */
    public double[] getemin() {
        return emin;
    }

    public double getMax(int variable) {
        return emax[variable];
    }

    public double getMin(int variable) {
        return emin[variable];
    }

    /**
     * It gets the size of the data-set
     * @return int the number of examples in the data-set
     */
    public int getnData() {
        return nData;
    }

    /**
     * It gets the number of variables of the data-set (including the output)
     * @return int the number of variables of the data-set (including the output)
     */
    public int getnVars() {
        return nVars;
    }

    /**
     * It gets the number of input attributes of the data-set
     * @return int the number of input attributes of the data-set
     */
    public int getnInputs() {
        return nInputs;
    }

    /**
     * It gets the number of output attributes of the data-set (for example number of classes in classification)
     * @return int the number of different output values of the data-set
     */
    public int getnClasses() {
        return nClasses;
    }

    /**
     * This function checks if the attribute value is missing
     * @param i int Example id
     * @param j int Variable id
     * @return boolean True is the value is missing, else it returns false
     */
    public boolean isMissing(int i, int j) {
        return missing[i][j];
    }

    /**
     * It reads the whole input data-set and it stores each example and its associated output value in
     * local arrays to ease their use.
     * @param datasetFile String name of the file containing the dataset
     * @param train boolean It must have the value "true" if we are reading the training data-set
     * @throws IOException If there ocurs any problem with the reading of the data-set
     */
    public void readClassificationSet(String datasetFile, boolean train) throws
            IOException {
        try {
            // Load in memory a dataset that contains a classification problem
            IS.readSet(datasetFile, train);
            nData = IS.getNumInstances();
            nInputs = Attributes.getInputNumAttributes();
            nVars = nInputs + Attributes.getOutputNumAttributes();
            
            
            Removed=new Boolean[nData];
            for(int i=0 ; i<nData ; ++i)
                Removed[i]=false;
            

            // outputIntegerheck that there is only one output variable
            if (Attributes.getOutputNumAttributes() > 1) {
                System.out.println(
                        "This algorithm can not process MIMO datasets");
                System.out.println(
                        "All outputs but the first one will be removed");
                System.exit(1);
            }
            boolean noOutputs = false;
            if (Attributes.getOutputNumAttributes() < 1) {
                System.out.println(
                        "This algorithm can not process datasets without outputs");
                System.out.println("Zero-valued output generated");
                noOutputs = true;
                System.exit(1);
            }

            // Initialice and fill our own tables
            X = new double[nData][nInputs];
            missing = new boolean[nData][nInputs];
            outputInteger = new int[nData];
            outputReal = new double[nData];
            output = new String[nData];

            // Maximum and minimum of inputs
            emax = new double[nInputs];
            emin = new double[nInputs];

            // All values are casted into double/integer
            nClasses = 0;
            for (int i = 0; i < nData; i++) {
                Instance inst = IS.getInstance(i);
                for (int j = 0; j < nInputs; j++) {
                    X[i][j] = IS.getInputNumericValue(i, j); //inst.getInputRealValues(j);
                    missing[i][j] = inst.getInputMissingValues(j);
                    if (X[i][j] > emax[j] || i == 0) {
                        emax[j] = X[i][j];
                    }
                    if (X[i][j] < emin[j] || i == 0) {
                        emin[j] = X[i][j];
                    }
                }

                if (noOutputs) {
                    outputInteger[i] = 0;
                    output[i] = "";
                } else {
                    outputInteger[i] = (int) IS.getOutputNumericValue(i, 0);
                    output[i] = IS.getOutputNominalValue(i, 0);
                }
                if (outputInteger[i] > nClasses) {
                    nClasses = outputInteger[i];
                }
            }
            nClasses++;
            System.out.println("Number of classes=" + nClasses);

        } catch (Exception e) {
            System.out.println("DBG: Exception in readSet");
            e.printStackTrace();
        }
        computeStatistics();
        this.computeInstancesPerClass();
    }

    /**
     * It reads the whole input data-set and it stores each example and its associated output value in
     * local arrays to ease their use.
     * @param datasetFile String name of the file containing the dataset
     * @param train boolean It must have the value "true" if we are reading the training data-set
     * @throws IOException If there ocurs any problem with the reading of the data-set
     */
    public void readRegressionSet(String datasetFile, boolean train) throws
            IOException {
        try {
            // Load in memory a dataset that contains a regression problem
            IS.readSet(datasetFile, train);
            nData = IS.getNumInstances();
            nInputs = Attributes.getInputNumAttributes();
            nVars = nInputs + Attributes.getOutputNumAttributes();

            // outputIntegerheck that there is only one output variable
            if (Attributes.getOutputNumAttributes() > 1) {
                System.out.println(
                        "This algorithm can not process MIMO datasets");
                System.out.println(
                        "All outputs but the first one will be removed");
                System.exit(1);
            }
            boolean noOutputs = false;
            if (Attributes.getOutputNumAttributes() < 1) {
                System.out.println(
                        "This algorithm can not process datasets without outputs");
                System.out.println("Zero-valued output generated");
                noOutputs = true;
                System.exit(1);
            }

            // Initialice and fill our own tables
            X = new double[nData][nInputs];
            missing = new boolean[nData][nInputs];
            outputInteger = new int[nData];

            // Maximum and minimum of inputs
            emax = new double[nInputs];
            emin = new double[nInputs];

            // All values are casted into double/integer
            nClasses = 0;
            for (int i = 0; i < nData; i++) {
                Instance inst = IS.getInstance(i);
                for (int j = 0; j < nInputs; j++) {
                    X[i][j] = IS.getInputNumericValue(i, j);
                    missing[i][j] = inst.getInputMissingValues(j);
                    if (X[i][j] > emax[j] || i == 0) {
                        emax[j] = X[i][j];
                    }
                    if (X[i][j] < emin[j] || i == 0) {
                        emin[j] = X[i][j];
                    }
                }

                if (noOutputs) {
                    outputReal[i] = outputInteger[i] = 0;
                } else {
                    outputReal[i] = IS.getOutputNumericValue(i, 0);
                    outputInteger[i] = (int) outputReal[i];
                }
            }
        } catch (Exception e) {
            System.out.println("DBG: Exception in readSet");
            e.printStackTrace();
        }
        computeStatistics();
    }


    /**
     * It copies the header of the dataset
     * @return String A string containing all the data-set information
     */
    public String copyHeader() {
        String p = new String("");
        p = "@relation " + Attributes.getRelationName() + "\n";
        p += Attributes.getInputAttributesHeader();
        p += Attributes.getOutputAttributesHeader();
        p += Attributes.getInputHeader() + "\n";
        p += Attributes.getOutputHeader() + "\n";
        p += "@data\n";
        return p;
    }
    

    /**
     * It checks if the data-set has any real value
     * @return boolean True if it has some real values, else false.
     */
    public boolean hasRealAttributes() {
        return Attributes.hasRealAttributes();
    }

    public boolean hasNumericalAttributes() {
        return (Attributes.hasIntegerAttributes() ||
                Attributes.hasRealAttributes());
    }

    /**
     * It checks if the data-set has any missing value
     * @return boolean True if it has some missing values, else false.
     */
    public boolean hasMissingAttributes() {
        return (this.sizeWithoutMissing() < this.getnData());
    }

    /**
     * It return the size of the data-set without having account the missing values
     * @return int the size of the data-set without having account the missing values
     */
    public int sizeWithoutMissing() {
        int tam = 0;
        for (int i = 0; i < nData; i++) {
            int j;
            for (j = 1; (j < nInputs) && (!isMissing(i, j)); j++) {
                ;
            }
            if (j == nInputs) {
                tam++;
            }
        }
        return tam;
    }

    public int size() {
        return nData;
    }

    /**
     * It computes the average and standard deviation of the input attributes
     */
    private void computeStatistics(){
    	
        stdev = new double[nData];
        average = new double[nData];
        

    	
        for (int i = 0; i < nData ; i++) {

            average[i] = 0;

            for (int j = 0; j < X[i].length; j++) {
                average[i] += X[i][j];
            }
            average[i] /= X[i].length;
        }
    
        average[average.length-1] = 0;
        for (int j = 0; j < outputReal.length; j++) {
            average[average.length-1] += outputReal[j];
        }
        average[average.length-1] /= outputReal.length;


        for (int i = 0; i < nData ; i++) {
            double sum = 0;
            for (int j = 0; j < X[i].length; j++) {
                sum += (X[i][j] - average[i]) * (X[i][j] - average[i]);
            }
            sum /= X[i].length;
            stdev[i] = Math.sqrt(sum);
        }
        double sum = 0;
        for (int j = 0; j < outputReal.length; j++) {
            sum += (outputReal[j] - average[average.length-1]) * (outputReal[j] - average[average.length-1]);
        }
        sum /= outputReal.length;
        stdev[stdev.length-1] = Math.sqrt(sum);

    }


    /**
     * It return the standard deviation of an specific attribute
     * @param position int attribute id (position of the attribute)
     * @return double the standard deviation  of the attribute
     */
    public double stdDev(int position) {
        return stdev[position];
    }

    /**
     * It return the average of an specific attribute
     * @param position int attribute id (position of the attribute)
     * @return double the average of the attribute
     */
    public double average(int position) {
        return average[position];
    }

    public void computeInstancesPerClass() {
        instancesCl = new int[nClasses];
        for (int i = 0; i < this.getnData(); i++) {
            instancesCl[this.outputInteger[i]]++;
        }
    }

    public int numberInstances(int clas) {
        return instancesCl[clas];
    }

    public int numberValues(int attribute) {
        return Attributes.getInputAttribute(attribute).getNumNominalValues();
    }

    public String getOutputValue(int intValue) {
        return Attributes.getOutputAttribute(0).getNominalValue(intValue);
    }

    public int getTipo(int variable) {
        if (Attributes.getAttribute(variable).getType() ==
            Attributes.getAttribute(0).INTEGER) {
            return this.INTEGER;
        }
        if (Attributes.getAttribute(variable).getType() ==
            Attributes.getAttribute(0).REAL) {
            return this.REAL;
        }
        if (Attributes.getAttribute(variable).getType() ==
            Attributes.getAttribute(0).NOMINAL) {
            return this.NOMINAL;
        }
        return 0;
    }

    /**
     * Devuelve el universo de discuros de las variables de entrada y salida
     * @return double[][] El rango minimo y maximo de cada variable
     */
    public double [][] devuelveRangos(){
      double [][] rangos = new double[this.getnVars()][2];
      for (int i = 0; i < this.getnInputs(); i++){
        if (Attributes.getInputAttribute(i).getNumNominalValues() > 0){
          rangos[i][0] = 0;
          rangos[i][1] = Attributes.getInputAttribute(i).getNumNominalValues()-1;
        }else{
          rangos[i][0] = Attributes.getInputAttribute(i).getMinAttribute();
          rangos[i][1] = Attributes.getInputAttribute(i).getMaxAttribute();
        }
      }
      rangos[this.getnVars()-1][0] = Attributes.getOutputAttribute(0).getMinAttribute();
      rangos[this.getnVars()-1][1] = Attributes.getOutputAttribute(0).getMaxAttribute();
      return rangos;
    }
    

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    
    public double Desnormalizar(Double valor, int numAtributo, double t){
    
        double A =emax[numAtributo] - emin[numAtributo];
        return ((valor*A)/t)+emin[numAtributo];
       
    }
    
    
    /*devuelve la clase predominante. Cuenta instancias no removidas*/
    public int ClasePredominante(){
        
    	int max, clase;
        int instancesNotRemovedPerClass[]=new int[nClasses];
        
        //inicializo a 0
        for(int i=0 ; i<nClasses ; ++i)
            instancesNotRemovedPerClass[i]=0;

        //cuento instancias no removidas por clase
        for (int i = 0; i < this.getnData(); i++)
            if(!Removed[i])
                instancesNotRemovedPerClass[this.outputInteger[i]]++;
        
        //devuelvo el maximo
        max=instancesNotRemovedPerClass[0];
        clase=0;
        for (int i = 1 ; i < nClasses ; i++) {
            if(instancesNotRemovedPerClass[i]>max){
                max=instancesNotRemovedPerClass[i];
                clase=i;
            }
        }
        
        return clase;
    }
    
    /**
     * It transform the input space into the [0,t] range
     */
    public void normalize(double t){
    	
        int atts = this.getnInputs();
        double maxs[] = new double[atts];
        
        
        for (int j=0 ; j<atts ; ++j){
        	maxs[j] = t / ((emax[j]+1)-emin[j]);
        }
        
        for(int i=0 ; i<this.getnData() ; ++i){

            for(int j=0 ; j<atts ; ++j){
            	X[i][j] = (X[i][j] - emin[j]) * maxs[j];
            }
        }
    }
    
    public int NumClassesNotRemoved(){
        
        int instancesNotRemovedPerClass[]=new int[nClasses];
        
        //inicializo a 0
        for(int i=0 ; i<nClasses ; ++i)
            instancesNotRemovedPerClass[i]=0;

        //cuento instancias no removidas por clase
        for (int i = 0; i < this.getnData(); i++) 
            if(!Removed[i])
                instancesNotRemovedPerClass[this.outputInteger[i]]++;
        
        //devuelvo el numero de clases de instancias sin remover
        int res=0;
        for (int i = 0; i < nClasses ; i++) {
            if(instancesNotRemovedPerClass[i]>0)
            	res++;
        }
        
        return res;
    }

    public boolean getRemoved(int i){
    	
    	return Removed[i];
    }
    
    public void setRemoved(int i, boolean value){
    	
    	Removed[i]=value;
    }
    
    public int noClasificadas(){
        int cont=0;
        
        //cuento instancias no removidas
        for(int i=0 ; i<nData ; ++i)
            if(!Removed[i])
            	cont++;
        
        return cont;
    }
    
    public Boolean QuedanMasInstancias(double porc){
    
        int cont=noClasificadas();
        
        if(cont>porc*nData)
            return true;
        else
            return false;
    }
    
}


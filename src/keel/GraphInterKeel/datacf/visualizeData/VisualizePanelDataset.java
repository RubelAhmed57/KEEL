/***********************************************************************

	This file is part of KEEL-software, the Data Mining tool for regression, 
	classification, clustering, pattern mining and so on.

	Copyright (C) 2004-2010
	
	F. Herrera (herrera@decsai.ugr.es)
    L. SÃ¡nchez (luciano@uniovi.es)
    J. AlcalÃ¡-Fdez (jalcala@decsai.ugr.es)
    S. GarcÃ­a (sglopez@ujaen.es)
    A. FernÃ¡ndez (alberto.fernandez@ujaen.es)
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

package keel.GraphInterKeel.datacf.visualizeData;

/**
 * <p>
 * @author Written by Juan Carlos FernÃ¡ndez and Pedro Antonio GutiÃ©rrez(University of CÃ³rdoba) 23/10/2008
 * @version 1.0
 * @since JDK1.5
 * </p>
 */
public class VisualizePanelDataset extends javax.swing.JPanel {

    /**
     * <p>
     * Class that implements a Panel for datasets
     * </p>
     */

    /**
     * <p>
     * Constructor that initializes the panel
     * </p>
     */
    public VisualizePanelDataset() {
        initComponents();
    }

    /**
     * <p>
     * This method is called from within the constructor to
     * initialize the form.
     * </p>
     * <p>
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     * </p>
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dataSetScrollPane = new javax.swing.JScrollPane();
        dataSetTextArea = new javax.swing.JTextArea();

        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Content"));
        setName("Form"); // NOI18N

        dataSetScrollPane.setName("dataSetScrollPane"); // NOI18N

        dataSetTextArea.setColumns(20);
        dataSetTextArea.setRows(5);
        dataSetTextArea.setName("dataSetTextArea"); // NOI18N
        dataSetScrollPane.setViewportView(dataSetTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dataSetScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dataSetScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane dataSetScrollPane;
    private javax.swing.JTextArea dataSetTextArea;
    // End of variables declaration//GEN-END:variables

    /**
     * <p>
     * Sets text for dataset area
     * </p>
     * @param text Test for dataset area
     */
    public void setTextData(String text) {
        this.dataSetTextArea.setText(text);
    }

    /**
     * <p>
     * Sets care postion for dataset area
     * </p>
     * @param care Care postion for dataset area
     */
    public void setCaret(int care) {
        this.dataSetTextArea.setCaretPosition(care);
    }
}

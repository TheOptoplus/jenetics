/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jenetics.example.image;

import static java.util.stream.Collectors.averagingLong;

import java.text.NumberFormat;
import java.time.Duration;

import javax.swing.JPanel;

import org.jenetics.engine.EvolutionResult;

/**
 * Panel for showing the evolution result.
 */
public final class EvolutionResultPanel extends JPanel {

	private static final NumberFormat _format = NumberFormat.getNumberInstance();

	/**
	 * Creates new form EvolutionResultPanel
	 */
	public EvolutionResultPanel() {
		initComponents();

		_format.setMaximumIntegerDigits(1);
		_format.setMinimumIntegerDigits(1);
		_format.setMinimumFractionDigits(4);
		_format.setMaximumFractionDigits(4);
	}

	void update(final EvolutionResult<?, Double> result) {
		final String generation = Long.toString(result.getGeneration());
		final String bestFitness = _format.format(result.getBestFitness());
		final double age = result.getPopulation().stream()
			.collect(averagingLong(p -> p.getAge(result.getGeneration())));

		_generationTextField.setText(generation);
		_bestFitnessTextField.setText(bestFitness);
		_populationAgeTextField.setText(_format.format(age));
		_evaluationTimeTextField.setText(format(
			result.getDurations().getEvaluationDuration()
		));
	}

	private static String format(final Duration duration) {
		final long seconds = duration.getSeconds();
		final int millis = duration.getNano()/1_000_000;

		return String.format("%02d.%03d", seconds, millis);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        _generationLabel = new javax.swing.JLabel();
        _generationTextField = new javax.swing.JTextField();
        _bestFitnessLabel = new javax.swing.JLabel();
        _bestFitnessTextField = new javax.swing.JTextField();
        _populationAgeLabel = new javax.swing.JLabel();
        _populationAgeTextField = new javax.swing.JTextField();
        _evaluationTimeLabel = new javax.swing.JLabel();
        _evaluationTimeTextField = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        _generationLabel.setLabelFor(_generationTextField);
        _generationLabel.setText("Generation:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        add(_generationLabel, gridBagConstraints);

        _generationTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        add(_generationTextField, gridBagConstraints);

        _bestFitnessLabel.setLabelFor(_bestFitnessTextField);
        _bestFitnessLabel.setText("Best fitness:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        add(_bestFitnessLabel, gridBagConstraints);

        _bestFitnessTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        add(_bestFitnessTextField, gridBagConstraints);

        _populationAgeLabel.setLabelFor(_populationAgeTextField);
        _populationAgeLabel.setText("Population age:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        add(_populationAgeLabel, gridBagConstraints);

        _populationAgeTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        add(_populationAgeTextField, gridBagConstraints);

        _evaluationTimeLabel.setLabelFor(_evaluationTimeTextField);
        _evaluationTimeLabel.setText("Evaluation time:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        add(_evaluationTimeLabel, gridBagConstraints);

        _evaluationTimeTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        add(_evaluationTimeTextField, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel _bestFitnessLabel;
    private javax.swing.JTextField _bestFitnessTextField;
    private javax.swing.JLabel _evaluationTimeLabel;
    private javax.swing.JTextField _evaluationTimeTextField;
    private javax.swing.JLabel _generationLabel;
    private javax.swing.JTextField _generationTextField;
    private javax.swing.JLabel _populationAgeLabel;
    private javax.swing.JTextField _populationAgeTextField;
    // End of variables declaration//GEN-END:variables
}

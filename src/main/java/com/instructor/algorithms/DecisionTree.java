package com.instructor.algorithms;

import com.instructor.data.PoseDataReader;

import java.util.ArrayList;
import java.util.Map;

import com.instructor.data.PoseDataProcessing;

import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

// TODO: COMPLETE DECISION TREE FOR CLASSIFYING CORRECT/INCORRECT POSE
public class DecisionTree {

	private J48 tree; // Weka's Decision Tree Classifier
	private Instances trainingData;

	public DecisionTree() {
		// Initialize the decision tree
		tree = new J48();
	}

	public void train(Map<String, Map<Integer, float[]>> proKeypointsMap) throws Exception {
		// Extract the dataset attributes
		ArrayList<Attribute> attributes = new ArrayList<>();

		// Add angles for each relevant joint

		// Add distances between keypoints

		// Add class label (correct or incorrect pose)
	}
}

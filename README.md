# Dataset Tool
A tool designed to aid in programatically using datasets of facial features with other automation tools. 

## Requirements
In order for this library to function correctly, the dataset to be used must be formatted in the following way.

![Graph](https://raw.githubusercontent.com/2019-summer-research/Facial-Recgonition-Dataset-API/master/img/FileGraph.png)

* A dataset root directory must contain a list of other directories. One for each person to be used.
* Each person directory must have multiple images within it of (.jpg) or (.png) format. 

Provided the above formatting is correct, the library provides the following features.

## Implemented Features
* The ability to create samples of `n` size from the overall dataset, storing these samples in a seperate directory to be worked with for testing phases
* The ability to reference samples described above back to the original dataset.

## Planned Features
* The ability to connect to an external Cloud API (Amazon Rekognition, Microsoft Facial Recognition, etc.) and perform 
tests measuring the output of a Neural Network API to the legitimate dataset sample. 

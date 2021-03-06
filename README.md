# AutoDoc
AutoDoc aims to build a framework for automated analysis of human nails using image processing and machine learning algorithms. The software provides GUI for loading image data, performing operations on it using predefined algorithms. Also, use can validate our machine learning model and save the model so that it can be loaded after. The purpose of this project it to speed-up research in nail image processing.

Related paper published: http://www.mecs-press.org/ijitcs/ijitcs-v8-n7/IJITCS-V8-N7-5.pdf

# How to run

1. Download below jar files and keep them into ./lib folder:
h2-1.3.171.jar
libsvm.jar
LibSVM.jar
weka.jar

2. Compile the source code in ./src folder with libraries in ./lib folder using JavaSE-1.7 and create Jar file:
AutoDoc.jar

3. Execute jar file with different arguments:

To install database i.e. autodoc.sql, execute jar file as:
java -jar AutoDoc.jar INSTALL_MODE

To train machine learning models, execute jar file as:
java -jar AutoDoc.jar TRAIN_MODE

To use machine learning models, execute jar file as:
java -jar AutoDoc.jar USE_MODE

# Dataset size

Total images: 150
---------------------
Healthy: 24
Anemia: 24
Liver Cirrosis: 18
Pulmonary: 32
Arthritis, Thyroid: 22
Psoriasis: 18
Kidney Failure: 12

# Accuracy of different models

1. Average Color Model: 54.66%

2. Average Bicolor Model: 81.33%

3. GLCM Texture Based Model: 52.00%

4. Decision Tree Model: 84.66%

5. Neural Network #1 Model: 81.33%

6. Neural Network #2 Model: 88.00%

7. Polynomial SVM Model: 71.33%

8. Radial SVM Model: 85.33%

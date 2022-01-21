#include <iostream> 
#include <ap_int.h> 

using namespace std; 
 
void fuzzifier (double posicion, double anguloCamion);
void rulesEvaluation(); 
double defuzzifieranguloDireccion();
double fuzzyController(double posicion, double anguloCamion);

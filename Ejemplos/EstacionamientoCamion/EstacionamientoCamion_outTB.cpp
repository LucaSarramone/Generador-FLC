#include <iostream> 
#include "EstacionamientoCamion_out.h" 
#include <ap_int.h> 


using namespace std;

int main(int argc, char **argv){ 

	double posicion = 127;
	double anguloCamion = 127;

	cout <<  fuzzyController(posicion, anguloCamion); 

	return 0;
}

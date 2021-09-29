#include <iostream> 
#include "pendulo_invertido_out.h" 
#include <ap_int.h> 

typedef ap_uint<8> fixed_int;
typedef ap_uint<18> fixed_aux;


using namespace std;

int main(int argc, char **argv){ 

	fixed_int angulo = 127; 
	fixed_int velocidad = 127; 

	cout <<  fuzzyController(angulo, velocidad); 

	return 0;
}
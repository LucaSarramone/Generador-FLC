#include <iostream> 
#include "Autoenfoque_out.h" 
#include <ap_int.h> 

typedef ap_uint<8> fixed_int;
typedef ap_uint<18> fixed_aux;


using namespace std;

int main(int argc, char **argv){ 

	fixed_int izq = 127; 
	fixed_int cen = 127; 
	fixed_int der = 127; 

	cout <<  fuzzyController(izq, cen, der); 

	return 0;
}
#include <iostream> 
#include <ap_int.h> 
 
typedef ap_uint<8> fixed_int; 
typedef ap_uint<23> fixed_aux_fuerza; 
typedef ap_uint<8> fixed_out; 
using namespace std; 
 
void fuzzifier (fixed_int angulo, fixed_int velocidad); 
void rulesEvaluation(); 
fixed_int defuzzifierfuerza();
fixed_out fuzzyController(fixed_int angulo, fixed_int velocidad); 

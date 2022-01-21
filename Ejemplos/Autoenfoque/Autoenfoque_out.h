#include <iostream> 
#include <ap_int.h> 
 
typedef ap_uint<8> fixed_int; 
typedef ap_uint<20> fixed_aux_prob_izq; 
typedef ap_uint<20> fixed_aux_prob_cen; 
typedef ap_uint<20> fixed_aux_prob_der; 
typedef ap_uint<24> fixed_out; 
using namespace std; 
 
void fuzzifier (fixed_int izq, fixed_int cen, fixed_int der); 
void rulesEvaluation(); 
fixed_int defuzzifierprob_izq();
fixed_int defuzzifierprob_cen();
fixed_int defuzzifierprob_der();
fixed_out fuzzyController(fixed_int izq, fixed_int cen, fixed_int der); 

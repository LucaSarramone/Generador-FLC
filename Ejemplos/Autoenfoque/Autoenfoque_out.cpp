#include <iostream> 
#include <ap_int.h> 
 
typedef ap_uint<8> fixed_int; 
typedef ap_uint<20> fixed_aux_prob_izq; 
typedef ap_uint<20> fixed_aux_prob_cen; 
typedef ap_uint<20> fixed_aux_prob_der; 
typedef ap_uint<24> fixed_out; 
using namespace std; 
 
const fixed_int functionizq0 = 255/(23 - 0); 
const fixed_int functionizq11 = 255/(23 - 0); 
const fixed_int functionizq12 = 255/(100 - 23); 
const fixed_int functionizq2 = 255/(100 - 23); 

const fixed_int functioncen0 = 255/(23 - 0); 
const fixed_int functioncen11 = 255/(23 - 0); 
const fixed_int functioncen12 = 255/(100 - 23); 
const fixed_int functioncen2 = 255/(100 - 23); 

const fixed_int functionder0 = 255/(23 - 0); 
const fixed_int functionder11 = 255/(23 - 0); 
const fixed_int functionder12 = 255/(100 - 23); 
const fixed_int functionder2 = 255/(100 - 23); 

fixed_int izqFuzz[3] = {0, 0, 0}; 
fixed_int cenFuzz[3] = {0, 0, 0}; 
fixed_int derFuzz[3] = {0, 0, 0}; 
fixed_int prob_izqMembershipValues[4] = {0, 0, 0, 0}; 
fixed_int prob_cenMembershipValues[4] = {0, 0, 0, 0}; 
fixed_int prob_derMembershipValues[4] = {0, 0, 0, 0}; 

 
void fuzzifier (fixed_int izq, fixed_int cen, fixed_int der) { 
 #pragma HLS PIPELINE 
 #pragma HLS ARRAY_PARTITION variable=izqFuzz complete dim=1 
 #pragma HLS ARRAY_PARTITION variable=cenFuzz complete dim=1 
 #pragma HLS ARRAY_PARTITION variable=derFuzz complete dim=1 

 //izq------------------------------------

	if (izq < 0) 
		izqFuzz[0] = 255;
	else 
		if (izq > 23) 
			izqFuzz[0] = 0; 
		else 
			izqFuzz[0] = functionizq0 * (23 - izq);


	if (izq < 0 || izq > 100) 
		izqFuzz[1] = 0;
	else 
		if (izq > 23) 
			izqFuzz[1] = functionizq11 * (izq-0); 
		else 
			izqFuzz[1] = functionizq12 * (100 - izq);


	if (izq < 23) 
		izqFuzz[2] = 0;
	else 
		if (izq > 100) 
			izqFuzz[2] = 255;
		else 
			izqFuzz[2] = functionizq2 * (izq - 23);


 //cen------------------------------------

	if (cen < 0) 
		cenFuzz[0] = 255;
	else 
		if (cen > 23) 
			cenFuzz[0] = 0; 
		else 
			cenFuzz[0] = functioncen0 * (23 - cen);


	if (cen < 0 || cen > 100) 
		cenFuzz[1] = 0;
	else 
		if (cen > 23) 
			cenFuzz[1] = functioncen11 * (cen-0); 
		else 
			cenFuzz[1] = functioncen12 * (100 - cen);


	if (cen < 23) 
		cenFuzz[2] = 0;
	else 
		if (cen > 100) 
			cenFuzz[2] = 255;
		else 
			cenFuzz[2] = functioncen2 * (cen - 23);


 //der------------------------------------

	if (der < 0) 
		derFuzz[0] = 255;
	else 
		if (der > 23) 
			derFuzz[0] = 0; 
		else 
			derFuzz[0] = functionder0 * (23 - der);


	if (der < 0 || der > 100) 
		derFuzz[1] = 0;
	else 
		if (der > 23) 
			derFuzz[1] = functionder11 * (der-0); 
		else 
			derFuzz[1] = functionder12 * (100 - der);


	if (der < 23) 
		derFuzz[2] = 0;
	else 
		if (der > 100) 
			derFuzz[2] = 255;
		else 
			derFuzz[2] = functionder2 * (der - 23);


} 
 
void rulesEvaluation(){ 
 #pragma HLS PIPELINE 
 #pragma HLS ARRAY_PARTITION variable=izqFuzz complete dim=1 
 #pragma HLS ARRAY_PARTITION variable=cenFuzz complete dim=1 
 #pragma HLS ARRAY_PARTITION variable=derFuzz complete dim=1 
 #pragma HLS ARRAY_PARTITION variable=prob_izqMembershipValues complete dim=1 
 #pragma HLS ARRAY_PARTITION variable=prob_cenMembershipValues complete dim=1 
 #pragma HLS ARRAY_PARTITION variable=prob_derMembershipValues complete dim=1 

	fixed_int aux = 0; 
	prob_derMembershipValues[0] = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[2])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[2])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[1], derFuzz[1])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[1], derFuzz[2])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[2])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[2])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[2])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[2])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[0], derFuzz[0])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[0], derFuzz[2])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[2])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[2])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[2], derFuzz[2])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[0], derFuzz[0])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[0], derFuzz[2])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[1], derFuzz[1])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[1], derFuzz[2])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[2])); 
	if (prob_derMembershipValues[0] < aux) 
		 prob_derMembershipValues[0] = aux; 

	prob_derMembershipValues[1] = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[1], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[0], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[2], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[0], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[1], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[0])); 
	if (prob_derMembershipValues[1] < aux) 
		 prob_derMembershipValues[1] = aux; 

	prob_derMembershipValues[2] = min(izqFuzz[0], min(cenFuzz[0], derFuzz[1])); 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[1])); 
	if (prob_derMembershipValues[2] < aux) 
		 prob_derMembershipValues[2] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[1], derFuzz[1])); 
	if (prob_derMembershipValues[2] < aux) 
		 prob_derMembershipValues[2] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[1])); 
	if (prob_derMembershipValues[2] < aux) 
		 prob_derMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[0], derFuzz[1])); 
	if (prob_derMembershipValues[2] < aux) 
		 prob_derMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_derMembershipValues[2] < aux) 
		 prob_derMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_derMembershipValues[2] < aux) 
		 prob_derMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_derMembershipValues[2] < aux) 
		 prob_derMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_derMembershipValues[2] < aux) 
		 prob_derMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[2], derFuzz[1])); 
	if (prob_derMembershipValues[2] < aux) 
		 prob_derMembershipValues[2] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[0], derFuzz[1])); 
	if (prob_derMembershipValues[2] < aux) 
		 prob_derMembershipValues[2] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[1], derFuzz[1])); 
	if (prob_derMembershipValues[2] < aux) 
		 prob_derMembershipValues[2] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[1])); 
	if (prob_derMembershipValues[2] < aux) 
		 prob_derMembershipValues[2] = aux; 

	prob_cenMembershipValues[0] = min(izqFuzz[0], min(cenFuzz[2], derFuzz[0])); 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[1])); 
	if (prob_cenMembershipValues[0] < aux) 
		 prob_cenMembershipValues[0] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[2])); 
	if (prob_cenMembershipValues[0] < aux) 
		 prob_cenMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[2], derFuzz[0])); 
	if (prob_cenMembershipValues[0] < aux) 
		 prob_cenMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[2], derFuzz[1])); 
	if (prob_cenMembershipValues[0] < aux) 
		 prob_cenMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[2], derFuzz[2])); 
	if (prob_cenMembershipValues[0] < aux) 
		 prob_cenMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[0])); 
	if (prob_cenMembershipValues[0] < aux) 
		 prob_cenMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[1])); 
	if (prob_cenMembershipValues[0] < aux) 
		 prob_cenMembershipValues[0] = aux; 

	prob_cenMembershipValues[1] = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[1])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[1])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[2])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[2])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[0])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[2])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[0], derFuzz[0])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[0], derFuzz[0])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[0], derFuzz[1])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[0], derFuzz[2])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[0], derFuzz[0])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[0], derFuzz[0])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[0], derFuzz[1])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[0], derFuzz[2])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[0])); 
	if (prob_cenMembershipValues[1] < aux) 
		 prob_cenMembershipValues[1] = aux; 

	prob_cenMembershipValues[2] = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[1], derFuzz[0])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[1], derFuzz[1])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[1], derFuzz[1])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[1], derFuzz[2])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[0])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[2])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[0])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[0])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[2])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[2])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[1], derFuzz[0])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[1], derFuzz[1])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[1], derFuzz[1])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[1], derFuzz[2])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[0])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[2])); 
	if (prob_cenMembershipValues[2] < aux) 
		 prob_cenMembershipValues[2] = aux; 

	prob_cenMembershipValues[3] = min(izqFuzz[0], min(cenFuzz[2], derFuzz[0])); 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[2])); 
	if (prob_cenMembershipValues[3] < aux) 
		 prob_cenMembershipValues[3] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_cenMembershipValues[3] < aux) 
		 prob_cenMembershipValues[3] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_cenMembershipValues[3] < aux) 
		 prob_cenMembershipValues[3] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_cenMembershipValues[3] < aux) 
		 prob_cenMembershipValues[3] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_cenMembershipValues[3] < aux) 
		 prob_cenMembershipValues[3] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[0])); 
	if (prob_cenMembershipValues[3] < aux) 
		 prob_cenMembershipValues[3] = aux; 

	prob_izqMembershipValues[0] = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[1])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[2])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[0])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[2])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[0], derFuzz[0])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[0], derFuzz[0])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[0], derFuzz[1])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[0], derFuzz[2])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[1], derFuzz[0])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[1], derFuzz[1])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[1], derFuzz[1])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[1], derFuzz[2])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[0])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[0])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[0])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[0])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[1])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	aux = min(izqFuzz[2], min(cenFuzz[2], derFuzz[2])); 
	if (prob_izqMembershipValues[0] < aux) 
		 prob_izqMembershipValues[0] = aux; 

	prob_izqMembershipValues[1] = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[0])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[1])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[0], derFuzz[2])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[1], derFuzz[0])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[1], derFuzz[1])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[1], derFuzz[1])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[1], derFuzz[2])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[0])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[0])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[0])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[0])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[1])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[2])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[2])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[2])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	aux = min(izqFuzz[0], min(cenFuzz[2], derFuzz[2])); 
	if (prob_izqMembershipValues[1] < aux) 
		 prob_izqMembershipValues[1] = aux; 

	prob_izqMembershipValues[2] = min(izqFuzz[1], min(cenFuzz[0], derFuzz[0])); 

	aux = min(izqFuzz[1], min(cenFuzz[0], derFuzz[0])); 
	if (prob_izqMembershipValues[2] < aux) 
		 prob_izqMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[0], derFuzz[1])); 
	if (prob_izqMembershipValues[2] < aux) 
		 prob_izqMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[0], derFuzz[2])); 
	if (prob_izqMembershipValues[2] < aux) 
		 prob_izqMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[0])); 
	if (prob_izqMembershipValues[2] < aux) 
		 prob_izqMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_izqMembershipValues[2] < aux) 
		 prob_izqMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_izqMembershipValues[2] < aux) 
		 prob_izqMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_izqMembershipValues[2] < aux) 
		 prob_izqMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[1])); 
	if (prob_izqMembershipValues[2] < aux) 
		 prob_izqMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[1], derFuzz[2])); 
	if (prob_izqMembershipValues[2] < aux) 
		 prob_izqMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[2], derFuzz[0])); 
	if (prob_izqMembershipValues[2] < aux) 
		 prob_izqMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[2], derFuzz[1])); 
	if (prob_izqMembershipValues[2] < aux) 
		 prob_izqMembershipValues[2] = aux; 

	aux = min(izqFuzz[1], min(cenFuzz[2], derFuzz[2])); 
	if (prob_izqMembershipValues[2] < aux) 
		 prob_izqMembershipValues[2] = aux; 

} 

const fixed_int outputValuesprob_izq[4] = {76, 127, 204, 255}; 
fixed_int defuzzifierprob_izq() {
 #pragma HLS PIPELINE 
 #pragma HLS ARRAY_PARTITION variable=prob_izqMembershipValues complete dim=1 
 #pragma HLS ARRAY_PARTITION variable=outputValuesprob_izq complete dim=1 

	fixed_aux_prob_izq numerator = 0; 
 	fixed_aux_prob_izq denominator = 0; 
 	for(int i=0; i<4; i++){ 
		numerator = numerator + prob_izqMembershipValues[i] * outputValuesprob_izq[i]; 
		denominator = denominator + prob_izqMembershipValues[i]; 
	} 
	return numerator/denominator; 
}
const fixed_int outputValuesprob_cen[4] = {76, 127, 204, 255}; 
fixed_int defuzzifierprob_cen() {
 #pragma HLS PIPELINE 
 #pragma HLS ARRAY_PARTITION variable=prob_cenMembershipValues complete dim=1 
 #pragma HLS ARRAY_PARTITION variable=outputValuesprob_cen complete dim=1 

	fixed_aux_prob_cen numerator = 0; 
 	fixed_aux_prob_cen denominator = 0; 
 	for(int i=0; i<4; i++){ 
		numerator = numerator + prob_cenMembershipValues[i] * outputValuesprob_cen[i]; 
		denominator = denominator + prob_cenMembershipValues[i]; 
	} 
	return numerator/denominator; 
}
const fixed_int outputValuesprob_der[4] = {76, 127, 204, 255}; 
fixed_int defuzzifierprob_der() {
 #pragma HLS PIPELINE 
 #pragma HLS ARRAY_PARTITION variable=prob_derMembershipValues complete dim=1 
 #pragma HLS ARRAY_PARTITION variable=outputValuesprob_der complete dim=1 

	fixed_aux_prob_der numerator = 0; 
 	fixed_aux_prob_der denominator = 0; 
 	for(int i=0; i<4; i++){ 
		numerator = numerator + prob_derMembershipValues[i] * outputValuesprob_der[i]; 
		denominator = denominator + prob_derMembershipValues[i]; 
	} 
	return numerator/denominator; 
}

fixed_out fuzzyController(fixed_int izq, fixed_int cen, fixed_int der){ 
 #pragma HLS DATAFLOW 

	fuzzifier(izq, cen, der);
	rulesEvaluation(); 

	fixed_out output0 = defuzzifierprob_izq();
	fixed_out output1 = defuzzifierprob_cen() <<8;
	fixed_out output2 = defuzzifierprob_der() <<16;
	return output0 + output1 + output2;
}

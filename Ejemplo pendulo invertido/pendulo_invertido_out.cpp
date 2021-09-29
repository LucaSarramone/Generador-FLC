#include <iostream> 
#include <ap_int.h> 
 
typedef ap_uint<8> fixed_int; 
typedef ap_uint<23> fixed_aux_fuerza; 
typedef ap_uint<8> fixed_out; 
using namespace std; 
 
const fixed_int functionangulo0 = 255/(78 - 54); 
const fixed_int functionangulo11 = 255/(99 - 0); 
const fixed_int functionangulo12 = 255/(113 - 99); 
const fixed_int functionangulo21 = 255/(117 - 103); 
const fixed_int functionangulo22 = 255/(127 - 117); 
const fixed_int functionangulo31 = 255/(127 - 120); 
const fixed_int functionangulo32 = 255/(132 - 127); 
const fixed_int functionangulo41 = 255/(138 - 127); 
const fixed_int functionangulo42 = 255/(152 - 138); 
const fixed_int functionangulo51 = 255/(156 - 142); 
const fixed_int functionangulo52 = 255/(255 - 156); 
const fixed_int functionangulo6 = 255/(177 - 166); 

const fixed_int functionvelocidad0 = 255/(64 - 25); 
const fixed_int functionvelocidad11 = 255/(64 - 0); 
const fixed_int functionvelocidad12 = 255/(106 - 64); 
const fixed_int functionvelocidad21 = 255/(106 - 64); 
const fixed_int functionvelocidad22 = 255/(127 - 106); 
const fixed_int functionvelocidad31 = 255/(127 - 106); 
const fixed_int functionvelocidad32 = 255/(149 - 127); 
const fixed_int functionvelocidad41 = 255/(149 - 127); 
const fixed_int functionvelocidad42 = 255/(191 - 149); 
const fixed_int functionvelocidad51 = 255/(191 - 149); 
const fixed_int functionvelocidad52 = 255/(255 - 191); 
const fixed_int functionvelocidad6 = 255/(229 - 191); 

fixed_int anguloFuzz[7] = {0, 0, 0, 0, 0, 0, 0}; 
fixed_int velocidadFuzz[7] = {0, 0, 0, 0, 0, 0, 0}; 
fixed_int fuerzaMembershipValues[7] = {0, 0, 0, 0, 0, 0, 0}; 

 
void fuzzifier (fixed_int angulo, fixed_int velocidad) { 
 #pragma HLS PIPELINE 
 #pragma #pragma HLS ARRAY_PARTITION variable=anguloFuzz complete dim=1 
 #pragma #pragma HLS ARRAY_PARTITION variable=velocidadFuzz complete dim=1 

 //angulo------------------------------------

	if (angulo < 54) 
		anguloFuzz[0] = 255;
	else 
		if (angulo > 78) 
			anguloFuzz[0] = 0; 
		else 
			anguloFuzz[0] = functionangulo0 * (78 - angulo);


	if (angulo < 0 || angulo > 113) 
		anguloFuzz[1] = 0;
	else 
		if (angulo > 99) 
			anguloFuzz[1] = functionangulo11 * (angulo-0); 
		else 
			anguloFuzz[1] = functionangulo12 * (113 - angulo);


	if (angulo < 103 || angulo > 127) 
		anguloFuzz[2] = 0;
	else 
		if (angulo > 117) 
			anguloFuzz[2] = functionangulo21 * (angulo-103); 
		else 
			anguloFuzz[2] = functionangulo22 * (127 - angulo);


	if (angulo < 120 || angulo > 132) 
		anguloFuzz[3] = 0;
	else 
		if (angulo > 127) 
			anguloFuzz[3] = functionangulo31 * (angulo-120); 
		else 
			anguloFuzz[3] = functionangulo32 * (132 - angulo);


	if (angulo < 127 || angulo > 152) 
		anguloFuzz[4] = 0;
	else 
		if (angulo > 138) 
			anguloFuzz[4] = functionangulo41 * (angulo-127); 
		else 
			anguloFuzz[4] = functionangulo42 * (152 - angulo);


	if (angulo < 142 || angulo > 255) 
		anguloFuzz[5] = 0;
	else 
		if (angulo > 156) 
			anguloFuzz[5] = functionangulo51 * (angulo-142); 
		else 
			anguloFuzz[5] = functionangulo52 * (255 - angulo);


	if (angulo < 166) 
		anguloFuzz[6] = 0;
	else 
		if (angulo > 177) 
			anguloFuzz[6] = 255;
		else 
			anguloFuzz[6] = functionangulo6 * (angulo - 166);


 //velocidad------------------------------------

	if (velocidad < 25) 
		velocidadFuzz[0] = 255;
	else 
		if (velocidad > 64) 
			velocidadFuzz[0] = 0; 
		else 
			velocidadFuzz[0] = functionvelocidad0 * (64 - velocidad);


	if (velocidad < 0 || velocidad > 106) 
		velocidadFuzz[1] = 0;
	else 
		if (velocidad > 64) 
			velocidadFuzz[1] = functionvelocidad11 * (velocidad-0); 
		else 
			velocidadFuzz[1] = functionvelocidad12 * (106 - velocidad);


	if (velocidad < 64 || velocidad > 127) 
		velocidadFuzz[2] = 0;
	else 
		if (velocidad > 106) 
			velocidadFuzz[2] = functionvelocidad21 * (velocidad-64); 
		else 
			velocidadFuzz[2] = functionvelocidad22 * (127 - velocidad);


	if (velocidad < 106 || velocidad > 149) 
		velocidadFuzz[3] = 0;
	else 
		if (velocidad > 127) 
			velocidadFuzz[3] = functionvelocidad31 * (velocidad-106); 
		else 
			velocidadFuzz[3] = functionvelocidad32 * (149 - velocidad);


	if (velocidad < 127 || velocidad > 191) 
		velocidadFuzz[4] = 0;
	else 
		if (velocidad > 149) 
			velocidadFuzz[4] = functionvelocidad41 * (velocidad-127); 
		else 
			velocidadFuzz[4] = functionvelocidad42 * (191 - velocidad);


	if (velocidad < 149 || velocidad > 255) 
		velocidadFuzz[5] = 0;
	else 
		if (velocidad > 191) 
			velocidadFuzz[5] = functionvelocidad51 * (velocidad-149); 
		else 
			velocidadFuzz[5] = functionvelocidad52 * (255 - velocidad);


	if (velocidad < 191) 
		velocidadFuzz[6] = 0;
	else 
		if (velocidad > 229) 
			velocidadFuzz[6] = 255;
		else 
			velocidadFuzz[6] = functionvelocidad6 * (velocidad - 191);


} 
 
void rulesEvaluation(){ 
 #pragma HLS PIPELINE 
 #pragma #pragma HLS ARRAY_PARTITION variable=anguloFuzz complete dim=1 
 #pragma #pragma HLS ARRAY_PARTITION variable=velocidadFuzz complete dim=1 
 #pragma #pragma HLS ARRAY_PARTITION variable=fuerzaMembershipValues complete dim=1 

	fixed_int aux = 0; 
	fuerzaMembershipValues[0] = min(anguloFuzz[6],velocidadFuzz[5]); 

	aux = min(anguloFuzz[5], velocidadFuzz[6]); 
	if (fuerzaMembershipValues[0] < aux) 
		 fuerzaMembershipValues[0] = aux; 

	aux = min(anguloFuzz[6], velocidadFuzz[6]); 
	if (fuerzaMembershipValues[0] < aux) 
		 fuerzaMembershipValues[0] = aux; 

	fuerzaMembershipValues[1] = min(anguloFuzz[6],velocidadFuzz[3]); 

	aux = min(anguloFuzz[5], velocidadFuzz[4]); 
	if (fuerzaMembershipValues[1] < aux) 
		 fuerzaMembershipValues[1] = aux; 

	aux = min(anguloFuzz[6], velocidadFuzz[4]); 
	if (fuerzaMembershipValues[1] < aux) 
		 fuerzaMembershipValues[1] = aux; 

	aux = min(anguloFuzz[4], velocidadFuzz[5]); 
	if (fuerzaMembershipValues[1] < aux) 
		 fuerzaMembershipValues[1] = aux; 

	aux = min(anguloFuzz[5], velocidadFuzz[5]); 
	if (fuerzaMembershipValues[1] < aux) 
		 fuerzaMembershipValues[1] = aux; 

	aux = min(anguloFuzz[3], velocidadFuzz[6]); 
	if (fuerzaMembershipValues[1] < aux) 
		 fuerzaMembershipValues[1] = aux; 

	aux = min(anguloFuzz[4], velocidadFuzz[6]); 
	if (fuerzaMembershipValues[1] < aux) 
		 fuerzaMembershipValues[1] = aux; 

	fuerzaMembershipValues[2] = min(anguloFuzz[6],velocidadFuzz[1]); 

	aux = min(anguloFuzz[5], velocidadFuzz[2]); 
	if (fuerzaMembershipValues[2] < aux) 
		 fuerzaMembershipValues[2] = aux; 

	aux = min(anguloFuzz[6], velocidadFuzz[2]); 
	if (fuerzaMembershipValues[2] < aux) 
		 fuerzaMembershipValues[2] = aux; 

	aux = min(anguloFuzz[4], velocidadFuzz[3]); 
	if (fuerzaMembershipValues[2] < aux) 
		 fuerzaMembershipValues[2] = aux; 

	aux = min(anguloFuzz[5], velocidadFuzz[3]); 
	if (fuerzaMembershipValues[2] < aux) 
		 fuerzaMembershipValues[2] = aux; 

	aux = min(anguloFuzz[3], velocidadFuzz[4]); 
	if (fuerzaMembershipValues[2] < aux) 
		 fuerzaMembershipValues[2] = aux; 

	aux = min(anguloFuzz[4], velocidadFuzz[4]); 
	if (fuerzaMembershipValues[2] < aux) 
		 fuerzaMembershipValues[2] = aux; 

	aux = min(anguloFuzz[2], velocidadFuzz[5]); 
	if (fuerzaMembershipValues[2] < aux) 
		 fuerzaMembershipValues[2] = aux; 

	aux = min(anguloFuzz[3], velocidadFuzz[5]); 
	if (fuerzaMembershipValues[2] < aux) 
		 fuerzaMembershipValues[2] = aux; 

	aux = min(anguloFuzz[1], velocidadFuzz[6]); 
	if (fuerzaMembershipValues[2] < aux) 
		 fuerzaMembershipValues[2] = aux; 

	aux = min(anguloFuzz[2], velocidadFuzz[6]); 
	if (fuerzaMembershipValues[2] < aux) 
		 fuerzaMembershipValues[2] = aux; 

	fuerzaMembershipValues[3] = min(anguloFuzz[6],velocidadFuzz[0]); 

	aux = min(anguloFuzz[5], velocidadFuzz[1]); 
	if (fuerzaMembershipValues[3] < aux) 
		 fuerzaMembershipValues[3] = aux; 

	aux = min(anguloFuzz[4], velocidadFuzz[2]); 
	if (fuerzaMembershipValues[3] < aux) 
		 fuerzaMembershipValues[3] = aux; 

	aux = min(anguloFuzz[3], velocidadFuzz[3]); 
	if (fuerzaMembershipValues[3] < aux) 
		 fuerzaMembershipValues[3] = aux; 

	aux = min(anguloFuzz[2], velocidadFuzz[4]); 
	if (fuerzaMembershipValues[3] < aux) 
		 fuerzaMembershipValues[3] = aux; 

	aux = min(anguloFuzz[1], velocidadFuzz[5]); 
	if (fuerzaMembershipValues[3] < aux) 
		 fuerzaMembershipValues[3] = aux; 

	aux = min(anguloFuzz[0], velocidadFuzz[6]); 
	if (fuerzaMembershipValues[3] < aux) 
		 fuerzaMembershipValues[3] = aux; 

	fuerzaMembershipValues[4] = min(anguloFuzz[4],velocidadFuzz[0]); 

	aux = min(anguloFuzz[5], velocidadFuzz[0]); 
	if (fuerzaMembershipValues[4] < aux) 
		 fuerzaMembershipValues[4] = aux; 

	aux = min(anguloFuzz[3], velocidadFuzz[1]); 
	if (fuerzaMembershipValues[4] < aux) 
		 fuerzaMembershipValues[4] = aux; 

	aux = min(anguloFuzz[4], velocidadFuzz[1]); 
	if (fuerzaMembershipValues[4] < aux) 
		 fuerzaMembershipValues[4] = aux; 

	aux = min(anguloFuzz[2], velocidadFuzz[2]); 
	if (fuerzaMembershipValues[4] < aux) 
		 fuerzaMembershipValues[4] = aux; 

	aux = min(anguloFuzz[3], velocidadFuzz[2]); 
	if (fuerzaMembershipValues[4] < aux) 
		 fuerzaMembershipValues[4] = aux; 

	aux = min(anguloFuzz[1], velocidadFuzz[3]); 
	if (fuerzaMembershipValues[4] < aux) 
		 fuerzaMembershipValues[4] = aux; 

	aux = min(anguloFuzz[2], velocidadFuzz[3]); 
	if (fuerzaMembershipValues[4] < aux) 
		 fuerzaMembershipValues[4] = aux; 

	aux = min(anguloFuzz[0], velocidadFuzz[4]); 
	if (fuerzaMembershipValues[4] < aux) 
		 fuerzaMembershipValues[4] = aux; 

	aux = min(anguloFuzz[1], velocidadFuzz[4]); 
	if (fuerzaMembershipValues[4] < aux) 
		 fuerzaMembershipValues[4] = aux; 

	aux = min(anguloFuzz[0], velocidadFuzz[5]); 
	if (fuerzaMembershipValues[4] < aux) 
		 fuerzaMembershipValues[4] = aux; 

	fuerzaMembershipValues[5] = min(anguloFuzz[2],velocidadFuzz[0]); 

	aux = min(anguloFuzz[3], velocidadFuzz[0]); 
	if (fuerzaMembershipValues[5] < aux) 
		 fuerzaMembershipValues[5] = aux; 

	aux = min(anguloFuzz[1], velocidadFuzz[1]); 
	if (fuerzaMembershipValues[5] < aux) 
		 fuerzaMembershipValues[5] = aux; 

	aux = min(anguloFuzz[2], velocidadFuzz[1]); 
	if (fuerzaMembershipValues[5] < aux) 
		 fuerzaMembershipValues[5] = aux; 

	aux = min(anguloFuzz[0], velocidadFuzz[2]); 
	if (fuerzaMembershipValues[5] < aux) 
		 fuerzaMembershipValues[5] = aux; 

	aux = min(anguloFuzz[1], velocidadFuzz[2]); 
	if (fuerzaMembershipValues[5] < aux) 
		 fuerzaMembershipValues[5] = aux; 

	aux = min(anguloFuzz[0], velocidadFuzz[3]); 
	if (fuerzaMembershipValues[5] < aux) 
		 fuerzaMembershipValues[5] = aux; 

	fuerzaMembershipValues[6] = min(anguloFuzz[0],velocidadFuzz[0]); 

	aux = min(anguloFuzz[1], velocidadFuzz[0]); 
	if (fuerzaMembershipValues[6] < aux) 
		 fuerzaMembershipValues[6] = aux; 

	aux = min(anguloFuzz[0], velocidadFuzz[1]); 
	if (fuerzaMembershipValues[6] < aux) 
		 fuerzaMembershipValues[6] = aux; 

} 

const fixed_int outputValuesfuerza[7] = {0, 51, 106, 127, 149, 204, 255}; 
fixed_int defuzzifierfuerza() {
 #pragma HLS PIPELINE 
 #pragma #pragma HLS ARRAY_PARTITION variable=fuerzaMembershipValues complete dim=1 
 #pragma #pragma HLS ARRAY_PARTITION variable=outputValuesfuerza complete dim=1 

	fixed_aux_fuerza numerator = 0; 
 	fixed_aux_fuerza denominator = 0; 
 	for(int i=0; i<7; i++){ 
		numerator = numerator + fuerzaMembershipValues[i] * outputValuesfuerza[i]; 
		denominator = denominator + fuerzaMembershipValues[i]; 
	} 
	return numerator/denominator; 
}

fixed_out fuzzyController(fixed_int angulo, fixed_int velocidad){ 
 #pragma HLS DATAFLOW 

	fuzzifier(angulo, velocidad);
	rulesEvaluation(); 

	fixed_out output = defuzzifierfuerza(); 
	return output;
}

# Generador FLC

Herramienta de generación de FLC en alto nivel. En base a una descripción abstracta de los componentes de un controlador difuso, se genera un código en C++, optimizado 
usando las directivas de Vivado HLS. Usando dicha herarmienta de Xilinx es posible generar códigos en lenguaje VHDL o Verilog, ejecutables en cualquier placa compatible.

Contenidos:
- El proyecto JAVA completo puede encontrarse en la capreta "Compilador". Para su desarrollo se utilizo el IDE Eclipse, por lo que se recomienda abrir el proyecto con 
dicha herramienta para evitar problemas de compatibilidad.

- El directorio YACC contiene la gramatica usada por el programa, ademas de la versión de YACC usada en la compilación y un pequeño script usado para compilar y reemplazar 
los archivos del parser generado dentro del proyecto de JAVA.

- El archivo Generador FLC v1.0 contiene una primera versión ejecutable del programa.

- El archivo Lenguaje_ejemplo.txt muestra un ejemplo del lenguaje utilizado por la herramienta. Puede usarse para testear el funcionamiento de la herramienta.

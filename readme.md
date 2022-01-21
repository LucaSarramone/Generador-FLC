# Generador FLC

Herramienta de generación de FLC en alto nivel. En base a una descripción abstracta de los componentes de un controlador difuso, se genera un código en C++, optimizado 
usando las directivas de Vivado HLS. Usando dicha herramienta de Xilinx es posible generar códigos en lenguaje VHDL o Verilog, ejecutables en cualquier placa compatible.

Contenidos:
- El proyecto JAVA completo puede encontrarse en la carpeta "Compilador". Para su desarrollo se utilizo el IDE Eclipse, por lo que se recomienda abrir el proyecto con 
dicha herramienta para evitar problemas de compatibilidad.

- El directorio YACC contiene la gramática usada por el programa, ademas de la versión de YACC usada en la compilación y un pequeño script usado para compilar y reemplazar 
los archivos del parser generado dentro del proyecto de JAVA.

- El archivo Generador FLC contiene una versión ejecutable del programa.

- El directorio llamado "Ejemplos" contiene la descripción en alto nivel para el problema del péndulo invertido, estacionamiento y autoenfoque usada por el generador, ademas de sus respectivos
archivos generados por la herramienta. Estos últimos pueden borrarse y recompilarse procesando el archivo original (extensión txt) con la herramienta.

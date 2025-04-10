package calcularprecio;

import javax.swing.JOptionPane;

public class Impuestos {
    
    public static double agregarImpuesto(double subtotal, double porcImp) {
        if(!validarPorcentaje(porcImp)){
            mostrarError("Coloque un porcentaje valido");
        }
           return subtotal * (1 + (porcImp / 100));
    }
    
    // --- Métodos auxiliares ---
    private static boolean validarPorcentaje(double porcentaje) {
        return porcentaje >= 0 && porcentaje <= 100;
    }
    
    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "ERROR", JOptionPane.WARNING_MESSAGE);
    }
}
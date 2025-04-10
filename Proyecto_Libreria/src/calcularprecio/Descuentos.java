/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package calcularprecio;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class Descuentos {
    public static double calcularPorcentajeDescuento(double precioOG, double porcDescuento){
        if(!validarPrecio(precioOG)||!validarDescuento(porcDescuento)){
            JOptionPane.showMessageDialog(null,"PORCENTAJE NO VALIDO", "ERROR",JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return precioOG - (precioOG * porcDescuento / 100);
    }
    
    private static boolean validarDescuento(double descuento) {
        return descuento >= 0 && descuento <= 100;
    }
    
    private static boolean validarPrecio(double precio) {
        return precio > 0;
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "ERROR", JOptionPane.WARNING_MESSAGE);
    }
     
     
}

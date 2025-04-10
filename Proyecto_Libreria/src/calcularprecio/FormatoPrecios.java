/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calcularprecio;

/**
 *
 * @author migue
 */
import javax.swing.JOptionPane;
import java.text.DecimalFormat;

public class FormatoPrecios {
    
    public static double redondearPrecio(double precio, int decimales) {
        if (decimales < 0 || precio <= 0) {
            mostrarError("Datos inválidos: Precio > 0 y decimales >= 0");
            return -1;
        }
        double factor = Math.pow(10, decimales);
        return Math.round(precio * factor) / factor;
    }
    
    public static String formatearPrecio(double precio) {
        if (precio <= 0) {
            return "$0.00";
        }
        DecimalFormat df = new DecimalFormat("$#0.00");
        return df.format(precio);
    }
    
    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "ERROR", JOptionPane.WARNING_MESSAGE);
    }
}

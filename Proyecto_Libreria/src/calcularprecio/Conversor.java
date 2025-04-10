/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package calcularprecio;

import javax.swing.JOptionPane;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class Conversor {
    private static final String API_KEY = "a007f3623d1136f7d76564e9"; // Tu API key
    private JSONObject tasasDeCambio;

    public Conversor() {
        actualizarTasas();
    }

    private void actualizarTasas() {
        try {
            URL url = new URL("https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/USD");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");

            BufferedReader lector = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            StringBuilder respuesta = new StringBuilder();
            String linea;

            while ((linea = lector.readLine()) != null) {
                respuesta.append(linea);
            }
            lector.close();

            JSONObject json = new JSONObject(respuesta.toString());
            this.tasasDeCambio = json.getJSONObject("conversion_rates");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al obtener tasas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public double convertir(double cantidad, String monedaOrigen, String monedaDestino) {
        if (tasasDeCambio == null) {
            JOptionPane.showMessageDialog(null, "No se pudieron cargar las tasas de cambio", "Error", JOptionPane.WARNING_MESSAGE);
            return -1;
        }

        try {
            double tasaOrigen = tasasDeCambio.getDouble(monedaOrigen);
            double tasaDestino = tasasDeCambio.getDouble(monedaDestino);
            return (cantidad / tasaOrigen) * tasaDestino;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Moneda no válida: " + e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
    }

    public String formatearConversion(double cantidad, String monedaOrigen, double resultado, String monedaDestino) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(cantidad) + " " + monedaOrigen + " = " + df.format(resultado) + " " + monedaDestino;
    }
}

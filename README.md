# Calculo-de-precios-y-descuentos
Descripción general: Libreria que ofrece conjunto de clases diseñadas para calcular precios finales en operaciones de venta, incluyendo conversión de moneda, aplicación de impuestos, descuentos porcentuales o fijos, y formateo de precios.

# Indice
- [Explicación del código](#Explicación_del_codigo)
- [Instrucciones para importar el .jar](#Instrucciones_para_importar_el_.jar)
- [Video Explicativo](#Video_Explicativo) 
- [Autores](#Autores)

# Explicación del codigo

## Clase Conversor
Clase encargada de convertir montos de una moneda a otra utilizando tasas de cambio obtenidas desde una API externa (https://www.exchangerate-api.com). La clase se conecta a una API, obtiene las tasas de conversion basados en el dolar Estadounidense (USD), para finalmente poderconvertir valores entre monedas.

### Métodos

#### Constructor()

```java
public Conversor() {
        actualizarTasas();
    }
```
##### _Explicación_:
El constructor llama al método actualizarTasas() cuando se crea una instancia de la clase, con el objetivo de cargar las tasas de cambio desde la API.

#### actualizarTasas()
Tiene la función de conectarse a la API, hacer una solicitud para obtener las tasas de cambio con respecto al dólar estadounidense (USD), y guardar esa información en el atributo tasasDeCambio para usarla después en conversiones de moneda.  

```java
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
```
##### _Explicación_:
  1.-Primero se construye una URL que apunta al endpoint que proporciona las tasas de conversión.
  ```java
  try {
    // Crear la URL con la clave API y el endpoint 'latest/USD'
    URL url = new URL("https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/USD");
  ```
  2.-Se abre una conexión HTTP con la URL y especifica que se quiere hacer una solicitud de recopilación de datos (GET), el cual en este caso son las tasas de cambio.
  ```java
   HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
   conexion.setRequestMethod("GET");
  ```
  3.-Se crea un lector de flujo de entrada para leer la respuesta que envía el servidor de la API. La instancia InputStreamReader convierte los bytes en caracteres, y la instancia BufferedReader permite leerlos línea por línea.
  ```java
  BufferedReader lector = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
  ```
 4.-Luego se inicializa una variable de la clase StringBuilder para almacenar la respuesta, lee cada línea de la respuesta y se agrega al StringBuilder, y finalmente se cierra el lector para liberar recursos.
   ```java
  StringBuilder respuesta = new StringBuilder();
  String linea;

  while ((linea = lector.readLine()) != null) {
      respuesta.append(linea);
  }
      lector.close();
  ```
 5.-Se convierte la respuesta (cabe aclarar que la respuesta es un texto en formato JSON) en un objeto de tipo JSONObject. Posteriormente se extrae del JSON principal el objeto "conversion_rates", el cual contiene un listado de pares moneda -- tasa. 
   ```java
   JSONObject json = new JSONObject(respuesta.toString());
   this.tasasDeCambio = json.getJSONObject("conversion_rates");
  ```
 El contenido se ve de la siguiente manera:
  ```json
{
  "MXN": 17.50,
  "EUR": 0.92,
  "JPY": 151.15,
  ...
}
```
El objeto se guarda en el atributo tasasDeCambio
  
6.-Si ocurre cualquier excepción durante el proceso (error de conexión, URL inválida, error de parsing), se captura en este bloque.
  ```java
  } catch (Exception e) {
    JOptionPane.showMessageDialog(null, "Error al obtener tasas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
  }
 ```
#### convertir()
Realiza la conversion  de una cantidad de dinero de una moneda a otra, utilizando las tasas de cambio que previamente se han cargado a través del método actualizarTasas().
  ```java
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
  ```
#### _Parámetros_:
```java
public double convertir(double cantidad, String monedaOrigen, String monedaDestino) {}
```
--cantidad: Es cantidad de dinero que se quiere convertir.

--monedaOrigen: Es el código de la moneda de la que se desea convertir (por ejemplo, dolar("USD"), euro("EUR"), peso mexicano("MXN")).

--monedaDestino: El código de la moneda a la que se desea convertir (por ejemplo, "MXN", "EUR", "USD").

##### _Valor de retorno_: 
Un valor double que representa la cantidad convertida, o -1 en caso de error.

##### _Explicación_:
1.-Primero verifica si el objeto tasaDeCambio  es nulo. Esto ocurriría si las tasas de cambio no se pudieron cargar correctamente, es decir, si el método actualizarTasas() falló. En caso de error muestra un mensaje de advertencia y retorna -1.
  ```java
  if (tasasDeCambio == null) {
    JOptionPane.showMessageDialog(null, "No se pudieron cargar las tasas de cambio", "Error", JOptionPane.WARNING_MESSAGE);
    return -1;
}
  ```
2.-Se extraen las tasas de cambio para la moneda de origen (tasaOrigen) y la moneda de destino (tasaDestino) del objeto tasasDeCambio.
  ```java
  try {
    double tasaOrigen = tasasDeCambio.getDouble(monedaOrigen);
    double tasaDestino = tasasDeCambio.getDouble(monedaDestino);
  ```
3.-Se divide la cantidad entre la tasa de la moneda de origen para obtener su valor en términos de USD. Luego se multiplica por la tasa de la moneda destino para obtener la cantidad convertida en la moneda destino.
  ```java
  return (cantidad / tasaOrigen) * tasaDestino;
  ```
4.-Si ocurre algún error durante el proceso, el bloque catch captura la excepción.
  ```java
  } catch (Exception e) {
    JOptionPane.showMessageDialog(null, "Moneda no válida: " + e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
    return -1;
}
  ```
##### formatearConversion()
Toma los valores de la conversión la cantidad original, la moneda de origen, el resultado convertido y la moneda de destino) y devuelve una cadena de texto que muestre la conversión de manera legible.
```java
  public String formatearConversion(double cantidad, String monedaOrigen, double resultado, String monedaDestino) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(cantidad) + " " + monedaOrigen + " = " + df.format(resultado) + " " + monedaDestino;
    }
```
##### _Parámetros_:
```java
public String formatearConversion(double cantidad, String monedaOrigen, double resultado, String monedaDestino) {}
```
--cantidad:  Es la cantidad de dinero original que se va a convertir.

--monedaOrigen: Es la moneda de la que se está convirtiendo (por ejemplo, "USD", "EUR", "MXM").

--resultado: Es el valor convertido de la cantidad en la moneda de destino.

--monedaDestino: Es la moneda a la que se ha convertido la cantidad (por ejemplo, "MXN", "EUR", "USD").

##### _Valor de retorno_:
Una cadena de texto que representa la conversión con el formato adecuado.

##### _Explicación_:
1.-El método utiliza la clase DecimalFormat para formatear números de acuerdo con un patrón específico, donde el patrón #,##0.00 indica cómo debe formatearse el número.

2.-Retorna un String que contiene la conversión en un formato legible, como por ejemplo:
```java
"1,000.00 USD = 18,000.00 MXN"
```
Se utiliza el método format() de DecimalFormat para formatear tanto la cantidad como el resultado con el patrón definido (con separadores de miles y dos decimales).



## Clase Descuentos
La clase calcula el precio final de un producto después de aplicar un descuento porcentual sobre su precio original. Además, realiza las validaciones necesarias para asegurar que el precio y el porcentaje de descuento sean válidos antes de realizar el cálculo.

### Métodos

#### calcularPorcentajeDescuento()
```java
public static double calcularPorcentajeDescuento(double precioOG, double porcDescuento){
        if(!validarPrecio(precioOG)||!validarDescuento(porcDescuento)){
            JOptionPane.showMessageDialog(null,"PORCENTAJE NO VALIDO", "ERROR",JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return precioOG - (precioOG * porcDescuento / 100);
    }
```
##### _Parámetros_:
```java
public static double calcularPorcentajeDescuento(double precioOG, double porcDescuento){}
```
--precioOG: Precio original del producto o servicio.

--porcDescuento: Porcentaje de descuento a aplicar.

##### _Valor de retorno_: 
Un valor double que representa la cantidad convertida, o -1 en caso de error.

##### _Explicación_:
1.-Realiza la validación del precio y del porcentaje de descuento llamando a los métodos validarPrecio() y validarDescuento().
```java
if(!validarPrecio(precioOG)||!validarDescuento(porcDescuento))
```
2.-Si cualquiera de los dos valores no es válido, muestra un mensaje de advertencia con JOptionPane y retorna -1 para indicar un error.
```java
JOptionPane.showMessageDialog(null,"PORCENTAJE NO VALIDO", "ERROR",JOptionPane.WARNING_MESSAGE);
return -1;
```
3.-Si los valores son válidos, aplica la fórmula de descuento.
```java
return precioOG - (precioOG * porcDescuento / 100);
```

#### validarDescuento()
```java
 private static boolean validarDescuento(double descuento) {
        return descuento >= 0 && descuento <= 100;
    }
```
##### _Parámetros_:
--descuento: Porcentaje de descuento a aplicar
##### _Valor de retorno_: 
--true si el descuento está entre 0 y 100.

--false si el descuento está fuera de este rango.
##### _Explicación_:

1.-Valida que el valor del descuento esté en el rango válido entre 0 y 100. Esto asegura que el descuento no sea negativo ni mayor al 100%.
```java
  return descuento >= 0 && descuento <= 100;
```

#### validarPrecio()
```java
 private static boolean validarPrecio(double precio) {
        return precio > 0;
    }
```
##### _Parámetros_:
--precio: Valor del precio a ingresar.
##### _Valor de retorno_: 
--true si el precio es mayor que cero.

--false si el precio es cero o negativo.
##### _Explicación_:
1.-Valida que el precio original sea mayor que cero, ya que no tiene sentido trabajar con precios negativos o cero en una compra
```java
  return precio > 0;
```

#### validarPrecio()
```java
 private static boolean validarPrecio(double precio) {
        return precio > 0;
    }
```
##### _Parámetros_:
--precio: Valor del precio a ingresar.
##### _Valor de retorno_: 
--true si el precio es mayor que cero.

--false si el precio es cero o negativo.
##### _Explicación_:
1.-Valida que el precio original sea mayor que cero, ya que no tiene sentido trabajar con precios negativos o cero en una compra
```java
  return precio > 0;
```

#### mostrarError()
```java
 private void mostrarError(String mensaje) {
    JOptionPane.showMessageDialog(null, mensaje, "ERROR", JOptionPane.WARNING_MESSAGE);
}
```
##### _Parámetros_:
--mensaje: El mensaje que se desea mostrar en la ventana de advertencia.

##### _Explicación_:
1.-Muestra un mensaje de error al usuario en una ventana emergente. Se usa para mostrar advertencias o errores específicos relacionados con la aplicación.
```java
  JOptionPane.showMessageDialog(null, mensaje, "ERROR", JOptionPane.WARNING_MESSAGE);
```


## Clase Impuestos
La clase calcula el precio final de un producto o servicio después de aplicar un impuesto. Además, valida que el porcentaje del impuesto sea un valor válido entre 0 y 100. Si el porcentaje del impuesto no es válido, muestra un mensaje de advertencia al usuario.

### Métodos

#### agregarImpuesto()
```java
public static double agregarImpuesto(double subtotal, double porcImp) {
        if(!validarPorcentaje(porcImp)){
            mostrarError("Coloque un porcentaje valido");
        }
           return subtotal * (1 + (porcImp / 100));
    }
```
##### _Parámetros_:
--subtotal: El precio base o subtotal de los productos, sin impuestos.

--porcImp: El porcentaje de impuesto que se desea aplicar.
##### _Valor de retorno_: 
El precio final después de aplicar el impuesto.
##### _Explicación_:
1.-Primero, se valida si el porcentaje del impuesto es válido llamando al método.
```java
if(!validarPorcentaje(porcImp))
```
2.-Si el porcentaje no es válido (fuera del rango de 0 a 100), se llama al método mostrarError() para mostrar un mensaje de advertencia al usuario.
```java
mostrarError("Coloque un porcentaje valido");
```
3.-Si el porcentaje es valido, calcula el precio final sumando el impuesto al subtotal. 
```java
return subtotal * (1 + (porcImp / 100));
```

#### validarPorcentaje()
```java
private static boolean validarPorcentaje(double porcentaje) {
        return porcentaje >= 0 && porcentaje <= 100;
    }
```
##### _Parámetros_:
--porcentaje: El porcentaje de impuesto que se desea aplicar.
##### _Valor de retorno_: 
--true si el porcentaje está dentro del rango de 0 a 100.

--false si el porcentaje es menor que 0 o mayor que 100.
##### _Explicación_:
1.-Valida que el porcentaje del impuesto esté dentro del rango válido de 0 a 100, asegurando que el impuesto no sea negativo ni mayor que el 100%.
```java
return porcentaje >= 0 && porcentaje <= 100;
```

#### mostrarError()
```java
private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "ERROR", JOptionPane.WARNING_MESSAGE);
    }
```
##### _Parámetros_:
--mensaje: El mensaje que se mostrará en la ventana de error.
##### _Explicación_:
1.-Muestra un mensaje de advertencia al usuario utilizando una ventana emergente (JOptionPane), indicando que ocurrió un error con el valor ingresado
```java
JOptionPane.showMessageDialog(null, mensaje, "ERROR", JOptionPane.WARNING_MESSAGE);
```


## Clase FormatoPrecios
La clase formatea precios para mostrar valores monetarios de una manera adecuada, además de permitir redondear precios a un número específico de decimales. Está diseñada para asegurar que los precios se muestren de manera clara y profesional

### Métodos

#### redondearPrecio()
```java
public static double redondearPrecio(double precio, int decimales) {
        if (decimales < 0 || precio <= 0) {
            mostrarError("Datos inválidos: Precio > 0 y decimales >= 0");
            return -1;
        }
        double factor = Math.pow(10, decimales);
        return Math.round(precio * factor) / factor;
    }
```
##### _Parámetros_:
--precio: El precio que se desea redondear.

--decimales: La cantidad de decimales a la que se desea redondear el precio
##### _Valor de retorno_: 

El precio redondeado al número de decimales especificado, o -1 en caso de un error.
##### _Explicación_:
1.-Verifica que el precio  sea mayor que cero y que la cantidad de decimales no sea negativa. 
```java
if (decimales < 0 || precio <= 0) {
```
2.-Si alguna de estas condiciones no se cumple, se muestra un mensaje de error a través del método mostrarError() y el método retorna -1 para indicar un error.
```java
mostrarError("Datos inválidos: Precio > 0 y decimales >= 0");
return -1;
```
3.-Calcula un factor de multiplicación basado en el número de decimales.
```java
double factor = Math.pow(10, decimales);
```
3.-Multiplica el precio por el factor, redondea el resultado usando Math.round(), y luego lo divide nuevamente por el factor para obtener el valor redondeado.
```java
return Math.round(precio * factor) / factor;
```

#### formatearPrecio()
```java
public static String formatearPrecio(double precio) {
        if (precio <= 0) {
            return "$0.00";
        }
        DecimalFormat df = new DecimalFormat("$#0.00");
        return df.format(precio);
    }
```
##### _Parámetros_:
--precio: El precio que se desea redondear.
##### _Valor de retorno_: 
Una cadena con el precio formateado en formato monetario.
##### _Explicación_:
1.-Si el precio es menor o igual a cero, devuelve una cadena "$0.00", asegurando que los precios negativos o nulos no se muestren de forma incorrecta.
```java
if (precio <= 0) {
        return "$0.00";
}
```
2.-Si el precio es mayor que cero, utiliza la clase DecimalFormat con el patrón "$#0.00" para formatear el precio.
```java
DecimalFormat df = new DecimalFormat("$#0.00");
return df.format(precio);
```

#### mostrarError()
```java
private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "ERROR", JOptionPane.WARNING_MESSAGE);
    }
```
##### _Parámetros_:
--mensaje: El mensaje que se mostrará en la ventana de error.
##### _Explicación_:
1.-Muestra un mensaje de advertencia al usuario utilizando una ventana emergente (JOptionPane), indicando que ocurrió un error con el valor ingresado
```java
JOptionPane.showMessageDialog(null, mensaje, "ERROR", JOptionPane.WARNING_MESSAGE);
```

# Instrucciones para importar el .jar

## 1.-Abre tu proyecto en NetBeans.
![Paso1](https://github.com/Dany-502/Calculo-de-precios-y-descuentos/blob/c31a7777a8af7a13852dc431ca77a7d51f02c7ea/Capturas_TAP/PASO1.png)
## 2.-Haz clic derecho en la carpeta Libraries (dentro de tu proyecto en el panel Projects).
## 3.-Selecciona "Add JAR/Folder...", deberá abrirte la siguiente ventana:
## 4.-Busca el archivo .jar que descargaste y seleccionalo.
## 5.-Haz clic en "Open".
## 6.-La librería aparecerá en Libraries y podrás usarla en tu código.

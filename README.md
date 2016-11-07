# Java
Ejemplo con la integración al Webservice de Timbox

Se deberá hacer uso de las URL que hacen referencia al WSDL, en cada petición realizada:

- [Timbox Pruebas](https://staging.ws.timbox.com.mx/timbrado/wsdl)

- [Timbox Producción](https://sistema.timbox.com.mx/timbrado/wsdl)

Para integrar el Webservice al proyecto se requiere hacer uso del modulo Base64:

```
import java.nio.charset.StandardCharsets;
import java.util.Base64;
```

También se requiere importar la libreria:

```
import javax.xml.soap.*;
```

##Timbrar CFDI
Para hacer una petición de timbrado de un CFDI, deberá enviar las credenciales asignadas, asi como el xml que desea timbrar convertido a una cadena en base64:

En el proyecto existe una Clase Timbrado.java, la cual facilita la creacion de timbrado, solo es necesario agragarla al proyecto y crear un objeto.
```
//Timbrar Factura
// Parametros para el servicio
String usuarioTimbrado = "usuario_prueba";
String contrasenaTimbrado = "contrasena_prueba";
String documentoTimbrado = "xml";

try {
    // Creacion del objeto Timbrado
    Timbrado timbrado = new Timbrado(usuarioTimbrado, contrasenaTimbrado, documentoTimbrado);
    //Ejecucion del servicio
    SOAPMessage soapResponse = timbrado.Timbrar();
    // Imprime la respuesta
    System.out.print("Response SOAP Message:");
    soapResponse.writeTo(System.out);
} catch (Exception exception) {
    throw exception;
}
```

##Cancelar CFDI
Para la cancelación son necesarias las credenciales asignadas, RFC del emisor, un arreglo de UUIDs, el archivo PFX convertido a cadena en base64 y el password del archivo PFX:

En el proyecto existe una Clase Timbrado.java, la cual facilita la creacion de timbrado, solo es necesario agragarla al proyecto y crear un objeto.
```
//Cancelar Factura
// Parametros para el servicio
String usuarioCancelacion = "usuario_prueba";
String contrasenaCancelacion = "contrasena_prueba";
String rfcEmisorCancelacion = "RFCPRUEBA";
String[] uuidsCancelacion = { "UUID1", "UUID2", "UUID3" };
String pfxCancelacion = "valor_PFX";
String pfxContrasenaCancelacion = "contrasena_prueba";
// Conversion del PFX a base64
byte[] bytes = pfxCancelacion.getBytes(StandardCharsets.UTF_8);
String encodedPfxCancelacion  = Base64.getEncoder().encodeToString(bytes);

try {
    // Creacion del objeto Cancelacion
    Cancelacion timbrado = new Cancelacion(usuarioCancelacion, contrasenaCancelacion, rfcEmisorCancelacion, uuidsCancelacion, encodedPfxCancelacion, pfxContrasenaCancelacion);
    //Ejecucion del servicio
    SOAPMessage soapResponse = timbrado.Cancelar();
    // Imprime la respuesta
    System.out.print("Response SOAP Message:");
    soapResponse.writeTo(System.out);
} catch (Exception exception) {
    throw exception;
}
```

# Java
Ejemplo con la integración al Webservice de Timbox

Se deberá hacer uso de las URL que hacen referencia al WSDL, en cada petición realizada:

- [Timbox Pruebas](https://staging.ws.timbox.com.mx/timbrado_cfdi33/wsdl)

- [Timbox Producción](https://sistema.timbox.com.mx/timbrado_cfdi33/wsdl)

Para integrar el Webservice al proyecto se requiere hacer uso del modulo Base64:

```
import java.nio.charset.StandardCharsets;
import java.util.Base64;
```

También se requiere importar las libreríaas:

```
import javax.xml.soap.*;
import org.jdom2.*;
```

El jar de jdom viene incluido en el repositorio y se utiliza para buscar información en el XML y actualizar los atributos de Fecha y Sello antes de timbrar.

## Timbrar CFDI
### Generacion de Sello
Para generar el sello se necesita: la llave privada (.key) en formato PEM. También es necesario incluir el XSLT del SAT para poder transformar el XML y obtener la cadena original.

La cadena original se utiliza para obtener el digest, usando comandos de OpenSSL, luego se utiliza el digest y la llave privada para obtener el sello.

Una vez generado el sello, se actualiza en el XML para que este sea codificado y enviado al servicio de timbrado.
Esto se logra mandando llamar el método de generar_sello:
```
generar_sello("ejemplo_cfdi_33.xml");
```
### Timbrado
Para hacer una petición de timbrado de un CFDI, deberá enviar las credenciales asignadas, asi como el xml que desea timbrar convertido a una cadena en base64:

En el proyecto existe una clase Timbrado.java, la cual facilita la creacion de timbrado, solo es necesario agragarla al proyecto y crear un objeto.
```
// Parametros generales para el sevicio
String usuario = "AAA010101000";
String contrasena = "h6584D56fVdBbSmmnB";

//
// Timbrar Factura
//
// Parametros para el servicio

//Actualizar sello en XML antes de mandar
generar_sello("ejemplo_cfdi_33.xml");
byte[] archivoXml = Files.readAllBytes(Paths.get("archivoXml.xml"));
String xmlBase64 = Base64.getEncoder().encodeToString(archivoXml);

try {
    // Creacion del objeto Timbrado
    Timbrado timbrado = new Timbrado(usuario, contrasena, xmlBase64);
    //Ejecucion del servicio
    String facturaTimbrada = timbrado.Timbrar();
    // Imprime la respuesta
    System.out.println("Comprobante timbrado: \n");
    System.out.print(facturaTimbrada);
} catch (Exception exception) {
    throw exception;
}
```

## Cancelar CFDI

Para la cancelación son necesarias las credenciales asignadas, RFC del emisor, un arreglo de UUIDs, el archivo PFX convertido a cadena en base64 y el password del archivo PFX:

En el proyecto existe una clase Cancelacion.java, la cual facilita la cancelacion de un CFDI, solo es necesario agragarla al proyecto y crear un objeto.
```
// Parametros generales para el sevicio
String usuario = "AAA010101000";
String contrasena = "h6584D56fVdBbSmmnB";

//Cancelar Factura
// Parametros para el servicio
String rfcEmisorCancelacion = "AAA010101AAA";
String[] uuidsCancelacion = { "E28DBCF2-F852-4B2F-8198-CD8383891EB0", "3CFF7200-0DE5-4BEE-AC22-AA2A49052FBC", "51408B33-FE29-47DA-9517-FBF420240FD3" };
byte[] pfx = Files.readAllBytes(Paths.get("archivoPfx.pfx"));
String pfxContrasena = "12345678a";
// Conversion del PFX a base64
String pfxBase64  = Base64.getEncoder().encodeToString(pfx);

try {
    // Creacion del objeto Cancelacion
    Cancelacion timbrado = new Cancelacion(usuario, contrasena, rfcEmisorCancelacion, uuidsCancelacion, pfxBase64, pfxContrasena);
    //Ejecucion del servicio
    String facturaCancelada = timbrado.Cancelar();
    // Imprime la respuesta
    System.out.println("Comprabantes cancelados: \n");
    System.out.println(facturaCancelada);
} catch (Exception exception) {
    throw exception;
}
```

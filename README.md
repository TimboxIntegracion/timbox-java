# Java
Ejemplo con la integración al Webservice de Timbox

Se deberá hacer uso de las URL que hacen referencia al WSDL, en cada petición realizada:

Webservice de Timbrado:
- [Timbox Pruebas](https://staging.ws.timbox.com.mx/timbrado_cfdi33/wsdl)

- [Timbox Producción](https://sistema.timbox.com.mx/timbrado_cfdi33/wsdl)

Webservice de Cancelación:

- [Timbox Pruebas](https://staging.ws.timbox.com.mx/cancelacion/wsdl)

- [Timbox Producción](https://sistema.timbox.com.mx/cancelacion/wsdl)

Para integrar el Webservice al proyecto se requiere hacer uso del modulo Base64:

```
import java.nio.charset.StandardCharsets;
import java.util.Base64;
```

También se requiere importar las librerías:

```
import javax.xml.soap.*;
import org.jdom2.*;
```

El jar de jdom viene incluido en el repositorio y se utiliza para actualizar los atributos de Fecha y Sello antes de timbrar.

## Timbrar CFDI
### Generación de Sello
Para generar el sello se necesita: la llave privada (.key) en formato PEM y el XSLT del SAT (cadenaoriginal_3_3.xslt).El XSLT del SAT se utiliza para poder transformar el XML y obtener la cadena original.

La cadena original se utiliza para obtener el digest, usando comandos de OpenSSL, luego se utiliza el digest y la llave privada para obtener el sello.

Una vez generado el sello, se actualiza en el XML para que este sea codificado y enviado al servicio de timbrado.
Esto se logra mandando a llamar el método de generar_sello:
```
generar_sello("ejemplo_cfdi_33.xml");
```
### Timbrado
Para hacer una petición de timbrado de un CFDI, deberá enviar las credenciales asignadas, asi como el xml que desea timbrar convertido a una cadena en base64:

En el proyecto existe una clase Timbrado.java, la cual facilita la creacion de timbrado, solo es necesario agregarla al proyecto y crear un objeto.
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
Para la cancelación son necesarios el certificado y llave, en formato pem que corresponde al emisor del comprobante:
```
byte[] file_cer_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.cer.pem"));
byte[] file_key_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.key.pem"));
```
En el proyecto existe una clase Cancelacion.java, la cual facilita la cancelacion de un CFDI, solo es necesario agregarla al proyecto y crear un objeto.
```
// Parametros generales para el sevicio
String usuario = "AAA010101000";
String contrasena = "h6584D56fVdBbSmmnB";

// Cancelar Factura
// Parametros para el servicio
String rfc_emisor = "AAA010101AAA";
String rfc_receptor = "IAD121214B34";

String uuid = "752D5486-8C1E-4F1A-8C9A-C6C28DA02E78";
String uuid2 = "DF7C62C2-A15A-4765-9034-EEDD8C9A9057";

String total = "7261.60";
String total2 = "1751.60";

byte[] file_cer_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.cer.pem"));
byte[] file_key_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.key.pem"));

String content_cer_file = new String(file_cer_pem, "UTF8");
String content_key_file = new String(file_key_pem, "UTF8");

// Agregar N folios a cancelar
List<folio> folios = new ArrayList<folio>();
folios.add(new folio(uuid, rfc_receptor, total));
folios.add(new folio(uuid2, rfc_receptor, total2));

try {
   // Creacion del objeto Cancelacion
  Cancelacion cancelacion = new Cancelacion(usuario, contrasena, rfc_emisor, folios, content_cer_file, content_key_file);
  // Ejecucion del servicio
  String facturaCancelada = cancelacion.Cancelar();
  // Imprime la respuesta
  System.out.println("Comprabantes cancelados: \n");
  System.out.println(facturaCancelada);
} catch (Exception exception) {
  throw exception;
}
```

## Consultar Estatus CFDI
Para la consulta de estatus de CFDI solo es necesario generar la petición de consulta,
En el proyecto existe una clase Consultar.java, la cual facilita la consulta de un CFDI, solo es necesario agregarla al proyecto y crear un objeto.
```
// Parametros generales para el sevicio
String usuario = "AAA010101000";
String contrasena = "h6584D56fVdBbSmmnB";

// Consultar Estatus
// Parametros para el servicio
String rfc_emisor = "AAA010101AAA";
String rfc_receptor = "IAD121214B34";

String uuid = "752D5486-8C1E-4F1A-8C9A-C6C28DA02E78";
String total = "7261.60";

try {
  // Creacion del objeto Consultar
  Consultar consulta = new Consultar(usuario, contrasena, uuid, rfc_emisor, rfc_receptor, total);
  // Ejecucion del servicio
  String consulta_estatus = consulta.Consulta();
  // Imprime la respuesta
  System.out.println("Estado: \n");
  System.out.println(consulta_estatus);
} catch (Exception exception) {
  throw exception;
}
```
## Consultar Peticiones Pendientes
Para la consulta de peticiones pendientes son necesarios el certificado y llave, en formato pem que corresponde al receptor del comprobante:
```
byte[] file_cer_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.cer.pem"));
byte[] file_key_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.key.pem"));
```
En el proyecto existe una clase PeticionesPendientes.java, la cual facilita la consulta de CDFI pendientes, solo es necesario agregarla al proyecto y crear un objeto.
```
// Parametros generales para el sevicio
String usuario = "AAA010101000";
String contrasena = "h6584D56fVdBbSmmnB";

// Consultar Peticiones Pendientes
// Parametros para el servicio
String rfc_receptor = "AAA010101AAA";

byte[] file_cer_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.cer.pem"));
byte[] file_key_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.key.pem"));

String content_cer_file = new String(file_cer_pem, "UTF8");
String content_key_file = new String(file_key_pem, "UTF8");

try {
  // Creacion del objeto PeticionesPendientes
  PeticionesPendientes peticiones = new PeticionesPendientes(usuario, contrasena, rfc_receptor, content_cer_file, content_key_file);
  // Ejecucion del servicio
  String Comprobantes = peticiones.ConsultaPeticiones();
  // Imprime la respuesta
  System.out.println("Peticiones pendientes: \n");
  System.out.println(Comprobantes);
} catch (Exception exception) {
  throw exception;
}

```

## Procesar Respuesta
Para realizar la petición de aceptación/rechazo de la solicitud de cancelación son necesarios el certificado y llave, en formato pem que corresponde al receptor del comprobante:
```
byte[] file_cer_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.cer.pem"));
byte[] file_key_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.key.pem"));
```
En el proyecto existe una clase Procesar.java, la cual facilita la petición de aceptación/rechazo de la solicitud de cancelación, solo es necesario agregarla al proyecto y crear un objeto.
```
// Parametros generales para el sevicio
String usuario = "AAA010101000";
String contrasena = "h6584D56fVdBbSmmnB";

// Procesar Respuestas
// Parametros para el servicio
String rfc_emisor = "AAA010101AAA";
String rfc_receptor = "AAA010101AAA";
String uuid = "752D5486-8C1E-4F1A-8C9A-C6C28DA02E78";
String total = "7261.60";

// A(Aceptar la solicitud), R(Rechazar la solicitud)
String respuesta = "A";

byte[] file_cer_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.cer.pem"));
byte[] file_key_pem = Files.readAllBytes(Paths.get("CSD01_AAA010101AAA.key.pem"));

String content_cer_file = new String(file_cer_pem, "UTF8");
String content_key_file = new String(file_key_pem, "UTF8");

// Agregar N foliosRespuesras a procesar
List<foliosRespuestas> foliosrespuestas = new ArrayList();
foliosrespuestas.add(new foliosRespuestas(uuid, rfc_emisor, total, respuesta));

try {
  // Creacion del objeto Procesar
  Procesar procesar = new Procesar(usuario, contrasena, rfc_receptor, foliosrespuestas, content_cer_file, content_key_file);
  // Ejecucion del servicio
  String procesar_respuestas = procesar.ProcesarRespuestas();
  // Imprime la respuesta
  System.out.println("Respuestas procesadas: \n");
  System.out.println(procesar_respuestas);
} catch (Exception exception) {
throw exception;
}
```

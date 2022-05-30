# Java
Ejemplo con la integración al Webservice de Timbox

Se deberá hacer uso de las URL que hacen referencia al WSDL, en cada petición realizada:

Webservice de Timbrado 3.3:
- [Timbox Pruebas](https://staging.ws.timbox.com.mx/timbrado_cfdi33/action)

- [Timbox Producción](https://sistema.timbox.com.mx/timbrado_cfdi33/action)

Webservice de Timbrado 4.0 :
- [Timbox Pruebas](https://staging.ws.timbox.com.mx/timbrado_cfdi40/action)

- [Timbox Producción](https://sistema.timbox.com.mx/timbrado_cfdi40/action)

Webservice de Cancelación:

- [Timbox Pruebas](https://staging.ws.timbox.com.mx/cancelacion/action)

- [Timbox Producción](https://sistema.timbox.com.mx/cancelacion/action)

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

Se debe de crear un nuevo proyecto, copiar los directorios contenidos en src/com/timbox y crear una instancia de las clases sobre la clase principal Main para ejecutar cada acción como en el siguiente ejemplo:
```
  Timbrado40.Timbrado40Main timbrado40 = new Timbrado40Main();
  timbrado40.Timbrado40();
```

## Timbrar CFDI
### Generación de Sello
Para generar el sello se necesita: la llave privada (.key) en formato PEM y el XSLT del SAT (cadenaoriginal_3_3.xslt o cadenaoriginal_4_0.xslt).El XSLT del SAT se utiliza para poder transformar el XML y obtener la cadena original.

La cadena original se utiliza para obtener el digest, usando comandos de OpenSSL, luego se utiliza el digest y la llave privada para obtener el sello.

Una vez generado el sello, se actualiza en el XML para que este sea codificado y enviado al servicio de timbrado.
Esto se logra mandando a llamar el método de generar_sello:
```
generar_sello("ejemplo_cfdi_33.xml");
```
### Timbrado 3.3
Para hacer una petición de timbrado de un CFDI, deberá enviar las credenciales asignadas, asi como el xml que desea timbrar convertido a una cadena en base64:

En el proyecto existe una clase Timbrado.java, la cual facilita la creacion de timbrado, solo es necesario agregarla al proyecto y crear un objeto.
```
// Parametros generales para el sevicio
String usuario = "usuario";
String contrasena = "contraseña";

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
### Timbrado 4.0
Para hacer una petición de timbrado de un CFDI, deberá enviar las credenciales asignadas, asi como el xml que desea timbrar convertido a una cadena en base64:

En el proyecto existe una clase Timbrado.java, la cual facilita la creacion de timbrado, solo es necesario agregarla al proyecto y crear un objeto.
```
// Parametros generales para el sevicio
String usuario = "usuario";
String contrasena = "contraseña";

//
// Timbrar Factura
//
// Parametros para el servicio

//Actualizar sello en XML antes de mandar
generar_sello("ejemplo_cfdi_40.xml");
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

A partir del 2022 será necesario señalar el motivo de la cancelación de los comprobantes. Al seleccionar como motivo de cancelación la clave 01 “Comprobante emitido con errores con relación deberá relacionarse el folio fiscal del comprobante que sustituye al cancelado. Se actualizan los plazos para realizar la cancelación de facturas.

**<b> Motivos de Cancelación (Código - Descripción) </b><br>**
**<b>  01    -    Comprobante emitido con errores con relación </b><br>**
**<b>  02    -    Comprobante emitido con errores sin relación </b><br>**
**<b>  03    -    No se llevó a cabo la operación </b><br>**
**<b>  04    -    Operación nominativa relacionada en la factura global </b><br>**

Para la cancelación son necesarios el certificado y llave, en formato pem que corresponde al emisor del comprobante:
```
byte[] file_cer_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.cer.pem"));
byte[] file_key_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.key.pem"));
```
En el proyecto existe una clase Cancelacion.java, la cual facilita la cancelacion de un CFDI, solo es necesario agregarla al proyecto y crear un objeto.
```
// Parametros generales para el sevicio
String usuario = "usuario";
String contrasena = "contraseña";

// Cancelar Factura
// Parametros para el servicio
String rfc_emisor = "EKU9003173C9";
String rfc_receptor = "IAD121214B34";

String uuid = "752D5486-8C1E-4F1A-8C9A-C6C28DA02E78";

String total = "7261.60";
String total2 = "1751.60";
        
String motivo = "02";
        
String folio_sustituto = "";

byte[] file_cer_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.cer.pem"));
byte[] file_key_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.key.pem"));

String content_cer_file = new String(file_cer_pem, "UTF8");
String content_key_file = new String(file_key_pem, "UTF8");

//Agregar N folios a cancelar
List<folio> folios = new ArrayList<folio>();
folios.add(new folio(uuid, rfc_receptor, total,motivo,folio_sustituto));

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
String usuario = "usuario";
String contrasena = "contraseña";

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
byte[] file_cer_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.cer.pem"));
byte[] file_key_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.key.pem"));
```
En el proyecto existe una clase PeticionesPendientes.java, la cual facilita la consulta de CDFI pendientes, solo es necesario agregarla al proyecto y crear un objeto.
```
// Parametros generales para el sevicio
String usuario = "usuario";
String contrasena = "contraseña";

// Consultar Peticiones Pendientes
// Parametros para el servicio
String rfc_receptor = "AAA010101AAA";

byte[] file_cer_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.cer.pem"));
byte[] file_key_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.key.pem"));

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
byte[] file_cer_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.cer.pem"));
byte[] file_key_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.key.pem"));
```
En el proyecto existe una clase Procesar.java, la cual facilita la petición de aceptación/rechazo de la solicitud de cancelación, solo es necesario agregarla al proyecto y crear un objeto.
```
// Parametros generales para el sevicio
String usuario = "usuario";
String contrasena = "contraseña";

// Procesar Respuestas
// Parametros para el servicio
String rfc_emisor = "AAA010101AAA";
String rfc_receptor = "AAA010101AAA";
String uuid = "752D5486-8C1E-4F1A-8C9A-C6C28DA02E78";
String total = "7261.60";

// A(Aceptar la solicitud), R(Rechazar la solicitud)
String respuesta = "A";

byte[] file_cer_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.cer.pem"));
byte[] file_key_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.key.pem"));

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
## Consultar Documentos Relacionados
Para realizar la petición de consultar documentos relacionados son necesarios el certificado y llave, en formato pem que corresponde al receptor del comprobante:
```
byte[] file_cer_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.cer.pem"));
byte[] file_key_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.key.pem"));
```
En el proyecto existe una clase Relacionados.java, la cual facilita la petición de consulta de documentos relacionados, solo es necesario agregarla al proyecto y crear un objeto.
```
// Parametros generales para el sevicio
String usuario = "usuario";
String contrasena = "contraseña";

// Consultar Documentos Relacionados
// Parametros para el servicio
String rfc_receptor = "AAA010101AAA";
String uuid = "A2872717-52E1-4AC1-877A-A74102AB6EDD";

byte[] file_cer_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.cer.pem"));
byte[] file_key_pem = Files.readAllBytes(Paths.get("src/Certificados/EKU9003173C9.key.pem"));

String content_cer_file = new String(file_cer_pem, "UTF8");
String content_key_file = new String(file_key_pem, "UTF8");

try {
   // Creacion del objeto Documentos
   Relacionados documentos = new Relacionados(usuario, contrasena, uuid, rfc_receptor, content_cer_file, content_key_file);
   // Ejecucion del servicio
   String docs_relacionados = documentos.Consultar();
   // Imprime la respuesta
   System.out.println("Documentos Relacionados: \n");
   System.out.println(docs_relacionados);
} catch (Exception exception) {
   throw exception;
}
```


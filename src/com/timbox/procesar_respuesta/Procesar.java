package com.timbox.procesar_respuesta;

import java.io.ByteArrayOutputStream;
import java.util.List;
import javax.xml.soap.*;

public class Procesar {

    // URL del servicio
    static String URL = "https://staging.ws.timbox.com.mx/cancelacion/action";
    // Accion para procesar respuestas
    final static String ACCION = "procesar_respuesta";
    // Propiedades
    private String usuario = "", contrasena = "", rfcReceptor = "", cer_pem = "", key_pem = "";
    private List<foliosRespuestas> foliosrespuestas;

    public Procesar(String usuarioValue, String contrasenaValue, String rfcReceptorValue, List<foliosRespuestas> foliosValues, String cerFileValue, String keyFileValue) {
        usuario = usuarioValue;
        contrasena = contrasenaValue;
        rfcReceptor = rfcReceptorValue;
        foliosrespuestas = foliosValues;
        cer_pem = cerFileValue;
        key_pem = keyFileValue;
    }

    public String ProcesarRespuestas() throws Exception {
        // Conexion SOAP
        String response = "";
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapResponse = messageFactory.createMessage();
        try {
            // Ejecucion del metodo para procesar respuestas
            soapResponse = soapConnection.call(procesarRespuestas(), URL);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            soapResponse.writeTo(outputStream);
            response = new String(outputStream.toByteArray());

        } catch (Exception ex) {
            throw ex;
        } finally {
            soapConnection.close();
        }

        return response;
    }

    private SOAPMessage procesarRespuestas() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        // Creacion de envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("urn", "urn:WashOut");
        envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");

        // Cuerpo del envelope
        SOAPBody soapBody = envelope.getBody();
        SOAPElement urn = soapBody.addChildElement(ACCION, "urn");

        // Parametros para el usuario
        SOAPElement usernameElement = urn.addChildElement("username");
        usernameElement.addTextNode(usuario);
        usernameElement.setAttribute("xsi:type", "xsd:string");

        // Parametros para la contrasena
        SOAPElement passwordElement = urn.addChildElement("password");
        passwordElement.addTextNode(contrasena);
        passwordElement.setAttribute("xsi:type", "xsd:string");

        // Parametros para el rfc_emisor
        SOAPElement rfcReceptorElement = urn.addChildElement("rfc_receptor");
        rfcReceptorElement.addTextNode(rfcReceptor);
        rfcReceptorElement.setAttribute("xsi:type", "xsd:string");

        // Parametros para respuestas
        SOAPElement respuestasElement = urn.addChildElement("respuestas");
        respuestasElement.setAttribute("xsi:type", "respuestas");

        // Creacion de folio con array list foliorespuestas
        for (foliosRespuestas fr : foliosrespuestas) {

            SOAPElement folioRespuestaElement = respuestasElement.addChildElement("folios_respuestas");
            folioRespuestaElement.setAttribute("xsi:type", "urn:folio");

            SOAPElement uuidElement = folioRespuestaElement.addChildElement("uuid");
            uuidElement.addTextNode(fr.getUuid());
            uuidElement.setAttribute("xsi:type", "xsd:string");

            SOAPElement RfcEmisorElement = folioRespuestaElement.addChildElement("rfc_emisor");
            RfcEmisorElement.addTextNode(fr.getRfc_emisor());
            RfcEmisorElement.setAttribute("xsi:type", "xsd:string");

            SOAPElement totalElement = folioRespuestaElement.addChildElement("total");
            totalElement.addTextNode(fr.getTotal());
            totalElement.setAttribute("xsi:type", "xsd:string");

            SOAPElement respuestsElement = folioRespuestaElement.addChildElement("respuesta");
            respuestsElement.addTextNode(fr.getRespuesta());
            respuestsElement.setAttribute("xsi:type", "xsd:string");

        }

        // Parametros para el cert_pem
        SOAPElement pfxbase64Element = urn.addChildElement("cert_pem");
        pfxbase64Element.addTextNode(cer_pem);
        pfxbase64Element.setAttribute("xsi:type", "xsd:string");

        // Parametros para el llave_pem
        SOAPElement pfxpasswordElement = urn.addChildElement("llave_pem");
        pfxpasswordElement.addTextNode(key_pem);
        pfxpasswordElement.setAttribute("xsi:type", "xsd:string");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", ACCION);

        soapMessage.saveChanges();

        return soapMessage;
    }
}

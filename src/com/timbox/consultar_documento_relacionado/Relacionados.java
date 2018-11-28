package com.timbox.consultar_documento_relacionado;

import java.io.ByteArrayOutputStream;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

public class Relacionados {

    // URL del servicio
    static String URL = "https://staging.ws.timbox.com.mx/cancelacion/action";
    // Accion para consultar documentos relacionados
    final static String ACCION = "consultar_documento_relacionado";
    // Propiedades
    private String usuario = "", contrasena = "", rfcReceptor = "", uuid = "",cer_pem = "", key_pem = "";

    public Relacionados(String usuarioValue, String contrasenaValue, String uuidValue, String rfcReceptorValue, String cerFileValue, String keyFileValue) {
        usuario = usuarioValue;
        contrasena = contrasenaValue;
        rfcReceptor = rfcReceptorValue;
        uuid = uuidValue;
        cer_pem = cerFileValue;
        key_pem = keyFileValue;
    }

    public String Consultar() throws Exception {

        // Conexion SOAP
        String response = "";
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapResponse = messageFactory.createMessage();

        try {
            // Ejecucion del metodo para consultar_documento_relacionado
            soapResponse = soapConnection.call(peticionConsultarRelacionados(), URL);

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

    private SOAPMessage peticionConsultarRelacionados() throws Exception {
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

        // Parametros para el uuid
        SOAPElement uuidElement = urn.addChildElement("uuid");
        uuidElement.addTextNode(uuid);
        uuidElement.setAttribute("xsi:type", "xsd:string");
        
        // Parametros para el rfc_receptor
        SOAPElement rfcEmisorElement = urn.addChildElement("rfc_receptor");
        rfcEmisorElement.addTextNode(rfcReceptor);
        rfcEmisorElement.setAttribute("xsi:type", "xsd:string");

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

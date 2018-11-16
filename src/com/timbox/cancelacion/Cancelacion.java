package com.timbox.cancelacion;

import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class Cancelacion {

    // URL del servicio
    static String URL = "https://staging.ws.timbox.com.mx/cancelacion/action";
    // Accion para cancelar cfdi
    final static String ACCION = "cancelar_cfdi";
    // Propiedades
    private String usuario = "", contrasena = "", rfcEmisor = "", cer_pem = "", key_pem = "";
    private List<folio> folios;

    public Cancelacion(String usuarioValue, String contrasenaValue, String rfcEmisorValue, List<folio> foliosValue, String cerFileValue, String keyFileValue) {
        usuario = usuarioValue;
        contrasena = contrasenaValue;
        rfcEmisor = rfcEmisorValue;
        folios = foliosValue;
        cer_pem = cerFileValue;
        key_pem = keyFileValue;
    }

    public String Cancelar() throws Exception {

        // Conexion SOAP
        String response = "";
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapResponse = messageFactory.createMessage();

        try {
            // Ejecucion del metodo para cancelar_cfdi
            soapResponse = soapConnection.call(peticionCancelacion(), URL);

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

    private SOAPMessage peticionCancelacion() throws Exception {
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
        SOAPElement rfcEmisorElement = urn.addChildElement("rfc_emisor");
        rfcEmisorElement.addTextNode(rfcEmisor);
        rfcEmisorElement.setAttribute("xsi:type", "xsd:string");

        // Parametros para folios
        SOAPElement foliosElement = urn.addChildElement("folios");
        foliosElement.setAttribute("xsi:type", "urn:folios");

        // Creacion de folio con array list folio
        for (folio f : this.folios) {

            SOAPElement folioElement = foliosElement.addChildElement("folio");
            folioElement.setAttribute("xsi:type", "urn:folio");

            SOAPElement uuidElement = folioElement.addChildElement("uuid");
            uuidElement.addTextNode(f.getUuid());
            uuidElement.setAttribute("xsi:type", "xsd:string");

            SOAPElement RfcReceptorElement = folioElement.addChildElement("rfc_receptor");
            RfcReceptorElement.addTextNode(f.getRfc_receptor());
            RfcReceptorElement.setAttribute("xsi:type", "xsd:string");

            SOAPElement totalElement = folioElement.addChildElement("total");
            totalElement.addTextNode(f.getTotal());
            totalElement.setAttribute("xsi:type", "xsd:string");
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

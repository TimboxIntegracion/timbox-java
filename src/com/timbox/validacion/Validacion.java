package com.timbox.validacion;


import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class Validacion {

    // URL del servicio
    static String URL = "https://staging.ws.timbox.com.mx/valida_cfdi/action";
    // Accion para validar_cfdi
    final static String ACCION = "validar_cfdi";
    // Propiedades
    private String usuario = "AAA010101000", contrasena = "h6584D56fVdBbSmmnB";
    private List<Comprobante> lista_comprobantes;

    public Validacion(String usuarioValue, String contrasenaValue, List<Comprobante> comprobantes) {
        usuario = usuarioValue;
        contrasena = contrasenaValue;
        lista_comprobantes = comprobantes;
    }

    public String Validar() throws Exception {

        // Conexion SOAP
        String response = "";
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapResponse = messageFactory.createMessage();

        try {
            // Ejecucion del metodo para cancelar_cfdi
            soapResponse = soapConnection.call(peticionValidacion(), URL);

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

    private SOAPMessage peticionValidacion() throws Exception {
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

        // Parametros para validar
        SOAPElement ValidarElement = urn.addChildElement("validar");
        ValidarElement.setAttribute("xsi:type", "urn:xml_string");
        
        // Creacion de xml con array list xmls
        for (Comprobante f : this.lista_comprobantes) {

            SOAPElement comprobanteElement = ValidarElement.addChildElement("Comprobante");
            comprobanteElement.setAttribute("xsi:type", "urn:Comprobante");

            SOAPElement xmlElement = comprobanteElement.addChildElement("sxml");
            xmlElement.addTextNode(f.getSxml());
            xmlElement.setAttribute("xsi:type", "xsd:string");
            
            SOAPElement externalElement = comprobanteElement.addChildElement("external_id");
            externalElement.addTextNode(f.getExternal());
            externalElement.setAttribute("xsi:type", "xsd:string");
            
        }

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", ACCION);

        soapMessage.saveChanges();

        return soapMessage;
    }
}

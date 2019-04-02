package com.timbox.validacion;


public class Comprobante {

    public String sxml;
    public String external_id;
    public Comprobante(String xml_encode, String external) {
        sxml = xml_encode;
        external_id = external;
    }

    public String getSxml() {
        return sxml;
    }
     
    public String getExternal() {
        return external_id;
    }

}

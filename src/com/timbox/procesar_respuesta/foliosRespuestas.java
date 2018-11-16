package com.timbox.procesar_respuesta;

public class foliosRespuestas {

    public String uuid, rfc_emisor, total, respuesta;

    public foliosRespuestas(String uuidValue, String rfc_emisorValue, String totalValue, String respuestaValue) {
        uuid = uuidValue;
        rfc_emisor = rfc_emisorValue;
        total = totalValue;
        respuesta = respuestaValue;
    }

    public String getUuid() {
        return uuid;
    }

    public String getRfc_emisor() {
        return rfc_emisor;
    }

    public String getTotal() {
        return total;
    }

    public String getRespuesta() {
        return respuesta;
    }

}

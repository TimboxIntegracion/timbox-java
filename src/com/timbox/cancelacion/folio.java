package com.timbox.cancelacion;

public class folio {

    public String uuid, rfc_receptor, total;

    public folio(String uuidValue, String rfc_receptorValue, String totalValue) {
        uuid = uuidValue;
        rfc_receptor = rfc_receptorValue;
        total = totalValue;
    }

    public String getUuid() {
        return uuid;
    }

    public String getRfc_receptor() {
        return rfc_receptor;
    }

    public String getTotal() {
        return total;
    }

}

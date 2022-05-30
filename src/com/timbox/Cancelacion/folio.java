/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cancelacion;

/**
 *
 * @author timbox-07
 */
public class folio {
    
    public String uuid, rfc_receptor, total, motivo, folio_sustituto;

    public folio(String uuidValue, String rfc_receptorValue, String totalValue, String motivoValue, String folio_sustitutoValue) {
        uuid = uuidValue;
        rfc_receptor = rfc_receptorValue;
        total = totalValue;
        motivo = motivoValue;
        folio_sustituto = folio_sustitutoValue;
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
    
    public String getMotivo() {
        return motivo;
    }
    
    public String getFolio_sustituto() {
        return folio_sustituto;
    }
}

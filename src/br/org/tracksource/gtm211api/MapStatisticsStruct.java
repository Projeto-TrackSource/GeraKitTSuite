/*
 * MapStatisticsStruct.java
 *
 * Created on September 5, 2012, 13:47 PM
 *
 */
package br.org.tracksource.gtm211api;

import br.org.tracksource.gtm211api.domain.Trknome1;

/**
 * This class defines a data structure to hold some statiscs informations
 * regarding a Map
 */
public class MapStatisticsStruct {
    
    private int qtdTrknome1Routable;        // Total number of rountable tracks 
    private double totalMetersRoutable;     // Total meters of tracks routable cover by the entire Map
    private double totalMetersNonRoutable;  // Total meters of tracks non routable cover by the entire Map
    private double totalMetersRPA;          // Total meters of tracks "Rodovia Principal Azul"
    private double totalMetersRPV;          // Total meters of tracks "Rodovia Principal Vermelha"
    private double totalMetersAPP;          // Total meters of tracks "Avenida Pavimentada Principal"
    private double totalMetersPDP;          // Total meters of tracks "Pista Dupla Pavimentada"
    private double totalMetersRPP;          // Total meters of tracks "Rua Pavimentada Principal"
    private double totalMetersVA;           // Total meters of tracks "Via de Acesso"
    private double totalMetersETP;          // Total meters of tracks "Estrada de Terra Principal"
    private double totalMetersRPS;          // Total meters of tracks "Rua Pavimentada Secundaria"
    private double totalMetersET;           // Total meters of tracks "Estrada de Terra"
    private double totalMetersT4;           // Total meters of tracks "Trilha 4x4"
    private double totalMetersTM;           // Total meters of tracks "Trilha de Moto"
    private double totalMetersTCB;          // Total meters of tracks "Trilha Caminhada/Bicicleta"
    private double totalMetersLM;           // Total meters of tracks "Linha Maritima"
    

    // Class MapStatisticsStruct constructor
    public MapStatisticsStruct() {
        qtdTrknome1Routable = 0;
        totalMetersRoutable = 0.0d;
        totalMetersNonRoutable = 0.0d;
        totalMetersRPA = 0.0d;
        totalMetersRPV = 0.0d;
        totalMetersAPP = 0.0d;
        totalMetersPDP = 0.0d;
        totalMetersRPP = 0.0d;
        totalMetersVA = 0.0d;
        totalMetersETP = 0.0d;
        totalMetersRPS = 0.0d;
        totalMetersET = 0.0d;
        totalMetersT4 = 0.0d;
        totalMetersTM = 0.0d;
        totalMetersTCB = 0.0d;
        totalMetersLM = 0.0d;
    }
    
    /**
     * Analyse a track and add its statistic in the structure fields
     * @param track : Track to be analysed
     */
    public void add (Trknome1 track) {
        double tsize;
        if (track.isTypeRodoviaPrincipalAzul()) { 
            tsize = track.getTsize();
            this.totalMetersRPA += tsize;
            this.totalMetersRoutable += tsize;
            ++this.qtdTrknome1Routable;
        } 
        else if (track.isTypeRodoviaPrincipalVermelha()) {
            tsize = track.getTsize();
            this.totalMetersRPV += tsize;
            this.totalMetersRoutable += tsize;
            ++this.qtdTrknome1Routable;
        }
        else if (track.isTypeAvenidaPavimentadaPrincipal ()) {
            tsize = track.getTsize();
            this.totalMetersAPP += tsize;
            this.totalMetersRoutable += tsize;
            ++this.qtdTrknome1Routable;
        }
        else if (track.isTypePistaDuplaPavimentada()) {
            tsize = track.getTsize();
            this.totalMetersPDP += tsize;
            this.totalMetersRoutable += tsize;
            ++this.qtdTrknome1Routable;
        }
        else if (track.isTypeRuaPavimentadaPrincipal()) {
            tsize = track.getTsize();
            this.totalMetersRPP += tsize;
            this.totalMetersRoutable += tsize;
            ++this.qtdTrknome1Routable;
        }
        else if (track.isTypeViaAcesso()) {
            tsize = track.getTsize();
            this.totalMetersVA += tsize;
            this.totalMetersRoutable += tsize;
            ++this.qtdTrknome1Routable;
        }
        else if (track.isTypeEstradaTerraPrincipal()) {
            tsize = track.getTsize();
            this.totalMetersETP += tsize;
            this.totalMetersRoutable += tsize;
            ++this.qtdTrknome1Routable;
        }
        else if (track.isTypeRuaPavimentadaSecundaria()) {
            tsize = track.getTsize();
            this.totalMetersRPS += tsize;
            this.totalMetersRoutable += tsize;
            ++this.qtdTrknome1Routable;
        }
        else if (track.isTypeEstradaTerra()) {
            tsize = track.getTsize();
            this.totalMetersET += tsize;
            this.totalMetersRoutable += tsize;
            ++this.qtdTrknome1Routable;
        }
        else if (track.isTypeTrilha4x4()) {
            tsize = track.getTsize();
            this.totalMetersT4 += tsize;
            this.totalMetersRoutable += tsize;
            ++this.qtdTrknome1Routable;
        }
        else if (track.isTypeTrilhaCaminhadaBicicleta()) {
            tsize = track.getTsize();
            this.totalMetersTCB += tsize;
            this.totalMetersRoutable += tsize;
            ++this.qtdTrknome1Routable;
        }
        else if (track.isTypeTrilhaMoto()) {
            tsize = track.getTsize();
            this.totalMetersTM += tsize;
            this.totalMetersRoutable += tsize;
            ++this.qtdTrknome1Routable;
        }
        else if (track.isTypeLinhaMaritma()){
            tsize = track.getTsize();
            this.totalMetersLM += tsize;
            this.totalMetersRoutable += tsize;
            ++this.qtdTrknome1Routable;
        }
        else {
            this.totalMetersNonRoutable += track.getTsize();
        }
    }
    
    /**
     * Analyse a track and remove its statistic of the structure fields
     * @param track : Track to be analysed
     */
    public void remove (Trknome1 track) {
        double tsize;
        if (track.isTypeRodoviaPrincipalAzul()) { 
            tsize = track.getTsize();
            this.totalMetersRPA -= tsize;
            this.totalMetersRoutable -= tsize;
            --this.qtdTrknome1Routable;
        } 
        else if (track.isTypeRodoviaPrincipalVermelha()) {
            tsize = track.getTsize();
            this.totalMetersRPV -= tsize;
            this.totalMetersRoutable -= tsize;
            --this.qtdTrknome1Routable;
        }
        else if (track.isTypeAvenidaPavimentadaPrincipal ()) {
            tsize = track.getTsize();
            this.totalMetersAPP -= tsize;
            this.totalMetersRoutable -= tsize;
            --this.qtdTrknome1Routable;
        }
        else if (track.isTypePistaDuplaPavimentada()) {
            tsize = track.getTsize();
            this.totalMetersPDP -= tsize;
            this.totalMetersRoutable -= tsize;
            --this.qtdTrknome1Routable;
        }
        else if (track.isTypeRuaPavimentadaPrincipal()) {
            tsize = track.getTsize();
            this.totalMetersRPP -= tsize;
            this.totalMetersRoutable -= tsize;
            --this.qtdTrknome1Routable;
        }
        else if (track.isTypeViaAcesso()) {
            tsize = track.getTsize();
            this.totalMetersVA -= tsize;
            this.totalMetersRoutable -= tsize;
            --this.qtdTrknome1Routable;
        }
        else if (track.isTypeEstradaTerraPrincipal()) {
            tsize = track.getTsize();
            this.totalMetersETP -= tsize;
            this.totalMetersRoutable -= tsize;
            --this.qtdTrknome1Routable;
        }
        else if (track.isTypeRuaPavimentadaSecundaria()) {
            tsize = track.getTsize();
            this.totalMetersRPS -= tsize;
            this.totalMetersRoutable -= tsize;
            --this.qtdTrknome1Routable;
        }
        else if (track.isTypeEstradaTerra()) {
            tsize = track.getTsize();
            this.totalMetersET -= tsize;
            this.totalMetersRoutable -= tsize;
            --this.qtdTrknome1Routable;
        }
        else if (track.isTypeTrilha4x4()) {
            tsize = track.getTsize();
            this.totalMetersT4 -= tsize;
            this.totalMetersRoutable -= tsize;
            --this.qtdTrknome1Routable;
        }
        else if (track.isTypeTrilhaCaminhadaBicicleta()) {
            tsize = track.getTsize();
            this.totalMetersTCB -= tsize;
            this.totalMetersRoutable -= tsize;
            --this.qtdTrknome1Routable;
        }
        else if (track.isTypeTrilhaMoto()) {
            tsize = track.getTsize();
            this.totalMetersTM -= tsize;
            this.totalMetersRoutable -= tsize;
            --this.qtdTrknome1Routable;
        }
        else if (track.isTypeLinhaMaritma()){
            tsize = track.getTsize();
            this.totalMetersLM -= tsize;
            this.totalMetersRoutable -= tsize;
            --this.qtdTrknome1Routable;
        }
        else {
            this.totalMetersNonRoutable -= track.getTsize();
        }
    }
    
    /** Returns Total number of rountable tracks */ 
    public int getQuantityOfRoutableTracks() {
        return this.qtdTrknome1Routable;
    }
    
    /** Returns total kilometers of tracks routable cover by the entire Map */
    public double getRoutableKM() {
        return this.totalMetersRoutable / 1000;
    }
    
    /** Returns total kilometers of tracks non routable cover by the entire Map */
    public double getNonRoutableKM() {
        return this.totalMetersNonRoutable / 1000;
    }
    
    /** Returns total kilometers of tracks "Rodovia Principal Azul" */
    public double getRodoviaPrincipalAzulKM() {
        return this.totalMetersRPA / 1000;
    }
    
    /** Returns total kilometers of tracks "Rodovia Principal Vermelha" */
    public double getRodoviaPrincipalVermelhaKM() {
        return this.totalMetersRPV / 1000;
    }
    
    /** Returns total kilometers of tracks "Avenida Pavimentada Principal" */
    public double getAvenidaPavimentadaPrincipalKM() {
        return this.totalMetersAPP / 1000;
    }
    
    /** Returns total kilometers of tracks "Pista Dupla Pavimentada" */
    public double getPistaDuplaPavimentadaKM() {
        return this.totalMetersPDP / 1000;
    }
    
    /** Returns total kilometers of tracks "Rua Pavimentada Principal" */
    public double getRuaPavimentadaPrincipalKM() {
        return this.totalMetersRPP / 1000;
    }
    
    /** Returns total kilometers of tracks "Via de Acesso" */
    public double getViaAcessoKM() {
        return this.totalMetersVA / 1000;
    }
    
    /** Returns total kilometers of tracks "Estrada de Terra Principal" */
    public double getEstradaTerraPrincipalKM() {
        return this.totalMetersETP / 1000;
    }
    
    /** Returns total kilometers of tracks "Rua Pavimentada Secundaria" */
    public double getRuaPavimentadaSecundariaKM() {
        return this.totalMetersRPS / 1000;
    }
    
    /** Returns total kilometers of tracks "Estrada de Terra" */
    public double getEstradaTerraKM() {
        return this.totalMetersET / 1000;
    }
    
    /** Returns total kilometers of tracks "Trilha 4x4" */
    public double getTrilha4x4KM() {
        return this.totalMetersT4 / 1000;
    }
    
    /** Returns total kilometers of tracks "Trilha de Moto" */
    public double getTrilhaMotoKM() {
        return this.totalMetersTM / 1000;
    }
    
    /** Returns total kilometers of tracks "Trilha Caminhada/Bicicleta" */
    public double getTrilhaCaminhadaBicicletaKM() {
        return this.totalMetersTCB / 1000;
    }
    
    /** Returns total kilometers of tracks "Linha Maritima" */
    public double getLinhaMaritimaKM() {
        return this.totalMetersLM / 1000;
    }
    
}

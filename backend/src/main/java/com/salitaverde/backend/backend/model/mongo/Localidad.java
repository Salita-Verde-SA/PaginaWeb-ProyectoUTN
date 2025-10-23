package com.salitaverde.backend.backend.model.mongo;

public enum Localidad {
    CAPITAL("Capital", "Ciudad de Mendoza"),
    GUAYMALLEN("Guaymallén", "Guaymallén"),
    GODOY_CRUZ("Godoy Cruz", "Godoy Cruz"),
    MAIPU("Maipú", "Maipú"),
    LAS_HERAS("Las Heras", "Las Heras"),
    LUJAN_DE_CUYO("Luján de Cuyo", "Luján"),
    SAN_MARTIN("San Martín", "Gral. San Martín"),
    JUNIN("Junín", "Junín"),
    RIVADAVIA("Rivadavia", "Rivadavia"),
    LA_PAZ("La Paz", "La Paz"),
    SANTA_ROSA("Santa Rosa", "Santa Rosa"),
    TUNUYAN("Tunuyán", "Tunuyán"),
    TUPUNGATO("Tupungato", "Tupungato"),
    SAN_CARLOS("San Carlos", "San Carlos"),
    SAN_RAFAEL("San Rafael", "San Rafael"),
    GENERAL_ALVEAR("General Alvear", "Alvear"),
    MALARGUE("Malargüe", "Malargüe");

    private final String nombre;
    private final String alias;

    Localidad(String nombre, String alias) {
        this.nombre = nombre;
        this.alias = alias;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAlias() {
        return alias;
    }
}


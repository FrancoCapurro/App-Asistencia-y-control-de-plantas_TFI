package Entidad;

public class Planta {

    private int idPlanta;
    private String nombrePropio;
    private DatosPlanta datos;
    private boolean estado; // true = activa
    private boolean interior; // true = interior, false = exterior
    private String fecha;
    private String observaciones;

    public Planta() {
        this.datos = new DatosPlanta();
    }

    public Planta(String nombre, boolean interior) {
        this.nombrePropio = nombre;
        this.interior = interior;
    }

    // Getters y setters
    public int getIdPlanta() { return idPlanta; }
    public void setIdPlanta(int idPlanta) { this.idPlanta = idPlanta; }

    public String getNombrePropio() { return nombrePropio; }
    public void setNombrePropio(String nombrePropio) { this.nombrePropio = nombrePropio; }

    public DatosPlanta getDatos() { return datos; }
    public void setDatos(DatosPlanta datos) { this.datos = datos; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public boolean isInterior() { return interior; }
    public void setInterior(boolean interior) { this.interior = interior; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
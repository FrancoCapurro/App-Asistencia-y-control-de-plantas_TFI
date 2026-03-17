package Entidad;

public class DatosPlanta {

    private int idDato;
    private String nombreComun;
    private String nombreCientifico;
    private int temperaturaMin;
    private int temperaturaMax;
    private int humedad;
    private int cantidadAgua;
    private int frecuenciaRiego;
    private String tipoRiego;
    private boolean estado; // true = descargado desde API
    private ImagenPlanta imagen;

    public DatosPlanta() {
        this.imagen = new ImagenPlanta();
    }

    // Getters y setters
    public int getIdDato() { return idDato; }
    public void setIdDato(int idDato) { this.idDato = idDato; }

    public String getNombreComun() { return nombreComun; }
    public void setNombreComun(String nombreComun) { this.nombreComun = nombreComun; }

    public String getNombreCientifico() { return nombreCientifico; }
    public void setNombreCientifico(String nombreCientifico) { this.nombreCientifico = nombreCientifico; }

    public int getTemperaturaMin() { return temperaturaMin; }
    public void setTemperaturaMin(int temperaturaMin) { this.temperaturaMin = temperaturaMin; }

    public int getTemperaturaMax() { return temperaturaMax; }
    public void setTemperaturaMax(int temperaturaMax) { this.temperaturaMax = temperaturaMax; }

    public int getHumedad() { return humedad; }
    public void setHumedad(int humedad) { this.humedad = humedad; }

    public int getCantidadAgua() { return cantidadAgua; }
    public void setCantidadAgua(int cantidadAgua) { this.cantidadAgua = cantidadAgua; }

    public int getFrecuenciaRiego() { return frecuenciaRiego; }
    public void setFrecuenciaRiego(int frecuenciaRiego) { this.frecuenciaRiego = frecuenciaRiego; }

    public String getTipoRiego() { return tipoRiego; }
    public void setTipoRiego(String tipoRiego) { this.tipoRiego = tipoRiego; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public ImagenPlanta getImagen() { return imagen; }
    public void setImagen(ImagenPlanta imagen) { this.imagen = imagen; }
}
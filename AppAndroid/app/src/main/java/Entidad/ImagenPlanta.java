package Entidad;

public class ImagenPlanta {

    private String url;
    private String rutaLocal;
    private boolean descargada;

    public ImagenPlanta() {}

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getRutaLocal() { return rutaLocal; }
    public void setRutaLocal(String rutaLocal) { this.rutaLocal = rutaLocal; }

    public boolean isDescargada() { return descargada; }
    public void setDescargada(boolean descargada) { this.descargada = descargada; }
}
package cl.duoc.maquinaria.model;

/**
 * Excavadora que arrienda la empresa. Participa en el programa de certificación de seguridad.
 * Costo del servicio: base $150.000; si no cuenta con mantención al día, recargo del 25%.
 */
public class Excavadora extends Maquina implements Certificable {

    private static final double COSTO_BASE = 150000;
    private static final double RECARGO_MANTENCION_PENDIENTE = 0.25;

    private double pesoToneladas;
    private boolean mantencionAlDia;
    private boolean certificacionSeguridad;

    public Excavadora() {
    }

    public Excavadora(String codigo, int horasUso, double potencia, double pesoToneladas, boolean mantencionAlDia) {
        super(codigo, horasUso, potencia);
        setPesoToneladas(pesoToneladas);
        setMantencionAlDia(mantencionAlDia);
        this.certificacionSeguridad = false;
    }

    public Excavadora(int id, String codigo, int horasUso, double potencia, double pesoToneladas,
                      boolean mantencionAlDia, boolean certificacionSeguridad) {
        this(codigo, horasUso, potencia, pesoToneladas, mantencionAlDia);
        setId(id);
        this.certificacionSeguridad = certificacionSeguridad;
    }

    public double getPesoToneladas() {
        return pesoToneladas;
    }

    public void setPesoToneladas(double pesoToneladas) {
        if (!Double.isFinite(pesoToneladas) || pesoToneladas <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero.");
        }
        this.pesoToneladas = pesoToneladas;
    }

    public boolean isMantencionAlDia() {
        return mantencionAlDia;
    }

    public void setMantencionAlDia(boolean mantencionAlDia) {
        this.mantencionAlDia = mantencionAlDia;
    }

    @Override
    public boolean tieneCertificacionSeguridad() {
        return certificacionSeguridad;
    }

    @Override
    public void certificarSeguridad() {
        this.certificacionSeguridad = true;
    }

    public void setCertificacionSeguridad(boolean certificacionSeguridad) {
        this.certificacionSeguridad = certificacionSeguridad;
    }

    @Override
    public double calcularCostoServicio() {
        if (mantencionAlDia) {
            return COSTO_BASE;
        }
        return COSTO_BASE + COSTO_BASE * RECARGO_MANTENCION_PENDIENTE;
    }

    @Override
    public String getTipo() {
        return "Excavadora";
    }

    @Override
    public String getDetalle() {
        String estadoMantencion = mantencionAlDia ? "mantención al día" : "sin mantención";
        String estadoCertificacion = certificacionSeguridad ? "certificada" : "sin cert.";
        return pesoToneladas + " ton (" + estadoMantencion + ", " + estadoCertificacion + ")";
    }
}

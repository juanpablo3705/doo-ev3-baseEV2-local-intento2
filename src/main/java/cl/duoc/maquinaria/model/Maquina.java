package cl.duoc.maquinaria.model;

/**
 * Representa a cualquier máquina que arrienda la empresa.
 * Define los datos comunes y delega en cada tipo de maquinaria el cálculo del
 * costo del servicio y su detalle.
 */
public abstract class Maquina {

    private int id;
    private String codigo;
    private int horasUso;
    private double potencia;

    protected Maquina() {
    }

    public Maquina(String codigo, int horasUso, double potencia) {
        setCodigo(codigo);
        setHorasUso(horasUso);
        setPotencia(potencia);
    }

    public Maquina(int id, String codigo, int horasUso, double potencia) {
        this(codigo, horasUso, potencia);
        setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("El ID no puede ser negativo.");
        }
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        this.codigo = codigo.trim();
    }

    public int getHorasUso() {
        return horasUso;
    }

    public void setHorasUso(int horasUso) {
        if (horasUso < 0 || horasUso > 20000) {
            throw new IllegalArgumentException("Las horas de uso deben estar entre 0 y 20000.");
        }
        this.horasUso = horasUso;
    }

    public double getPotencia() {
        return potencia;
    }

    public void setPotencia(double potencia) {
        if (!Double.isFinite(potencia) || potencia <= 0) {
            throw new IllegalArgumentException("La potencia debe ser mayor que cero.");
        }
        this.potencia = potencia;
    }

    public abstract double calcularCostoServicio();

    public double getCostoServicio() {
        return calcularCostoServicio();
    }

    public abstract String getTipo();

    public abstract String getDetalle();

    @Override
    public String toString() {
        return "ID: " + id + " | Código: " + codigo + " | Horas de uso: " + horasUso;
    }
}

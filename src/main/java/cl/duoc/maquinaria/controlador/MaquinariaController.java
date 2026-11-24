package cl.duoc.maquinaria.controlador;

import cl.duoc.maquinaria.model.CargadorFrontal;
import cl.duoc.maquinaria.model.Excavadora;
import cl.duoc.maquinaria.model.Maquina;
import cl.duoc.maquinaria.repositorio.CargadorFrontalRepository;
import cl.duoc.maquinaria.repositorio.ExcavadoraRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLIntegrityConstraintViolationException;

public class MaquinariaController {

    @FXML private ComboBox<String> cmbTipo;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtHorasUso;
    @FXML private TextField txtPotencia;
    @FXML private Label lblSeccionEspecifica;
    @FXML private Label lblDetalle;
    @FXML private TextField txtDetalle;
    @FXML private CheckBox chkMantencion;
    @FXML private CheckBox chkCertificacion;
    @FXML private TableView<Maquina> tablaMaquinas;
    @FXML private TableColumn<Maquina, Number> columnaId;
    @FXML private TableColumn<Maquina, String> columnaTipo;
    @FXML private TableColumn<Maquina, String> columnaCodigo;
    @FXML private TableColumn<Maquina, Number> columnaHorasUso;
    @FXML private TableColumn<Maquina, Number> columnaPotencia;
    @FXML private TableColumn<Maquina, String> columnaDetalle;
    @FXML private TableColumn<Maquina, Number> columnaCosto;
    @FXML private TextField txtBuscar;
    @FXML private Label lblMensaje;
    @FXML private Button btnRegistrar;
    @FXML private Button btnCancelarEdicion;
    @FXML private Button btnEditar;

    private final ExcavadoraRepository excavadoraRepository = new ExcavadoraRepository();
    private final CargadorFrontalRepository cargadorRepository = new CargadorFrontalRepository();
    private final ObservableList<Maquina> maquinas = FXCollections.observableArrayList();
    private Maquina maquinaEnEdicion;

    @FXML
    private void initialize() {
        cmbTipo.getItems().addAll("Excavadora", "Cargador Frontal");
        cmbTipo.setValue("Excavadora");
        tablaMaquinas.setItems(maquinas);
        actualizarCampos();
        cargarMaquinas();
        btnCancelarEdicion.setDisable(true);
    }

    private void cargarMaquinas() {
        try {
            maquinas.clear();
            maquinas.addAll(excavadoraRepository.obtenerTodos());
            maquinas.addAll(cargadorRepository.obtenerTodos());
            mostrarMensaje("Máquinas cargadas desde MySQL.", true);
        } catch (Exception ex) {
            mostrarMensaje("No se pudieron cargar las máquinas: " + ex.getMessage(), false);
        }
    }

    @FXML
    private void cambiarTipo() {
        actualizarCampos();
    }

    private void actualizarCampos() {
        boolean esExcavadora = "Excavadora".equals(cmbTipo.getValue());
        lblSeccionEspecifica.setText(esExcavadora ? "Datos de la excavadora" : "Datos del cargador frontal");
        lblDetalle.setText(esExcavadora ? "Peso (ton):" : "Capacidad balde (m³):");
        chkMantencion.setVisible(esExcavadora);
        chkMantencion.setManaged(esExcavadora);
        chkCertificacion.setVisible(esExcavadora);
        chkCertificacion.setManaged(esExcavadora);
    }

    @FXML
    private void registrar() {
        try {
            String codigo = txtCodigo.getText();
            int horasUso = Integer.parseInt(txtHorasUso.getText().trim());
            double potencia = Double.parseDouble(txtPotencia.getText().trim().replace(',', '.'));
            double datoEspecifico = Double.parseDouble(txtDetalle.getText().trim().replace(',', '.'));
            String mensajeExito;

            if (maquinaEnEdicion == null) {
                if ("Excavadora".equals(cmbTipo.getValue())) {
                    Excavadora excavadora = new Excavadora(
                            codigo, horasUso, potencia, datoEspecifico, chkMantencion.isSelected());
                    if (chkCertificacion.isSelected()) {
                        excavadora.certificarSeguridad();
                    }
                    excavadoraRepository.agregar(excavadora);
                } else {
                    CargadorFrontal cargador = new CargadorFrontal(
                            codigo, horasUso, potencia, datoEspecifico);
                    cargadorRepository.agregar(cargador);
                }
                mensajeExito = "Máquina registrada correctamente.";
            } else {
                maquinaEnEdicion.setCodigo(codigo);
                maquinaEnEdicion.setHorasUso(horasUso);
                maquinaEnEdicion.setPotencia(potencia);

                if (maquinaEnEdicion instanceof Excavadora) {
                    Excavadora excavadora = (Excavadora) maquinaEnEdicion;
                    excavadora.setPesoToneladas(datoEspecifico);
                    excavadora.setMantencionAlDia(chkMantencion.isSelected());
                    excavadora.setCertificacionSeguridad(chkCertificacion.isSelected());
                    excavadoraRepository.actualizar(excavadora);
                } else {
                    CargadorFrontal cargador = (CargadorFrontal) maquinaEnEdicion;
                    cargador.setCapacidadBalde(datoEspecifico);
                    cargadorRepository.actualizar(cargador);
                }
                mensajeExito = "Máquina actualizada correctamente.";
            }

            limpiarFormulario();
            cargarMaquinas();
            mostrarMensaje(mensajeExito, true);

        } catch (NumberFormatException ex) {
            mostrarMensaje("Horas de uso debe ser entero; potencia, peso y capacidad deben ser numéricos.", false);
        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), false);
        } catch (SQLIntegrityConstraintViolationException ex) {
            mostrarMensaje("Ya existe una máquina registrada con ese código.", false);
        } catch (Exception ex) {
            mostrarMensaje("No se pudo guardar la máquina: " + ex.getMessage(), false);
        }
    }

    @FXML
    private void editarSeleccionado() {
        Maquina seleccionada = tablaMaquinas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Seleccione una máquina para editar.", false);
            return;
        }

        maquinaEnEdicion = seleccionada;
        cmbTipo.setValue(seleccionada.getTipo());
        cmbTipo.setDisable(true);
        txtCodigo.setText(seleccionada.getCodigo());
        txtHorasUso.setText(String.valueOf(seleccionada.getHorasUso()));
        txtPotencia.setText(String.valueOf(seleccionada.getPotencia()));

        if (seleccionada instanceof Excavadora) {
            Excavadora excavadora = (Excavadora) seleccionada;
            txtDetalle.setText(String.valueOf(excavadora.getPesoToneladas()));
            chkMantencion.setSelected(excavadora.isMantencionAlDia());
            chkCertificacion.setSelected(excavadora.tieneCertificacionSeguridad());
        } else {
            CargadorFrontal cargador = (CargadorFrontal) seleccionada;
            txtDetalle.setText(String.valueOf(cargador.getCapacidadBalde()));
            chkMantencion.setSelected(false);
            chkCertificacion.setSelected(false);
        }

        actualizarCampos();
        btnRegistrar.setText("Guardar cambios");
        btnCancelarEdicion.setDisable(false);
        mostrarMensaje("Editando: " + seleccionada.getCodigo(), true);
    }

    @FXML
    private void cancelarEdicion() {
        limpiarFormulario();
        cargarMaquinas();
        mostrarMensaje("Edición cancelada.", true);
    }

    @FXML
    private void eliminarSeleccionado() {
        Maquina seleccionada = tablaMaquinas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Seleccione una máquina para eliminar.", false);
            return;
        }

        try {
            if (seleccionada instanceof Excavadora) {
                excavadoraRepository.eliminar(seleccionada.getId());
            } else {
                cargadorRepository.eliminar(seleccionada.getId());
            }
            maquinas.remove(seleccionada);
            if (maquinaEnEdicion == seleccionada) {
                limpiarFormulario();
            }
            mostrarMensaje("Máquina eliminada correctamente.", true);
        } catch (Exception ex) {
            mostrarMensaje("No se pudo eliminar: " + ex.getMessage(), false);
        }
    }

    @FXML
    private void buscar() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        cargarMaquinas();
        if (!texto.isEmpty()) {
            maquinas.removeIf(maquina -> !maquina.getCodigo().toLowerCase().contains(texto));
        }
        if (maquinas.isEmpty()) {
            mostrarMensaje("No se encontraron máquinas.", false);
        }
    }

    @FXML
    private void mostrarTodos() {
        txtBuscar.clear();
        cargarMaquinas();
    }

    private void limpiarFormulario() {
        maquinaEnEdicion = null;
        cmbTipo.setDisable(false);
        cmbTipo.setValue("Excavadora");
        txtCodigo.clear();
        txtHorasUso.clear();
        txtPotencia.clear();
        txtDetalle.clear();
        chkMantencion.setSelected(false);
        chkCertificacion.setSelected(false);
        btnRegistrar.setText("Registrar");
        btnCancelarEdicion.setDisable(true);
        actualizarCampos();
    }

    private void mostrarMensaje(String texto, boolean correcto) {
        lblMensaje.setText(texto);
        lblMensaje.setStyle(correcto
                ? "-fx-padding: 9; -fx-background-color: #edf6ec; -fx-text-fill: #247a3b; -fx-font-weight: bold;"
                : "-fx-padding: 9; -fx-background-color: #fce8e6; -fx-text-fill: #b3261e; -fx-font-weight: bold;");
    }
}

package cl.duoc.maquinaria.repositorio;

import cl.duoc.maquinaria.conexion.ConexionSQL;
import cl.duoc.maquinaria.model.Excavadora;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class ExcavadoraRepository {

    public ObservableList<Excavadora> obtenerTodos() throws SQLException {
        ObservableList<Excavadora> listaExcavadoras = FXCollections.observableArrayList();
        String sql = "SELECT id, codigo, horas_uso, potencia, peso_toneladas, mantencion_al_dia, certificacion_seguridad FROM excavadora";
        try (Connection conexion = ConexionSQL.obtenerConexion();
             Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(sql)) {
            while (resultado.next()) {
                Excavadora excavadora = new Excavadora(
                        resultado.getString("codigo"),
                        resultado.getInt("horas_uso"),
                        resultado.getDouble("potencia"),
                        resultado.getDouble("peso_toneladas"),
                        resultado.getBoolean("mantencion_al_dia"));
                excavadora.setId(resultado.getInt("id"));
                excavadora.setCertificacionSeguridad(resultado.getBoolean("certificacion_seguridad"));
                listaExcavadoras.add(excavadora);
            }
        }
        return  listaExcavadoras;
    }

    public void agregar(Excavadora excavadora) throws SQLException {
        String sql = "INSERT INTO excavadora (codigo, horas_uso, potencia, peso_toneladas, mantencion_al_dia, certificacion_seguridad) " + "VALUES (?,?,?,?,?,?)";
        try (Connection conexion = ConexionSQL.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, excavadora.getCodigo());
            sentencia.setInt(2, excavadora.getHorasUso());
            sentencia.setDouble(3, excavadora.getPotencia());
            sentencia.setDouble(4, excavadora.getPesoToneladas());
            sentencia.setBoolean(5, excavadora.isMantencionAlDia());
            sentencia.setBoolean(6, excavadora.tieneCertificacionSeguridad());
            sentencia.executeUpdate();
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    excavadora.setId(claves.getInt(1));
                }
            }
        }
    }

    public void actualizar(Excavadora excavadora) throws SQLException {
        String sql = "UPDATE excavadora SET codigo=?, horas_uso=?, potencia=?, peso_toneladas=?, mantencion_al_dia=?, certificacion_seguridad=? WHERE id=?";
        try (Connection conexion = ConexionSQL.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, excavadora.getCodigo());
            sentencia.setInt(2, excavadora.getHorasUso());
            sentencia.setDouble(3, excavadora.getPotencia());
            sentencia.setDouble(4, excavadora.getPesoToneladas());
            sentencia.setBoolean(5, excavadora.isMantencionAlDia());
            sentencia.setBoolean(6, excavadora.tieneCertificacionSeguridad());
            sentencia.setInt(7, excavadora.getId());
            sentencia.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM excavadora WHERE id=?";
        try (Connection conexion = ConexionSQL.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        }
    }
}
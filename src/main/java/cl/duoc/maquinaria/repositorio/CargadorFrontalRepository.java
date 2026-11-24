package cl.duoc.maquinaria.repositorio;

import cl.duoc.maquinaria.conexion.ConexionSQL;
import cl.duoc.maquinaria.model.CargadorFrontal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class CargadorFrontalRepository {

    public ObservableList<CargadorFrontal> obtenerTodos() throws SQLException {
        ObservableList<CargadorFrontal> listaCargadores = FXCollections.observableArrayList();
        String sql = "SELECT id, codigo, horas_uso, potencia, capacidad_balde FROM cargador_frontal";
        try (Connection conexion = ConexionSQL.obtenerConexion();
             Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(sql)) {
            while (resultado.next()) {
                CargadorFrontal cargadorFrontal = new CargadorFrontal(
                        resultado.getString("codigo"),
                        resultado.getInt("horas_uso"),
                        resultado.getDouble("potencia"),
                        resultado.getDouble("capacidad_balde"));
                cargadorFrontal.setId(resultado.getInt("id"));
                listaCargadores.add(cargadorFrontal);
            }
        }
        return listaCargadores;
    }

    public void agregar(CargadorFrontal cargadorFrontal) throws SQLException {
        String sql = "INSERT INTO cargador_frontal (codigo, horas_uso, potencia, capacidad_balde) " + "VALUES (?,?,?,?)";
        try (Connection conexion = ConexionSQL.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, cargadorFrontal.getCodigo());
            sentencia.setInt(2, cargadorFrontal.getHorasUso());
            sentencia.setDouble(3, cargadorFrontal.getPotencia());
            sentencia.setDouble(4, cargadorFrontal.getCapacidadBalde());
            sentencia.executeUpdate();
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    cargadorFrontal.setId(claves.getInt(1));
                }
            }
        }
    }

    public void actualizar(CargadorFrontal cargadorFrontal) throws SQLException {
        String sql = "UPDATE cargador_frontal SET codigo=?, horas_uso=?, potencia=?, capacidad_balde=? WHERE id=?";
        try (Connection conexion = ConexionSQL.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, cargadorFrontal.getCodigo());
            sentencia.setInt(2, cargadorFrontal.getHorasUso());
            sentencia.setDouble(3, cargadorFrontal.getPotencia());
            sentencia.setDouble(4, cargadorFrontal.getCapacidadBalde());
            sentencia.setInt(5, cargadorFrontal.getId());
            sentencia.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM cargador_frontal WHERE id=?";
        try (Connection conexion = ConexionSQL.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        }
    }
}
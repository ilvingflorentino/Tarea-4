import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Usuarios extends JFrame {
    private JTable TablaClientes;
    private JTextField txtNombre;
    private JPanel cuerpoPrincipal;
    private JButton BtnEliminar;
    private JButton BtnActualizar;
    private JButton BtnSalir;
    private JTextField txtApellido;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtUsuario;

    private int selectedUserId = -1;

    public Usuarios() {
        inicializarForma();
        cargarUsuarios();
        configurarEventos();
        BtnSalir.addActionListener(e ->cerrarSesion());
    }

    private void inicializarForma() {
        setContentPane(cuerpoPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
    }

    private void cerrarSesion(){
        mostrarMensaje("Vuelve Pronto");
        this.dispose();
        Login login = new Login();
        login.setVisible(true);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void cargarUsuarios() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Apellido");
        model.addColumn("Telefono");
        model.addColumn("Correo");
        model.addColumn("Usuario");

        try {

            Connection conn = Conexion.getConnection();

            String query = "SELECT id, Nombre, Apellido, Telefono, Correo, Usuario FROM Usuarios";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("Nombre"),
                        rs.getString("Apellido"),
                        rs.getString("Telefono"),
                        rs.getString("Correo"),
                        rs.getString("Usuario")
                });
            }


            TablaClientes.setModel(model);

        } catch (SQLException ex) {

            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los usuarios: " + ex.getMessage());
        }
    }

    private void configurarEventos() {

        TablaClientes.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = TablaClientes.getSelectedRow();
            if (selectedRow >= 0) {

                selectedUserId = (int) TablaClientes.getValueAt(selectedRow, 0);


                txtNombre.setText((String) TablaClientes.getValueAt(selectedRow, 1));
                txtApellido.setText((String) TablaClientes.getValueAt(selectedRow, 2));
                txtTelefono.setText((String) TablaClientes.getValueAt(selectedRow, 3));
                txtCorreo.setText((String) TablaClientes.getValueAt(selectedRow, 4));
                txtUsuario.setText((String) TablaClientes.getValueAt(selectedRow, 5));
            }
        });


        BtnActualizar.addActionListener(e -> actualizarUsuario());
        BtnEliminar.addActionListener(e -> eliminarUsuario());
    }

    private void actualizarUsuario() {
        if (selectedUserId == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un Cliente para actualizar.");
            return;
        }

        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();
        String usuario = txtUsuario.getText();

        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || correo.isEmpty() || usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un cliente para actualizar.");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {

            String query = "UPDATE Usuarios SET Nombre = ?, Apellido = ?, Telefono = ?, Correo = ?, Usuario = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, telefono);
            stmt.setString(4, correo);
            stmt.setString(5, usuario);
            stmt.setInt(6, selectedUserId);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente.");
                cargarUsuarios();


                txtNombre.setText("");
                txtApellido.setText("");
                txtTelefono.setText("");
                txtCorreo.setText("");
                txtUsuario.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el Cliente.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar los datos: " + ex.getMessage());
        }
    }
    private void eliminarUsuario() {
        if (selectedUserId == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un Cliente para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este usuario?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = Conexion.getConnection()) {
                // Consulta SQL para eliminar al usuario
                String query = "DELETE FROM Usuarios WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, selectedUserId);

                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.");
                    cargarUsuarios();
                    txtNombre.setText("");
                    txtApellido.setText("");
                    txtTelefono.setText("");
                    txtCorreo.setText("");
                    txtUsuario.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el Cliente.");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al eliminar los datos: " + ex.getMessage());
            }
        }
    }
    public static void main(String[] args) {
        Usuarios usuarios = new Usuarios();
        usuarios.setVisible(true);
    }
}

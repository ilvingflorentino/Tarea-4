import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Registrar extends JFrame {
    private JLabel labelNombre;
    private JLabel labelApellido;
    private JLabel labelUsuario;
    private JLabel labelPassword;
    private JLabel labelConfirPassword;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtUsuario;
    private JPasswordField txtConfirContraseña;
    private JButton btnRegistrar;
    private JPanel panel1;
    private JPasswordField txtContraseña;
    private JTextField txtTelefono;
    private JTextField txtCorreo;

    public Registrar() {
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);

        btnRegistrar.addActionListener(e -> registrarUsuario());
    }

    private void registrarUsuario() {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();
        String usuario = txtUsuario.getText();
        String contraseña = new String(txtContraseña.getPassword());
        String confirmarContraseña = new String(txtConfirContraseña.getPassword());

        if (!contraseña.equals(confirmarContraseña)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
            return;
        }

        if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            String query = "INSERT INTO Usuarios (Nombre, Apellido, Telefono, Correo, Usuario, Contraseña) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, telefono);
            stmt.setString(4, correo);
            stmt.setString(5, usuario);
            stmt.setString(6, contraseña);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente.");
                this.dispose();
                Login login = new Login();
                login.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el usuario.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        Registrar registrar = new Registrar();
        registrar.setVisible(true);
    }
}

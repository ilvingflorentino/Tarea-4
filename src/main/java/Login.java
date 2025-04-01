import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {
    private JLabel loginLb;
    private JTextField textField1;
    private JPanel PanelPrincipal;
    private JPasswordField txtContraUsuario;
    private JButton BtnEntrar;
    private JPanel principal;
    private JButton BtnRegistrar;

    public Login() {
        inicializarForma();
        BtnEntrar.addActionListener(e -> validar());
        BtnRegistrar.addActionListener(e -> abrirFormularioRegistro());
    }

    private void inicializarForma() {
        setContentPane(principal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
    }

    private void validar() {
        String usuario = this.textField1.getText();
        String contra = new String(this.txtContraUsuario.getPassword());

        try (Connection conn = Conexion.getConnection()) {
            String query = "SELECT * FROM Usuarios WHERE Usuario = ? AND Contraseña = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, usuario);
            stmt.setString(2, contra);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                mostrarMensaje(" Bienvenido de nuevo  has iniciado con exito");
                this.dispose();
                Usuarios usuarios = new Usuarios();
                usuarios.setVisible(true);
            } else {
                mostrarMensaje("Parace que hay Datos incorrectos!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            mostrarMensaje("Error de conexión: " + ex.getMessage());
        }
    }

    private void abrirFormularioRegistro() {
        this.dispose();
        Registrar registrar = new Registrar();
        registrar.setVisible(true);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public static void main(String[] args) {
        Login login = new Login();
        login.setVisible(true);
    }
}

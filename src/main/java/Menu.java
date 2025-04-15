import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {
    private JButton btnUsuario;
    private JButton btnProductos;
    private JButton cerrarButton;
    private JPanel principal;

    public Menu() {


        setContentPane(principal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        btnUsuario.setIcon(new ImageIcon(getClass().getResource("/Images/pngwing.com.png")));
        btnUsuario.addActionListener(e -> abritUsuarios());
        btnProductos.addActionListener(e -> abrirProductos());
    }

    private void abritUsuarios(){
        this.dispose();
        Usuarios usuarios = new Usuarios();
        usuarios.setVisible(true);
    }

    private void abrirProductos(){
        this.dispose();
        Productos productos = new Productos();
        productos.setVisible(true);
    }


}

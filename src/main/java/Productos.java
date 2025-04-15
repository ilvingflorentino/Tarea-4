import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Productos extends JFrame {
    private JPanel principal;
    private JTable TablaProductos;
    private JButton btnAgregar;
    private JButton btnSalir;

    public Productos(){
        setContentPane(principal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        cargarProductos();

        btnSalir.addActionListener(e -> cerrar());
        btnAgregar.addActionListener(e -> abrirModal());

        TablaProductos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = TablaProductos.getSelectedRow();
                if (fila >= 0) {
                    int id = (int) TablaProductos.getValueAt(fila, 0);
                    String nombre = (String) TablaProductos.getValueAt(fila, 1);
                    String marca = (String) TablaProductos.getValueAt(fila, 2);
                    String categoria = (String) TablaProductos.getValueAt(fila, 3);
                    int precio = Integer.parseInt(TablaProductos.getValueAt(fila, 4).toString());
                    int cantidad = Integer.parseInt(TablaProductos.getValueAt(fila, 5).toString());

                    new ProductosModal(Productos.this, id, nombre, marca, categoria, precio, cantidad);
                }
            }
        });

    }

    private void cerrar(){
        this.dispose();
        Login login = new Login();
        login.setVisible(true);
    }

    private void abrirModal(){
        ProductosModal productosModal = new ProductosModal(this);
        productosModal.setVisible(true);
    }
    public void cargarProductos() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Marca");
        model.addColumn("Categoria");
        model.addColumn("Precio");
        model.addColumn("Cantidad");

        try {

            Connection conn = Conexion.getConnection();

            String query = "SELECT idProducto, NombreProducto, MarcaProducto, CategoriaProducto, PrecioProducto, StockProducto FROM productos";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("idProducto"),
                        rs.getString("NombreProducto"),
                        rs.getString("MarcaProducto"),
                        rs.getString("CategoriaProducto"),
                        rs.getString("PrecioProducto"),
                        rs.getString("StockProducto")
                });
            }


            TablaProductos.setModel(model);

        } catch (SQLException ex) {

            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los productos: " + ex.getMessage());
        }
    }

}

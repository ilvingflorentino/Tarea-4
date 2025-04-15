import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductosModal extends JFrame{
    private JPanel panel1;
    private JTextField txtNombre;
    private JTextField txtMarca;
    private JTextField txtCantidad;
    private JTextField txtCategoria;
    private JTextField txtPrecio;
    private JPanel principal;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private Productos productosForm;

    //Constructor para actualizar y eliminar
    private Integer productoId = null;
    public ProductosModal(Productos productosForm, Integer id, String nombre, String marca, String categoria, int precio, int cantidad) {
        this.productosForm = productosForm;
        this.productoId = id;

        setContentPane(principal);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);

        // Cargar datos en los campos
        txtNombre.setText(nombre);
        txtMarca.setText(marca);
        txtCategoria.setText(categoria);
        txtPrecio.setText(String.valueOf(precio));
        txtCantidad.setText(String.valueOf(cantidad));

        btnGuardar.setText("Actualizar");
        btnEliminar.setText("Eliminar");
        btnEliminar.setVisible(true);

        btnGuardar.addActionListener(e -> actualizarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());

        setVisible(true);
    }


    //Constructor para guardar
    // Polimorfismo: el botón Guardar ejecuta diferentes acciones según el contexto (insertar o actualizar productos)
    public ProductosModal(Productos productosForm) {
        this.productosForm = productosForm;
        this.productoId = null;

        setContentPane(principal);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);


        btnGuardar.setText("Guardar");
        btnEliminar.setVisible(false);

        btnGuardar.addActionListener(e -> registrarProducto());

        setVisible(true);

        txtNombre.addMouseListener(new MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (txtNombre.getText().equals("NOMBRE")) {
                    txtNombre.setText("");
                }
            }
        });
        txtMarca.addMouseListener(new MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (txtMarca.getText().equals("MARCA")) {
                    txtMarca.setText("");
                }
            }
        });
        txtCategoria.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (txtCategoria.getText().equals("CATEGORIA")) {
                    txtCategoria.setText("");
                }
            }
        });
        txtPrecio.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (txtPrecio.getText().equals("PRECIO")) {
                    txtPrecio.setText("");
                }
            }
        });
        txtCantidad.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (txtCantidad.getText().equals("CANTIDAD")) {
                    txtCantidad.setText("");
                }
            }
        });
    }

    private void actualizarProducto() {
        String nombre = txtNombre.getText();
        String marca = txtMarca.getText();
        String categoria = txtCategoria.getText();
        int cantidad = Integer.parseInt(txtCantidad.getText());
        int precio = Integer.parseInt(txtPrecio.getText());

        try (Connection conn = Conexion.getConnection()) {
            String sql = "UPDATE productos SET NombreProducto=?, MarcaProducto=?, CategoriaProducto=?, PrecioProducto=?, StockProducto=? WHERE idProducto=?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, nombre);
            stmt.setString(2, marca);
            stmt.setString(3, categoria);
            stmt.setInt(4, precio);
            stmt.setInt(5, cantidad);
            stmt.setInt(6, productoId);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Producto actualizado.");
            productosForm.cargarProductos();
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminarProducto() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = Conexion.getConnection()) {
            String sql = "DELETE FROM productos WHERE idProducto=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productoId);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Producto eliminado.");
            productosForm.cargarProductos();
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }

    private void registrarProducto() {
        String nombre = txtNombre.getText();
        String marca = txtMarca.getText();
        String categoria = txtCategoria.getText();
        String cantidadTexto = txtCantidad.getText();
        String precioTexto = txtPrecio.getText();

        if (nombre.isEmpty() || marca.isEmpty() || categoria.isEmpty() || cantidadTexto.isEmpty() || precioTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }

        int cantidad = Integer.parseInt(cantidadTexto);
        int precio = Integer.parseInt(precioTexto);

        try (Connection conn = Conexion.getConnection()) {
            String query = "INSERT INTO productos (NombreProducto, MarcaProducto, CategoriaProducto, PrecioProducto, StockProducto) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, nombre);
            stmt.setString(2, marca);
            stmt.setString(3, categoria);
            stmt.setInt(4, precio);
            stmt.setInt(5, cantidad);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Producto registrado exitosamente.");
                if (productosForm != null) {
                    productosForm.cargarProductos(); // ⚡ actualiza la tabla en el otro form
                }

                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el producto.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }
}

import javax.swing.*;
import java.awt.*;

public class FormularioPrincipal extends JFrame {
    private ListaLigadaRuta ruta;
    private DefaultListModel<String> listModel;
    private JList<String> jListParadas;

    private JTextField txtNombre, txtLatitud, txtLongitud, txtTiempo, txtDescripcion;
    private JLabel lblDistancia;

    public FormularioPrincipal() {
        ruta = new ListaLigadaRuta();
        listModel = new DefaultListModel<>();
        
        setTitle("I.U. Envigado - Sistema de Gestión de Rutas");
        setSize(850, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentesIzquierda();
        inicializarComponentesCentro();
        inicializarComponentesDerecha();

        configurarEventos();
    }

    private void inicializarComponentesIzquierda() {
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos de la Parada"));
        
        panelForm.add(new JLabel(" Nombre:")); txtNombre = new JTextField(); panelForm.add(txtNombre);
        panelForm.add(new JLabel(" Latitud:")); txtLatitud = new JTextField(); panelForm.add(txtLatitud);
        panelForm.add(new JLabel(" Longitud:")); txtLongitud = new JTextField(); panelForm.add(txtLongitud);
        panelForm.add(new JLabel(" Tiempo Estimado:")); txtTiempo = new JTextField(); panelForm.add(txtTiempo);
        panelForm.add(new JLabel(" Descripción:")); txtDescripcion = new JTextField(); panelForm.add(txtDescripcion);
        
        JButton btnAgregar = new JButton("Agregar Parada");
        JButton btnModificar = new JButton("Modificar Seleccionada");
        panelForm.add(btnAgregar);
        panelForm.add(btnModificar);

        btnAgregar.setName("btnAgregar");
        btnModificar.setName("btnModificar");

        add(panelForm, BorderLayout.WEST);
    }

    private void inicializarComponentesCentro() {
        JPanel panelLista = new JPanel(new BorderLayout(5, 5));
        panelLista.setBorder(BorderFactory.createTitledBorder("Visualización de la Ruta (Lista Ligada)"));
        jListParadas = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(jListParadas);
        panelLista.add(scrollPane, BorderLayout.CENTER);
        add(panelLista, BorderLayout.CENTER);
    }

    private void inicializarComponentesDerecha() {
        JPanel panelBotonesAccion = new JPanel(new GridLayout(5, 1, 5, 5));
        panelBotonesAccion.setBorder(BorderFactory.createTitledBorder("Operaciones"));
        
        JButton btnEliminar = new JButton("Eliminar Parada");
        JButton btnGuardar = new JButton("Guardar Ruta (JSON)");
        JButton btnCargar = new JButton("Cargar Ruta (JSON)");
        JButton btnCalcular = new JButton("Calcular Distancia Total");
        
        btnEliminar.setName("btnEliminar");
        btnGuardar.setName("btnGuardar");
        btnCargar.setName("btnCargar");
        btnCalcular.setName("btnCalcular");

        panelBotonesAccion.add(btnEliminar);
        panelBotonesAccion.add(btnGuardar);
        panelBotonesAccion.add(btnCargar);
        panelBotonesAccion.add(btnCalcular);

        lblDistancia = new JLabel("Distancia Total: 0.00 km", SwingConstants.CENTER);
        lblDistancia.setFont(new Font("Arial", Font.BOLD, 13));
        panelBotonesAccion.add(lblDistancia);

        add(panelBotonesAccion, BorderLayout.EAST);
    }

    private void configurarEventos() {
        JButton btnAgregar = (JButton) encontrarComponentePorNombre(this, "btnAgregar");
        JButton btnModificar = (JButton) encontrarComponentePorNombre(this, "btnModificar");
        JButton btnEliminar = (JButton) encontrarComponentePorNombre(this, "btnEliminar");
        JButton btnGuardar = (JButton) encontrarComponentePorNombre(this, "btnGuardar");
        JButton btnCargar = (JButton) encontrarComponentePorNombre(this, "btnCargar");
        JButton btnCalcular = (JButton) encontrarComponentePorNombre(this, "btnCalcular");

        jListParadas.addListSelectionListener(e -> {
            int idx = jListParadas.getSelectedIndex();
            if (idx != -1) {
                Parada p = ruta.obtener(idx);
                txtNombre.setText(p.getNombre());
                txtLatitud.setText(String.valueOf(p.getLatitud()));
                txtLongitud.setText(String.valueOf(p.getLongitud()));
                txtTiempo.setText(p.getTiempoEstimado());
                txtDescripcion.setText(p.getDescripcion());
            }
        });

        btnAgregar.addActionListener(e -> {
            try {
                ruta.agregar(capturarCampos());
                actualizarListaGUI();
                limpiarCampos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en los datos. Revise las coordenadas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnModificar.addActionListener(e -> {
            int idx = jListParadas.getSelectedIndex();
            if (idx != -1) {
                try {
                    ruta.modificar(idx, capturarCampos());
                    actualizarListaGUI();
                    JOptionPane.showMessageDialog(this, "Parada modificada correctamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al validar los campos.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una parada para modificar.");
            }
        });

        btnEliminar.addActionListener(e -> {
            int idx = jListParadas.getSelectedIndex();
            if (idx != -1) {
                ruta.eliminar(idx);
                actualizarListaGUI();
                limpiarCampos();
            }
        });

        btnCalcular.addActionListener(e -> {
            double dist = ruta.calcularDistanciaTotal();
            lblDistancia.setText(String.format("Distancia Total: %.2f km", dist));
        });

        btnGuardar.addActionListener(e -> {
            try {
                GestionArchivos.guardarRuta(ruta);
                JOptionPane.showMessageDialog(this, "Ruta guardada en JSON correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al escribir el archivo.");
            }
        });

        btnCargar.addActionListener(e -> {
            try {
                GestionArchivos.cargarRuta(ruta);
                actualizarListaGUI();
                JOptionPane.showMessageDialog(this, "Ruta recuperada desde el archivo JSON.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo JSON.");
            }
        });
    }

    private Parada capturarCampos() throws Exception {
        return new Parada(
            txtNombre.getText().trim(),
            Double.parseDouble(txtLatitud.getText().trim()),
            Double.parseDouble(txtLongitud.getText().trim()),
            txtTiempo.getText().trim(),
            txtDescripcion.getText().trim()
        );
    }

    private void actualizarListaGUI() {
        listModel.clear();
        Nodo actual = ruta.getCabeza();
        int num = 1;
        while (actual != null) {
            listModel.addElement(num + ". " + actual.parada.toString());
            actual = actual.siguiente;
            num++;
        }
    }

    private void limpiarCampos() {
        txtNombre.setText(""); txtLatitud.setText(""); txtLongitud.setText("");
        txtTiempo.setText(""); txtDescripcion.setText("");
        jListParadas.clearSelection();
    }

    private Component encontrarComponentePorNombre(Container contenedor, String nombre) {
        for (Component comp : contenedor.getComponents()) {
            if (nombre.equals(comp.getName())) return comp;
            if (comp instanceof Container) {
                Component resultado = encontrarComponentePorNombre((Container) comp, nombre);
                if (resultado != null) return resultado;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormularioPrincipal().setVisible(true);
        });
    }
}
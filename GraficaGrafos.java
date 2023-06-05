package grafos;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import grafos.Csv;
import grafos.User;
import grafos.UsersPanel;
import grafos.CreateUserPanel;
import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays; 
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;


public class GraficaGrafos extends javax.swing.JFrame {
    Csv UsersCsv;
    Csv UsersRelationsCsv;
    Boolean successValidations = false;
    Integer[][] matrizAdyacente;
    private static Object lock = new Object();
    String sortMode = "none";
    boolean[] visitadoAnchura;
    boolean[] visitadoProfunidad;
    ArrayList<Integer> recorridos;
    HashMap<Integer, HashMap> recorridoTree;
    ArrayList nodosRecorridosEnTreeProcess;
    Integer[][] coordinates;
    Integer Xcoordinates;
    Integer Ycoordinates;
    Integer NodeI;
    
    
    public GraficaGrafos(Csv usersCsv, Csv usersRelationsCsv) {
        UsersCsv = usersCsv;
        UsersRelationsCsv = usersRelationsCsv;
        initComponents();
    }

    private void initComponents() {
        JButton BtnPanelUsuarios = new javax.swing.JButton();
        JButton BtnPanelCrearUsuario = new javax.swing.JButton();
        JButton BtnGuardar = new javax.swing.JButton();
        JButton BtnBFS = new javax.swing.JButton();
        JButton BtnDFS = new javax.swing.JButton();
        JPanel panel = new JPanel();
        
        setTitle("Grafos");
        setBounds(0, 0, 1920, 1080);
        
        BtnPanelUsuarios.setText("Ver Usuarios");
        BtnPanelCrearUsuario.setText("Crear Usuario");
        BtnGuardar.setText("Guardar");
        BtnBFS.setText("BFS");
        BtnDFS.setText("DFS");
        
        panel.add(BtnPanelUsuarios);
        panel.add(BtnPanelCrearUsuario);
        panel.add(BtnGuardar);
        panel.add(BtnBFS);
        panel.add(BtnDFS);
        panel.setBackground(Color.decode("#202020"));
        panel.setPreferredSize(new Dimension(1700, 900));
        getContentPane().add(panel);
        
        BtnPanelUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPanelUsuariosPerformed(evt);
            }
        });
        
        BtnPanelCrearUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPanelCrearUsuario(evt);
            }
        });
        
        BtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    BtnGuardarPerformed(evt);
                } catch(Exception e) {
                    System.out.println("error");
                }
                
            }
        });
        
        BtnBFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBFSPerformed(evt);
            }
        });
        
        BtnDFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDFSPerformed(evt);
            }
        });
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        
        pack();
    }                    
    
    public void setMatrizAdyacente() {
        matrizAdyacente = new Integer[this.UsersCsv.usersArr.length][this.UsersCsv.usersArr.length];
        for(int i = 0; i < this.UsersCsv.usersArr.length; i++) {
            for(int j = 0; j < this.UsersCsv.usersArr.length; j++) {
                for(int a = 0; a < this.UsersRelationsCsv.usersRelationsArr.length; a++) {
                    if(this.UsersRelationsCsv.usersRelationsArr[a].User1ID.equals(this.UsersCsv.usersArr[i].ID) || this.UsersRelationsCsv.usersRelationsArr[a].User2ID.equals(this.UsersCsv.usersArr[i].ID)) {
                        if(this.UsersRelationsCsv.usersRelationsArr[a].User1ID.equals(this.UsersCsv.usersArr[j].ID) || this.UsersRelationsCsv.usersRelationsArr[a].User2ID.equals(this.UsersCsv.usersArr[j].ID)) {
                            if(i != j) {
                                matrizAdyacente[i][j] = 1;
                            } else {
                                matrizAdyacente[i][j] = 0;
                            }
                        } else {
                            matrizAdyacente[i][j] = 0;
                        }
                    } else if(matrizAdyacente[i][j] == null) {
                        matrizAdyacente[i][j] = 0;
                    } 
                }
            }
        }
    }
    
    public void sortModeNormal(Graphics g) {
        Graphics2D circulo = (Graphics2D) g;
        coordinates = new Integer[this.UsersCsv.usersArr.length][2];
        
        for(int i = 0; i < this.UsersCsv.usersArr.length; i++) {
            coordinates[i][0] = NumeroAleatorio(800);
            coordinates[i][1] = NumeroAleatorio(600);
        }
        
        circulo.setColor(Color.gray);
        for(int a = 0; a < this.UsersRelationsCsv.usersRelationsArr.length; a++) {
            Integer user1Index = Arrays.asList(this.UsersCsv.usersArr).indexOf(this.UsersRelationsCsv.usersRelationsArr[a].User1);
            Integer user2Index = Arrays.asList(this.UsersCsv.usersArr).indexOf(this.UsersRelationsCsv.usersRelationsArr[a].User2);
            Integer[] user1Coordinates = coordinates[user1Index];
            Integer[] user2Coordinates = coordinates[user2Index];
            circulo.drawLine(user1Coordinates[0]+50, user1Coordinates[1]+50, user2Coordinates[0]+50, user2Coordinates[1]+50);
        }
        
        circulo.setPaint(Color.BLUE);
        for(int i = 0; i < this.UsersCsv.usersArr.length; i++) {
            circulo.setStroke(new BasicStroke(3.f));
            circulo.fillOval(coordinates[i][0], coordinates[i][1], 50, 50);
        }
        
        circulo.setColor(Color.WHITE);
        for(int i = 0; i < this.UsersCsv.usersArr.length; i++) {
            circulo.setColor(Color.WHITE);
            circulo.drawString(this.UsersCsv.usersArr[i].Username, coordinates[i][0]+50, coordinates[i][1]+50);
        }
    }
    
    public HashMap<Integer, HashMap> getNodeTree(int index, boolean sumYCoordinates) {
        Xcoordinates += 100;
        if(sumYCoordinates) {
            Ycoordinates += 100;
        }
        coordinates[index][0] = Xcoordinates;
        coordinates[index][1] = Ycoordinates;
        HashMap<Integer, HashMap> nodeTree = new HashMap<>();
        Integer userIndex = recorridos.get(index);
        Integer siblingNodesNum = 0;
        for(int i = 0; i < this.recorridos.size(); i++) {
            if(this.matrizAdyacente[userIndex][this.recorridos.get(i)].equals(1) && !nodosRecorridosEnTreeProcess.contains(i)) {
                boolean sum_y_coordinates = false;
                if(siblingNodesNum == 0) {
                    sum_y_coordinates = true;
                }
                nodosRecorridosEnTreeProcess.add(i);
                HashMap<Integer, HashMap> childrenNode = getNodeTree(i, sum_y_coordinates);
                nodeTree.put(i, childrenNode);
                siblingNodesNum += 1;
            }
        }
        return nodeTree;
    }
    
    public void sortModeBFS(Integer nodoI) {
        setMatrizAdyacente();
        //Lista donde guardo los nodos recorridos
        recorridos = new ArrayList<Integer>();
        visitadoAnchura = new boolean[this.matrizAdyacente.length];
        //El nodo inicial ya está visitado
        visitadoAnchura[nodoI] = true;
        //Cola de visitas de los nodos adyacentes
        ArrayList<Integer> cola = new ArrayList<Integer>();
        //Se lista el nodo como ya recorrido
        recorridos.add(nodoI);
        //Se agrega el nodo a la cola de visitas
        cola.add(nodoI);
        //Hasta que visite todos los nodos
        while (!cola.isEmpty()) {
            int j = cola.remove(0); //Se saca el primero nodo de la cola
        //Se busca en la matriz que representa el grafo los nodos adyacentes
            for (int i = 0; i < this.matrizAdyacente.length; i++) {
        //Si es un nodo adyacente y no está visitado entonces
                if (this.matrizAdyacente[j][i] == 1 && !visitadoAnchura[i]) {
                    cola.add(i);//Se agrega a la cola de visitas
                    recorridos.add(i);//Se marca como recorrido
                    visitadoAnchura[i] = true;//Se marca como visitado
                }
            }
        }
        nodosRecorridosEnTreeProcess = new ArrayList();
        coordinates = new Integer[recorridos.size()][2];
        this.Xcoordinates = 100;
        this.Ycoordinates = 100;
        recorridoTree = new HashMap();
        recorridoTree.put(0, getNodeTree(0, true));
        System.out.println("Hola Mundo");
    }
    
    public ArrayList<Integer> sortModeDFS(Integer nodoI) {
        //Lista donde guardo los nodos recorridos
        ArrayList<Integer> nodosRecorridos = new ArrayList<Integer>();
        visitadoProfunidad[nodoI] = true;//El nodo inicial ya está visitado
        //Cola de visitas de los nodos adyacentes
        ArrayList<Integer> cola = new ArrayList<Integer>();
        nodosRecorridos.add(nodoI);//Listo el nodo como ya recorrido
        cola.add(nodoI);//Se agrega el nodo a la cola de visitas
        while (!cola.isEmpty()) {//Hasta que visite todos los nodos
            int j = cola.remove(0);//Se saca el primer nodo de la cola
            //Se busca en la matriz que representa el grafo los nodos adyacentes
            for (int i = 0; i < this.matrizAdyacente.length; i++) {
                //Si es un nodo adyacente y no está visitado entonces
                if (this.matrizAdyacente[j][i] == 1 && !visitadoProfunidad[i]) {
                    cola.add(i);//Se agrega a la cola de visita
                    //Se recorren los hijos del nodo actual de visita y se agrega el recorrido al la lista
                    ArrayList<Integer> node = sortModeDFS(i);
                    if(node != null) {
                        nodosRecorridos.addAll(node);
                        visitadoProfunidad[i] = true;//Se marca como visitado
                    }
                }
            }
        }
        System.out.println("Hola Mundo");
        return nodosRecorridos;
    }
    
    public void paintSortModes(Graphics g) {
        Graphics2D circulo = (Graphics2D) g;
        circulo.setColor(Color.gray);
        ArrayList<String> Usernames = new ArrayList();
        ArrayList<Integer> coordinatesOrder = new ArrayList();
        for(int i = 0; i < this.UsersRelationsCsv.usersRelationsArr.length; i++) {
            Integer user1Index = Arrays.asList(this.UsersCsv.usersArr).indexOf(this.UsersRelationsCsv.usersRelationsArr[i].User1);
            Integer user2Index = Arrays.asList(this.UsersCsv.usersArr).indexOf(this.UsersRelationsCsv.usersRelationsArr[i].User2);
            Integer user1recorridosIndex = recorridos.indexOf(user1Index);
            Integer user2recorridosIndex = recorridos.indexOf(user2Index);
            if(!user1recorridosIndex.equals(-1) && !user2recorridosIndex.equals(-1)) {
                Usernames.add(this.UsersRelationsCsv.usersRelationsArr[i].User1.Username);
                Usernames.add(this.UsersRelationsCsv.usersRelationsArr[i].User2.Username);
                coordinatesOrder.add(user1recorridosIndex);
                coordinatesOrder.add(user2recorridosIndex);
                circulo.drawLine(coordinates[user1recorridosIndex][0]+50, coordinates[user1recorridosIndex][1]+50, coordinates[user2recorridosIndex][0]+50, coordinates[user2recorridosIndex][1]+50);
            }
        }
        
        circulo.setPaint(Color.BLUE);
        for(int i = 0; i < coordinates.length; i++) {
            circulo.setStroke(new BasicStroke(3.f));
            circulo.fillOval(coordinates[i][0], coordinates[i][1], 50, 50);
        }
        
        circulo.setColor(Color.WHITE);
        for(int i = 0; i < coordinatesOrder.size(); i++) {
            if(i < coordinates.length) {
                circulo.setColor(Color.WHITE);
                circulo.drawString(Usernames.get(i), coordinates[coordinatesOrder.get(i)][0]+50, coordinates[coordinatesOrder.get(i)][1]+50);
            }
        }
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D circulo = (Graphics2D) g;
        if(sortMode == "none") {
            sortModeNormal(g);
        }
        if(sortMode == "BFS") {
            sortModeBFS( NodeI);
            paintSortModes(g);
        }
        if(sortMode == "DFS") {
            setMatrizAdyacente();
            visitadoProfunidad = new boolean[this.matrizAdyacente.length];
            recorridos = sortModeDFS(NodeI);
            nodosRecorridosEnTreeProcess = new ArrayList();
            coordinates = new Integer[recorridos.size()][2];
            this.Xcoordinates = 100;
            this.Ycoordinates = 100;
            recorridoTree = new HashMap();
            recorridoTree.put(NodeI, getNodeTree(0, true));
            paintSortModes(g);
        }
       
    }
    
    //abrir ventanas de usuarios
    public void BtnPanelUsuariosPerformed(java.awt.event.ActionEvent evt) { 
        setVisible(false);
        UsersPanel usersPanel = new UsersPanel(UsersCsv, UsersRelationsCsv);
        usersPanel.setVisible(true);

        Thread t = new Thread() {
            public void run() {
                synchronized(lock) {
                    while (usersPanel.isVisible())
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
        };
        t.start();

        usersPanel.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                synchronized (lock) {
                    usersPanel.setVisible(false);
                    lock.notify();
                    setVisible(true);
                    UsersCsv = usersPanel.UsersCsv;
                    UsersRelationsCsv = usersPanel.usersRelationsCsv;
                }
            }

        });
    }  
    
    //abrir ventana de crear Usuario
    public void BtnPanelCrearUsuario(java.awt.event.ActionEvent evt) {
        setVisible(false);
        CreateUserPanel createUserPanel = new CreateUserPanel(UsersCsv);
        createUserPanel.setVisible(true);

        Thread t = new Thread() {
            public void run() {
                synchronized(lock) {
                    while (createUserPanel.isVisible())
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
        };
        t.start();

        createUserPanel.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                synchronized (lock) {
                    createUserPanel.setVisible(false);
                    lock.notify();
                    setVisible(true);
                }
            }

        });
        
        
        createUserPanel.jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createUserPanel.jButton1ActionPerformed(e);
                synchronized (lock) {
                    lock.notify();
                    UsersCsv = createUserPanel.UsersCsv;
                    setVisible(true);
                }
            }
        });
    }
    
    //guardar los datos de los csv
    public void BtnGuardarPerformed(java.awt.event.ActionEvent evt) throws IOException {
        String UsersCsvStrData = "";
        String UsersRelationsCsvStrData = "";
        for(int i = 0; i < UsersCsv.usersArr.length; i++) {
            UsersCsvStrData += UsersCsv.usersArr[i].ID.toString()+","+UsersCsv.usersArr[i].Username+"";
            if(i != UsersCsv.usersArr.length-1) {
                UsersCsvStrData += "\r\n";
            }
        }
        
        for(int i = 0; i < this.UsersRelationsCsv.usersRelationsArr.length; i++) {
            UsersRelationsCsvStrData += this.UsersRelationsCsv.usersRelationsArr[i].User1ID.toString()+",";
            UsersRelationsCsvStrData += this.UsersRelationsCsv.usersRelationsArr[i].User2ID.toString()+",";
            UsersRelationsCsvStrData += this.UsersRelationsCsv.usersRelationsArr[i].RelationTime.toString();
            if(i != this.UsersRelationsCsv.usersRelationsArr.length-1) {
                UsersRelationsCsvStrData += "\r\n";
            }
        }
        
        Path usersfileName = Path.of(this.UsersCsv.filePath);
        Files.writeString(usersfileName, UsersCsvStrData);
        Path usersRelationsfileName = Path.of(this.UsersRelationsCsv.filePath);
        Files.writeString(usersRelationsfileName, UsersRelationsCsvStrData);
    }
    
    
    //mostrar recorrido de anchura
    public void BtnBFSPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        selectNodePanel newWindow = new selectNodePanel(UsersCsv.usersArr);
        newWindow.setVisible(true);

        Thread t = new Thread() {
            public void run() {
                synchronized(lock) {
                    while (newWindow.isVisible())
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
        };
        t.start();
        
        newWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                synchronized (lock) {
                    newWindow.setVisible(false);
                    lock.notify();
                    setVisible(true);
                }
            }

        });
        
        newWindow.jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newWindow.jButton1ActionPerformed(e);
                synchronized (lock) {
                    lock.notify();
                    NodeI = newWindow.selectedUserIndex;
                    setVisible(true);
                }
            }
        });
        this.sortMode = "BFS";
        repaint();
    }
    
    //mostrar recorrido de profundidad
    public void BtnDFSPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        selectNodePanel newWindow = new selectNodePanel(UsersCsv.usersArr);
        newWindow.setVisible(true);

        Thread t = new Thread() {
            public void run() {
                synchronized(lock) {
                    while (newWindow.isVisible())
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
        };
        t.start();
        
        newWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                synchronized (lock) {
                    newWindow.setVisible(false);
                    lock.notify();
                    setVisible(true);
                }
            }

        });
        
        newWindow.jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newWindow.jButton1ActionPerformed(e);
                synchronized (lock) {
                    lock.notify();
                    NodeI = newWindow.selectedUserIndex;
                    setVisible(true);
                }
            }
        });
        this.sortMode = "DFS";
        repaint();
    }
    
    public int NumeroAleatorio(int n) {
        int ale = 0;
        ale = (int) (Math.random() * n);
        return ale;
    }
   
}
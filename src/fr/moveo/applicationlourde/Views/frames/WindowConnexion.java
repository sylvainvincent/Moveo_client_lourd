package fr.moveo.applicationlourde.Views.frames;

import com.apple.eawt.Application;
import fr.moveo.applicationlourde.Events.MyListener;
import fr.moveo.applicationlourde.Views.panels.ScreenConnection;
import fr.moveo.applicationlourde.model.User;
import fr.moveo.applicationlourde.services.AbstractMethods;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Sylvain on 12/03/15.
 * Fenetre de connection. Censé etre la premiere fenetre qui doit apparaitre.
 */
public class WindowConnexion extends JFrame implements ActionListener {

    // DECLARATION DES PANELS
    private ScreenConnection screen;
    ImageIcon icon = new ImageIcon("res/img/Logo.png");
    JLabel jLabel;

    // CONSTRUCTEUR
    public WindowConnexion(){
        jLabel = new JLabel(icon);
        screen = new ScreenConnection();
        this.add(jLabel, BorderLayout.NORTH);
        this.add(screen, BorderLayout.CENTER);

        screen.getLoginButton().addActionListener(this);
        this.windowConfiguration();
    }

    // Procedure permettant de configuration la fenêtre
    public void windowConfiguration(){
        MyListener myListener = new MyListener();
        addWindowListener(myListener);
        Color homeColor = new Color(5, 100, 200);
        this.setBackground(homeColor); // Couleur de fond
        Application application = Application.getApplication();// spécifique aux ordi Apple
        application.setDockIconImage(Toolkit.getDefaultToolkit().getImage("res/img/icon.png"));//definit l'image pour les dock d'apple
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("res/img/icon.png")); 	//Icône en haut à gauche
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Arrête complètement le processus avec le bouton [X]
        this.setTitle("Connexion");  //Définit un titre pour notre fenêtre
        this.setSize(500, 5000); // Taille fenêtre en pixel (h,v) h=horizontal v=vertical
        this.setLocationRelativeTo(null);  // Centre la fenêtre par défaut
        this.setResizable(true); // Empêche le redimensionnement de la fenêtre
        pack();
        this.setVisible(true); //Rendre visible la fenêtre admin@moveo.fr admin

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        User moderator = new User();
        StringBuffer result = new StringBuffer();

        //bout de code pour tester l'application avec connexion internet

        AbstractMethods abstractMethods = new AbstractMethods();
        if (screen.getMailEditText().getText().equals("")|| screen.getPasswordEditText().getText().equals("")){
            JOptionPane.showMessageDialog(null, "veuillez remplir les deux champs","Attention",JOptionPane.ERROR_MESSAGE);
        }else{
            result = abstractMethods.loggin(screen.getMailEditText().getText(),screen.getPasswordEditText().getText());
            System.out.println(result);
            JSONObject json = new JSONObject(result.toString());
            if (json.getInt("error")==1){
                JOptionPane.showMessageDialog(null, "le modérateur n'existe pas","Oups",JOptionPane.WARNING_MESSAGE);
            }else {
                moderator.setLastName(json.getJSONObject("moderator").getString("moderator_name"));
                moderator.setId(json.getJSONObject("moderator").getInt("moderator_id"));
                moderator.setEmail(json.getJSONObject("moderator").getString("moderator_email"));
                boolean is_admin = (1 == json.getJSONObject("moderator").getInt("is_admin"));
                moderator.setIsAdmin(is_admin);

                ArrayList<User> userList = abstractMethods.getArrayList(abstractMethods.getUsers());

                if (result.toString()!="acces refuse"){
                    this.dispose();
                    new WindowMain(moderator, userList);
                }
                else JOptionPane.showMessageDialog(null, result.toString(),"REFUSE",JOptionPane.WARNING_MESSAGE);
            }
        }
    }


}

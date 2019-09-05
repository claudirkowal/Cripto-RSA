package RSA;
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
 
public class JanelaRSA extends JFrame implements ActionListener{
     
    private JTextField caixaTamPrimo;
    private JButton btnGerarChaves;
    private ButtonGroup grupoBotaoOpcao;
    private JRadioButton btnEncriptar, btnDesEncriptar;
    private JTextArea areaOrigem, areaDestino;
    private Container cntAreas, cntGerChaves;
    private RSA rsa;
    private BigInteger[] textoCifrado;
     
     
    public JanelaRSA() {
                
        caixaTamPrimo = new JTextField();
        caixaTamPrimo.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                generarClaves();
            }
        });
         
        btnGerarChaves = new JButton("Gerar Chaves");
        btnGerarChaves.addActionListener(this);
         
        btnEncriptar = new JRadioButton("Criptografar", false);
        btnEncriptar.addActionListener( new ManejadorBotonOpcion() );
        btnEncriptar.setEnabled(false);
        btnDesEncriptar = new JRadioButton("Descriptografar", false);
        btnDesEncriptar.addActionListener( new ManejadorBotonOpcion() );
        btnDesEncriptar.setEnabled(false);
         
        grupoBotaoOpcao = new ButtonGroup();
        grupoBotaoOpcao.add(btnEncriptar);
        grupoBotaoOpcao.add(btnDesEncriptar);
         
        areaOrigem = new JTextArea();
        areaOrigem.setWrapStyleWord(true);
        areaOrigem.setLineWrap(true);
        areaDestino = new JTextArea();
        areaDestino.setLineWrap(true);
        areaDestino.setEditable(false);
         
        cntAreas = new Container();
        cntGerChaves = new Container();
         
        cntGerChaves.setLayout(new GridLayout(2,3));
        cntGerChaves.add(new JLabel("Tamanho da chave: "));
        cntGerChaves.add(caixaTamPrimo);
        cntGerChaves.add(btnGerarChaves);
        cntGerChaves.add(new JLabel("Texto a Criptografar:"));
        cntGerChaves.add(btnEncriptar);
        cntGerChaves.add(btnDesEncriptar);
         
        cntAreas.setLayout(new GridLayout(1,2,5,5));
        cntAreas.add(new JScrollPane(areaOrigem));
        cntAreas.add(new JScrollPane(areaDestino));
         
        getContentPane().add(cntGerChaves, BorderLayout.NORTH);
        getContentPane().add(cntAreas, BorderLayout.CENTER);
         
        setSize(650,300);
        setVisible(true);
    }
     
   
    public void actionPerformed( ActionEvent evento ) {
        if(evento.getSource().equals(btnGerarChaves))
            generarClaves();
    }
     
     
    private void generarClaves() {
        if(caixaTamPrimo.getText().equals(""))
            JOptionPane.showMessageDialog(null,"Digite o tamanho da chave", "Campo Vazio", JOptionPane.ERROR_MESSAGE);
        else {
            rsa = new RSA(Integer.parseInt(caixaTamPrimo.getText()));
            rsa.geraPrimos();
            rsa.geraChaves();
            JTextArea area = new JTextArea(20,50);
            area.setEditable(false);
            area.setLineWrap(true);
            area.append("Tam chave: "+caixaTamPrimo.getText()+"\n\n");
            area.append("p:["+rsa.damep()+"]\n\nq:["+rsa.dameq()+"]\n\n");
            area.append("Chave publica (n,e):\n\nn:["+rsa.damen()+"]\n\ne:["+rsa.damee()+"]\n\n");
            area.append("Chave privada (n,d):\n\nn:["+rsa.damen()+"]\n\nd:["+rsa.damed()+"]");
            JOptionPane.showMessageDialog(null, new JScrollPane(area),"Valores Gerados", JOptionPane.INFORMATION_MESSAGE);
            btnEncriptar.setEnabled(true);
            btnDesEncriptar.setEnabled(true);
        }
    }
     
    private class ManejadorBotonOpcion implements ActionListener {
         
        public void actionPerformed( ActionEvent evento ) {
            if(evento.getSource().equals(btnEncriptar)) {
                if(areaOrigem.getText().equals(""))
                    JOptionPane.showMessageDialog(null,"Insira amensagem a criptografar", "Campo Vazio", JOptionPane.ERROR_MESSAGE);
                else {
                    textoCifrado = rsa.encripta(areaOrigem.getText());
                    areaDestino.setText("");
                    for(int i=0; i<textoCifrado.length; i++)
                        areaDestino.append(textoCifrado[i].toString());
                }
            } else if(evento.getSource().equals(btnDesEncriptar)) {
                
                    areaDestino.setText("");
                    String recuperarTextoPlano = rsa.desencripta(textoCifrado);
                    areaDestino.setText(recuperarTextoPlano);
                }
            }
        }
         
     
    public static void main(String args[]) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        JanelaRSA ventana = new JanelaRSA();
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
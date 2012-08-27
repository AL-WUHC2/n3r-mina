package org.n3r.byter;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;

public class MinaByter {

    JFrame frmMinaByter;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MinaByter window = new MinaByter();
                    window.frmMinaByter.setVisible(true);
                    window.panel.setDividerLocation();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MinaByter() throws IOException {
        initialize();
    }

    final MinaByterPanel panel = new MinaByterPanel();

    /**
     * Initialize the contents of the frame.
     * @throws IOException 
     */
    private void initialize() throws IOException {
        frmMinaByter = new JFrame();
        frmMinaByter.setTitle("Mina Byter");
        frmMinaByter.setBounds(100, 100, 750, 600);
        frmMinaByter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMinaByter.getContentPane().add(panel, BorderLayout.CENTER);

        frmMinaByter.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            }
        });

        frmMinaByter.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.setDividerLocation();
            }
        });
    }

}

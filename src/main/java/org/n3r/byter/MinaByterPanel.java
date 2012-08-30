package org.n3r.byter;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.core.lang.RHex;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.utils.JCTypeUtils;

import static org.n3r.core.lang.RByte.*;
import static org.n3r.mina.utils.JCMessageUtils.*;

public class MinaByterPanel extends JPanel {

    private static final long serialVersionUID = 962688610312724912L;
    private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private JPanel topPanel = new JPanel();
    private JPanel leftPanel = new JPanel();
    private JPanel rightPanel = new JPanel();
    private JSplitPane splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    final JComboBox comboTypes = new JComboBox(new String[] { "JAVA卡平台侧", "ESS侧" });
    JComboBox comboJobType = new JComboBox(new String[] {
            "0x01：卡片验证请求",
            "0x02：可下载应用列表请求",
            "0x03：已下载应用列表请求",
            "0x04：首页列表更新请求",
            "0x05：应用下载请求",
            "0x06：应用删除请求",
            "0x07：应用锁定请求",
            "0x08：应用解锁请求",
            "0x09：应用升级请求" });
    private JButton btnByter = new JButton("转换");
    private JButton btnClean = new JButton("清空");

    JTextPane textHEX;
    JTextPane textJSON;

    public void setDividerLocation() {
        splitPane.setDividerLocation(0.1);
        splitPane1.setDividerLocation(0.5);
    }

    public MinaByterPanel() {
        // Layout
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWeights = new double[] { 1.0 };
        gridBagLayout.rowWeights = new double[] { 1.0 };
        gridBagLayout.rowHeights = new int[] { 512 };
        setLayout(gridBagLayout);

        // Top
        GridBagLayout gbl_topPanel = new GridBagLayout();
        gbl_topPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
        gbl_topPanel.rowWeights = new double[] { 1.0 };
        gbl_topPanel.rowHeights = new int[] { 10 };
        topPanel.setLayout(gbl_topPanel);

        JLabel lblTypes = new JLabel("报文所属:");
        GridBagConstraints gbc_lblTypes = new GridBagConstraints();
        gbc_lblTypes.insets = new Insets(0, 0, 0, 5);
        gbc_lblTypes.gridx = 0;
        gbc_lblTypes.gridy = 0;
        topPanel.add(lblTypes, gbc_lblTypes);

        GridBagConstraints gbc_comboTypes = new GridBagConstraints();
        gbc_comboTypes.fill = GridBagConstraints.CENTER;
        gbc_comboTypes.insets = new Insets(0, 5, 0, 5);
        gbc_comboTypes.gridx = 1;
        gbc_comboTypes.gridy = 0;
        topPanel.add(comboTypes, gbc_comboTypes);

        GridBagConstraints gbc_comboIFNo = new GridBagConstraints();
        gbc_comboIFNo.fill = GridBagConstraints.CENTER;
        gbc_comboIFNo.insets = new Insets(0, 5, 0, 5);
        gbc_comboIFNo.gridx = 2;
        gbc_comboIFNo.gridy = 0;
        topPanel.add(comboJobType, gbc_comboIFNo);

        GridBagConstraints gbc_btnByter = new GridBagConstraints();
        gbc_btnByter.fill = GridBagConstraints.CENTER;
        gbc_btnByter.insets = new Insets(0, 5, 0, 5);
        gbc_btnByter.gridx = 3;
        gbc_btnByter.gridy = 0;
        topPanel.add(btnByter, gbc_btnByter);

        GridBagConstraints gbc_btnClean = new GridBagConstraints();
        gbc_btnClean.fill = GridBagConstraints.CENTER;
        gbc_btnClean.insets = new Insets(0, 5, 0, 0);
        gbc_btnClean.gridx = 4;
        gbc_btnClean.gridy = 0;
        topPanel.add(btnClean, gbc_btnClean);

        GridBagConstraints gbc_Empty = new GridBagConstraints();
        gbc_Empty.fill = GridBagConstraints.CENTER;
        gbc_Empty.insets = new Insets(0, 0, 0, 0);
        gbc_Empty.gridx = 5;
        gbc_Empty.gridy = 0;
        topPanel.add(new JLabel(""), gbc_Empty);

        comboTypes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboTypes.getSelectedItem().equals("ESS侧")) comboJobType.setVisible(false);
                else comboJobType.setVisible(true);
            }
        });

        btnByter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String hexStr = textHEX.getText();
                    byte[] bytes = RHex.decode(hexStr);
                    if (bytes.length < 2) {
                        textJSON.setText("{Error: 非法内容}");
                        return;
                    }

                    short len = new BeanFromBytes<Short>().fromBytes(bytes, Short.class, 0).getBean();
                    if (bytes.length < 2 + len) {
                        textJSON.setText("{Error: 长度不足}");
                        return;
                    }

                    byte[] content = subBytes(bytes, 2, len);
                    ParseBean<JCMessageHead> messageHead = parseMessageHead(content);
                    String ifNo = JCTypeUtils.getMsgTypeIFString(messageHead.getBean().getTypeFlag());

                    JCMessage message = null;
                    if (comboTypes.getSelectedItem().equals("ESS侧")) {
                        Class<?> bodyType = ifNo.equals("IF1") ? parseReqBodyType(ifNo) : parseRspBodyType(ifNo, "");
                        message = parseMessage(content, messageHead, bodyType).getBean();
                    }
                    else {
                        String jobType = RHex.encode(new byte[] { (byte) (comboJobType.getSelectedIndex() + 1) });
                        Class<?> bodyType = ifNo.equals("IF1") ? parseRspBodyType(ifNo, jobType)
                                : parseReqBodyType(ifNo);
                        message = parseMessage(content, messageHead, bodyType).getBean();
                    }

                    textJSON.setText(JSONEx.toJSONString(message, true));

                    textHEX.requestFocus();
                    textHEX.select(0, (len + 2) * 2);
                }
                catch (Throwable ex) {
                    textJSON.setText(ex.toString());
                    return;
                }
            }
        });

        btnClean.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textHEX.setText("");
                textJSON.setText("");
            }
        });

        // Left
        GridBagLayout gbl_leftPanel = new GridBagLayout();
        gbl_leftPanel.rowWeights = new double[] { 0.0, 1.0 };
        gbl_leftPanel.columnWeights = new double[] { 1.0 };
        leftPanel.setLayout(gbl_leftPanel);

        JLabel lblHEX = new JLabel("十六进制字符串:");
        GridBagConstraints gbc_lblHEX = new GridBagConstraints();
        gbc_lblHEX.insets = new Insets(0, 0, 0, 5);
        gbc_lblHEX.gridx = 0;
        gbc_lblHEX.gridy = 0;
        leftPanel.add(lblHEX, gbc_lblHEX);

        textHEX = new JTextPane();
        textHEX.setFont(new Font("Consolas", Font.PLAIN, 15));
        JScrollPane scrollHex = new JScrollPane();
        scrollHex.setViewportView(textHEX);

        GridBagConstraints gbc_scrollHex = new GridBagConstraints();
        gbc_scrollHex.fill = GridBagConstraints.BOTH;
        gbc_scrollHex.gridx = 0;
        gbc_scrollHex.gridy = 1;
        leftPanel.add(scrollHex, gbc_scrollHex);

        // Right
        GridBagLayout gbl_rightPanel = new GridBagLayout();
        gbl_rightPanel.rowWeights = new double[] { 0.0, 1.0 };
        gbl_rightPanel.columnWeights = new double[] { 1.0 };
        rightPanel.setLayout(gbl_rightPanel);

        JLabel lblJSON = new JLabel("JSON结果:");
        GridBagConstraints gbc_lblJSON = new GridBagConstraints();
        gbc_lblJSON.insets = new Insets(0, 0, 0, 5);
        gbc_lblJSON.gridx = 0;
        gbc_lblJSON.gridy = 0;
        rightPanel.add(lblJSON, gbc_lblJSON);

        textJSON = new JTextPane();
        textJSON.setFont(new Font("Consolas", Font.PLAIN, 15));
        textJSON.setEditable(false);
        textJSON.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        JScrollPane scrollJSON = new JScrollPane();
        scrollJSON.setViewportView(textJSON);

        GridBagConstraints gbc_scrollJSON = new GridBagConstraints();
        gbc_scrollJSON.fill = GridBagConstraints.BOTH;
        gbc_scrollJSON.gridx = 0;
        gbc_scrollJSON.gridy = 1;
        rightPanel.add(scrollJSON, gbc_scrollJSON);

        // Layout
        splitPane1.setLeftComponent(leftPanel);
        splitPane1.setRightComponent(rightPanel);
        splitPane1.setContinuousLayout(true);
        splitPane1.setOneTouchExpandable(true);

        splitPane.setTopComponent(topPanel);
        splitPane.setBottomComponent(splitPane1);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);

        GridBagConstraints gbc_splitPane = new GridBagConstraints();
        gbc_splitPane.fill = GridBagConstraints.BOTH;
        gbc_splitPane.gridx = 0;
        gbc_splitPane.gridy = 0;
        this.add(splitPane, gbc_splitPane);
    }
}

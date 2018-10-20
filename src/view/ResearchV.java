/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.ResearchController;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.event.KeyEvent;
import static java.awt.image.ImageObserver.WIDTH;
import java.util.Timer;
import java.util.TimerTask;
import model.Research;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author IFL08
 */
public class ResearchV extends javax.swing.JFrame {

    private DefaultTableModel tm;
    
    /**
     * Creates new form Research
     */
    public ResearchV() {
        initComponents();
        
        tm = (DefaultTableModel) jTable1.getModel();
        
        initList();
        
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if(jTable1.getSelectedRows().length > 0) {
                    String val = (String) tm.getValueAt(jTable1.getSelectedRow(), 5);
                    if(val.equals("1")) {
                        borrow2.setVisible(false);
                    } else {
                        borrow2.setVisible(true);
                    }
                }
            }
        });
    }
    
    public void animatePanel(){
        TimerTask repeatedTask = new TimerTask() {
            boolean nextPhase = false;
            int i = 0;
            
            public void run() {
                if(!nextPhase) {
                    jPanel7.setLocation(jPanel7.getLocation().x - 1, jPanel7.getLocation().y);
                    if(jPanel7.getLocation().x == 670) {
                        nextPhase = true;
                    }
                } else {
                    if(i < 3000) {
                        i++;
                    } else {
                        jPanel7.setLocation(jPanel7.getLocation().x + 1, jPanel7.getLocation().y);
                        if(jPanel7.getLocation().x == 900) {
                            cancel();
                        }
                    }
                }
                
            }
        };
                
        Timer timer = new Timer("Timer");
        long delay  = 1L;
        long period = 1L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }
    
    public void appendResearch(ResultSet rs)
    {

        try {
            if(rs.next()) {
                jLabel2.setText("Research has been added");
                animatePanel();
                
                String a[] = new String[6];
                String dtxt;
                String trans = (rs.getString(6) == null) ? "" : rs.getString(6);
                if(rs.getTimestamp(4) != null) {
                    Date dt = rs.getTimestamp(4);
                    dtxt = new SimpleDateFormat("MMM dd, YYYY").format(dt) + "";
                } else {
                    dtxt = "";
                }

                a[0] = rs.getInt(1) + "";
                a[1] = rs.getString(2) + "";
                a[2] = rs.getString(3) + "";
                a[3] = dtxt;
                a[4] = rs.getString(5) + "";
                a[5] = trans;

                tm.addRow(a);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResearchV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateRow(ResultSet rs)
    {
        if(rs == null) return;
        try {
            if(rs.next()) {
                jLabel2.setText("Research has been updated");
                animatePanel();
                
                String a[] = new String[6];
                String dtxt;
                if(rs.getTimestamp(4) != null) {
                    Date dt = rs.getTimestamp(4);
                    dtxt = new SimpleDateFormat("MMM dd, YYYY").format(dt) + "";
                } else {
                    dtxt = "";
                }

                a[0] = rs.getInt(1) + "";
                a[1] = rs.getString(2) + "";
                a[2] = rs.getString(3) + "";
                a[3] = dtxt;
                a[4] = rs.getString(5) + "";
                a[5] = rs.getString(6) + "";
                
                int index = jTable1.getSelectedRow();
                jTable1.setValueAt(a[0], index, 0);
                jTable1.setValueAt(a[1], index, 1);
                jTable1.setValueAt(a[2], index, 2);
                jTable1.setValueAt(a[3], index, 3);
                jTable1.setValueAt(a[4], index, 4);

            }
        } catch (SQLException ex) {
            Logger.getLogger(ResearchV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateRowTransaction(int trans)
    {
        tm.setValueAt(trans + "", jTable1.getSelectedRow(), 5);
        if(trans == 1) {
            borrow2.setVisible(false);
            jLabel2.setText("Research has been borrowed");
            animatePanel();
        } else {
            borrow2.setVisible(true);
            jLabel2.setText("Research has been returned");
            animatePanel();
        }
    }
    
    public void initList()
    {
        ResearchController rControl = new ResearchController();
                
        // get columns info
        ResultSetMetaData rsmd;
        ResultSet rs = rControl.getResearches();
        
        try {
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // clear existing columns 
            tm.setColumnCount(0);

            // add specified columns to table
            tm.addColumn("Research ID");
            tm.addColumn("Research Title");
            tm.addColumn("Research Description");
            tm.addColumn("Author");
            tm.addColumn("Published Date");
            tm.addColumn("Trans");
            
            // clear existing rows
            tm.setRowCount(0);

            // add rows to table
            while (rs.next()) {
                String[] a = new String[columnCount];
                String dtxt;
                
                if(rs.getTimestamp(4) != null) {
                    Date dt = rs.getTimestamp(4);
                    dtxt = new SimpleDateFormat("MMM dd, YYYY").format(dt) + "";
                } else {
                    dtxt = "";
                }
                
                a[0] = rs.getInt(1) + "";
                a[1] = rs.getString(2) + "";
                a[2] = rs.getString(3) + "";
                a[3] = dtxt;
                a[4] = rs.getString(5) + "";
                a[5] = rs.getString(6) + "";
                
                tm.addRow(a);
            }
            jTable1.removeColumn(jTable1.getColumnModel().getColumn(5));
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initList(String search)
    {
        ResearchController rControl = new ResearchController();
                
        // get columns info
        ResultSetMetaData rsmd;
        ResultSet rs = rControl.getResearches(search);
        
        try {
            rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // clear existing columns 
            tm.setColumnCount(0);

            // add specified columns to table
            for (int i = 1; i <= columnCount; i++ ) {
                tm.addColumn(rsmd.getColumnName(i));
            }
            
            // clear existing rows
            tm.setRowCount(0);

            // add rows to table
            while (rs.next()) {
                String[] a = new String[columnCount];
                String dtxt;
                
                if(rs.getTimestamp(4) != null) {
                    Date dt = rs.getTimestamp(4);
                    dtxt = new SimpleDateFormat("MMM dd, YYYY").format(dt) + "";
                } else {
                    dtxt = "";
                }
                
                a[0] = rs.getInt(1) + "";
                a[1] = rs.getString(2) + "";
                a[2] = rs.getString(3) + "";
                a[3] = dtxt;
                a[4] = rs.getString(5) + "";
                a[5] = rs.getString(6) + "";
                
                tm.addRow(a);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        borrow2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        borrow3 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jTextField1 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(33, 33, 33));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(159, 204, 204));
        jPanel4.setEnabled(false);
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel4MouseExited(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/images/recycle-bin.png"))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Delete Research");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel4)
                .addGap(28, 28, 28)
                .addComponent(jLabel5)
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 230, -1));

        jPanel5.setBackground(new java.awt.Color(159, 204, 204));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel5MouseExited(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/images/add-icon.png"))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Add Research");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel6)
                .addGap(29, 29, 29)
                .addComponent(jLabel7)
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 230, -1));

        jPanel6.setBackground(new java.awt.Color(159, 204, 204));
        jPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel6MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel6MouseExited(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/images/minus-gross-horizontal-straight-line-symbol.png"))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Edit Research");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel8)
                .addGap(28, 28, 28)
                .addComponent(jLabel9)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 230, -1));

        borrow2.setBackground(new java.awt.Color(159, 204, 204));
        borrow2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                borrow2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                borrow2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                borrow2MouseExited(evt);
            }
        });
        borrow2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/images/open-book.png"))); // NOI18N
        borrow2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 0, -1, 50));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("Borrow Research");
        borrow2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 0, -1, 50));

        jPanel1.add(borrow2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 230, -1));

        borrow3.setBackground(new java.awt.Color(159, 204, 204));
        borrow3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                borrow3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                borrow3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                borrow3MouseExited(evt);
            }
        });
        borrow3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/images/back-arrow.png"))); // NOI18N
        borrow3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 0, -1, 50));

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setText("Return Research");
        borrow3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, -3, -1, 50));

        jPanel1.add(borrow3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 230, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 480));

        jPanel3.setBackground(new java.awt.Color(33, 33, 33));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Research ID", "Research Title", "Research Description", "Author", "Published Date", "Transaction Type"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(204, 204, 204));
        jScrollPane1.setViewportView(jTable1);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 860, 380));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Search:");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 50, 30));
        jPanel3.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, 210, 10));

        jTextField1.setBackground(new java.awt.Color(33, 33, 33));
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setBorder(null);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
        jPanel3.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 210, -1));

        jPanel7.setBackground(new java.awt.Color(0, 153, 153));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Research Successfully Added");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/images/checked.png"))); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel3.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 30, 210, 30));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 900, 480));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
        // TODO add your handling code here:
        ResearchDialogSubmit researchDialogSubmit = new ResearchDialogSubmit(this, true);
        researchDialogSubmit.setDefaultCloseOperation(researchDialogSubmit.DISPOSE_ON_CLOSE);
        researchDialogSubmit.setVisible(true);
    }//GEN-LAST:event_jPanel5MouseClicked

    private void jPanel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseEntered
        // TODO add your handling code here:
        jPanel5.setOpaque(true);
        jPanel5.setBackground(new Color(149, 191, 191));        
    }//GEN-LAST:event_jPanel5MouseEntered

    private void jPanel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseExited
        // TODO add your handling code here:
        jPanel5.setOpaque(true);
        jPanel5.setBackground(new Color(159,204,204));
    }//GEN-LAST:event_jPanel5MouseExited

    private void jPanel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseEntered
        // TODO add your handling code here:
        jPanel6.setOpaque(true);
        jPanel6.setBackground(new Color(149, 191, 191)); 
    }//GEN-LAST:event_jPanel6MouseEntered

    private void jPanel6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseExited
        // TODO add your handling code here:
        jPanel6.setOpaque(true);
        jPanel6.setBackground(new Color(159,204,204));
    }//GEN-LAST:event_jPanel6MouseExited

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        // TODO add your handling code here:
        if(jTable1.getSelectedRows().length == 0) return;
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like Delete","Warning",dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
          // Saving code here
        } else {
            System.out.println("No");
            return;
        }
        
        int[] index = jTable1.getSelectedRows();
        long[] id = new long[index.length];
        
        int i = 0;
        for(int ind : index) {
            id[i] = Integer.parseInt(jTable1.getModel().getValueAt(ind, 0).toString());
            i++;
        }
        
        ResearchController researchController = new ResearchController();
        int[] res = researchController.deleteResearch(id);
        
        if(res.length > 0) {
            jTable1.getSelectionModel().clearSelection();
            for (int j = index.length - 1; j >= 0; j--) {
                tm.removeRow(index[j]);
            }
            
            jLabel2.setText("Research has been deleted");
            animatePanel();
        }
    }//GEN-LAST:event_jPanel4MouseClicked

    private void jPanel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseEntered
        // TODO add your handling code here:
        jPanel4.setOpaque(true);
        jPanel4.setBackground(new Color(149, 191, 191));
    }//GEN-LAST:event_jPanel4MouseEntered

    private void jPanel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseExited
        // TODO add your handling code here:
        jPanel4.setOpaque(true);
        jPanel4.setBackground(new Color(159,204,204));
    }//GEN-LAST:event_jPanel4MouseExited

    private void jPanel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseClicked
        // TODO add your handling code here:
        if(jTable1.getSelectedRows().length == 0) return;
        
        int index = jTable1.getSelectedRow();
        int id = Integer.parseInt(jTable1.getModel().getValueAt(index, 0).toString());
        
        ResearchDialogSubmit researchDialogSubmit = new ResearchDialogSubmit(id, this, true);
        researchDialogSubmit.setDefaultCloseOperation(researchDialogSubmit.DISPOSE_ON_CLOSE);
        researchDialogSubmit.setVisible(true);
    }//GEN-LAST:event_jPanel6MouseClicked

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            initList(jTextField1.getText());
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void borrow2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrow2MouseClicked
        // TODO add your handling code here:
        if(jTable1.getSelectedRows().length == 0) return;
        
        int index = jTable1.getSelectedRow();
        int id = Integer.parseInt(jTable1.getModel().getValueAt(index, 0).toString());
        String title = jTable1.getModel().getValueAt(index, 1).toString();
        
        BorrowDialog dialog = new BorrowDialog(id, title, 1, this, true);
        dialog.setDefaultCloseOperation(dialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }//GEN-LAST:event_borrow2MouseClicked

    private void borrow2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrow2MouseEntered
        // TODO add your handling code here:
        borrow2.setOpaque(true);
        borrow2.setBackground(new Color(149, 191, 191));   
    }//GEN-LAST:event_borrow2MouseEntered

    private void borrow2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrow2MouseExited
        // TODO add your handling code here:
        borrow2.setOpaque(true);
        borrow2.setBackground(new Color(159,204,204));   
    }//GEN-LAST:event_borrow2MouseExited

    private void borrow3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrow3MouseClicked
        // TODO add your handling code here:
        if(jTable1.getSelectedRows().length == 0) return;
        
        int index = jTable1.getSelectedRow();
        int id = Integer.parseInt(jTable1.getModel().getValueAt(index, 0).toString());
        String title = jTable1.getModel().getValueAt(index, 1).toString();
        
        BorrowDialog dialog = new BorrowDialog(id, title, 2, this, true);
        dialog.setDefaultCloseOperation(dialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }//GEN-LAST:event_borrow3MouseClicked

    private void borrow3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrow3MouseEntered
        // TODO add your handling code here:
        borrow3.setOpaque(true);
        borrow3.setBackground(new Color(149, 191, 191));   
    }//GEN-LAST:event_borrow3MouseEntered

    private void borrow3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrow3MouseExited
        // TODO add your handling code here:
        borrow3.setOpaque(true);
        borrow3.setBackground(new Color(159,204,204));   
    }//GEN-LAST:event_borrow3MouseExited

   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Research.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Research.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Research.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Research.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ResearchV().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel borrow2;
    private javax.swing.JPanel borrow3;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}

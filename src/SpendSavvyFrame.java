
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.RowFilter;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author giomjda
 */
public class SpendSavvyFrame extends javax.swing.JFrame {

    private TableRowSorter<DefaultTableModel> sorter;

    /**
     * Creates new form SpendSavvyFrame
     */
    public SpendSavvyFrame() {
        initComponents();
        initButtonListeners();
        setupSearch();
        
        setLocationRelativeTo(null);
    }

    private void initButtonListeners() {
        addExpenseBtn.addActionListener(e -> addExpense());
        editExpenseBtn.addActionListener(e -> editExpense());
        removeExpenseBtn.addActionListener(e -> removeExpense());
    }

    private void setupSearch() {
        DefaultTableModel model = (DefaultTableModel) expenseListTable.getModel();

        sorter = new TableRowSorter<>(model);
        expenseListTable.setRowSorter(sorter);

        searchCategoryTextPane.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });
    }

    private void filterTable() {
        String searchText = searchCategoryTextPane.getText().trim();

        if (searchText.isEmpty()) {
            sorter.setRowFilter(null);
        }

        List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();

        filters.add(RowFilter.regexFilter("(?i)" + searchText, 0));
        filters.add(RowFilter.regexFilter("(?i)" + searchText, 2));

        try {
            Double.parseDouble(searchText.replace(",", "").replace("P", ""));
            filters.add(RowFilter.regexFilter(searchText, 1));
        } catch (Exception e) {
            // Maya na
        }

        RowFilter<DefaultTableModel, Integer> combinedFilter = RowFilter.orFilter(filters);
        sorter.setRowFilter(combinedFilter);
    }

    private void addExpense() {
        String category = (String) categoryComboBox.getSelectedItem();
        Double amount = ((Number) amountFormatTextField.getValue()).doubleValue();
        String description = descriptionTextArea.getText();

        DefaultTableModel model = (DefaultTableModel) expenseListTable.getModel();
        model.addRow(new Object[]{category, amount, description});

        if (sorter != null) {
            sorter.sort();
        }

        descriptionTextArea.setText("");
        updateTotalExpenses();
    }

    private void editExpense() {
        int row = expenseListTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense entry to edit.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) expenseListTable.getModel();

        String category = (String) model.getValueAt(row, 0);
        Double amount;

        try {
            Object val = model.getValueAt(row, 1);
            if (val instanceof Number) {
                amount = ((Number) val).doubleValue();
            } else {
                amount = Double.parseDouble(val.toString());
            }
        } catch (Exception e) {
            amount = 0.0;
        }

        String description = (String) model.getValueAt(row, 2);

        JPanel editPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField amountField = new JTextField(amount.toString());
        JTextField categoryField = new JTextField(category);
        JTextArea descArea = new JTextArea(description, 3, 20);
        JScrollPane descScroll = new JScrollPane(descArea);

        editPanel.add(new JLabel("Category:"));
        editPanel.add(categoryField);
        editPanel.add(new JLabel("Amount:"));
        editPanel.add(amountField);
        editPanel.add(new JLabel("Description:"));
        editPanel.add(descScroll);

        int result = JOptionPane.showConfirmDialog(this, editPanel, "Edit Expense Entry",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                double newAmount = Double.parseDouble(amountField.getText());
                String newCategory = categoryField.getText();
                String newDescription = descArea.getText();

                model.setValueAt(newCategory, row, 0);
                model.setValueAt(newAmount, row, 1);
                model.setValueAt(newDescription, row, 2);

                updateTotalExpenses();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error in input values. Please check and try again.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        if (sorter != null) {
            sorter.sort();
        }
    }

    private void removeExpense() {
        int row = expenseListTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense entry to remove.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove the selected expense?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) expenseListTable.getModel();
            model.removeRow(row);
            updateTotalExpenses();
        }
        
        if (sorter != null) {
            sorter.sort();
        }
    }

    private void updateTotalExpenses() {
        DefaultTableModel model = (DefaultTableModel) expenseListTable.getModel();
        double total = 0.0;

        for (int i = 0; i < expenseListTable.getRowCount(); i++) {
            int modelRow = expenseListTable.convertRowIndexToModel(i);
            Object val = model.getValueAt(modelRow, 1);
            if (val instanceof Number) {
                total += ((Number) val).doubleValue();
            } else if (val != null) {
                try {
                    total += Double.parseDouble(val.toString());
                } catch (Exception e) {
                    // Skip if not parseable
                }
            }
        }

        String formattedLabel = String.format("P%,.2f", total);
        totalExpensesLabel.setText(formattedLabel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        addExpenseBtn = new javax.swing.JButton();
        editExpenseBtn = new javax.swing.JButton();
        removeExpenseBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        expenseListTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        totalExpensesLabel = new javax.swing.JLabel("P0.00");
        categoryComboBox = new javax.swing.JComboBox<>();
        descriptionTextArea = new javax.swing.JTextArea();
        NumberFormatter formatter = new NumberFormatter();
        formatter.setValueClass(Double.class);
        formatter.setAllowsInvalid(false);
        formatter.setMinimum(0.0);
        amountFormatTextField = new javax.swing.JFormattedTextField(formatter);
        searchCategoryTextPane = new javax.swing.JTextPane();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(getPreferredSize());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("Expense Entry");

        jLabel5.setFont(new java.awt.Font("Segoe UI Historic", 1, 22)); // NOI18N
        jLabel5.setText("Amount :");

        jLabel6.setFont(new java.awt.Font("Segoe UI Historic", 1, 22)); // NOI18N
        jLabel6.setText("Category :");

        jLabel7.setFont(new java.awt.Font("Segoe UI Historic", 1, 22)); // NOI18N
        jLabel7.setText("Description :");

        addExpenseBtn.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        addExpenseBtn.setText("Add");
        addExpenseBtn.setToolTipText("");

        editExpenseBtn.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        editExpenseBtn.setText("Edit");

        removeExpenseBtn.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        removeExpenseBtn.setText("Remove");

        jLabel1.setFont(new java.awt.Font("Segoe UI Historic", 2, 18)); // NOI18N
        jLabel1.setText("\"Empowering informed and smart spending decisions!\"");

        jLabel2.setFont(new java.awt.Font("Segoe UI Historic", 1, 24)); // NOI18N
        jLabel2.setText("Welcome to SpendSavvy!");

        jScrollPane1.setToolTipText("");
        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        expenseListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Category", "Amount", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(expenseListTable);

        jLabel8.setFont(new java.awt.Font("Segoe UI Historic", 1, 24)); // NOI18N
        jLabel8.setText("SUMMARY PANEL");

        jLabel10.setFont(new java.awt.Font("Segoe UI Historic", 1, 22)); // NOI18N
        jLabel10.setText("Total Expense :");

        totalExpensesLabel.setFont(new java.awt.Font("Segoe UI Historic", 1, 36)); // NOI18N
        totalExpensesLabel.setText(null);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(jLabel8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(totalExpensesLabel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalExpensesLabel)
                .addGap(0, 161, Short.MAX_VALUE))
        );

        categoryComboBox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        categoryComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Food & Dining", "Transportation", "Utilities", "Housing", "Health & Medical", "Entertainment", "Shopping", "Education", "Savings", "Debt Payments", "Gifts & Donations", "Others / Misc." }));
        categoryComboBox.setToolTipText("Select your category here");

        descriptionTextArea.setColumns(20);
        descriptionTextArea.setRows(5);
        descriptionTextArea.setToolTipText("Place your expense description here");

        amountFormatTextField.setColumns(10);
        amountFormatTextField.setFont(new java.awt.Font("Segoe UI", 0, 16));
        amountFormatTextField.setValue(0.0);
        amountFormatTextField.setToolTipText("Put an desired amount here");

        searchCategoryTextPane.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        searchCategoryTextPane.setToolTipText("Search by category, amount, or description");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setText("Search");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(addExpenseBtn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(editExpenseBtn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(removeExpenseBtn)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(167, 296, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(28, 28, 28)
                                        .addComponent(categoryComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(41, 41, 41)
                                        .addComponent(amountFormatTextField))
                                    .addComponent(descriptionTextArea, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9)
                                .addComponent(searchCategoryTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(16, 16, 16))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addContainerGap(163, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(searchCategoryTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addComponent(amountFormatTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(descriptionTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addExpenseBtn)
                            .addComponent(editExpenseBtn)
                            .addComponent(removeExpenseBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(SpendSavvyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SpendSavvyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SpendSavvyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SpendSavvyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SpendSavvyFrame frame = new SpendSavvyFrame();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addExpenseBtn;
    private javax.swing.JFormattedTextField amountFormatTextField;
    private javax.swing.JComboBox<String> categoryComboBox;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JButton editExpenseBtn;
    private javax.swing.JTable expenseListTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeExpenseBtn;
    private javax.swing.JTextPane searchCategoryTextPane;
    private javax.swing.JLabel totalExpensesLabel;
    // End of variables declaration//GEN-END:variables
}

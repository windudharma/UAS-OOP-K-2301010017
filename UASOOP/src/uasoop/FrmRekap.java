/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package uasoop;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import static uasoop.dbkoneksi.koneksi;

/**
 *
 * @author windudharma
 */
public class FrmRekap extends javax.swing.JFrame {
    DefaultTableModel TP = new DefaultTableModel();
    /**
     * Creates new form FrmRekap
     */
    public FrmRekap()throws SQLException {
        initComponents();

    monthChooser.addPropertyChangeListener(new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            try {
                dtPengeluaran();
            } catch (SQLException ex) {
                Logger.getLogger(FrmRekap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });

    // Fungsi lainnya

        
        
        tgl.setDate(new java.util.Date(System.currentTimeMillis()));  // Mengatur tanggal saat ini
        JTextField dateTextField = (JTextField) tgl.getDateEditor().getUiComponent();
        dateTextField.setEditable(false); // Menonaktifkan hanya input teks


        Connection cnn = koneksi();
        
        TPENG.setModel(TP);
        TP.addColumn("No");
        TP.addColumn("Deskripsi");
        TP.addColumn("Jumlah");
        TP.addColumn("Kategori");        
        TP.addColumn("Tanggal");
        TP.addColumn("id");

        
        
        this.dtPengeluaran();

        
        fieldisEnabled(false);
        tombolisEnabled(false);
        cmdTambah.setEnabled(true);        
        cmdTutup.setEnabled(true);
        hideColumn(5);


    
    }
    
    private void fieldisEnabled(boolean opsi){
        txdeskripsi.setEditable(opsi);
        txjumlah.setEditable(opsi);
        bxkategori.setEnabled(opsi);
        tgl.setEnabled(opsi);
        
    }
        
    private void tombolisEnabled(boolean opsi){
        cmdTambah.setEnabled(opsi);
        cmdUbah.setEnabled(opsi);
        cmdHapus.setEnabled(opsi);
    }
    
    private void hideColumn(int columnIndex) {
    TableColumnModel columnModel = TPENG.getColumnModel();
    TableColumn column = columnModel.getColumn(columnIndex);
    
    // Menyembunyikan kolom dengan indeks yang diberikan
    column.setMaxWidth(0);
    column.setMinWidth(0);
    column.setPreferredWidth(0);
    column.setResizable(false);
}

    
    private void resetForm(){
        txdeskripsi.setText("");
        txjumlah.setText("");
        bxkategori.setSelectedIndex(0);
        tgl.setDate(new java.util.Date(System.currentTimeMillis()));  // Mengatur tanggal saat ini
    }
            
    
       private void storeData() throws SQLException{
        
       String deskripsi = txdeskripsi.getText();
       int jumlah = Integer.parseInt(txjumlah.getText());  // Mengonversi jumlah dari String ke int
       String kategori = (String) bxkategori.getSelectedItem();  // Mengambil kategori yang dipilih dari JComboBox
       java.util.Date tanggalUtil = tgl.getDate();  // Mengambil tanggal dari JDateChooser
   
       // Mengonversi java.util.Date ke java.sql.Date
       java.sql.Date tanggal = new java.sql.Date(tanggalUtil.getTime());  // Menggunakan getTime() untuk mendapatkan milisecond
      
       

       Connection cnn = koneksi();
       PreparedStatement PS = cnn.prepareStatement("INSERT INTO tblpengeluaran (deskripsi, jumlah, kategori, tanggal) VALUES (?, ?, ?, ?)");
       PS.setString(1, deskripsi);
       PS.setInt(2, jumlah);
       PS.setString(3, kategori);
       PS.setDate(4, tanggal);
       PS.executeUpdate();
    }
       
    private void updateData() throws SQLException {
        // Ambil data dari form
        String deskripsi = txdeskripsi.getText();
        int jumlah = Integer.parseInt(txjumlah.getText());  // Mengonversi jumlah dari String ke int
        String kategori = (String) bxkategori.getSelectedItem();  // Mengambil kategori yang dipilih dari JComboBox
        java.util.Date tanggalUtil = tgl.getDate();  // Mengambil tanggal dari JDateChooser

        // Mengonversi java.util.Date ke java.sql.Date
        java.sql.Date tanggal = new java.sql.Date(tanggalUtil.getTime());  // Menggunakan getTime() untuk mendapatkan milisecond

        // Mendapatkan idpengeluaran dari row yang dipilih di JTable
        int selectedRow = TPENG.getSelectedRow(); // Mendapatkan row yang dipilih di JTable
        int idpengeluaran = (int) TPENG.getValueAt(selectedRow, 5); // Ambil idpengeluaran yang ada di kolom ke-5 (indeks 5)

        // Membuat koneksi dan pernyataan SQL
        Connection cnn = koneksi();
        PreparedStatement PS = cnn.prepareStatement("UPDATE tblpengeluaran SET deskripsi = ?, jumlah = ?, kategori = ?, tanggal = ? WHERE idpengeluaran = ?");

        // Set parameter untuk prepared statement
        PS.setString(1, deskripsi);
        PS.setInt(2, jumlah);
        PS.setString(3, kategori);
        PS.setDate(4, tanggal);
        PS.setInt(5, idpengeluaran);  
        PS.executeUpdate();
    }

    
    
    private void deleteData() throws SQLException {
        // Mendapatkan baris yang dipilih dari JTable
        int selectedRow = TPENG.getSelectedRow();

        if (selectedRow != -1) {  // Pastikan ada baris yang dipilih
            // Mengambil idpengeluaran dari kolom ke-5 (indeks 5) di baris yang dipilih
            int idpengeluaran = (int) TPENG.getValueAt(selectedRow, 5);

            try {
                // Menyiapkan koneksi dan statement untuk menghapus data
                Connection cnn = koneksi();
                PreparedStatement PS = cnn.prepareStatement("DELETE FROM tblpengeluaran WHERE idpengeluaran = ?");
                PS.setInt(1, idpengeluaran);  // Menggunakan idpengeluaran untuk query

                // Menjalankan query untuk menghapus data
                PS.executeUpdate();

                // Memberi feedback kepada pengguna bahwa data telah dihapus
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");

                // Mengupdate tampilan data di JTable setelah penghapusan
                dtPengeluaran();  // Menampilkan ulang data setelah dihapus
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus.");
        }

    }



           
private void dtPengeluaran() throws SQLException {
    int selectedMonth = monthChooser.getMonth() + 1;
    String selectedKategori = filkategori.getSelectedItem().toString();

    Connection cnn = koneksi();
    PreparedStatement PS;

    if (selectedKategori.equalsIgnoreCase("Semua Kategori")) {
        PS = cnn.prepareStatement("SELECT * FROM tblpengeluaran WHERE MONTH(tanggal) = ?");
        PS.setInt(1, selectedMonth);
    } else {
        PS = cnn.prepareStatement("SELECT * FROM tblpengeluaran WHERE MONTH(tanggal) = ? AND kategori = ?");
        PS.setInt(1, selectedMonth);
        PS.setString(2, selectedKategori);
    }

    ResultSet RS = PS.executeQuery();

    TP.getDataVector().removeAllElements();
    TP.fireTableDataChanged();

    int nomor = 1;
    while (RS.next()) {
        Object[] dta = new Object[6];
        dta[0] = nomor++;
        dta[1] = RS.getString("deskripsi");
        dta[2] = RS.getString("jumlah");
        dta[3] = RS.getString("kategori");
        dta[4] = RS.getString("tanggal");
        dta[5] = RS.getInt("idpengeluaran");

        TP.addRow(dta);
    }

    hitungTotal();
}



private void hitungTotal() {
    int total = 0;

    // Pastikan tabel tidak kosong sebelum menghitung
    if (TP.getRowCount() == 0) {
        totalLabel.setText("Total: 0");
        return;  // Jika tidak ada data, keluar dari fungsi
    }

    // Looping untuk setiap baris di tabel dan menjumlahkan kolom "Jumlah" (indeks 2)
    for (int i = 0; i < TP.getRowCount(); i++) {
        try {
            total += Integer.parseInt(TP.getValueAt(i, 2).toString());
        } catch (NumberFormatException e) {
            // Tangani jika terjadi kesalahan saat konversi
            System.err.println("Error parsing number at row " + i);
        }
    }

    // Update label total dengan nilai total
    totalLabel.setText("Total Pengeluaran: Rp." + total);
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        bxkategori = new javax.swing.JComboBox<>();
        txdeskripsi = new javax.swing.JTextField();
        txjumlah = new javax.swing.JTextField();
        tgl = new com.toedter.calendar.JDateChooser();
        cmdUbah = new javax.swing.JButton();
        cmdHapus = new javax.swing.JButton();
        cmdTutup = new javax.swing.JButton();
        cmdTambah = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        monthChooser = new com.toedter.calendar.JMonthChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        TPENG = new javax.swing.JTable();
        filkategori = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("Deskripsi");

        jLabel3.setText("Jumlah");

        jLabel4.setText("Kategori");

        jLabel5.setText("Tanggal");

        bxkategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Makan & Minum", "Belanja", "Transportasi", "Tagihan", "Kategori Lainnya"
        }));
        bxkategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bxkategoriActionPerformed(evt);
            }
        });

        txjumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txjumlahActionPerformed(evt);
            }
        });
        txjumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txjumlahKeyTyped(evt);
            }
        });

        cmdUbah.setText("UBAH");
        cmdUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUbahActionPerformed(evt);
            }
        });

        cmdHapus.setText("HAPUS");
        cmdHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdHapusActionPerformed(evt);
            }
        });

        cmdTutup.setText("TUTUP");
        cmdTutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTutupActionPerformed(evt);
            }
        });

        cmdTambah.setText("TAMBAH");
        cmdTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTambahActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(51, 51, 255));

        jLabel1.setFont(new java.awt.Font("Sinhala MN", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("APLIKASI PENGELOLAAN PENGELUARAN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(jLabel1)
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(44, 44, 44))
        );

        totalLabel.setFont(new java.awt.Font("Hiragino Maru Gothic ProN", 0, 14)); // NOI18N
        totalLabel.setText("Total : ");

        monthChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                monthChooserPropertyChange(evt);
            }
        });

        TPENG.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TPENG.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TPENGMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TPENG);

        filkategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Kategori", "Makan & Minum", "Belanja", "Transportasi", "Tagihan", "Kategori Lainnya" }));
        filkategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filkategoriActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(tgl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bxkategori, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txjumlah, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txdeskripsi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmdTambah, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdUbah, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdHapus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdTutup, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(filkategori, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(monthChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(63, 63, 63))
            .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(totalLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(65, 65, 65))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txdeskripsi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txjumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bxkategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdTutup, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(60, 60, 60)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalLabel)
                            .addComponent(filkategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(monthChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTambahActionPerformed
        if(cmdTambah.getText().equals("TAMBAH")){
            cmdTambah.setText("SIMPAN");
            cmdTutup.setText("BATAL");
            resetForm();
            fieldisEnabled(true);
            tombolisEnabled(false);
            cmdTambah.setEnabled(true);
        }else{
            try {
            cmdTambah.setText("TAMBAH");
            cmdTutup.setText("TUTUP");
            
            fieldisEnabled(false);
            tombolisEnabled(false);
            cmdTambah.setEnabled(true);
                        
            storeData();
            dtPengeluaran();
            resetForm();
        } catch (SQLException ex) {
            Logger.getLogger(FrmRekap.class.getName()).log(Level.SEVERE, null, ex);
           }
        }

    }//GEN-LAST:event_cmdTambahActionPerformed

    private void cmdTutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTutupActionPerformed
        if(cmdTutup.getText().equals("TUTUP")){
        int jopsi = JOptionPane.showOptionDialog(this, 
                "Yakin akan menutup Aplikasi?", 
                "Konfirmasi Tutup Aplikasi", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, null, null);
        
        if(jopsi== JOptionPane.YES_OPTION){
            System.exit(0);
        }
        }else{
            resetForm();
            fieldisEnabled(false);
            tombolisEnabled(false);
            cmdTambah.setEnabled(true);
            
            cmdTambah.setText("TAMBAH");
            cmdTutup.setText("TUTUP");
            cmdUbah.setText("UBAH");

            
        }        
    }//GEN-LAST:event_cmdTutupActionPerformed

    private void cmdHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdHapusActionPerformed
        int jopsi = JOptionPane.showOptionDialog(this, 
                "Yakin akan Menghapus Data ?", 
                "Konfirmasi Hapus Data", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, null, null); 
        
        
        if(jopsi== JOptionPane.YES_OPTION){
            try {
                deleteData();
                dtPengeluaran();
                
                resetForm();
                cmdUbah.setEnabled(false);
                cmdHapus.setEnabled(false);
            } catch (SQLException ex) {
                Logger.getLogger(FrmRekap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cmdHapusActionPerformed

    private void cmdUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUbahActionPerformed
        if(cmdUbah.getText().equals("UBAH")){
            cmdUbah.setText("SIMPAN");
            cmdTutup.setText("BATAL");
            fieldisEnabled(true);
            tombolisEnabled(false);
            cmdUbah.setEnabled(true);
        }else{
            try {
            cmdUbah.setText("UBAH");
            cmdTutup.setText("TUTUP");
  
            fieldisEnabled(false);
            tombolisEnabled(false);
            cmdTambah.setEnabled(true);
            
            
            updateData();
            dtPengeluaran();
            resetForm();
        } catch (SQLException ex) {
            Logger.getLogger(FrmRekap.class.getName()).log(Level.SEVERE, null, ex);
           }
        }

    }//GEN-LAST:event_cmdUbahActionPerformed

    private void bxkategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bxkategoriActionPerformed

    }//GEN-LAST:event_bxkategoriActionPerformed

    private void txjumlahKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txjumlahKeyTyped
        char c = evt.getKeyChar();

        // Mengecek apakah karakter yang dimasukkan adalah angka, backspace atau delete
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
            evt.consume();  // Mencegah input selain angka, backspace atau delete
        }
    }//GEN-LAST:event_txjumlahKeyTyped

    private void txjumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txjumlahActionPerformed
        
    }//GEN-LAST:event_txjumlahActionPerformed

    private void TPENGMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TPENGMouseClicked
        // Mendapatkan indeks baris yang dipilih
        int selectedRow = TPENG.getSelectedRow();

        txdeskripsi.setText(TPENG.getValueAt(selectedRow, 1).toString());
        txjumlah.setText(TPENG.getValueAt(selectedRow, 2).toString());
        bxkategori.setSelectedItem(TPENG.getValueAt(selectedRow, 3).toString());

        String tanggalStr = TPENG.getValueAt(selectedRow, 4).toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date tanggal = sdf.parse(tanggalStr);
            tgl.setDate(tanggal);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tombolisEnabled(true);

    }//GEN-LAST:event_TPENGMouseClicked

    private void monthChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_monthChooserPropertyChange

    }//GEN-LAST:event_monthChooserPropertyChange

    private void filkategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filkategoriActionPerformed
    try {
        dtPengeluaran(); // Panggil ulang method ini, tapi modifikasi dulu isinya
    } catch (SQLException ex) {
        Logger.getLogger(FrmRekap.class.getName()).log(Level.SEVERE, null, ex);
    }
    }//GEN-LAST:event_filkategoriActionPerformed

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
            java.util.logging.Logger.getLogger(FrmRekap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmRekap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmRekap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmRekap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new FrmRekap().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(FrmRekap.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TPENG;
    private javax.swing.JComboBox<String> bxkategori;
    private javax.swing.JButton cmdHapus;
    private javax.swing.JButton cmdTambah;
    private javax.swing.JButton cmdTutup;
    private javax.swing.JButton cmdUbah;
    private javax.swing.JComboBox<String> filkategori;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JMonthChooser monthChooser;
    private com.toedter.calendar.JDateChooser tgl;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JTextField txdeskripsi;
    private javax.swing.JTextField txjumlah;
    // End of variables declaration//GEN-END:variables
}

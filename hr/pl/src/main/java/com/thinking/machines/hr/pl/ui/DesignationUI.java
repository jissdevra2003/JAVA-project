package com.thinking.machines.hr.pl.ui;
import com.thinking.machines.hr.pl.model.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;
public class DesignationUI extends JFrame implements DocumentListener,ListSelectionListener
{
private JTable designationTable;
private JLabel designationsLabel;
private JLabel searchLabel;
private JLabel searchErrorLabel;
private JTextField searchTextField;
private JButton clearSearchTextFieldButton;
private ImageIcon clearSearchIcon;
private DesignationModel designationModel;
private JScrollPane scrollPane;
private Container container;
private DesignationPanel designationPanel;
private enum MODE{VIEW,ADD,DELETE,EDIT,EXPORT_TO_PDF};
private MODE mode;
public DesignationUI()
{
initComponents();
setAppearance();
addListeners();
setViewMode();
designationPanel.setViewMode();
}

private void initComponents()
{
clearSearchIcon=new ImageIcon(this.getClass().getResource("/image_files/clear_search_icon48.png"));
designationModel=new DesignationModel();
designationsLabel=new JLabel("Designations");
searchLabel=new JLabel("Search");
searchTextField=new JTextField();
clearSearchTextFieldButton=new JButton(clearSearchIcon);
searchErrorLabel=new JLabel("");
designationTable=new JTable(designationModel);
scrollPane=new JScrollPane(designationTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
container=getContentPane();
designationPanel=new DesignationPanel();

}
private void setAppearance()
{
Font titleFont=new Font("Calibri",Font.BOLD,20);
Font searchFont=new Font("Verdana",Font.BOLD,14);
Font dataFont=new Font("Verdana",Font.PLAIN,16);
Font searchErrorLabelFont=new Font("Verdana",Font.PLAIN,11);
Font headerFont=new Font("Arial",Font.BOLD,16);

designationsLabel.setFont(titleFont);
searchLabel.setFont(searchFont);
searchTextField.setFont(dataFont);
searchErrorLabel.setFont(searchErrorLabelFont);
designationTable.setFont(dataFont);
searchErrorLabel.setForeground(Color.red);
designationTable.setRowHeight(30);
designationTable.getColumnModel().getColumn(0).setPreferredWidth(20);
designationTable.getColumnModel().getColumn(1).setPreferredWidth(400);
JTableHeader header=designationTable.getTableHeader();
header.setFont(headerFont);
designationTable.setRowSelectionAllowed(true);
designationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
header.setReorderingAllowed(false);
header.setResizingAllowed(false);


container.setLayout(null);
int leftMargin,topMargin;
leftMargin=0;
topMargin=0;
designationsLabel.setBounds(leftMargin+10,topMargin+10,200,40);
searchLabel.setBounds(leftMargin+10,topMargin+10+10+20,100,40);
searchTextField.setBounds(leftMargin+10+100+5,topMargin+10+20+10+5,400,30);
clearSearchTextFieldButton.setBounds(leftMargin+10+110+400+5+3,topMargin+10+20+10+3,48,35);
scrollPane.setBounds(leftMargin+10,topMargin+10+50+20,568,280);
designationPanel.setBounds(leftMargin+10,topMargin+10+70+280+10,568,180);
searchErrorLabel.setBounds(leftMargin+57+400,topMargin+20,100,20);


container.add(designationsLabel);
container.add(searchLabel);
container.add(searchTextField);
container.add(clearSearchTextFieldButton);
container.add(scrollPane);
container.add(designationPanel);
container.add(searchErrorLabel);
int width=600;
int height=600;
setSize(width,height);
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
int x=(d.width/2)-(width/2);
int y=(d.height/2)-(height/2);
setLocation(x,y);

}
private void searchDesignation()
{
searchErrorLabel.setText("");
String title=searchTextField.getText().trim();
if(title.length()==0) return;
int rowIndex;
try
{
rowIndex=designationModel.indexOfTitle(title,true);
}
catch(BLException blException)
{
searchErrorLabel.setText("Not found");
return;
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rectangle);

}
private void addListeners()
{
searchTextField.getDocument().addDocumentListener(this);
clearSearchTextFieldButton.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent ev)
{
searchTextField.setText("");
searchTextField.requestFocus();
}
});
designationTable.getSelectionModel().addListSelectionListener(this);
setDefaultCloseOperation(EXIT_ON_CLOSE);
}
public void changedUpdate(DocumentEvent de)
{
searchDesignation();
}
public void removeUpdate(DocumentEvent de)
{
searchDesignation();
}
public void insertUpdate(DocumentEvent de)
{
searchDesignation();
}
public void valueChanged(ListSelectionEvent ev)
{
int selectedRowIndex=designationTable.getSelectedRow();
try
{
DesignationInterface designation=designationModel.getDesignationAt(selectedRowIndex);
designationPanel.setDesignation(designation);
}catch(BLException blException)
{
designationPanel.clearDesignation();
}
}
private void setViewMode()
{
this.mode=MODE.VIEW;
if(designationModel.getRowCount()==0)
{
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
else 
{
searchTextField.setEnabled(true);
clearSearchTextFieldButton.setEnabled(true);
designationTable.setEnabled(true);
}
}
private void setAddMode()
{
this.mode=MODE.ADD;
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
private void setEditMode()
{
this.mode=MODE.EDIT;
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
private void setDeleteMode()
{
this.mode=MODE.DELETE;
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
private void setExportToPDFMode()
{
this.mode=MODE.EXPORT_TO_PDF;
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}

//inner class start DesignationPanel
private class DesignationPanel extends JPanel
{
private JLabel designationCaptionLabel;
private JTextField titleTextField;
private JButton clearTitleTextFieldButton;
private JButton addButton;
private JButton editButton;
private JButton cancelButton;
private JButton deleteButton;
private JButton exportToPDFButton;
private JPanel buttonsPanel;
private JLabel addCaptionLabel;
private ImageIcon addIcon;
private ImageIcon editIcon;
private ImageIcon cancelIcon;
private ImageIcon deleteIcon;
private ImageIcon exportToPDFIcon;
private ImageIcon saveIcon;
private ImageIcon logoIcon;
private DesignationInterface designation;
DesignationPanel()
{
setBorder(BorderFactory.createLineBorder(new Color(150,150,150)));
initComponents();
setAppearance();
addListeners();
}
public void setDesignation(DesignationInterface designation)
{
this.designation=designation;
titleTextField.setText(designation.getTitle());
}
public void clearDesignation()
{
this.designation=null;
titleTextField.setText("");
}
private void initComponents()
{
logoIcon=new ImageIcon(getClass().getResource("/image_files/logo_icon48.png"));
setIconImage(logoIcon.getImage());
addIcon=new ImageIcon(getClass().getResource("/image_files/add_icon48-1.png"));
editIcon=new ImageIcon(getClass().getResource("/image_files/edit_icon48-1.png"));
deleteIcon=new ImageIcon(getClass().getResource("/image_files/delete_icon48-1.png"));
cancelIcon=new ImageIcon(getClass().getResource("/image_files/cancel_icon48-1.png"));
saveIcon=new ImageIcon(getClass().getResource("/image_files/save_icon48-1.png"));
exportToPDFIcon=new ImageIcon(getClass().getResource("/image_files/pdf_icon32.png"));

designation=null;
designationCaptionLabel=new JLabel("Designation");
titleTextField=new JTextField();
clearTitleTextFieldButton=new JButton("X");
addButton=new JButton(addIcon);
editButton=new JButton(editIcon);
cancelButton=new JButton(cancelIcon);
deleteButton=new JButton(deleteIcon);
addCaptionLabel=new JLabel("Enter designation to add :");
exportToPDFButton=new JButton(exportToPDFIcon);
buttonsPanel=new JPanel();

}
private void setAppearance()
{
Font CaptionFont=new Font("Arial",Font.BOLD,18);
Font dataFont=new Font("Arial",Font.PLAIN,16);
Font addCaptionLabelFont=new Font("Arial",Font.PLAIN,12);
designationCaptionLabel.setFont(CaptionFont);
addCaptionLabel.setFont(addCaptionLabelFont);
setLayout(null);
int leftMargin=0,topMargin=0;

designationCaptionLabel.setBounds(leftMargin+10,topMargin+10+5,150,20);
titleTextField.setBounds(leftMargin+10+120,topMargin+10+5+5+5,350,20);
clearTitleTextFieldButton.setBounds(leftMargin+350+10+140,topMargin+15+10,35,20);
addCaptionLabel.setBounds(leftMargin+10+140,topMargin+7,200,10);
buttonsPanel.setLayout(null);
buttonsPanel.setBorder(BorderFactory.createLineBorder(new Color(165,165,165)));
buttonsPanel.setBounds(leftMargin+20,topMargin+15+50,523,100);
addButton.setBounds(leftMargin+25+30+25,topMargin+27,56,56);
editButton.setBounds(leftMargin+25+60+30+45,topMargin+27,56,56);
cancelButton.setBounds(leftMargin+25+60+60+30+20+45,topMargin+27,56,56);
deleteButton.setBounds(leftMargin+25+60+60+60+30+20+20+45,topMargin+27,56,56);
exportToPDFButton.setBounds(leftMargin+25+240+30+20+20+65,topMargin+27,56,56);

buttonsPanel.add(addButton);
buttonsPanel.add(editButton);
buttonsPanel.add(cancelButton);
buttonsPanel.add(deleteButton);
buttonsPanel.add(exportToPDFButton);

add(designationCaptionLabel);
add(titleTextField);
add(clearTitleTextFieldButton);
add(addCaptionLabel);
add(buttonsPanel);
}
private boolean addDesignation()
{
String title=titleTextField.getText().trim();
if(title.length()==0)
{
JOptionPane.showMessageDialog(this,"Designation required");
titleTextField.requestFocus();
return false;
}
DesignationInterface d=new Designation();
d.setTitle(title);
int rowIndex=0;
try
{
designationModel.add(d);
try
{
rowIndex=designationModel.indexOfDesignation(d);
}catch(BLException blException)
{
//do nothing
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rectangle);
JOptionPane.showMessageDialog(this,"Designation added successfully");
return true;
}catch(BLException blException)
{
if(blException.hasGenericException())
{
JOptionPane.showMessageDialog(this,blException.getGenericException());
}
else
{
if(blException.hasException("title"))
{
JOptionPane.showMessageDialog(this,blException.getException("title"));
}
}
titleTextField.requestFocus();
return false;
}
}
private boolean updateDesignation()
{
String title=titleTextField.getText().trim();
if(title.length()==0)
{
JOptionPane.showMessageDialog(this,"Designation required");
titleTextField.requestFocus();
return false;
}
DesignationInterface d=new Designation();
int rowIndex=0;
d.setCode(this.designation.getCode());
d.setTitle(title);
try
{
designationModel.update(d);
try
{
rowIndex=designationModel.indexOfDesignation(d);
}catch(BLException blException)
{
//do nothing
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rectangle);
JOptionPane.showMessageDialog(this,"Designation updated successfully");
return true;
}catch(BLException blException)
{
if(blException.hasGenericException())
{
JOptionPane.showMessageDialog(this,blException.getGenericException());
}
else
{
if(blException.hasException("title"))
{
JOptionPane.showMessageDialog(this,blException.getException("title"));
}
}
titleTextField.requestFocus();
return false;
}
}

private void deleteDesignation()
{
int code=this.designation.getCode();
String title=this.designation.getTitle();
int selectedOption=JOptionPane.showConfirmDialog(this,"Delete "+title,"Confirm ",JOptionPane.YES_NO_OPTION);
if(selectedOption==JOptionPane.NO_OPTION) return;
try
{
designationModel.remove(code);
JOptionPane.showMessageDialog(this,"Designation "+title+" deleted successfully");
}catch(BLException blException)
{
if(blException.hasGenericException())
{
JOptionPane.showMessageDialog(this,blException.getGenericException());
}
else
{
if(blException.hasException("title"))
{
JOptionPane.showMessageDialog(this,blException.getException("title"));
}
}
}
}
private void addListeners()
{
addButton.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent ev)
{
if(mode==MODE.VIEW)
{
setAddMode();
}
else
{
if(addDesignation())
{
setViewMode();
}
}
}
});
editButton.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent ev)
{
if(mode==MODE.VIEW)
{
setEditMode();
}
else
{
if(updateDesignation())
{
setViewMode();
}
}
}
});

deleteButton.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent ev)
{
setDeleteMode();
}
});

cancelButton.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent ev)
{
setViewMode();
}
});

exportToPDFButton.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent ev)
{
JFileChooser jfc=new JFileChooser();
jfc.setAcceptAllFileFilterUsed(false);
jfc.setCurrentDirectory(new File("."));
jfc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter()
{
public boolean accept(File file)
{
if(file.isDirectory()) return true;
if(file.getName().endsWith(".pdf")) return true;
return false;
}
public String getDescription()
{
return "PDF files";
}
});
int selectedOption=jfc.showSaveDialog(DesignationUI.this);
if(selectedOption==jfc.APPROVE_OPTION)
{
try
{
File selectedFile=jfc.getSelectedFile();
if(selectedFile.exists())
{
int option=JOptionPane.showConfirmDialog(DesignationUI.this,"Do you want to delete the existing file and create a new file ?","File already exists",JOptionPane.YES_NO_OPTION);
if(option==JOptionPane.YES_OPTION)
{
selectedFile.delete();
}
else return;
}
String pdfFile=selectedFile.getAbsolutePath();
if(pdfFile.endsWith(".")) pdfFile+="pdf";
else if(pdfFile.endsWith(".pdf")==false) pdfFile+=".pdf";
File file=new File(pdfFile);
File parent=new File(file.getParent());
if(parent.exists()==false || parent.isDirectory()==false)
{
JOptionPane.showMessageDialog(DesignationUI.this,"Incorrect path :"+file.getAbsolutePath());
return;
}
designationModel.exportToPDF(file);
JOptionPane.showMessageDialog(DesignationUI.this,"Data exported to :"+file.getAbsolutePath());
}catch(BLException blException)
{
if(blException.hasGenericException())
{
System.out.println(blException.getGenericException());
}
}
catch(Exception e)
{
System.out.println(e);
}
}
}

});
}


void setViewMode()
{
DesignationUI.this.setViewMode();
addButton.setIcon(addIcon);
editButton.setIcon(editIcon); 
designationCaptionLabel.setVisible(true);
addCaptionLabel.setVisible(false);
titleTextField.setVisible(false);
clearTitleTextFieldButton.setVisible(false);
this.addButton.setEnabled(true);
this.cancelButton.setEnabled(false);
if(designationModel.getRowCount()>0)
{
this.editButton.setEnabled(true);
this.deleteButton.setEnabled(true);
this.exportToPDFButton.setEnabled(true);
}
else 
{
this.editButton.setEnabled(false);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
}
}
void setAddMode()
{
DesignationUI.this.setAddMode();
titleTextField.setText("");
designationCaptionLabel.setVisible(true); 
addCaptionLabel.setVisible(true);
this.addButton.setIcon(saveIcon);
titleTextField.setVisible(true);
clearTitleTextFieldButton.setVisible(true);
this.editButton.setEnabled(false);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
this.cancelButton.setEnabled(true);

}
void setEditMode()
{
if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
{
JOptionPane.showMessageDialog(this,"Select designation to edit");
return;
}
DesignationUI.this.setEditMode();
titleTextField.setText(this.designation.getTitle());
designationCaptionLabel.setVisible(true); 
titleTextField.setVisible(true);
clearTitleTextFieldButton.setVisible(true);
this.editButton.setIcon(saveIcon);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
this.cancelButton.setEnabled(true);
this.addButton.setEnabled(false);

}
void setDeleteMode()
{
if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
{
JOptionPane.showMessageDialog(this,"Select designation to delete");
return;
}
DesignationUI.this.setDeleteMode();
designationCaptionLabel.setVisible(true); 
this.editButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
this.cancelButton.setEnabled(false);
deleteButton.setEnabled(false);
this.addButton.setEnabled(false);
deleteDesignation();
DesignationUI.this.setViewMode();
this.setViewMode();

}
void setExportToPDFMode()
{
DesignationUI.this.setExportToPDFMode();
designationCaptionLabel.setVisible(true); 
this.editButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
this.cancelButton.setEnabled(false);
deleteButton.setEnabled(false);
this.addButton.setEnabled(false);
}
}//inner class ends here
}

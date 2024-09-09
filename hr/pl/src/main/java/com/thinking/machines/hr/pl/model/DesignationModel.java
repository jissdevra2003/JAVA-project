package com.thinking.machines.hr.pl.model;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
import javax.swing.table.*;
import java.io.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.image.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.property.*; 
import com.itextpdf.layout.borders.*;
public class DesignationModel extends AbstractTableModel
{
private String[] columnTitle;
private java.util.List<DesignationInterface> designations;
private DesignationManagerInterface designationManager;
public DesignationModel()
{
populateDataStructures();
}
private void populateDataStructures()
{
columnTitle=new String[2];
columnTitle[0]="S.No.";
columnTitle[1]="Designation";

this.designations=new LinkedList<>();
try
{
designationManager=DesignationManager.getDesignationManager();
}catch(BLException blException)
{
//????????????
}
Set<DesignationInterface> BLDesignations=designationManager.getDesignations();
for(DesignationInterface designation:BLDesignations)
{
this.designations.add(designation);
}
Collections.sort(this.designations,new Comparator<DesignationInterface>()
{
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
}
public int getRowCount()
{
return this.designations.size();
}
public int getColumnCount()
{
return columnTitle.length;
}
public String getColumnName(int columnIndex)
{
return columnTitle[columnIndex];
}
public Object getValueAt(int rowIndex,int columnIndex)
{
if(columnIndex==0) return rowIndex+1;
return this.designations.get(rowIndex).getTitle();
}
public Class getColumnClass(int columnIndex)
{
if(columnIndex==0) return Integer.class; //special treatment (Class.forName("java.lang.Integer"))
return String.class; //Class.forName("java.lang.String")
}
public boolean isCellEditable(int rowIndex,int columnIndex)
{
return false;
}
//Application specific methods
public void add(DesignationInterface designation) throws BLException
{
designationManager.addDesignation(designation);
this.designations.add(designation);
Collections.sort(this.designations,new Comparator<DesignationInterface>()
{
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
fireTableDataChanged();
}
public int indexOfDesignation(DesignationInterface designation) throws BLException
{
Iterator<DesignationInterface> iterator=this.designations.iterator();
DesignationInterface d;
int index=0;
while(iterator.hasNext())
{
d=iterator.next();
if(d.equals(designation))  //code k bharose compare kregi equals.
{
return index;
}
index++;
}
BLException blException=new BLException();
blException.setGenericException("Invalid designation :"+designation.getTitle());
throw blException;
}
public int indexOfTitle(String title,boolean partialLeftSearch) throws BLException
{
Iterator<DesignationInterface> iterator=this.designations.iterator();
DesignationInterface d;
int index=0;
while(iterator.hasNext())
{
d=iterator.next();
if(partialLeftSearch)
{
if(d.getTitle().toUpperCase().startsWith(title.toUpperCase()))
{
return index;
}
}
else 
{
if(d.getTitle().equalsIgnoreCase(title))
{
return index;
}
}
index++;
}
BLException blException=new BLException();
blException.setGenericException("Invalid designation title :"+title);
throw blException;
}
public void update(DesignationInterface designation) throws BLException
{
designationManager.updateDesignation(designation);
this.designations.remove(indexOfDesignation(designation));  //purana remove krdiya DS me se
this.designations.add(designation); //naya add krdiya DS me
Collections.sort(this.designations,new Comparator<DesignationInterface>()
{
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
fireTableDataChanged();
}
public void remove(int code) throws BLException
{
designationManager.removeDesignation(code);
Iterator<DesignationInterface> iterator=this.designations.iterator();
int index=0;
while(iterator.hasNext())
{
if(iterator.next().getCode()==code) break;
index++;
}
if(index==this.designations.size())
{
BLException blException=new BLException();
blException.setGenericException("Invalid designation code :"+code);
throw blException;
}
this.designations.remove(index);
fireTableDataChanged();
}
public DesignationInterface getDesignationAt(int index) throws BLException
{
if(index<0 || index>=this.designations.size())
{
BLException blException=new BLException();
blException.setGenericException("Invalid index :"+index);
throw blException;
}
return this.designations.get(index);
}
public void exportToPDF(File file) throws BLException
{
try
{
PdfWriter pdfWriter=new PdfWriter(file);
PdfDocument pdfDocument=new PdfDocument(pdfWriter);
Document document=new Document(pdfDocument);
Paragraph companyNamePara=new Paragraph("Jiss 7 Ltd.");
Image logoImage=new Image(ImageDataFactory.create(this.getClass().getResource("/image_files/logo_icon48.png")));
Paragraph logoPara=new Paragraph();
logoPara.add(logoImage); 
PdfFont companyNameFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
companyNamePara.setFont(companyNameFont);
companyNamePara.setFontSize(20);
Paragraph pageNumberPara;
PdfFont pageNumberFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);

PdfFont columnTitleFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
Paragraph columnTitle1=new Paragraph("S.No.");
columnTitle1.setFont(columnTitleFont);
columnTitle1.setFontSize(14);

Paragraph columnTitle2=new Paragraph("Designations");
columnTitle2.setFont(columnTitleFont);
columnTitle2.setFontSize(14);
Paragraph dataPara;

PdfFont dataFont=PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
Cell cell;
Paragraph reportTitlePara=new Paragraph("Designations");
PdfFont reportTitleFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
reportTitlePara.setFont(reportTitleFont);
reportTitlePara.setFontSize(16);

Paragraph softwareByPara=new Paragraph("Software by Jiss Devra");
PdfFont softwareByParaFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
softwareByPara.setFont(softwareByParaFont);
softwareByPara.setFontSize(15);
Table footerTable;

float topTableColumnWidths[]={1,5};
float dataTableColumnWidths[]={1,5};
Table topTable;
Table dataTable=null;
Table reportTitleTable;
int pageNumber=0;
int pageSize=8;
int r=0;
int sno=0;
int numberOfPages=this.designations.size()/pageSize;
if((this.designations.size()%pageSize)!=0) numberOfPages++;
boolean newPage=true;
Table pageNumberTable;
DesignationInterface designation;
while(r<this.designations.size())
{
if(newPage)
{
//create header
pageNumber++;
topTable=new Table(UnitValue.createPercentArray(topTableColumnWidths));
cell=new Cell();
cell.setBorder(Border.NO_BORDER);
cell.add(logoPara);
topTable.addCell(cell);
cell=new Cell();
cell.setBorder(Border.NO_BORDER);
cell.add(companyNamePara);
cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
topTable.addCell(cell);
reportTitleTable=new Table(1);
reportTitleTable.setWidth(UnitValue.createPercentValue(100));
cell=new Cell();
cell.setBorder(Border.NO_BORDER);
cell.setTextAlignment(TextAlignment.CENTER);
cell.add(reportTitlePara);
reportTitleTable.addCell(cell);


pageNumberTable=new Table(1);
pageNumberTable.setWidth(UnitValue.createPercentValue(100));
cell=new Cell();
cell.setBorder(Border.NO_BORDER);
pageNumberPara=new Paragraph();
pageNumberPara.add("Page:"+pageNumber+"/"+numberOfPages);
pageNumberPara.setFont(pageNumberFont);
pageNumberPara.setFontSize(15);
cell.add(pageNumberPara);
cell.setTextAlignment(TextAlignment.RIGHT);
pageNumberTable.addCell(cell);

cell=new Cell(1,2);
Paragraph listPara=new Paragraph("List of designations");
PdfFont listFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
listPara.setFont(listFont);
listPara.setFontSize(16);
cell.add(listPara);
cell.setTextAlignment(TextAlignment.CENTER);

dataTable=new Table(UnitValue.createPercentArray(dataTableColumnWidths));
dataTable.setWidth(UnitValue.createPercentValue(100));
dataTable.addHeaderCell(cell);
dataTable.addHeaderCell(columnTitle1);
dataTable.addHeaderCell(columnTitle2);


document.add(topTable);
document.add(reportTitleTable);
document.add(pageNumberTable);


newPage=false;
}
sno++;
cell=new Cell();
dataPara=new Paragraph(String.valueOf(sno));
dataPara.setFont(dataFont);
dataPara.setFontSize(14);
cell.add(dataPara);
cell.setTextAlignment(TextAlignment.RIGHT);
dataTable.addCell(cell);

designation=this.designations.get(r);
cell=new Cell();
dataPara=new Paragraph(designation.getTitle().toUpperCase());
dataPara.setFont(dataFont);
dataPara.setFontSize(14);
cell.add(dataPara);
dataTable.addCell(cell);

r++;
if(sno%pageSize==0 || r==this.designations.size())
{
//create footer
document.add(dataTable);
footerTable=new Table(1);
footerTable.setWidth(UnitValue.createPercentValue(100));
cell=new Cell();
cell.setBorder(Border.NO_BORDER);
cell.add(softwareByPara);
footerTable.addCell(cell);
document.add(footerTable);

if(sno<this.designations.size())
{
document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
newPage=true;
}
}
}
document.close();
}catch(Exception e)
{
BLException blException=new BLException();
blException.setGenericException(e.getMessage());
throw blException;
}
}
}
package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.util.*;
import java.io.*;
public class DesignationDAO implements DesignationDAOInterface
{
private final static String FILE_NAME="designation.data";
public void add(DesignationDTOInterface designationDTO) throws DAOException
{
if(designationDTO==null) throw new DAOException("Designation cannot be null");
String title=designationDTO.getTitle();
if(title==null) throw new DAOException("Title cannot be null");
title=title.trim();
if(title.length()==0) throw new DAOException("Title length cannot be zero");
try
{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
int lastGeneratedCode=0;
int recordCount=0;
String lastGeneratedCodeString="";
String recordCountString="";
if(randomAccessFile.length()==0)
{
lastGeneratedCodeString="0";
while(lastGeneratedCodeString.length()<10) lastGeneratedCodeString+=" ";
recordCountString="0";
while(recordCountString.length()<10) recordCountString+=" ";
randomAccessFile.writeBytes(lastGeneratedCodeString);
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(recordCountString);
randomAccessFile.writeBytes("\n");
}
else 
{
lastGeneratedCodeString=randomAccessFile.readLine().trim();
recordCountString=randomAccessFile.readLine().trim();
lastGeneratedCode=Integer.parseInt(lastGeneratedCodeString);
recordCount=Integer.parseInt(recordCountString);
}
int fcode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fTitle.equalsIgnoreCase(title))
{
randomAccessFile.close();
throw new DAOException("Title "+title+" already exists");
}
}
int code=lastGeneratedCode+1;
designationDTO.setCode(code);
randomAccessFile.writeBytes(String.valueOf(code));
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(title+"\n");
lastGeneratedCode++;
recordCount++;
lastGeneratedCodeString=String.valueOf(lastGeneratedCode);
while(lastGeneratedCodeString.length()<10) lastGeneratedCodeString+=" ";
recordCountString=String.valueOf(recordCount);
while(recordCountString.length()<10) recordCountString+=" ";
randomAccessFile.seek(0);
randomAccessFile.writeBytes(lastGeneratedCodeString);
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(recordCountString+"\n");
randomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public void update(DesignationDTOInterface designationDTO) throws DAOException
{
if(designationDTO==null) throw new DAOException("Designation is null");
String title=designationDTO.getTitle();
int code=designationDTO.getCode();
if(title==null || title.trim().length()==0) throw new DAOException("Title is null");
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) throw new DAOException("Invalid entry");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid entry");
}
int fcode;
String fTitle;
int recordCount;
String lastGeneratedCodeString="";
String recordCountString="";
boolean found=false;
randomAccessFile.readLine();  //lastGeneratedCode read from file.
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid entry");
}
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fcode==code)
{
found=true;
break;
}
}
if(found==false)
{
randomAccessFile.close();
throw new DAOException("Invalid entry");
}
randomAccessFile.seek(0);
randomAccessFile.readLine();
randomAccessFile.readLine();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fcode!=code && fTitle.equalsIgnoreCase(title)==true)
{
randomAccessFile.close();
throw new DAOException("Title"+title+" already exists");
}
}
System.out.println("Updating data of :"+code);
File tmpFile=new File("tmp.tmp");
RandomAccessFile tmpRaf=new RandomAccessFile(tmpFile,"rw");
tmpRaf.setLength(0);
randomAccessFile.seek(0);
tmpRaf.writeBytes(randomAccessFile.readLine()+"\n");
tmpRaf.writeBytes(randomAccessFile.readLine()+"\n");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fcode==code)
{
tmpRaf.writeBytes(String.valueOf(code)+"\n");
tmpRaf.writeBytes(title+"\n");
}
else
{
tmpRaf.writeBytes(String.valueOf(fcode)+"\n");
tmpRaf.writeBytes(fTitle+"\n");
}
}
randomAccessFile.seek(0);
tmpRaf.seek(0);
while(tmpRaf.getFilePointer()<tmpRaf.length())
{
randomAccessFile.writeBytes(tmpRaf.readLine()+"\n");
}
randomAccessFile.setLength(tmpRaf.length());
tmpRaf.setLength(0);
randomAccessFile.close();
tmpRaf.close();
System.out.println("Data updated of code"+code);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public void delete(int code) throws DAOException
{
if(code<=0) throw new DAOException("Invalid code :"+code);
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) throw new DAOException("Invalid code :"+code);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid code :"+code);
}
int recordCount;
randomAccessFile.readLine();
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid entry");
}
int fcode;
String fTitle="";
String lastGeneratedCodeString="";
String recordCountString="";
boolean found=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fcode==code)
{
found=true;
break;
}
}
if(found==false)
{
randomAccessFile.close();
throw new DAOException("Invalid code :"+code);
}
if(new EmployeeDAO().isDesignationAlloted(code))
{
randomAccessFile.close();
throw new DAOException("Employee exists with designation :"+fTitle);
}
File tmpFile=new File("tmp.tmp");
RandomAccessFile tmpRaf=new RandomAccessFile(tmpFile,"rw");
tmpRaf.setLength(0);
randomAccessFile.seek(0);
lastGeneratedCodeString=randomAccessFile.readLine();
recordCountString=randomAccessFile.readLine();
tmpRaf.writeBytes(lastGeneratedCodeString+"\n");
tmpRaf.writeBytes(recordCountString+"\n");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fcode!=code)
{
tmpRaf.writeBytes(String.valueOf(fcode)+"\n");
tmpRaf.writeBytes(fTitle+"\n");
}
}
tmpRaf.seek(0);
randomAccessFile.seek(0);
randomAccessFile.writeBytes(tmpRaf.readLine()+"\n");
recordCount=Integer.parseInt(tmpRaf.readLine().trim());
recordCount--;
recordCountString=String.valueOf(recordCount);
while(recordCountString.length()<10) recordCountString+=" ";
randomAccessFile.writeBytes(recordCountString+"\n");
while(tmpRaf.getFilePointer()<tmpRaf.length())
{
randomAccessFile.writeBytes(tmpRaf.readLine()+"\n");
}
randomAccessFile.setLength(tmpRaf.length());
tmpRaf.setLength(0);
randomAccessFile.close();
tmpRaf.close();
System.out.println("Data of code "+code+" has been deleted.");
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public Set<DesignationDTOInterface> getAll() throws DAOException
{
Set<DesignationDTOInterface> designations;
designations=new TreeSet<>();
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return designations;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return designations;
}
randomAccessFile.readLine();
randomAccessFile.readLine();
DesignationDTOInterface designationDTO;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
designationDTO=new DesignationDTO();
designationDTO.setCode(Integer.parseInt(randomAccessFile.readLine()));
designationDTO.setTitle(randomAccessFile.readLine());
designations.add(designationDTO);
}
randomAccessFile.close();
return designations;
}
catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public DesignationDTOInterface getByCode(int code) throws DAOException
{
if(code<=0) throw new DAOException("Invalid code :"+code);
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) throw new DAOException("Invalid code :"+code);
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid code :"+code);
}
randomAccessFile.readLine();
int recordCount;
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid code :"+code);
}
int fcode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fcode==code)
{
randomAccessFile.close();
DesignationDTOInterface designationDTO;
designationDTO=new DesignationDTO();
designationDTO.setCode(fcode);
designationDTO.setTitle(fTitle);
return designationDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid code :"+code);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public DesignationDTOInterface getByTitle(String title) throws DAOException
{
if(title==null || title.trim().length()==0) throw new DAOException("Invalid title :"+title);
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) throw new DAOException("Invalid title :"+title);
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid title :"+title);
}
randomAccessFile.readLine();
int recordCount;
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid title :"+title);
}
int fcode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fTitle.equalsIgnoreCase(title))
{
randomAccessFile.close();
DesignationDTOInterface designationDTO;
designationDTO=new DesignationDTO();
designationDTO.setCode(fcode);
designationDTO.setTitle(fTitle);
return designationDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid title :"+title);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean codeExists(int code) throws DAOException
{
if(code<=0) return false;
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return false;
}
randomAccessFile.readLine();
int recordCount;
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return false;
}
int fcode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fcode==code) 
{
randomAccessFile.close();
return true;
}
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean titleExists(String title) throws DAOException
{
if(title==null || title.trim().length()==0) return false;
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return false;
}
randomAccessFile.readLine();
int recordCount;
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return false;
}
int fcode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fTitle.equalsIgnoreCase(title)) 
{
randomAccessFile.close();
return true;
}
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public int getCount() throws DAOException
{
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) return 0;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
int recordCount=0;
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return 0;
}
randomAccessFile.readLine();
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
randomAccessFile.close();
return recordCount;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
} 

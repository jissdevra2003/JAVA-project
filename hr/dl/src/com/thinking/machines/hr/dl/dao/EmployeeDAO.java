package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.io.*;
import java.text.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
public class EmployeeDAO implements EmployeeDAOInterface
{
private static final String FILE_NAME="employee.data";
public void add(EmployeeDTOInterface employeeDTO) throws DAOException
{
if(employeeDTO==null) throw new DAOException("Employee value cannot be null");

String name=employeeDTO.getName();
Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null) throw new DAOException("Date of birth is null");
char gender=employeeDTO.getGender();
if(gender!='m' && gender!='M' && gender!='f' && gender!='F') throw new DAOException("Invalid gender");
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null) throw new DAOException("Basic salary is null");
if(basicSalary.signum()==-1) throw new DAOException("Basic salary is negative");
boolean isIndian=employeeDTO.getIsIndian();
if(name==null) throw new DAOException("Name cannot be empty");
name=name.trim();
if(name.length()==0) throw new DAOException("Name cannot be empty");
int designationCode=employeeDTO.getDesignationCode();
if(designationCode<=0) throw new DAOException("Invalid code :"+designationCode);
DesignationDAOInterface designationDAO;
designationDAO=new DesignationDAO();
if(!(designationDAO.codeExists(designationCode))) throw new DAOException("Invalid code :"+designationCode);
String panNumber=employeeDTO.getPANNumber();
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(panNumber==null) throw new DAOException("PAN number is null");
if(aadharCardNumber==null) throw new DAOException("Aadhar card number is null");
aadharCardNumber=aadharCardNumber.trim();
if(!(aadharCardNumber.length()==12)) throw new DAOException("Aadhar card Number should contain exactly 12 numbers , Example:143523452346");
panNumber=panNumber.trim();
if(panNumberExists(panNumber)) throw new DAOException("Employee id already exists with PAN number as :"+panNumber);
if(aadharCardNumberExists(aadharCardNumber)) throw new DAOException("Employee id already exists with aadhar card number as :"+aadharCardNumber);

try
{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
String lastGeneratedEmployeeIdString="";
String recordCountString="";
int lastGeneratedEmployeeId=10000000;
int recordCount=0;
if(randomAccessFile.length()==0)
{
lastGeneratedEmployeeIdString=String.format("%-10s","10000000");
randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
recordCountString=String.format("%-10s","0");
randomAccessFile.writeBytes(recordCountString+"\n");
}
else
{
lastGeneratedEmployeeIdString=randomAccessFile.readLine().trim();
recordCountString=randomAccessFile.readLine().trim();
lastGeneratedEmployeeId=Integer.parseInt(lastGeneratedEmployeeIdString);
recordCount=Integer.parseInt(recordCountString);
}
String fName;
String EmployeeId;
String fPanNumber;
String fAadharCardNumber;
boolean panNumberExists,aadharCardNumberExists;
panNumberExists=false;
aadharCardNumberExists=false;
EmployeeId=generateEmployeeId(lastGeneratedEmployeeId+1);
String fEmployeeId;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine(); //read employeeId
randomAccessFile.readLine(); //read name
randomAccessFile.readLine(); //read DOB
fPanNumber=randomAccessFile.readLine(); //read panNumber
fAadharCardNumber=randomAccessFile.readLine(); //read aadharNumber
if(panNumberExists==false && fPanNumber.equalsIgnoreCase(panNumber))
{
panNumberExists=true;
}
if(aadharCardNumberExists==false && fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
aadharCardNumberExists=true;
}
if(panNumberExists && aadharCardNumberExists) break;
randomAccessFile.readLine(); //read designationCode
randomAccessFile.readLine(); //read gender
randomAccessFile.readLine(); //read isIndian
randomAccessFile.readLine(); //read basicSalary
}
if(panNumberExists && aadharCardNumberExists)
{
randomAccessFile.close();
throw new DAOException("PAN number ("+panNumber+") & Aadhar card number ("+aadharCardNumber+") exists");
}
if(panNumberExists)
{
randomAccessFile.close();
throw new DAOException("PAN number "+panNumber+" exists");
}
if(aadharCardNumberExists)
{
randomAccessFile.close();
throw new DAOException("Aadhar card number "+aadharCardNumber+" exists");
}

String fDateOfBirth;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
fDateOfBirth=sdf.format(dateOfBirth);
String fBasicSalary=basicSalary.toPlainString();
randomAccessFile.writeBytes(EmployeeId+"\n");
randomAccessFile.writeBytes(name+"\n");
randomAccessFile.writeBytes(fDateOfBirth+"\n");
randomAccessFile.writeBytes(panNumber+"\n");
randomAccessFile.writeBytes(aadharCardNumber+"\n");
randomAccessFile.writeBytes(designationCode+"\n");
randomAccessFile.writeBytes(gender+"\n");
randomAccessFile.writeBytes(isIndian+"\n");
randomAccessFile.writeBytes(fBasicSalary+"\n");
lastGeneratedEmployeeId++;
recordCount++;
lastGeneratedEmployeeIdString=String.format("%-10d",lastGeneratedEmployeeId);
recordCountString=String.format("%-10d",recordCount);
randomAccessFile.seek(0);
randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
randomAccessFile.writeBytes(recordCountString+"\n");
randomAccessFile.close();
employeeDTO.setEmployeeId(EmployeeId);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public void update(EmployeeDTOInterface employeeDTO) throws DAOException
{
if(employeeDTO==null) throw new DAOException("Employee is null");
String employeeId=employeeDTO.getEmployeeId();
if(employeeId==null) throw new DAOException("Employee id is null");
if(employeeId.length()==0) throw new DAOException("Employee id length is zero");
String name=employeeDTO.getName();
if(name==null) throw new DAOException("Name cannot be null");
name=name.trim();
if(name.length()==0) throw new DAOException("Length of name is zero");
int designationCode=employeeDTO.getDesignationCode();
if(designationCode<=0) throw new DAOException("Designation code is zero");
DesignationDAOInterface designationDAO;
designationDAO=new DesignationDAO();
boolean isDesignationCodeValid=designationDAO.codeExists(designationCode);
if(isDesignationCodeValid==false) throw new DAOException("Invalid designation code :"+designationCode);
Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null) throw new DAOException("Date is null");
char gender=employeeDTO.getGender();
if(gender==' ') throw new DAOException("Gender not set to MALE/FEMALE");
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null) throw new DAOException("Basic salary is null");
if(basicSalary.signum()==-1) throw new DAOException("Basic salary is negative");
String panNumber=employeeDTO.getPANNumber();
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(panNumber==null) throw new DAOException("PAN number is invalid");
if(aadharCardNumber==null) throw new DAOException("Aadhar card number is invalid");
aadharCardNumber=aadharCardNumber.trim();
if(!(aadharCardNumber.length()==12)) throw new DAOException("Aadhar card Number should contain exactly 12 numbers , Example:143523452346");
panNumber=panNumber.trim();
if(panNumberExists(panNumber)) throw new DAOException("Employee id already exists with PAN number as :"+panNumber);
if(aadharCardNumberExists(aadharCardNumber)) throw new DAOException("Employee id already exists with aadhar card number as :"+aadharCardNumber);
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) throw new DAOException("Invalid employee Id :"+employeeId);
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid employee id :"+employeeId);
}
randomAccessFile.readLine();
randomAccessFile.readLine();
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
String fEmployeeId;
String fName;
String fPanNumber;
String fAadharCardNumber;
BigDecimal fBasicSalary;
int fDesignationCode;
char fGender;
String fDateOfBirth;
boolean fIsIndian;
boolean employeeIdFound=false;
boolean panNumberFound=false;
boolean aadharCardNumberFound=false;
String panNumberFoundAgainstEmployeeId="";
String aadharCardNumberFoundAgainstEmployeeId="";
long foundAt=0;
int x;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
if(employeeIdFound==false) foundAt=randomAccessFile.getFilePointer();
fEmployeeId=randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
fPanNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
for(x=1;x<=4;x++) randomAccessFile.readLine();
if(employeeIdFound==false && fEmployeeId.equalsIgnoreCase(employeeId))
{
employeeIdFound=true;
}
if(panNumberFound==false && fPanNumber.equalsIgnoreCase(panNumber))
{
panNumberFound=true;
panNumberFoundAgainstEmployeeId=fEmployeeId;
}
if(aadharCardNumberFound==false && fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
aadharCardNumberFound=true;
aadharCardNumberFoundAgainstEmployeeId=fEmployeeId;
}
if(employeeIdFound && panNumberFound && aadharCardNumberFound) break;
}
if(employeeIdFound==false)
{
randomAccessFile.close();
throw new DAOException("Invalid employee id :"+employeeId);
}
boolean panNumberExists=false;
if(panNumberFound && panNumberFoundAgainstEmployeeId.equalsIgnoreCase(employeeId)==false)
{
panNumberExists=true;
}
boolean aadharCardNumberExists=false;
if(aadharCardNumberFound && aadharCardNumberFoundAgainstEmployeeId.equalsIgnoreCase(employeeId)==false)
{
aadharCardNumberExists=true;
}
if(aadharCardNumberExists && panNumberExists)
{
randomAccessFile.close();
throw new DAOException("PAN number ("+panNumber+") and aadhar card number ("+aadharCardNumber+") exists");
}
if(panNumberExists)
{
randomAccessFile.close();
throw new DAOException("PAN number ("+panNumber+") exists");
}
if(aadharCardNumberExists)
{
randomAccessFile.close();
throw new DAOException("Aadhar card number ("+aadharCardNumber+") exists");
}
randomAccessFile.seek(foundAt);
for(x=1;x<=9;x++) randomAccessFile.readLine();
File tmpFile=new File("tmp.tmp");
if(tmpFile.exists()) tmpFile.delete();
RandomAccessFile tmpRaf=new RandomAccessFile(tmpFile,"rw");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
tmpRaf.writeBytes(randomAccessFile.readLine()+"\n");
}

fDateOfBirth=sdf.format(dateOfBirth);
randomAccessFile.seek(foundAt);
randomAccessFile.writeBytes(employeeId+"\n");
randomAccessFile.writeBytes(name+"\n");
randomAccessFile.writeBytes(fDateOfBirth+"\n");
randomAccessFile.writeBytes(panNumber+"\n");
randomAccessFile.writeBytes(aadharCardNumber+"\n");
randomAccessFile.writeBytes(designationCode+"\n");
randomAccessFile.writeBytes(gender+"\n");
randomAccessFile.writeBytes(isIndian+"\n");
randomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
tmpRaf.seek(0);
while(tmpRaf.getFilePointer()<tmpRaf.length())
{
randomAccessFile.writeBytes(tmpRaf.readLine()+"\n");
}
randomAccessFile.setLength(randomAccessFile.getFilePointer());
tmpRaf.setLength(0);
randomAccessFile.close();
tmpRaf.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}

}
public void delete(String employeeId) throws DAOException
{
if(employeeId==null) throw new DAOException("Employee id is null");
if(employeeId.length()==0) throw new DAOException("Employee id length is zero");
int recordCount;
String recordCountString="";
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) throw new DAOException("Invalid employee Id :"+employeeId);
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid employee id :"+employeeId);
}
randomAccessFile.readLine();
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
String fEmployeeId;
boolean employeeIdFound=false;
long foundAt=0;
int x;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
foundAt=randomAccessFile.getFilePointer();
fEmployeeId=randomAccessFile.readLine();
for(x=1;x<=8;x++) randomAccessFile.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId))
{
employeeIdFound=true;
break;
}
}
if(employeeIdFound==false)
{
randomAccessFile.close();
throw new DAOException("Invalid employee id :"+employeeId);
}
File tmpFile=new File("tmp.tmp");
if(tmpFile.exists()) tmpFile.delete();
RandomAccessFile tmpRaf=new RandomAccessFile(tmpFile,"rw");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
tmpRaf.writeBytes(randomAccessFile.readLine()+"\n");
}
randomAccessFile.seek(foundAt);

tmpRaf.seek(0);
while(tmpRaf.getFilePointer()<tmpRaf.length())
{
randomAccessFile.writeBytes(tmpRaf.readLine()+"\n");
}
randomAccessFile.setLength(randomAccessFile.getFilePointer());
recordCount--;
recordCountString=String.format("%-10d",recordCount);
randomAccessFile.seek(0);
randomAccessFile.readLine();
randomAccessFile.writeBytes(recordCountString+"\n");
randomAccessFile.close();
tmpRaf.setLength(0);
tmpRaf.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}

}
public Set<EmployeeDTOInterface> getAll() throws DAOException
{
Set<EmployeeDTOInterface> employees;
employees=new TreeSet<>();
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return employees;
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return employees;
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return employees;
}
String fEmployeeId;
String fName;
String fPanNumber;
String fAadharCardNumber;
int fDesignationCode;
BigDecimal fBasicSalary;
Date fDateOfBirth=new Date();
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
boolean fIsIndian;
char fGender;
EmployeeDTOInterface employeeDTO;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
employeeDTO=new EmployeeDTO();
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
try
{
fDateOfBirth=sdf.parse(randomAccessFile.readLine());
}catch(ParseException pe)
{
throw new DAOException(pe.getMessage()); 
}
fPanNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
fGender=randomAccessFile.readLine().charAt(0);
if(fGender=='M')
{
employeeDTO.setGender(GENDER.MALE);
}
if(fGender=='F')
{
employeeDTO.setGender(GENDER.FEMALE);
}
fIsIndian=Boolean.parseBoolean(randomAccessFile.readLine());
fBasicSalary=new BigDecimal(randomAccessFile.readLine());
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
employeeDTO.setPANNumber(fPanNumber);
employeeDTO.setBasicSalary(fBasicSalary);
employeeDTO.setAadharCardNumber(fAadharCardNumber);
employeeDTO.setDateOfBirth(fDateOfBirth);
employeeDTO.setIsIndian(fIsIndian);
employees.add(employeeDTO);
}
randomAccessFile.close();
return employees;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException
{
if(new DesignationDAO().codeExists(designationCode)==false) throw new DAOException("Invalid designation code :"+designationCode);
Set<EmployeeDTOInterface> employees;
employees=new TreeSet<>();
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return employees;
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return employees;
}
randomAccessFile.readLine();
randomAccessFile.readLine();
String fEmployeeId;
String fName;
String fPanNumber;
Date fDateOfBirth=new Date();
String fAadharCardNumber;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
EmployeeDTOInterface employeeDTO;
int fDesignationCode;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
try
{
fDateOfBirth=sdf.parse(randomAccessFile.readLine());
}catch(ParseException pe)
{
//do nothing
}
fPanNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fDesignationCode!=designationCode)
{
for(int x=1;x<=3;x++) randomAccessFile.readLine();
continue;
}
employeeDTO =new EmployeeDTO();
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDateOfBirth(fDateOfBirth);
employeeDTO.setPANNumber(fPanNumber);
employeeDTO.setAadharCardNumber(fAadharCardNumber);
employeeDTO.setDesignationCode(fDesignationCode);
employeeDTO.setGender((randomAccessFile.readLine().charAt(0)=='M')?GENDER.MALE:GENDER.FEMALE);
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employees.add(employeeDTO);
}
randomAccessFile.close();
return employees;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException
{
if(employeeId==null) throw new DAOException("Employee id is null");
try
{
File file=new File(FILE_NAME);
if(!file.exists()) throw new DAOException("Invalid employee id :"+employeeId);
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid employee id :"+employeeId);
}
randomAccessFile.readLine();
randomAccessFile.readLine();
String fEmployeeId;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
EmployeeDTOInterface employeeDTO;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId))
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(randomAccessFile.readLine());
try
{
employeeDTO.setDateOfBirth(sdf.parse(randomAccessFile.readLine()));
}catch(ParseException pe)
{
//do nothing
}
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
employeeDTO.setGender((randomAccessFile.readLine().charAt(0)=='M')?GENDER.MALE:GENDER.FEMALE);
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
return employeeDTO;
}
for(int x=1;x<=8;x++) randomAccessFile.readLine();
}
randomAccessFile.close();
throw new DAOException("Invalid employee id :"+employeeId);

}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException
{
if(panNumber==null) throw new DAOException("PAN number is null");
String fPanNumber=panNumber.trim();
if(fPanNumber.length()==0)
{
throw new DAOException("Invalid PAN number :"+panNumber);
}
try
{
File file=new File(FILE_NAME);
if(!file.exists()) throw new DAOException("Invalid PAN number :"+panNumber);
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid PAN number :"+panNumber);
}
randomAccessFile.readLine();
int recordCount;
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid PAN number :"+panNumber);
}
String fEmployeeId;
String fAadharCardNumber;
String fName;
Date fDateOfBirth=new Date();
String dateString;
int fDesignationCode;
char fGender;
boolean fIsIndian;
BigDecimal fBasicSalary;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
dateString=randomAccessFile.readLine();
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
try
{
fDateOfBirth=sdf.parse(dateString);
}catch(ParseException pe)
{
throw new DAOException(pe.getMessage());
}
fPanNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
fGender=randomAccessFile.readLine().charAt(0);
fIsIndian=Boolean.parseBoolean(randomAccessFile.readLine());
fBasicSalary=new BigDecimal(randomAccessFile.readLine());
if(fPanNumber.equalsIgnoreCase(panNumber))
{
randomAccessFile.close();
EmployeeDTOInterface employeeDTO;
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
if(fGender=='M')
{
employeeDTO.setGender(GENDER.MALE);
}
if(fGender=='F')
{
employeeDTO.setGender(GENDER.FEMALE);
}
employeeDTO.setPANNumber(fPanNumber);
employeeDTO.setBasicSalary(fBasicSalary);
employeeDTO.setAadharCardNumber(fAadharCardNumber);
employeeDTO.setDateOfBirth(fDateOfBirth);
employeeDTO.setIsIndian(fIsIndian);
return employeeDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid PAN number :"+panNumber);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
{
if(aadharCardNumber==null) throw new DAOException("Aadhar card number is null");
String fAadharCardNumber=aadharCardNumber.trim();
if(fAadharCardNumber.length()==0)
{
throw new DAOException("Invalid Aadhar card number :"+aadharCardNumber);
}
try
{
File file=new File(FILE_NAME);
if(!file.exists()) throw new DAOException("Invalid Aadhar card number :"+aadharCardNumber);
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid Aadhar card  number :"+aadharCardNumber);
}
randomAccessFile.readLine();
int recordCount;
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid Aadhar card number :"+aadharCardNumber);
}
String fEmployeeId;
String fPanNumber;
String fName;
Date fDateOfBirth=new Date();
String dateString;
int fDesignationCode;
char fGender;
boolean fIsIndian;
BigDecimal fBasicSalary;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
dateString=randomAccessFile.readLine();
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
try
{
fDateOfBirth=sdf.parse(dateString);
}catch(ParseException pe)
{
throw new DAOException(pe.getMessage());
}
fPanNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
fGender=randomAccessFile.readLine().charAt(0);
fIsIndian=Boolean.parseBoolean(randomAccessFile.readLine());
fBasicSalary=new BigDecimal(randomAccessFile.readLine());
if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
randomAccessFile.close();
EmployeeDTOInterface employeeDTO;
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
if(fGender=='M')
{
employeeDTO.setGender(GENDER.MALE);
}
if(fGender=='F')
{
employeeDTO.setGender(GENDER.FEMALE);
}
employeeDTO.setPANNumber(fPanNumber);
employeeDTO.setBasicSalary(fBasicSalary);
employeeDTO.setAadharCardNumber(fAadharCardNumber);
employeeDTO.setDateOfBirth(fDateOfBirth);
employeeDTO.setIsIndian(fIsIndian);
return employeeDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid Aadhar card number :"+aadharCardNumber);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean employeeIdExists(String employeeId) throws DAOException
{
if(employeeId==null) throw new DAOException("Invalid employee id"+employeeId);
String fEmployeeId=employeeId.trim();
if(fEmployeeId.length()==0) throw new DAOException("Invalid employee id :"+employeeId);
try
{
File file=new File(FILE_NAME);
if(!file.exists()) throw new DAOException("Invalid employee id :"+employeeId);
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return false;
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return false;
}
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId))
{
randomAccessFile.close();
return true;
}
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean panNumberExists(String panNumber) throws DAOException
{
if(panNumber==null) return false;
String fPanNumber=panNumber.trim();
if(fPanNumber.length()==0) return false;
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return false;
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return false;
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return false;
}
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
fPanNumber=randomAccessFile.readLine();
if(fPanNumber.equalsIgnoreCase(panNumber))
{
randomAccessFile.close();
return true;
}
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException
{
if(aadharCardNumber==null) return false;
String fAadharCardNumber=aadharCardNumber.trim();
if(fAadharCardNumber.length()==0) return false;
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return false;
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return false;
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return false;
}
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
randomAccessFile.close();
return true;
}
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean isDesignationAlloted(int designationCode) throws DAOException
{
if(new DesignationDAO().codeExists(designationCode)==false) throw new DAOException("Invalid designation code :"+designationCode);
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) return false;
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return false;
}
randomAccessFile.readLine();
randomAccessFile.readLine();
int fDesignationCode;
int x;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fDesignationCode==designationCode)
{
randomAccessFile.close();
return true;
}
for(x=1;x<=3;x++) randomAccessFile.readLine();
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
if(!file.exists()) return 0;
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return 0;
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return 0;
}
return recordCount;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public int getCountByDesignation(int designationCode) throws DAOException
{
throw new DAOException("Not yet implemented");
}
private static String generateEmployeeId(int id)
{
String employeeId;
employeeId="A"+id;
return employeeId;
}
}
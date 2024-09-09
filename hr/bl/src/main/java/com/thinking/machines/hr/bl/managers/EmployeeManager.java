package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;
public class EmployeeManager implements EmployeeManagerInterface
{
private Set<EmployeeInterface> employeesSet;
private Map<String,EmployeeInterface> employeeIdWiseEmployeesMap;
private Map<String,EmployeeInterface> panNumberWiseEmployeesMap;
private Map<String,EmployeeInterface> aadharCardNumberWiseEmployeesMap;
private Map<Integer,Set<EmployeeInterface>> designationCodeWiseEmployeesMap;
private static EmployeeManager employeeManager=null;
private EmployeeManager() throws BLException
{
populateDataStructures();
}
public static EmployeeManagerInterface getEmployeeManager() throws BLException
{
if(employeeManager==null) employeeManager=new EmployeeManager();
return employeeManager;
}
private void populateDataStructures() throws BLException
{
this.employeesSet=new TreeSet<>();
this.employeeIdWiseEmployeesMap=new HashMap<>();
this.panNumberWiseEmployeesMap=new HashMap<>();
this.aadharCardNumberWiseEmployeesMap=new HashMap<>();
this.designationCodeWiseEmployeesMap=new HashMap<>();
try
{
Set<EmployeeDTOInterface> dlEmployees;
dlEmployees=new EmployeeDAO().getAll();
EmployeeInterface employee;
DesignationManagerInterface designationManager;
designationManager=DesignationManager.getDesignationManager();
DesignationInterface designation;
Set<EmployeeInterface> ets;
for(EmployeeDTOInterface DLemployee:dlEmployees)
{
employee=new Employee();
employee.setEmployeeId(DLemployee.getEmployeeId());
employee.setName(DLemployee.getName());
employee.setAadharCardNumber(DLemployee.getAadharCardNumber());
employee.setBasicSalary(DLemployee.getBasicSalary());
employee.setDateOfBirth((Date)DLemployee.getDateOfBirth().clone());
designation=designationManager.getDesignationByCode(DLemployee.getDesignationCode());
employee.setDesignation(designation);
if(DLemployee.getGender()=='M')
{
employee.setGender(GENDER.MALE);
}
else
{
employee.setGender(GENDER.FEMALE);
}
employee.setIsIndian(DLemployee.getIsIndian());
employee.setPANNumber(DLemployee.getPANNumber());
this.employeesSet.add(employee);
this.employeeIdWiseEmployeesMap.put(employee.getEmployeeId().toUpperCase(),employee);
this.panNumberWiseEmployeesMap.put(employee.getPANNumber().toUpperCase(),employee);
this.aadharCardNumberWiseEmployeesMap.put(employee.getAadharCardNumber().toUpperCase(),employee);
ets=this.designationCodeWiseEmployeesMap.get(designation.getCode());
if(ets==null)
{
ets=new TreeSet<>();
ets.add(employee);
designationCodeWiseEmployeesMap.put(new Integer(designation.getCode()),ets);
}
else
{
ets.add(employee);
}
}
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
}
}
public void addEmployee(EmployeeInterface employee) throws BLException
{
BLException blException=new BLException();
if(employee==null)
{
blException.setGenericException("Employee is null");
}
String employeeId=employee.getEmployeeId();
String name=employee.getName();
DesignationInterface designation=employee.getDesignation();
int designationCode=0;
Date dateOfBirth=employee.getDateOfBirth();
char gender=employee.getGender();
boolean isIndian=employee.getIsIndian();
BigDecimal basicSalary=employee.getBasicSalary();
String panNumber=employee.getPANNumber();
String aadharCardNumber=employee.getAadharCardNumber();
if(employeeId!=null)
{
employeeId=employeeId.trim();
if(employeeId.length()>0)
{
blException.addException("employeeId","Employee id should be nil/empty");
}
}
if(name==null)
{
blException.addException("name","Name required");
}
else
{
name=name.trim();
if(name.length()==0)
{
blException.addException("name","Name required");
}
}
DesignationManagerInterface designationManager;
designationManager=DesignationManager.getDesignationManager();
if(designation==null)
{
blException.addException("designation","Designation required");
}
designationCode=designation.getCode();
if(designationManager.designationCodeExists(designationCode)==false)
{
blException.addException("designation","Invalid designation");
}

if(dateOfBirth==null)
{
blException.addException("dateOfBirth","Date of birth required");
}
if(gender==' ')
{
blException.addException("gender","Gender required");
}
if(basicSalary==null)
{
blException.addException("basicSalary","Basic salary required");
}
else
{
if(basicSalary.signum()==-1)
{
blException.addException("basicSalary","Basic salary cannot be negative");
}
}
if(panNumber==null)
{
blException.addException("panNumber","PAN number required");
}
else
{
panNumber=panNumber.trim();
if(panNumber.length()==0)
{
blException.addException("panNumber","PAN number required");
}
}

if(aadharCardNumber==null)
{
blException.addException("aadharCardNumber","Aadhar card number required");
}
else
{
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)
{
blException.addException("aadharCardNumber","Aadhar card number required");
}
}
if(panNumber!=null && panNumber.length()>0)
{
if(panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase()))
{
blException.addException("panNumber","PAN number"+panNumber+" exists");
}
}
if(aadharCardNumber!=null && aadharCardNumber.length()>0)
{
if(aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase()))
{
blException.addException("aadharCardNumber","Aadhar card number"+aadharCardNumber+" exists");
}
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
EmployeeDTOInterface dlEmployee=new EmployeeDTO();
dlEmployee.setName(name);
dlEmployee.setDesignationCode(designationCode);
dlEmployee.setDateOfBirth((Date)dateOfBirth.clone());
dlEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
dlEmployee.setBasicSalary(basicSalary);
dlEmployee.setIsIndian(isIndian);
dlEmployee.setPANNumber(panNumber);
dlEmployee.setAadharCardNumber(aadharCardNumber);
employeeDAO.add(dlEmployee);
employee.setEmployeeId(dlEmployee.getEmployeeId());
EmployeeInterface dsEmployee=new Employee();
dsEmployee.setEmployeeId(employee.getEmployeeId());
dsEmployee.setName(name);
dsEmployee.setAadharCardNumber(aadharCardNumber);
dsEmployee.setBasicSalary(basicSalary);
dsEmployee.setDateOfBirth((Date)dateOfBirth.clone());
dsEmployee.setIsIndian(isIndian);
dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designationCode));
dsEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
dsEmployee.setPANNumber(panNumber);
employeesSet.add(dsEmployee);
panNumberWiseEmployeesMap.put(dsEmployee.getPANNumber().toUpperCase(),dsEmployee);
aadharCardNumberWiseEmployeesMap.put(dsEmployee.getAadharCardNumber().toUpperCase(),dsEmployee);
employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(),dsEmployee);
Set<EmployeeInterface> ets=designationCodeWiseEmployeesMap.get(designationCode);
if(ets==null)
{
ets=new TreeSet<>();
ets.add(dsEmployee);
designationCodeWiseEmployeesMap.put(dsEmployee.getDesignation().getCode(),ets);
}
else
{
ets.add(dsEmployee);
}
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
}
}




public void updateEmployee(EmployeeInterface employee) throws BLException
{
BLException blException=new BLException();
if(employee==null)
{
blException.setGenericException("Employee is null");
}
String employeeId=employee.getEmployeeId();
if(employeeId==null)
{
blException.addException("employeeId","Employee id required");
}
employeeId=employeeId.trim();
if(employeeId.length()==0)
{
blException.addException("employeeId","Employee id required");
}
else
{
if(employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())==false)
{
blException.addException("employeeId","Invalid 1employee id");
throw blException;
}
}

String name=employee.getName();
DesignationInterface designation=employee.getDesignation();
int designationCode=0;
Date dateOfBirth=employee.getDateOfBirth();
char gender=employee.getGender();
boolean isIndian=employee.getIsIndian();
BigDecimal basicSalary=employee.getBasicSalary();
String panNumber=employee.getPANNumber();
String aadharCardNumber=employee.getAadharCardNumber();


if(name==null)
{
blException.addException("name","Name required");
}
else
{
name=name.trim();
if(name.length()==0)
{
blException.addException("name","Name required");
}
}
DesignationManagerInterface designationManager;
designationManager=DesignationManager.getDesignationManager();
if(designation==null)
{
blException.addException("designation","Designation required");
}
designationCode=designation.getCode();
if(designationManager.designationCodeExists(designationCode)==false)
{
blException.addException("designation","Invalid designation");
}

if(dateOfBirth==null)
{
blException.addException("dateOfBirth","Date of birth required");
}
if(gender==' ')
{
blException.addException("gender","Gender required");
}
if(basicSalary==null)
{
blException.addException("basicSalary","Basic salary required");
}
else
{
if(basicSalary.signum()==-1)
{
blException.addException("basicSalary","Basic salary cannot be negative");
}
}
if(panNumber==null)
{
blException.addException("panNumber","PAN number required");
}
else
{
panNumber=panNumber.trim();
if(panNumber.length()==0)
{
blException.addException("panNumber","PAN number required");
}
}

if(aadharCardNumber==null)
{
blException.addException("aadharCardNumber","Aadhar card number required");
}
else
{
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)
{
blException.addException("aadharCardNumber","Aadhar card number required");
}
}
if(panNumber!=null && panNumber.length()>0)
{
EmployeeInterface ee=panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
if(ee!=null && ee.getEmployeeId().equalsIgnoreCase(employeeId)==false)
{
blException.addException("panNumber","PAN number "+panNumber+" already assigned to another employee id");
}
}
if(aadharCardNumber!=null && aadharCardNumber.length()>0)
{
EmployeeInterface ee=aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
if(ee!=null && ee.getEmployeeId().equalsIgnoreCase(employeeId)==false)
{
blException.addException("aadharCardNumber","Aadhar card number "+aadharCardNumber+" already assigned to another employee id");
}
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
EmployeeInterface dsEmployee;
dsEmployee=employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
String oldPANNumber=dsEmployee.getPANNumber();
String oldAadharCardNumber=dsEmployee.getAadharCardNumber();
int oldDesignationCode=dsEmployee.getDesignation().getCode();
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(dsEmployee.getEmployeeId());
employeeDTO.setName(name);
employeeDTO.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE); 
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setAadharCardNumber(aadharCardNumber);
employeeDTO.setDateOfBirth((Date)dateOfBirth.clone());
employeeDTO.setIsIndian(isIndian);
employeeDTO.setPANNumber(panNumber);
employeeDTO.setDesignationCode(designationCode);
employeeDAO.update(employeeDTO);
dsEmployee.setAadharCardNumber(aadharCardNumber);
dsEmployee.setBasicSalary(basicSalary);
dsEmployee.setDateOfBirth((Date)dateOfBirth.clone());
dsEmployee.setDesignation(((DesignationManager)designationManager).getDesignationByCode(designation.getCode()));
dsEmployee.setIsIndian(isIndian);
dsEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
dsEmployee.setPANNumber(panNumber);
dsEmployee.setName(name);
//remove the old object from DS
employeesSet.remove(dsEmployee);
employeeIdWiseEmployeesMap.remove(dsEmployee.getEmployeeId().toUpperCase());
panNumberWiseEmployeesMap.remove(oldPANNumber.toUpperCase());
aadharCardNumberWiseEmployeesMap.remove(oldAadharCardNumber.toUpperCase());
designationCodeWiseEmployeesMap.remove(designationCode);

//add updated object in DS

employeesSet.add(dsEmployee);
panNumberWiseEmployeesMap.put(panNumber.toUpperCase(),dsEmployee);
aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(),dsEmployee);
employeeIdWiseEmployeesMap.put(employeeId.toUpperCase(),dsEmployee);
if(oldDesignationCode!=dsEmployee.getDesignation().getCode())
{
Set<EmployeeInterface> ets;
ets=designationCodeWiseEmployeesMap.get(oldDesignationCode);
ets.remove(dsEmployee);
ets=designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
if(ets==null)
{
ets=new TreeSet<>();
ets.add(dsEmployee);
designationCodeWiseEmployeesMap.put(dsEmployee.getDesignation().getCode(),ets);
}
else
{
ets.add(dsEmployee);
}
}
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}

}
public void removeEmployee(String employeeId) throws BLException
{
if(employeeId==null)
{
BLException blException=new BLException();
blException.addException("employeeId","Employee id required");
throw blException;
}
employeeId=employeeId.trim();
if(employeeId.length()==0)
{
BLException blException=new BLException();
blException.addException("employeeId","Employee id required");
throw blException;
}
else
{
if(employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())==false)
{
BLException blException=new BLException();
blException.addException("employeeId","Invalid employee id");
throw blException;
}
}

try
{
EmployeeInterface dsEmployee;
dsEmployee=employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
String PANNumber=dsEmployee.getPANNumber();
String AadharCardNumber=dsEmployee.getAadharCardNumber();
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
employeeDAO.delete(dsEmployee.getEmployeeId());

//remove the  object from DS

employeesSet.remove(dsEmployee);
employeeIdWiseEmployeesMap.remove(dsEmployee.getEmployeeId().toUpperCase());
panNumberWiseEmployeesMap.remove(PANNumber.toUpperCase());
aadharCardNumberWiseEmployeesMap.remove(AadharCardNumber.toUpperCase());
Set<EmployeeInterface> ets;
ets=designationCodeWiseEmployeesMap.get(dsEmployee);
ets.remove(dsEmployee);
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public EmployeeInterface getEmployeeByEmployeeId(String employeeId) throws BLException
{
BLException blException=new BLException();
if(employeeId==null)
{
blException.setGenericException("Employee id required");
throw blException;
}
employeeId=employeeId.trim();
if(employeeId.length()==0)
{
blException.setGenericException("Employee id required");
throw blException;
}
EmployeeInterface employee=this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
if(employee==null)
{
blException.addException("employeeId","Invalid Employee id");
throw blException;
}
EmployeeInterface e=new Employee();
e.setEmployeeId(employee.getEmployeeId());
e.setName(employee.getName());
e.setBasicSalary(employee.getBasicSalary());
e.setAadharCardNumber(employee.getAadharCardNumber());
e.setPANNumber(employee.getPANNumber());
e.setDateOfBirth((Date)employee.getDateOfBirth().clone());
DesignationInterface dsDesignation=employee.getDesignation();
DesignationInterface designation=new Designation();
designation.setCode(dsDesignation.getCode());
designation.setTitle(dsDesignation.getTitle());
e.setDesignation(designation);
e.setGender((employee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
e.setIsIndian(employee.getIsIndian());
return e;

}
public EmployeeInterface getEmployeeByPANNumber(String panNumber) throws BLException
{
BLException blException=new BLException();
if(panNumber==null)
{
blException.setGenericException("PAN number required");
throw blException;
}
panNumber=panNumber.trim();
if(panNumber.length()==0)
{
blException.setGenericException("PAN number required");
throw blException;
}
EmployeeInterface employee=this.panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
if(employee==null)
{
blException.addException("panNumber","Invalid PAN number");
throw blException;
}
EmployeeInterface e=new Employee();
e.setEmployeeId(employee.getEmployeeId());
e.setName(employee.getName());
e.setBasicSalary(employee.getBasicSalary());
e.setAadharCardNumber(employee.getAadharCardNumber());
e.setPANNumber(employee.getPANNumber());
e.setDateOfBirth((Date)employee.getDateOfBirth().clone());
DesignationInterface dsDesignation=employee.getDesignation();
DesignationInterface designation=new Designation();
designation.setCode(dsDesignation.getCode());
designation.setTitle(dsDesignation.getTitle());
e.setDesignation(designation);
e.setGender((employee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
e.setIsIndian(employee.getIsIndian());
return e;

}
public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber) throws BLException
{
BLException blException=new BLException();
if(aadharCardNumber==null)
{
blException.setGenericException("Aadhar card number required");
throw blException;
}
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)
{
blException.setGenericException("Aadhar card number required");
throw blException;
}
EmployeeInterface employee=this.aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
if(employee==null)
{
blException.addException("aadharCardNumber","Invalid Aadhar card number");
throw blException;
}
EmployeeInterface e=new Employee();
e.setEmployeeId(employee.getEmployeeId());
e.setName(employee.getName());
e.setBasicSalary(employee.getBasicSalary());
e.setAadharCardNumber(employee.getAadharCardNumber());
e.setPANNumber(employee.getPANNumber());
e.setDateOfBirth((Date)employee.getDateOfBirth().clone());
DesignationInterface dsDesignation=employee.getDesignation();
DesignationInterface designation=new Designation();
designation.setCode(dsDesignation.getCode());
designation.setTitle(dsDesignation.getTitle());
e.setDesignation(designation);
e.setGender((employee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
e.setIsIndian(employee.getIsIndian());
return e;
}
public int getEmployeeCount() 
{
return employeesSet.size();
}
public boolean employeeIdExists(String employeeId) 
{
return employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase());
}
public boolean employeePANNumberExists(String panNumber) 
{
return panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase());
}
public boolean employeeAadharCardNumberExists(String aadharCardNumber) 
{
return aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase());
}
public Set<EmployeeInterface> getEmployees()
{
Set<EmployeeInterface> employees=new TreeSet<>();
EmployeeInterface employee;
DesignationInterface dsDesignation;
DesignationInterface designation;
for(EmployeeInterface dsEmployee:employeesSet)
{
employee=new Employee();
employee.setEmployeeId(dsEmployee.getEmployeeId());
employee.setName(dsEmployee.getName());
employee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(dsEmployee.getIsIndian());
dsDesignation=dsEmployee.getDesignation();
designation=new Designation();
designation.setCode(dsDesignation.getCode());
designation.setTitle(dsDesignation.getTitle());
employee.setDesignation(designation);
employee.setBasicSalary(dsEmployee.getBasicSalary());
employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
employee.setPANNumber(dsEmployee.getPANNumber());
employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
employees.add(employee);
}
return employees;
}
public Set<EmployeeInterface> getEmployeesByDesignationCode(int designationCode) throws BLException
{
DesignationManagerInterface designationManager;
designationManager=DesignationManager.getDesignationManager();
if(designationManager.designationCodeExists(designationCode)==false)
{
BLException blException=new BLException();
blException.setGenericException("Invalid designation code :"+designationCode);
throw blException;
}
Set<EmployeeInterface> ets;
Set<EmployeeInterface> employees=new TreeSet<>();
ets=designationCodeWiseEmployeesMap.get(designationCode);
if(ets==null)
{
return employees;
}
EmployeeInterface employee;
DesignationInterface designation;
DesignationInterface dsDesignation;
for(EmployeeInterface dsEmployee:ets)
{
employee=new Employee();
employee.setEmployeeId(dsEmployee.getEmployeeId());
employee.setName(dsEmployee.getName());
employee.setGender((dsEmployee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(dsEmployee.getIsIndian());
dsDesignation=dsEmployee.getDesignation();
designation=new Designation();
designation.setCode(dsDesignation.getCode());
designation.setTitle(dsDesignation.getTitle());
employee.setDesignation(designation);
employee.setBasicSalary(dsEmployee.getBasicSalary());
employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
employee.setPANNumber(dsEmployee.getPANNumber());
employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
employees.add(employee);
}
return employees;
}
public int getEmployeeCountByDesignationCode(int designationCode) throws BLException
{
Set<EmployeeInterface> ets;
ets=this.designationCodeWiseEmployeesMap.get(designationCode);
if(ets==null) return 0;
else return ets.size();
}
public boolean designationAlloted(int designationCode) throws BLException
{
return this.designationCodeWiseEmployeesMap.containsKey(designationCode);
}

}

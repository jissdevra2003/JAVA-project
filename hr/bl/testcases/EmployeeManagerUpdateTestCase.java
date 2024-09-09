import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import java.util.*;
import java.math.*;
import java.text.*;
class EmployeeManagerUpdateTestCase
{
public static void main(String gg[])
{
try
{
String employeeId="A10000004";
String name="Ada Shelby";
String panNumber="ADNO90898";
String aadharCardNumber="UID129856789";
boolean isIndian=false;
DesignationInterface designation=new Designation();
designation.setCode(8);
Date dateOfBirth=new Date();
BigDecimal basicSalary=new BigDecimal("90000000.90");
EmployeeInterface employee;
employee=new Employee();
employee.setEmployeeId(employeeId);
employee.setName(name);
employee.setDateOfBirth(dateOfBirth);
employee.setAadharCardNumber(aadharCardNumber);
employee.setPANNumber(panNumber);
employee.setBasicSalary(basicSalary);
employee.setDesignation(designation);
employee.setIsIndian(isIndian);
employee.setGender(GENDER.MALE);
EmployeeManagerInterface employeeManager=EmployeeManager.getEmployeeManager();
employeeManager.updateEmployee(employee);
System.out.println("Employee updated successsfully");
}catch(BLException blException)
{
if(blException.hasGenericException())
{
System.out.println(blException.getGenericException());
}
List<String> properties=blException.getProperties();
for(String property:properties)
{
System.out.println(blException.getException(property));
}
}
}
}
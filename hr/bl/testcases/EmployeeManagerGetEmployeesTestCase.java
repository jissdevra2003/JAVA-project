import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import java.util.*;
import java.math.*;
import java.text.*;
class EmployeeManagerGetEmployeesTestCase
{
public static void main(String gg[])
{
try
{
EmployeeManagerInterface employeeManager=EmployeeManager.getEmployeeManager();
Set<EmployeeInterface> employees=employeeManager.getEmployees();
employees.forEach((employee)->
{
System.out.println("Employee id :"+employee.getEmployeeId());
System.out.println("Name :"+employee.getName());
System.out.println("Basic salary :"+employee.getBasicSalary());
System.out.println("Date of birth :"+employee.getDateOfBirth());
System.out.println("IsIndian :"+employee.getIsIndian());
System.out.println("Gender :"+employee.getGender());
System.out.println("PAN number :"+employee.getPANNumber());
System.out.println("Aadhar card number :"+employee.getAadharCardNumber());

});

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
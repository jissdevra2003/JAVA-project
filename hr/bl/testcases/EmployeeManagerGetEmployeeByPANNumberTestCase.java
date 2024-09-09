import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import java.util.*;
import java.math.*;
import java.text.*;
class EmployeeManagerGetEmployeeByPANNumberTestCase
{
public static void main(String gg[])
{
String panNumber=gg[0];
try
{
EmployeeManagerInterface employeeManager=EmployeeManager.getEmployeeManager();
EmployeeInterface employee=employeeManager.getEmployeeByPANNumber(panNumber);
System.out.println("Employee id :"+employee.getEmployeeId());
System.out.println("PAN number :"+employee.getPANNumber());
System.out.println("Name :"+employee.getName());
System.out.println("Gender :"+employee.getGender());
System.out.println("Salary :"+employee.getBasicSalary());

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
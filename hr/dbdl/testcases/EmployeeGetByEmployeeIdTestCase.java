import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
public class EmployeeGetByEmployeeIdTestCase
{
public static void main(String gg[])
{
String employeeId=gg[0];
try
{
EmployeeDTOInterface employeeDTO;
EmployeeDAOInterface employeeDAO;
employeeDAO=new EmployeeDAO();
employeeDTO=employeeDAO.getByEmployeeId(employeeId);
System.out.println("PAN number :"+employeeDTO.getPANNumber());
System.out.println("Employee id :"+employeeDTO.getEmployeeId()+"Name :"+employeeDTO.getName());
System.out.println("Gender :"+employeeDTO.getGender());
System.out.println("Aadhar card number :"+employeeDTO.getAadharCardNumber());
System.out.println("Is Indian :"+employeeDTO.getIsIndian());


}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}
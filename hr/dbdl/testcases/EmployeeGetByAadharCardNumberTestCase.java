import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
public class EmployeeGetByAadharCardNumberTestCase
{
public static void main(String gg[])
{
String aadharCardNumber=gg[0];
try
{
EmployeeDTOInterface employeeDTO;
EmployeeDAOInterface employeeDAO;
employeeDAO=new EmployeeDAO();
employeeDTO=employeeDAO.getByAadharCardNumber(aadharCardNumber);
System.out.println("Aadhar card  number :"+employeeDTO.getAadharCardNumber());
System.out.println("Employee id :"+employeeDTO.getEmployeeId()+"Name :"+employeeDTO.getName());
}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}
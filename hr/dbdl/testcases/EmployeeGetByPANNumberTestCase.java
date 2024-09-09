import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
public class EmployeeGetByPANNumberTestCase
{
public static void main(String gg[])
{
String panNumber=gg[0];
try
{
EmployeeDTOInterface employeeDTO;
EmployeeDAOInterface employeeDAO;
employeeDAO=new EmployeeDAO();
employeeDTO=employeeDAO.getByPANNumber(panNumber);
System.out.println("PAN number :"+employeeDTO.getPANNumber());
System.out.println("Employee id :"+employeeDTO.getEmployeeId()+"Name :"+employeeDTO.getName());
}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}
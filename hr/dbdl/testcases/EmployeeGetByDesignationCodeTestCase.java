import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import java.util.*;
public class EmployeeGetByDesignationCodeTestCase
{
public static void main(String gg[])
{
int code=Integer.parseInt(gg[0]);
try
{

Set<EmployeeDTOInterface> employees;
EmployeeDAOInterface employeeDAO;
employeeDAO=new EmployeeDAO();
employees=employeeDAO.getByDesignationCode(code);
employees.forEach((p)->
{
System.out.println("Employee id :"+p.getEmployeeId()+", Name :"+p.getName()+", PAN number :"+p.getPANNumber());

});



}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}
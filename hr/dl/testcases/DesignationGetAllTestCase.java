import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import java.util.*;
public class DesignationGetAllTestCase
{
public static void main(String gg[])
{
try
{
Set<DesignationDTOInterface> designations;
DesignationDAOInterface designationDAO;
designationDAO=new DesignationDAO();
designations=designationDAO.getAll();
designations.forEach((p)->
{
System.out.printf("Code : %d , Title : %s\n",p.getCode(),p.getTitle());
});
}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}
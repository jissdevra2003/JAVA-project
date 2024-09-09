import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
class DesignationManagerUpdateTestCase
{
public static void main(String gg[])
{
DesignationInterface designation;
designation=new Designation();
designation.setCode(12);
designation.setTitle("Engineer");
try
{
DesignationManagerInterface designationManager;
designationManager=DesignationManager.getDesignationManager();
designationManager.updateDesignation(designation);
System.out.println("Designation updated successfully");
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
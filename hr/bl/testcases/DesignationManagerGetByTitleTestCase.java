import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
class DesignationManagerGetByTitleTestCase
{
public static void main(String gg[])
{
String title=gg[0];
title=title.toUpperCase();
DesignationInterface designation;
try
{
DesignationManagerInterface designationManager;
designationManager=DesignationManager.getDesignationManager();
designation=designationManager.getDesignationByTitle(title);
System.out.println("Designation :"+designation.getTitle()+"Code is :"+designation.getCode());
}catch(BLException blException)
{
if(blException.hasGenericException())
{
System.out.println(blException.getGenericException());
}
}
}
}
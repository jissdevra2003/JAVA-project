package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import java.util.*;
public class DesignationManager implements DesignationManagerInterface
{
private Map<Integer,DesignationInterface> codeWiseDesignationsMap;
private Map<String,DesignationInterface> titleWiseDesignationsMap;
private Set<DesignationInterface> designationsSet;
private static DesignationManager designationManager=null;
private DesignationManager() throws BLException
{
populateDataStructures();
}
public static DesignationManager getDesignationManager() throws BLException
{
if(designationManager==null) designationManager=new DesignationManager();
return designationManager;
}
private void populateDataStructures() throws BLException
{
this.codeWiseDesignationsMap=new HashMap<>();
this.titleWiseDesignationsMap=new HashMap<>();
this.designationsSet=new TreeSet<>();
try
{
Set<DesignationDTOInterface> dlDesignations;
dlDesignations=new DesignationDAO().getAll();
DesignationInterface designation;
for(DesignationDTOInterface dlDesignation:dlDesignations)
{
designation=new Designation();
designation.setCode(dlDesignation.getCode());
designation.setTitle(dlDesignation.getTitle());
this.codeWiseDesignationsMap.put((new Integer(designation.getCode())),designation);
this.titleWiseDesignationsMap.put(designation.getTitle().toUpperCase(),designation);
this.designationsSet.add(designation);
}
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public void addDesignation(DesignationInterface designation) throws BLException
{
BLException blException;
blException=new BLException();
if(designation==null)
{
blException.setGenericException("Designation required");
throw blException;
}
String title=designation.getTitle();
int code=designation.getCode();
if(code!=0)
{
blException.addException("code","Code should be zero");
}
if(title==null)
{
blException.addException("title","Title required");
title="";
}
else
{
title=title.trim();
if(title.length()==0)
{
blException.addException("title","Title required");
}
}
if(title.length()>0)
{
if(this.titleWiseDesignationsMap.containsKey(title.toUpperCase()))
{
blException.addException("title","Title "+title+" already exists");
}
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
DesignationDTOInterface designationDTO;
designationDTO=new DesignationDTO();
designationDTO.setTitle(title);
DesignationDAOInterface designationDAO;
designationDAO=new DesignationDAO();
designationDAO.add(designationDTO);
code=designationDTO.getCode();
designation.setCode(code);
DesignationInterface dsDesignation;
dsDesignation=new Designation();
dsDesignation.setCode(code);
dsDesignation.setTitle(title);
this.codeWiseDesignationsMap.put(new Integer(code),dsDesignation);
this.titleWiseDesignationsMap.put(title.toUpperCase(),dsDesignation);
this.designationsSet.add(dsDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
}
}
public void removeDesignation(int code) throws BLException
{
BLException blException=new BLException();
if(code<=0)
{
blException.addException("code","Invalid code");
throw blException;
}
if(code>0)
{
if(!this.codeWiseDesignationsMap.containsKey(code))
{
blException.addException("code","Invalid code :"+code);
throw blException;
}
}
try
{
DesignationInterface designation=this.codeWiseDesignationsMap.get(code);
System.out.println(designation.getTitle()+" To remove");
new DesignationDAO().delete(code);
this.codeWiseDesignationsMap.remove(code);
this.titleWiseDesignationsMap.remove(designation.getTitle());
this.designationsSet.remove(designation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public void updateDesignation(DesignationInterface designation) throws BLException
{
BLException blException;
blException=new BLException();
if(designation==null)
{
blException.setGenericException("Designation required");
throw blException;
}
String title=designation.getTitle();
int code=designation.getCode();
if(code<=0)
{
blException.addException("code","Invalid code");
}
if(code>0)
{
if(!this.codeWiseDesignationsMap.containsKey(code))
{
blException.addException("code","Invalid code :"+code);
}
}
if(title==null)
{
blException.addException("title","Title required");
title="";
}
else
{
title=title.trim();
if(title.length()==0)
{
blException.addException("title","Title required");
}
}
if(title.length()>0)
{
DesignationInterface d;
d=this.titleWiseDesignationsMap.get(title.toUpperCase());
if(d!=null && d.getCode()!=code)
{
blException.addException("title","Designation "+title+" already exists");
}
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
DesignationInterface DSdesignation=this.codeWiseDesignationsMap.get(code);
System.out.println(DSdesignation.getTitle()+" To update");
DesignationDTOInterface dlDesignation=new DesignationDTO();
dlDesignation.setCode(code);
dlDesignation.setTitle(title);
new DesignationDAO().update(dlDesignation);
this.codeWiseDesignationsMap.remove(code);
this.titleWiseDesignationsMap.remove(DSdesignation.getTitle().toUpperCase());
this.designationsSet.remove(DSdesignation);

DSdesignation.setTitle(title);
this.codeWiseDesignationsMap.put(code,DSdesignation);
this.titleWiseDesignationsMap.put(title.toUpperCase(),DSdesignation);
this.designationsSet.add(DSdesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
}

}
public DesignationInterface getDesignationByCode(int code) throws BLException
{
DesignationInterface designation=null;
if(code<=0) return designation;
if(!this.codeWiseDesignationsMap.containsKey(code)) return designation;
designation=this.codeWiseDesignationsMap.get(code);
DesignationInterface d=new Designation();
d.setCode(designation.getCode());
d.setTitle(designation.getTitle());
return d;
}
DesignationInterface getDSDesignationByCode(int code)
{
DesignationInterface designation=codeWiseDesignationsMap.get(code);
return designation;
}
public DesignationInterface getDesignationByTitle(String title) throws BLException
{
DesignationInterface designation;
designation=this.titleWiseDesignationsMap.get(title.toUpperCase());
if(designation==null)
{
BLException blException;
blException=new BLException();
blException.addException("title","Invalid title :"+title);
throw blException;
}
DesignationInterface d=new Designation();
d.setCode(designation.getCode());
d.setTitle(designation.getTitle());
return d;
}
public int getDesignationCount()
{
return this.designationsSet.size();
}
public boolean designationCodeExists(int code)
{
return this.codeWiseDesignationsMap.containsKey(code);
}
public boolean designationTitleExists(String title)
{
return this.titleWiseDesignationsMap.containsKey(title.toUpperCase());
}
public Set<DesignationInterface> getDesignations()
{
Set<DesignationInterface> designations;
designations=new TreeSet<>();
this.designationsSet.forEach((designation)->{
DesignationInterface d=new Designation();
d.setTitle(designation.getTitle());
d.setCode(designation.getCode());
designations.add(d);
});
return designations;
}

}
/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package cm.success.main;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes.Name;

import javax.annotation.PostConstruct;
import javax.xml.bind.ParseConversionEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.content.Professional;
import com.content.School;
import com.content.SchoolPost;
import com.content.SchoolProfessional;
import com.csvreader.CsvWriter;
import com.tool.util.Filter;
import com.util.FileUtil;
import com.util.SendUtil;
import com.util.TextUtil;
import com.util.UrlUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * ControllMain.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Dec 14, 2017$
 * @since 1.0
 */
public class ControllSchoolMain
{
    public static final String bigURL = "http://yz.chsi.com.cn";
    public static final String schoolURL = "http://yz.chsi.com.cn/sch/search.do?start="; 
    public static final Integer page = 44;
    public static final Integer pageSize = 20;
    public static final String path = "." + File.separator + "school"+ File.separator + "tmp";
    public static final String pathSchool = path+ File.separator + "school";
    public static final String pathResult = "." + File.separator + "school"+ File.separator + "result";
    public static final String introduceSchool = path+ File.separator + "introduce";
    public static final String schoolProfessional = path+ File.separator + "professional";
    
    public static final String postSchool = "http://39.104.60.7:8080/success/school/updateSchool";
    public static final String searchSchool = "http://39.104.60.7:8080/success/school/searchSchool";
    
    public static final String getURL = "http://39.104.60.7:8080/success/professional/getAllProfessional";
    public static Map<String, Integer> schoolProfessionalMapping = new HashMap<String, Integer>();
    
    public static int schoolcount = 1;
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        getPostProfessional();
        for (int i = 0; i < page; i ++)
        {
            try
            {
                String url = schoolURL + (i * pageSize);
                System.out.println(i + "   " +url);
                File destFile = new File(pathSchool, i + ".html");
                //UrlUtil.download(new URL(url), destFile, 120*60);
                String contentHtml = FileUtil.getFileContent(destFile.getAbsolutePath());
                int start = contentHtml.indexOf("<tbody>");
                int end = contentHtml.indexOf("</tbody>") + 8;
                String content = contentHtml.substring(start, end);
                content = Filter.DeleteSymbol(content);
                List<School> schools = getSchools(content);
                save(schools, pathResult + File.separator + i + ".csv");
                postSchool(schools);
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    /**
     * @param schools
     */
    private static void postSchool(List<School> schools)
    {
        for(School school : schools)
        {
            Integer schoolId = getSchoolId(school);
            if (schoolId == null || schoolId == 0)
            {
                System.out.println("NOT PASSSS========================" + school.getName());
            }
            else
                continue;
            
            System.out.println("Start post========================" + school.getName());
            getPostProfessional();
            
            SchoolPost schoolPost = new SchoolPost();
            
            schoolPost.setId(schoolId);
            schoolPost.setName(school.getName());
            schoolPost.setProvince(school.getProvince());
            schoolPost.setSchoolProfessionals(school.getSchoolProfessionals());
            
            JSONObject schoolProfessionalJson = JSONObject.fromObject(schoolPost);
            //System.out.println(schoolProfessionalJson);
            String result = "failed";
            while ("failed".equals(result))
            {
                result = SendUtil.sendPost(postSchool, schoolProfessionalJson.toString());
                try
                {
                    Thread.sleep(1000);
                    if (!"failed".equals(result))
                        System.out.println("success========================" + school.getName());
                    
                    schoolId = getSchoolId(school);
                    schoolPost.setId(schoolId);
                    schoolProfessionalJson = JSONObject.fromObject(schoolPost);
                    
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            
            
        }
        
    }

    /**
     * @param school
     * @return
     */
    private static Integer getSchoolId(School school)
    {
        String schoolString = SendUtil.sendGet(searchSchool);
        
        JSONArray jsonArray = JSONArray.fromObject(schoolString);
        Integer schoolId = 0;
        for (int i = 0; i < jsonArray.size(); i ++)
        {
            JSONObject jo = (JSONObject) jsonArray.get(i);
            Integer id = jo.getInt("id");
            String province = jo.getString("province");
            String name = jo.getString("name");
            
            if (school.getProvince().equals(province) 
                    && school.getName().equals(name))
            {
                schoolId = id;
                break;
            }
        }
        
        return schoolId;
    }

    private static void getPostProfessional()
    {
        String professionalString = SendUtil.sendGet(getURL);
        //System.out.println(professionalString);
        JSONArray jsonArray = JSONArray.fromObject(professionalString);
        List<Professional> professionals = new ArrayList<Professional>();
        for (int i = 0; i < jsonArray.size(); i ++)
        {
            Professional professional = new Professional();
            JSONObject jo = (JSONObject) jsonArray.get(i);
            professional.setId(jo.getInt("id"));
            professional.setName(jo.getString("name"));
            professional.setCode(jo.getString("code"));
            professionals.add(professional);
        }
        
        for(Professional professional : professionals)
        {
            //System.out.println(professional.getCode() + "  " + professional.getName() + "   " + professional.getCode());
            schoolProfessionalMapping.put(professional.getCode(), professional.getId());
        }
        
    }
    
    /**
     * @param content
     * @return
     */
    private static List<School> getSchools(String content)
    {
        //System.out.println(content);
        List<School> schools = new ArrayList<School>();
        try
        {
            //得到DOM解析器的工厂实例
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            //从DOM工厂中获得DOM解析器
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            //把要解析的xml文档读入DOM解析器
            Document doc = dbBuilder.parse(new InputSource(new StringReader(content)));
            NodeList nList = doc.getElementsByTagName("tr");
            //遍历该集合，显示结合中的元素及其子元素的名字
            for(int i = 0; i< nList.getLength() ; i ++)
            {
                try
                {
                    School school = new School();
                    Element node = (Element) nList.item(i);
                    Element nameNode = (Element) node.getElementsByTagName("td").item(0).getFirstChild();
                    String name = nameNode.getFirstChild().getNodeValue();
                    school.setName(name);
                    
                    String schoolURL = nameNode.getAttribute("href");
                    schoolURL = bigURL + schoolURL;
                    //System.out.println("url  " + schoolURL);
                    File schoolIntro = new File(introduceSchool, school.getName() + ".html");
                    if (!schoolIntro.exists())
                        UrlUtil.download(new URL(schoolURL), schoolIntro, 120*60);
                    List<SchoolProfessional> schoolProfessionals = parseProfessional(schoolIntro);
                    school.setSchoolProfessionals(schoolProfessionals);
                    String province = node.getElementsByTagName("td").item(1).getFirstChild().getNodeValue();
                    school.setProvince(province);
                    String memberShip = null;
                    try
                    {
                        memberShip = node.getElementsByTagName("td").item(2).getFirstChild().getNodeValue();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    school.setMemberShip(memberShip);
                    String level = node.getElementsByTagName("td").item(3).getTextContent();
                    school.setIs211(level.indexOf("211") != -1 ? Boolean.TRUE : null);
                    school.setIs985(level.indexOf("985") != -1 ? Boolean.TRUE : null);
                    schools.add(school);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return schools;
    }
    
    /**
     * @param schoolIntro
     * @return
     */
    private static List<SchoolProfessional> parseProfessional(File schoolIntro)
    {
        List<SchoolProfessional> schoolProfessionals = new ArrayList<SchoolProfessional>();
        try
        {
            if (!schoolIntro.exists())
                return schoolProfessionals;
            
            String contentHtml = FileUtil.getFileContent(schoolIntro.getAbsolutePath());
            int start = contentHtml.indexOf("<div class=\"yxk-content\">");
            int end = contentHtml.indexOf("</div>", start) + 6;
            String content = contentHtml.substring(start, end);
            content = Filter.DeleteSymbol(content);
            String professionalUrl = getSchoolProfessionalUrl(content);
            //System.out.println(professionalUrl);
            File schoolProfessionalPath = new File(schoolProfessional, "professional" + schoolIntro.getName());
            //UrlUtil.download(new URL(professionalUrl), schoolProfessionalPath, 120*60);
            if (schoolProfessionalPath.exists())
            {
                String contentProHtml = FileUtil.getFileContent(schoolProfessionalPath.getAbsolutePath());
                if (contentProHtml.indexOf("该招生单位没有硕士专业。") != -1)
                {
                    System.out.println("这个学校没有硕士   " + schoolProfessionalPath.getAbsolutePath());
                    return schoolProfessionals;
                }
                
                int pStart = contentProHtml.indexOf("<div class=\"tab-content\">");
                int pEnd = contentProHtml.indexOf("<div class=\"yxk-column-title-left\">", pStart);
                String pContent = contentProHtml.substring(pStart, pEnd);
                pContent = "<div>" + pContent;
                pContent = Filter.DeleteSymbol(pContent);
                //System.out.println(pContent);
                
                //System.out.println(schoolProfessionalPath.getAbsolutePath() + "=====");
                
                //得到DOM解析器的工厂实例
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                //从DOM工厂中获得DOM解析器
                DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
                //把要解析的xml文档读入DOM解析器
                Document doc = dbBuilder.parse(new InputSource(new StringReader(pContent)));
                NodeList nList = doc.getElementsByTagName("li");
                //遍历该集合，显示结合中的元素及其子元素的名字
                for(int i = 0; i< nList.getLength() ; i ++)
                {
                    SchoolProfessional sp = new SchoolProfessional();
                    
                    Element node = (Element) nList.item(i);
                    NodeList aList = node.getElementsByTagName("a");
                    String professional = null;
                    if (aList.getLength() >0)
                        professional = aList.item(0).getFirstChild().getNodeValue();
                    else
                        professional = node.getFirstChild().getNodeValue();
                    
                    String[] pSplit = professional.split("\\[");
                    //System.out.println(pSplit[0] + "   " + pSplit[1].replace("]", ""));
                    String name = pSplit[0];
                    String code = pSplit[1].replace("]", "");
                    Integer spId = schoolProfessionalMapping.get(code);
                    if (spId == null)
                    {
                        while (true)
                        {
                            getPostProfessional();
                            spId = schoolProfessionalMapping.get(code);
                            
                            if (spId != null)
                                break;
                            else
                            {
                                ControlProfessionalMain.postSchoolProfessional(name, code);
                            }
                        }
                    }
                    
                    sp.setId(0);
                    sp.setSchoolId(0);
                    sp.setProfessionalId(spId);
                    schoolProfessionals.add(sp);
                    
                }
                
               // System.out.println(schoolProfessionalPath.getAbsolutePath() + "=====end  " 
               //                     + (schoolProfessionals.size() == nList.getLength() ? "success" : "failed"));
            }
        }
        catch (Exception e)
        {
            System.out.println("==== 没有内容 " + schoolIntro.getAbsolutePath());
            e.printStackTrace();
        }
        return schoolProfessionals;
    }

    /**
     * @param content
     * @return
     */
    private static String getSchoolProfessionalUrl(String content)
    {
        String url = null;
        try
        {
            //得到DOM解析器的工厂实例
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            //从DOM工厂中获得DOM解析器
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            //把要解析的xml文档读入DOM解析器
            Document doc = dbBuilder.parse(new InputSource(new StringReader(content)));
            NodeList nList = doc.getElementsByTagName("li");
            Element node = (Element) nList.item(2);
            Element nameNode = (Element) node.getElementsByTagName("a").item(0);
            url = nameNode.getAttribute("href");
            url = bigURL + url;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return url;
    }

    public static void save(List<School> schools, String csvFilePath)
    {
        try 
        {  
            // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);  
            CsvWriter csvWriter = new CsvWriter(csvFilePath, ',', Charset.forName("UTF-8"));  
            // 写表头  
            String[] csvHeaders = { "name", "province", "memberShip" , "985", "211"};  
            csvWriter.writeRecord(csvHeaders);  
            // 写内容  
            for (School school : schools) 
            {  
                String[] csvContent = { school.getName(), school.getProvince(), school.getMemberShip(), 
                        Boolean.TRUE.equals(school.getIs985()) ? "1" : "0" , 
                        Boolean.TRUE.equals(school.getIs211()) ? "1" : "0" };  
                csvWriter.writeRecord(csvContent);  
            }  
            csvWriter.close();  
            System.out.println("--------CSV文件已经写入--------" + csvFilePath);  
        } 
        catch (IOException e) 
        {  
            e.printStackTrace();  
        }  

    }

}

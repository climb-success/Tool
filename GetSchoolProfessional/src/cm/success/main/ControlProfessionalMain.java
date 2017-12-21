/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package cm.success.main;

import java.io.File;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.content.Professional;
import com.content.School;
import com.tool.util.Filter;
import com.util.FileUtil;
import com.util.SendUtil;
import com.util.UrlUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * ControlProfessionalMain.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Dec 15, 2017$
 * @since 1.0
 */
public class ControlProfessionalMain
{
    public static final String URL = "http://yz.chsi.com.cn";
    
    public static final String categoryURLBig = "http://yz.chsi.com.cn/zyk/specialityCategory.do?method=subCategoryMl&key=";
    public static final String professionalURLBig = "http://yz.chsi.com.cn/zyk/specialityCategory.do?method=subCategoryXk&key=";
    
    public static final Integer page = 44;
    public static final Integer pageSize = 20;
    public static final String path = "." + File.separator + "professional" + File.separator + "tmp";
    public static final String pathCategory = "." + File.separator + "professional" + File.separator + "tmp" + File.separator + "category";
    public static final String pathSubCategory = "." + File.separator + "professional" + File.separator + "tmp" + File.separator + "subcategory";
    public static final String professional = "." + File.separator + "professional" + File.separator + "tmp" + File.separator + "professional";
    public static final String pathSchool = "." + File.separator + "professional" + File.separator + "tmp" + File.separator + "schoolProfessional";
    
    
    
    public static final String pathResult = "." + File.separator + "professional" + File.separator + "result";
    
    public static final String postURL = "http://39.104.60.7:8080/success/professional/updateProfessional";
    public static final String getURL = "http://39.104.60.7:8080/success/professional/getAllProfessional";
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        generateMaster(10);
        generateMaster(20);
        //generateMaster(30);
        //generateMaster(40);
        getPostProfessional();
    }
    
    private static void getPostProfessional()
    {
        String professionalString = SendUtil.sendGet(getURL);
        System.out.println(professionalString);
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
        
        int i = 1;
        for(Professional professional : professionals)
        {
            System.out.println(i + "   " + professional.getCode() + "  " + professional.getName() + "   " + professional.getCode());
            i ++;
        }
        
    }


    private static void generateMaster(int category)
    {
        List<Professional> professionals = new ArrayList<Professional>();
        
        try
        {
            String categoryURL = categoryURLBig + category;
            System.out.println(categoryURL);
            File destFile = new File(pathCategory, category + ".html");
            //UrlUtil.download(new URL(categoryURL), destFile, 120*60);
            String categoryHtml = FileUtil.getFileContent(destFile.getAbsolutePath());
            String categoryContent = Filter.DeleteSymbol(categoryHtml);
            categoryContent = "<tbody>" + categoryContent +"</tbody>";
            List<String> categoryNum = getCategory(categoryContent);
            for (String num : categoryNum)
            {
                String categorySubURL = categoryURLBig + num;
                //System.out.println(categorySubURL);
                File subFile = new File(pathSubCategory, num + ".html");
                //UrlUtil.download(new URL(categorySubURL), subFile, 120*60);
                String categorySubHtml = FileUtil.getFileContent(subFile.getAbsolutePath());
                String categorySubContent = Filter.DeleteSymbol(categorySubHtml);
                categorySubContent = "<tbody>" + categorySubContent +"</tbody>";
                List<String> categorySubNum = getCategory(categorySubContent);
                //System.out.println(categorySubNum.toString());
                for (String subNum : categorySubNum)
                {
                    String professionalURL = professionalURLBig + subNum;
                    System.out.println(professionalURL);
                    File professionalFile = new File(professional, subNum + ".html");
                    //UrlUtil.download(new URL(professionalURL), professionalFile, 120*60);
                    String professionalbHtml = FileUtil.getFileContent(professionalFile.getAbsolutePath());
                    String professionalContent = Filter.DeleteSymbol(professionalbHtml);
                    getProfessional(professionalContent, professionals);
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        //getSchoolProfessional(professionals);
        postSchoolProfessional(professionals);
    }

    public static void postSchoolProfessional(String name, String code)
    {
        Professional professional = new Professional();
        professional.setCode(code);
        professional.setName(name);
        List<Professional> professionals = new ArrayList<Professional>();
        professionals.add(professional);
        postSchoolProfessional(professionals);
    }

    /**
     * @param professionals
     */
    public static void postSchoolProfessional(List<Professional> professionals)
    {
        int i = 1;
        for (Professional professional : professionals)
        {
            professional.setId(0);
            //System.out.println(professional.getName() + "  " + professional.getCode());
            JSONObject professionalJson = JSONObject.fromObject(professional);
            System.out.println(i + "  " +professionalJson.toString());
            String result = "failed";
            while ("failed".equals(result))
            {
                result = SendUtil.sendPost(postURL, professionalJson.toString());
                try
                {
                    if ("failed".equals(result))
                        Thread.sleep(1000);
                    else
                        System.out.println("success");
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            
            i ++ ;
        }
    }


    /**
     * @param professionals
     */
/*    private static void getSchoolProfessional(List<Professional> professionals)
    {
        
            for (Professional professional : professionals)
            {
                String schoolURL = professional.getSchoolURL();
                schoolURL = schoolURL.replaceAll("URLzydm", "&zydm");
                schoolURL = schoolURL.replaceAll("URLssdm", "&ssdm");
                schoolURL = schoolURL.replaceAll("URLmethod", "&method");
                schoolURL = schoolURL.replaceAll("URLcckey", "&cckey");
                schoolURL = schoolURL.replaceAll("URLzymc", "&zymc");
                schoolURL = URL + schoolURL;
                try
                {
                    File schoolFile = new File(pathSchool, professional.getCode() + "school.html");
                    //UrlUtil.download(new URL(schoolURL), schoolFile, 120*60);
                    if (!schoolFile.exists())
                        System.out.println("error    " + schoolURL);
                    //String schoolHtml = FileUtil.getFileContent(schoolFile.getAbsolutePath());
                    //String schoolContent = Filter.DeleteSymbol(schoolHtml);
                    //getProfessional(professionalContent, professionals);
                }
                catch (Exception e)
                {
                    System.out.println("error    " + schoolURL);
                    e.printStackTrace();
                    
                }
            }
        
    }*/


    private static void getProfessional(String content,
            List<Professional> professionals)
    {
      //System.out.println(content);
        content = content.replaceAll("&zydm", "URLzydm");
        content = content.replaceAll("&ssdm", "URLssdm");
        content = content.replaceAll("&method", "URLmethod");
        content = content.replaceAll("&cckey", "URLcckey");
        content = content.replaceAll("&zymc", "URLzymc");
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
            for(int i = 1; i< nList.getLength() ; i ++)
            {
                try
                {
                    Professional professional = new Professional();
                    Element node = (Element) nList.item(i);
                    String name = node.getElementsByTagName("td").item(0).getFirstChild().getFirstChild().getNodeValue();
                    professional.setName(name);
                    String code = node.getElementsByTagName("td").item(1).getFirstChild().getNodeValue();
                    professional.setCode(code);
                    
                    Element nodeHref = (Element) node.getElementsByTagName("td").item(2).getFirstChild();
                    String href = nodeHref.getAttribute("href");
                    //TODO the professional ur;
                    //professional.setSchoolURL(href);
                    professionals.add(professional);
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
    }


    /**
     * @param categoryContent
     * @return
     */
    private static List<String> getCategory(String content)
    {
      //System.out.println(content);
        List<String> categoryNum = new ArrayList<String>();
        try
        {
            //得到DOM解析器的工厂实例
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            //从DOM工厂中获得DOM解析器
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            //把要解析的xml文档读入DOM解析器
            Document doc = dbBuilder.parse(new InputSource(new StringReader(content)));
            NodeList nList = doc.getElementsByTagName("li");
            //遍历该集合，显示结合中的元素及其子元素的名字
            for(int i = 0; i< nList.getLength() ; i ++)
            {
                try
                {
                    Element node = (Element) nList.item(i);
                    categoryNum.add(node.getAttribute("id"));
                    
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
        
        return categoryNum;
    }
}

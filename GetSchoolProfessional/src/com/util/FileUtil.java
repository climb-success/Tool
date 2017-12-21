/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package com.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * FileUtil.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Dec 13, 2017$
 * @since 1.0
 */
public class FileUtil
{
    private static Logger log = Logger.getLogger(FileUtil.class);
    
    /**
     * Copy file from inputFile to outputFile.
     * @param inputFile
     * @param outputFile the directory should exists.
     * @return true if succeeds.
     * @throws IOException
     * @throws IOException
     */
    public static boolean copy(File inputFile, File outputFile)
        throws IOException
    {
        return copy(inputFile, outputFile, false);
    }
    
    /**
     * Copy file from inputFile to outputFile.
     * @param inputFile
     * @param outputFile the directory should exists.
     * @param overwriteFlag overwriteFlag
     * @return true if succeeds.
     * @throws IOException
     * @throws IOException
     */
    public static boolean copy(File inputFile, File outputFile, boolean overwriteFlag)
            throws IOException
    {
        return copy(inputFile, outputFile, overwriteFlag, false);
    }
    
    /**
     * Moves src file to new file.
     * @param inputFile the src file
     * @param outputFile the dest file
     * @return true if succeeds
     * @throws IOException
     */
    public static boolean move(File inputFile, File outputFile)
            throws IOException
    {
        return copy(inputFile, outputFile, true, true);
    }

    public static boolean copy(File inputFile, File outputFile,
            boolean overwriteFlag, boolean deleteOnCopy) throws IOException
    {
        if (inputFile == null || !inputFile.exists() || !inputFile.isFile()
                || !inputFile.canRead())
        {
            log.error("Source file does not exist or can not be read: "+ inputFile.getAbsolutePath()+ ". Copy skipped." );
            return false;
        }
        
        if (!outputFile.getParentFile().exists())
        {
            if (!outputFile.getParentFile().mkdirs())
            {
                log.error("Can not make dir for file:" + outputFile.getAbsolutePath()+ ". Copy skipped.");
                return false;
            }
        }
        
        if (!outputFile.getParentFile().canWrite())
        {
            log.error("Out put file:" + outputFile.getAbsolutePath()
                    + " can not be write.");
            return false;
        }
        
        if (outputFile.exists() && !outputFile.canWrite())
        {
            log.error("Out put file:" + outputFile.getAbsolutePath()
                    + " can not be write.");
            return false;
        }
        
        if (!overwriteFlag && outputFile.exists() && outputFile.length()==inputFile.length())
        {
            log.warn("Dest file: "+ outputFile.getAbsolutePath() +" already exists (same length), copy operation skipped!");
            return true;
        }
        
        if (inputFile.getAbsolutePath().equalsIgnoreCase(
                outputFile.getAbsolutePath()))
        {
            log.warn("Source file and dest file are same file.");
            return false;
        }

        doCopy(inputFile, outputFile, deleteOnCopy);
        return true;
    }
    
    private static void doCopy(File sourceFile, File destFile, boolean deleteOnCopy)
            throws FileNotFoundException, IOException
    {
        if (deleteOnCopy)
        {
            boolean destFileExists = destFile.exists();
            if (destFileExists)
                destFileExists = (!destFile.delete());

            if (!destFileExists)
            {
                if (sourceFile.renameTo(destFile))
                {
                    log.info("File renamed from: " + sourceFile.getAbsolutePath()
                                + "\n to: " + destFile.getAbsolutePath());
                    return;
                }
                
                destFile.createNewFile();
            }
        }
        
        FileInputStream is = null;
        FileOutputStream os = null;
        FileChannel source = null;
        FileChannel destination = null;
        try
        {
            source = (is = new FileInputStream(sourceFile)).getChannel();
            destination = (os = new FileOutputStream(destFile)).getChannel();

            int maxCount = (16 * 1024 * 1024) - (32 * 1024); // 16MB in buffer
            long size = source.size();
            long position = 0;
            while (position < size)
            {
                position += source.transferTo(position, maxCount, destination);
            }
        }
        finally
        {
            IOUtil.close(is);
            IOUtil.close(os);
            IOUtil.close(source);
            IOUtil.close(destination);
        }
        
        if (deleteOnCopy && !sourceFile.delete())
        {
            log.warn("Failed to delete source file: " + sourceFile);
        }
        
        log.info("File copy from:" + sourceFile.getAbsolutePath() + "\n to: "
                + destFile.getAbsolutePath());
    }

    /**
     * Copy file from inputFile to outputFile then delete inputFile.
     * @param sourceFile
     * @param destFile the directory should exists.
     * @return if succeeds.
     */
    public static boolean copyAndDeleteSouce(File sourceFile, File destFile)
    {
        boolean result = false;
        try
        {
            result = copy(sourceFile, destFile, false, true);
        }
        catch (IOException e)
        {
            log.error(e);
        }
        return result;

    }

    /**
     * @param in
     * @param out
     * @throws IOException
     */
    public static void doWriteData(InputStream inputStream, OutputStream outputStream) throws IOException
    {
        InputStream in = null;
        OutputStream out = null;
        try
        {
            in = new BufferedInputStream(inputStream, 512 * 10);
            out = new BufferedOutputStream(outputStream, 512 * 10);
            pipe(in, out);
        }
        finally
        {
            IOUtil.close(in);
            IOUtil.close(out);
        }
    }
    
    private static void pipe(InputStream in, OutputStream out)
        throws IOException
    {
        byte[] buf = new byte[1024];
        int bytes;
        while ((bytes = in.read(buf)) > 0)
            out.write(buf, 0, bytes);
    }

    /**
     * Wraps the file content with 2 strings.
     * @param srcPath the source path
     * @param destPath the dest path
     * @param encoding the file encoding
     * @param preappend the preappend string
     * @param append the append string
     */
    public static void wrapFileContent(File srcPath, File destPath, String encoding, String preappend, String append)
    {
        if (srcPath == null || !srcPath.exists())
            throw new IllegalArgumentException("Source path is null or doesn't exist: " + srcPath);
    
        if (destPath == null)
            throw new IllegalArgumentException("Dest path is null");
        
        if (!destPath.getParentFile().exists())
            destPath.getParentFile().mkdirs();
        
        InputStream in = null;
        OutputStream out = null;
        try
        {
            out = new BufferedOutputStream(new FileOutputStream(destPath));
            if (!TextUtil.isEmpty(preappend))
                out.write(preappend.getBytes(encoding));
            
            in = new BufferedInputStream(new FileInputStream(srcPath));
            pipe(in, out);
            
            if (!TextUtil.isEmpty(append))
                out.write(append.getBytes(encoding));
        }
        catch (IOException ex)
        {
            log.error(LogUtil.toString(ex));
        }
        finally
        {
            IOUtil.close(in);
            IOUtil.close(out);
        }
    }

    /**
     * Create the file as per the dirFile and fileName, if the file already
     * exists, append ['number'] to file name e.g., if 'a' exsits, the new file
     * will be named as 'a[1]'.
     * @param dirFile
     * @param fileName
     * @return the created File
     */
    public static File getCreatableFile(File dirFile, String fileName)
    {
        File dest = new File(dirFile, fileName);
        if (dest.exists())
        {
            int i = fileName.lastIndexOf('.');
            String pn = fileName.substring(0, i) + "[";
            String ext = "]" + fileName.substring(i);
            i = 1;
            do
            {
                fileName = pn + (i++) + ext;
                dest = new File(dirFile, fileName);
            }
            while (dest.exists());
        }
        return dest;
    }

    /**
     * Delete file according to file path.
     * @param filePath
     */
    public static void deleteFile(String filePath)
    {
        if (filePath == null || filePath.length() == 0
                || filePath.endsWith("\\"))
        {
            log.warn("File path is null.");
            return;
        }

        File file = new File(filePath);
        if (file.exists() && file.canWrite())
        {
            file.delete();
            log.debug("File:" + filePath + " is deleted.");
        }
        else
        {
            log.warn("File:" + filePath + " not exists or can not be written.");
        }
    }
    
    /**
     * delete a file , including it's sub dir and sub dir's files
     * @param filePath
     */
    public static void deleteAllFiles(String filePath)
    {
        if (filePath == null || filePath.length() == 0
                || filePath.endsWith("\\"))
        {
            log.warn("File path is null.");
            return;
        }

        File file = new File(filePath);
        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++)
            {
                deleteAllFiles(files[i].getAbsolutePath());
            }
            file.delete();
        }
        else
        {
            deleteFile(file.getAbsolutePath());
        }
    }
    /**
     * Get a list of file names incide the input directory.
     * @param resultList
     * @param dir
     * @param filter: if not filter: null
     * @param cutPos: cut position, if not cut: 0
     * @return list of file names.
     */
    public static List getFileList(List resultList, File dir,
            FileFilter filter, int cutPos)
    {
        resultList = getFileList(resultList, dir, filter);
        resultList = getFileNameList(resultList, cutPos);
        return resultList;
    }

    /**
     * Get a list of file names
     * @param originList
     * @param cutPos
     * @return
     */
    public static List getFileNameList(List originList, int cutPos)
    {
        List resultList = new ArrayList();
        String path;

        for (Iterator iter = originList.iterator(); iter.hasNext();)
        {
            File file = (File) iter.next();
            path = file.getAbsolutePath().substring(cutPos).replaceAll("\\\\",
                    "/");
            resultList.add(path);
        }
        return resultList;
    }

    /**
     * Get a list of File , include sub directorys
     * @param resultList
     * @param dir
     * @param filter
     * @return
     */
    public static List getFileList(List resultList, File dir, FileFilter filter)
    {
        File[] files = null;
        
        if (filter != null)
        {
            files = dir.listFiles(filter);
        }
        else
        {
            files = dir.listFiles();
        }

        if (files != null && files.length > 0)
        {
            for (int i = 0; i < files.length; i++)
            {
                File tmpFile = files[i];

                if (!tmpFile.isDirectory())
                {
                    resultList.add(tmpFile);
                }
                else
                {
                    resultList = getFileList(resultList, tmpFile, filter);
                }
            }
        }
        return resultList;
    }

    /**
     * Filter files according to gap between lastModified and current. if gap is
     * larger then minites, this file is transport completed; if not, this file
     * should be removed from list
     * @param originList
     * @param minites
     * @return
     */
    public static List filterFiles(List originList, int minites)
    {
        List resultList = new ArrayList();
        for (Iterator iter = originList.iterator(); iter.hasNext();)
        {
            File file = (File) iter.next();
            long diff = Math.abs((new Date().getTime() - file.lastModified())
                    / (1000 * 60));
            if (diff > minites)
            {
                resultList.add(file);
            }
        }
        return resultList;
    }

    /**
     * get file content, return all content in the file 
     * @param absolutePath
     * @return
     * @throws Exception
     */
    public static String getFileContent(String absolutePath) throws Exception
    {
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;
        String content = "";
        try
        {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(absolutePath),"UTF-8"));
            while((content = reader.readLine()) != null)
                sb.append(content).append(System.getProperty("line.separator"));
        }
        finally
        {
            if(reader != null)
                reader.close();
        }
        return sb.toString();
    }
    
    protected static class FileModifiedDate
    {
        private File file;
        private long lastModified;
        private String fileName;

        public FileModifiedDate(File file, long lastModified, String fileName)
        {
            this.file = file;
            this.lastModified = lastModified;
            this.fileName = fileName;
        }

        /**
         * Gets the file.
         * @return file the file.
         */

        public File getFile()
        {
            return file;
        }

        /**
         * Gets the lastModified.
         * @return lastModified the lastModified.
         */
        public long getLastModified()
        {
            return lastModified;
        }

        /**
         * Sets the fileName.
         * @param fileName the fileName to set.
         */
        public String getFileName()
        {
            return fileName;
        }

    };

    /**
     * 1. Sort file list according to lastModified date desc. 2. If lastModified
     * equal, then sort by filename. 3. To avoid invoking lastModified() too
     * many times, use middle model FileModifiedDate.
     * @param originList
     * @return
     */
    public static List sortFilesByDate(List originList)
    {
        List fileModifiedList = new ArrayList();
        for (Iterator iter = originList.iterator(); iter.hasNext();)
        {
            File file = (File) iter.next();
            FileModifiedDate fd = new FileModifiedDate(file, file
                    .lastModified(), file.getName());
            fileModifiedList.add(fd);
        }

        Comparator c = new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                FileModifiedDate item1 = (FileModifiedDate) o1;
                FileModifiedDate item2 = (FileModifiedDate) o2;
                return (item2.getLastModified() != item1.getLastModified()) ? ((item2
                        .getLastModified() - item1.getLastModified()) > 0 ? 1
                        : -1)
                        : item1.getFileName().compareTo(item2.getFileName());
            }
        };

        Collections.sort(fileModifiedList, c);
        originList = new ArrayList();
        for (Iterator iter = fileModifiedList.iterator(); iter.hasNext();)
        {
            FileModifiedDate fd = (FileModifiedDate) iter.next();
            originList.add(fd.getFile());
        }
        return originList;
    }

    /**
     * @param extentList
     * @return
     */
    public FileFilter getUserFileFilter(List extentList)
    {
        return new UserFileFilter(extentList);
    }

    private class UserFileFilter implements FileFilter
    {
        private List fileExtents;

        public UserFileFilter(List fileExtents)
        {
            this.fileExtents = fileExtents;
        }

        public boolean accept(File file)
        {
            boolean result = false;

            if (file.isDirectory())
            {
                result = true;
            }

            if (!result)
            {

                for (Iterator iter = fileExtents.iterator(); iter.hasNext();)
                {
                    String extent = (String) iter.next();
                    if (file.getName().toLowerCase().endsWith(
                            extent.toLowerCase()))
                    {
                        result = true;
                        break;
                    }
                }
            }
            return result;
        }
    }

    /**
     * Get the shorten path by path, maxLength and omitStr.
     * @param path: input path should be seperated by "/"
     * @param maxLength
     * @param omitStr: eg: '...'
     * @return the shorten path.
     */
    public static String getShortenPath(String path, int maxLength,
            String omitStr)
    {
        // remove doble //
        while (path.indexOf("//") >= 0)
        {
            path = path.replaceAll("//", "/");
        }

        if (path.length() > maxLength)
        {
            String firstPart = "";

            if (path.indexOf(omitStr) > 0)
            {
                int slashPos = path.indexOf("/", omitStr.length() + 2);

                if (slashPos > 0)
                {
                    firstPart = path.substring(omitStr.length() + 1, slashPos);
                    path = path.replaceAll(firstPart, "");
                }
                else
                // if name part exceeds maxLength
                {
                    firstPart = path.substring(omitStr.length() + 2);
                    path = "/" + omitStr + "/"
                            + firstPart.substring(path.length() - maxLength);
                }

            }
            else
            {
                int slashPos = path.indexOf("/", 1);

                if (slashPos > 0)
                {
                    firstPart = path.substring(1, slashPos);
                    path = path.replaceAll(firstPart, omitStr);
                }
                else
                {
                    firstPart = path.substring(1);
                    path = "/" + omitStr + "/"
                            + firstPart.substring(path.length() - maxLength);
                }
            }
            path = getShortenPath(path, maxLength, omitStr);
        }
        else
        {
            return path;
        }
        return path;
    }
    
    public static String getFormatedFileSize(int fileSize)
    {
        String result = "";
        int mValue = fileSize/1024/1024;
        int kValue = (fileSize - mValue * 1024 * 1024)/1024;
        int bValue = fileSize - mValue * 1024 * 1024 - kValue * 1024;
        
        if (mValue > 0)
        {
            result = mValue + "." + kValue + " M";   
        }
        else
        {
            result =kValue + "." + bValue+ " K";  
        }
        return result;
    }
    
    /**
     * Write string to outputFile.
     * @param data the source string data.
     * @param outputFile the directory should exists.
     * @return true if succeeds.
     * @throws IOException
     * @throws IOException
     */
    public static boolean write(String data, File outputFile, String charset)
            throws IOException
    {
        if (!outputFile.getParentFile().exists())
        {
            if (!outputFile.getParentFile().mkdirs())
            {
                log.error("Can not make dir for file:" + outputFile.getAbsolutePath()+ ". Copy skipped.");
                return false;
            }
        }
        
        if (!outputFile.getParentFile().canWrite())
        {
            log.error("Out put file:" + outputFile.getAbsolutePath()
                    + " can not be write.");
            return false;
        }
        
        if (outputFile.exists() && !outputFile.canWrite())
        {
            log.error("Out put file:" + outputFile.getAbsolutePath()
                    + " can not be write.");
            return false;
        }
        
        doWriteData(new ByteArrayInputStream(TextUtil.isEmpty(charset)?data.getBytes():data.getBytes(charset)), new FileOutputStream(outputFile));
        
        return true;
    }
    
    public static String projectPath()
    {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath());
        return file.getAbsolutePath();
    }
}

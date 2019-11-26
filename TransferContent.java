package com.test.content.transfer.chunked;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

public class TransferContent
  extends HttpServlet
{
  public void execute(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
  {
    try
    {
      File file = getTransferFile(httpServletRequest);
      if (file.exists())
      {
        httpServletResponse.setHeader("Transfer-Encoding", "chunked");
        httpServletResponse.setContentType("application/octet-stream");
        
        OutputStream out = httpServletResponse.getOutputStream();
        try
        {
          Files.copy(file.toPath(), out);
        }
        finally
        {
          IOUtils.closeQuietly(out);
        }
        httpServletResponse.setStatus(200);
      }
      else
      {
        System.out.println("Cannot find file for GET: " + file.getAbsolutePath());
        System.out.println(httpServletResponse.getStatus());
      }
    }
    catch (Exception e)
    {
      System.out.println("Exception: " + e);
    }
  }
  
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    execute(req, resp);
  }
  
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    doGet(req, resp);
  }
  
  protected File getTransferFile(HttpServletRequest httpServletRequest)
    throws Exception
  {
    ServletContext servletContext = httpServletRequest.getServletContext();
    String contextPath = servletContext.getRealPath(File.separator);
    File file = new File(contextPath + "/" + httpServletRequest.getParameter("fileName"));
    
    System.out.println(file.getAbsolutePath());
    
    return file;
  }
}

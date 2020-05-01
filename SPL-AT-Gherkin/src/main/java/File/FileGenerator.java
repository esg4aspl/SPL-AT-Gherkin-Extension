package File;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author sercansensulun
 */
public abstract class FileGenerator 
{

	private String absolutePath;

	public FileGenerator(){

	}
	public FileGenerator(String absolutePath){
		setAbsolutePath(absolutePath);
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
		this.createFolderIfNotExist(this.getAbsolutePath());
	}

	protected String getAbsolutePath(){
		return absolutePath;
	}
	
	protected boolean isFileExist(String fileName)
	{
		File f = new File(getAbsolutePath()+fileName);
		return f.isFile();
	}
	
	protected boolean writeToFile(String fileName, String fileContent) throws IOException
	{
		String filePath = this.getAbsolutePath()+"/"+fileName;
		
		//create file and write the content to it.	
		PrintWriter writer = new PrintWriter(filePath, "UTF-8");
		
		writer.println(fileContent);
		writer.close();
			
		return true;
	}
	
	protected void writeContentToConsole(String fileName){
		
		String fileAbsolutePath = getAbsolutePath()+fileName;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileAbsolutePath));
			String line = "";
			
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
	}
	
	protected String getFileContent(String fileName){
		
		if (!isFileExist(fileName)) {
			return null;
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(getAbsolutePath()+fileName));
			String line = "";
			String fileContent = "";
			
			while ((line = br.readLine()) != null) {
				fileContent += "\n" + line;
			}
			
			return fileContent;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	protected void createFolderIfNotExist(String path){
		File directory = new File(path);
		if (! directory.exists()){
			directory.mkdir();
		}
	}

}

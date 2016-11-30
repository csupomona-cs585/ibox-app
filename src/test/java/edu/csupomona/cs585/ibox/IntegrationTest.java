package edu.csupomona.cs585.ibox;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;
import edu.csupomona.cs585.ibox.sync.GoogleDriveServiceProvider;


public class IntegrationTest {

	private GoogleDriveFileSyncManager syncManager;
	private String filepath;
	
	@Before
	public void setUp(){
		syncManager = new GoogleDriveFileSyncManager(GoogleDriveServiceProvider.get().getGoogleDriveClient());
		filepath = "C:\\Drive\\Test";
	}
	
	@Test
	public void testAddFile(){
		java.io.File file = new java.io.File(filepath);
		
		try{
			file.createNewFile();
			syncManager.addFile(file);
			assertNotNull(syncManager.getFileId(file.getName()));
			syncManager.deleteFile(file);
		}
		catch(IOException e){
			System.out.println("Error in testAddFile()");
		}
		finally{
			file.delete();
		}
	}
	
	public void testUpdateFile(){
		java.io.File file = new java.io.File(filepath);
		
		try{
			file.createNewFile();
			syncManager.addFile(file);
			long fileSizeBefore = file.lastModified();
			
			FileWriter fWriter = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bWriter = new BufferedWriter(fWriter);
			bWriter.write("Making a change!");
			bWriter.close();
			fWriter.close();
			
			syncManager.updateFile(file);
			long fileSizeAfter = file.lastModified();
			org.junit.Assert.assertNotEquals(fileSizeBefore, fileSizeAfter);
			syncManager.deleteFile(file);
		}
		catch (IOException e){
			System.out.println("Error in testUpdateFile()");
		}
		finally{
			file.delete();
		}
	}
	
	@Test
	public void testDeleteFile(){
		java.io.File file = new java.io.File(filepath);
		
		try{
			file.createNewFile();
			syncManager.addFile(file);
			syncManager.deleteFile(file);
			assertNull(syncManager.getFileId(file.getName()));
		}
		catch (IOException e){
			System.out.println("Error in testDeleteFile()");
		}
		finally{
			file.delete();
		}
	}
}

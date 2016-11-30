package edu.csupomona.cs585.ibox;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import edu.csupomona.cs585.ibox.sync.*;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.List;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.client.http.AbstractInputStreamContent;

import java.io.IOException;

public class GoogleDriveFileSyncManagerTest {
	
	private Drive mockedDrive;
	private GoogleDriveFileSyncManager syncManager;
	private Drive.Files mockedGoogleFiles;
	private File testGoogleFile;
	private java.io.File mockedLocalFile;
	private String fileName;
	private List mockedList;
	private FileList testFileList;

	@Before
	public void setUp() throws Exception {
		mockedDrive = mock(Drive.class);
		syncManager = new GoogleDriveFileSyncManager(mockedDrive);
		
		mockedGoogleFiles = mock(Files.class);
		testGoogleFile = new File();
		testGoogleFile.setId("test");
		testGoogleFile.setTitle("test");
		
		mockedLocalFile = mock(java.io.File.class);
		fileName = "test";
		
		mockedList = mock(List.class);
		testFileList = new FileList();
		java.util.List<File> list = new java.util.ArrayList<File>();
		list.add(testGoogleFile);
		testFileList.setItems(list); 
		
	}

	@Test
	//Only appropriate test I can think of for the constructor is service!=null
	public void testGoogleDriveFileSyncManager() {
		assertNotNull(syncManager.service);
	}

	@Test
	public void testAddFile() throws IOException{
		//Stub functionality
		Files.Insert insertFiles = mock(Files.Insert.class);
		when(mockedGoogleFiles.list()).thenReturn(mockedList);
		when(mockedLocalFile.getName()).thenReturn(fileName);
		when(mockedList.execute()).thenReturn(testFileList);
		when(mockedDrive.files()).thenReturn(mockedGoogleFiles);
		when(mockedGoogleFiles.insert(any(File.class), any(AbstractInputStreamContent.class))).thenReturn(insertFiles);
		when(insertFiles.execute()).thenReturn(testGoogleFile);

		//Testing
		syncManager.addFile(mockedLocalFile);
		verify(insertFiles).execute();
	}
	
	@Test()
	public void testUpdateFile() throws IOException{
		//Stub functionality
		Files.Update updateFiles = mock(Files.Update.class);
		when(mockedGoogleFiles.list()).thenReturn(mockedList);
		when(mockedLocalFile.getName()).thenReturn(fileName);
		when(mockedList.execute()).thenReturn(testFileList);
		when(mockedDrive.files()).thenReturn(mockedGoogleFiles);
		when(mockedGoogleFiles.update(any(String.class), any(File.class), any(AbstractInputStreamContent.class))).thenReturn(updateFiles);
		when(updateFiles.execute()).thenReturn(testGoogleFile);
		
		//Testing
		syncManager.updateFile(mockedLocalFile);
		verify(updateFiles).execute();
	}
	
	@Test
	public void testDeleteFile() throws IOException{
		//Stub functionality
		Files.Delete deleteFiles = mock(Files.Delete.class);
		when(mockedGoogleFiles.list()).thenReturn(mockedList);
		when(mockedLocalFile.getName()).thenReturn(fileName);
		when(mockedList.execute()).thenReturn(testFileList);
		when(mockedDrive.files()).thenReturn(mockedGoogleFiles);
		when(mockedGoogleFiles.delete(any(String.class))).thenReturn(deleteFiles);
		//"deleteFiles.execute()" returns void, so no need to stub the return
		
		//Testing
		syncManager.deleteFile(mockedLocalFile);
		verify(deleteFiles).execute();
	}
	
	@Test
	public void testGetFileId() throws IOException{
		//Stub functionality
		when(mockedGoogleFiles.list()).thenReturn(mockedList);
		when(mockedList.execute()).thenReturn(testFileList);
		when(mockedDrive.files()).thenReturn(mockedGoogleFiles);
		
		//Testing
		assertEquals(syncManager.getFileId("test"),"test");
	}
}

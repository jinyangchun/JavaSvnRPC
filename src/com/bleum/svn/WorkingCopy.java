package com.bleum.svn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class WorkingCopy {

	private static SVNClientManager ourClientManager;
	private static String svnUser = "vince.chen";
	private static String svnPass = "Qwert,2004";
	private static ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
	
	static{
		
		ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options,
				svnUser, svnPass);
		
	}
	
	//新建文件夹
	private static SVNCommitInfo makeDirectory(SVNURL url, String commitMessage)
			throws SVNException {
		return ourClientManager.getCommitClient().doMkDir(new SVNURL[] { url },commitMessage);
	}

	//导入文件夹
	private static SVNCommitInfo importDirectory(File localPath, SVNURL dstURL,
		String commitMessage, boolean isRecursive) throws SVNException {
		return ourClientManager.getCommitClient().doImport(localPath, dstURL, commitMessage, new SVNProperties(), true, true, SVNDepth.fromRecurse(isRecursive));
	}
	
	//更新
	private static long update( File wcPath , SVNRevision updateToRevision , boolean isRecursive,boolean allowUnversionedObstructions ) throws SVNException {
	 
	  	SVNUpdateClient updateClient = ourClientManager.getUpdateClient( );
	    updateClient.setIgnoreExternals( false );
	    return updateClient.doUpdate(wcPath, updateToRevision, SVNDepth.fromRecurse(isRecursive), allowUnversionedObstructions, true);
	}
		   
	//提交
	private static SVNCommitInfo commit(File wcPath, boolean keepLocks,
		String commitMessage) throws SVNException {
        return ourClientManager.getCommitClient().doCommit( new File[] { wcPath } , keepLocks , commitMessage , false , true );
	}

	//检出
	private static long checkout(SVNURL url, SVNRevision revision,
			File destPath, boolean isRecursive, boolean allowUnversionedObstructions) throws SVNException {
		SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
		updateClient.setIgnoreExternals(false);
		return updateClient.doCheckout(url, destPath, revision, revision, SVNDepth.FILES, allowUnversionedObstructions);
		
	}
	
	//加锁
	private static void getLock( File wcPath , boolean isStealLock , String lockComment ) throws SVNException {
		ourClientManager.getWCClient( ).doLock( new File[] { wcPath } , isStealLock , lockComment );
	}
	
	//释放锁
	private static void releaseLock(File wcPath, boolean breakLock) throws SVNException{
		ourClientManager.getWCClient( ).doUnlock(new File[]{wcPath}, breakLock);
	}
	
	//添加到版本控制，下一次提交(add命令)
	private static void add( File wcPath ) throws SVNException {
		ourClientManager.getWCClient( ).doAdd(wcPath, false, false, false, SVNDepth.fromRecurse(true), false, false);
    }
	
	//删除本地文件
	private static void delete( File wcPath , boolean force ) throws SVNException {
        ourClientManager.getWCClient( ).doDelete( wcPath , force , false );
    }
	
	//状态读取
	 private static void showStatus( File wcPath , boolean isRecursive , boolean isRemote , boolean isReportAll ,
		 boolean isIncludeIgnored , boolean isCollectParentExternals ,List<String> changeFilterList) throws SVNException {
		 ourClientManager.getStatusClient( ).doStatus(wcPath, SVNRevision.HEAD, SVNDepth.fromRecurse(isRecursive), isRemote, isReportAll, isIncludeIgnored, isCollectParentExternals, new StatusHandler(isRemote), changeFilterList);
	}
	 
	 //拷贝的相关信息
	 private static void showInfo( File wcPath , SVNRevision revision , boolean isRecursive, List<String>  changeLists) throws SVNException {
        ourClientManager.getWCClient( ).doInfo(wcPath, revision, revision, SVNDepth.fromRecurse(isRecursive), changeLists, new InfoHandler());        
	 }
	 
	 //
	
	@Test
	public void testLock() throws SVNException{
		getLock(new File("C:/Documents and Settings/Vince.Chen/Desktop/Test2/Test/test.txt"), true, "svn test");
	}
	
	@Test
	public void testUnlock() throws SVNException{
		releaseLock(new File("C:/Documents and Settings/Vince.Chen/Desktop/Test2/Test/test.txt"), true);
	}
	 
	@Test
	public void testAdd() throws SVNException{
		add(new File("C:/Documents and Settings/Vince.Chen/Desktop/Copy2/test3/add.txt"));
	}
	
	@Test
	public void testDelete() throws SVNException{
		delete(new File("C:/Documents and Settings/Vince.Chen/Desktop/Copy2/test3/add.txt"), false);
	}
	
	@Test
	public void testCommit() throws SVNException{
		commit(new File("C:/Documents and Settings/Vince.Chen/Desktop/Test/Copy2/test3/SVNKit Guide.doc"), false, "svn test");
	}
	
	@Test
	public void testUpdate() throws SVNException{
		update(new File("C:/Documents and Settings/Vince.Chen/Desktop/Copy2/test3/SVNKit Guide.doc"), SVNRevision.HEAD, true, true);
	}
	
	@Test
	public void testCreateFolder() throws SVNException{
		makeDirectory(SVNURL.parseURIEncoded("http://bleum-hydrang-2/research/Hydra_research/Copy/test5"), "svn test");
	}
	
	@Test
	public void testUploadFolder()throws SVNException{
		importDirectory(new File("C:/Documents and Settings/Vince.Chen/Desktop/Test"), SVNURL.parseURIEncoded("http://bleum-hydrang-2/research/Hydra_research/Copy/Test"), "svn test", true);
	}
	
	@Test
	public void testShowStatus() throws SVNException{
		showStatus(new File("C:/Documents and Settings/Vince.Chen/Desktop/Test/test3/"), true, true, true, true, false, new ArrayList<String>());
	}
	
	@Test
	public void testShowInfo() throws SVNException{
		showInfo(new File("C:/Documents and Settings/Vince.Chen/Desktop/Test/test3"), SVNRevision.HEAD, true,new ArrayList<String>());
	}
	
	@Test
	public void testRepairSVN() throws SVNException{
		//checkout(SVNURL.parseURIEncoded("http://bleum-hydrang-2/research/Hydra_research/MyRepos/importFiles"), SVNRevision.HEAD, new File("C:/Documents and Settings/Vince.Chen/Desktop/Test2/"), true, true);
		//update(new File("C:/Documents and Settings/Vince.Chen/Desktop/Test2/importFiles"), SVNRevision.HEAD, true, true);
	}
	
	@Test
	public void testRepairSVN2() throws SVNException{
		checkout(SVNURL.parseURIEncoded("http://bleum-hydrang-2/research/Hydra_research/MyRepos"), SVNRevision.HEAD, new File("C:/Documents and Settings/Vince.Chen/Desktop/Test2"), true, true);
		//update(new File("C:/Documents and Settings/Vince.Chen/Desktop/Test2/importFiles"), SVNRevision.HEAD, true, true);
	}
	
	@Test
	public void testProcess() throws Exception{
		//make a directory
		makeDirectory(SVNURL.parseURIEncoded("http://bleum-hydrang-2/research/Hydra_research/MyRepos"), "svn test");
		
		//import a directory
		importDirectory(new File("C:/Documents and Settings/Vince.Chen/Desktop/Test/ImportDir"), SVNURL.parseURIEncoded("http://bleum-hydrang-2/research/Hydra_research/MyRepos/"), "svn test", true);
	
		//check out repo
		checkout(SVNURL.parseURIEncoded("http://bleum-hydrang-2/research/Hydra_research/MyRepos"), SVNRevision.HEAD, new File("C:/Documents and Settings/Vince.Chen/Desktop/Test/MyRepos"), true ,false);
	}
	
	//Error
	@Test
	public void testCheckout() throws SVNException{
		checkout(SVNURL.parseURIEncoded("http://bleum-hydrang-2/research/Hydra_research/Copy/test3"), SVNRevision.HEAD, new File("D:/good"), true,false);
	}

}

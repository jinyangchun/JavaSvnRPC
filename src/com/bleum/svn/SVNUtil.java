package com.bleum.svn;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNUtil {

	// Content Root
	private String svnRoot;
	// SVN User Information
	private String svnUser;
	private String svnPass;
	// Local copy directory
	private String workingCopyPath;
	// Create Options
	private ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
	// Repo
	private SVNRepository repository;

	private SVNClientManager manager;
	static {
		// http:// and https://
		DAVRepositoryFactory.setup();
		// svn://
		// SVNRepositoryFactoryImpl.setup();
		// file://
		// FSRepositoryFactory.setup();

	}

	public SVNUtil() {
	}

	public void doCommit(){
		
	}
	
	public boolean loginValidate() {
		try {
			// repository connect
			repository = DAVRepositoryFactory.create(SVNURL
					.parseURIEncoded(this.svnRoot));
			// identify validate
			ISVNAuthenticationManager authManager = SVNWCUtil
					.createDefaultAuthenticationManager(this.svnUser,
							this.svnPass);
			// save identify inforation
			repository.setAuthenticationManager(authManager);

			System.out.println("Repository Root: "
					+ repository.getRepositoryRoot(true));
			System.out.println("Repository UUID: "
					+ repository.getRepositoryUUID(true));

			SVNNodeKind nodeKind = repository.checkPath("", -1);
			if (nodeKind == SVNNodeKind.NONE) {
				System.err.println("There is no entry at '" + svnRoot + "'.");
				System.exit(1);
			} else if (nodeKind == SVNNodeKind.FILE) {
				System.err.println("The entry at '" + svnRoot
						+ "' is a file while a directory was expected.");
				System.exit(1);
			}

			listEntries( repository , "" );

			return true;

		} catch (SVNException svne) {
			svne.printStackTrace();
			return false;
		}

	}

	@SuppressWarnings("rawtypes")
	public static void listEntries( SVNRepository repository, String path ) throws SVNException {
		
		Collection entries = repository.getDir(path, -1, null,
				(Collection) null);
		Iterator iterator = entries.iterator();
		while (iterator.hasNext()) {
			SVNDirEntry entry = (SVNDirEntry) iterator.next();
			System.out.println("/" + (path.equals("") ? "" : path + "/")
					+ entry.getName() + " ( author: '" + entry.getAuthor()
					+ "'; revision: " + entry.getRevision() + "; date: "
					+ entry.getDate() + ")");
			if (entry.getKind() == SVNNodeKind.DIR) {
				listEntries(repository, (path.equals("")) ? entry.getName()
						: path + "/" + entry.getName());
			}
		}
		
	}
	
	public void checkOut() {

		manager = SVNClientManager.newInstance((DefaultSVNOptions) options,
				svnUser, svnPass);

		/* 工作副本目录创建 */
		File wcDir = new File(workingCopyPath);
		if (wcDir.exists()) {
			System.out.println("the destination directory '"
					+ wcDir.getAbsolutePath() + "' already exists!");
		} else {
			wcDir.mkdirs();
		}

		try {

			SVNUpdateClient updateClient = manager.getUpdateClient();
			updateClient.setIgnoreExternals(false);

			updateClient.doCheckout(SVNURL.parseURIEncoded(this.svnRoot),
					wcDir, SVNRevision.HEAD, SVNRevision.HEAD, true);

		} catch (SVNException svne) {
			//
		}

	}

	// public test
	@Test
	public void testCheckOut() {
		SVNUtil util = new SVNUtil();
		util.setSvnRoot("http://bleum-hydrang-2/HydraV9/Management/Design/Architecture/HydraNF201203R");
		util.setSvnUser("vince.chen");
		util.setSvnPass("Qwert,2004");
		util.setWorkingCopyPath("C:/Documents and Settings/Vince.Chen/Desktop/Copy/test3");
		util.loginValidate();
		// util.checkOut();
	}

	public String getSvnRoot() {
		return svnRoot;
	}

	public void setSvnRoot(String svnRoot) {
		this.svnRoot = svnRoot;
	}

	public String getSvnUser() {
		return svnUser;
	}

	public void setSvnUser(String svnUser) {
		this.svnUser = svnUser;
	}

	public String getSvnPass() {
		return svnPass;
	}

	public void setSvnPass(String svnPass) {
		this.svnPass = svnPass;
	}

	public String getWorkingCopyPath() {
		return workingCopyPath;
	}

	public void setWorkingCopyPath(String workingCopyPath) {
		this.workingCopyPath = workingCopyPath;
	}

}

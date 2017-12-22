package ca.uwaterloo.ece.ece658project;

import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

public class MySaltedAuthentificationInfo implements SaltedAuthenticationInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String username;
	private final String password;
	private final String salt;
	  
	public MySaltedAuthentificationInfo(String username, String password, String salt){
		this.username = username;
		this.password = password;
		this.salt = salt;
	}
	 
	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public PrincipalCollection getPrincipals() {
		PrincipalCollection coll = new SimplePrincipalCollection(username, username);
	    return coll;
	}

	@Override
	public ByteSource getCredentialsSalt() {
		// TODO Auto-generated method stub
		return new SimpleByteSource(Base64.decode(this.salt));
	}

}

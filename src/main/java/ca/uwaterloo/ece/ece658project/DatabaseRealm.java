package ca.uwaterloo.ece.ece658project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.jdbc.JdbcRealm;

import ca.uwaterloo.ece.ece658project.entity.UserEntity;
 
public class DatabaseRealm extends JdbcRealm {
	
	private static final Logger logger = Logger.getLogger(DatabaseRealm.class.getName());
	
	
	@Override
	  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
	    // identify account to log to
	    UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;
	    final String username = userPassToken.getUsername();
	    final char[] password = userPassToken.getPassword();
	    
	    if (username == null) {
	      logger.info("Username is null.");
	      return null;
	    }
	    
	    ArrayList<String> users = new ArrayList<String>();
	    try {
			BeanManager beanManager = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
			Bean<realmUserBean> user_info_bean = (Bean<realmUserBean>)beanManager.resolve(beanManager.getBeans(realmUserBean.class));
			CreationalContext<?> creationalContext = beanManager.createCreationalContext(null);
			realmUserBean user_info = user_info_bean.create((CreationalContext<realmUserBean>) creationalContext);
			users.addAll(user_info.getUserInfo(username));
			creationalContext.release();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	    
	   

	    if (users.isEmpty()) {
	      logger.info("No account found for user [" + username + "]");
	      return null;
	    }

	    // return salted credentials
	    SaltedAuthenticationInfo info = new MySaltedAuthentificationInfo(username, users.get(0), users.get(1));

	    return info;
	  }
}

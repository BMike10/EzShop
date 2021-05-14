package it.polito.ezshop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import org.junit.Test;

import it.polito.ezshop.data.RoleEnum;
import it.polito.ezshop.data.UserClass;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;

public class UserClassTest {
	/* if(username==null || username.isEmpty()) throw new InvalidUsernameException();
	 if(password==null || password.isEmpty()) throw new InvalidPasswordException();
	 if(role==null || role.isEmpty() ) throw new InvalidRoleException();*/
	 
	@Test
	public void testUserClassConstructor() {
		//invalid id
		assertThrows(InvalidUserIdException.class, ()->{
			UserClass u = new UserClass(0,"username","password",RoleEnum.Administrator);});
		// invalid username
		assertThrows(InvalidUsernameException.class, ()->{
			UserClass u = new UserClass(1,null,"password",RoleEnum.Administrator);});
		// invalid password
		assertThrows(InvalidPasswordException.class, ()->{
			UserClass u = new UserClass(1,"username",null,RoleEnum.Administrator);});
		// invalid role
		assertThrows(InvalidRoleException.class, ()->{
			UserClass u = new UserClass(1,"username","password",null);});
		// valid
				try {
					UserClass u = new UserClass(1, "username", "password", RoleEnum.Administrator );					
				}catch(Exception e) {fail();}
	
	}
	
	@Test
	public void testSetUserId() {
		final UserClass u = new UserClass(1, "username", "password", RoleEnum.Administrator );					
		assertEquals(new Integer(1), u.getId());
				// null
				assertThrows(Exception.class, ()->{u.setId(null);});
				assertEquals(new Integer(1), u.getId());
				// invalid
				assertThrows(Exception.class, ()->{u.setId(-1);});
				assertEquals(new Integer(1), u.getId());
				// valid
				try {
					u.setId(2);
				}catch(Exception e) {
					fail();
				}
		
		
	}
	
	@Test
	public void testSetUsername() {
		final UserClass u = new UserClass(1, "username", "password", RoleEnum.Administrator );					
		assertEquals("username", u.getUsername());
				// null
				assertThrows(Exception.class, ()->{u.setUsername(null);});
				assertEquals("username", u.getUsername());
				// empty
				assertThrows(Exception.class, ()->{u.setUsername("");});
				assertEquals("username", u.getUsername());
				// valid
				try {
					u.setUsername("username");
				}catch(Exception e) {
					fail();
				}
	}
		
		@Test
		public void testSetPassword() {
			final UserClass u = new UserClass(1, "username", "password", RoleEnum.Administrator );					
			assertEquals("password", u.getPassword());
					// null
					assertThrows(Exception.class, ()->{u.setPassword(null);});
					assertEquals("password", u.getPassword());
					// empty
					assertThrows(Exception.class, ()->{u.setPassword("");});
					assertEquals("password", u.getPassword());
					// valid
					try {
						u.setPassword("password");
					}catch(Exception e) {
						fail();
					}
		}
		
		@Test
		public void testSetRole() {
			final UserClass u = new UserClass(1, "username", "password", RoleEnum.Administrator );					
			assertEquals("Administrator", u.getRole());
					// null
					assertThrows(Exception.class, ()->{u.setRole(null);});
					assertEquals("Administrator", u.getRole());
					// empty
					assertThrows(Exception.class, ()->{u.setRole("");});
					assertEquals("Administrator", u.getRole());
					// valid
					try {
						u.setPassword("Administrator");
					}catch(Exception e) {
						fail();
					}
		}
		
	
}

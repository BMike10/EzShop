package it.polito.ezshop.data;

public class UserClass implements User {
	
	private int id;
	private String username;
	private String password;
	private RoleEnum role;

	public UserClass(int id, String username, String password, RoleEnum role){
		super();
		this.id=id;
		this.username=username;
		this.password=password;
		this.role=role;
	}
	

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id=id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setUsername(String username) {
		this.username=username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password=password;
	}

	@Override
	public String getRole() {
		return role.toString();
	}

	@Override
	public void setRole(String role) {
	   this.role=RoleEnum.valueOf(role);
	}
	public RoleEnum getRoleEnum () {
		return role;
	}


}

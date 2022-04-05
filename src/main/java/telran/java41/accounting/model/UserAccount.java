package telran.java41.accounting.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(of = {"login"})
@Document(collection = "users")
public class UserAccount {
	@Id
	String login;
	@Setter
	String password;
	@Setter
	LocalDateTime lastDatePassword;
	@Setter
    String firstName;
	@Setter
    String lastName;
    Set<String> roles;
	
    public UserAccount() {
    	lastDatePassword = LocalDateTime.MIN;
    	roles = new HashSet<>();
    	roles.add("USER");
	}

	public UserAccount(String login, String password, String firstName, String lastName) {
		this();
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
    
	public boolean addRole(String role) {
		return roles.add(role);
	}
	
	public boolean removeRole(String role) {
		return roles.remove(role);
	}
	
}

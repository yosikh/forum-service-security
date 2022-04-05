package telran.java41.accounting.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import telran.java41.accounting.dao.UserAccountRepository;
import telran.java41.accounting.dto.RolesResponseDto;
import telran.java41.accounting.dto.UserAccountResponseDto;
import telran.java41.accounting.dto.UserRegisterDto;
import telran.java41.accounting.dto.UserUpdateDto;
import telran.java41.accounting.dto.exceptions.UserExistsException;
import telran.java41.accounting.dto.exceptions.UserNotFoundException;
import telran.java41.accounting.model.UserAccount;

@Service
@AllArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

	UserAccountRepository repository;
	ModelMapper modelMapper;
	PasswordEncoder passwordEncoder;
	
	@Override
	public UserAccountResponseDto addUser(UserRegisterDto registerDto) {
		if (repository.existsById(registerDto.getLogin())) {
			throw new UserExistsException(registerDto.getLogin());
		}
		UserAccount userAccount = modelMapper.map(registerDto, UserAccount.class);
		String password = passwordEncoder.encode(registerDto.getPassword());
		userAccount.setPassword(password);
		userAccount.setLastDatePassword(LocalDateTime.now().plusMinutes(15));
		repository.save(userAccount);
		return modelMapper.map(userAccount, UserAccountResponseDto.class);
	}

	@Override
	public UserAccountResponseDto getUser(String login) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		return modelMapper.map(userAccount, UserAccountResponseDto.class);
	}

	@Override
	public UserAccountResponseDto removeUser(String login) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		repository.deleteById(login);
		return modelMapper.map(userAccount, UserAccountResponseDto.class);
	}

	@Override
	public UserAccountResponseDto editUser(String login, UserUpdateDto updateDto) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		if (updateDto.getFirstName() != null) {
			userAccount.setFirstName(updateDto.getFirstName());
		}
		if (updateDto.getLastName() != null) {
			userAccount.setLastName(updateDto.getLastName());
		}
		repository.save(userAccount);
		return modelMapper.map(userAccount, UserAccountResponseDto.class);
	}

	@Override
	public RolesResponseDto changeRolesList(String login, String role, boolean isAddRole) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		boolean res;
		if (isAddRole) {
			res = userAccount.addRole(role.toUpperCase());
		} else {
			res = userAccount.removeRole(role.toUpperCase());
		}
		if (res) {
			repository.save(userAccount);
		}
		return modelMapper.map(userAccount, RolesResponseDto.class);
	}

	@Override
	public void changePassword(String login, String newPassword) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		String password = passwordEncoder.encode(newPassword);
		userAccount.setPassword(password);
		userAccount.setLastDatePassword(LocalDateTime.now().plusMinutes(15));
		repository.save(userAccount);
	}

}

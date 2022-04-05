package telran.java41.security.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import telran.java41.accounting.dao.UserAccountRepository;
import telran.java41.accounting.model.UserAccount;
import telran.java41.forum.dao.PostRepository;
import telran.java41.forum.model.Post;

@Service("customSecurity")
@AllArgsConstructor
public class CustomWebSecurity {
	
	UserAccountRepository userRepository;
	PostRepository postRepository;
	
	public boolean checkPostAuthority(String postId, String userName) {
		Post post = postRepository.findById(postId).orElse(null);
		return post != null && userName.equals(post.getAuthor());
	}
	
	public boolean checkLastDatePasswordUser(String userName) {
		UserAccount userAccount = userRepository.findById(userName).orElse(null);
		return !(userAccount == null || userAccount.getLastDatePassword().isBefore(LocalDateTime.now()));
	}
}

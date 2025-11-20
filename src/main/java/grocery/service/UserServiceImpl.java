package grocery.service;

import grocery.dto.UserDTO;
import grocery.entity.User;
import grocery.exception.ResourceNotFoundException;
import grocery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Implementation of UserService.
 * Handles business logic for user profiles.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // Gets the current user's profile.
    @Override
    public UserDTO getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return mapToDTO(user);
    }

    // Updates the user's email.
    @Override
    @Transactional
    public UserDTO updateUserProfile(String username, UserDTO userDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // For now, only allow email updates.
        user.setEmail(userDTO.getEmail());
        
        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    // Helper to map a User entity to a DTO.
    private UserDTO mapToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        // Map roles from User entity to UserDTO
        userDTO.setRoles(user.getRoles().stream()
                                .map(role -> role.getName())
                                .collect(Collectors.toSet()));
        return userDTO;
    }
}

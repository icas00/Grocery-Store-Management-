package grocery.service;

import grocery.dto.UserDTO;

/**
 * Service interface for user profile management.
 */
public interface UserService {

    // Gets the current user's profile.
    UserDTO getUserProfile(String username);

    // Updates the current user's email.
    UserDTO updateUserProfile(String username, UserDTO userDTO);
}

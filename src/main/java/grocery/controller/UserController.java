package grocery.controller;

import grocery.dto.UserDTO;
import grocery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for user profile management.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Gets the profile of the currently logged-in user.
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserProfile(userDetails.getUsername()));
    }

    // Updates the profile of the currently logged-in user.
    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUserProfile(userDetails.getUsername(), userDTO));
    }
}

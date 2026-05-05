package ru.glashiii.springauthms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.glashiii.springauthms.dto.RegisterRequest;
import ru.glashiii.springauthms.entities.UserInfo;
import ru.glashiii.springauthms.repositories.UserInfoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserInfoService(final UserInfoRepository userInfoRepository, final PasswordEncoder passwordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // This method uses email to find a user
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = userInfoRepository.findByEmail(email);

        if (userInfo.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        UserInfo user = userInfo.get();
        return new User(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRoles())));
    }

    public UserInfo findByEmail(String email) {
        return userInfoRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public String addUser(RegisterRequest registerRequest) {
        Optional<UserInfo> user = userInfoRepository.findByEmail(registerRequest.getEmail());
        if (user.isEmpty()) {
            UserInfo userInfo = UserInfo.builder()
                    .email(registerRequest.getEmail())
                    .name(registerRequest.getUsername())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    // TODO to define it in config
                    .roles("ROLE_USER")
                    .build();

            userInfoRepository.save(userInfo);
            return "User added successfully";
        }
        else  {
            return "User with this email already exists";
        }

    }
}

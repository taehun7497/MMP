package com.example.MMP.siteuser;

import com.example.MMP.DataNotFoundException;
import com.example.MMP.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Transactional
    public void resetPassword(String userId, String email) throws Exception {
        SiteUser user = siteUserRepository.findByUserIdAndEmail(userId, email)
                .orElseThrow(() -> new Exception("등록되지 않은 아이디 또는 이메일입니다."));
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(passwordEncoder.encode(tempPassword));
        siteUserRepository.save(user);
        mailService.mailSend(user.getEmail(), "비밀번호 재설정", "임시 비밀번호: " + tempPassword);
    }


    public void adminSignup(String name, String number, String gender, String birthDay, String email) {
        SiteUser siteUser = new SiteUser();
        siteUser.setPassword(passwordEncoder.encode(number));
        siteUser.setName(name);
        siteUser.setNumber(number);
        siteUser.setBirthDate(birthDay);
        siteUser.setGender(gender);
        siteUser.setEmail(email);
        siteUser.setUserRole("admin");
        siteUserRepository.save(siteUser);
        siteUser.setUserId("admin" + siteUser.getId());
        siteUserRepository.save(siteUser);
    }

    public SiteUser userSignup(String name,String number,String gender, String birthDay, String email, String userRole){
        SiteUser siteUser = new SiteUser();
        siteUser.setUserId(number);
        siteUser.setPassword(passwordEncoder.encode(birthDay));
        siteUser.setName(name);
        siteUser.setNumber(number);
        siteUser.setBirthDate(birthDay);
        siteUser.setGender(gender);
        siteUser.setEmail(email);
        siteUser.setUserRole(userRole);
        return siteUserRepository.save(siteUser);
    }

    public SiteUser getUser(String name) {
        SiteUser siteUser = siteUserRepository.findByUserId(name).get();
        return siteUser;
    }

    public SiteUser getUserByUsername(String username) {
        Optional<SiteUser> siteUser = this.siteUserRepository.findByName (username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException ("사용자를 찾을 수 없습니다.");
        }
    }


    public void changePassword(Long userId, String currentPassword, String newPassword) throws Exception {
        SiteUser user = siteUserRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new Exception("현재 비밀번호가 올바르지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        siteUserRepository.save(user);
    }


    public SiteUser findByUserName(String username){
        return siteUserRepository.findByUserId(username).orElseThrow();
    }

    public SiteUser findById(Long id){
        return siteUserRepository.findById(id).orElseThrow();
    }

}



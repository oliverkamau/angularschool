package com.app.schservice.config;


import com.app.schservice.users.model.User;
import com.app.schservice.users.repository.UserRepo;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;

@Service
public class SystemUserDetails implements UserDetailsService {
    private UserRepo userRepo;
    private BCryptPasswordEncoder encoder;
    private static final long OTP_VALID_DURATION = 5 * 60 * 1000;;
    private JavaMailSender javaMailSender;


    public SystemUserDetails(UserRepo userRepo, BCryptPasswordEncoder encoder, JavaMailSender javaMailSender) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepo.findByUsernameAndActiveTrue(username);
        if (user==null) {
            throw new UsernameNotFoundException("Invalid Username or Password");
        }
        return new UserDetailsImpl(user);

    }

    public String generateOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }

    public void promptOTP(User user) {

        String otp =generateOTP();
        user.setOtp(encoder.encode(otp));
        user.setOtpRequestedTime(new Date());
        userRepo.save(user);
        new Thread(
                () -> {
                    try {
                        mailOTP(user,otp);
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }).start();

    }

    public void verifyOTP(User user, String otp) throws Exception {

        if (user.getOtpRequestedTime().getTime() + OTP_VALID_DURATION < System.currentTimeMillis()) {
            throw new Exception("OTP has expired!");
        }
        if(!encoder.matches(otp,user.getOtp())) {
            throw new Exception("OTP is invalid!");
        }


    }



    private void mailOTP(User user, String token) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("kamaugaby13@gmail.com","MERCHANTPORTAL");
        helper.setTo(user.getEmail());

        helper.setSubject("OTP LOGIN VERIFICATION");
        String name=user.getUserFirstname()+" "+user.getUserLastname();
        message.setContent("<h2>Register to Support System Request</h2>" +
                "<h3 style=\"color:black\">Dear "+name+" Your One Time Verification Code is <span style=\"color:blue;font-weight: bold;\">"+token+"</span></h3> ","text/html; charset=utf-8");
        javaMailSender.send(message);
    }


}

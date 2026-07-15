package com.binhtv.sendsmsservice.service;

import com.binhtv.sendsmsservice.dto.ContactRequest;
import com.binhtv.sendsmsservice.model.NewsletterSubscriber;
import com.binhtv.sendsmsservice.repository.NewsletterSubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final NewsletterSubscriberRepository subscriberRepository;

    @Value("${mail.from:no-reply@evo.local}") private String from;
    @Value("${mail.support-to:support@evo.local}") private String supportTo;

    public void sendContact(ContactRequest request) {
        send(supportTo, "Liên hệ mới từ " + request.name(),
                "Email: " + request.email() + "\nĐiện thoại: " + request.phone() + "\n\n" + request.message());
    }

    @Transactional
    public void subscribe(String email) {
        if (!subscriberRepository.existsByEmailIgnoreCase(email)) {
            subscriberRepository.save(new NewsletterSubscriber(email));
            send(email, "Chào mừng đến Evo Nội Thất", "Bạn đã đăng ký nhận bản tin Evo Nội Thất thành công.");
        }
    }

    public void sendPasswordReset(String email, String username, String resetUrl) {
        send(email, "Đặt lại mật khẩu Evo", "Xin chào " + username + ",\n\nĐặt lại mật khẩu tại: " + resetUrl
                + "\nLiên kết hết hạn sau 15 phút.");
    }

    public void sendOrderConfirmation(String email, String receiverName, String orderNumber) {
        send(email, "Xác nhận đơn hàng " + orderNumber,
                "Xin chào " + receiverName + ",\n\nEvo đã nhận đơn hàng " + orderNumber + ".");
    }

    private void send(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}

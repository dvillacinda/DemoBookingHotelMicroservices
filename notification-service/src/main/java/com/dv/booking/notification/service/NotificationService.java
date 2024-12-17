package com.dv.booking.notification.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    public final JavaMailSender javaMailSender; 

    @KafkaListener(topics = "reservation-placed", groupId = "notification-service")
    public void listen(com.dv.microservices.reservation.event.ReservationEvent reservationEvent) {
        log.info("Got Message from reservation-placed topic {}", reservationEvent);
        
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("hotels@example.com"); 
            messageHelper.setTo(reservationEvent.getEmail());
            messageHelper.setSubject(String.format("Your Hotel Reservation with Reservation Number %s is Confirmed", reservationEvent.getReservationNumber()));
            messageHelper.setText(String.format("""
                            Hi %s,

                            Your hotel reservation with reservation number %s is now confirmed.
                            
                            Best Regards,
                            Hotel Booking Team
                            """,
                    reservationEvent.getEmail(),
                    reservationEvent.getReservationNumber()));
        };

        try {
            javaMailSender.send(messagePreparator);
            log.info("Reservation notification email sent!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new RuntimeException("Exception occurred when sending mail to " + reservationEvent.getEmail(), e);
        }
    }
}

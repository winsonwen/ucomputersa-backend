package com.ucomputersa.monolithic.service.otpServices;


import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioVerifyService {

    @Value("${twilio.verify-service-sid}")
    private String verifyServiceSid;

    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioVerifyService.class);

    /**
     * Send an OTP to the given phone number.
     *
     * @param phoneNumber The phone number to send the OTP to.
     */
    public void sendOtp(String phoneNumber) {

        // US phone only for testing
        String formattedNumber = formatPhoneNumber(phoneNumber, "1");

        // TODO: if phone number is not in a valid length, do something here

        LOGGER.info("Send OTP to phone number : {}", formattedNumber);

        Verification.creator(
                verifyServiceSid,
                formattedNumber,
                "sms"             // Channel (sms, call, email, etc.)
        ).create();
    }

    private String formatPhoneNumber(String phoneNumber, String countryCode) {
        String cleanedNumber = phoneNumber.replaceAll("[^0-9]", "");

        return "+" + countryCode + cleanedNumber;
    }

    /**
     * Verify the OTP entered by the user.
     *
     * @param phoneNumber The phone number to verify.
     * @param otp         The OTP entered by the user.
     * @return True if the OTP is valid, false otherwise.
     */
    public boolean verifyOtp(String phoneNumber, String otp) {
        // US phone only for testing
        String formattedNumber = formatPhoneNumber(phoneNumber, "1");

        VerificationCheck verificationCheck = VerificationCheck.creator(
                        verifyServiceSid)
                .setCode(otp)
                .setTo(formattedNumber)
                .create();
        return "approved".equals(verificationCheck.getStatus()); // Check if OTP is approved
    }
}
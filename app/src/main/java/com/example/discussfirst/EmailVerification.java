package com.example.discussfirst;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailVerification extends AppCompatActivity {

    private EditText emailInput;
    private Button sendButton;
    private String sentCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        emailInput = findViewById(R.id.emailInput);
        sendButton = findViewById(R.id.sendButton);

        // Vendos automatikisht emailin nga aktiviteti i mëparshëm
        String emailFromLogin = getIntent().getStringExtra("USER_EMAIL");
        if (emailFromLogin != null && !emailFromLogin.isEmpty()) {
            emailInput.setText(emailFromLogin); // Vendos emailin e marrë
            emailInput.setFocusable(false); // E bën readonly
            emailInput.setClickable(false);
        } else {
            emailInput.setText(""); // Nëse nuk ka email, vendos bosh
        }

        sendButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim(); // Merret nga EditText
            String code = generateVerificationCode();
            sentCode = code;
            sendEmail(email, code);
        });
    }

    private String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    private void sendEmail(String recipient, String code) {
        final String senderEmail = "erand.kurtaliqi@student.uni-pr.edu";
        final String senderPassword = "lmpp pseh chzs osdc";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        new Thread(() -> {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject("Your Verification Code");
                message.setText("Your 6-digit verification code is: " + code);

                Transport.send(message);

                runOnUiThread(() -> {
                    Toast.makeText(EmailVerification.this, "Verification code sent!", Toast.LENGTH_SHORT).show();
                    String emailFromLogin = getIntent().getStringExtra("USER_EMAIL");
                    Intent intent = new Intent(EmailVerification.this, CodeVerificationActivity.class);
                    intent.putExtra("sentCode", sentCode);
                    intent.putExtra("USER_EMAIL", emailFromLogin);
                    startActivity(intent);
                });
            } catch (Exception e) {
                Log.e("EmailVerification", "Dërëgimi i emailit dështoi", e);
                runOnUiThread(() -> {
                    Toast.makeText(EmailVerification.this, "Dërëgimi i emailit dështoi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}

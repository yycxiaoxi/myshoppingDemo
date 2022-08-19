package com.xiaoxiz.xiaomaibu.util;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmaillUtil {
    public static void  send_mail(String to,String text,String Subject) throws MessagingException {
        Properties properties=new Properties();
        properties.put("mail.smtp.host", "smtp.qq.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //两个参数分别是发送邮件的账户和密码
                return new PasswordAuthentication("1632624277@qq.com","manpgiuitcrtcadi");
            }
        });

        //创建邮件对象
        Message message = new MimeMessage(session);
        //设置发件人
        message.setFrom(new InternetAddress("1632624277@qq.com"));
        //设置收件人
        message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
        //设置主题
        message.setSubject(Subject);
        //设置邮件正文  第二个参数是邮件发送的类型
        message.setContent("欢迎使用xiaomaibu,"+text,"text/html;charset=UTF-8");
        //发送一封邮件
        Transport.send(message);

    }
	/*public static void main(String[] args) throws Exception { // 做测试用
		EmailUitl.send_mail("3527468562@qq.com","你好，这是一封测试邮件，无需回复。");//填写接收邮箱※
        System.out.println("发送成功");
    }*/
}

package com.evan.aiu.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.evan.aiu.entity.User;
import com.evan.aiu.result.Result;
import com.evan.aiu.result.ResultFactory;
import com.evan.aiu.service.AdminUserRoleService;
import com.evan.aiu.service.UserService;
import com.evan.aiu.utils.Sample;
import com.evan.aiu.utils.VerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AdminUserRoleService adminUserRoleService;

    @GetMapping("/api/admin/user")
    public Result listUsers() {
        return ResultFactory.buildSuccessResult(userService.list());
    }

    @GetMapping("/api/userbyname")
    public Result listUsers(@RequestParam String userName) {
        return ResultFactory.buildSuccessResult(userService.findByUsername(userName));
    }

    /**
     * 查询志愿者
     * @return
     */
    @GetMapping("/api/userbyrole")
    public Result listUsersbyRole() {
        return ResultFactory.buildSuccessResult(userService.findByRole());
    }

    @GetMapping("/api/volunteerauti")
    public Result volunteerAuti(@RequestParam String userName) {
        return ResultFactory.buildSuccessResult(userService.volunteerAuti(userName));
    }

    @PutMapping("/api/admin/user/status")
    public Result updateUserStatus(@RequestBody @Valid User requestUser) {
        userService.updateUserStatus(requestUser);
        return ResultFactory.buildSuccessResult("用户状态更新成功");
    }

    @PutMapping("/api/admin/user/password")
    public Result resetPassword(@RequestBody @Valid User requestUser) {
        userService.resetPassword(requestUser);
        return ResultFactory.buildSuccessResult("重置密码成功");
    }


    @PutMapping("/api/updatepassword")
    public Result updatePassword(@RequestBody @Valid User requestUser) {
        userService.updatePassword(requestUser);
        return ResultFactory.buildSuccessResult("重置密码成功");
    }

    @PutMapping("/api/admin/user")
    public Result editUser(@RequestBody @Valid User requestUser) {
        userService.editUser(requestUser);
        return ResultFactory.buildSuccessResult("修改用户信息成功");
    }

    @PutMapping("/api/admin/usera")
    public Result editUsera(@RequestBody @Valid User requestUser) {
        userService.editUsera(requestUser);
        return ResultFactory.buildSuccessResult("修改用户信息成功");
    }

    @GetMapping(path = "/api/getCode")
    @ResponseBody
    public Map getCode(HttpServletResponse response) throws IOException {
        Map result = new HashMap();
        Map verificationCode = (Map) VerificationCode.getVerificationCode();
        BufferedImage bufferedImage = (BufferedImage) verificationCode.get("imgStream");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage,"jpeg",outputStream);
        byte[] bytes = outputStream.toByteArray();
        bufferedImage.flush();
        outputStream.flush();
        outputStream.close();
        result.put("img",bytes);
        result.put("code",verificationCode.get("code"));
        return result;
    }

    @GetMapping(path = "/api/sendMsg")
    @ResponseBody
    public Result sendMsg(@RequestParam  String phone) throws Exception {
        com.aliyun.dysmsapi20170525.Client client = Sample.createClient("LTAI5tRHXr5bvQ1VuPQnPQB2", "wc83vLiWjKrSa2zOC2YtquYNnJddN4");
        //定义取值范围
        String str = "0123456789";
        //容量为4
        StringBuilder code = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            //遍历4次，拿到某个字符并且拼接
            char ch = str.charAt(new Random().nextInt(str.length()));
            code.append(ch);
        }
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setPhoneNumbers(phone)
                .setTemplateParam("{\"code\":\""+code+"\"}");
        // 复制代码运行请自行打印 API 的返回值
        client.sendSms(sendSmsRequest);
        SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
        System.out.println(JSONObject.toJSONString(sendSmsResponse));
        return ResultFactory.buildSuccessResult(code);
    }
}

package org.example.controller;


import org.example.entity.User;
import org.example.service.UserService;
import org.example.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin //解决跨域问题
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;



    @RequestMapping(value = "/login")
    public ModelMap login(@RequestBody Map<String,Object> param){
        ModelMap result=new ModelMap();
        ModelMap meta=new ModelMap();
        if(param.get("username")!=null && param.get("password")!=null) {
            User user=new User();
            String username = param.get("username").toString();
            String password = param.get("password").toString();
            user.setUsername(username);
            user.setPassword(password);
            try {
                if(userService.login(user)!=null){
                    Integer userid=userService.login(user);
                    String token=TokenUtils.createToken(username,password);
                    ModelMap data=new ModelMap();
                    meta.addAttribute("msg","Login Success");
                    meta.addAttribute("status",200);
                    data.addAttribute("userid",userid);
                    data.addAttribute("username",username);
                    data.addAttribute("token",token);
                    result.addAttribute("data",data);
                }
                else {
                    meta.addAttribute("msg","Login Failure");
                    meta.addAttribute("status",404);
                }
            } catch (Exception e) {
                e.printStackTrace();
                meta.addAttribute("msg", "DataBase Error");
                meta.addAttribute("status",500);
                result.addAttribute("meta",meta);
                return result;
            }
        }
        else {
            meta.addAttribute("msg","Some parameters are empty");
            meta.addAttribute("status",400);
        }
        result.addAttribute("meta",meta);
        return result;
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> meta = new HashMap<>();

        try {
            // 从请求头中获取token
            String token = request.getHeader("Authorization");
            if (token == null || token.isEmpty()) {
                meta.put("status", 400);
                meta.put("msg", "Token不存在");
                result.put("meta", meta);
                return result;
            }

            // 这里可以添加token失效的逻辑，比如将token加入黑名单
            // 具体实现取决于你的token管理策略

            meta.put("status", 200);
            meta.put("msg", "退出成功");
            result.put("meta", meta);
        } catch (Exception e) {
            meta.put("status", 500);
            meta.put("msg", "退出失败: " + e.getMessage());
            result.put("meta", meta);
        }

        return result;
    }

    @PostMapping("/register")
    public ModelMap register(@RequestBody Map<String, Object> param) {
        ModelMap result = new ModelMap();
        ModelMap meta = new ModelMap();

        if (param.get("username") == null || param.get("password") == null) {
            meta.addAttribute("msg", "用户名或密码不能为空");
            meta.addAttribute("status", 400);
            result.addAttribute("meta", meta);
            return result;
        }

        String username = param.get("username").toString();
        String password = param.get("password").toString();

        try {
            // 检查用户名是否已存在
            if (userService.isExistByUsername(username)) {
                meta.addAttribute("msg", "用户名已存在");
                meta.addAttribute("status", 400);
                result.addAttribute("meta", meta);
                return result;
            }

            // 创建新用户
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            if (userService.register(user)) {
                meta.addAttribute("msg", "注册成功");
                meta.addAttribute("status", 200);
            } else {
                meta.addAttribute("msg", "注册失败");
                meta.addAttribute("status", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            meta.addAttribute("msg", "数据库错误");
            meta.addAttribute("status", 500);
        }

        result.addAttribute("meta", meta);
        return result;
    }
}

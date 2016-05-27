package cn.edu.hj.controller;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.edu.hj.bean.User;

@Controller
@RequestMapping(value = "/user")
//写上这个注解的话那么在Model中进行添加key为loginUser的时候会默认添加到session中
@SessionAttributes(value = "loginUser")
public class UserController {
	
	Map<String,User> users = new LinkedHashMap<String,User>();
	public UserController(){//无参数的构造方法会默认被调用一次
		System.out.println("初始化....");
		users.put("ldh",new User("ldh", "123", "ldh@qq.com"));
		users.put("gfc",new User("gfc", "123", "gfc@qq.com"));
		users.put("zxy",new User("zxy", "123", "zxy@qq.com"));
		users.put("lm ",new User("lm ", "123", " lm@qq.com"));
	}
	/**
	 * 初始化user列表信息
	 * @param model
	 * @param user
	 */
	public void init(Model model,User user){
		if(user != null){
			users.put(user.getUsername(), user);
		}
		model.addAttribute("users", users);
	}
	
	/**
	 * 跳转到list页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list.htm")
	public String list(Model model){
		init(model, null);
		return "user/userlist";
	}
	
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@RequestMapping(value = "/add.htm", method = RequestMethod.GET)//使用get方法的时候
	public String add(Model model){
		//因为在页面上有个modelAttribute="user" user对象，所以要传递一个空的过去，否则报错
		model.addAttribute(new User());//默认的键为值的类型,为user
		return "user/adduser";
	}
	
	/**
	 * 执行添加操作,并跳转到list页面
	 * @param user 如果要验证前面需要加上@Valid注解
	 * @param binding 这个是在验证的时候把信息跟该类进行绑定,可以得到验证的结果信息
	 * binding的参数必须要放在要验证的那个对象之后
	 * @param model 
	 * @return
	 */
	@RequestMapping(value = "/add.htm", method = RequestMethod.POST)//使用post方法的时候
	public String add(@Valid User user,BindingResult binding,Model model){
		if(binding.hasErrors()){
			return "user/adduser";
		}
		init(model, user);
		return "redirect:/user/list.htm";
	}
	
	/**
	 * 根据username得到user对象
	 * 路径为：/springmvc/user/ldh.htm
	 * @param username前面加上@PathVariable表示路径变量,会跟@RequestMapping的{username}进行对应
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{username}.htm",method = RequestMethod.GET)
	public String show(@PathVariable String username,Model model){
		User user = users.get(username);
		model.addAttribute("user", user);
		return "user/show";
	}
	
	/**
	 * 做删除操作,url类似REST风格
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/delete/{username}.htm",method = RequestMethod.GET)
	public String delete(@PathVariable String username){
		users.remove(username);
		return "redirect:/user/list.htm";
	}
	
	/**
	 * 根据username得到user转到更新jsp页面
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/update/{username}.htm",method = RequestMethod.GET)
	public String update(@PathVariable String username,Model model){
		User user = users.get(username);
		model.addAttribute("user", user);
		return "user/adduser";
	}
	/**
	 * 做更新操作,url类似REST风格
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/update/{username}.htm",method = RequestMethod.POST)
	public String update(@PathVariable String username,@Valid User user,BindingResult br){
		if(br.hasErrors()){
			return "/user/adduser";
		}
		users.put(user.getUsername(), user);
		return "redirect:/user/list.htm";
	}
	
	/**
	 * params = "json"相当于在方法参数中写入@RequestParam String json
	 * 那么访问该视图的话必须是/springmvc/user/{username}.htm?json=..
	 * 目前tomcat不支持此类型的解析，方法可以进来，但是返回不了
	 * @param username
	 * @param model
	 * @return
	 */
	@ResponseBody//加上该注解,可以返回对象而不用是String类型
	@RequestMapping(value = "/{username}.htm",params = "json")
	public User showJson(@PathVariable String username,Model model){
		System.out.println("username:"+username);
		return users.get(username);
	}
	
	/**
	 * 转到登录界面
	 * @return
	 */
	@RequestMapping(value = "/login.htm",method = RequestMethod.GET)
	public String login(){
		return "/user/login";
	}
	
	/**
	 * 在登录界面进行登录
	 * @param username
	 * @param password
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login.htm",method = RequestMethod.POST)
	public String login(String username,String password,Model model){
		if(!users.containsKey(username)){
			throw new RuntimeException("用户名不存在!");
		}
		if(!password.equals(users.get(username).getPassword())){
			throw new RuntimeException("密码不正确");
		}
		//存放入session中,因为前面已经加了@SessionAttributes(value = "loginUser")注解
		model.addAttribute("loginUser", users.get(username));
		return "redirect:/user/list.htm";
	}
	
	/**
	 * 进行异常的捕获,这里是该action中抛出的RuntimeException都在这个方法进行捕获
	 * @param ex
	 * @param req
	 * @return
	 */
	@ExceptionHandler(value = {RuntimeException.class})
	public String handlerException(Exception ex,HttpServletRequest req){
		req.setAttribute("ex", ex);//把异常放入request请求中
		return "error";//转到error页面
	}
	
	/**
	 * 使用重定向来进行传递对象
	 * @param model
	 * @param ra
	 * @return
	 */
	@RequestMapping(value = "/redir.htm")
	public String redir(Model model,RedirectAttributes ra){
//		model.addAttribute("movie", "海贼王");//使用这种方式在重定向是传递不了的
		ra.addFlashAttribute("movie", "海贼王");//使用这种可以
		return "redirect:/user/list.htm";
	}
	
	/**
	 * 跳转到上传页面
	 * @param photo
	 * @return
	 */
	@RequestMapping(value = "upload.htm",method = RequestMethod.GET)
	public String uploadPhoto(){
		return "user/upload";
	}
	
	/**
	 * 进行单个文件上传
	 * @param photo 这个参数名需要跟表单中上传的name属性名称一样,否则上传不成功
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "upload.htm",method = RequestMethod.POST)
	public String uploadPhoto(MultipartFile photo,Model model,HttpServletRequest req){
		System.out.println(photo.getContentType());//文件的类型,image/jpeg
		System.out.println(photo.getName());//上传的表单名称
		System.out.println(photo.getOriginalFilename());//上传的文件名
		String realpath = req.getSession().getServletContext().getRealPath("/upload/");
		System.out.println(realpath);//容器的webapp目录下
		try {
			FileUtils.copyInputStreamToFile(photo.getInputStream(), new File(realpath + "/" +photo.getOriginalFilename()));
		} catch (IOException e) {e.printStackTrace();}
		model.addAttribute("message", "上传成功");
		return "user/upload";
	}
	
	/**
	 * 进行多个文件上传
	 * @param photos 这个参数名需要跟表单中上传的name属性名称一样,否则上传不成功
	 * 在批量上传的时候需要加上这个注解@RequestParam
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "uploads.htm",method = RequestMethod.POST)
	public String uploadPhoto(@RequestParam(required = false)MultipartFile[] photos,Model model,HttpServletRequest req){
		String realpath = req.getSession().getServletContext().getRealPath("/upload/");
		try {
			for(MultipartFile photo : photos){
				if(photo.isEmpty())continue;//可能会有三个上传表单却不一定三个都进行上传，这样其他的就为空需要判断
				FileUtils.copyInputStreamToFile(photo.getInputStream(), new File(realpath + "/" +photo.getOriginalFilename()));
			}
		} catch (IOException e) {e.printStackTrace();}
		model.addAttribute("message", "上传成功");
		return "user/upload";
	}
}

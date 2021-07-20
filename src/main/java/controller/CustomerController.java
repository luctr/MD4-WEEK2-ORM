package controller;

import model.Customer;
import model.CustomerUploadFile;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import service.ICustomerService;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    private final ICustomerService customerService;

    private final Environment environment;

    public CustomerController(Environment environment,
                              ICustomerService customerService){
        this.environment = environment;
        this.customerService = customerService;
    }

    @GetMapping("/Show")
    public String showAll(ModelMap modelMap){
//        ModelAndView modelAndView = new ModelAndView("home");
        List<Customer> customerList = customerService.findAll();
        modelMap.addAttribute("Customer",customerList);
        return "home";
    }

    @GetMapping("Create")
    public ModelAndView createForm(){
        ModelAndView modelAndView = new ModelAndView("create");
        modelAndView.addObject("obj",new CustomerUploadFile());
        return modelAndView;

    }

    @GetMapping("remove")
    public String remove(int id){
        customerService.delete(id);
        return "redirect:/customers/Show";
    }

    @GetMapping("Update")
    public ModelAndView modelAndView(int id){
        ModelAndView modelAndView = new ModelAndView("update");
        Customer customer = customerService.findOne(id);
        modelAndView.addObject("customer",customer);
        modelAndView.addObject("customerF",new CustomerUploadFile());
        return modelAndView;
    }

    @PostMapping("Update")
    public String update(CustomerUploadFile customerUploadFile){
        MultipartFile multipartFile = customerUploadFile.getImg();
        String nameFile = multipartFile.getOriginalFilename();
        String localFile = environment.getProperty("uploadFile");
        try {
            FileCopyUtils.copy(multipartFile.getBytes(),new File(localFile+nameFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Customer customer = new Customer();
        customer.setId(customerUploadFile.getId());
        customer.setName(customerUploadFile.getName());
        customer.setAddress(customerUploadFile.getAddress());
        customer.setImg(nameFile);
        customerService.edit(customer.getId(),customer);
        return "redirect:Show";
    }

    @PostMapping("Create")
    public String create(CustomerUploadFile customerUploadFile){
        MultipartFile multipartFile = customerUploadFile.getImg();
        String nameFile = multipartFile.getOriginalFilename();
        String localFile = environment.getProperty("uploadFile");
        try {
            FileCopyUtils.copy(multipartFile.getBytes(),new File(localFile + nameFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Customer customer = new Customer();
        customer.setId((int) (Math.random()*10000));
        customer.setName(customerUploadFile.getName());
        customer.setAddress(customerUploadFile.getAddress());
        customer.setEmail(customerUploadFile.getEmail());
        customer.setImg(nameFile);
        customerService.save(customer);
        return "redirect:Show";
    }
}

package com.itheima.crm.web.action;

import com.itheima.crm.domain.Customer;
import com.itheima.crm.domain.PageBean;
import com.itheima.crm.service.CustomerService;
import com.itheima.crm.utils.UploadUitls;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;


public class CustomerAction extends ActionSupport implements ModelDriven<Customer>{
    private Customer customer = new Customer();

    public static final Logger logger = LoggerFactory.getLogger(CustomerAction.class);

    @Override
    public Customer getModel() {
        return customer;
    }

    private CustomerService customerService;

    private String uploadFileName;
    private String uploadContentType;
    private File upload;

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    public void setUpload(File upload) {
        this.upload = upload;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public String saveUI(){
        return "saveUI";
    }

    public String save() throws IOException {
        if (upload != null){
            String path = "D:/upload";
            String uuidFileName = UploadUitls.getUuidFileName(uploadFileName);
            String realPath = UploadUitls.getPath(uuidFileName);
            String url = path+realPath;
            File file = new File(url);
            if (!file.exists()){
                file.mkdirs();
            }
            File dictFile = new File(url + "/" + uuidFileName);
            FileUtils.copyFile(upload,dictFile);
            customer.setCust_image(url+"/"+uuidFileName);
        }

        customerService.save(customer);
        return "saveSuccess";
    }

    //private Integer currPage;
    private Integer currPage = 1;
    public void setCurrPage(Integer currPage) {
        if (currPage == null){
            this.currPage = 1;
        }else {
            this.currPage = currPage;
        }
    }

    //private Integer pageSize;
    private Integer pageSize = 3;
    public void setPageSize(Integer pageSize) {
        if (pageSize == null){
            this.pageSize = 3;
        }else {
            this.pageSize = pageSize;
        }

    }

    public String findAll(){
        logger.info("用户查询列表接口"+ JSONObject.fromObject(customer));
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Customer.class);
        if (customer.getCust_name() != null && !"".equals(customer.getCust_name())){
            detachedCriteria.add(Restrictions.like(
                    "cust_name","%" +customer.getCust_name() +"%"));
        }
        if (customer.getBaseDictSource() != null) {
            if (customer.getBaseDictSource().getDict_id() != null && !"".equals(customer.getBaseDictSource().getDict_id())){
                detachedCriteria.add(Restrictions.eq(
                        "baseDictSource.dict_id",customer.getBaseDictSource().getDict_id()));
            }
        }
        if (customer.getBaseDictLevel() != null) {
            if (customer.getBaseDictLevel().getDict_id() != null && !"".equals(customer.getBaseDictLevel().getDict_id())){
                detachedCriteria.add(Restrictions.eq(
                        "baseDictLevel.dict_id",customer.getBaseDictLevel().getDict_id()));
            }
        }
        if (customer.getBaseDictIndustry() != null) {
            if (customer.getBaseDictIndustry().getDict_id() != null && !"".equals(customer.getBaseDictIndustry().getDict_id())){
                detachedCriteria.add(Restrictions.eq(
                        "baseDictIndustry.dict_id",customer.getBaseDictIndustry().getDict_id()));
            }
        }
        logger.info(JSONObject.fromObject(detachedCriteria)+"");
        PageBean<Customer> pageBean = customerService.findByPage(detachedCriteria,currPage,pageSize);
        ActionContext.getContext().getValueStack().push(pageBean);
        return "findAll";
    }

    public String delete(){
        customer = customerService.findById(customer.getCust_id());
        if (customer.getCust_image() != null){
            File file = new File(customer.getCust_image());
            if (file.exists()){
                file.delete();
            }
        }
        customerService.delete(customer);
        return "deleteSuccess";
    }

    public String edit(){
        customer = customerService.findById(customer.getCust_id());
        return "editSuccess";
    }

    public String update() throws IOException {
        if (upload != null){
            String cust_image = customer.getCust_image();
            if (cust_image!=null && !"".equals(cust_image)){
                File file = new File(cust_image);
                if (file.exists()){
                    file.delete();
                }
            }
            String uuidFileName = UploadUitls.getUuidFileName(uploadFileName);
            String path = "D:\\upload\\"+uuidFileName;
            File file = new File(path);
            FileUtils.copyFile(upload,file);
        }
        customerService.update(customer);
        return "updateSuccess";
    }
}

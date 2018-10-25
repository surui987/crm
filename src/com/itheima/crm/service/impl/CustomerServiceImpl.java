package com.itheima.crm.service.impl;

import com.itheima.crm.dao.CustomerDao;
import com.itheima.crm.dao.impl.CustomerDaoImpl;
import com.itheima.crm.domain.Customer;
import com.itheima.crm.domain.PageBean;
import com.itheima.crm.service.CustomerService;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class CustomerServiceImpl implements CustomerService {
    private CustomerDaoImpl customerDao;

    public void setCustomerDao(CustomerDaoImpl customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public void save(Customer customer) {
        customerDao.save(customer);
    }


    @Override
    public PageBean<Customer> findByPage(DetachedCriteria detachedCriteria, Integer currPage,Integer pageSize) {
        PageBean<Customer> pageBean = new PageBean<>();
        pageBean.setCurrPage(currPage);
        pageBean.setPageSize(pageSize);
        Integer totalCouunt = customerDao.findCount(detachedCriteria);
        pageBean.setTotalCount(totalCouunt);
        Double tc = totalCouunt.doubleValue();
        Double num = Math.ceil(tc/ pageSize);
        pageBean.setTotalPage(num.intValue());

        Integer begin = (currPage - 1) * pageSize;
        List<Customer> list = customerDao.findByPage(detachedCriteria, begin, pageSize);

        pageBean.setList(list);

        return pageBean;
    }

    @Override
    public Customer findById(Long cust_id) {
        return customerDao.findById(cust_id);
    }

    @Override
    public void delete(Customer customer) {
        customerDao.delete(customer);
    }

    @Override
    public void update(Customer customer) {
        customerDao.update(customer);
    }

}

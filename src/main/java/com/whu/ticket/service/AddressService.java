package com.whu.ticket.service;

import com.whu.ticket.pojo.Address;
import org.springframework.jdbc.datasource.AbstractDriverBasedDataSource;

import java.util.List;

public interface AddressService {
    public void addAddress(Address address);
    public void deleteAddress(int id,int user_id)throws Exception;
    public List<Address> queryAddress(int user_id,int pageNo,int pageSize);
    public void modifyAddress(Address address);
}

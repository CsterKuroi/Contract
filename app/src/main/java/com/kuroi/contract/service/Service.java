package com.kuroi.contract.service;

import java.util.List;

import com.kuroi.contract.dataaccess.DBOperation;
import com.kuroi.contract.model.Contract;

import android.content.Context;

public class Service {
    private DBOperation dao = null;
    public Service(Context context) {
        dao = new DBOperation(context);
    }
    public boolean save(Contract contract) {
        boolean flag = dao.save(contract);
        return flag;
    }
    public List getByName(String queryName, int sort) {
        List list = dao.getByName(queryName, sort);
        return list;
    }
    public Contract getById(int id) {
        Contract contract = dao.getById(id);
        return contract;
    }
    public boolean update(Contract contract) {
        boolean flag = dao.update(contract);
        return flag;
    }
    public void delete(int id) {
        dao.delete(id);
    }
    public Long getCount() {
        return dao.getCount();
    }
}

package com.xl.jd.service;

import com.xl.jd.dao.ItemDao;
import com.xl.jd.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemDao itemDao;

    /**
     * 保存商品
     * @param item
     */
    @Transactional
    public void saveItem(Item item){
        this.itemDao.save(item);
    }

    /**
     * 根据条件查询商品
     * @param item
     * @return
     */
    public List<Item> findAll(Item item){
        Example<Item> example = Example.of(item);
        List<Item> items = this.itemDao.findAll(example);
        return items;
    }
}

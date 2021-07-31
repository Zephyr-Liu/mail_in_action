package com.zenas.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.zenas.mall.mbg.mapper.PmsBrandMapper;
import com.zenas.mall.mbg.model.PmsBrand;
import com.zenas.mall.mbg.model.PmsBrandExample;
import com.zenas.mall.service.PmsBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ClientInfoStatus;
import java.util.List;

@Service
public class PmsBrandServiceImpl implements PmsBrandService {


    private PmsBrandMapper brandMapper;

    @Autowired
    public void setBrandMapper(PmsBrandMapper brandMapper) {
        this.brandMapper = brandMapper;
    }

    @Override
    public List<PmsBrand> listAllBrand() {
        return brandMapper.selectByExample(new PmsBrandExample());
    }

    @Override
    public int createBrand(PmsBrand brand) {
        return brandMapper.insertSelective(brand);
    }

    @Override
    public int updateBrand(Long id, PmsBrand brand) {
        brand.setId(id);
        return brandMapper.updateByPrimaryKeySelective(brand);
    }

    @Override
    public int deleteBrand(Long id) {
        return brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<PmsBrand> listBrand(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return brandMapper.selectByExample(new PmsBrandExample());
    }

    @Override
    public PmsBrand getBrand(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }
}

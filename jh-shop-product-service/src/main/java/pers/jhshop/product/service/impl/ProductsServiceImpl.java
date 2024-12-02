package pers.jhshop.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.product.model.req.ProductsCreateReq;
import pers.jhshop.product.model.req.ProductsQueryReq;
import pers.jhshop.product.model.req.ProductsUpdateReq;
import pers.jhshop.product.model.vo.ProductsVO;
import pers.jhshop.product.model.entity.Products;
import pers.jhshop.product.mapper.ProductsMapper;
import pers.jhshop.product.service.IProductsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.jhshop.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Function;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products> implements IProductsService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBiz(ProductsCreateReq createReq) {
        if (Objects.isNull(createReq.getName())) {
            throw new ServiceException("商品名称不能为空");
        }



        Products entity = new Products();
        BeanUtil.copyProperties(createReq, entity);

        boolean insertResult = entity.insert();

        if (!insertResult) {
            throw new ServiceException("数据插入失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBiz(ProductsUpdateReq updateReq) {

        // 1.入参有效性判断
        if (Objects.isNull(updateReq.getId())) {
            throw new ServiceException("id不能为空");
        }

        Products entity = getById(updateReq.getId());
        if (Objects.isNull(entity)) {
            throw new ServiceException("商品表不存在");
        }

        // 2.重复性判断
        Products entityToUpdate = new Products();
        BeanUtil.copyProperties(updateReq, entityToUpdate);

        boolean updateResult = entityToUpdate.updateById();
        if (!updateResult) {
            throw new ServiceException("数据更新失败");
        }
    }

    @Override
    public ProductsVO getByIdBiz(Long id) {
        // 1.入参有效性判断
        if (Objects.isNull(id)) {
            throw new ServiceException("id不能为空");
        }

        // 2.存在性判断
        Products entity = getById(id);
        if (Objects.isNull(entity)) {
            throw new ServiceException("商品表不存在");
        }

        ProductsVO vo = new ProductsVO();
        BeanUtil.copyProperties(entity, vo);

            return vo;
    }

    @Override
    public Page<ProductsVO> pageBiz(ProductsQueryReq queryReq) {
        Page<Products> page = new Page<>(queryReq.getCurrent(), queryReq.getSize());
        page.addOrder(OrderItem.desc("id"));

        LambdaQueryWrapper<Products> queryWrapper = getLambdaQueryWrapper(queryReq);

        page(page, queryWrapper);

        Page<ProductsVO> pageVOResult = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<Products> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageVOResult;
        }

        List<ProductsVO> vos = records.stream().map(record -> {
            ProductsVO vo = new ProductsVO();
            BeanUtil.copyProperties(record, vo);
    
            return vo;
        }).collect(Collectors.toList());

        pageVOResult.setRecords(vos);
        return pageVOResult;
    }

    @Override
    public Page<Products> page(ProductsQueryReq queryReq) {
        Page<Products> page = new Page<>(queryReq.getCurrent(), queryReq.getSize());
        LambdaQueryWrapper<Products> queryWrapper = getLambdaQueryWrapper(queryReq);
        page(page, queryWrapper);
        return page;
    }

    @Override
    public List<Products> listByQueryReq(ProductsQueryReq queryReq) {
        LambdaQueryWrapper<Products> queryWrapper = getLambdaQueryWrapper(queryReq);
        List<Products> listByQueryReq = list(queryWrapper);
        return listByQueryReq;
    }

    @Override
    public Map<Long, Products> getIdEntityMap(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new HashMap<>();
        }

        LambdaQueryWrapper<Products> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Products::getId, ids);
        List<Products> entities = list(queryWrapper);
        if (CollectionUtils.isEmpty(entities)) {
            return new HashMap<>();
        }

        return entities.stream().collect(Collectors.toMap(Products::getId, Function.identity(), (v1, v2) -> v1));
    }

    @Override
    public Products getOneByQueryReq(ProductsQueryReq queryReq) {
        LambdaQueryWrapper<Products> queryWrapper = getLambdaQueryWrapper(queryReq);
        queryWrapper.last("LIMIT 1");

        List<Products> listByQueryReq = list(queryWrapper);
        if (CollectionUtils.isEmpty(listByQueryReq)) {
            return null;
        }

        return listByQueryReq.get(0);
    }

    private LambdaQueryWrapper<Products> getLambdaQueryWrapper(ProductsQueryReq queryReq) {
        LambdaQueryWrapper<Products> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(Objects.nonNull(queryReq.getId()), Products::getId, queryReq.getId());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getName()), Products::getName, queryReq.getName());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getNameLike()), Products::getName, queryReq.getNameLike());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getProductDescription()), Products::getProductDescription, queryReq.getProductDescription());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getProductDescriptionLike()), Products::getProductDescription, queryReq.getProductDescriptionLike());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getBrand()), Products::getBrand, queryReq.getBrand());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getBrandLike()), Products::getBrand, queryReq.getBrandLike());
        queryWrapper.eq(Objects.nonNull(queryReq.getCategoryId()), Products::getCategoryId, queryReq.getCategoryId());
        queryWrapper.eq(Objects.nonNull(queryReq.getPrice()), Products::getPrice, queryReq.getPrice());
        queryWrapper.eq(Objects.nonNull(queryReq.getStock()), Products::getStock, queryReq.getStock());
        queryWrapper.eq(Objects.nonNull(queryReq.getStatus()), Products::getStatus, queryReq.getStatus());
        queryWrapper.eq(Objects.nonNull(queryReq.getCreatedAt()), Products::getCreatedAt, queryReq.getCreatedAt());
        queryWrapper.eq(Objects.nonNull(queryReq.getUpdatedAt()), Products::getUpdatedAt, queryReq.getUpdatedAt());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getImageUrl()), Products::getImageUrl, queryReq.getImageUrl());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getImageUrlLike()), Products::getImageUrl, queryReq.getImageUrlLike());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getDescription()), Products::getDescription, queryReq.getDescription());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getDescriptionLike()), Products::getDescription, queryReq.getDescriptionLike());
        queryWrapper.eq(Objects.nonNull(queryReq.getValidFlag()), Products::getValidFlag, queryReq.getValidFlag());

        return queryWrapper;
    }

}

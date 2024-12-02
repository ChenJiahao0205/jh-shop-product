package pers.jhshop.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.product.model.req.ImagesCreateReq;
import pers.jhshop.product.model.req.ImagesQueryReq;
import pers.jhshop.product.model.req.ImagesUpdateReq;
import pers.jhshop.product.model.vo.ImagesVO;
import pers.jhshop.product.model.entity.Images;
import pers.jhshop.product.mapper.ImagesMapper;
import pers.jhshop.product.service.IImagesService;
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
 * 商品图片表 服务实现类
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImagesServiceImpl extends ServiceImpl<ImagesMapper, Images> implements IImagesService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBiz(ImagesCreateReq createReq) {


        Images entity = new Images();
        BeanUtil.copyProperties(createReq, entity);

        boolean insertResult = entity.insert();

        if (!insertResult) {
            throw new ServiceException("数据插入失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBiz(ImagesUpdateReq updateReq) {

        // 1.入参有效性判断
        if (Objects.isNull(updateReq.getId())) {
            throw new ServiceException("id不能为空");
        }

        Images entity = getById(updateReq.getId());
        if (Objects.isNull(entity)) {
            throw new ServiceException("商品图片表不存在");
        }

        // 2.重复性判断
        Images entityToUpdate = new Images();
        BeanUtil.copyProperties(updateReq, entityToUpdate);

        boolean updateResult = entityToUpdate.updateById();
        if (!updateResult) {
            throw new ServiceException("数据更新失败");
        }
    }

    @Override
    public ImagesVO getByIdBiz(Long id) {
        // 1.入参有效性判断
        if (Objects.isNull(id)) {
            throw new ServiceException("id不能为空");
        }

        // 2.存在性判断
        Images entity = getById(id);
        if (Objects.isNull(entity)) {
            throw new ServiceException("商品图片表不存在");
        }

        ImagesVO vo = new ImagesVO();
        BeanUtil.copyProperties(entity, vo);

            return vo;
    }

    @Override
    public Page<ImagesVO> pageBiz(ImagesQueryReq queryReq) {
        Page<Images> page = new Page<>(queryReq.getCurrent(), queryReq.getSize());
        page.addOrder(OrderItem.desc("id"));

        LambdaQueryWrapper<Images> queryWrapper = getLambdaQueryWrapper(queryReq);

        page(page, queryWrapper);

        Page<ImagesVO> pageVOResult = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<Images> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageVOResult;
        }

        List<ImagesVO> vos = records.stream().map(record -> {
            ImagesVO vo = new ImagesVO();
            BeanUtil.copyProperties(record, vo);
    
            return vo;
        }).collect(Collectors.toList());

        pageVOResult.setRecords(vos);
        return pageVOResult;
    }

    @Override
    public Page<Images> page(ImagesQueryReq queryReq) {
        Page<Images> page = new Page<>(queryReq.getCurrent(), queryReq.getSize());
        LambdaQueryWrapper<Images> queryWrapper = getLambdaQueryWrapper(queryReq);
        page(page, queryWrapper);
        return page;
    }

    @Override
    public List<Images> listByQueryReq(ImagesQueryReq queryReq) {
        LambdaQueryWrapper<Images> queryWrapper = getLambdaQueryWrapper(queryReq);
        List<Images> listByQueryReq = list(queryWrapper);
        return listByQueryReq;
    }

    @Override
    public Map<Long, Images> getIdEntityMap(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new HashMap<>();
        }

        LambdaQueryWrapper<Images> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Images::getId, ids);
        List<Images> entities = list(queryWrapper);
        if (CollectionUtils.isEmpty(entities)) {
            return new HashMap<>();
        }

        return entities.stream().collect(Collectors.toMap(Images::getId, Function.identity(), (v1, v2) -> v1));
    }

    @Override
    public Images getOneByQueryReq(ImagesQueryReq queryReq) {
        LambdaQueryWrapper<Images> queryWrapper = getLambdaQueryWrapper(queryReq);
        queryWrapper.last("LIMIT 1");

        List<Images> listByQueryReq = list(queryWrapper);
        if (CollectionUtils.isEmpty(listByQueryReq)) {
            return null;
        }

        return listByQueryReq.get(0);
    }

    private LambdaQueryWrapper<Images> getLambdaQueryWrapper(ImagesQueryReq queryReq) {
        LambdaQueryWrapper<Images> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(Objects.nonNull(queryReq.getId()), Images::getId, queryReq.getId());
        queryWrapper.eq(Objects.nonNull(queryReq.getProductId()), Images::getProductId, queryReq.getProductId());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getImageUrl()), Images::getImageUrl, queryReq.getImageUrl());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getImageUrlLike()), Images::getImageUrl, queryReq.getImageUrlLike());
        queryWrapper.eq(Objects.nonNull(queryReq.getIsMain()), Images::getIsMain, queryReq.getIsMain());
        queryWrapper.eq(Objects.nonNull(queryReq.getCreatedAt()), Images::getCreatedAt, queryReq.getCreatedAt());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getDescription()), Images::getDescription, queryReq.getDescription());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getDescriptionLike()), Images::getDescription, queryReq.getDescriptionLike());
        queryWrapper.eq(Objects.nonNull(queryReq.getValidFlag()), Images::getValidFlag, queryReq.getValidFlag());

        return queryWrapper;
    }

}

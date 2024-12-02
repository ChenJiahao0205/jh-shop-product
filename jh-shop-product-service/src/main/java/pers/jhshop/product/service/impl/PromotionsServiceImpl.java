package pers.jhshop.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.product.model.req.PromotionsCreateReq;
import pers.jhshop.product.model.req.PromotionsQueryReq;
import pers.jhshop.product.model.req.PromotionsUpdateReq;
import pers.jhshop.product.model.vo.PromotionsVO;
import pers.jhshop.product.model.entity.Promotions;
import pers.jhshop.product.mapper.PromotionsMapper;
import pers.jhshop.product.service.IPromotionsService;
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
 * 商品促销表 服务实现类
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionsServiceImpl extends ServiceImpl<PromotionsMapper, Promotions> implements IPromotionsService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBiz(PromotionsCreateReq createReq) {


        Promotions entity = new Promotions();
        BeanUtil.copyProperties(createReq, entity);

        boolean insertResult = entity.insert();

        if (!insertResult) {
            throw new ServiceException("数据插入失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBiz(PromotionsUpdateReq updateReq) {

        // 1.入参有效性判断
        if (Objects.isNull(updateReq.getId())) {
            throw new ServiceException("id不能为空");
        }

        Promotions entity = getById(updateReq.getId());
        if (Objects.isNull(entity)) {
            throw new ServiceException("商品促销表不存在");
        }

        // 2.重复性判断
        Promotions entityToUpdate = new Promotions();
        BeanUtil.copyProperties(updateReq, entityToUpdate);

        boolean updateResult = entityToUpdate.updateById();
        if (!updateResult) {
            throw new ServiceException("数据更新失败");
        }
    }

    @Override
    public PromotionsVO getByIdBiz(Long id) {
        // 1.入参有效性判断
        if (Objects.isNull(id)) {
            throw new ServiceException("id不能为空");
        }

        // 2.存在性判断
        Promotions entity = getById(id);
        if (Objects.isNull(entity)) {
            throw new ServiceException("商品促销表不存在");
        }

        PromotionsVO vo = new PromotionsVO();
        BeanUtil.copyProperties(entity, vo);

            return vo;
    }

    @Override
    public Page<PromotionsVO> pageBiz(PromotionsQueryReq queryReq) {
        Page<Promotions> page = new Page<>(queryReq.getCurrent(), queryReq.getSize());
        page.addOrder(OrderItem.desc("id"));

        LambdaQueryWrapper<Promotions> queryWrapper = getLambdaQueryWrapper(queryReq);

        page(page, queryWrapper);

        Page<PromotionsVO> pageVOResult = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<Promotions> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageVOResult;
        }

        List<PromotionsVO> vos = records.stream().map(record -> {
            PromotionsVO vo = new PromotionsVO();
            BeanUtil.copyProperties(record, vo);
    
            return vo;
        }).collect(Collectors.toList());

        pageVOResult.setRecords(vos);
        return pageVOResult;
    }

    @Override
    public Page<Promotions> page(PromotionsQueryReq queryReq) {
        Page<Promotions> page = new Page<>(queryReq.getCurrent(), queryReq.getSize());
        LambdaQueryWrapper<Promotions> queryWrapper = getLambdaQueryWrapper(queryReq);
        page(page, queryWrapper);
        return page;
    }

    @Override
    public List<Promotions> listByQueryReq(PromotionsQueryReq queryReq) {
        LambdaQueryWrapper<Promotions> queryWrapper = getLambdaQueryWrapper(queryReq);
        List<Promotions> listByQueryReq = list(queryWrapper);
        return listByQueryReq;
    }

    @Override
    public Map<Long, Promotions> getIdEntityMap(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new HashMap<>();
        }

        LambdaQueryWrapper<Promotions> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Promotions::getId, ids);
        List<Promotions> entities = list(queryWrapper);
        if (CollectionUtils.isEmpty(entities)) {
            return new HashMap<>();
        }

        return entities.stream().collect(Collectors.toMap(Promotions::getId, Function.identity(), (v1, v2) -> v1));
    }

    @Override
    public Promotions getOneByQueryReq(PromotionsQueryReq queryReq) {
        LambdaQueryWrapper<Promotions> queryWrapper = getLambdaQueryWrapper(queryReq);
        queryWrapper.last("LIMIT 1");

        List<Promotions> listByQueryReq = list(queryWrapper);
        if (CollectionUtils.isEmpty(listByQueryReq)) {
            return null;
        }

        return listByQueryReq.get(0);
    }

    private LambdaQueryWrapper<Promotions> getLambdaQueryWrapper(PromotionsQueryReq queryReq) {
        LambdaQueryWrapper<Promotions> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(Objects.nonNull(queryReq.getId()), Promotions::getId, queryReq.getId());
        queryWrapper.eq(Objects.nonNull(queryReq.getProductId()), Promotions::getProductId, queryReq.getProductId());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getPromotionType()), Promotions::getPromotionType, queryReq.getPromotionType());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getPromotionTypeLike()), Promotions::getPromotionType, queryReq.getPromotionTypeLike());
        queryWrapper.eq(Objects.nonNull(queryReq.getDiscount()), Promotions::getDiscount, queryReq.getDiscount());
        queryWrapper.eq(Objects.nonNull(queryReq.getStartTime()), Promotions::getStartTime, queryReq.getStartTime());
        queryWrapper.eq(Objects.nonNull(queryReq.getEndTime()), Promotions::getEndTime, queryReq.getEndTime());
        queryWrapper.eq(Objects.nonNull(queryReq.getCreatedAt()), Promotions::getCreatedAt, queryReq.getCreatedAt());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getDescription()), Promotions::getDescription, queryReq.getDescription());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getDescriptionLike()), Promotions::getDescription, queryReq.getDescriptionLike());
        queryWrapper.eq(Objects.nonNull(queryReq.getValidFlag()), Promotions::getValidFlag, queryReq.getValidFlag());

        return queryWrapper;
    }

}

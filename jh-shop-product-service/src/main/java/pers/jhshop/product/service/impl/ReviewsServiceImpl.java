package pers.jhshop.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.product.model.req.ReviewsCreateReq;
import pers.jhshop.product.model.req.ReviewsQueryReq;
import pers.jhshop.product.model.req.ReviewsUpdateReq;
import pers.jhshop.product.model.vo.ReviewsVO;
import pers.jhshop.product.model.entity.Reviews;
import pers.jhshop.product.mapper.ReviewsMapper;
import pers.jhshop.product.service.IReviewsService;
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
 * 商品评价表 服务实现类
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewsServiceImpl extends ServiceImpl<ReviewsMapper, Reviews> implements IReviewsService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBiz(ReviewsCreateReq createReq) {


        Reviews entity = new Reviews();
        BeanUtil.copyProperties(createReq, entity);

        boolean insertResult = entity.insert();

        if (!insertResult) {
            throw new ServiceException("数据插入失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBiz(ReviewsUpdateReq updateReq) {

        // 1.入参有效性判断
        if (Objects.isNull(updateReq.getId())) {
            throw new ServiceException("id不能为空");
        }

        Reviews entity = getById(updateReq.getId());
        if (Objects.isNull(entity)) {
            throw new ServiceException("商品评价表不存在");
        }

        // 2.重复性判断
        Reviews entityToUpdate = new Reviews();
        BeanUtil.copyProperties(updateReq, entityToUpdate);

        boolean updateResult = entityToUpdate.updateById();
        if (!updateResult) {
            throw new ServiceException("数据更新失败");
        }
    }

    @Override
    public ReviewsVO getByIdBiz(Long id) {
        // 1.入参有效性判断
        if (Objects.isNull(id)) {
            throw new ServiceException("id不能为空");
        }

        // 2.存在性判断
        Reviews entity = getById(id);
        if (Objects.isNull(entity)) {
            throw new ServiceException("商品评价表不存在");
        }

        ReviewsVO vo = new ReviewsVO();
        BeanUtil.copyProperties(entity, vo);

            return vo;
    }

    @Override
    public Page<ReviewsVO> pageBiz(ReviewsQueryReq queryReq) {
        Page<Reviews> page = new Page<>(queryReq.getCurrent(), queryReq.getSize());
        page.addOrder(OrderItem.desc("id"));

        LambdaQueryWrapper<Reviews> queryWrapper = getLambdaQueryWrapper(queryReq);

        page(page, queryWrapper);

        Page<ReviewsVO> pageVOResult = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<Reviews> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageVOResult;
        }

        List<ReviewsVO> vos = records.stream().map(record -> {
            ReviewsVO vo = new ReviewsVO();
            BeanUtil.copyProperties(record, vo);
    
            return vo;
        }).collect(Collectors.toList());

        pageVOResult.setRecords(vos);
        return pageVOResult;
    }

    @Override
    public Page<Reviews> page(ReviewsQueryReq queryReq) {
        Page<Reviews> page = new Page<>(queryReq.getCurrent(), queryReq.getSize());
        LambdaQueryWrapper<Reviews> queryWrapper = getLambdaQueryWrapper(queryReq);
        page(page, queryWrapper);
        return page;
    }

    @Override
    public List<Reviews> listByQueryReq(ReviewsQueryReq queryReq) {
        LambdaQueryWrapper<Reviews> queryWrapper = getLambdaQueryWrapper(queryReq);
        List<Reviews> listByQueryReq = list(queryWrapper);
        return listByQueryReq;
    }

    @Override
    public Map<Long, Reviews> getIdEntityMap(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new HashMap<>();
        }

        LambdaQueryWrapper<Reviews> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Reviews::getId, ids);
        List<Reviews> entities = list(queryWrapper);
        if (CollectionUtils.isEmpty(entities)) {
            return new HashMap<>();
        }

        return entities.stream().collect(Collectors.toMap(Reviews::getId, Function.identity(), (v1, v2) -> v1));
    }

    @Override
    public Reviews getOneByQueryReq(ReviewsQueryReq queryReq) {
        LambdaQueryWrapper<Reviews> queryWrapper = getLambdaQueryWrapper(queryReq);
        queryWrapper.last("LIMIT 1");

        List<Reviews> listByQueryReq = list(queryWrapper);
        if (CollectionUtils.isEmpty(listByQueryReq)) {
            return null;
        }

        return listByQueryReq.get(0);
    }

    private LambdaQueryWrapper<Reviews> getLambdaQueryWrapper(ReviewsQueryReq queryReq) {
        LambdaQueryWrapper<Reviews> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(Objects.nonNull(queryReq.getId()), Reviews::getId, queryReq.getId());
        queryWrapper.eq(Objects.nonNull(queryReq.getProductId()), Reviews::getProductId, queryReq.getProductId());
        queryWrapper.eq(Objects.nonNull(queryReq.getUserId()), Reviews::getUserId, queryReq.getUserId());
        queryWrapper.eq(Objects.nonNull(queryReq.getRating()), Reviews::getRating, queryReq.getRating());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getComment()), Reviews::getComment, queryReq.getComment());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getCommentLike()), Reviews::getComment, queryReq.getCommentLike());
        queryWrapper.eq(Objects.nonNull(queryReq.getCreatedAt()), Reviews::getCreatedAt, queryReq.getCreatedAt());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getDescription()), Reviews::getDescription, queryReq.getDescription());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getDescriptionLike()), Reviews::getDescription, queryReq.getDescriptionLike());
        queryWrapper.eq(Objects.nonNull(queryReq.getValidFlag()), Reviews::getValidFlag, queryReq.getValidFlag());

        return queryWrapper;
    }

}

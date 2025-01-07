package pers.jhshop.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.jhshop.common.exception.ServiceException;
import pers.jhshop.product.mapper.CategoriesMapper;
import pers.jhshop.product.model.entity.Categories;
import pers.jhshop.product.model.req.CategoriesCreateReq;
import pers.jhshop.product.model.req.CategoriesQueryReq;
import pers.jhshop.product.model.req.CategoriesUpdateReq;
import pers.jhshop.product.model.vo.CategoriesVO;
import pers.jhshop.product.service.ICategoriesService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品分类表 服务实现类
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl extends ServiceImpl<CategoriesMapper, Categories> implements ICategoriesService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBiz(CategoriesCreateReq createReq) {
        if (Objects.isNull(createReq.getName())) {
            throw new ServiceException("分类名称(如“手机”，“家电”，“衣物”等)不能为空");
        }



        Categories entity = new Categories();
        BeanUtil.copyProperties(createReq, entity);

        boolean insertResult = entity.insert();

        if (!insertResult) {
            throw new ServiceException("数据插入失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBiz(CategoriesUpdateReq updateReq) {

        // 1.入参有效性判断
        if (Objects.isNull(updateReq.getId())) {
            throw new ServiceException("id不能为空");
        }

        Categories entity = getById(updateReq.getId());
        if (Objects.isNull(entity)) {
            throw new ServiceException("商品分类表不存在");
        }

        // 2.重复性判断
        Categories entityToUpdate = new Categories();
        BeanUtil.copyProperties(updateReq, entityToUpdate);

        boolean updateResult = entityToUpdate.updateById();
        if (!updateResult) {
            throw new ServiceException("数据更新失败");
        }
    }

    @Override
    public CategoriesVO getByIdBiz(Long id) {
        // 1.入参有效性判断
        if (Objects.isNull(id)) {
            throw new ServiceException("id不能为空");
        }

        // 2.存在性判断
        Categories entity = getById(id);
        if (Objects.isNull(entity)) {
            throw new ServiceException("商品分类表不存在");
        }

        CategoriesVO vo = new CategoriesVO();
        BeanUtil.copyProperties(entity, vo);

            return vo;
    }

    @Override
    public Page<CategoriesVO> pageBiz(CategoriesQueryReq queryReq) {
        Page<Categories> page = new Page<>(queryReq.getCurrent(), queryReq.getSize());
        page.addOrder(OrderItem.desc("id"));

        LambdaQueryWrapper<Categories> queryWrapper = getLambdaQueryWrapper(queryReq);

        page(page, queryWrapper);

        Page<CategoriesVO> pageVOResult = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<Categories> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageVOResult;
        }

        List<CategoriesVO> vos = records.stream().map(record -> {
            CategoriesVO vo = new CategoriesVO();
            BeanUtil.copyProperties(record, vo);
    
            return vo;
        }).collect(Collectors.toList());

        pageVOResult.setRecords(vos);
        return pageVOResult;
    }

    @Override
    public Page<Categories> page(CategoriesQueryReq queryReq) {
        Page<Categories> page = new Page<>(queryReq.getCurrent(), queryReq.getSize());
        LambdaQueryWrapper<Categories> queryWrapper = getLambdaQueryWrapper(queryReq);
        page(page, queryWrapper);
        return page;
    }

    @Override
    public List<Categories> listByQueryReq(CategoriesQueryReq queryReq) {
        LambdaQueryWrapper<Categories> queryWrapper = getLambdaQueryWrapper(queryReq);
        List<Categories> listByQueryReq = list(queryWrapper);
        return listByQueryReq;
    }

    @Override
    public Map<Long, Categories> getIdEntityMap(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new HashMap<>();
        }

        LambdaQueryWrapper<Categories> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Categories::getId, ids);
        List<Categories> entities = list(queryWrapper);
        if (CollectionUtils.isEmpty(entities)) {
            return new HashMap<>();
        }

        return entities.stream().collect(Collectors.toMap(Categories::getId, Function.identity(), (v1, v2) -> v1));
    }

    @Override
    public Categories getOneByQueryReq(CategoriesQueryReq queryReq) {
        LambdaQueryWrapper<Categories> queryWrapper = getLambdaQueryWrapper(queryReq);
        queryWrapper.last("LIMIT 1");

        List<Categories> listByQueryReq = list(queryWrapper);
        if (CollectionUtils.isEmpty(listByQueryReq)) {
            return null;
        }

        return listByQueryReq.get(0);
    }

    private LambdaQueryWrapper<Categories> getLambdaQueryWrapper(CategoriesQueryReq queryReq) {
        LambdaQueryWrapper<Categories> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(Objects.nonNull(queryReq.getId()), Categories::getId, queryReq.getId());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryReq.getIdList()), Categories::getId, queryReq.getIdList());

        queryWrapper.eq(Objects.nonNull(queryReq.getParentId()), Categories::getParentId, queryReq.getParentId());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getName()), Categories::getName, queryReq.getName());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getNameLike()), Categories::getName, queryReq.getNameLike());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getProductCategoriesDescription()), Categories::getProductCategoriesDescription, queryReq.getProductCategoriesDescription());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getProductCategoriesDescriptionLike()), Categories::getProductCategoriesDescription, queryReq.getProductCategoriesDescriptionLike());
        queryWrapper.eq(Objects.nonNull(queryReq.getCreatedAt()), Categories::getCreatedAt, queryReq.getCreatedAt());
        queryWrapper.eq(Objects.nonNull(queryReq.getUpdatedAt()), Categories::getUpdatedAt, queryReq.getUpdatedAt());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getDescription()), Categories::getDescription, queryReq.getDescription());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getDescriptionLike()), Categories::getDescription, queryReq.getDescriptionLike());
        queryWrapper.eq(Objects.nonNull(queryReq.getValidFlag()), Categories::getValidFlag, queryReq.getValidFlag());

        return queryWrapper;
    }

}

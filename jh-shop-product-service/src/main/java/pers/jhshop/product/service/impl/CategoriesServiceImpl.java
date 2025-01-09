package pers.jhshop.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.jhshop.common.exception.ServiceException;
import pers.jhshop.product.consts.RedisKeyConstants;
import pers.jhshop.product.enums.CategoryParentIdLevelEnum;
import pers.jhshop.product.mapper.CategoriesMapper;
import pers.jhshop.product.model.entity.Categories;
import pers.jhshop.product.model.req.CategoriesCreateReq;
import pers.jhshop.product.model.req.CategoriesQueryReq;
import pers.jhshop.product.model.req.CategoriesUpdateReq;
import pers.jhshop.product.model.vo.AllLabelIdAndNameAndSubVO;
import pers.jhshop.product.model.vo.CategoriesVO;
import pers.jhshop.product.service.ICategoriesService;

import java.util.*;
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

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBiz(CategoriesCreateReq createReq) {
        if (Objects.isNull(createReq.getParentId())){
            throw new ServiceException("分类父ID不能为空");
        }

        if (StringUtils.isBlank(createReq.getName())) {
            throw new ServiceException("分类名称不能为空");
        }

        if (StringUtils.isBlank(createReq.getDescription())) {
            throw new ServiceException("分类描述不能为空");
        }

        // 重复性判断 名称不能重复
        CategoriesQueryReq categoriesQueryReq = new CategoriesQueryReq();
        categoriesQueryReq.setName(createReq.getName());
        Categories oneByQueryReq = getOneByQueryReq(categoriesQueryReq);
        if (Objects.nonNull(oneByQueryReq)){
            throw new ServiceException("分类名称不能重复");
        }

        Categories entity = new Categories();
        BeanUtil.copyProperties(createReq, entity);

        boolean insertResult = entity.insert();

        if (!insertResult) {
            throw new ServiceException("数据插入失败");
        }

        // 更新Redis（重新获取一遍所有标签）
        updateRedisAllProductCategories();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBiz(CategoriesUpdateReq updateReq) {
        // 1.入参有效性判断
        if (Objects.isNull(updateReq.getId())) {
            throw new ServiceException("id不能为空");
        }

        if (Objects.isNull(updateReq.getParentId())){
            throw new ServiceException("分类父ID不能为空");
        }

        if (StringUtils.isBlank(updateReq.getName())) {
            throw new ServiceException("分类名称不能为空");
        }

        if (StringUtils.isBlank(updateReq.getDescription())) {
            throw new ServiceException("分类描述不能为空");
        }

        Categories entity = getById(updateReq.getId());
        if (Objects.isNull(entity)) {
            throw new ServiceException("商品分类表不存在");
        }

        // 2.重复性判断 名称不能重复
        CategoriesQueryReq categoriesQueryReq = new CategoriesQueryReq();
        categoriesQueryReq.setName(updateReq.getName());
        Categories oneByQueryReq = getOneByQueryReq(categoriesQueryReq);
        if (Objects.nonNull(oneByQueryReq) && !Objects.equals(oneByQueryReq.getId(), updateReq.getId())) {
            throw new ServiceException("分类名称不能重复");
        }

        Categories entityToUpdate = new Categories();
        BeanUtil.copyProperties(updateReq, entityToUpdate);

        boolean updateResult = entityToUpdate.updateById();
        if (!updateResult) {
            throw new ServiceException("数据更新失败");
        }

        // 更新Redis（重新获取一遍所有标签）
        updateRedisAllProductCategories();
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

    @Override
    public AllLabelIdAndNameAndSubVO getAllProductCategories() {
        return getAllProductCategories(true);
    }

    /**
     * 获取所有标签（是否查询缓存）
     * @param queryRedisFlag
     * @return true：查询缓存，false：跳过缓存，直接查询数据库
     */
    private AllLabelIdAndNameAndSubVO getAllProductCategories(Boolean queryRedisFlag) {
        // 优先查询Redis
        if (BooleanUtils.isTrue(queryRedisFlag)){
            String allProductCategories = stringRedisTemplate.opsForValue().get(RedisKeyConstants.ALL_PRODUCT_CATEGORY_REDIS_KEY);
            if (StringUtils.isNotBlank(allProductCategories)){
                return JSON.parseObject(allProductCategories, AllLabelIdAndNameAndSubVO.class);
            }
        }

        AllLabelIdAndNameAndSubVO allLabelIdAndNameAndSubVO = new AllLabelIdAndNameAndSubVO();
        // 查询出所有标签
        List<Categories> allCategoryList = list();
        if (CollectionUtils.isEmpty(allCategoryList)){
            return allLabelIdAndNameAndSubVO;
        }

        // 根据parent_id对商品类型进行分组
        Map<Long, List<Categories>> parentIdAndCategoryMap = allCategoryList.stream()
                .collect(Collectors.groupingBy(Categories::getParentId));

        // 梳理标签层级关系（递归填充获取分类）
        List<AllLabelIdAndNameAndSubVO.LabelIdAndNameAndSub> allRootCategoryList = buildProductCategoryTree(CategoryParentIdLevelEnum.ROOT_CATEGORY.getValue(), parentIdAndCategoryMap);
        allLabelIdAndNameAndSubVO.setAllCategoriesInfo(allRootCategoryList);

        // 更新缓存
        updateRedisAllProductCategories(allLabelIdAndNameAndSubVO);

        return allLabelIdAndNameAndSubVO;
    }

    /**
     * 更新redis中的商品分类
     */
    private void updateRedisAllProductCategories() {
        updateRedisAllProductCategories(getAllProductCategories(false));
    }

    /**
     * 更新redis中的商品分类
     */
    private void updateRedisAllProductCategories(AllLabelIdAndNameAndSubVO allLabelIdAndNameAndSubVO) {
        stringRedisTemplate.opsForValue()
                .set(RedisKeyConstants.ALL_PRODUCT_CATEGORY_REDIS_KEY, JSONObject.toJSONString(allLabelIdAndNameAndSubVO));
    }

    /**
     * 梳理标签层级关系（递归填充获取分类）
     */
    private List<AllLabelIdAndNameAndSubVO.LabelIdAndNameAndSub> buildProductCategoryTree(Long parentId, Map<Long, List<Categories>> parentIdAndCategoryMap) {
        List<AllLabelIdAndNameAndSubVO.LabelIdAndNameAndSub> result = new ArrayList<>();

        // 根据父ID从map中获取当前层级的子标签
        List<Categories> childrenCategories = parentIdAndCategoryMap.get(parentId);
        if (CollectionUtils.isEmpty(childrenCategories)){
            // 已经没有子标签了，结束递归
            return result;
        }

        // 遍历子标签集合
        childrenCategories.forEach(c ->{
            AllLabelIdAndNameAndSubVO.LabelIdAndNameAndSub labelIdAndNameAndSub = new AllLabelIdAndNameAndSubVO.LabelIdAndNameAndSub();
            labelIdAndNameAndSub.setLabelId(c.getId());
            labelIdAndNameAndSub.setLabelName(c.getName());
            // 继续查找子标签
            labelIdAndNameAndSub.setSubList(buildProductCategoryTree(c.getId(), parentIdAndCategoryMap));

            result.add(labelIdAndNameAndSub);
        });

        return result;
    }

    private LambdaQueryWrapper<Categories> getLambdaQueryWrapper(CategoriesQueryReq queryReq) {
        LambdaQueryWrapper<Categories> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(Objects.nonNull(queryReq.getId()), Categories::getId, queryReq.getId());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryReq.getIdList()), Categories::getId, queryReq.getIdList());

        queryWrapper.eq(Objects.nonNull(queryReq.getParentId()), Categories::getParentId, queryReq.getParentId());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getName()), Categories::getName, queryReq.getName());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getNameLike()), Categories::getName, queryReq.getNameLike());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getDescription()), Categories::getDescription, queryReq.getDescription());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getDescriptionLike()), Categories::getDescription, queryReq.getDescriptionLike());
        queryWrapper.eq(Objects.nonNull(queryReq.getValidFlag()), Categories::getValidFlag, queryReq.getValidFlag());

        return queryWrapper;
    }

}

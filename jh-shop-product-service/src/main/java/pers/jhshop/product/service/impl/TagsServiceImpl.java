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
import pers.jhshop.product.mapper.TagsMapper;
import pers.jhshop.product.model.entity.Tags;
import pers.jhshop.product.model.req.TagsCreateReq;
import pers.jhshop.product.model.req.TagsQueryReq;
import pers.jhshop.product.model.req.TagsUpdateReq;
import pers.jhshop.product.model.vo.AllLabelIdAndNameAndSubVO;
import pers.jhshop.product.model.vo.TagsVO;
import pers.jhshop.product.service.ITagsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品标签表 服务实现类
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements ITagsService {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBiz(TagsCreateReq createReq) {
        if (StringUtils.isBlank(createReq.getTagName())){
            throw new ServiceException("标签名称不能为空");
        }

        if (StringUtils.isBlank(createReq.getDescription())){
            throw new ServiceException("标签描述不能为空");
        }

        // 重复性校验 名称重复校验
        TagsQueryReq tagsQueryReq = new TagsQueryReq();
        tagsQueryReq.setTagName(createReq.getTagName());
        Tags oneByQueryReq = getOneByQueryReq(tagsQueryReq);
        if (Objects.nonNull(oneByQueryReq)){
            throw new ServiceException("标签名称不能重复");
        }

        Tags entity = new Tags();
        BeanUtil.copyProperties(createReq, entity);

        boolean insertResult = entity.insert();

        if (!insertResult) {
            throw new ServiceException("数据插入失败");
        }

        // 更新缓存
        updateRedisAllProductTags();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBiz(TagsUpdateReq updateReq) {

        // 1.入参有效性判断
        if (Objects.isNull(updateReq.getId())) {
            throw new ServiceException("id不能为空");
        }

        if (StringUtils.isBlank(updateReq.getTagName())) {
            throw new ServiceException("标签名称不能为空");
        }

        if (StringUtils.isBlank(updateReq.getDescription())) {
            throw new ServiceException("标签描述不能为空");
        }

        Tags entity = getById(updateReq.getId());
        if (Objects.isNull(entity)) {
            throw new ServiceException("商品标签表不存在");
        }

        // 2.重复性判断 名称重复校验
        TagsQueryReq tagsQueryReq = new TagsQueryReq();
        tagsQueryReq.setTagName(updateReq.getTagName());
        Tags oneByQueryReq = getOneByQueryReq(tagsQueryReq);
        if (Objects.nonNull(oneByQueryReq) && !Objects.equals(oneByQueryReq.getId(), updateReq.getId())) {
            throw new ServiceException("标签名称不能重复");
        }

        Tags entityToUpdate = new Tags();
        BeanUtil.copyProperties(updateReq, entityToUpdate);

        boolean updateResult = entityToUpdate.updateById();
        if (!updateResult) {
            throw new ServiceException("数据更新失败");
        }

        // 更新缓存
        updateRedisAllProductTags();
    }

    @Override
    public TagsVO getByIdBiz(Long id) {
        // 1.入参有效性判断
        if (Objects.isNull(id)) {
            throw new ServiceException("id不能为空");
        }

        // 2.存在性判断
        Tags entity = getById(id);
        if (Objects.isNull(entity)) {
            throw new ServiceException("商品标签表不存在");
        }

        TagsVO vo = new TagsVO();
        BeanUtil.copyProperties(entity, vo);

            return vo;
    }

    @Override
    public Page<TagsVO> pageBiz(TagsQueryReq queryReq) {
        Page<Tags> page = new Page<>(queryReq.getCurrent(), queryReq.getSize());
        page.addOrder(OrderItem.desc("id"));

        LambdaQueryWrapper<Tags> queryWrapper = getLambdaQueryWrapper(queryReq);

        page(page, queryWrapper);

        Page<TagsVO> pageVOResult = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<Tags> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageVOResult;
        }

        List<TagsVO> vos = records.stream().map(record -> {
            TagsVO vo = new TagsVO();
            BeanUtil.copyProperties(record, vo);
    
            return vo;
        }).collect(Collectors.toList());

        pageVOResult.setRecords(vos);
        return pageVOResult;
    }

    @Override
    public Page<Tags> page(TagsQueryReq queryReq) {
        Page<Tags> page = new Page<>(queryReq.getCurrent(), queryReq.getSize());
        LambdaQueryWrapper<Tags> queryWrapper = getLambdaQueryWrapper(queryReq);
        page(page, queryWrapper);
        return page;
    }

    @Override
    public List<Tags> listByQueryReq(TagsQueryReq queryReq) {
        LambdaQueryWrapper<Tags> queryWrapper = getLambdaQueryWrapper(queryReq);
        List<Tags> listByQueryReq = list(queryWrapper);
        return listByQueryReq;
    }

    @Override
    public Map<Long, Tags> getIdEntityMap(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new HashMap<>();
        }

        LambdaQueryWrapper<Tags> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Tags::getId, ids);
        List<Tags> entities = list(queryWrapper);
        if (CollectionUtils.isEmpty(entities)) {
            return new HashMap<>();
        }

        return entities.stream().collect(Collectors.toMap(Tags::getId, Function.identity(), (v1, v2) -> v1));
    }

    @Override
    public Tags getOneByQueryReq(TagsQueryReq queryReq) {
        LambdaQueryWrapper<Tags> queryWrapper = getLambdaQueryWrapper(queryReq);
        queryWrapper.last("LIMIT 1");

        List<Tags> listByQueryReq = list(queryWrapper);
        if (CollectionUtils.isEmpty(listByQueryReq)) {
            return null;
        }

        return listByQueryReq.get(0);
    }

    @Override
    public AllLabelIdAndNameAndSubVO getAllProductTags() {
        return getAllProductTags(true);
    }

    /**
     * 获取所有商品标签（是否查询缓存）
     * @param queryRedisFlag
     * @return true：查询缓存，false：跳过缓存，直接查询数据库
     */
    private AllLabelIdAndNameAndSubVO getAllProductTags(Boolean queryRedisFlag) {
        // 优先查询缓存
        if (BooleanUtils.isTrue(queryRedisFlag)){
            String AllProductTagsStr = stringRedisTemplate.opsForValue().get(RedisKeyConstants.ALL_PRODUCT_TAG_REDIS_KEY);
            if (StringUtils.isBlank(AllProductTagsStr)){
                return JSON.parseObject(AllProductTagsStr, AllLabelIdAndNameAndSubVO.class);
            }
        }

        AllLabelIdAndNameAndSubVO allLabelIdAndNameAndSubVO = new AllLabelIdAndNameAndSubVO();
        // 查询所有标签
        List<Tags> allProductTags = list();
        if (CollectionUtils.isEmpty(allProductTags)){
            return allLabelIdAndNameAndSubVO;
        }

        // 转换为VO
        List<AllLabelIdAndNameAndSubVO.LabelIdAndNameAndSub> allLabelInfoList = allProductTags.stream()
                .map(tag ->{
                    AllLabelIdAndNameAndSubVO.LabelIdAndNameAndSub labelIdAndNameAndSub = new AllLabelIdAndNameAndSubVO.LabelIdAndNameAndSub();
                    labelIdAndNameAndSub.setLabelId(tag.getId());
                    labelIdAndNameAndSub.setLabelName(tag.getTagName());
                    return labelIdAndNameAndSub;
                })
                .collect(Collectors.toList());
        allLabelIdAndNameAndSubVO.setAllCategoriesInfo(allLabelInfoList);

        // 更新缓存
        updateRedisAllProductTags(allLabelIdAndNameAndSubVO);

        return allLabelIdAndNameAndSubVO;
    }

    /**
     * 将所有商品标签存入缓存
     */
    private void updateRedisAllProductTags() {
        updateRedisAllProductTags(getAllProductTags(false));
    }

    /**
     * 将所有商品标签存入缓存
     */
    private void updateRedisAllProductTags(AllLabelIdAndNameAndSubVO allLabelIdAndNameAndSubVO) {
        stringRedisTemplate.opsForValue().set(RedisKeyConstants.ALL_PRODUCT_TAG_REDIS_KEY, JSONObject.toJSONString(allLabelIdAndNameAndSubVO));
    }

    private LambdaQueryWrapper<Tags> getLambdaQueryWrapper(TagsQueryReq queryReq) {
        LambdaQueryWrapper<Tags> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(Objects.nonNull(queryReq.getId()), Tags::getId, queryReq.getId());
        queryWrapper.in(CollectionUtils.isNotEmpty(queryReq.getIdList()), Tags::getId, queryReq.getIdList());

        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getTagName()), Tags::getTagName, queryReq.getTagName());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getTagNameLike()), Tags::getTagName, queryReq.getTagNameLike());
        queryWrapper.eq(StringUtils.isNotBlank(queryReq.getDescription()), Tags::getDescription, queryReq.getDescription());
        queryWrapper.like(StringUtils.isNotBlank(queryReq.getDescriptionLike()), Tags::getDescription, queryReq.getDescriptionLike());
        queryWrapper.eq(Objects.nonNull(queryReq.getValidFlag()), Tags::getValidFlag, queryReq.getValidFlag());

        return queryWrapper;
    }

}

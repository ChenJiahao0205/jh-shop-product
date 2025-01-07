package pers.jhshop.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.jhshop.common.exception.ServiceException;
import pers.jhshop.common.utils.JhShopFieldTypeConvertUtil;
import pers.jhshop.fapi.es.dto.req.EsProductsBatchCreateOrUpdateReq;
import pers.jhshop.fapi.es.service.EsProductsApiService;
import pers.jhshop.product.mapper.ProductsMapper;
import pers.jhshop.product.model.entity.Categories;
import pers.jhshop.product.model.entity.Products;
import pers.jhshop.product.model.entity.Tags;
import pers.jhshop.product.model.req.*;
import pers.jhshop.product.model.vo.ProductsVO;
import pers.jhshop.product.service.ICategoriesService;
import pers.jhshop.product.service.IProductsService;
import pers.jhshop.product.service.ITagsService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    private final EsProductsApiService esProductsApiService;

    private final ICategoriesService categoriesService;

    private final ITagsService tagsService;

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

    @Transactional
    @Override
    public void combineCreateProductInfo(CombineCreateProductInfoReq combineCreateReq) {
        // 1.校验入参
        checkProductInfoParams(combineCreateReq);

        // 2.插入商品数据
        Long productId = insertProduct(combineCreateReq);

        // 3.数据同步到ES
        syncProductInfoToEs(productId, combineCreateReq);
    }

    /**
     * 同步商品数据到ES
     */
    private void syncProductInfoToEs(Long productId, CombineCreateProductInfoReq combineCreateReq) {
        List<EsProductsBatchCreateOrUpdateReq> createEsProductReqList = new ArrayList<>();
        EsProductsBatchCreateOrUpdateReq createEsProductReq = new EsProductsBatchCreateOrUpdateReq();
        createEsProductReq.setId(productId);
        createEsProductReq.setName(combineCreateReq.getProductName());
        createEsProductReq.setProductDescription(combineCreateReq.getProductDescription());
        createEsProductReq.setBrand(combineCreateReq.getProductBrand());

        List<Long> productCategoryIdList = combineCreateReq.getProductCategoryIdList();
        List<Long> productTagIdList = combineCreateReq.getProductTagIdList();

        createEsProductReq.setProductCategoryIdArr(JhShopFieldTypeConvertUtil.listConvertStr(productCategoryIdList));
        createEsProductReq.setProductTagIdArr(JhShopFieldTypeConvertUtil.listConvertStr(productTagIdList));

        // 设置分类名称
        CategoriesQueryReq categoriesQueryReq = new CategoriesQueryReq();
        categoriesQueryReq.setIdList(productCategoryIdList);
        List<Categories> categories = categoriesService.listByQueryReq(categoriesQueryReq);
        if (CollectionUtils.isNotEmpty(categories)){
            List<String> productCategoryNameArr = categories.stream().map(Categories::getName).collect(Collectors.toList());
            createEsProductReq.setProductCategoryNameArr(JhShopFieldTypeConvertUtil.listConvertStr(productCategoryNameArr));
        }

        // 设置标签名称
        TagsQueryReq tagsQueryReq = new TagsQueryReq();
        tagsQueryReq.setIdList(productTagIdList);
        List<Tags> tags = tagsService.listByQueryReq(tagsQueryReq);
        if (CollectionUtils.isNotEmpty(tags)){
            List<String> productTagIdNameArr = tags.stream().map(Tags::getTagName).collect(Collectors.toList());
            createEsProductReq.setProductTagIdNameArr(JhShopFieldTypeConvertUtil.listConvertStr(productTagIdNameArr));
        }

        createEsProductReq.setPrice(String.valueOf(combineCreateReq.getProductPrice()));
        createEsProductReq.setStock(combineCreateReq.getProductStock());
        createEsProductReq.setStatus(true);

        DateTime now = DateUtil.date();
        createEsProductReq.setCreatedAt(now);
        createEsProductReq.setUpdatedAt(now);
        createEsProductReq.setImageUrl(combineCreateReq.getProductImageUrl());
        createEsProductReq.setDescription(combineCreateReq.getProductDescription());
        createEsProductReq.setValidFlag(true);
        createEsProductReq.setCreateTime(now);
        createEsProductReq.setUpdateTime(now);


        createEsProductReqList.add(createEsProductReq);
        esProductsApiService.batchCreateOrUpdateReq(createEsProductReqList);
    }

    /**
     * 插入商品数据
     */
    private Long insertProduct(CombineCreateProductInfoReq combineCreateReq) {
        Products products = new Products();
        // 设置默认值
        products.setStatus(true);
        products.setValidFlag(true);

        LocalDateTime now = LocalDateTimeUtil.now();
        products.setCreatedAt(now);
        products.setUpdatedAt(now);
        products.setCreateTime(now);
        products.setUpdateTime(now);

        // 填充前端传入的数据
        products.setName(combineCreateReq.getProductName());
        products.setProductDescription(combineCreateReq.getProductDescription());
        products.setBrand(combineCreateReq.getProductBrand());
        products.setPrice(combineCreateReq.getProductPrice());
        products.setStock(combineCreateReq.getProductStock());
        products.setImageUrl(combineCreateReq.getProductImageUrl());
        products.setDescription(combineCreateReq.getProductDescription());

        // 额外需要处理的字段
        products.setProductCategoryIdArr(JhShopFieldTypeConvertUtil.listConvertStr(combineCreateReq.getProductCategoryIdList()));
        products.setProductTagIdArr(JhShopFieldTypeConvertUtil.listConvertStr(combineCreateReq.getProductTagIdList()));

        boolean save = save(products);
        if (BooleanUtils.isNotTrue(save)){
            throw new ServiceException("商品创建失败");
        }

        return products.getId();
    }

    /**
     * 校验商品信息相关参数
     */
    private void checkProductInfoParams(CombineCreateProductInfoReq combineCreateReq) {
        if (StringUtils.isBlank(combineCreateReq.getProductName())){
            throw new ServiceException("商品名称不能为空");
        }

        if (StringUtils.isBlank(combineCreateReq.getProductDescription())){
            throw new ServiceException("商品描述不能为空");
        }

        if (StringUtils.isBlank(combineCreateReq.getProductBrand())){
            throw new ServiceException("商品品牌不能为空");
        }

        if (CollectionUtils.isEmpty(combineCreateReq.getProductCategoryIdList())){
            throw new ServiceException("商品分类不能为空");
        }

        if (CollectionUtils.isEmpty(combineCreateReq.getProductTagIdList())){
            throw new ServiceException("商品标签不能为空");
        }

        if (Objects.isNull(combineCreateReq.getProductPrice())){
            throw new ServiceException("商品价格不能为空");
        }

        if (Objects.isNull(combineCreateReq.getProductStock())){
            throw new ServiceException("商品数量不能为空");
        }

        if (StringUtils.isBlank(combineCreateReq.getProductImageUrl())){
            throw new ServiceException("商品主图URL不能为空");
        }
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

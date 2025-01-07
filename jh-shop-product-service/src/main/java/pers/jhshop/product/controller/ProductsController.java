package pers.jhshop.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pers.jhshop.common.entity.ResultBo;
import pers.jhshop.product.consts.JhShopUserApiConstants;
import pers.jhshop.product.model.req.CombineCreateProductInfoReq;
import pers.jhshop.product.model.req.ProductsCreateReq;
import pers.jhshop.product.model.req.ProductsQueryReq;
import pers.jhshop.product.model.req.ProductsUpdateReq;
import pers.jhshop.product.model.vo.ProductsVO;
import pers.jhshop.product.service.IProductsService;
/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Slf4j
@RestController
@RequestMapping(JhShopUserApiConstants.API_USER + "products")
@RequiredArgsConstructor
public class ProductsController {
    private final IProductsService productsService;

    @PostMapping("create")
    public ResultBo create(@RequestBody ProductsCreateReq createReq) {
        productsService.createBiz(createReq);
        return ResultBo.success();
    }

    @PostMapping("update")
    public ResultBo update(@RequestBody ProductsUpdateReq updateReq) {
        productsService.updateBiz(updateReq);
        return ResultBo.success();
    }

    @GetMapping("getById")
    public ResultBo<ProductsVO> getById(Long id) {
        ProductsVO vo = productsService.getByIdBiz(id);
        return ResultBo.success(vo);
    }

    @PostMapping("page")
    public ResultBo<Page<ProductsVO>> page(@RequestBody ProductsQueryReq queryReq) {
        Page page = productsService.pageBiz(queryReq);
        return ResultBo.success(page);
    }

    @PostMapping("combineCreateProductInfo")
    public ResultBo combineCreateProductInfo(@RequestBody CombineCreateProductInfoReq combineCreateReq) {
        productsService.combineCreateProductInfo(combineCreateReq);
        return ResultBo.success();
    }
}


package pers.jhshop.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.common.entity.ResultBo;
import pers.jhshop.product.consts.JhShopUserApiConstants;
import pers.jhshop.product.model.req.CategoriesCreateReq;
import pers.jhshop.product.model.req.CategoriesQueryReq;
import pers.jhshop.product.model.req.CategoriesUpdateReq;
import pers.jhshop.product.model.vo.CategoriesVO;
import pers.jhshop.product.service.ICategoriesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
/**
 * <p>
 * 商品分类表 前端控制器
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Slf4j
@RestController
@RequestMapping(JhShopUserApiConstants.API_USER + "categories")
@RequiredArgsConstructor
public class CategoriesController {
    private final ICategoriesService categoriesService;

    @PostMapping("create")
    public ResultBo create(@RequestBody CategoriesCreateReq createReq) {
        categoriesService.createBiz(createReq);
        return ResultBo.success();
    }

    @PostMapping("update")
    public ResultBo update(@RequestBody CategoriesUpdateReq updateReq) {
        categoriesService.updateBiz(updateReq);
        return ResultBo.success();
    }

    @GetMapping("getById")
    public ResultBo<CategoriesVO> getById(Long id) {
        CategoriesVO vo = categoriesService.getByIdBiz(id);
        return ResultBo.success(vo);
    }

    @PostMapping("page")
    public ResultBo<Page<CategoriesVO>> page(@RequestBody CategoriesQueryReq queryReq) {
        Page page = categoriesService.pageBiz(queryReq);
        return ResultBo.success(page);
    }
}


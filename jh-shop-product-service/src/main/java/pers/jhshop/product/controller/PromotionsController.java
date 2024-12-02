package pers.jhshop.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.common.entity.ResultBo;
import pers.jhshop.product.consts.JhShopUserApiConstants;
import pers.jhshop.product.model.req.PromotionsCreateReq;
import pers.jhshop.product.model.req.PromotionsQueryReq;
import pers.jhshop.product.model.req.PromotionsUpdateReq;
import pers.jhshop.product.model.vo.PromotionsVO;
import pers.jhshop.product.service.IPromotionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
/**
 * <p>
 * 商品促销表 前端控制器
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Slf4j
@RestController
@RequestMapping(JhShopUserApiConstants.API_USER + "promotions")
@RequiredArgsConstructor
public class PromotionsController {
    private final IPromotionsService promotionsService;

    @PostMapping("create")
    public ResultBo create(@RequestBody PromotionsCreateReq createReq) {
        promotionsService.createBiz(createReq);
        return ResultBo.success();
    }

    @PostMapping("update")
    public ResultBo update(@RequestBody PromotionsUpdateReq updateReq) {
        promotionsService.updateBiz(updateReq);
        return ResultBo.success();
    }

    @GetMapping("getById")
    public ResultBo<PromotionsVO> getById(Long id) {
        PromotionsVO vo = promotionsService.getByIdBiz(id);
        return ResultBo.success(vo);
    }

    @PostMapping("page")
    public ResultBo<Page<PromotionsVO>> page(@RequestBody PromotionsQueryReq queryReq) {
        Page page = promotionsService.pageBiz(queryReq);
        return ResultBo.success(page);
    }
}


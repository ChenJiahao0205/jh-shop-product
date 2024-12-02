package pers.jhshop.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.common.entity.ResultBo;
import pers.jhshop.product.consts.JhShopUserApiConstants;
import pers.jhshop.product.model.req.ReviewsCreateReq;
import pers.jhshop.product.model.req.ReviewsQueryReq;
import pers.jhshop.product.model.req.ReviewsUpdateReq;
import pers.jhshop.product.model.vo.ReviewsVO;
import pers.jhshop.product.service.IReviewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
/**
 * <p>
 * 商品评价表 前端控制器
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Slf4j
@RestController
@RequestMapping(JhShopUserApiConstants.API_USER + "reviews")
@RequiredArgsConstructor
public class ReviewsController {
    private final IReviewsService reviewsService;

    @PostMapping("create")
    public ResultBo create(@RequestBody ReviewsCreateReq createReq) {
        reviewsService.createBiz(createReq);
        return ResultBo.success();
    }

    @PostMapping("update")
    public ResultBo update(@RequestBody ReviewsUpdateReq updateReq) {
        reviewsService.updateBiz(updateReq);
        return ResultBo.success();
    }

    @GetMapping("getById")
    public ResultBo<ReviewsVO> getById(Long id) {
        ReviewsVO vo = reviewsService.getByIdBiz(id);
        return ResultBo.success(vo);
    }

    @PostMapping("page")
    public ResultBo<Page<ReviewsVO>> page(@RequestBody ReviewsQueryReq queryReq) {
        Page page = reviewsService.pageBiz(queryReq);
        return ResultBo.success(page);
    }
}


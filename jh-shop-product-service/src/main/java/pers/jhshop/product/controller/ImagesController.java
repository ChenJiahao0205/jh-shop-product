package pers.jhshop.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.common.entity.ResultBo;
import pers.jhshop.product.consts.JhShopUserApiConstants;
import pers.jhshop.product.model.req.ImagesCreateReq;
import pers.jhshop.product.model.req.ImagesQueryReq;
import pers.jhshop.product.model.req.ImagesUpdateReq;
import pers.jhshop.product.model.vo.ImagesVO;
import pers.jhshop.product.service.IImagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
/**
 * <p>
 * 商品图片表 前端控制器
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Slf4j
@RestController
@RequestMapping(JhShopUserApiConstants.API_USER + "images")
@RequiredArgsConstructor
public class ImagesController {
    private final IImagesService imagesService;

    @PostMapping("create")
    public ResultBo create(@RequestBody ImagesCreateReq createReq) {
        imagesService.createBiz(createReq);
        return ResultBo.success();
    }

    @PostMapping("update")
    public ResultBo update(@RequestBody ImagesUpdateReq updateReq) {
        imagesService.updateBiz(updateReq);
        return ResultBo.success();
    }

    @GetMapping("getById")
    public ResultBo<ImagesVO> getById(Long id) {
        ImagesVO vo = imagesService.getByIdBiz(id);
        return ResultBo.success(vo);
    }

    @PostMapping("page")
    public ResultBo<Page<ImagesVO>> page(@RequestBody ImagesQueryReq queryReq) {
        Page page = imagesService.pageBiz(queryReq);
        return ResultBo.success(page);
    }
}


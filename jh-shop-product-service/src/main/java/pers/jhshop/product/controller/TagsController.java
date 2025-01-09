package pers.jhshop.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.common.entity.ResultBo;
import pers.jhshop.product.consts.JhShopUserApiConstants;
import pers.jhshop.product.model.req.TagsCreateReq;
import pers.jhshop.product.model.req.TagsQueryReq;
import pers.jhshop.product.model.req.TagsUpdateReq;
import pers.jhshop.product.model.vo.AllLabelIdAndNameAndSubVO;
import pers.jhshop.product.model.vo.TagsVO;
import pers.jhshop.product.service.ITagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
/**
 * <p>
 * 商品标签表 前端控制器
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Slf4j
@RestController
@RequestMapping(JhShopUserApiConstants.API_USER + "tags")
@RequiredArgsConstructor
public class TagsController {
    private final ITagsService tagsService;

    @PostMapping("create")
    public ResultBo create(@RequestBody TagsCreateReq createReq) {
        tagsService.createBiz(createReq);
        return ResultBo.success();
    }

    @PostMapping("update")
    public ResultBo update(@RequestBody TagsUpdateReq updateReq) {
        tagsService.updateBiz(updateReq);
        return ResultBo.success();
    }

    @GetMapping("getById")
    public ResultBo<TagsVO> getById(Long id) {
        TagsVO vo = tagsService.getByIdBiz(id);
        return ResultBo.success(vo);
    }

    @PostMapping("page")
    public ResultBo<Page<TagsVO>> page(@RequestBody TagsQueryReq queryReq) {
        Page page = tagsService.pageBiz(queryReq);
        return ResultBo.success(page);
    }

    @GetMapping("get-all-product-tags")
    public ResultBo<AllLabelIdAndNameAndSubVO> getAllProductTags(){
        return ResultBo.success(tagsService.getAllProductTags());
    }
}


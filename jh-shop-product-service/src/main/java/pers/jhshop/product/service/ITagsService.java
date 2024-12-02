package pers.jhshop.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.jhshop.product.model.entity.Tags;
import pers.jhshop.product.model.req.TagsCreateReq;
import pers.jhshop.product.model.req.TagsQueryReq;
import pers.jhshop.product.model.req.TagsUpdateReq;
import pers.jhshop.product.model.vo.TagsVO;
import java.util.Map;
import java.util.List;

/**
 * <p>
 * 商品标签表 服务类
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
public interface ITagsService extends IService<Tags> {

    void createBiz(TagsCreateReq createReq);

    void updateBiz(TagsUpdateReq updateReq);

    TagsVO getByIdBiz(Long id);

    Page<TagsVO> pageBiz(TagsQueryReq queryReq);

    Page<Tags> page(TagsQueryReq queryReq);

    List<Tags> listByQueryReq(TagsQueryReq queryReq);

    Map<Long, Tags> getIdEntityMap(List<Long> ids);

    Tags getOneByQueryReq(TagsQueryReq queryReq);

}

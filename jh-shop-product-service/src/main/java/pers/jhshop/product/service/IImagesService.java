package pers.jhshop.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.jhshop.product.model.entity.Images;
import pers.jhshop.product.model.req.ImagesCreateReq;
import pers.jhshop.product.model.req.ImagesQueryReq;
import pers.jhshop.product.model.req.ImagesUpdateReq;
import pers.jhshop.product.model.vo.ImagesVO;
import java.util.Map;
import java.util.List;

/**
 * <p>
 * 商品图片表 服务类
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
public interface IImagesService extends IService<Images> {

    void createBiz(ImagesCreateReq createReq);

    void updateBiz(ImagesUpdateReq updateReq);

    ImagesVO getByIdBiz(Long id);

    Page<ImagesVO> pageBiz(ImagesQueryReq queryReq);

    Page<Images> page(ImagesQueryReq queryReq);

    List<Images> listByQueryReq(ImagesQueryReq queryReq);

    Map<Long, Images> getIdEntityMap(List<Long> ids);

    Images getOneByQueryReq(ImagesQueryReq queryReq);

}

package pers.jhshop.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.jhshop.product.model.entity.Promotions;
import pers.jhshop.product.model.req.PromotionsCreateReq;
import pers.jhshop.product.model.req.PromotionsQueryReq;
import pers.jhshop.product.model.req.PromotionsUpdateReq;
import pers.jhshop.product.model.vo.PromotionsVO;
import java.util.Map;
import java.util.List;

/**
 * <p>
 * 商品促销表 服务类
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
public interface IPromotionsService extends IService<Promotions> {

    void createBiz(PromotionsCreateReq createReq);

    void updateBiz(PromotionsUpdateReq updateReq);

    PromotionsVO getByIdBiz(Long id);

    Page<PromotionsVO> pageBiz(PromotionsQueryReq queryReq);

    Page<Promotions> page(PromotionsQueryReq queryReq);

    List<Promotions> listByQueryReq(PromotionsQueryReq queryReq);

    Map<Long, Promotions> getIdEntityMap(List<Long> ids);

    Promotions getOneByQueryReq(PromotionsQueryReq queryReq);

}

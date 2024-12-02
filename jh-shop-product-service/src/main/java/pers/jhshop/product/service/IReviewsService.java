package pers.jhshop.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.jhshop.product.model.entity.Reviews;
import pers.jhshop.product.model.req.ReviewsCreateReq;
import pers.jhshop.product.model.req.ReviewsQueryReq;
import pers.jhshop.product.model.req.ReviewsUpdateReq;
import pers.jhshop.product.model.vo.ReviewsVO;
import java.util.Map;
import java.util.List;

/**
 * <p>
 * 商品评价表 服务类
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
public interface IReviewsService extends IService<Reviews> {

    void createBiz(ReviewsCreateReq createReq);

    void updateBiz(ReviewsUpdateReq updateReq);

    ReviewsVO getByIdBiz(Long id);

    Page<ReviewsVO> pageBiz(ReviewsQueryReq queryReq);

    Page<Reviews> page(ReviewsQueryReq queryReq);

    List<Reviews> listByQueryReq(ReviewsQueryReq queryReq);

    Map<Long, Reviews> getIdEntityMap(List<Long> ids);

    Reviews getOneByQueryReq(ReviewsQueryReq queryReq);

}

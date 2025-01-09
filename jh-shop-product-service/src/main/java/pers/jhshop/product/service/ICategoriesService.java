package pers.jhshop.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.jhshop.product.model.entity.Categories;
import pers.jhshop.product.model.req.CategoriesCreateReq;
import pers.jhshop.product.model.req.CategoriesQueryReq;
import pers.jhshop.product.model.req.CategoriesUpdateReq;
import pers.jhshop.product.model.vo.AllLabelIdAndNameAndSubVO;
import pers.jhshop.product.model.vo.CategoriesVO;
import java.util.Map;
import java.util.List;

/**
 * <p>
 * 商品分类表 服务类
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
public interface ICategoriesService extends IService<Categories> {

    void createBiz(CategoriesCreateReq createReq);

    void updateBiz(CategoriesUpdateReq updateReq);

    CategoriesVO getByIdBiz(Long id);

    Page<CategoriesVO> pageBiz(CategoriesQueryReq queryReq);

    Page<Categories> page(CategoriesQueryReq queryReq);

    List<Categories> listByQueryReq(CategoriesQueryReq queryReq);

    Map<Long, Categories> getIdEntityMap(List<Long> ids);

    Categories getOneByQueryReq(CategoriesQueryReq queryReq);

    /**
     * 查询所有商品标签
     */
    AllLabelIdAndNameAndSubVO getAllProductCategories();
}

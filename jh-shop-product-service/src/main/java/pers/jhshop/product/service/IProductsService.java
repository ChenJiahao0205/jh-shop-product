package pers.jhshop.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.jhshop.product.model.entity.Products;
import pers.jhshop.product.model.req.CombineCreateProductInfoReq;
import pers.jhshop.product.model.req.ProductsCreateReq;
import pers.jhshop.product.model.req.ProductsQueryReq;
import pers.jhshop.product.model.req.ProductsUpdateReq;
import pers.jhshop.product.model.vo.ProductsVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
public interface IProductsService extends IService<Products> {

    void createBiz(ProductsCreateReq createReq);

    void updateBiz(ProductsUpdateReq updateReq);

    ProductsVO getByIdBiz(Long id);

    Page<ProductsVO> pageBiz(ProductsQueryReq queryReq);

    Page<Products> page(ProductsQueryReq queryReq);

    List<Products> listByQueryReq(ProductsQueryReq queryReq);

    Map<Long, Products> getIdEntityMap(List<Long> ids);

    Products getOneByQueryReq(ProductsQueryReq queryReq);

    /**
     * 商品相关信息联合创建Req
     */
    void combineCreateProductInfo(CombineCreateProductInfoReq combineCreateReq);
}

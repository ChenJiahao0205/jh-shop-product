package pers.jhshop.product.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品相关信息联合创建Req
 * @author ChenJiahao(五条)
 * @date 2025/01/07 21:59:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CombineCreateProductInfoReq", description = "商品相关信息联合创建Req")
public class CombineCreateProductInfoReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品描述")
    private String productDescription;

    @ApiModelProperty(value = "商品品牌")
    private String productBrand;

    @ApiModelProperty(value = "商品分类ID集合")
    private List<Long> productCategoryIdList;

    @ApiModelProperty(value = "商品标签ID集合")
    private List<Long> productTagIdList;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal productPrice;

    @ApiModelProperty(value = "商品库存")
    private Integer productStock;

    @ApiModelProperty(value = "商品主图URL")
    private String productImageUrl;
}


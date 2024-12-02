package pers.jhshop.product.model.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pers.jhshop.product.model.entity.Products;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品表查询Req
 * </p>
 *
 * @author ChenJiahao(wutiao)
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProductsQueryReq", description = "商品表查询Req")
public class ProductsQueryReq extends Page<Products> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品唯一标识")
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品名称-模糊匹配")
    private String nameLike;

    @ApiModelProperty(value = "商品描述")
    private String productDescription;

    @ApiModelProperty(value = "商品描述-模糊匹配")
    private String productDescriptionLike;

    @ApiModelProperty(value = "商品品牌")
    private String brand;

    @ApiModelProperty(value = "商品品牌-模糊匹配")
    private String brandLike;

    @ApiModelProperty(value = "商品分类ID，外键关联CATEGORIES表")
    private Integer categoryId;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "商品库存")
    private Integer stock;

    @ApiModelProperty(value = "商品状态（1=在售，0=下架）")
    private Boolean status;

    @ApiModelProperty(value = "商品创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "商品信息最后更新时间")
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "商品主图URL")
    private String imageUrl;

    @ApiModelProperty(value = "商品主图URL-模糊匹配")
    private String imageUrlLike;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "描述-模糊匹配")
    private String descriptionLike;

    @ApiModelProperty(value = "生效标志(TRUE-生效, FALSE-失效)")
    private Boolean validFlag;



}
